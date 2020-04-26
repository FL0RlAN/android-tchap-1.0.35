package im.vector.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.activity.CountryPickerActivity;
import im.vector.activity.NotificationPrivacyActivity;
import im.vector.contacts.ContactsManager;
import im.vector.preference.BingRulePreference;
import im.vector.preference.ProgressBarPreference;
import im.vector.preference.UserAvatarPreference;
import im.vector.preference.VectorCustomActionEditTextPreference;
import im.vector.push.PushManager;
import im.vector.settings.FontScale;
import im.vector.settings.VectorLocale;
import im.vector.util.ExternalApplicationsUtilKt;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.PhoneNumberUtils;
import im.vector.util.PreferencesManager;
import im.vector.util.VectorUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.model.rest.DeviceInfo;
import org.matrix.androidsdk.data.Pusher;
import org.matrix.androidsdk.data.RoomMediaMessage;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.client.AccountDataRestClient;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.bingrules.PushRuleSet;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.pid.ThreePid;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;
import org.matrix.androidsdk.rest.model.sync.DeviceInfoUtil;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Û\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010#\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0017*\u0001I\u0018\u0000 Ì\u00012\u00020\u00012\u00020\u0002:\u0002Ì\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0016\u0010}\u001a\u00020~2\f\u0010\u001a\b\u0012\u0004\u0012\u00020?0>H\u0002J\u0012\u0010\u0001\u001a\u00020~2\u0007\u0010\u0001\u001a\u00020(H\u0002J\u001d\u0010\u0001\u001a\u00020~2\b\u0010\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0002J\u0014\u0010\u0001\u001a\u00020~2\t\u0010\u0001\u001a\u0004\u0018\u00010?H\u0002J\u0014\u0010\u0001\u001a\u00020~2\t\u0010\u0001\u001a\u0004\u0018\u00010?H\u0002J\u0012\u0010\u0001\u001a\u00020~2\u0007\u0010\u0001\u001a\u00020?H\u0002J\t\u0010\u0001\u001a\u00020~H\u0002J\u0013\u0010\u0001\u001a\u00020~2\b\u0010\u0001\u001a\u00030\u0001H\u0002J\t\u0010\u0001\u001a\u00020~H\u0002J\u0007\u0010\u0001\u001a\u00020~J\f\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0002J\t\u0010\u0001\u001a\u00020~H\u0002J\u0013\u0010\u0001\u001a\u00020~2\b\u0010\u0001\u001a\u00030\u0001H\u0002J\u0013\u0010\u0001\u001a\u00020~2\b\u0010\u0001\u001a\u00030\u0001H\u0002J\t\u0010\u0001\u001a\u00020~H\u0003J\u0015\u0010\u0001\u001a\u00020~2\n\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0002J\n\u0010\u0001\u001a\u00030\u0001H\u0002J)\u0010\u0001\u001a\u00020~2\b\u0010\u0001\u001a\u00030 \u00012\b\u0010¡\u0001\u001a\u00030 \u00012\n\u0010¢\u0001\u001a\u0005\u0018\u00010\u0001H\u0016J\u0014\u0010£\u0001\u001a\u00020~2\t\u0010¤\u0001\u001a\u0004\u0018\u00010(H\u0002J\u0015\u0010¥\u0001\u001a\u00020~2\n\u0010¦\u0001\u001a\u0005\u0018\u00010§\u0001H\u0016J.\u0010¨\u0001\u001a\u0005\u0018\u00010\u00012\b\u0010©\u0001\u001a\u00030ª\u00012\n\u0010«\u0001\u001a\u0005\u0018\u00010¬\u00012\n\u0010¦\u0001\u001a\u0005\u0018\u00010§\u0001H\u0016J\t\u0010­\u0001\u001a\u00020~H\u0002J\t\u0010®\u0001\u001a\u00020~H\u0016J\u0015\u0010¯\u0001\u001a\u00020~2\n\u0010¢\u0001\u001a\u0005\u0018\u00010\u0001H\u0002J\u001c\u0010°\u0001\u001a\u00020~2\u0007\u0010±\u0001\u001a\u00020(2\b\u0010²\u0001\u001a\u00030\u0001H\u0002J\t\u0010³\u0001\u001a\u00020~H\u0016J\u001c\u0010´\u0001\u001a\u00020~2\b\u0010µ\u0001\u001a\u00030¶\u00012\u0007\u0010·\u0001\u001a\u00020(H\u0016J\t\u0010¸\u0001\u001a\u00020~H\u0002J\t\u0010¹\u0001\u001a\u00020~H\u0002J\u0014\u0010º\u0001\u001a\u00020~2\t\u0010»\u0001\u001a\u0004\u0018\u00010?H\u0002J\t\u0010¼\u0001\u001a\u00020~H\u0002J\t\u0010½\u0001\u001a\u00020~H\u0002J\t\u0010¾\u0001\u001a\u00020~H\u0002J\t\u0010¿\u0001\u001a\u00020~H\u0002J\t\u0010À\u0001\u001a\u00020~H\u0002J\t\u0010Á\u0001\u001a\u00020~H\u0002J\t\u0010Â\u0001\u001a\u00020~H\u0002J\t\u0010Ã\u0001\u001a\u00020~H\u0002J\t\u0010Ä\u0001\u001a\u00020~H\u0002J\t\u0010Å\u0001\u001a\u00020~H\u0002J\t\u0010Æ\u0001\u001a\u00020~H\u0002J\t\u0010Ç\u0001\u001a\u00020~H\u0002J\u0013\u0010È\u0001\u001a\u00020(2\b\u0010É\u0001\u001a\u00030 \u0001H\u0002J\t\u0010Ê\u0001\u001a\u00020~H\u0002J\t\u0010Ë\u0001\u001a\u00020~H\u0002R#\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058BX\u0002¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR#\u0010\u000b\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058BX\u0002¢\u0006\f\n\u0004\b\r\u0010\n\u001a\u0004\b\f\u0010\bR\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0002¢\u0006\f\n\u0004\b\u0012\u0010\n\u001a\u0004\b\u0010\u0010\u0011R\u001b\u0010\u0013\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b\u0017\u0010\n\u001a\u0004\b\u0015\u0010\u0016R\u001b\u0010\u0018\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b\u001a\u0010\n\u001a\u0004\b\u0019\u0010\u0016R\u001b\u0010\u001b\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b\u001d\u0010\n\u001a\u0004\b\u001c\u0010\u0016R\u001b\u0010\u001e\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b \u0010\n\u001a\u0004\b\u001f\u0010\u0016R\u001b\u0010!\u001a\u00020\u000f8BX\u0002¢\u0006\f\n\u0004\b#\u0010\n\u001a\u0004\b\"\u0010\u0011R\u001b\u0010$\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b&\u0010\n\u001a\u0004\b%\u0010\u0016R\u0010\u0010'\u001a\u0004\u0018\u00010(X\u000e¢\u0006\u0002\n\u0000R\u001b\u0010)\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b+\u0010\n\u001a\u0004\b*\u0010\u0016R\u001b\u0010,\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\b0\u0010\n\u001a\u0004\b.\u0010/R\u001b\u00101\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\b3\u0010\n\u001a\u0004\b2\u0010/R\u001b\u00104\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\b6\u0010\n\u001a\u0004\b5\u0010/R\u001b\u00107\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\b9\u0010\n\u001a\u0004\b8\u0010/R\u001b\u0010:\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\b<\u0010\n\u001a\u0004\b;\u0010/R\u0014\u0010=\u001a\b\u0012\u0004\u0012\u00020?0>X\u000e¢\u0006\u0002\n\u0000R#\u0010@\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058BX\u0002¢\u0006\f\n\u0004\bB\u0010\n\u001a\u0004\bA\u0010\bR\u0014\u0010C\u001a\b\u0012\u0004\u0012\u00020(0DX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010E\u001a\b\u0012\u0004\u0012\u00020(0DX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010F\u001a\b\u0012\u0004\u0012\u00020G0DX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010H\u001a\u00020IX\u0004¢\u0006\u0004\n\u0002\u0010JR\u001b\u0010K\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\bM\u0010\n\u001a\u0004\bL\u0010/R\u001b\u0010N\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\bP\u0010\n\u001a\u0004\bO\u0010/R\u0010\u0010Q\u001a\u0004\u0018\u00010?X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020SX\u0004¢\u0006\u0002\n\u0000R#\u0010T\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058BX\u0002¢\u0006\f\n\u0004\bV\u0010\n\u001a\u0004\bU\u0010\bR#\u0010W\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058BX\u0002¢\u0006\f\n\u0004\bY\u0010\n\u001a\u0004\bX\u0010\bR\u0016\u0010Z\u001a\n\u0012\u0004\u0012\u00020(\u0018\u00010[X\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\\\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\b^\u0010\n\u001a\u0004\b]\u0010/R\u001b\u0010_\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\ba\u0010\n\u001a\u0004\b`\u0010/R#\u0010b\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00058BX\u0002¢\u0006\f\n\u0004\bd\u0010\n\u001a\u0004\bc\u0010\bR\u000e\u0010e\u001a\u00020fX.¢\u0006\u0002\n\u0000R\u001d\u0010g\u001a\u0004\u0018\u00010h8BX\u0002¢\u0006\f\n\u0004\bk\u0010\n\u001a\u0004\bi\u0010jR\u001b\u0010l\u001a\u00020m8BX\u0002¢\u0006\f\n\u0004\bp\u0010\n\u001a\u0004\bn\u0010oR\u001b\u0010q\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\bs\u0010\n\u001a\u0004\br\u0010/R\u001b\u0010t\u001a\u00020-8BX\u0002¢\u0006\f\n\u0004\bv\u0010\n\u001a\u0004\bu\u0010/R\u001b\u0010w\u001a\u00020\u000f8BX\u0002¢\u0006\f\n\u0004\by\u0010\n\u001a\u0004\bx\u0010\u0011R\u001b\u0010z\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b|\u0010\n\u001a\u0004\b{\u0010\u0016¨\u0006Í\u0001"}, d2 = {"Lim/vector/fragments/VectorSettingsPreferencesFragment;", "Landroid/preference/PreferenceFragment;", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "()V", "backgroundSyncCategory", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "getBackgroundSyncCategory", "()Landroid/preference/Preference;", "backgroundSyncCategory$delegate", "Lkotlin/Lazy;", "backgroundSyncDivider", "getBackgroundSyncDivider", "backgroundSyncDivider$delegate", "backgroundSyncPreference", "Landroid/preference/CheckBoxPreference;", "getBackgroundSyncPreference", "()Landroid/preference/CheckBoxPreference;", "backgroundSyncPreference$delegate", "cryptoInfoDeviceIdPreference", "Lim/vector/preference/VectorCustomActionEditTextPreference;", "getCryptoInfoDeviceIdPreference", "()Lim/vector/preference/VectorCustomActionEditTextPreference;", "cryptoInfoDeviceIdPreference$delegate", "cryptoInfoDeviceNamePreference", "getCryptoInfoDeviceNamePreference", "cryptoInfoDeviceNamePreference$delegate", "cryptoInfoTextPreference", "getCryptoInfoTextPreference", "cryptoInfoTextPreference$delegate", "exportPref", "getExportPref", "exportPref$delegate", "hideFromUsersDirectoryPreference", "getHideFromUsersDirectoryPreference", "hideFromUsersDirectoryPreference$delegate", "importPref", "getImportPref", "importPref$delegate", "mAccountPassword", "", "mContactPhonebookCountryPreference", "getMContactPhonebookCountryPreference", "mContactPhonebookCountryPreference$delegate", "mContactSettingsCategory", "Landroid/preference/PreferenceCategory;", "getMContactSettingsCategory", "()Landroid/preference/PreferenceCategory;", "mContactSettingsCategory$delegate", "mCryptographyCategory", "getMCryptographyCategory", "mCryptographyCategory$delegate", "mCryptographyCategoryDivider", "getMCryptographyCategoryDivider", "mCryptographyCategoryDivider$delegate", "mDevicesListSettingsCategory", "getMDevicesListSettingsCategory", "mDevicesListSettingsCategory$delegate", "mDevicesListSettingsCategoryDivider", "getMDevicesListSettingsCategoryDivider", "mDevicesListSettingsCategoryDivider$delegate", "mDevicesNameList", "", "Lorg/matrix/androidsdk/crypto/model/rest/DeviceInfo;", "mDisplayNamePreference", "getMDisplayNamePreference", "mDisplayNamePreference$delegate", "mDisplayedEmails", "Ljava/util/ArrayList;", "mDisplayedPhoneNumber", "mDisplayedPushers", "Lorg/matrix/androidsdk/data/Pusher;", "mEventsListener", "im/vector/fragments/VectorSettingsPreferencesFragment$mEventsListener$1", "Lim/vector/fragments/VectorSettingsPreferencesFragment$mEventsListener$1;", "mIgnoredUserSettingsCategory", "getMIgnoredUserSettingsCategory", "mIgnoredUserSettingsCategory$delegate", "mIgnoredUserSettingsCategoryDivider", "getMIgnoredUserSettingsCategoryDivider", "mIgnoredUserSettingsCategoryDivider$delegate", "mMyDeviceInfo", "mNetworkListener", "Lorg/matrix/androidsdk/core/listeners/IMXNetworkEventListener;", "mNotificationPrivacyPreference", "getMNotificationPrivacyPreference", "mNotificationPrivacyPreference$delegate", "mPasswordPreference", "getMPasswordPreference", "mPasswordPreference$delegate", "mPublicisedGroups", "", "mPushersSettingsCategory", "getMPushersSettingsCategory", "mPushersSettingsCategory$delegate", "mPushersSettingsDivider", "getMPushersSettingsDivider", "mPushersSettingsDivider$delegate", "mRingtonePreference", "getMRingtonePreference", "mRingtonePreference$delegate", "mSession", "Lorg/matrix/androidsdk/MXSession;", "mSyncRequestDelayPreference", "Landroid/preference/EditTextPreference;", "getMSyncRequestDelayPreference", "()Landroid/preference/EditTextPreference;", "mSyncRequestDelayPreference$delegate", "mUserAvatarPreference", "Lim/vector/preference/UserAvatarPreference;", "getMUserAvatarPreference", "()Lim/vector/preference/UserAvatarPreference;", "mUserAvatarPreference$delegate", "mUserSettingsCategory", "getMUserSettingsCategory", "mUserSettingsCategory$delegate", "notificationsSettingsCategory", "getNotificationsSettingsCategory", "notificationsSettingsCategory$delegate", "showJoinLeaveMessagesPreference", "getShowJoinLeaveMessagesPreference", "showJoinLeaveMessagesPreference$delegate", "textSizePreference", "getTextSizePreference", "textSizePreference$delegate", "buildDevicesSettings", "", "aDeviceInfoList", "deleteDevice", "deviceId", "displayDelete3PIDConfirmationDialog", "pid", "Lorg/matrix/androidsdk/rest/model/pid/ThirdPartyIdentifier;", "preferenceSummary", "", "displayDeviceDeletionDialog", "aDeviceInfoToDelete", "displayDeviceDetailsDialog", "aDeviceInfo", "displayDeviceRenameDialog", "aDeviceInfoToRename", "displayLoadingView", "displayTextSizeSelection", "activity", "Landroid/app/Activity;", "doShowPasswordChangeDialog", "exportKeys", "getLoadingView", "Landroid/view/View;", "hideLoadingView", "refresh", "", "hideUserFromUsersDirectory", "hidden", "importKeys", "intent", "Landroid/content/Intent;", "isUserAllowedToHideHimself", "onActivityResult", "requestCode", "", "resultCode", "data", "onCommonDone", "errorMessage", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onPasswordUpdateClick", "onPause", "onPhonebookCountryUpdate", "onPushRuleClick", "fResourceText", "newValue", "onResume", "onSharedPreferenceChanged", "sharedPreferences", "Landroid/content/SharedPreferences;", "key", "onUpdateAvatarClick", "refreshBackgroundSyncPrefs", "refreshCryptographyPreference", "aMyDeviceInfo", "refreshDevicesList", "refreshDisplay", "refreshEmailsList", "refreshIgnoredUsersList", "refreshNotificationPrivacy", "refreshNotificationRingTone", "refreshPhoneNumbersList", "refreshPreferences", "refreshPushersList", "refreshUsersDirectoryVisibility", "removeCryptographyPreference", "removeDevicesPreference", "secondsToText", "seconds", "setContactsPreferences", "setUserInterfacePreferences", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    static final /* synthetic */ KProperty[] $$delegatedProperties;
    private static final String ADD_PHONE_NUMBER_PREFERENCE_KEY = "ADD_PHONE_NUMBER_PREFERENCE_KEY";
    private static final String APP_INFO_LINK_PREFERENCE_KEY = "APP_INFO_LINK_PREFERENCE_KEY";
    private static final String ARG_MATRIX_ID = "VectorSettingsPreferencesFragment.ARG_MATRIX_ID";
    public static final Companion Companion = new Companion(null);
    private static final String DEVICES_PREFERENCE_KEY_BASE = "DEVICES_PREFERENCE_KEY_BASE";
    private static final String DUMMY_RULE = "DUMMY_RULE";
    private static final String EMAIL_PREFERENCE_KEY_BASE = "EMAIL_PREFERENCE_KEY_BASE";
    private static final String IGNORED_USER_KEY_BASE = "IGNORED_USER_KEY_BASE";
    private static final String LABEL_UNAVAILABLE_DATA = "none";
    /* access modifiers changed from: private */
    public static final String LOG_TAG;
    private static final String PHONE_NUMBER_PREFERENCE_KEY_BASE = "PHONE_NUMBER_PREFERENCE_KEY_BASE";
    private static final String PUSHER_PREFERENCE_KEY_BASE = "PUSHER_PREFERENCE_KEY_BASE";
    private static final int REQUEST_E2E_FILE_REQUEST_CODE = 123;
    private static final int REQUEST_LOCALE = 777;
    private static final int REQUEST_NEW_PHONE_NUMBER = 456;
    private static final int REQUEST_NOTIFICATION_RINGTONE = 888;
    private static final int REQUEST_PHONEBOOK_COUNTRY = 789;
    private static Map<String, String> mPushesRuleByResourceId;
    private HashMap _$_findViewCache;
    private final Lazy backgroundSyncCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$backgroundSyncCategory$2(this));
    private final Lazy backgroundSyncDivider$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$backgroundSyncDivider$2(this));
    private final Lazy backgroundSyncPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$backgroundSyncPreference$2(this));
    private final Lazy cryptoInfoDeviceIdPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$cryptoInfoDeviceIdPreference$2(this));
    private final Lazy cryptoInfoDeviceNamePreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$cryptoInfoDeviceNamePreference$2(this));
    private final Lazy cryptoInfoTextPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$cryptoInfoTextPreference$2(this));
    private final Lazy exportPref$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$exportPref$2(this));
    private final Lazy hideFromUsersDirectoryPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$hideFromUsersDirectoryPreference$2(this));
    private final Lazy importPref$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$importPref$2(this));
    /* access modifiers changed from: private */
    public String mAccountPassword;
    private final Lazy mContactPhonebookCountryPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mContactPhonebookCountryPreference$2(this));
    private final Lazy mContactSettingsCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mContactSettingsCategory$2(this));
    private final Lazy mCryptographyCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mCryptographyCategory$2(this));
    private final Lazy mCryptographyCategoryDivider$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mCryptographyCategoryDivider$2(this));
    private final Lazy mDevicesListSettingsCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mDevicesListSettingsCategory$2(this));
    private final Lazy mDevicesListSettingsCategoryDivider$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mDevicesListSettingsCategoryDivider$2(this));
    private List<? extends DeviceInfo> mDevicesNameList = new ArrayList();
    private final Lazy mDisplayNamePreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mDisplayNamePreference$2(this));
    private ArrayList<String> mDisplayedEmails = new ArrayList<>();
    private ArrayList<String> mDisplayedPhoneNumber = new ArrayList<>();
    private ArrayList<Pusher> mDisplayedPushers = new ArrayList<>();
    private final VectorSettingsPreferencesFragment$mEventsListener$1 mEventsListener = new VectorSettingsPreferencesFragment$mEventsListener$1(this);
    private final Lazy mIgnoredUserSettingsCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mIgnoredUserSettingsCategory$2(this));
    private final Lazy mIgnoredUserSettingsCategoryDivider$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mIgnoredUserSettingsCategoryDivider$2(this));
    private DeviceInfo mMyDeviceInfo;
    private final IMXNetworkEventListener mNetworkListener = new VectorSettingsPreferencesFragment$mNetworkListener$1(this);
    private final Lazy mNotificationPrivacyPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mNotificationPrivacyPreference$2(this));
    private final Lazy mPasswordPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mPasswordPreference$2(this));
    private Set<String> mPublicisedGroups;
    private final Lazy mPushersSettingsCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mPushersSettingsCategory$2(this));
    private final Lazy mPushersSettingsDivider$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mPushersSettingsDivider$2(this));
    private final Lazy mRingtonePreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mRingtonePreference$2(this));
    /* access modifiers changed from: private */
    public MXSession mSession;
    private final Lazy mSyncRequestDelayPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mSyncRequestDelayPreference$2(this));
    private final Lazy mUserAvatarPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mUserAvatarPreference$2(this));
    private final Lazy mUserSettingsCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$mUserSettingsCategory$2(this));
    private final Lazy notificationsSettingsCategory$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$notificationsSettingsCategory$2(this));
    private final Lazy showJoinLeaveMessagesPreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$showJoinLeaveMessagesPreference$2(this));
    private final Lazy textSizePreference$delegate = LazyKt.lazy(new VectorSettingsPreferencesFragment$textSizePreference$2(this));

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \r*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0017X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lim/vector/fragments/VectorSettingsPreferencesFragment$Companion;", "", "()V", "ADD_PHONE_NUMBER_PREFERENCE_KEY", "", "APP_INFO_LINK_PREFERENCE_KEY", "ARG_MATRIX_ID", "DEVICES_PREFERENCE_KEY_BASE", "DUMMY_RULE", "EMAIL_PREFERENCE_KEY_BASE", "IGNORED_USER_KEY_BASE", "LABEL_UNAVAILABLE_DATA", "LOG_TAG", "kotlin.jvm.PlatformType", "PHONE_NUMBER_PREFERENCE_KEY_BASE", "PUSHER_PREFERENCE_KEY_BASE", "REQUEST_E2E_FILE_REQUEST_CODE", "", "REQUEST_LOCALE", "REQUEST_NEW_PHONE_NUMBER", "REQUEST_NOTIFICATION_RINGTONE", "REQUEST_PHONEBOOK_COUNTRY", "mPushesRuleByResourceId", "", "newInstance", "Lim/vector/fragments/VectorSettingsPreferencesFragment;", "matrixId", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorSettingsPreferencesFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final VectorSettingsPreferencesFragment newInstance(String str) {
            Intrinsics.checkParameterIsNotNull(str, "matrixId");
            VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = new VectorSettingsPreferencesFragment();
            Bundle bundle = new Bundle();
            bundle.putString(VectorSettingsPreferencesFragment.ARG_MATRIX_ID, str);
            vectorSettingsPreferencesFragment.setArguments(bundle);
            return vectorSettingsPreferencesFragment;
        }
    }

    private final Preference getBackgroundSyncCategory() {
        Lazy lazy = this.backgroundSyncCategory$delegate;
        KProperty kProperty = $$delegatedProperties[15];
        return (Preference) lazy.getValue();
    }

    private final Preference getBackgroundSyncDivider() {
        Lazy lazy = this.backgroundSyncDivider$delegate;
        KProperty kProperty = $$delegatedProperties[16];
        return (Preference) lazy.getValue();
    }

    private final CheckBoxPreference getBackgroundSyncPreference() {
        Lazy lazy = this.backgroundSyncPreference$delegate;
        KProperty kProperty = $$delegatedProperties[17];
        return (CheckBoxPreference) lazy.getValue();
    }

    /* access modifiers changed from: private */
    public final VectorCustomActionEditTextPreference getCryptoInfoDeviceIdPreference() {
        Lazy lazy = this.cryptoInfoDeviceIdPreference$delegate;
        KProperty kProperty = $$delegatedProperties[23];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    /* access modifiers changed from: private */
    public final VectorCustomActionEditTextPreference getCryptoInfoDeviceNamePreference() {
        Lazy lazy = this.cryptoInfoDeviceNamePreference$delegate;
        KProperty kProperty = $$delegatedProperties[22];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    /* access modifiers changed from: private */
    public final VectorCustomActionEditTextPreference getCryptoInfoTextPreference() {
        Lazy lazy = this.cryptoInfoTextPreference$delegate;
        KProperty kProperty = $$delegatedProperties[26];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    private final VectorCustomActionEditTextPreference getExportPref() {
        Lazy lazy = this.exportPref$delegate;
        KProperty kProperty = $$delegatedProperties[24];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    /* access modifiers changed from: private */
    public final CheckBoxPreference getHideFromUsersDirectoryPreference() {
        Lazy lazy = this.hideFromUsersDirectoryPreference$delegate;
        KProperty kProperty = $$delegatedProperties[27];
        return (CheckBoxPreference) lazy.getValue();
    }

    private final VectorCustomActionEditTextPreference getImportPref() {
        Lazy lazy = this.importPref$delegate;
        KProperty kProperty = $$delegatedProperties[25];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    private final VectorCustomActionEditTextPreference getMContactPhonebookCountryPreference() {
        Lazy lazy = this.mContactPhonebookCountryPreference$delegate;
        KProperty kProperty = $$delegatedProperties[5];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    private final PreferenceCategory getMContactSettingsCategory() {
        Lazy lazy = this.mContactSettingsCategory$delegate;
        KProperty kProperty = $$delegatedProperties[4];
        return (PreferenceCategory) lazy.getValue();
    }

    private final PreferenceCategory getMCryptographyCategory() {
        Lazy lazy = this.mCryptographyCategory$delegate;
        KProperty kProperty = $$delegatedProperties[6];
        return (PreferenceCategory) lazy.getValue();
    }

    private final PreferenceCategory getMCryptographyCategoryDivider() {
        Lazy lazy = this.mCryptographyCategoryDivider$delegate;
        KProperty kProperty = $$delegatedProperties[7];
        return (PreferenceCategory) lazy.getValue();
    }

    /* access modifiers changed from: private */
    public final PreferenceCategory getMDevicesListSettingsCategory() {
        Lazy lazy = this.mDevicesListSettingsCategory$delegate;
        KProperty kProperty = $$delegatedProperties[10];
        return (PreferenceCategory) lazy.getValue();
    }

    private final PreferenceCategory getMDevicesListSettingsCategoryDivider() {
        Lazy lazy = this.mDevicesListSettingsCategoryDivider$delegate;
        KProperty kProperty = $$delegatedProperties[11];
        return (PreferenceCategory) lazy.getValue();
    }

    private final Preference getMDisplayNamePreference() {
        Lazy lazy = this.mDisplayNamePreference$delegate;
        KProperty kProperty = $$delegatedProperties[2];
        return (Preference) lazy.getValue();
    }

    private final PreferenceCategory getMIgnoredUserSettingsCategory() {
        Lazy lazy = this.mIgnoredUserSettingsCategory$delegate;
        KProperty kProperty = $$delegatedProperties[13];
        return (PreferenceCategory) lazy.getValue();
    }

    private final PreferenceCategory getMIgnoredUserSettingsCategoryDivider() {
        Lazy lazy = this.mIgnoredUserSettingsCategoryDivider$delegate;
        KProperty kProperty = $$delegatedProperties[12];
        return (PreferenceCategory) lazy.getValue();
    }

    private final Preference getMNotificationPrivacyPreference() {
        Lazy lazy = this.mNotificationPrivacyPreference$delegate;
        KProperty kProperty = $$delegatedProperties[20];
        return (Preference) lazy.getValue();
    }

    private final Preference getMPasswordPreference() {
        Lazy lazy = this.mPasswordPreference$delegate;
        KProperty kProperty = $$delegatedProperties[3];
        return (Preference) lazy.getValue();
    }

    private final PreferenceCategory getMPushersSettingsCategory() {
        Lazy lazy = this.mPushersSettingsCategory$delegate;
        KProperty kProperty = $$delegatedProperties[9];
        return (PreferenceCategory) lazy.getValue();
    }

    private final PreferenceCategory getMPushersSettingsDivider() {
        Lazy lazy = this.mPushersSettingsDivider$delegate;
        KProperty kProperty = $$delegatedProperties[8];
        return (PreferenceCategory) lazy.getValue();
    }

    private final Preference getMRingtonePreference() {
        Lazy lazy = this.mRingtonePreference$delegate;
        KProperty kProperty = $$delegatedProperties[18];
        return (Preference) lazy.getValue();
    }

    private final EditTextPreference getMSyncRequestDelayPreference() {
        Lazy lazy = this.mSyncRequestDelayPreference$delegate;
        KProperty kProperty = $$delegatedProperties[14];
        return (EditTextPreference) lazy.getValue();
    }

    private final UserAvatarPreference getMUserAvatarPreference() {
        Lazy lazy = this.mUserAvatarPreference$delegate;
        KProperty kProperty = $$delegatedProperties[1];
        return (UserAvatarPreference) lazy.getValue();
    }

    private final PreferenceCategory getMUserSettingsCategory() {
        Lazy lazy = this.mUserSettingsCategory$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (PreferenceCategory) lazy.getValue();
    }

    private final PreferenceCategory getNotificationsSettingsCategory() {
        Lazy lazy = this.notificationsSettingsCategory$delegate;
        KProperty kProperty = $$delegatedProperties[19];
        return (PreferenceCategory) lazy.getValue();
    }

    /* access modifiers changed from: private */
    public final CheckBoxPreference getShowJoinLeaveMessagesPreference() {
        Lazy lazy = this.showJoinLeaveMessagesPreference$delegate;
        KProperty kProperty = $$delegatedProperties[28];
        return (CheckBoxPreference) lazy.getValue();
    }

    private final VectorCustomActionEditTextPreference getTextSizePreference() {
        Lazy lazy = this.textSizePreference$delegate;
        KProperty kProperty = $$delegatedProperties[21];
        return (VectorCustomActionEditTextPreference) lazy.getValue();
    }

    private final boolean isUserAllowedToHideHimself() {
        return true;
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view == null) {
            View view2 = getView();
            if (view2 == null) {
                return null;
            }
            view = view2.findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), view);
        }
        return view;
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public static final /* synthetic */ MXSession access$getMSession$p(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        MXSession mXSession = vectorSettingsPreferencesFragment.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        return mXSession;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Activity activity = getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Context applicationContext = activity.getApplicationContext();
        MXSession session = Matrix.getInstance(applicationContext).getSession(getArguments().getString(ARG_MATRIX_ID));
        if (session == null || !session.isAlive()) {
            getActivity().finish();
            return;
        }
        this.mSession = session;
        addPreferencesFromResource(R.xml.vector_settings_preferences);
        UserAvatarPreference mUserAvatarPreference = getMUserAvatarPreference();
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        mUserAvatarPreference.setSession(mXSession);
        mUserAvatarPreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$1(this));
        Preference mDisplayNamePreference = getMDisplayNamePreference();
        String str2 = "it";
        Intrinsics.checkExpressionValueIsNotNull(mDisplayNamePreference, str2);
        MXSession mXSession2 = this.mSession;
        if (mXSession2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        mDisplayNamePreference.setSummary(mXSession2.getMyUser().displayname);
        Preference mPasswordPreference = getMPasswordPreference();
        Intrinsics.checkExpressionValueIsNotNull(mPasswordPreference, "mPasswordPreference");
        mPasswordPreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$3(this));
        refreshEmailsList();
        refreshPhoneNumbersList();
        setContactsPreferences();
        setUserInterfacePreferences();
        MXSession mXSession3 = this.mSession;
        if (mXSession3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (mXSession3.isURLPreviewEnabled()) {
            MXSession mXSession4 = this.mSession;
            if (mXSession4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            mXSession4.setURLPreviewStatus(false, new VectorSettingsPreferencesFragment$onCreate$4());
        }
        Preference mNotificationPrivacyPreference = getMNotificationPrivacyPreference();
        Intrinsics.checkExpressionValueIsNotNull(mNotificationPrivacyPreference, "mNotificationPrivacyPreference");
        mNotificationPrivacyPreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$5(this));
        refreshNotificationPrivacy();
        Preference mRingtonePreference = getMRingtonePreference();
        Intrinsics.checkExpressionValueIsNotNull(mRingtonePreference, "mRingtonePreference");
        mRingtonePreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$6(this));
        refreshNotificationRingTone();
        for (String findPreference : mPushesRuleByResourceId.keySet()) {
            Preference findPreference2 = findPreference(findPreference);
            if (findPreference2 != null) {
                if (findPreference2 instanceof CheckBoxPreference) {
                    ((CheckBoxPreference) findPreference2).setOnPreferenceChangeListener(new VectorSettingsPreferencesFragment$onCreate$7(this, findPreference2));
                } else if (findPreference2 instanceof BingRulePreference) {
                    ((BingRulePreference) findPreference2).setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$8(this, findPreference2));
                }
            }
        }
        Matrix instance = Matrix.getInstance(applicationContext);
        if (instance == null) {
            Intrinsics.throwNpe();
        }
        PushManager pushManager = instance.getPushManager();
        if (!pushManager.useFcm() || !pushManager.hasRegistrationToken()) {
            CheckBoxPreference backgroundSyncPreference = getBackgroundSyncPreference();
            Intrinsics.checkExpressionValueIsNotNull(pushManager, "pushManager");
            backgroundSyncPreference.setChecked(pushManager.isBackgroundSyncAllowed());
            backgroundSyncPreference.setOnPreferenceChangeListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$2(this, pushManager));
        } else {
            getPreferenceScreen().removePreference(getBackgroundSyncDivider());
            getPreferenceScreen().removePreference(getBackgroundSyncCategory());
        }
        refreshPushersList();
        refreshIgnoredUsersList();
        Preference findPreference3 = findPreference(PreferencesManager.SETTINGS_DATA_SAVE_MODE_PREFERENCE_KEY);
        if (findPreference3 != null) {
            findPreference3.setOnPreferenceChangeListener(new VectorSettingsPreferencesFragment$onCreate$10(this));
        }
        refreshDevicesList();
        Preference findPreference4 = findPreference(PreferencesManager.SETTINGS_USE_RAGE_SHAKE_KEY);
        if (findPreference4 != null) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference4;
            checkBoxPreference.setChecked(PreferencesManager.useRageshake(applicationContext));
            checkBoxPreference.setOnPreferenceChangeListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$3(applicationContext));
            Preference findPreference5 = findPreference(PreferencesManager.SETTINGS_MEDIA_SAVING_PERIOD_KEY);
            Intrinsics.checkExpressionValueIsNotNull(findPreference5, str2);
            findPreference5.setSummary(PreferencesManager.getSelectedMediasSavingPeriodString(getActivity()));
            findPreference5.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$4(findPreference5, this));
            Preference findPreference6 = findPreference(PreferencesManager.SETTINGS_CLEAR_MEDIA_CACHE_PREFERENCE_KEY);
            MXMediaCache.getCachesSize(getActivity(), new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$5(findPreference6, this));
            Intrinsics.checkExpressionValueIsNotNull(findPreference6, str2);
            findPreference6.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$6(findPreference6, this));
            Preference findPreference7 = findPreference(PreferencesManager.SETTINGS_CLEAR_CACHE_PREFERENCE_KEY);
            MXSession.getApplicationSizeCaches(getActivity(), new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$7(findPreference7, this, applicationContext));
            Intrinsics.checkExpressionValueIsNotNull(findPreference7, str2);
            findPreference7.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$8(this, applicationContext));
            Preference findPreference8 = findPreference(PreferencesManager.SETTINGS_DEACTIVATE_ACCOUNT_KEY);
            Intrinsics.checkExpressionValueIsNotNull(findPreference8, "findPreference(Preferenc…S_DEACTIVATE_ACCOUNT_KEY)");
            findPreference8.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$15(this));
            CheckBoxPreference hideFromUsersDirectoryPreference = getHideFromUsersDirectoryPreference();
            if (isUserAllowedToHideHimself()) {
                refreshUsersDirectoryVisibility();
                hideFromUsersDirectoryPreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$9(this));
            } else {
                getMUserSettingsCategory().removePreference(hideFromUsersDirectoryPreference);
            }
            getShowJoinLeaveMessagesPreference().setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$10(this, applicationContext));
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.preference.CheckBoxPreference");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        if (onCreateView != null) {
            View findViewById = onCreateView.findViewById(16908298);
            if (findViewById != null) {
                findViewById.setPadding(0, 0, 0, 0);
            }
        }
        return onCreateView;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        Intrinsics.checkParameterIsNotNull(sharedPreferences, "sharedPreferences");
        Intrinsics.checkParameterIsNotNull(str, "key");
        if (TextUtils.equals(str, ContactsManager.CONTACTS_BOOK_ACCESS_KEY)) {
            ContactsManager.getInstance().clearSnapshot();
        }
    }

    public void onResume() {
        super.onResume();
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (mXSession.isAlive()) {
            Activity activity = getActivity();
            Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
            Context applicationContext = activity.getApplicationContext();
            MXSession mXSession2 = this.mSession;
            if (mXSession2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            mXSession2.getDataHandler().addListener(this.mEventsListener);
            Matrix instance = Matrix.getInstance(applicationContext);
            if (instance == null) {
                Intrinsics.throwNpe();
            }
            instance.addNetworkEventListener(this.mNetworkListener);
            MXSession mXSession3 = this.mSession;
            if (mXSession3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            mXSession3.getMyUser().refreshThirdPartyIdentifiers(new VectorSettingsPreferencesFragment$onResume$1(this));
            Matrix instance2 = Matrix.getInstance(applicationContext);
            if (instance2 == null) {
                Intrinsics.throwNpe();
            }
            PushManager pushManager = instance2.getPushManager();
            Matrix instance3 = Matrix.getInstance(applicationContext);
            if (instance3 == null) {
                Intrinsics.throwNpe();
            }
            pushManager.refreshPushersList(instance3.getSessions(), new VectorSettingsPreferencesFragment$onResume$2(this, getActivity()));
            PreferenceManager.getDefaultSharedPreferences(applicationContext).registerOnSharedPreferenceChangeListener(this);
            refreshPreferences();
            refreshNotificationPrivacy();
            refreshDisplay();
            refreshBackgroundSyncPrefs();
        }
    }

    public void onPause() {
        super.onPause();
        Activity activity = getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Context applicationContext = activity.getApplicationContext();
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (mXSession.isAlive()) {
            MXSession mXSession2 = this.mSession;
            if (mXSession2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            mXSession2.getDataHandler().removeListener(this.mEventsListener);
            Matrix instance = Matrix.getInstance(applicationContext);
            if (instance == null) {
                Intrinsics.throwNpe();
            }
            instance.removeNetworkEventListener(this.mNetworkListener);
        }
        PreferenceManager.getDefaultSharedPreferences(applicationContext).unregisterOnSharedPreferenceChangeListener(this);
    }

    private final View getLoadingView() {
        View view = getView();
        View view2 = null;
        while (view != null && view2 == null) {
            view2 = view.findViewById(R.id.vector_settings_spinner_views);
            ViewParent parent = view.getParent();
            if (parent != null) {
                view = (View) parent;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type android.view.View");
            }
        }
        return view2;
    }

    /* access modifiers changed from: private */
    public final void displayLoadingView() {
        View loadingView = getLoadingView();
        if (loadingView != null) {
            loadingView.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public final void hideLoadingView() {
        hideLoadingView(false);
    }

    /* access modifiers changed from: private */
    public final void hideLoadingView(boolean z) {
        View loadingView = getLoadingView();
        if (loadingView != null) {
            loadingView.setVisibility(8);
        }
        if (z) {
            refreshDisplay();
        }
    }

    /* access modifiers changed from: private */
    public final void refreshDisplay() {
        boolean z;
        boolean z2;
        Matrix instance = Matrix.getInstance(getActivity());
        if (instance == null) {
            Intrinsics.throwNpe();
        }
        boolean isConnected = instance.isConnected();
        Activity activity = getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Context applicationContext = activity.getApplicationContext();
        PreferenceManager preferenceManager = getPreferenceManager();
        getMUserAvatarPreference().refreshAvatar();
        getMUserAvatarPreference().setEnabled(isConnected);
        Preference mDisplayNamePreference = getMDisplayNamePreference();
        Intrinsics.checkExpressionValueIsNotNull(mDisplayNamePreference, "mDisplayNamePreference");
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        mDisplayNamePreference.setSummary(mXSession.getMyUser().displayname);
        Preference mPasswordPreference = getMPasswordPreference();
        Intrinsics.checkExpressionValueIsNotNull(mPasswordPreference, "mPasswordPreference");
        mPasswordPreference.setEnabled(isConnected);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        MXSession mXSession2 = this.mSession;
        if (mXSession2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        PushRuleSet pushRules = mXSession2.getDataHandler().pushRules();
        Matrix instance2 = Matrix.getInstance(applicationContext);
        if (instance2 == null) {
            Intrinsics.throwNpe();
        }
        PushManager pushManager = instance2.getPushManager();
        Iterator it = mPushesRuleByResourceId.keySet().iterator();
        while (true) {
            z = true;
            if (!it.hasNext()) {
                break;
            }
            String str2 = (String) it.next();
            Preference findPreference = preferenceManager.findPreference(str2);
            if (findPreference != null) {
                if (findPreference instanceof BingRulePreference) {
                    BingRulePreference bingRulePreference = (BingRulePreference) findPreference;
                    if (pushRules == null || !isConnected || !pushManager.areDeviceNotificationsAllowed()) {
                        z = false;
                    }
                    bingRulePreference.setEnabled(z);
                    MXSession mXSession3 = this.mSession;
                    if (mXSession3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException(str);
                    }
                    PushRuleSet pushRules2 = mXSession3.getDataHandler().pushRules();
                    if (pushRules2 != null) {
                        bingRulePreference.setBingRule(pushRules2.findDefaultRule((String) mPushesRuleByResourceId.get(str2)));
                    }
                } else if (findPreference instanceof CheckBoxPreference) {
                    if (Intrinsics.areEqual((Object) str2, (Object) PreferencesManager.SETTINGS_ENABLE_THIS_DEVICE_PREFERENCE_KEY)) {
                        ((CheckBoxPreference) findPreference).setChecked(pushManager.areDeviceNotificationsAllowed());
                    } else if (Intrinsics.areEqual((Object) str2, (Object) PreferencesManager.SETTINGS_TURN_SCREEN_ON_PREFERENCE_KEY)) {
                        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference;
                        Intrinsics.checkExpressionValueIsNotNull(pushManager, "pushManager");
                        checkBoxPreference.setChecked(pushManager.isScreenTurnedOn());
                        checkBoxPreference.setEnabled(pushManager.areDeviceNotificationsAllowed());
                    } else {
                        CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference;
                        if (pushRules == null || !isConnected) {
                            z = false;
                        }
                        checkBoxPreference2.setEnabled(z);
                        checkBoxPreference2.setChecked(defaultSharedPreferences.getBoolean(str2, false));
                    }
                }
            }
        }
        if (pushRules != null) {
            BingRule findDefaultRule = pushRules.findDefaultRule(BingRule.RULE_ID_DISABLE_ALL);
            if (findDefaultRule != null && findDefaultRule.isEnabled) {
                z2 = true;
                Preference mRingtonePreference = getMRingtonePreference();
                Intrinsics.checkExpressionValueIsNotNull(mRingtonePreference, "mRingtonePreference");
                mRingtonePreference.setEnabled(z2 && pushManager.areDeviceNotificationsAllowed());
                Preference mNotificationPrivacyPreference = getMNotificationPrivacyPreference();
                Intrinsics.checkExpressionValueIsNotNull(mNotificationPrivacyPreference, "mNotificationPrivacyPreference");
                if (z2 || !pushManager.areDeviceNotificationsAllowed() || !pushManager.useFcm()) {
                    z = false;
                }
                mNotificationPrivacyPreference.setEnabled(z);
            }
        }
        z2 = false;
        Preference mRingtonePreference2 = getMRingtonePreference();
        Intrinsics.checkExpressionValueIsNotNull(mRingtonePreference2, "mRingtonePreference");
        mRingtonePreference2.setEnabled(z2 && pushManager.areDeviceNotificationsAllowed());
        Preference mNotificationPrivacyPreference2 = getMNotificationPrivacyPreference();
        Intrinsics.checkExpressionValueIsNotNull(mNotificationPrivacyPreference2, "mNotificationPrivacyPreference");
        z = false;
        mNotificationPrivacyPreference2.setEnabled(z);
    }

    /* access modifiers changed from: private */
    public final void onPasswordUpdateClick() {
        new Builder(getActivity()).setTitle((int) R.string.dialog_title_warning).setMessage((int) R.string.settings_change_pwd_caution).setPositiveButton((int) R.string.settings_change_password, (OnClickListener) new VectorSettingsPreferencesFragment$onPasswordUpdateClick$1(this)).setNegativeButton((int) R.string.encryption_export_room_keys, (OnClickListener) new VectorSettingsPreferencesFragment$onPasswordUpdateClick$2(this)).setNeutralButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public final void doShowPasswordChangeDialog() {
        getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1(this));
    }

    /* access modifiers changed from: private */
    public final void onPushRuleClick(String str, boolean z) {
        Matrix instance = Matrix.getInstance(getActivity());
        if (instance == null) {
            Intrinsics.throwNpe();
        }
        PushManager pushManager = instance.getPushManager();
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPushRuleClick ");
        sb.append(str);
        sb.append(" : set to ");
        sb.append(z);
        Log.d(str2, sb.toString());
        String str3 = "pushManager";
        if (Intrinsics.areEqual((Object) str, (Object) PreferencesManager.SETTINGS_TURN_SCREEN_ON_PREFERENCE_KEY)) {
            Intrinsics.checkExpressionValueIsNotNull(pushManager, str3);
            if (pushManager.isScreenTurnedOn() != z) {
                pushManager.setScreenTurnedOn(z);
            }
        } else if (Intrinsics.areEqual((Object) str, (Object) PreferencesManager.SETTINGS_ENABLE_THIS_DEVICE_PREFERENCE_KEY)) {
            Matrix instance2 = Matrix.getInstance(getActivity());
            if (instance2 == null) {
                Intrinsics.throwNpe();
            }
            boolean isConnected = instance2.isConnected();
            boolean areDeviceNotificationsAllowed = pushManager.areDeviceNotificationsAllowed();
            if (areDeviceNotificationsAllowed != z) {
                pushManager.setDeviceNotificationsAllowed(!areDeviceNotificationsAllowed);
                if (isConnected && pushManager.useFcm()) {
                    Intrinsics.checkExpressionValueIsNotNull(pushManager, str3);
                    if (pushManager.isServerRegistered() || pushManager.isServerUnRegistered()) {
                        VectorSettingsPreferencesFragment$onPushRuleClick$listener$1 vectorSettingsPreferencesFragment$onPushRuleClick$listener$1 = new VectorSettingsPreferencesFragment$onPushRuleClick$listener$1(this, pushManager, areDeviceNotificationsAllowed);
                        displayLoadingView();
                        if (pushManager.isServerRegistered()) {
                            pushManager.unregister(vectorSettingsPreferencesFragment$onPushRuleClick$listener$1);
                        } else {
                            pushManager.register(vectorSettingsPreferencesFragment$onPushRuleClick$listener$1);
                        }
                    }
                }
            }
        } else {
            String str4 = (String) mPushesRuleByResourceId.get(str);
            MXSession mXSession = this.mSession;
            String str5 = "mSession";
            if (mXSession == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str5);
            }
            PushRuleSet pushRules = mXSession.getDataHandler().pushRules();
            BingRule findDefaultRule = pushRules != null ? pushRules.findDefaultRule(str4) : null;
            boolean z2 = findDefaultRule != null && findDefaultRule.isEnabled;
            CharSequence charSequence = str4;
            if (TextUtils.equals(charSequence, BingRule.RULE_ID_DISABLE_ALL) || TextUtils.equals(charSequence, BingRule.RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS)) {
                z2 = !z2;
            }
            if (!(z == z2 || findDefaultRule == null)) {
                displayLoadingView();
                MXSession mXSession2 = this.mSession;
                if (mXSession2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str5);
                }
                MXDataHandler dataHandler = mXSession2.getDataHandler();
                Intrinsics.checkExpressionValueIsNotNull(dataHandler, "mSession.dataHandler");
                dataHandler.getBingRulesManager().updateEnableRuleStatus(findDefaultRule, !findDefaultRule.isEnabled, new VectorSettingsPreferencesFragment$onPushRuleClick$1(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public final void onUpdateAvatarClick() {
        getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onUpdateAvatarClick$1(this));
    }

    private final void refreshNotificationRingTone() {
        Preference mRingtonePreference = getMRingtonePreference();
        Intrinsics.checkExpressionValueIsNotNull(mRingtonePreference, "mRingtonePreference");
        mRingtonePreference.setSummary(PreferencesManager.getNotificationRingToneName(getActivity()));
    }

    private final void refreshNotificationPrivacy() {
        Matrix instance = Matrix.getInstance(getActivity());
        if (instance == null) {
            Intrinsics.throwNpe();
        }
        PushManager pushManager = instance.getPushManager();
        if (pushManager.useFcm()) {
            Activity activity = getActivity();
            Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
            Context applicationContext = activity.getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(pushManager, "pushManager");
            String notificationPrivacyString = NotificationPrivacyActivity.getNotificationPrivacyString(applicationContext, pushManager.getNotificationPrivacy());
            Preference mNotificationPrivacyPreference = getMNotificationPrivacyPreference();
            Intrinsics.checkExpressionValueIsNotNull(mNotificationPrivacyPreference, "mNotificationPrivacyPreference");
            mNotificationPrivacyPreference.setSummary(notificationPrivacyString);
            return;
        }
        getNotificationsSettingsCategory().removePreference(getMNotificationPrivacyPreference());
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            Parcelable parcelable = null;
            if (i == 1) {
                Context activity = getActivity();
                MXSession mXSession = this.mSession;
                String str = "mSession";
                if (mXSession == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                }
                Uri thumbnailUriFromIntent = VectorUtils.getThumbnailUriFromIntent(activity, intent, mXSession.getMediaCache());
                if (thumbnailUriFromIntent != null) {
                    displayLoadingView();
                    Resource openResource = ResourceUtils.openResource(getActivity(), thumbnailUriFromIntent, null);
                    if (openResource != null) {
                        MXSession mXSession2 = this.mSession;
                        if (mXSession2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException(str);
                        }
                        mXSession2.getMediaCache().uploadContent(openResource.mContentStream, null, openResource.mMimeType, null, new VectorSettingsPreferencesFragment$onActivityResult$1(this));
                    }
                }
            } else if (i == REQUEST_E2E_FILE_REQUEST_CODE) {
                importKeys(intent);
            } else if (i == REQUEST_NEW_PHONE_NUMBER) {
                refreshPhoneNumbersList();
            } else if (i == REQUEST_LOCALE) {
                Activity activity2 = getActivity();
                Intrinsics.checkExpressionValueIsNotNull(activity2, "activity");
                startActivity(activity2.getIntent());
                getActivity().finish();
            } else if (i == REQUEST_PHONEBOOK_COUNTRY) {
                onPhonebookCountryUpdate(intent);
            } else if (i == REQUEST_NOTIFICATION_RINGTONE) {
                Context activity3 = getActivity();
                if (intent != null) {
                    parcelable = intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
                }
                PreferencesManager.setNotificationRingTone(activity3, (Uri) parcelable);
                if (PreferencesManager.getNotificationRingToneName(getActivity()) == null) {
                    PreferencesManager.setNotificationRingTone(getActivity(), PreferencesManager.getNotificationRingTone(getActivity()));
                }
                refreshNotificationRingTone();
            }
        }
    }

    /* access modifiers changed from: private */
    public final void refreshPreferences() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…aredPreferences(activity)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        edit.putString(PreferencesManager.SETTINGS_DISPLAY_NAME_PREFERENCE_KEY, mXSession.getMyUser().displayname);
        edit.putString(PreferencesManager.SETTINGS_VERSION_PREFERENCE_KEY, VectorUtils.getApplicationVersion(getActivity()));
        MXSession mXSession2 = this.mSession;
        if (mXSession2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        PushRuleSet pushRules = mXSession2.getDataHandler().pushRules();
        if (pushRules != null) {
            for (String str2 : mPushesRuleByResourceId.keySet()) {
                Preference findPreference = findPreference(str2);
                if (findPreference != null && (findPreference instanceof CheckBoxPreference)) {
                    String str3 = (String) mPushesRuleByResourceId.get(str2);
                    BingRule findDefaultRule = pushRules.findDefaultRule(str3);
                    boolean z = false;
                    boolean z2 = findDefaultRule != null && findDefaultRule.isEnabled;
                    CharSequence charSequence = str3;
                    if (TextUtils.equals(charSequence, BingRule.RULE_ID_DISABLE_ALL) || TextUtils.equals(charSequence, BingRule.RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS)) {
                        z = !z2;
                    } else {
                        if (z2) {
                            if (findDefaultRule == null) {
                                Intrinsics.throwNpe();
                            }
                            List<Object> list = findDefaultRule.actions;
                            if (list != null && !list.isEmpty()) {
                                if (1 == list.size()) {
                                    try {
                                        Object obj = list.get(0);
                                        if (obj != null) {
                                            z = !TextUtils.equals((String) obj, BingRule.ACTION_DONT_NOTIFY);
                                        } else {
                                            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                                        }
                                    } catch (Exception e) {
                                        String str4 = LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("## refreshPreferences failed ");
                                        sb.append(e.getMessage());
                                        Log.e(str4, sb.toString(), e);
                                    }
                                }
                            }
                        }
                        z = z2;
                    }
                    edit.putBoolean(str2, z);
                }
            }
        }
        edit.apply();
    }

    private final void displayDelete3PIDConfirmationDialog(ThirdPartyIdentifier thirdPartyIdentifier, CharSequence charSequence) {
        String mediumFriendlyName = ThreePid.getMediumFriendlyName(thirdPartyIdentifier.medium, getActivity());
        Intrinsics.checkExpressionValueIsNotNull(mediumFriendlyName, "ThreePid.getMediumFriend…ame(pid.medium, activity)");
        Locale applicationLocale = VectorLocale.INSTANCE.getApplicationLocale();
        if (mediumFriendlyName != null) {
            String lowerCase = mediumFriendlyName.toLowerCase(applicationLocale);
            Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            new Builder(getActivity()).setTitle((int) R.string.dialog_title_confirmation).setMessage((CharSequence) getString(R.string.settings_delete_threepid_confirmation, new Object[]{lowerCase, charSequence})).setPositiveButton((int) R.string.remove, (OnClickListener) new VectorSettingsPreferencesFragment$displayDelete3PIDConfirmationDialog$1(this, thirdPartyIdentifier)).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    private final void refreshIgnoredUsersList() {
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        MXDataHandler dataHandler = mXSession.getDataHandler();
        Intrinsics.checkExpressionValueIsNotNull(dataHandler, "mSession.dataHandler");
        List<String> ignoredUserIds = dataHandler.getIgnoredUserIds();
        Intrinsics.checkExpressionValueIsNotNull(ignoredUserIds, "ignoredUsersList");
        CollectionsKt.sortWith(ignoredUserIds, VectorSettingsPreferencesFragment$refreshIgnoredUsersList$1.INSTANCE);
        getPreferenceScreen().removePreference(getMIgnoredUserSettingsCategory());
        getPreferenceScreen().removePreference(getMIgnoredUserSettingsCategoryDivider());
        getMIgnoredUserSettingsCategory().removeAll();
        if (ignoredUserIds.size() > 0) {
            getPreferenceScreen().addPreference(getMIgnoredUserSettingsCategoryDivider());
            getPreferenceScreen().addPreference(getMIgnoredUserSettingsCategory());
            for (String str : ignoredUserIds) {
                VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference(getActivity());
                String computeDisplayNameFromUserId = DinsicUtils.computeDisplayNameFromUserId(str);
                vectorCustomActionEditTextPreference.setTitle(computeDisplayNameFromUserId);
                StringBuilder sb = new StringBuilder();
                sb.append(IGNORED_USER_KEY_BASE);
                sb.append(str);
                vectorCustomActionEditTextPreference.setKey(sb.toString());
                vectorCustomActionEditTextPreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$refreshIgnoredUsersList$2(this, computeDisplayNameFromUserId, str));
                getMIgnoredUserSettingsCategory().addPreference(vectorCustomActionEditTextPreference);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void refreshPushersList() {
        Matrix instance = Matrix.getInstance(getActivity());
        if (instance == null) {
            Intrinsics.throwNpe();
        }
        PushManager pushManager = instance.getPushManager();
        ArrayList<Pusher> arrayList = new ArrayList<>(pushManager.mPushersList);
        if (arrayList.isEmpty()) {
            getPreferenceScreen().removePreference(getMPushersSettingsCategory());
            getPreferenceScreen().removePreference(getMPushersSettingsDivider());
            return;
        }
        boolean z = true;
        if (arrayList.size() == this.mDisplayedPushers.size()) {
            z = true ^ this.mDisplayedPushers.containsAll(arrayList);
        }
        if (z) {
            getMPushersSettingsCategory().removeAll();
            this.mDisplayedPushers = arrayList;
            int i = 0;
            Iterator it = this.mDisplayedPushers.iterator();
            while (it.hasNext()) {
                Pusher pusher = (Pusher) it.next();
                if (pusher.lang != null) {
                    Intrinsics.checkExpressionValueIsNotNull(pushManager, "pushManager");
                    boolean equals = TextUtils.equals(pushManager.getCurrentRegistrationToken(), pusher.pushkey);
                    VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference((Context) getActivity(), equals ? 1 : 0);
                    vectorCustomActionEditTextPreference.setTitle(pusher.deviceDisplayName);
                    StringBuilder sb = new StringBuilder();
                    sb.append(PUSHER_PREFERENCE_KEY_BASE);
                    sb.append(i);
                    vectorCustomActionEditTextPreference.setKey(sb.toString());
                    i++;
                    getMPushersSettingsCategory().addPreference(vectorCustomActionEditTextPreference);
                    if (!equals) {
                        vectorCustomActionEditTextPreference.setOnPreferenceLongClickListener(new VectorSettingsPreferencesFragment$refreshPushersList$1(this, pushManager, pusher));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final void refreshEmailsList() {
        String str;
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        ArrayList arrayList = new ArrayList(mXSession.getMyUser().getlinkedEmails());
        ArrayList<String> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((ThirdPartyIdentifier) it.next()).address);
        }
        if (arrayList2.size() == this.mDisplayedEmails.size() ? !this.mDisplayedEmails.containsAll(arrayList2) : true) {
            VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = this;
            int i = 0;
            int i2 = 0;
            while (true) {
                PreferenceCategory mUserSettingsCategory = vectorSettingsPreferencesFragment.getMUserSettingsCategory();
                StringBuilder sb = new StringBuilder();
                str = EMAIL_PREFERENCE_KEY_BASE;
                sb.append(str);
                sb.append(i2);
                Preference findPreference = mUserSettingsCategory.findPreference(sb.toString());
                if (findPreference == null) {
                    break;
                }
                vectorSettingsPreferencesFragment.getMUserSettingsCategory().removePreference(findPreference);
                i2++;
            }
            this.mDisplayedEmails = arrayList2;
            ArrayList<String> arrayList3 = this.mDisplayedEmails;
            if (arrayList3 != null && arrayList3.size() > 1) {
                Log.e(LOG_TAG, "## there are more than one email in the account ");
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                ThirdPartyIdentifier thirdPartyIdentifier = (ThirdPartyIdentifier) it2.next();
                VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference(getActivity());
                vectorCustomActionEditTextPreference.setTitle(getString(R.string.settings_email_address));
                vectorCustomActionEditTextPreference.setSummary(thirdPartyIdentifier.address);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(i);
                vectorCustomActionEditTextPreference.setKey(sb2.toString());
                getMUserSettingsCategory().addPreference(vectorCustomActionEditTextPreference);
                i++;
            }
        }
    }

    /* access modifiers changed from: private */
    public final void onCommonDone(String str) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onCommonDone$1(this, str));
        }
    }

    /* access modifiers changed from: private */
    public final void refreshPhoneNumbersList() {
        String str;
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        ArrayList arrayList = new ArrayList(mXSession.getMyUser().getlinkedPhoneNumbers());
        ArrayList<String> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((ThirdPartyIdentifier) it.next()).address);
        }
        if (arrayList2.size() == this.mDisplayedPhoneNumber.size() ? !this.mDisplayedPhoneNumber.containsAll(arrayList2) : true) {
            VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = this;
            int i = 0;
            int i2 = 0;
            while (true) {
                PreferenceCategory mUserSettingsCategory = vectorSettingsPreferencesFragment.getMUserSettingsCategory();
                StringBuilder sb = new StringBuilder();
                str = PHONE_NUMBER_PREFERENCE_KEY_BASE;
                sb.append(str);
                sb.append(i2);
                Preference findPreference = mUserSettingsCategory.findPreference(sb.toString());
                if (findPreference == null) {
                    break;
                }
                vectorSettingsPreferencesFragment.getMUserSettingsCategory().removePreference(findPreference);
                i2++;
            }
            this.mDisplayedPhoneNumber = arrayList2;
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                ThirdPartyIdentifier thirdPartyIdentifier = (ThirdPartyIdentifier) it2.next();
                VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference(getActivity());
                vectorCustomActionEditTextPreference.setTitle(getString(R.string.settings_phone_number));
                String str2 = thirdPartyIdentifier.address;
                try {
                    PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append('+');
                    sb2.append(str2);
                    str2 = PhoneNumberUtil.getInstance().format(instance.parse(sb2.toString(), null), PhoneNumberFormat.INTERNATIONAL);
                } catch (NumberParseException unused) {
                }
                vectorCustomActionEditTextPreference.setSummary(str2);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(i);
                vectorCustomActionEditTextPreference.setKey(sb3.toString());
                i++;
                getMUserSettingsCategory().addPreference(vectorCustomActionEditTextPreference);
            }
        }
    }

    private final void setContactsPreferences() {
        if (VERSION.SDK_INT >= 23) {
            getMContactSettingsCategory().removePreference(findPreference(ContactsManager.CONTACTS_BOOK_ACCESS_KEY));
        }
        getMContactPhonebookCountryPreference().setSummary(PhoneNumberUtils.getHumanCountryCode(PhoneNumberUtils.getCountryCode(getActivity())));
        getMContactPhonebookCountryPreference().setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$setContactsPreferences$1(this));
    }

    private final void onPhonebookCountryUpdate(Intent intent) {
        if (intent != null) {
            String str = CountryPickerActivity.EXTRA_OUT_COUNTRY_NAME;
            if (intent.hasExtra(str)) {
                String str2 = CountryPickerActivity.EXTRA_OUT_COUNTRY_CODE;
                if (intent.hasExtra(str2)) {
                    String stringExtra = intent.getStringExtra(str2);
                    if (!TextUtils.equals(stringExtra, PhoneNumberUtils.getCountryCode(getActivity()))) {
                        PhoneNumberUtils.setCountryCode(getActivity(), stringExtra);
                        getMContactPhonebookCountryPreference().setSummary(intent.getStringExtra(str));
                    }
                }
            }
        }
    }

    private final void setUserInterfacePreferences() {
        getTextSizePreference().setSummary(FontScale.INSTANCE.getFontScaleDescription());
        getTextSizePreference().setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$setUserInterfacePreferences$1(this));
    }

    /* access modifiers changed from: private */
    public final void displayTextSizeSelection(Activity activity) {
        View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_select_text_size, null);
        AlertDialog show = new Builder(activity).setTitle((int) R.string.font_size).setView(inflate).setPositiveButton((int) R.string.ok, (OnClickListener) null).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.text_selection_group_view);
        Intrinsics.checkExpressionValueIsNotNull(linearLayout, "linearLayout");
        int childCount = linearLayout.getChildCount();
        String fontScaleDescription = FontScale.INSTANCE.getFontScaleDescription();
        for (int i = 0; i < childCount; i++) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof CheckedTextView) {
                CheckedTextView checkedTextView = (CheckedTextView) childAt;
                checkedTextView.setChecked(TextUtils.equals(checkedTextView.getText(), fontScaleDescription));
                childAt.setOnClickListener(new VectorSettingsPreferencesFragment$displayTextSizeSelection$1(show, childAt, activity));
            }
        }
    }

    private final String secondsToText(int i) {
        String str = " ";
        if (i > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(i));
            sb.append(str);
            sb.append(getString(R.string.settings_seconds));
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(String.valueOf(i));
        sb2.append(str);
        sb2.append(getString(R.string.settings_second));
        return sb2.toString();
    }

    /* access modifiers changed from: private */
    public final void refreshBackgroundSyncPrefs() {
        if (getActivity() != null) {
            Matrix instance = Matrix.getInstance(getActivity());
            if (instance == null) {
                Intrinsics.throwNpe();
            }
            PushManager pushManager = instance.getPushManager();
            Intrinsics.checkExpressionValueIsNotNull(pushManager, "pushManager");
            int backgroundSyncDelay = pushManager.getBackgroundSyncDelay() / 1000;
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…aredPreferences(activity)");
            Editor edit = defaultSharedPreferences.edit();
            Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(backgroundSyncDelay));
            String str = "";
            sb.append(str);
            edit.putString(PreferencesManager.SETTINGS_SET_SYNC_DELAY_PREFERENCE_KEY, sb.toString());
            edit.apply();
            EditTextPreference mSyncRequestDelayPreference = getMSyncRequestDelayPreference();
            if (mSyncRequestDelayPreference != null) {
                mSyncRequestDelayPreference.setSummary(secondsToText(backgroundSyncDelay));
                StringBuilder sb2 = new StringBuilder();
                sb2.append(String.valueOf(backgroundSyncDelay));
                sb2.append(str);
                mSyncRequestDelayPreference.setText(sb2.toString());
                mSyncRequestDelayPreference.setOnPreferenceChangeListener(new VectorSettingsPreferencesFragment$refreshBackgroundSyncPrefs$$inlined$let$lambda$1(this, backgroundSyncDelay, pushManager));
            }
        }
    }

    private final void removeCryptographyPreference() {
        if (getPreferenceScreen() != null) {
            getPreferenceScreen().removePreference(getMCryptographyCategory());
            getPreferenceScreen().removePreference(getMCryptographyCategoryDivider());
        }
    }

    private final void refreshCryptographyPreference(DeviceInfo deviceInfo) {
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        String myUserId = mXSession.getMyUserId();
        Intrinsics.checkExpressionValueIsNotNull(myUserId, "mSession.myUserId");
        MXSession mXSession2 = this.mSession;
        if (mXSession2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        String str2 = mXSession2.getCredentials().deviceId;
        if (deviceInfo != null) {
            getCryptoInfoDeviceNamePreference().setSummary(deviceInfo.display_name);
            getCryptoInfoDeviceNamePreference().setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$refreshCryptographyPreference$1(this, deviceInfo));
            getCryptoInfoDeviceNamePreference().setOnPreferenceLongClickListener(new VectorSettingsPreferencesFragment$refreshCryptographyPreference$2(this, deviceInfo));
        }
        CharSequence charSequence = str2;
        if (!TextUtils.isEmpty(charSequence)) {
            getCryptoInfoDeviceIdPreference().setSummary(charSequence);
            getCryptoInfoDeviceIdPreference().setOnPreferenceLongClickListener(new VectorSettingsPreferencesFragment$refreshCryptographyPreference$3(this, str2));
            getExportPref().setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$refreshCryptographyPreference$4(this));
            getImportPref().setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$refreshCryptographyPreference$5(this));
        }
        if (!TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty(myUserId)) {
            MXSession mXSession3 = this.mSession;
            if (mXSession3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            MXCrypto crypto = mXSession3.getCrypto();
            if (crypto != null) {
                crypto.getDeviceInfo(myUserId, str2, new VectorSettingsPreferencesFragment$refreshCryptographyPreference$6(this));
            }
        }
    }

    /* access modifiers changed from: private */
    public final void removeDevicesPreference() {
        if (getPreferenceScreen() != null) {
            getPreferenceScreen().removePreference(getMDevicesListSettingsCategory());
            getPreferenceScreen().removePreference(getMDevicesListSettingsCategoryDivider());
        }
    }

    /* access modifiers changed from: private */
    public final void refreshDevicesList() {
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (mXSession.isCryptoEnabled()) {
            MXSession mXSession2 = this.mSession;
            if (mXSession2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            if (!TextUtils.isEmpty(mXSession2.getCredentials().deviceId)) {
                if (getMDevicesListSettingsCategory().getPreferenceCount() == 0) {
                    getMDevicesListSettingsCategory().addPreference(new ProgressBarPreference(getActivity()));
                }
                MXSession mXSession3 = this.mSession;
                if (mXSession3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                }
                mXSession3.getDevicesList(new VectorSettingsPreferencesFragment$refreshDevicesList$1(this));
                return;
            }
        }
        removeDevicesPreference();
        removeCryptographyPreference();
    }

    /* access modifiers changed from: private */
    public final void buildDevicesSettings(List<? extends DeviceInfo> list) {
        int i;
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        String str = mXSession.getCredentials().deviceId;
        if (list.size() == this.mDevicesNameList.size() ? !this.mDevicesNameList.containsAll(list) : true) {
            this.mDevicesNameList = list;
            DeviceInfoUtil.sortByLastSeen(this.mDevicesNameList);
            getMDevicesListSettingsCategory().removeAll();
            int i2 = 0;
            for (DeviceInfo deviceInfo : this.mDevicesNameList) {
                if (str == null || !Intrinsics.areEqual((Object) str, (Object) deviceInfo.device_id)) {
                    i = 0;
                } else {
                    this.mMyDeviceInfo = deviceInfo;
                    i = 1;
                }
                VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference((Context) getActivity(), i);
                if (deviceInfo.device_id != null || deviceInfo.display_name != null) {
                    if (deviceInfo.device_id != null) {
                        vectorCustomActionEditTextPreference.setTitle(deviceInfo.device_id);
                    }
                    if (deviceInfo.display_name != null) {
                        vectorCustomActionEditTextPreference.setSummary(deviceInfo.display_name);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(DEVICES_PREFERENCE_KEY_BASE);
                    sb.append(i2);
                    vectorCustomActionEditTextPreference.setKey(sb.toString());
                    i2++;
                    vectorCustomActionEditTextPreference.setOnPreferenceClickListener(new VectorSettingsPreferencesFragment$buildDevicesSettings$1(this, deviceInfo));
                    getMDevicesListSettingsCategory().addPreference(vectorCustomActionEditTextPreference);
                }
            }
            refreshCryptographyPreference(this.mMyDeviceInfo);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0121  */
    public final void displayDeviceDetailsDialog(DeviceInfo deviceInfo) {
        CharSequence charSequence;
        Builder builder = new Builder(getActivity());
        Activity activity = getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_device_details, null);
        if (deviceInfo != null) {
            TextView textView = (TextView) inflate.findViewById(R.id.device_id);
            String str = "textView";
            Intrinsics.checkExpressionValueIsNotNull(textView, str);
            textView.setText(deviceInfo.device_id);
            TextView textView2 = (TextView) inflate.findViewById(R.id.device_name);
            boolean isEmpty = TextUtils.isEmpty(deviceInfo.display_name);
            String str2 = LABEL_UNAVAILABLE_DATA;
            String str3 = isEmpty ? str2 : deviceInfo.display_name;
            Intrinsics.checkExpressionValueIsNotNull(textView2, str);
            textView2.setText(str3);
            TextView textView3 = (TextView) inflate.findViewById(R.id.device_last_seen);
            if (!TextUtils.isEmpty(deviceInfo.last_seen_ip)) {
                String str4 = deviceInfo.last_seen_ip;
                if (getActivity() != null) {
                    String format = new SimpleDateFormat("HH:mm:ss").format(new Date(deviceInfo.last_seen_ts));
                    DateFormat dateInstance = DateFormat.getDateInstance(3, Locale.getDefault());
                    StringBuilder sb = new StringBuilder();
                    sb.append(dateInstance.format(new Date(deviceInfo.last_seen_ts)));
                    sb.append(", ");
                    sb.append(format);
                    str2 = sb.toString();
                }
                String string = getString(R.string.devices_details_last_seen_format, new Object[]{str4, str2});
                Intrinsics.checkExpressionValueIsNotNull(textView3, str);
                textView3.setText(string);
            } else {
                View findViewById = inflate.findViewById(R.id.device_last_seen_title);
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "layout.findViewById<View…d.device_last_seen_title)");
                findViewById.setVisibility(8);
                Intrinsics.checkExpressionValueIsNotNull(textView3, str);
                textView3.setVisibility(8);
            }
            builder.setTitle((int) R.string.devices_details_dialog_title).setIcon(17301659).setView(inflate).setPositiveButton((int) R.string.rename, (OnClickListener) new VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$1(this, deviceInfo));
            MXSession mXSession = this.mSession;
            if (mXSession == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mSession");
            }
            MXCrypto crypto = mXSession.getCrypto();
            if (crypto != null) {
                MXDeviceInfo myDevice = crypto.getMyDevice();
                if (myDevice != null) {
                    charSequence = myDevice.deviceId;
                    if (!TextUtils.equals(charSequence, deviceInfo.device_id)) {
                        builder.setNegativeButton((int) R.string.delete, (OnClickListener) new VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$2(this, deviceInfo));
                    }
                    builder.setNeutralButton((int) R.string.cancel, (OnClickListener) null).setOnKeyListener(VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$3.INSTANCE).show();
                    return;
                }
            }
            charSequence = null;
            if (!TextUtils.equals(charSequence, deviceInfo.device_id)) {
            }
            builder.setNeutralButton((int) R.string.cancel, (OnClickListener) null).setOnKeyListener(VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$3.INSTANCE).show();
            return;
        }
        Log.e(LOG_TAG, "## displayDeviceDetailsDialog(): sanity check failure");
    }

    /* access modifiers changed from: private */
    public final void displayDeviceRenameDialog(DeviceInfo deviceInfo) {
        Activity activity = getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_base_edit_text, null);
        EditText editText = (EditText) inflate.findViewById(R.id.edit_text);
        editText.setText(deviceInfo.display_name);
        new Builder(getActivity()).setTitle((int) R.string.devices_details_device_name).setView(inflate).setPositiveButton((int) R.string.ok, (OnClickListener) new VectorSettingsPreferencesFragment$displayDeviceRenameDialog$1(this, editText, deviceInfo)).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public final void deleteDevice(String str) {
        displayLoadingView();
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        mXSession.deleteDevice(str, this.mAccountPassword, new VectorSettingsPreferencesFragment$deleteDevice$1(this));
    }

    /* access modifiers changed from: private */
    public final void displayDeviceDeletionDialog(DeviceInfo deviceInfo) {
        if (deviceInfo == null || deviceInfo.device_id == null) {
            Log.e(LOG_TAG, "## displayDeviceDeletionDialog(): sanity check failure");
        } else if (!TextUtils.isEmpty(this.mAccountPassword)) {
            String str = deviceInfo.device_id;
            Intrinsics.checkExpressionValueIsNotNull(str, "aDeviceInfoToDelete.device_id");
            deleteDevice(str);
        } else {
            Activity activity = getActivity();
            Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
            View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_device_delete, null);
            new Builder(getActivity()).setIcon(17301543).setTitle((int) R.string.devices_delete_dialog_title).setView(inflate).setPositiveButton((int) R.string.devices_delete_submit_button_label, (OnClickListener) new VectorSettingsPreferencesFragment$displayDeviceDeletionDialog$1(this, (EditText) inflate.findViewById(R.id.delete_password), deviceInfo)).setNegativeButton((int) R.string.cancel, (OnClickListener) new VectorSettingsPreferencesFragment$displayDeviceDeletionDialog$2(this)).setOnKeyListener(new VectorSettingsPreferencesFragment$displayDeviceDeletionDialog$3(this)).show();
        }
    }

    public final void exportKeys() {
        Activity activity = getActivity();
        String str = "activity";
        Intrinsics.checkExpressionValueIsNotNull(activity, str);
        if (PermissionsToolsKt.checkPermissions(2, activity, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE_EXPORT_KEYS)) {
            Activity activity2 = getActivity();
            Intrinsics.checkExpressionValueIsNotNull(activity2, str);
            View inflate = activity2.getLayoutInflater().inflate(R.layout.dialog_export_e2e_keys, null);
            Builder view = new Builder(getActivity()).setTitle((int) R.string.encryption_export_room_keys).setView(inflate);
            TextInputEditText textInputEditText = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_passphrase_edit_text);
            TextInputEditText textInputEditText2 = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_confirm_passphrase_edit_text);
            Button button = (Button) inflate.findViewById(R.id.dialog_e2e_keys_export_button);
            TextWatcher vectorSettingsPreferencesFragment$exportKeys$textWatcher$1 = new VectorSettingsPreferencesFragment$exportKeys$textWatcher$1(this, textInputEditText, button, textInputEditText2);
            textInputEditText.addTextChangedListener(vectorSettingsPreferencesFragment$exportKeys$textWatcher$1);
            textInputEditText2.addTextChangedListener(vectorSettingsPreferencesFragment$exportKeys$textWatcher$1);
            Intrinsics.checkExpressionValueIsNotNull(button, "exportButton");
            button.setEnabled(false);
            button.setOnClickListener(new VectorSettingsPreferencesFragment$exportKeys$1(this, textInputEditText, view.show()));
        }
    }

    /* access modifiers changed from: private */
    public final void importKeys() {
        Activity activity = getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        ExternalApplicationsUtilKt.openFileSelection(activity, this, false, REQUEST_E2E_FILE_REQUEST_CODE);
    }

    private final void importKeys(Intent intent) {
        if (intent != null) {
            ArrayList arrayList = new ArrayList(RoomMediaMessage.listRoomMediaMessages(intent));
            if (arrayList.size() > 0) {
                RoomMediaMessage roomMediaMessage = (RoomMediaMessage) arrayList.get(0);
                Activity activity = getActivity();
                String str = "activity";
                Intrinsics.checkExpressionValueIsNotNull(activity, str);
                View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_import_e2e_keys, null);
                Builder view = new Builder(getActivity()).setTitle((int) R.string.encryption_import_room_keys).setView(inflate);
                TextInputEditText textInputEditText = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_passphrase_edit_text);
                Button button = (Button) inflate.findViewById(R.id.dialog_e2e_keys_import_button);
                textInputEditText.addTextChangedListener(new VectorSettingsPreferencesFragment$importKeys$1(button, textInputEditText));
                Intrinsics.checkExpressionValueIsNotNull(button, "importButton");
                button.setEnabled(false);
                AlertDialog show = view.show();
                Activity activity2 = getActivity();
                Intrinsics.checkExpressionValueIsNotNull(activity2, str);
                VectorSettingsPreferencesFragment$importKeys$2 vectorSettingsPreferencesFragment$importKeys$2 = new VectorSettingsPreferencesFragment$importKeys$2(this, textInputEditText, activity2.getApplicationContext(), roomMediaMessage, show);
                button.setOnClickListener(vectorSettingsPreferencesFragment$importKeys$2);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void refreshUsersDirectoryVisibility() {
        Object obj;
        CheckBoxPreference hideFromUsersDirectoryPreference = getHideFromUsersDirectoryPreference();
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mSession");
        }
        MXDataHandler dataHandler = mXSession.getDataHandler();
        if (dataHandler != null) {
            IMXStore store = dataHandler.getStore();
            if (store != null) {
                AccountDataElement accountDataElement = store.getAccountDataElement("im.vector.hide_profile");
                if (accountDataElement != null) {
                    Map<String, Object> map = accountDataElement.content;
                    if (map != null) {
                        obj = map.get("hide_profile");
                        hideFromUsersDirectoryPreference.setChecked(Intrinsics.areEqual((Object) (Boolean) obj, (Object) Boolean.valueOf(true)));
                    }
                }
            }
        }
        obj = null;
        hideFromUsersDirectoryPreference.setChecked(Intrinsics.areEqual((Object) (Boolean) obj, (Object) Boolean.valueOf(true)));
    }

    /* access modifiers changed from: private */
    public final void hideUserFromUsersDirectory(boolean z) {
        displayLoadingView();
        MXSession mXSession = this.mSession;
        String str = "mSession";
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        AccountDataRestClient accountDataRestClient = mXSession.getAccountDataRestClient();
        MXSession mXSession2 = this.mSession;
        if (mXSession2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        String myUserId = mXSession2.getMyUserId();
        HashMap hashMap = new HashMap();
        hashMap.put("hide_profile", Boolean.valueOf(z));
        accountDataRestClient.setAccountData(myUserId, "im.vector.hide_profile", hashMap, new VectorSettingsPreferencesFragment$hideUserFromUsersDirectory$2(this, z));
    }

    static {
        Class<VectorSettingsPreferencesFragment> cls = VectorSettingsPreferencesFragment.class;
        $$delegatedProperties = new KProperty[]{Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mUserSettingsCategory", "getMUserSettingsCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mUserAvatarPreference", "getMUserAvatarPreference()Lim/vector/preference/UserAvatarPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mDisplayNamePreference", "getMDisplayNamePreference()Landroid/preference/Preference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mPasswordPreference", "getMPasswordPreference()Landroid/preference/Preference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mContactSettingsCategory", "getMContactSettingsCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mContactPhonebookCountryPreference", "getMContactPhonebookCountryPreference()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mCryptographyCategory", "getMCryptographyCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mCryptographyCategoryDivider", "getMCryptographyCategoryDivider()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mPushersSettingsDivider", "getMPushersSettingsDivider()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mPushersSettingsCategory", "getMPushersSettingsCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mDevicesListSettingsCategory", "getMDevicesListSettingsCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mDevicesListSettingsCategoryDivider", "getMDevicesListSettingsCategoryDivider()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mIgnoredUserSettingsCategoryDivider", "getMIgnoredUserSettingsCategoryDivider()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mIgnoredUserSettingsCategory", "getMIgnoredUserSettingsCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mSyncRequestDelayPreference", "getMSyncRequestDelayPreference()Landroid/preference/EditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "backgroundSyncCategory", "getBackgroundSyncCategory()Landroid/preference/Preference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "backgroundSyncDivider", "getBackgroundSyncDivider()Landroid/preference/Preference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "backgroundSyncPreference", "getBackgroundSyncPreference()Landroid/preference/CheckBoxPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mRingtonePreference", "getMRingtonePreference()Landroid/preference/Preference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "notificationsSettingsCategory", "getNotificationsSettingsCategory()Landroid/preference/PreferenceCategory;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "mNotificationPrivacyPreference", "getMNotificationPrivacyPreference()Landroid/preference/Preference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "textSizePreference", "getTextSizePreference()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "cryptoInfoDeviceNamePreference", "getCryptoInfoDeviceNamePreference()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "cryptoInfoDeviceIdPreference", "getCryptoInfoDeviceIdPreference()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "exportPref", "getExportPref()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "importPref", "getImportPref()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "cryptoInfoTextPreference", "getCryptoInfoTextPreference()Lim/vector/preference/VectorCustomActionEditTextPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "hideFromUsersDirectoryPreference", "getHideFromUsersDirectoryPreference()Landroid/preference/CheckBoxPreference;")), Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(cls), "showJoinLeaveMessagesPreference", "getShowJoinLeaveMessagesPreference()Landroid/preference/CheckBoxPreference;"))};
        LOG_TAG = cls.getSimpleName();
        String str = DUMMY_RULE;
        mPushesRuleByResourceId = MapsKt.mapOf(TuplesKt.to(PreferencesManager.SETTINGS_ENABLE_ALL_NOTIF_PREFERENCE_KEY, BingRule.RULE_ID_DISABLE_ALL), TuplesKt.to(PreferencesManager.SETTINGS_ENABLE_THIS_DEVICE_PREFERENCE_KEY, str), TuplesKt.to(PreferencesManager.SETTINGS_TURN_SCREEN_ON_PREFERENCE_KEY, str), TuplesKt.to(PreferencesManager.SETTINGS_CONTAINING_MY_DISPLAY_NAME_PREFERENCE_KEY, BingRule.RULE_ID_CONTAIN_DISPLAY_NAME), TuplesKt.to(PreferencesManager.SETTINGS_CONTAINING_MY_USER_NAME_PREFERENCE_KEY, BingRule.RULE_ID_CONTAIN_USER_NAME), TuplesKt.to(PreferencesManager.SETTINGS_MESSAGES_IN_ONE_TO_ONE_PREFERENCE_KEY, BingRule.RULE_ID_ONE_TO_ONE_ROOM), TuplesKt.to(PreferencesManager.SETTINGS_MESSAGES_IN_GROUP_CHAT_PREFERENCE_KEY, BingRule.RULE_ID_ALL_OTHER_MESSAGES_ROOMS), TuplesKt.to(PreferencesManager.SETTINGS_INVITED_TO_ROOM_PREFERENCE_KEY, BingRule.RULE_ID_INVITE_ME), TuplesKt.to(PreferencesManager.SETTINGS_CALL_INVITATIONS_PREFERENCE_KEY, BingRule.RULE_ID_CALL), TuplesKt.to(PreferencesManager.SETTINGS_MESSAGES_SENT_BY_BOT_PREFERENCE_KEY, BingRule.RULE_ID_SUPPRESS_BOTS_NOTIFICATIONS));
    }
}
