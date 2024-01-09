package com.swe.garage;

import android.content.Intent;

/**
 * Operations for the Intent with the Auto Class
 */
public class IntentHelperSendReceivData {

    /**
     * Modifies the Intent with the car which is needed in the next Activity
     * @param i the current Intent
     * @param auto the Object of typ Auto which is needed in the other Activity
     */
    void prepareIntentForSendingAuto(Intent i, Auto auto)
    {
        i.putExtra("id", auto.getId());
        i.putExtra("name", auto.getAnzeigename());
        i.putExtra("inner", auto.getKraftstoff_inner());
        i.putExtra("ausser", auto.getKraftstoff_ausser());
        i.putExtra("kombi", auto.getKraftstoff_komb());
        i.putExtra("km", auto.getKilometerstand());
        i.putExtra("tankStand", auto.getTankstand());
        i.putExtra("tankVolum", auto.getTankvolumen());
        i.putExtra("co2", auto.getCo2_aus());
    }

    /**
     * Creates a object of type Auto which was given to the Intent by the "prepareIntentForSending" method
     * @param receivedIntent the current Intent
     * @return the Auto with the received data
     */
    Auto receivIntentAuto(Intent receivedIntent)
    {
        Auto auto = new Auto();
        auto.setId(receivedIntent.getIntExtra("id", -1));
        auto.setAnzeigename(receivedIntent.getStringExtra("name"));
        auto.setKraftstoff_inner(receivedIntent.getDoubleExtra("inner", 0));
        auto.setKraftstoff_ausser(receivedIntent.getDoubleExtra("ausser", 0));
        auto.setKraftstoff_komb(receivedIntent.getDoubleExtra("kombi", 0));
        auto.setKilometerstand(receivedIntent.getIntExtra("km", 0));
        auto.setTankstand(receivedIntent.getIntExtra("tankStand", 0));
        auto.setTankvolumen(receivedIntent.getIntExtra("tankVolum", 0));
        auto.setCo2_aus(receivedIntent.getIntExtra("co2", 0));
        return auto;
    }


    void prepareIntentForSendingFuel(Intent i, OneFuelLoad fuelLoad)
    {
        i.putExtra("idFuel", fuelLoad.getId());
        i.putExtra("volumenFuel", fuelLoad.getVolumen());
        i.putExtra("preisFuel", fuelLoad.getPreis());
        i.putExtra("time_dataFuel",fuelLoad.time_data());
        i.putExtra("pictureFuel",fuelLoad.getPlaceholder_picture());
        i.putExtra("carIDFuel",fuelLoad.getCarID());
    }

    OneFuelLoad receivIntentFuel(Intent receivedIntent)
    {
        OneFuelLoad fuel = new OneFuelLoad();
        fuel.setId(receivedIntent.getIntExtra("idFuel",-1));
        fuel.setVolumen(receivedIntent.getDoubleExtra("volumenFuel", -1));
        fuel.setPreis(receivedIntent.getDoubleExtra("preisFuel",-1));
        fuel.setTime_data(receivedIntent.getStringExtra("time_dataFuel"));
        fuel.setPlaceholder_picture(receivedIntent.getByteArrayExtra("pictureFuel"));
        fuel.setCarID(receivedIntent.getIntExtra("carIDFuel",-1));
        return fuel;
    }

    void setStartFragmentNumber(Intent i,int tab)
    {
        i.putExtra("fragment",tab);
    }

    int receivStartFragmentNumber(Intent receivedIntent)
    {
        return receivedIntent.getIntExtra("fragment",0);
    }

    void prepareIntentForSendingWorkshop(Intent i, Workshop work)
    {
        i.putExtra("idWS", work.getId());
        i.putExtra("descriptionWS", work.getDescription());
        i.putExtra("time_dataWS", work.getTime_data());
        i.putExtra("placeholder_pictureWS", work.getPlaceholder_picture());
        i.putExtra("carIDWS", work.getCarID());
    }

    Workshop recievIntentWorkshop(Intent receivedIntent)
    {
        return new Workshop(
                receivedIntent.getIntExtra("idWS", -1),
                receivedIntent.getStringExtra("descriptionWS"),
                receivedIntent.getStringExtra("time_dataWS"),
                receivedIntent.getByteArrayExtra("placeholder_pictureWS"),
                receivedIntent.getIntExtra("carIDWS", -1)
        );
    }

    void prepareIntentForSendingRoute(Intent i, Route route){
        i.putExtra("idRoute", route.getId());
        i.putExtra("idCarRoute", route.getCarID());
        i.putExtra("time_dataRoute", route.getTime_data());
        i.putExtra("route_type", route.getRoute_type());
        i.putExtra("tankRoute", route.getTank());
        i.putExtra("kmRoute", route.getKm());
        i.putExtra("travel_type", route.getTravel_type());
        i.putExtra("co2", route.getCO2_ausstoß());
        i.putExtra("lange", route.getLänge_in_km());
        i.putExtra("kraftstoffverbrauch", route.getKraftstoffverbrauch());
    }

    Route recievIntentRoute(Intent receivedIntent)
    {
        return new Route(
                receivedIntent.getIntExtra("kmRoute",-1),
                receivedIntent.getIntExtra("tankRoute",-1),
                receivedIntent.getStringExtra("route_type"),
                receivedIntent.getStringExtra("time_dataRoute"),
                receivedIntent.getIntExtra("idCarRoute",-1),
                receivedIntent.getIntExtra("idRoute",-1),
                receivedIntent.getStringExtra("travel_type"),
                receivedIntent.getDoubleExtra("co2",-1),
                receivedIntent.getIntExtra("lange", -1),
                receivedIntent.getDoubleExtra("kraftstoffverbrauch",-1)
        );
    }
}
