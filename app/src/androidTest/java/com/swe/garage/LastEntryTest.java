package com.swe.garage;

import androidx.test.core.app.ApplicationProvider;
import android.content.Context;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LastEntryTest {

    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);

    @Test
    public void TestLastEntryMethods(){

        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");
        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoTwo = new Auto();
        UnitAuto.setAnzeigename("UnitAutoTwo");
        mUnitDatabaseHelper.addData(UnitAutoTwo);

        assertFalse("Test if a non existent entry returns a false",mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAuto.getId(),"test", 5));

        mUnitDatabaseHelper.LatestEntry(UnitAutoTwo.getId(),"test", 9999,-1,-1);

        assertFalse("Test if a non existent entry returns a false",mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAutoTwo.getId(),"test", 5));
        assertTrue("Test if a existent entry returns a true",mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAutoTwo.getId(),"test", 9999));

        mUnitDatabaseHelper.LatestEntry(UnitAutoTwo.getId(),"test", 10000,-1,-1);

        assertFalse("Test if the old entry got deleted and returns a false", mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAutoTwo.getId(),"test", 9999));
        assertTrue("Test if the new entry returns a true", mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAutoTwo.getId(),"test", 10000));

        assertFalse("Test if a correct entry with wrong CarID returns a false", mUnitDatabaseHelper.ReturnDetailsLastEntry(UnitAuto.getId(),"test", 10000));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
        mUnitDatabaseHelper.deleteAuto(UnitAutoTwo);
    }
}
