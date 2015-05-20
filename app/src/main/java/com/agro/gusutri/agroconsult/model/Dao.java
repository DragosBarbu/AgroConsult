package com.agro.gusutri.agroconsult.model;

import com.agro.gusutri.agroconsult.Service.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by dragos on 4/7/15.
 */
public class Dao {
    public static final String FIELD = "field";
    private static Dao ourInstance = new Dao();
    private Service.HTTPRequestHelper httpRequestHelper;


    public static final String USER = "user";
    public static final String EMAIL = "email";
    public static final String LOCATIONS = "locations";
    public static final String SERVER_URL = "http://46.101.190.175:8080/agroconsult/";

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
            HttpResponse response = httpRequestHelper.requestGET(url);

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
        String url = SERVER_URL + "register?email=" + user.getEmail() + "&pass=" + password + "&username=" + user.getName();
        try {
            HttpResponse response = httpRequestHelper.requestPOST(url);
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

    public ArrayList<Field> getFields(int userID) {
        ArrayList<Field> fields = new ArrayList<>();

        String url = SERVER_URL + "fieldlist?userid=" + userID;

        try {
            HttpResponse response = httpRequestHelper.requestGET(url);

            switch (response.getStatusLine().getStatusCode()) {

                case 200:
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Field>>() {
                    }.getType();
                    fields = gson.fromJson(result, listType);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return fields;
    }


    public String registerField(Field field) {
        String success = "q";
        String url = SERVER_URL + "registerfield";
        Gson gson= new Gson();
        String json=gson.toJson(field);


        try {
            HttpResponse response = httpRequestHelper.requestPOST(url,json);
            switch (response.getStatusLine().getStatusCode()) {

                case 200:
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    success = result;
                    break;
                default:
                    success = "error";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }
}
