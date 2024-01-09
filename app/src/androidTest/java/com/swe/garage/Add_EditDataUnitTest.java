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
public class Add_EditDataUnitTest {

    private final int NAME_INDEX = 1;
    private final int INNER_INDEX = 2;
    private final int AUSSER_INDEX = 3;
    private final int KOMB_INDEX = 4;
    private final int KILOMETERSTAND_INDEX = 5;
    private final int TANKSTAND_INDEX = 6;
    private final int TANKVOLUMEN_INDEX = 7;
    private final int CO2_INDEX = 8;

    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);

    @Test
    public void Test_Equals(){
        Auto UnitAuto = new Auto(0,"UnitAuto",1.0,2.0,3.0,50,10,20,1);
        Auto CheckUnitAuto = new Auto(UnitAuto);

        assertEquals("Check if equals works correct", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setId(1);

        assertNotEquals("Check if equals differences if the ID is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setId(0);
        CheckUnitAuto.setKraftstoff_inner(1.5);

        assertNotEquals("Check if equals differences if the Kraftstoff_inner is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setKraftstoff_inner(1.0);
        CheckUnitAuto.setKraftstoff_ausser(1.1);

        assertNotEquals("Check if equals differences if the Kraftstoff_ausser is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setKraftstoff_ausser(2.0);
        CheckUnitAuto.setKraftstoff_komb(1.7);

        assertNotEquals("Check if equals differences if the Kraftstoff_komb is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setKraftstoff_komb(3.0);
        CheckUnitAuto.setKilometerstand(60);

        assertNotEquals("Check if equals differences if the Kilometerstand is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setKilometerstand(50);
        CheckUnitAuto.setTankstand(17);

        assertNotEquals("Check if equals differences if the Tankstand is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setTankstand(10);
        CheckUnitAuto.setTankvolumen(44);

        assertNotEquals("Check if equals differences if the Tankvolumen is an other value", UnitAuto, CheckUnitAuto);

        CheckUnitAuto.setTankvolumen(20);
        CheckUnitAuto.setCo2_aus(2);

        assertNotEquals("Check if equals differences if the CO2 is an other value", UnitAuto, CheckUnitAuto);
    }

    @Test
    public void TestEditCarSameValuesLikeOther()
    {
        Auto first = new Auto();
        first.setAnzeigename("UnitAuto");
        first.setKraftstoff_inner(2.0);
        first.setKraftstoff_ausser(2.0);
        first.setKraftstoff_komb(2.0);
        first.setKilometerstand(500);
        first.setTankvolumen(40);
        first.setTankstand(15);
        first.setCo2_aus(1);

        int beforeValue = mUnitDatabaseHelper.getData().getCount();

        mUnitDatabaseHelper.addData(first);

        Auto second = new Auto();
        second.setAnzeigename("UnitAutoChanged");
        second.setKraftstoff_inner(4.0);
        second.setKraftstoff_ausser(4.0);
        second.setKraftstoff_komb(4.0);
        second.setKilometerstand(400);
        second.setTankvolumen(22);
        second.setTankstand(69);
        second.setCo2_aus(3);

        mUnitDatabaseHelper.addData(second);

        assertEquals("Check if there are two more entires than before the adding of the two cars",mUnitDatabaseHelper.getData().getCount(),beforeValue+2);

        Cursor cursor = mUnitDatabaseHelper.getData();
        ArrayList<Auto> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));

        assertNotEquals("Check if both cars are different",first,second);
        assertTrue("Check if the first Car is in the car database",list.contains(first));
        assertTrue("Check if the second Car is in the car database",list.contains(second));

        Auto secondNew = new Auto();
        secondNew.setAnzeigename("UnitAuto");
        secondNew.setKraftstoff_inner(2.0);
        secondNew.setKraftstoff_ausser(2.0);
        secondNew.setKraftstoff_komb(2.0);
        secondNew.setKilometerstand(500);
        secondNew.setTankvolumen(40);
        secondNew.setTankstand(15);
        secondNew.setCo2_aus(1);

        mUnitDatabaseHelper.updateName(secondNew,second);
        mUnitDatabaseHelper.updateTankStand(secondNew,second);
        mUnitDatabaseHelper.updateKm(secondNew,second);
        mUnitDatabaseHelper.updateTankVolumen(secondNew,second);
        mUnitDatabaseHelper.updateCO2(secondNew,second);
        mUnitDatabaseHelper.updateKraftStoffAusser(secondNew,second);
        mUnitDatabaseHelper.updateKraftStoffInner(secondNew,second);
        mUnitDatabaseHelper.updateKraftStoffKombi(secondNew,second);
        secondNew.setId(second.getId());

        assertEquals("Check if there are still two more entires than before the adding of the two cars",mUnitDatabaseHelper.getData().getCount(),beforeValue+2);

        assertNotEquals("Check if both have different IDs",secondNew.getId(),first.getId());
        assertNotEquals("Check if the Equals method differences them because of the id",secondNew,first);

        cursor = mUnitDatabaseHelper.getData();
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new Auto(cursor));

        assertTrue("Check if the first Car is in the car database",list.contains(first));
        assertTrue("Check if the second updated Car is in the car database",list.contains(secondNew));

        mUnitDatabaseHelper.deleteAuto(secondNew);
        mUnitDatabaseHelper.deleteAuto(first);

        assertEquals("Check if both cars could be removed from the table",mUnitDatabaseHelper.getData().getCount(),beforeValue);
    }

    @Test
    public void Test_updateName(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setAnzeigename("Second");

        mUnitDatabaseHelper.updateName(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateName method works", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertNotEquals("Check if the updateName method works and that the old name isnt the same as the one in the table",cursor.getString(NAME_INDEX),UnitAuto.getAnzeigename());

        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)),UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAuto.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateKraftStoffInner(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setKraftstoff_inner(4.2);

        mUnitDatabaseHelper.updateKraftStoffInner(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateKraftStoffInner method works", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAutoNewName.getKraftstoff_inner(),0.0005);
        assertNotEquals("Check if the updateKraftStoffInner method works and that the old name isnt the same as the one in the table", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAuto.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateKraftStoffAusser(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setKraftstoff_ausser(4.2);

        mUnitDatabaseHelper.updateKraftStoffAusser(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateKraftStoffAusser method works", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAutoNewName.getKraftstoff_ausser(),0.0005);
        assertNotEquals("Check if the updateKraftStoffAusser method works and that the old name isnt the same as the one in the table", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAuto.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateKraftStoffKombi(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setKraftstoff_komb(4.2);

        mUnitDatabaseHelper.updateKraftStoffKombi(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateKraftStoffKombi method works", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAutoNewName.getKraftstoff_komb(),0.0005);
        assertNotEquals("Check if the updateKraftStoffKombi method works and that the old name isnt the same as the one in the table", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAuto.getKraftstoff_komb(),0.0005);

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateKm(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setKilometerstand(2000);

        mUnitDatabaseHelper.updateKm(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateKm method works", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAutoNewName.getKilometerstand());
        assertNotEquals("Check if the updateKm method works and that the old name isnt the same as the one in the table", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAutoNewName.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateTankStand(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setTankstand(20);

        mUnitDatabaseHelper.updateTankStand(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateTankStand method works", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAutoNewName.getTankstand());
        assertNotEquals("Check if the updateTankStand method works and that the old name isnt the same as the one in the table", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAutoNewName.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateTankVolumen(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setTankvolumen(20);

        mUnitDatabaseHelper.updateTankVolumen(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateTankVolumen method works", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAutoNewName.getTankvolumen());
        assertNotEquals("Check if the updateTankVolumen method works and that the old name isnt the same as the one in the table", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAutoNewName.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }

    @Test
    public void Test_updateCO2(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("First");
        UnitAuto.setCo2_aus(1);
        UnitAuto.setTankstand(10);
        UnitAuto.setTankvolumen(40);
        UnitAuto.setKilometerstand(4000);
        UnitAuto.setKraftstoff_komb(1.0);
        UnitAuto.setKraftstoff_ausser(2.0);
        UnitAuto.setKraftstoff_inner(3.0);

        mUnitDatabaseHelper.addData(UnitAuto);

        Auto UnitAutoNewName = new Auto(UnitAuto);

        UnitAutoNewName.setCo2_aus(4);

        mUnitDatabaseHelper.updateCO2(UnitAutoNewName,UnitAuto);

        Cursor cursor = mUnitDatabaseHelper.getData();
        cursor.moveToLast();

        assertEquals("Check if the updateCO2 method works", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAutoNewName.getCo2_aus());
        assertNotEquals("Check if the updateCO2 method works and that the old name isnt the same as the one in the table", Integer.parseInt((cursor.getString(CO2_INDEX))), UnitAuto.getCo2_aus());

        assertEquals("Check if the other values are still the same", cursor.getString(NAME_INDEX),UnitAutoNewName.getAnzeigename());
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(INNER_INDEX)), UnitAuto.getKraftstoff_inner(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(AUSSER_INDEX)), UnitAuto.getKraftstoff_ausser(),0.0005);
        assertEquals("Check if the other values are still the same", Double.parseDouble(cursor.getString(KOMB_INDEX)), UnitAutoNewName.getKraftstoff_komb(),0.0005);
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(KILOMETERSTAND_INDEX))), UnitAuto.getKilometerstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKSTAND_INDEX))), UnitAuto.getTankstand());
        assertEquals("Check if the other values are still the same", Integer.parseInt((cursor.getString(TANKVOLUMEN_INDEX))), UnitAuto.getTankvolumen());

        mUnitDatabaseHelper.deleteAuto(UnitAutoNewName);
    }
}
