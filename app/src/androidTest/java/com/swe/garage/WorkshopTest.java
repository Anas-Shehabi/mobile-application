package com.swe.garage;

import androidx.test.core.app.ApplicationProvider;
import android.content.Context;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class WorkshopTest{

    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);

    @Before
    @Test
    public void TestEquals()
    {
        Workshop UnitWorkshop = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00",0, null);
        UnitWorkshop.setId(1);
        Workshop CheckUnitWorkshop = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00",0 , null);
        CheckUnitWorkshop.setId(1);

        assertEquals("Check if the equals method notices when the object is the same", UnitWorkshop, CheckUnitWorkshop);

        CheckUnitWorkshop.setDescription("Other");

        assertNotEquals("Check if the equals method notices a different value in Description", UnitWorkshop, CheckUnitWorkshop);

        CheckUnitWorkshop.setDescription("UnitWorkshop");
        CheckUnitWorkshop.setTime_data("00-00-0010 | 00:00:00");

        assertNotEquals("Check if the equals method notices a different value in Time_data", UnitWorkshop, CheckUnitWorkshop);

        CheckUnitWorkshop.setTime_data("00-00-0000 | 00:00:00");
        UnitWorkshop.setCarID(5);

        assertNotEquals("Check if the equals method notices a different value in CarID", UnitWorkshop, CheckUnitWorkshop);

        UnitWorkshop.setCarID(0);
        UnitWorkshop.setId(7);

        assertNotEquals("Check if the equals method notices a different value in ID", UnitWorkshop, CheckUnitWorkshop);

        UnitWorkshop.setId(1);
        byte[] temp = {0,5,7};
        CheckUnitWorkshop.setPlaceholder_picture(temp);

        assertNotEquals("Check if the equals method notices a different value in ID", UnitWorkshop, CheckUnitWorkshop);
    }


    @Test
    public void TestaddWorkshop()
    {
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");
        mUnitDatabaseHelper.addData(UnitAuto);
        Workshop UnitWorkshopFirst = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00",UnitAuto.getId(), null);
        Workshop UnitWorkshopSecond = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00",UnitAuto.getId(), null);



        assertEquals("Check if there are no Workshop entries for the new creadted car", 0, mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId()).getCount());

        mUnitDatabaseHelper.addWorkshop(UnitWorkshopFirst);

        assertEquals("Check if there is now a Workshop Entry for that car", 1, mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId()).getCount());

        mUnitDatabaseHelper.addWorkshop(UnitWorkshopSecond);

        assertEquals("Check if there are now two Workshop entries", 2, mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId()).getCount());

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
    }

    @Test
    public void TestgetWorkshopData(){

        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");
        mUnitDatabaseHelper.addData(UnitAuto);
        byte[] temp = {0,5,7};

        Workshop UnitWorkshopFirst = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00", UnitAuto.getId(),null);
        Workshop UnitWorkshopSecond = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00", UnitAuto.getId(), temp);

        mUnitDatabaseHelper.addWorkshop(UnitWorkshopFirst);

        Cursor cursor = mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId());
        ArrayList<Workshop> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Workshop(cursor));

        assertTrue("Check if the Workshop is in the Table", list.contains(UnitWorkshopFirst));
        assertFalse("Check if the other Workshop is not in the Table", list.contains(UnitWorkshopSecond));

        mUnitDatabaseHelper.addWorkshop(UnitWorkshopSecond);

        cursor = mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Workshop(cursor));

        assertTrue("Check if the first Workshop is in the Table", list.contains(UnitWorkshopFirst));
        assertTrue("Check if the second Workshop is in the Table", list.contains(UnitWorkshopSecond));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
    }

    @Test
    public void TestupdateWorkshop()
    {
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");
        mUnitDatabaseHelper.addData(UnitAuto);
        byte[] temp = {0,5,7};
        Workshop UnitWorkshop = new Workshop("UnitWorkshop","00-00-0000 | 00:00:00", UnitAuto.getId(), temp);

        mUnitDatabaseHelper.addWorkshop(UnitWorkshop);
        int startID = UnitWorkshop.getId();
        int startAutoID = UnitWorkshop.getCarID();

        Cursor cursor = mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId());
        ArrayList<Workshop> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Workshop(cursor));

        assertTrue(list.contains(UnitWorkshop));

        temp = new byte[]{6, 5, 7, 7};
        UnitWorkshop.setPlaceholder_picture(temp);
        mUnitDatabaseHelper.upDateWorkshop(UnitWorkshop);

        cursor = mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Workshop(cursor));

        assertTrue("Check if Updates workes correctly with the picture updated",list.contains(UnitWorkshop));

        UnitWorkshop.setDescription("Test");
        mUnitDatabaseHelper.upDateWorkshop(UnitWorkshop);

        cursor = mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Workshop(cursor));

        assertTrue("Check if Updates workes correctly with the description updated", list.contains(UnitWorkshop));

        UnitWorkshop.setTime_data("00-00-0001 | 00:10:00");
        mUnitDatabaseHelper.upDateWorkshop(UnitWorkshop);

        cursor = mUnitDatabaseHelper.getWorkshopData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Workshop(cursor));

        assertTrue("Check if Updates workes correctly with the time_data updated", list.contains(UnitWorkshop));

        assertEquals("Check if Updates never changed the id",startID, UnitWorkshop.getId());
        assertEquals("Check if Updates never changed the car id",startAutoID, UnitWorkshop.getCarID());
    }
}
