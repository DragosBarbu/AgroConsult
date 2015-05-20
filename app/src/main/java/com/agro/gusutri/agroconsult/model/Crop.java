package com.agro.gusutri.agroconsult.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by dragos on 5/13/15.
 */
public class Crop implements Parcelable {

    private int cropID;
    private String cropStage, cropType;
    private String datePlanted, dateHarvasted;
    private String previousCropPreferred, previousCropNotIndicated;


    public Crop(int cropID, String cropStage, String datePlanted, String cropType, String dateHarvasted, String previousCropPreferred,
                String previousCropNotIndicated) {
        this.cropID = cropID;
        this.cropStage = cropStage;
        this.datePlanted = datePlanted;
        this.cropType = cropType;
        this.dateHarvasted = dateHarvasted;
        this.previousCropPreferred = previousCropPreferred;
        this.previousCropNotIndicated = previousCropNotIndicated;
    }

    public Crop(String cropStage, String cropType){
        this.cropType = cropType;
        this.cropStage = cropStage;
    }
    public int getCropID() {
        return cropID;
    }

    public void setCropID(int cropID) {
        this.cropID = cropID;
    }

    public String getCropStage() {
        return cropStage;
    }

    public void setCropStage(String cropStage) {
        this.cropStage = cropStage;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getDatePlanted() {
        return datePlanted;
    }

    public void setDatePlanted(String datePlanted) {
        this.datePlanted = datePlanted;
    }

    public String getDateHarvasted() {
        return dateHarvasted;
    }

    public void setDateHarvasted(String dateHarvasted) {
        this.dateHarvasted = dateHarvasted;
    }

    public String getPreviousCropPreferred() {
        return previousCropPreferred;
    }

    public void setPreviousCropPreferred(String previousCropPreferred) {
        this.previousCropPreferred = previousCropPreferred;
    }

    public String getPreviousCropNotIndicated() {
        return previousCropNotIndicated;
    }

    public void setPreviousCropNotIndicated(String previousCropNotIndicated) {
        this.previousCropNotIndicated = previousCropNotIndicated;
    }

    protected Crop(Parcel in) {
        cropID = in.readInt();
        cropStage = in.readString();
        cropType = in.readString();
        datePlanted = in.readString();
        dateHarvasted = in.readString();
        previousCropPreferred = in.readString();
        previousCropNotIndicated = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cropID);
        dest.writeString(cropStage);
        dest.writeString(cropType);
        dest.writeString(datePlanted);
        dest.writeString(dateHarvasted);
        dest.writeString(previousCropPreferred);
        dest.writeString(previousCropNotIndicated);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Crop> CREATOR = new Parcelable.Creator<Crop>() {
        @Override
        public Crop createFromParcel(Parcel in) {
            return new Crop(in);
        }

        @Override
        public Crop[] newArray(int size) {
            return new Crop[size];
        }
    };
}
