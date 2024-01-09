package com.swe.garage;

import androidx.test.core.app.ApplicationProvider;
import android.content.Context;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class DatabaseDeleteUnitTest {
    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);

    @Test
    public void TestSimpleCarDelete(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitTesterSimpleCar");
        UnitAuto.setTankstand(25);
        UnitAuto.setTankvolumen(25);
        UnitAuto.setCo2_aus(2);
        UnitAuto.setKilometerstand(60000);
        UnitAuto.setKraftstoff_komb(2.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(2.0);

        int entrysbefore = mUnitDatabaseHelper.getData().getCount();

        mUnitDatabaseHelper.addData(UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        ArrayList<Auto> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));


        assertTrue("Checks if the new created Car which was just added to the Car Table, can be found there",list.contains(UnitAuto));

        assertEquals("Checks the amount of entrys in the car Table, expected is that there is one entry more than before" ,mUnitDatabaseHelper.getData().getCount(),entrysbefore + 1);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        cursor = mUnitDatabaseHelper.getData();
        ArrayList<Auto> listSecond = new ArrayList<Auto>();

        while(cursor.moveToNext())
            listSecond.add(new Auto(cursor));

        assertFalse("Checks if the car which got deleted is still in the Table, expected that it is not found",listSecond.contains(UnitAuto));

        assertEquals("Checks the amount of entries in the car Table, expected is that there is one entry less than before" ,mUnitDatabaseHelper.getData().getCount(),entrysbefore);
    }

    @Test
    public void TestIfDeleteIsSaveWithClones(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitTesterIfDeletCar");
        UnitAuto.setTankstand(25);
        UnitAuto.setTankvolumen(25);
        UnitAuto.setCo2_aus(2);
        UnitAuto.setKilometerstand(60000);
        UnitAuto.setKraftstoff_komb(2.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(2.0);
        Auto UnitAutoSecond = new Auto(UnitAuto);
        Auto UnitAutoThird = new Auto(UnitAuto);

        int entrysbefore = mUnitDatabaseHelper.getData().getCount();
        mUnitDatabaseHelper.addData(UnitAuto);
        mUnitDatabaseHelper.addData(UnitAutoSecond);
        mUnitDatabaseHelper.addData(UnitAutoThird);

        assertEquals("Test if there are 3 more entries in the car Table, which is expected because three Cars were added",mUnitDatabaseHelper.getData().getCount(),entrysbefore+3);

        Cursor cursor = mUnitDatabaseHelper.getData();
        ArrayList<Auto> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));

        assertTrue("Checks if one of the cars is added to the Table",list.contains(UnitAuto));
        assertTrue("Checks if one of the cars is added to the Table",list.contains(UnitAutoSecond));
        assertTrue("Checks if one of the cars is added to the Table",list.contains(UnitAutoThird));

        mUnitDatabaseHelper.deleteAuto(UnitAutoSecond);

        cursor = mUnitDatabaseHelper.getData();
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));

        assertEquals("Test if there are 2 more entries in the car Table than in the beginning, which is expected because three Cars were added and one got deleted",mUnitDatabaseHelper.getData().getCount(),entrysbefore+2);
        assertFalse("Checks if the right Car got deleted",list.contains(UnitAutoSecond));
        assertTrue("Checks if the other two cars dont got deleted",list.contains(UnitAuto));
        assertTrue("Checks if the other two cars dont got deleted",list.contains(UnitAutoThird));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        cursor = mUnitDatabaseHelper.getData();
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));

        assertEquals("Test if there are 1 more entries in the car Table than in the beginning, which is expected because three Cars were added and two got deleted",mUnitDatabaseHelper.getData().getCount(),entrysbefore+1);
        assertFalse("Checks if the first deleted car is still not in the table",list.contains(UnitAutoSecond));
        assertFalse("Checks if the right Car got deleted",list.contains(UnitAuto));
        assertTrue("Checks if the last car doesnt got deleted",list.contains(UnitAutoThird));

        mUnitDatabaseHelper.deleteAuto(UnitAutoThird);

        cursor = mUnitDatabaseHelper.getData();
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));

        assertEquals("Test if there are 0 more entries in the car Table than in the beginning, which is expected because three Cars were added and three got deleted",mUnitDatabaseHelper.getData().getCount(),entrysbefore);
        assertFalse("Checks if the first deleted car is still not in the table",list.contains(UnitAutoSecond));
        assertFalse("Checks if the second deleted car is still not in the table",list.contains(UnitAuto));
        assertFalse("Checks if the right Car got deleted",list.contains(UnitAutoThird));
    }

    @Test
    public void TestDeleteCarFuelFollows()
    {
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitTesterFuel");
        UnitAuto.setTankstand(25);
        UnitAuto.setTankvolumen(25);
        UnitAuto.setCo2_aus(2);
        UnitAuto.setKilometerstand(60000);
        UnitAuto.setKraftstoff_komb(2.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(2.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        OneFuelLoad UnitFuel = new OneFuelLoad();
        UnitFuel.setCarID(UnitAuto.getId());
        UnitFuel.setPreis(1.0);
        UnitFuel.setVolumen(2.0);
        UnitFuel.setTime_data("00-00-0000 | 00:00:00");

        Cursor allFuels = mUnitDatabaseHelper.getAllFuelData();

        int valueBefore = allFuels.getCount();

        Cursor cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());

        assertFalse("Check if no other Fuel for the new Car is in the table",cursor.moveToPosition(0));

        mUnitDatabaseHelper.addFuel(UnitFuel);

        allFuels = mUnitDatabaseHelper.getAllFuelData();
        assertEquals("Test if there is a fuel more", valueBefore + 1 , allFuels.getCount());

        cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());

        assertTrue("Check if there is now a new input in the Table ",cursor.moveToPosition(0));


        assertTrue("Check if the Id of the Car are same as the foreign key of th new fuel",cursor.getInt(5) == UnitAuto.getId());
        assertTrue("Check if the Price of the Table fuel is the same as the one form the fuel object",cursor.getInt(2) == UnitFuel.getPreis());

        assertTrue("Check that there is an entry in the Table with the ID of the Car as foreign Key",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount() == 1);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        allFuels = mUnitDatabaseHelper.getAllFuelData();
        assertEquals("Test if there is the same number like in the beginning", valueBefore, allFuels.getCount());

        assertTrue("Check that there is no longer an entry in the Table with the ID of the Car as foreign Key",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount() == 0);
    }

    @Test
    public void TestDeleteCarRouteFollows(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitTesterRoute");
        UnitAuto.setTankstand(25);
        UnitAuto.setTankvolumen(25);
        UnitAuto.setCo2_aus(2);
        UnitAuto.setKilometerstand(60000);
        UnitAuto.setKraftstoff_komb(2.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(2.0);

        mUnitDatabaseHelper.addData(UnitAuto);
        Route UnitRoute = new Route(20,20,"test","00-00-0000 | 00:00:00",UnitAuto.getId());

        assertEquals("Check if the Table of the routes has no entry for the new car which was just added", mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),0);

        mUnitDatabaseHelper.addRoute(UnitRoute);

        assertEquals("Check if the Table of the routes has now one entry for the new car which was just added", mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),1);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Check if the Table of the routes has now no entry again because the Car got deleted", mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),0);
    }

    @Test
    public void TestDeleteCarWorkshopFollows() {
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitTesterWorkshop");
        UnitAuto.setTankstand(25);
        UnitAuto.setTankvolumen(25);
        UnitAuto.setCo2_aus(2);
        UnitAuto.setKilometerstand(60000);
        UnitAuto.setKraftstoff_komb(2.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(2.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Workshop UnitWorkshop = new Workshop("Test","00-00-0000 | 00:00:00",UnitAuto.getId(), null);

        assertEquals("Check if the Table of the Workshops has no entry for the new car which was just added", mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId()).getCount(),0);

        mUnitDatabaseHelper.addWorkshop(UnitWorkshop);

        assertEquals("Check if the Table of the Workshops has now one entry for the new car which was just added", mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId()).getCount(),1);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Check if the Table of the Workshops has now again no entry for the new car because the associated car got deleted", mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId()).getCount(),0);
    }


    @Test
    public void TestDeleteCarLatestEntryFollows(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        assertFalse("Check if there is no Entry with this Values in the LastEntry Table", mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAuto.getId(),"test", 0));

        mUnitDatabaseHelper.LatestEntry(UnitAuto.getId(), "test" , 0,-1,-1);

        assertTrue("Check if this Entry is now found in the Table", mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAuto.getId(),"test", 0));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertFalse("Check if this Entry is no longer in the Table and got deleted on cascade", mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAuto.getId(),"test", 0));
    }

}
