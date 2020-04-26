package im.vector.util;

import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.VectorApp;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorRoomActivity;
import im.vector.widgets.WidgetsManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlinx.coroutines.DebugKt;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;

public class SlashCommandsParser {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = SlashCommandsParser.class.getSimpleName();

    public enum SlashCommand {
        EMOTE("/me", "<message>", R.string.command_description_emote),
        BAN_USER("/ban", "<user-id> [reason]", R.string.command_description_ban_user),
        UNBAN_USER("/unban", "<user-id>", R.string.command_description_unban_user),
        SET_USER_POWER_LEVEL("/op", "<user-id> [<power-level>]", R.string.command_description_op_user),
        RESET_USER_POWER_LEVEL("/deop", "<user-id>", R.string.command_description_deop_user),
        INVITE("/invite", "<user-id>", R.string.command_description_invite_user),
        JOIN_ROOM("/join", "<room-alias>", R.string.command_description_join_room),
        PART("/part", "<room-alias>", R.string.command_description_part_room),
        TOPIC("/topic", "<topic>", R.string.command_description_topic),
        KICK_USER("/kick", "<user-id> [reason]", R.string.command_description_kick_user),
        CHANGE_DISPLAY_NAME("/nick", "<display-name>", R.string.command_description_nick),
        MARKDOWN("/markdown", "<on|off>", R.string.command_description_markdown),
        CLEAR_SCALAR_TOKEN("/clear_scalar_token", "", R.string.command_description_clear_scalar_token);
        
        private static final Map<String, SlashCommand> lookup = null;
        private final String command;
        private int description;
        private String parameter;

        static {
            int i;
            SlashCommand[] values;
            lookup = new HashMap();
            for (SlashCommand slashCommand : values()) {
                lookup.put(slashCommand.getCommand(), slashCommand);
            }
        }

        private SlashCommand(String str, String str2, int i) {
            this.command = str;
            this.parameter = str2;
            this.description = i;
        }

        public static SlashCommand get(String str) {
            return (SlashCommand) lookup.get(str);
        }

        public String getCommand() {
            return this.command;
        }

        public String getParam() {
            return this.parameter;
        }

