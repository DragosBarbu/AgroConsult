package com.agro.gusutri.agroconsult.model;

import java.util.Date;

/**
 * Created by dragos on 5/13/15.
 */
public class Crop {

    private int cropID;
    private String cropStage, cropType;
    private String datePlanted, dateHarvasted;

    public Crop(int cropID, String cropStage, String datePlanted, String cropType, String dateHarvasted) {
        this.cropID = cropID;
        this.cropStage = cropStage;
        this.datePlanted = datePlanted;
        this.cropType = cropType;
        this.dateHarvasted = dateHarvasted;
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
}
