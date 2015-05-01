package com.agro.gusutri.agroconsult.model;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dragos on 4/7/15.
 */
public class Dao {
    private static Dao ourInstance = new Dao();

    public static final String USER = "user";

    public static Dao getInstance() {
        return ourInstance;
    }

    private Dao() {
    }

    //Tries to retrieve the user from database else it returns an user with id=-1
    public User getExistingUser(String email, String password) {
        //TODO: get user from DB
        User user = null;

        String url = "http://46.101.148.54:8080/agroconsult/login?email="+email+"&pass="+password;

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url);
        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("LoginResponse", response.getStatusLine().toString());

            switch (response.getStatusLine().getStatusCode()) {

                case 200:
                    // Get hold of the response entity
                    HttpEntity entity = response.getEntity();
                    // If the response does not enclose an entity, there is no need
                    // to worry about connection release

                    if (entity != null) {

                        // A Simple JSON Response Read
                        InputStream stream = entity.getContent();
                        String result = convertStreamToString(stream);
                        // now you have the string representation of the HTML request
                        Gson gson = new Gson();
                        user = gson.fromJson(result, User.class);
                        stream.close();
                    }
                    break;
                case 404://user not existing
                    user = new User(-404, "N/A", "N/A");
                    break;
                case 403://wrong password
                    user = new User(-401, "N/A", "N/A");
                    break;

            }


        } catch (Exception e) {
            Log.w("Connection exception", e.getMessage());
        }
        return user;
    }

    public void createUser(User user) {
        //TODO: add user to DB
    }

    public ArrayList<Task> getTasks() {
        //TODO: get task List fromDB
        ArrayList<Task> tasks = new ArrayList<>();

        Task t1 = new Task("Do something", "do it", 0);
        Task t2 = new Task("Dance baby dance", "dance", 1);
        Task t3 = new Task("Are you ready", "Ready?", 2);
        tasks.add(t1);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t2);
        tasks.add(t3);
        return tasks;
    }

    public ArrayList<Field> getFields() {
        //TODO: get Field List fromDB
        ArrayList<Field> fields = new ArrayList<>();
        Field f = new Field(101, "Dragus-1", 250, 10);
        Field f1 = new Field(102, "Dragus-2", 251, 60);
        Field f2 = new Field(103, "Dragus-3", 150, 75);
        Field f3 = new Field(104, "Dragus-4", 345, 40);
        fields.add(f);
        fields.add(f1);
        fields.add(f2);
        fields.add(f3);

        return fields;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
