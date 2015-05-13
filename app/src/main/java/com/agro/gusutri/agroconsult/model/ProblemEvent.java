package com.agro.gusutri.agroconsult.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by dragos on 5/7/15.
 */
public class ProblemEvent {

    private Bitmap image;
    private Date date;
    private String details, categoryName;
    private Field field;
    private LatLng location;
    private double radius;

    public ProblemEvent(Bitmap image, Date date, String details, Field field, String categoryName, LatLng location, Double radius) {
        this.image = image;
        this.date = date;
        this.details = details;
        this.field = field;
        this.categoryName = categoryName;
        this.location = location;
        this.radius = radius;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
