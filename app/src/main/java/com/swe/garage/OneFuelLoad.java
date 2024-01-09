package com.swe.garage;

import android.database.Cursor;

public class OneFuelLoad {
    private  int id = -1;
    private double volumen;
    private double preis;
    private String time_data;
    private byte[] placeholder_picture;
    private int carID;


    OneFuelLoad() {}

    OneFuelLoad(double volumen, double preis, String time_data, byte[] placeholder_picture, int carID){
        this.volumen = volumen;
        this.preis = preis;
        this.time_data = time_data;
        this.placeholder_picture = placeholder_picture;
        this.carID = carID;
    }

    OneFuelLoad(int id ,double volumen, double preis, String time_data, byte[] placeholder_picture, int carID){
        this.id = id;
        this.volumen = volumen;
        this.preis = preis;
        this.time_data = time_data;
        this.placeholder_picture = placeholder_picture;
        this.carID = carID;
    }

    OneFuelLoad(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.volumen = cursor.getDouble(1);
        this.preis = cursor.getDouble(2);
        this.time_data = cursor.getString(3);
        this.placeholder_picture = cursor.getBlob(4);
        this.carID = cursor.getInt(5);
    }

    @Override
    public String toString()
    {
        return + volumen + " l(kWh) | " + preis + "€" + "\n" + time_data;
    }

    @Override
    public boolean equals(Object obj)
    {
        OneFuelLoad fuel = (OneFuelLoad)obj;
        if(this.getPreis()!=fuel.getPreis())
            return false;
        if(this.getPlaceholder_picture()!=fuel.getPlaceholder_picture())
            return false;
        if(this.getId()!=fuel.getId())
            return false;
        if(this.getVolumen()!=fuel.getVolumen())
            return false;
        if(this.getCarID()!=fuel.getCarID())
            return false;
        if(!this.time_data.equals(fuel.time_data))
            return false;
        return true;
    }

    public double getVolumen() {
        return volumen;
    }

    public double getPreis() {
        return preis;
    }

    public String time_data()
    {
        return time_data;
    }

    public byte[] getPlaceholder_picture()
    {
        return placeholder_picture;
    }

    public int getCarID() {
        return carID;
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public void setPlaceholder_picture(byte[] placeholder_picture) {
        this.placeholder_picture = placeholder_picture;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public void setTime_data(String time_data) {
        this.time_data = time_data;
    }
}
