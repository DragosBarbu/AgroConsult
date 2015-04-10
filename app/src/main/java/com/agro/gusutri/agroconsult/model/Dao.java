package com.agro.gusutri.agroconsult.model;

/**
 * Created by dragos on 4/7/15.
 */
public class Dao {
    private static Dao ourInstance = new Dao();

    public static final String USER="user";

    public static Dao getInstance() {
        return ourInstance;
    }

    private Dao() {
    }

    //Tries to retrieve the user from database else it returns an user with id=-1
    public User getExistingUser(String email){
        //TODO: get user from DB
        User u=null;

        if(email.equals("a@a.com")) {
            u = new User(5, "john", "a@a.com", "12345");
        }
        else
            u= new User(-1,"N/A","N/A","N/A");

        return  u;
    }

    public void createUser(User user){
        //TODO: add user to DB
    }
}
