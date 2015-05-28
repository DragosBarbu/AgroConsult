package com.agro.gusutri.agroconsult.Service;

import android.location.LocationManager;
import android.util.Log;

import com.agro.gusutri.agroconsult.model.Field;
import com.agro.gusutri.agroconsult.model.Location;
import com.agro.gusutri.agroconsult.model.Solution;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

    public float calculateArea(List<Location> points) {

        int nrOfPoints = points.size();
        float sum_but_no_result = 0;

        for (int i = 0; i < (nrOfPoints - 1); i++) {
            sum_but_no_result += points.get(i).getLatitude() * points.get(i + 1).getLongitude() + points.get(i).getLongitude() * points.get(i + 1).getLatitude();
        }
        sum_but_no_result += points.get(nrOfPoints - 1).getLatitude() * points.get(0).getLongitude() + points.get(nrOfPoints - 1).getLongitude() * points.get(0).getLatitude();

        float sum = Math.abs(sum_but_no_result) / 2.0f;
        return sum;
    }

    public double calculatePerimeter(List<Location> locations) {

        double perimeter = 0;
        for (int i = 1; i < locations.size(); i++) {
            perimeter += distanceBetween(locations.get(i - 1), locations.get(i));
        }
        perimeter += distanceBetween(locations.get(locations.size() - 1), locations.get(0));
        return perimeter;

    }

    private float distanceBetween(Location l1, Location l2) {

        android.location.Location loc1 = new android.location.Location(LocationManager.GPS_PROVIDER);
        android.location.Location loc2 = new android.location.Location(LocationManager.GPS_PROVIDER);

        loc1.setLatitude(l1.getLatitude());
        loc1.setLongitude(l1.getLongitude());

        loc2.setLatitude(l2.getLatitude());
        loc2.setLongitude(l2.getLongitude());
        return loc1.distanceTo(loc2);
    }

    public Field getFieldOfLocation(ArrayList<Field> fields, LatLng location) {
        for (Field f : fields) {
            ArrayList<LatLng> vertices = new ArrayList<>();
            for (Location l : f.getLocations()) {
                vertices.add(new LatLng(l.getLatitude(), l.getLongitude()));
            }
            if (isPointInPolygon(location, vertices))
                return f;
        }

        return null;
    }

    private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng pointA, LatLng pointB) {

        double aY = pointA.latitude;
        double bY = pointB.latitude;
        double aX = pointA.longitude;
        double bX = pointB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX);               // Rise over run
        double bee = (-aX) * m + aY;                // y = mx + b
        double x = (pY - bee) / m;

        return x > pX;
    }

    public String getSolutionsAsString(ArrayList<Solution> solutions){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=1;i<=solutions.size();i++){
            Solution s= solutions.get(i-1);
            stringBuilder.append(i+". "+s.getName()+": "+s.getDetails()+"\n");
        }
        return stringBuilder.toString();
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

        public HttpResponse requestPOST(String url, String content)
                throws ClientProtocolException, IOException, IllegalStateException,
                JSONException {

            synchronized (instance) {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);

                byte[] bytes = content.getBytes("UTF-8");
                post.setEntity(new ByteArrayEntity(bytes));
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
