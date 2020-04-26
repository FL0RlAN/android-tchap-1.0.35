package im.vector.util;

import android.content.Context;
import fr.gouv.tchap.a.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.matrix.androidsdk.call.IMXCall;

public class CallUtilities {
    private static SimpleDateFormat mHourMinSecFormat;
    private static SimpleDateFormat mMinSecFormat;

    private static String formatSecondsToHMS(long j) {
        if (mHourMinSecFormat == null) {
            mHourMinSecFormat = new SimpleDateFormat("HH:mm:ss");
            String str = "UTC";
            mHourMinSecFormat.setTimeZone(TimeZone.getTimeZone(str));
            mMinSecFormat = new SimpleDateFormat("mm:ss");
            mMinSecFormat.setTimeZone(TimeZone.getTimeZone(str));
        }
        if (j < 3600) {
            return mMinSecFormat.format(new Date(j * 1000));
        }
        return mHourMinSecFormat.format(new Date(j * 1000));
    }

    public static String getCallStatus(Context context, IMXCall iMXCall) {
        if (iMXCall == null) {
            return null;
        }
        String callState = iMXCall.getCallState();
        char c = 65535;
        switch (callState.hashCode()) {
            case -1444885671:
                if (callState.equals(IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA)) {
                    c = 3;
                    break;
                }
                break;
            case -215535408:
                if (callState.equals(IMXCall.CALL_STATE_WAIT_CREATE_OFFER)) {
                    c = 7;
                    break;
                }
                break;
            case 183694318:
                if (callState.equals(IMXCall.CALL_STATE_CREATE_ANSWER)) {
                    c = 6;
                    break;
                }
                break;
            case 358221275:
                if (callState.equals(IMXCall.CALL_STATE_INVITE_SENT)) {
                    c = 4;
                    break;
                }
                break;
            case 946025035:
                if (callState.equals(IMXCall.CALL_STATE_CONNECTING)) {
                    c = 5;
                    break;
                }
                break;
            case 1322015527:
                if (callState.equals(IMXCall.CALL_STATE_ENDED)) {
                    c = 10;
                    break;
                }
                break;
            case 1333750288:
                if (callState.equals(IMXCall.CALL_STATE_READY)) {
                    c = 2;
                    break;
                }
                break;
            case 1700515443:
                if (callState.equals(IMXCall.CALL_STATE_CREATING_CALL_VIEW)) {
                    c = 1;
                    break;
                }
                break;
            case 1781900309:
                if (callState.equals(IMXCall.CALL_STATE_CREATED)) {
                    c = 0;
                    break;
                }
                break;
            case 1831632118:
                if (callState.equals(IMXCall.CALL_STATE_CONNECTED)) {
                    c = 9;
                    break;
                }
                break;
            case 1960371423:
                if (callState.equals(IMXCall.CALL_STATE_RINGING)) {
                    c = 8;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
                if (iMXCall.isIncoming()) {
                    if (iMXCall.isVideo()) {
                        return context.getString(R.string.incoming_video_call);
                    }
                    return context.getString(R.string.incoming_voice_call);
                }
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            case 8:
                if (!iMXCall.isIncoming()) {
                    return context.getString(R.string.call_ring);
                }
                if (iMXCall.isVideo()) {
                    return context.getString(R.string.incoming_video_call);
                }
                return context.getString(R.string.incoming_voice_call);
            case 9:
                long callElapsedTime = iMXCall.getCallElapsedTime();
                if (callElapsedTime < 0) {
                    return context.getString(R.string.call_connected);
                }
                return formatSecondsToHMS(callElapsedTime);
            case 10:
                return context.getString(R.string.call_ended);
            default:
                return null;
        }
        return context.getString(R.string.call_connecting);
    }
}
