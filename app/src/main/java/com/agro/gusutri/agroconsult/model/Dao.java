package com.agro.gusutri.agroconsult.model;

import com.agro.gusutri.agroconsult.Service.Service;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dragos on 4/7/15.
 */
public class Dao {
    private static Dao ourInstance = new Dao();
    private Service.HTTPRequestHelper httpRequestHelper;


    public static final String USER = "user";
    public static final String EMAIL = "email";
    public static final String SERVER_URL = "http://46.101.148.54:8080/agroconsult/";

    public static Dao getInstance() {
        return ourInstance;
    }

    private Dao() {
        httpRequestHelper = Service.HTTPRequestHelper.getInstance();
    }

    //Tries to retrieve the user from database else it returns an user with id=-1
    public User getExistingUser(String email, String password) {
        User user = null;
        String url = SERVER_URL + "login?email=" + email + "&pass=" + password;

        try {
            HttpResponse response = httpRequestHelper.request(url);

            switch (response.getStatusLine().getStatusCode()) {

                case 200:
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    Gson gson = new Gson();
                    user = gson.fromJson(result, User.class);
                    break;
                case 404://user not existing
                    user = new User(-404, "N/A", "N/A");
                    break;
                case 403://wrong password
                    user = new User(-401, "N/A", "N/A");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User registerUser(User user, String password) {
        String url = SERVER_URL+"register?email=" + user.getEmail()+ "&pass=" + password+"&username="+user.getName();
        try {
            HttpResponse response = httpRequestHelper.request(url);
            switch (response.getStatusLine().getStatusCode()) {

                case 200:
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    //TODO setID!
                    user.setId(1);
                    break;
                case 500://user not registered
                    user = new User(-1, "N/A", "N/A");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
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


}
