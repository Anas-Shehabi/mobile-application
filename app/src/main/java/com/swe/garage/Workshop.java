package com.swe.garage;

import android.database.Cursor;

import java.util.Arrays;

public class Workshop {
    private int id = -1;
    private String description;
    private String time_data;
    private int carID;
    private byte[] placeholder_picture = null;

    public Workshop(String description, String time_data, int carID, byte[] placeholder_picture) {
        this.description = description;
        this.time_data = time_data;
        this.carID = carID;
        this.placeholder_picture = placeholder_picture;
    }

    public Workshop(int ID, String description, String time_data, byte[] placeholder_picture, int carID) {
        this.setId(ID);
        this.description = description;
        this.time_data = time_data;
        this.carID = carID;
        this.placeholder_picture = placeholder_picture;
    }

    public Workshop(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.description = cursor.getString(1);
        this.time_data = cursor.getString(2);
        this.placeholder_picture = cursor.getBlob(3);
        this.carID = cursor.getInt(4);
    }

    @Override
    public boolean equals(Object obj){
        Workshop workshop = (Workshop) obj;
        if(this.carID!=workshop.carID)
            return false;
        if(!this.description.equals(workshop.description))
            return false;
        if(this.id!=workshop.id)
            return false;
        if(!this.time_data.equals(workshop.time_data))
            return false;
        if(!Arrays.equals(this.placeholder_picture, workshop.placeholder_picture))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return time_data + "\n" + description;
    }

    public int getId() {
        return id;
    }

    public String getDescription(){
        return description;
    }

    public String getTime_data() {
        return time_data;
    }

    public int getCarID() {
        return carID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime_data(String time_data) {
        this.time_data = time_data;
    }

    public void setDescription(String description) {
        this.description
                = description;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public byte[] getPlaceholder_picture(){return placeholder_picture;}

    public void setPlaceholder_picture(byte[] pb) {placeholder_picture = pb;}
}
