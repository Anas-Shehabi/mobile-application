package com.swe.garage;

import android.database.Cursor;

public class Route {
    private int id = -1;
    private int km;
    private int tank;
    private String route_type;
    private String time_data;
    private int carID;
    private String travel_type;
    private double CO2_ausstoß;
    private int länge_in_km;
    private double kraftstoffverbrauch;


    public Route(int km, int tank, String route_type, String time_data, int carID,String travel_type, double co2, int länge, double kraftstoffverbrauch) {
        this.km = km;
        this.tank = tank;
        this.route_type = route_type;
        this.time_data = time_data;
        this.carID = carID;
        this.travel_type = travel_type;
        this.CO2_ausstoß = co2;
        this.länge_in_km = länge;
        this.kraftstoffverbrauch = kraftstoffverbrauch;
    }

    public Route(int km, int tank, String route_type, String time_data, int carID, int id, String travel_type, double co2, int länge, double kraftstoffverbrauch) {
        this.km = km;
        this.tank = tank;
        this.route_type = route_type;
        this.time_data = time_data;
        this.carID = carID;
        this.id = id;
        this.travel_type = travel_type;
        this.CO2_ausstoß = co2;
        this.länge_in_km = länge;
        this.kraftstoffverbrauch = kraftstoffverbrauch;
    }

    public Route(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.km = cursor.getInt(1);
        this.tank = cursor.getInt(2);
        this.route_type = cursor.getString(3);
        this.time_data = cursor.getString(4);
        this.carID = cursor.getInt(5);
        this.travel_type = cursor.getString(6);
        this.CO2_ausstoß = cursor.getDouble(7);
        this.länge_in_km = cursor.getInt(8);
        this.kraftstoffverbrauch = cursor.getDouble(9);
    }

    @Override
    public boolean equals(Object obj)
    {
        Route route = (Route) obj;
        if(this.carID != route.carID)
            return false;
        if(this.id != route.id)
            return false;
        if(this.km != route.km)
            return false;
        if(!this.route_type.equals(route.route_type))
            return false;
        if(this.tank!=route.tank)
            return false;
        if(!this.time_data.equals(route.time_data))
            return false;
        if(!this.travel_type.equals(route.travel_type))
            return false;
        if(this.CO2_ausstoß!=route.CO2_ausstoß)
            return false;
        if(this.länge_in_km != route.länge_in_km)
            return false;
        if(this.kraftstoffverbrauch != route.kraftstoffverbrauch)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return + km + "km\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+ travel_type +"\n"+ time_data;
    }

    public int getId() {
        return id;
    }

    public int getKm() {
        return km;
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

    public void setKm(int km) {
        this.km = km;
    }

    public void setTime_data(String time_data) {
        this.time_data = time_data;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public int getTank() {
        return tank;
    }

    public void setTank(int tank) {
        this.tank = tank;
    }

    public String getRoute_type() {
        return route_type;
    }

    public void setRoute_type(String route_type) {
        this.route_type = route_type;
    }

    public String getTravel_type() {
        return travel_type;
    }

    public void setTravel_type(String travel_type) {
        this.travel_type = travel_type;
    }

    public double getCO2_ausstoß(){return CO2_ausstoß;}

    public void setCO2_ausstoß(double co2){CO2_ausstoß = co2;}

    public int getLänge_in_km(){return länge_in_km;}

    public void setLänge_in_km(int länge){länge_in_km = länge;}

    public double getKraftstoffverbrauch(){return kraftstoffverbrauch;}

    public void setKraftstoffverbrauch(double kraftstoffverbrauch){this.kraftstoffverbrauch = kraftstoffverbrauch;}
}
