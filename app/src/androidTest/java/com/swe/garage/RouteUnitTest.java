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
public class RouteUnitTest {

    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);

    @Test
    public void Test_checkEquals()
    {
        Route UnitRouteOne=new Route(10,15,"private","00-00-0000 | 00:00:00", 0,0);
        Route UnitRouteTwo=new Route(10,15,"private","00-00-0000 | 00:00:00", 0,0);

        assertEquals("Check if Route equals method recognizes two routes with the same values",UnitRouteOne,UnitRouteTwo);

        UnitRouteTwo.setKm(9);

        assertNotEquals("Check if Route equals method recognizes two routes with different values in km",UnitRouteOne,UnitRouteTwo);

        UnitRouteTwo.setKm(10);
        UnitRouteTwo.setTank(14);

        assertNotEquals("Check if Route equals method recognizes two routes with different values in tank",UnitRouteOne,UnitRouteTwo);

        UnitRouteTwo.setTank(15);
        UnitRouteTwo.setRoute_type("beruflich");

        assertNotEquals("Check if Route equals method recognizes two routes with different values in route_type",UnitRouteOne,UnitRouteTwo);

        UnitRouteTwo.setRoute_type("private");
        UnitRouteTwo.setTime_data("00-00-0100 | 00:00:00");

        assertNotEquals("Check if Route equals method recognizes two routes with different values in Time_data",UnitRouteOne,UnitRouteTwo);

        UnitRouteTwo.setTime_data("00-00-0000 | 00:00:00");
        UnitRouteTwo.setCarID(1);

        assertNotEquals("Check if Route equals method recognizes two routes with different values in carID",UnitRouteOne,UnitRouteTwo);

        UnitRouteTwo.setCarID(0);
        UnitRouteOne.setId(1);

        assertNotEquals("Check if Route equals method recognizes two routes with different values in carID",UnitRouteOne,UnitRouteTwo);
    }

    @Test
    public void Test_addRoute(){

        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);
        int valueBefore =  mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount();
        Route UnitRoute = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());
        UnitRoute.setCarID(UnitAuto.getId());

        mUnitDatabaseHelper.addRoute(UnitRoute);

        assertEquals("Check if there is one more Route in the Table after the adding",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore+1);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Removed garbage if fail: check database tests",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore);

    }

    @Test
    public void Test_getRouteData(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);
        Route UnitRouteOne = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());

        int valueBefore =  mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount();

        mUnitDatabaseHelper.addRoute(UnitRouteOne);

        Cursor cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        cursor.moveToFirst();
        Route curr = new Route(cursor);

        assertEquals("Check if the Route is still the same", UnitRouteOne, curr);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Check Database Test if fails",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore);
    }

    @Test
    public void TestDistinguishRoutes(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        Route UnitRouteOne=new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());
        Route UnitRouteTwo=new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());
        Route UnitRouteThree=new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());

        int valueBefore =  mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount();
        mUnitDatabaseHelper.addRoute(UnitRouteOne);

        assertEquals("Check if there is now one more entries than before in the Database",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore+1);

        Cursor cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        ArrayList <Route>list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitRouteOne));
        assertFalse("Check if one of the three entries, which was not added, is not in the database",list.contains(UnitRouteTwo));
        assertFalse("Check if one of the three entries, which was not added, is not in the database",list.contains(UnitRouteThree));

        mUnitDatabaseHelper.addRoute(UnitRouteTwo);

        assertEquals("Check if there are now two more entries than in the beginning in the Database",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore+2);

        cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitRouteOne));
        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitRouteTwo));
        assertFalse("Check if one of the three entries, which was not added, is not in the database",list.contains(UnitRouteThree));

        mUnitDatabaseHelper.addRoute(UnitRouteThree);

        assertEquals("Check if there is now three more entries than before in the Database",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore+3);

        cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if one of the three entries, which was added, is in the database", list.contains(UnitRouteOne));
        assertTrue("Check if one of the three entries, which was added, is in the database", list.contains(UnitRouteTwo));
        assertTrue("Check if one of the three entries, which was added, is in the database", list.contains(UnitRouteThree));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Check if there is now no entry in the Database, like in the beginning",mUnitDatabaseHelper.getRouteData(UnitAuto.getId()).getCount(),valueBefore);
    }

    @Test
    public void TestDistinguishRoutesAcrossCars(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        Route UnitRouteOne = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());
        Route UnitRouteTwo = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());
        Route UnitRouteThree = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAuto.getId());

        mUnitDatabaseHelper.addRoute(UnitRouteOne);
        mUnitDatabaseHelper.addRoute(UnitRouteTwo);
        mUnitDatabaseHelper.addRoute(UnitRouteThree);

        Auto UnitAutoSecond = new Auto();
        UnitAutoSecond.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAutoSecond);

        Route SecondUnitRouteOne = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAutoSecond.getId());
        Route SecondUnitRouteTwo = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAutoSecond.getId());
        Route SecondUnitRouteThree = new Route(10,15,"private","00-00-0000 | 00:00:00", UnitAutoSecond.getId());

        mUnitDatabaseHelper.addRoute(SecondUnitRouteOne);
        mUnitDatabaseHelper.addRoute(SecondUnitRouteTwo);
        mUnitDatabaseHelper.addRoute(SecondUnitRouteThree);

        Cursor cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        ArrayList <Route>list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if the entries of this car are found",list.contains(UnitRouteOne));
        assertTrue("Check if the entries of this car are found",list.contains(UnitRouteTwo));
        assertTrue("Check if the entries of this car are found",list.contains(UnitRouteThree));

        assertFalse("Check if the entries of the other car are not found",list.contains(SecondUnitRouteOne));
        assertFalse("Check if the entries of the other car are not found",list.contains(SecondUnitRouteTwo));
        assertFalse("Check if the entries of the other car are not found",list.contains(SecondUnitRouteThree));

        cursor = mUnitDatabaseHelper.getRouteData(UnitAutoSecond.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if the entries of this car are found",list.contains(SecondUnitRouteOne));
        assertTrue("Check if the entries of this car are found",list.contains(SecondUnitRouteTwo));
        assertTrue("Check if the entries of this car are found",list.contains(SecondUnitRouteThree));

        assertFalse("Check if the entries of the other car are not found",list.contains(UnitRouteOne));
        assertFalse("Check if the entries of the other car are not found",list.contains(UnitRouteTwo));
        assertFalse("Check if the entries of the other car are not found",list.contains(UnitRouteThree));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
        mUnitDatabaseHelper.deleteAuto(UnitAutoSecond);
    }

    @Test
    public void TestupdateRoute(){

        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        Route UnitRoute = new Route(2,1,"test","00-00-0000 | 00:00:00", UnitAuto.getId());

        mUnitDatabaseHelper.addRoute(UnitRoute);

        UnitRoute.setKm(20);

        mUnitDatabaseHelper.updateRoute(UnitRoute);

        Cursor cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        ArrayList <Route>list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if the km got updated", list.contains(UnitRoute));

        UnitRoute.setRoute_type("TEST");

        mUnitDatabaseHelper.updateRoute(UnitRoute);

        cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if the Route_type got updated", list.contains(UnitRoute));

        UnitRoute.setTime_data("00-00-1000 | 00:10:00");

        mUnitDatabaseHelper.updateRoute(UnitRoute);

        cursor = mUnitDatabaseHelper.getRouteData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Route(cursor));

        assertTrue("Check if the Time_data got updated", list.contains(UnitRoute));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
    }
}
