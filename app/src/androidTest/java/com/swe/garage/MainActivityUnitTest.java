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
public class MainActivityUnitTest {
    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);



    @Test
    public void TestEveryAddedCarOwnID()
    {
        Auto FirstUnitAuto = new Auto();
        FirstUnitAuto.setAnzeigename("UnitTesterMainActivity");
        Auto SecondUnitAuto = new Auto();
        SecondUnitAuto.setAnzeigename("UnitTesterMainActivity");
        Auto ThirdUnitAuto = new Auto();
        ThirdUnitAuto.setAnzeigename("UnitTesterMainActivity");

        mUnitDatabaseHelper.addData(FirstUnitAuto);
        mUnitDatabaseHelper.addData(SecondUnitAuto);
        mUnitDatabaseHelper.addData(ThirdUnitAuto);

        assertNotEquals("Checks if both Cars have a different id",FirstUnitAuto.getId(),SecondUnitAuto.getId());
        assertNotEquals("Checks if both Cars have a different id",SecondUnitAuto.getId(),ThirdUnitAuto.getId());
        assertNotEquals("Checks if both Cars have a different id",ThirdUnitAuto.getId(),FirstUnitAuto.getId());

        mUnitDatabaseHelper.deleteAuto(FirstUnitAuto);
        mUnitDatabaseHelper.deleteAuto(SecondUnitAuto);
        mUnitDatabaseHelper.deleteAuto(ThirdUnitAuto);
    }

}
