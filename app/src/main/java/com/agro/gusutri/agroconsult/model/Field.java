package com.agro.gusutri.agroconsult.model;

/**
 * Created by dragos on 4/10/15.
 */
public class Field {

    private int fieldID;
    private User user;
    private double perimeter, area;
    private String sirupCode;
    private Crop crop;

    public Field(int fieldID, User user, double perimeter, double area, String sirupCode, Crop crop) {
        this.fieldID = fieldID;
        this.user = user;
        this.perimeter = perimeter;
        this.area = area;
        this.sirupCode = sirupCode;
        this.crop = crop;
    }

    public int getFieldID() {
        return fieldID;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getSirupCode() {
        return sirupCode;
    }

    public void setSirupCode(String sirupCode) {
        this.sirupCode = sirupCode;
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }
}
