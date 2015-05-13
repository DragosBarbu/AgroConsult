package com.agro.gusutri.agroconsult.Service;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by dragos on 5/3/15.
 */
public class Service {

    private static Service instance;

    private Service() {
    }

    public static Service getInstance() {
        if (instance == null)
            instance = new Service();
        return instance;
    }

    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public float calculateArea(List<LatLng> points) {

        int nrOfPoints = points.size();
        float sum_but_no_result = 0;

        for (int i = 0; i < (nrOfPoints - 1); i++) {
            sum_but_no_result += points.get(i).latitude * points.get(i + 1).longitude + points.get(i).longitude * points.get(i + 1).latitude;
        }
        sum_but_no_result += points.get(nrOfPoints - 1).latitude * points.get(0).longitude + points.get(nrOfPoints - 1).longitude * points.get(0).latitude;

        float sum = Math.abs(sum_but_no_result) / 2.0f;
        return sum;
    }


    public static class HTTPRequestHelper {

        private static HTTPRequestHelper instance;

        private HTTPRequestHelper() {
        }

        public static HTTPRequestHelper getInstance() {
            if (instance == null)
                instance = new HTTPRequestHelper();
            return instance;
        }

        public HttpResponse request(String url, JSONObject request)
                throws ClientProtocolException, IOException, IllegalStateException,
                JSONException {

            synchronized (instance) {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                post.setEntity(new StringEntity(request.toString(), "utf-8"));
                HttpResponse response = client.execute(post);
                Log.i("LoginResponse", response.getStatusLine().toString());
                return response;
            }
        }

        public HttpResponse requestPOST(String url)
                throws ClientProtocolException, IOException, IllegalStateException,
                JSONException {

            synchronized (instance) {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                post.addHeader("Cache-Control", "no-cache");
                HttpResponse response = client.execute(post);
                Log.i("LoginResponse", response.getStatusLine().toString());
                return response;
            }
        }

        public HttpResponse requestGET(String url)
                throws ClientProtocolException, IOException, IllegalStateException,
                JSONException {

            synchronized (instance) {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                get.addHeader("Cache-Control", "no-cache");
                HttpResponse response = client.execute(get);
                Log.i("LoginResponse", response.getStatusLine().toString());
                return response;
            }

        }
    }
}
