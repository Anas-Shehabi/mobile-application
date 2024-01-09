package com.swe.garage;

import androidx.test.core.app.ApplicationProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class IntentHelperTest {
    final private IntentHelperSendReceivData IntentHelper = new IntentHelperSendReceivData();
    @Test
    public void Test_SendingAndReceivAutoMethod()
    {
        Auto UnitAuto = new Auto();
        UnitAuto.setId(0);
        UnitAuto.setAnzeigename("UnitAuto");
        UnitAuto.setKraftstoff_inner(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_komb(3.0);
        UnitAuto.setKilometerstand(777);
        UnitAuto.setTankstand(20);
        UnitAuto.setTankvolumen(30);
        UnitAuto.setCo2_aus(4);

        Intent testIntent = new Intent();
        IntentHelper.prepareIntentForSendingAuto(testIntent,UnitAuto);

        Auto CheckUnitAuto = IntentHelper.receivIntentAuto(testIntent);

        assertEquals("Check if the submit via method works",UnitAuto,CheckUnitAuto);
    }

    @Test
    public void Test_SendingAndReceivFuelMethod(){
        OneFuelLoad UnitFuel = new OneFuelLoad();
        UnitFuel.setId(0);
        UnitFuel.setPlaceholder_picture(null);
        UnitFuel.setTime_data("00-00-0000 | 00:00:00");
        UnitFuel.setVolumen(8.0);
        UnitFuel.setPreis(70);
        UnitFuel.setCarID(1);

        Intent testIntent = new Intent();
        IntentHelper.prepareIntentForSendingFuel(testIntent, UnitFuel);

        OneFuelLoad CheckUnitFuel = IntentHelper.receivIntentFuel(testIntent);

        assertEquals("Check if the submit via method works",UnitFuel, CheckUnitFuel);
    }

    @Test
    public void Test_FragmentIntentMethods()
    {
        Intent testIntent = new Intent();
        int numb = 5;

        IntentHelper.setStartFragmentNumber(testIntent,numb);

        int CheckNumb = IntentHelper.receivStartFragmentNumber(testIntent);

        assertEquals("Check if the submit via method works", numb, CheckNumb);
    }

    @Test
    public void Test_SendingAndReceivWorkshopMethod(){
        Intent testIntent = new Intent();
        byte[] temp = {1,7,8};
        Workshop workshop = new Workshop(
                0,
                "TestWorkshop",
                "00-00-0000 | 00:00:00",
                temp,
                1
        );

        IntentHelper.prepareIntentForSendingWorkshop(testIntent, workshop);

        Workshop Checkworkshop = IntentHelper.recievIntentWorkshop(testIntent);

        assertEquals("Check if both Workshop Intent methods worked:", workshop,Checkworkshop);
    }

    @Test
    public void Test_prepareIntentForSendingRoute(){
        Intent testIntent = new Intent();

        Route route = new Route(5,10,"test","00-00-0000 | 00:00:00",1,2);

        IntentHelper.prepareIntentForSendingRoute(testIntent, route);

        Route Checkroute = IntentHelper.recievIntentRoute(testIntent);

        assertEquals("Check if both routes are equal",route , Checkroute);
    }
}
