package com.agro.gusutri.agroconsult.model;

/**
 * Created by dragos on 5/21/15.
 */
public class Solution {
    private String name;
    private String details;

    public Solution(String name, String details) {
        this.name = name;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