        public int getDescription() {
            return this.description;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:122:0x02f4  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0314  */
    public static boolean manageSplashCommand(final VectorRoomActivity vectorRoomActivity, final MXSession mXSession, Room room, final String str, String str2, String str3) {
        String[] strArr;
        boolean z;
        boolean z2;
        Room room2;
        boolean z3;
        if (vectorRoomActivity == null || mXSession == null || room == null) {
            Log.e(LOG_TAG, "manageSplashCommand : invalid parameters");
            return false;
        }
        boolean z4 = true;
        if (str != null) {
            String str4 = "/";
            if (str.startsWith(str4)) {
                String str5 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("manageSplashCommand : ");
                sb.append(str);
                Log.d(str5, sb.toString());
                if (str.length() == 1 || str4.equals(str.substring(1, 2))) {
                    return false;
                }
                AnonymousClass1 r2 = new SimpleApiCallback<Void>(vectorRoomActivity) {
                    public void onSuccess(Void voidR) {
                        String access$000 = SlashCommandsParser.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("manageSplashCommand : ");
                        sb.append(str);
                        sb.append(" : the operation succeeded.");
                        Log.d(access$000, sb.toString());
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        if (MatrixError.FORBIDDEN.equals(matrixError.errcode)) {
                            Toast.makeText(vectorRoomActivity, matrixError.error, 1).show();
                            return;
                        }
                        if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                            vectorRoomActivity.getConsentNotGivenHelper().displayDialog(matrixError);
                        }
                    }
                };
                try {
                    strArr = str.split("\\s+");
                } catch (Exception e) {
                    String str6 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## manageSplashCommand() : split failed ");
                    sb2.append(e.getMessage());
                    Log.e(str6, sb2.toString(), e);
                    strArr = null;
                }
                if (strArr == null || strArr.length == 0) {
                    return false;
                }
                String str7 = strArr[0];
                if (!TextUtils.equals(str7, SlashCommand.CHANGE_DISPLAY_NAME.getCommand())) {
                    if (TextUtils.equals(str7, SlashCommand.TOPIC.getCommand())) {
                        String trim = str.substring(SlashCommand.TOPIC.getCommand().length()).trim();
                        if (trim.length() > 0) {
                            room.updateTopic(trim, r2);
                        }
                        z3 = false;
                        z = z3;
                        z2 = true;
                        if (z2) {
                            new Builder(vectorRoomActivity).setTitle((int) R.string.command_error).setMessage((CharSequence) vectorRoomActivity.getString(R.string.unrecognized_command, new Object[]{str7})).setPositiveButton((int) R.string.ok, (OnClickListener) null).show();
                        } else if (!z) {
                            new Builder(vectorRoomActivity).setTitle((int) R.string.command_error).setMessage((CharSequence) vectorRoomActivity.getString(R.string.command_problem_with_parameters, new Object[]{str7})).setPositiveButton((int) R.string.ok, (OnClickListener) null).show();
                        } else {
                            z4 = z2;
                        }
                        return z4;
                    }
                    if (TextUtils.equals(str7, SlashCommand.EMOTE.getCommand())) {
                        String trim2 = str.substring(SlashCommand.EMOTE.getCommand().length()).trim();
                        if (str2 == null || str2.length() <= SlashCommand.EMOTE.getCommand().length()) {
                            vectorRoomActivity.sendEmote(trim2, str2, str3);
                        } else {
                            vectorRoomActivity.sendEmote(trim2, str2.substring(SlashCommand.EMOTE.getCommand().length()), str3);
                        }
                    } else if (TextUtils.equals(str7, SlashCommand.JOIN_ROOM.getCommand())) {
                        String trim3 = str.substring(SlashCommand.JOIN_ROOM.getCommand().length()).trim();
                        if (trim3.length() > 0) {
                            mXSession.joinRoom(trim3, new SimpleApiCallback<String>(vectorRoomActivity) {
                                public void onSuccess(String str) {
                                    if (str != null) {
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", mXSession.getMyUserId());
                                        hashMap.put("EXTRA_ROOM_ID", str);
                                        CommonActivityUtils.goToRoomPage(vectorRoomActivity, mXSession, hashMap);
                                    }
                                }

                                public void onMatrixError(MatrixError matrixError) {
                                    if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                                        vectorRoomActivity.getConsentNotGivenHelper().displayDialog(matrixError);
                                    } else {
                                        Toast.makeText(vectorRoomActivity, matrixError.error, 1).show();
                                    }
                                }
                            });
                        }
                        z3 = false;
                        z = z3;
                        z2 = true;
                        if (z2) {
                        }
                        return z4;
                    } else if (TextUtils.equals(str7, SlashCommand.PART.getCommand())) {
                        String trim4 = str.substring(SlashCommand.PART.getCommand().length()).trim();
                        if (trim4.length() > 0) {
                            Iterator it = mXSession.getDataHandler().getStore().getRooms().iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    room2 = null;
                                    break;
                                }
                                room2 = (Room) it.next();
                                RoomState state = room2.getState();
                                if (state != null && (TextUtils.equals(state.getCanonicalAlias(), trim4) || state.getAliases().indexOf(trim4) >= 0)) {
                                    break;
                                }
                            }
                            if (room2 != null) {
                                room2.leave(r2);
                            }
                        }
                        z3 = false;
                        z = z3;
                        z2 = true;
                        if (z2) {
                        }
                        return z4;
                    } else {
                        if (TextUtils.equals(str7, SlashCommand.INVITE.getCommand())) {
                            if (!DinsicUtils.isExternalTchapSession(mXSession) && !room.isDirect()) {
                                if (strArr.length >= 2) {
                                    room.invite(strArr[1], (ApiCallback<Void>) r2);
                                }
                            }
                        } else if (TextUtils.equals(str7, SlashCommand.KICK_USER.getCommand())) {
                            if (strArr.length >= 2) {
                                String str8 = strArr[1];
                                room.kick(str8, str.substring(SlashCommand.BAN_USER.getCommand().length() + 1 + str8.length()).trim(), r2);
                            }
                        } else if (TextUtils.equals(str7, SlashCommand.BAN_USER.getCommand())) {
                            String trim5 = str.substring(SlashCommand.BAN_USER.getCommand().length()).trim();
                            String str9 = trim5.split(" ")[0];
                            String trim6 = trim5.substring(str9.length()).trim();
                            if (str9.length() > 0) {
                                room.ban(str9, trim6, r2);
                            }
                            z3 = false;
                            z = z3;
                            z2 = true;
                            if (z2) {
                            }
                            return z4;
                        } else if (TextUtils.equals(str7, SlashCommand.UNBAN_USER.getCommand())) {
                            if (strArr.length >= 2) {
                                room.unban(strArr[1], r2);
                            }
                        } else if (TextUtils.equals(str7, SlashCommand.SET_USER_POWER_LEVEL.getCommand())) {
                            if (strArr.length >= 3) {
                                String str10 = strArr[1];
                                String str11 = strArr[2];
                                try {
                                    if (str10.length() > 0 && str11.length() > 0) {
                                        room.updateUserPowerLevels(str10, Integer.parseInt(str11), r2);
                                    }
                                } catch (Exception e2) {
                                    String str12 = LOG_TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("mRoom.updateUserPowerLevels ");
                                    sb3.append(e2.getMessage());
                                    Log.e(str12, sb3.toString(), e2);
                                }
                            }
                        } else if (TextUtils.equals(str7, SlashCommand.RESET_USER_POWER_LEVEL.getCommand())) {
                            if (strArr.length >= 2) {
                                room.updateUserPowerLevels(strArr[1], 0, r2);
                            }
                        } else if (TextUtils.equals(str7, SlashCommand.MARKDOWN.getCommand())) {
                            if (strArr.length >= 2) {
                                if (TextUtils.equals(strArr[1], DebugKt.DEBUG_PROPERTY_VALUE_ON)) {
                                    PreferencesManager.setMarkdownEnabled(VectorApp.getInstance(), true);
                                    Toast.makeText(vectorRoomActivity, R.string.markdown_has_been_enabled, 0).show();
                                } else if (TextUtils.equals(strArr[1], DebugKt.DEBUG_PROPERTY_VALUE_OFF)) {
                                    PreferencesManager.setMarkdownEnabled(VectorApp.getInstance(), false);
                                    Toast.makeText(vectorRoomActivity, R.string.markdown_has_been_disabled, 0).show();
                                }
                            }
                        } else if (TextUtils.equals(str7, SlashCommand.CLEAR_SCALAR_TOKEN.getCommand())) {
                            WidgetsManager.clearScalarToken(vectorRoomActivity, mXSession);
                            Toast.makeText(vectorRoomActivity, "Scalar token cleared", 0).show();
                        }
                        z2 = true;
                        z = false;
                        if (z2) {
                        }
                        return z4;
                    }
                    z2 = true;
                    z = true;
                    if (z2) {
                    }
                    return z4;
                    z3 = true;
                    z = z3;
                    z2 = true;
                    if (z2) {
                    }
                    return z4;
                }
                z2 = false;
                z = false;
                if (z2) {
                }
                return z4;
            }
        }
        z4 = false;
        return z4;
    }
}
