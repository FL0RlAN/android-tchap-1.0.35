package org.matrix.androidsdk.rest.model;

import android.text.TextUtils;
import android.util.Patterns;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.rest.model.pid.Invite3Pid;

public class CreateRoomParams {
    public static final String PRESET_PRIVATE_CHAT = "private_chat";
    public static final String PRESET_PUBLIC_CHAT = "public_chat";
    public static final String PRESET_TRUSTED_PRIVATE_CHAT = "trusted_private_chat";
    public Object creation_content;
    @SerializedName("initial_state")
    public List<Event> initialStates;
    @SerializedName("invite_3pid")
    public List<Invite3Pid> invite3pids;
    @SerializedName("invite")
    public List<String> invitedUserIds;
    @SerializedName("is_direct")
    public Boolean isDirect;
    public String name;
    @SerializedName("power_level_content_override")
    public Map<String, Object> powerLevelContentOverride;
    public String preset;
    @SerializedName("room_alias_name")
    public String roomAliasName;
    public String topic;
    public String visibility;

    public void addCryptoAlgorithm(String str) {
        if (!TextUtils.isEmpty(str)) {
            Event event = new Event();
            event.type = "m.room.encryption";
            HashMap hashMap = new HashMap();
            hashMap.put(CryptoRoomEntityFields.ALGORITHM, str);
            event.updateContent(JsonUtils.getGson(false).toJsonTree(hashMap));
            event.stateKey = "";
            List<Event> list = this.initialStates;
            if (list == null) {
                this.initialStates = Arrays.asList(new Event[]{event});
                return;
            }
            list.add(event);
        }
    }

    public void setHistoryVisibility(String str) {
        List<Event> list = this.initialStates;
        String str2 = Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY;
        if (list != null && !list.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (Event event : this.initialStates) {
                if (!event.type.equals(str2)) {
                    arrayList.add(event);
                }
            }
            this.initialStates = arrayList;
        }
        if (!TextUtils.isEmpty(str)) {
            Event event2 = new Event();
            event2.type = str2;
            HashMap hashMap = new HashMap();
            hashMap.put("history_visibility", str);
            event2.updateContent(JsonUtils.getGson(false).toJsonTree(hashMap));
            event2.stateKey = "";
            List<Event> list2 = this.initialStates;
            if (list2 == null) {
                this.initialStates = Arrays.asList(new Event[]{event2});
                return;
            }
            list2.add(event2);
        }
    }

    public void setDirectMessage() {
        this.preset = PRESET_TRUSTED_PRIVATE_CHAT;
        this.isDirect = Boolean.valueOf(true);
    }

    private int getInviteCount() {
        List<String> list = this.invitedUserIds;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    private int getInvite3PidCount() {
        List<Invite3Pid> list = this.invite3pids;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public boolean isDirect() {
        if (TextUtils.equals(this.preset, PRESET_TRUSTED_PRIVATE_CHAT)) {
            Boolean bool = this.isDirect;
            if (bool != null && bool.booleanValue() && (1 == getInviteCount() || 1 == getInvite3PidCount())) {
                return true;
            }
        }
        return false;
    }

    public String getFirstInvitedUserId() {
        if (getInviteCount() != 0) {
            return (String) this.invitedUserIds.get(0);
        }
        if (getInvite3PidCount() != 0) {
            return ((Invite3Pid) this.invite3pids.get(0)).address;
        }
        return null;
    }

    public void addParticipantIds(HomeServerConnectionConfig homeServerConnectionConfig, List<String> list) {
        for (String str : list) {
            if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                if (this.invite3pids == null) {
                    this.invite3pids = new ArrayList();
                }
                Invite3Pid invite3Pid = new Invite3Pid();
                invite3Pid.id_server = homeServerConnectionConfig.getIdentityServerUri().getHost();
                invite3Pid.medium = "email";
                invite3Pid.address = str;
                this.invite3pids.add(invite3Pid);
            } else if (MXPatterns.isUserId(str) && !TextUtils.equals(homeServerConnectionConfig.getCredentials().userId, str)) {
                if (this.invitedUserIds == null) {
                    this.invitedUserIds = new ArrayList();
                }
                this.invitedUserIds.add(str);
            }
        }
    }
}
