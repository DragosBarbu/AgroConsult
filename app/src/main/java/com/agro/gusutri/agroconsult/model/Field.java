package com.agro.gusutri.agroconsult.model;

/**
 * Created by dragos on 4/10/15.
 */
public class Field {

    private long id;
    private String name;
    private int perimeter,surface;

    public Field(long id, String name, int surface, int perimeter) {
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

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }
}
