package org.matrix.androidsdk.rest.client;

import android.os.AsyncTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.Log;

public class UrlPostTask extends AsyncTask<String, Void, String> {
    private static final String LOG_TAG = "UrlPostTask";
    private IPostTaskListener mListener;

    public interface IPostTaskListener {
        void onError(String str);

        void onSucceed(JsonObject jsonObject);
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... strArr) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
            if (RestClient.getUserAgent() != null) {
                httpURLConnection.setRequestProperty("User-Agent", RestClient.getUserAgent());
            }
            httpURLConnection.setRequestMethod("POST");
            return convertStreamToString(new BufferedInputStream(httpURLConnection.getInputStream()));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void setListener(IPostTaskListener iPostTaskListener) {
        this.mListener = iPostTaskListener;
    }

    private static String convertStreamToString(InputStream inputStream) {
        StringBuilder sb;
        String str = "convertStreamToString finally failed ";
        String str2 = LOG_TAG;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb2 = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(readLine);
                    sb3.append("\n");
                    sb2.append(sb3.toString());
                } else {
                    try {
                        break;
                    } catch (Exception e) {
                        e = e;
                        sb = new StringBuilder();
                    }
                }
            } catch (Exception e2) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("convertStreamToString ");
                sb4.append(e2.getMessage());
                Log.e(str2, sb4.toString(), e2);
                try {
                } catch (Exception e3) {
                    e = e3;
                    sb = new StringBuilder();
                }
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e4) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str);
                    sb5.append(e4.getMessage());
                    Log.e(str2, sb5.toString(), e4);
                }
            }
        }
        return sb2.toString();
        sb.append(str);
        sb.append(e.getMessage());
        Log.e(str2, sb.toString(), e);
        return sb2.toString();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String str) {
        JsonObject jsonObject;
        StringBuilder sb = new StringBuilder();
        sb.append("onPostExecute ");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = LOG_TAG;
        Log.d(str2, sb2);
        try {
            jsonObject = new JsonParser().parse(str).getAsJsonObject();
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## onPostExecute() failed");
            sb3.append(e.getMessage());
            Log.e(str2, sb3.toString(), e);
            jsonObject = null;
        }
        IPostTaskListener iPostTaskListener = this.mListener;
        if (iPostTaskListener == null) {
            return;
        }
        if (jsonObject != null) {
            iPostTaskListener.onSucceed(jsonObject);
        } else {
            iPostTaskListener.onError(str);
        }
    }
}
