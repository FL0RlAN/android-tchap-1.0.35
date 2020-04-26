package org.piwik.sdk.dispatcher;

import android.text.TextUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

public class PacketFactory {
    private static final String LOGGER_TAG = "PIWIK:PacketFactory";
    public static final int PAGE_SIZE = 20;
    private final URL mApiUrl;

    public PacketFactory(URL url) {
        this.mApiUrl = url;
    }

    public List<Packet> buildPackets(List<Event> list) {
        Packet packet;
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        if (list.size() == 1) {
            Packet buildPacketForGet = buildPacketForGet((Event) list.get(0));
            if (buildPacketForGet == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(buildPacketForGet);
        }
        double size = (double) list.size();
        Double.isNaN(size);
        ArrayList arrayList = new ArrayList((int) Math.ceil((size * 1.0d) / 20.0d));
        int i = 0;
        while (i < list.size()) {
            int i2 = i + 20;
            List subList = list.subList(i, Math.min(i2, list.size()));
            if (subList.size() == 1) {
                packet = buildPacketForGet((Event) subList.get(0));
            } else {
                packet = buildPacketForPost(subList);
            }
            if (packet != null) {
                arrayList.add(packet);
            }
            i = i2;
        }
        return arrayList;
    }

    private Packet buildPacketForPost(List<Event> list) {
        if (list.isEmpty()) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            for (Event encodedQuery : list) {
                jSONArray.put(encodedQuery.getEncodedQuery());
            }
            jSONObject.put("requests", jSONArray);
            return new Packet(this.mApiUrl, jSONObject, list.size());
        } catch (JSONException e) {
            Timber.tag(LOGGER_TAG).w(e, "Cannot create json object:\n%s", TextUtils.join(", ", list));
            return null;
        }
    }

    private Packet buildPacketForGet(Event event) {
        if (event.getEncodedQuery().isEmpty()) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mApiUrl.toString());
            sb.append(event);
            return new Packet(new URL(sb.toString()));
        } catch (MalformedURLException e) {
            Timber.tag(LOGGER_TAG).w(e, null, new Object[0]);
            return null;
        }
    }
}
