package com.agro.gusutri.agroconsult.model;

/**
 * Created by dragos on 4/10/15.
 */
public class Field {

    private long id;
    private String name;
    private int perimeter;
    private float surface;

    public Field(long id, String name, float surface, int perimeter) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.perimeter = perimeter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(int perimeter) {
        this.perimeter = perimeter;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }
}
