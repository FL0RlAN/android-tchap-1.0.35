package org.matrix.androidsdk.data;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.pid.ThreePid;

public class MyUser extends User {
    private static final String LOG_TAG = MyUser.class.getSimpleName();
    /* access modifiers changed from: private */
    public boolean mAre3PIdsLoaded = false;
    private transient List<ThirdPartyIdentifier> mEmailIdentifiers = new ArrayList();
    /* access modifiers changed from: private */
    public boolean mIsAvatarRefreshed = false;
    /* access modifiers changed from: private */
    public boolean mIsDisplayNameRefreshed = false;
    private transient List<ThirdPartyIdentifier> mPhoneNumberIdentifiers = new ArrayList();
    private transient List<ApiCallback<Void>> mRefreshListeners;
    /* access modifiers changed from: private */
    public final transient Handler mUiHandler;

    public MyUser(User user) {
        clone(user);
        this.mUiHandler = new Handler(Looper.getMainLooper());
    }

    public void updateDisplayName(final String str, final ApiCallback<Void> apiCallback) {
        this.mDataHandler.getProfileRestClient().updateDisplayname(this.user_id, str, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                MyUser myUser = MyUser.this;
                myUser.displayname = str;
                myUser.mDataHandler.getStore().setDisplayName(str, System.currentTimeMillis());
                apiCallback.onSuccess(voidR);
            }
        });
    }

    public void updateAvatarUrl(final String str, final ApiCallback<Void> apiCallback) {
        this.mDataHandler.getProfileRestClient().updateAvatarUrl(this.user_id, str, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                MyUser.this.setAvatarUrl(str);
                MyUser.this.mDataHandler.getStore().setAvatarURL(str, System.currentTimeMillis());
                apiCallback.onSuccess(voidR);
            }
        });
    }

    public void requestEmailValidationToken(ThreePid threePid, ApiCallback<Void> apiCallback) {
        if (threePid != null) {
            threePid.requestEmailValidationToken(this.mDataHandler.getProfileRestClient(), null, false, apiCallback);
        }
    }

    public void requestPhoneNumberValidationToken(ThreePid threePid, ApiCallback<Void> apiCallback) {
        if (threePid != null) {
            threePid.requestPhoneNumberValidationToken(this.mDataHandler.getProfileRestClient(), false, apiCallback);
        }
    }

    public void add3Pid(ThreePid threePid, boolean z, final ApiCallback<Void> apiCallback) {
        if (threePid != null) {
            this.mDataHandler.getProfileRestClient().add3PID(threePid, z, new SimpleApiCallback<Void>(apiCallback) {
                public void onSuccess(Void voidR) {
                    MyUser.this.refreshThirdPartyIdentifiers(apiCallback);
                }
            });
        }
    }

    public void delete3Pid(ThirdPartyIdentifier thirdPartyIdentifier, final ApiCallback<Void> apiCallback) {
        if (thirdPartyIdentifier != null) {
            this.mDataHandler.getProfileRestClient().delete3PID(thirdPartyIdentifier, new SimpleApiCallback<Void>(apiCallback) {
                public void onSuccess(Void voidR) {
                    MyUser.this.refreshThirdPartyIdentifiers(apiCallback);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void buildIdentifiersLists() {
        List<ThirdPartyIdentifier> thirdPartyIdentifiers = this.mDataHandler.getStore().thirdPartyIdentifiers();
        this.mEmailIdentifiers = new ArrayList();
        this.mPhoneNumberIdentifiers = new ArrayList();
        for (ThirdPartyIdentifier thirdPartyIdentifier : thirdPartyIdentifiers) {
            String str = thirdPartyIdentifier.medium;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -1064943142) {
                if (hashCode == 96619420 && str.equals("email")) {
                    c = 0;
                }
            } else if (str.equals(ThreePid.MEDIUM_MSISDN)) {
                c = 1;
            }
            if (c == 0) {
                this.mEmailIdentifiers.add(thirdPartyIdentifier);
            } else if (c == 1) {
                this.mPhoneNumberIdentifiers.add(thirdPartyIdentifier);
            }
        }
    }

    public List<ThirdPartyIdentifier> getlinkedEmails() {
        if (this.mEmailIdentifiers == null) {
            buildIdentifiersLists();
        }
        return this.mEmailIdentifiers;
    }

    public List<ThirdPartyIdentifier> getlinkedPhoneNumbers() {
        if (this.mPhoneNumberIdentifiers == null) {
            buildIdentifiersLists();
        }
        return this.mPhoneNumberIdentifiers;
    }

    public void refreshUserInfos(ApiCallback<Void> apiCallback) {
        refreshUserInfos(false, apiCallback);
    }

    public void refreshThirdPartyIdentifiers(ApiCallback<Void> apiCallback) {
        this.mAre3PIdsLoaded = false;
        refreshUserInfos(false, apiCallback);
    }

    public void refreshUserInfos(boolean z, ApiCallback<Void> apiCallback) {
        boolean z2;
        if (!z) {
            synchronized (this) {
                z2 = this.mRefreshListeners != null;
                if (this.mRefreshListeners == null) {
                    this.mRefreshListeners = new ArrayList();
                }
                if (apiCallback != null) {
                    this.mRefreshListeners.add(apiCallback);
                }
            }
            if (z2) {
                return;
            }
        }
        if (!this.mIsDisplayNameRefreshed) {
            refreshUserDisplayname();
        } else if (!this.mIsAvatarRefreshed) {
            refreshUserAvatarUrl();
        } else if (!this.mAre3PIdsLoaded) {
            refreshThirdPartyIdentifiers();
        } else {
            synchronized (this) {
                if (this.mRefreshListeners != null) {
                    for (ApiCallback onSuccess : this.mRefreshListeners) {
                        try {
                            onSuccess.onSuccess(null);
                        } catch (Exception e) {
                            String str = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshUserInfos() : listener.onSuccess failed ");
                            sb.append(e.getMessage());
                            Log.e(str, sb.toString(), e);
                        }
                    }
                }
                this.mRefreshListeners = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshUserAvatarUrl() {
        this.mDataHandler.getProfileRestClient().avatarUrl(this.user_id, new SimpleApiCallback<String>() {
            public void onSuccess(String str) {
                if (MyUser.this.mDataHandler.isAlive()) {
                    MyUser.this.setAvatarUrl(str);
                    MyUser.this.mDataHandler.getStore().setAvatarURL(str, System.currentTimeMillis());
                    MyUser.this.mDataHandler.getStore().storeUser(MyUser.this);
                    MyUser.this.mIsAvatarRefreshed = true;
                    MyUser.this.refreshUserInfos(true, null);
                }
            }

            private void onError() {
                if (MyUser.this.mDataHandler.isAlive()) {
                    MyUser.this.mUiHandler.postDelayed(new Runnable() {
                        public void run() {
                            MyUser.this.refreshUserAvatarUrl();
                        }
                    }, 1000);
                }
            }

            public void onNetworkError(Exception exc) {
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                MyUser.this.mIsAvatarRefreshed = true;
                MyUser.this.refreshUserInfos(true, null);
            }

            public void onUnexpectedError(Exception exc) {
                MyUser.this.mIsAvatarRefreshed = true;
                MyUser.this.refreshUserInfos(true, null);
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshUserDisplayname() {
        this.mDataHandler.getProfileRestClient().displayname(this.user_id, new SimpleApiCallback<String>() {
            public void onSuccess(String str) {
                if (MyUser.this.mDataHandler.isAlive()) {
                    MyUser myUser = MyUser.this;
                    myUser.displayname = str;
                    myUser.mDataHandler.getStore().setDisplayName(str, System.currentTimeMillis());
                    MyUser.this.mIsDisplayNameRefreshed = true;
                    MyUser.this.refreshUserInfos(true, null);
                }
            }

            private void onError() {
                if (MyUser.this.mDataHandler.isAlive()) {
                    MyUser.this.mUiHandler.postDelayed(new Runnable() {
                        public void run() {
                            MyUser.this.refreshUserDisplayname();
                        }
                    }, 1000);
                }
            }

            public void onNetworkError(Exception exc) {
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                MyUser.this.mIsDisplayNameRefreshed = true;
                MyUser.this.refreshUserInfos(true, null);
            }

            public void onUnexpectedError(Exception exc) {
                MyUser.this.mIsDisplayNameRefreshed = true;
                MyUser.this.refreshUserInfos(true, null);
            }
        });
    }

    public void refreshThirdPartyIdentifiers() {
        this.mDataHandler.getProfileRestClient().threePIDs(new SimpleApiCallback<List<ThirdPartyIdentifier>>() {
            public void onSuccess(List<ThirdPartyIdentifier> list) {
                if (MyUser.this.mDataHandler.isAlive()) {
                    MyUser.this.mDataHandler.getStore().setThirdPartyIdentifiers(list);
                    MyUser.this.buildIdentifiersLists();
                    MyUser.this.mAre3PIdsLoaded = true;
                    MyUser.this.refreshUserInfos(true, null);
                }
            }

            private void onError() {
                if (MyUser.this.mDataHandler.isAlive()) {
                    MyUser.this.mUiHandler.postDelayed(new Runnable() {
                        public void run() {
                            MyUser.this.refreshThirdPartyIdentifiers();
                        }
                    }, 1000);
                }
            }

            public void onNetworkError(Exception exc) {
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                MyUser.this.mAre3PIdsLoaded = true;
                MyUser.this.refreshUserInfos(true, null);
            }

            public void onUnexpectedError(Exception exc) {
                MyUser.this.mAre3PIdsLoaded = true;
                MyUser.this.refreshUserInfos(true, null);
            }
        });
    }
}
