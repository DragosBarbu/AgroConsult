package com.agro.gusutri.agroconsult.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by dragos on 4/10/15.
 */
public class Field implements Parcelable{

    private int fieldID;
    private User user;
    private double perimeter, area;
    private String sirupCode;
    private Crop crop;
    private ArrayList<Location> locations;

    public Field(int fieldID, User user, double perimeter, double area, String sirupCode, Crop crop,ArrayList<Location> locations) {
        this.fieldID = fieldID;
        this.user = user;
        this.perimeter = perimeter;
        this.area = area;
        this.sirupCode = sirupCode;
        this.crop = crop;
        this.locations=locations;
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

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    protected Field(Parcel in) {
        fieldID = in.readInt();
        user = (User) in.readValue(User.class.getClassLoader());
        perimeter = in.readDouble();
        area = in.readDouble();
        sirupCode = in.readString();
        crop = (Crop) in.readValue(Crop.class.getClassLoader());
        if (in.readByte() == 0x01) {
            locations = new ArrayList<Location>();
            in.readList(locations, Location.class.getClassLoader());
        } else {
            locations = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fieldID);
        dest.writeValue(user);
        dest.writeDouble(perimeter);
        dest.writeDouble(area);
        dest.writeString(sirupCode);
        dest.writeValue(crop);
        if (locations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(locations);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };

}
