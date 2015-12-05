package ru.bmstu.iu6.hackatonmobile.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.squareup.otto.Bus;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mikrut on 04.12.15.
 */
public class JSONGetter extends AsyncTask<Void, Void, JSONObject> {
    private static final String baseurl = "http://sitenotifier.herokuapp.com/";
    private String suburl, method;
    private Bundle params;

    private Bus publisher;

    public JSONGetter(String suburl, String method, Bundle params, Bus publisher) {
        this.suburl = suburl;
        this.method = method;
        this.params = params;
        this.publisher = publisher;
    }

    @NonNull
    public static JSONObject getAPIResponse(String suburl, String method, Bundle keys) {
        JSONObject json = new JSONObject();
        String result = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(baseurl + suburl);

            for(String key: keys.keySet()) {
                uriBuilder.addParameter(key, keys.get(key).toString());
            }
            URL url = uriBuilder.build().toURL();

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            StringBuilder responseBuilder = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
            }
            rd.close();
            result = responseBuilder.toString();
            json = new JSONObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected JSONObject doInBackground(Void... nothing) {
        JSONObject response = getAPIResponse(suburl, method, params);
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (publisher != null)
            publisher.post(result);
    }

}
