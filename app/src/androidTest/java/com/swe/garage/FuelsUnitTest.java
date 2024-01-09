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
public class FuelsUnitTest {
    Context context = ApplicationProvider.getApplicationContext();
    private DatabaseHelper mUnitDatabaseHelper = new DatabaseHelper(context);

    @Test
    public void Test_checkEquals(){
        OneFuelLoad UnitFuelOne = new OneFuelLoad(0,5.0,6.0,"00-00-0000 | 00:00:00",null,4);
        OneFuelLoad CheckUnitFuelOne = new OneFuelLoad(0,5.0,6.0,"00-00-0000 | 00:00:00",null,4);

        assertEquals("Check if the Equals method for OneFuelLoads works", UnitFuelOne, CheckUnitFuelOne);

        CheckUnitFuelOne.setId(1);

        assertNotEquals("Check if the Equals method for OneFuelLoads differences the id", UnitFuelOne, CheckUnitFuelOne);

        CheckUnitFuelOne.setId(0);
        CheckUnitFuelOne.setVolumen(6.0);

        assertNotEquals("Check if the Equals method for OneFuelLoads differences the Volumen", UnitFuelOne, CheckUnitFuelOne);

        CheckUnitFuelOne.setVolumen(5.0);
        CheckUnitFuelOne.setPreis(7.0);

        assertNotEquals("Check if the Equals method for OneFuelLoads differences the Preis", UnitFuelOne, CheckUnitFuelOne);

        CheckUnitFuelOne.setPreis(6.0);
        CheckUnitFuelOne.setTime_data("00-01-0000 | 00:00:00");

        assertNotEquals("Check if the Equals method for OneFuelLoads differences the time_data", UnitFuelOne, CheckUnitFuelOne);

        CheckUnitFuelOne.setTime_data("00-00-0000 | 00:00:00");
        byte[] temp = {0,5,7};
        CheckUnitFuelOne.setPlaceholder_picture(temp);

        assertNotEquals("Check if the Equals method for OneFuelLoads differences the placeholder", UnitFuelOne, CheckUnitFuelOne);

        CheckUnitFuelOne.setPlaceholder_picture(null);
        CheckUnitFuelOne.setCarID(7);

        assertNotEquals("Check if the Equals method for OneFuelLoads differences the CarID", UnitFuelOne, CheckUnitFuelOne);
    }


    @Test
    public void Test_addFuel(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        int valueBefore =  mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount();

        OneFuelLoad UnitFuel = new OneFuelLoad();
        UnitFuel.setCarID(UnitAuto.getId());

        mUnitDatabaseHelper.addFuel(UnitFuel);

        assertEquals("Tests if there is one more car in the Table after the adding",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore+1);

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Removed garbage if fail, check database tests",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore);
    }

    @Test
    public void Test_getFuelData(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        OneFuelLoad UnitFuelOne = new OneFuelLoad();
        UnitFuelOne.setCarID(UnitAuto.getId());
        UnitFuelOne.setPreis(20.0);
        UnitFuelOne.setVolumen(20.2);
        UnitFuelOne.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelOne.setPlaceholder_picture(null);
        OneFuelLoad UnitFuelTwo = new OneFuelLoad();
        UnitFuelTwo.setCarID(UnitAuto.getId());
        UnitFuelTwo.setTime_data("00-00-0000 | 00:00:00");
        OneFuelLoad UnitFuelThree = new OneFuelLoad();
        UnitFuelThree.setCarID(UnitAuto.getId());
        UnitFuelThree.setTime_data("00-00-0000 | 00:00:00");

        int valueBefore =  mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount();

        mUnitDatabaseHelper.addFuel(UnitFuelOne);
        mUnitDatabaseHelper.addFuel(UnitFuelTwo);
        mUnitDatabaseHelper.addFuel(UnitFuelThree);

        assertEquals("Tests if there are three more car in the Table after the adding",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore+3);

        Cursor cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());
        ArrayList<OneFuelLoad> list = new ArrayList<>();

        OneFuelLoad CheckHelper_UnitFuelOne = null;

        while(cursor.moveToNext()) {
            OneFuelLoad curr = new OneFuelLoad(cursor);
            list.add(curr);
            if(curr.equals(UnitFuelOne))
                CheckHelper_UnitFuelOne = curr;
        }

        assertTrue("Check if attribut is still the same",CheckHelper_UnitFuelOne.getPreis() == 20.0);
        assertTrue("Check if attribut is still the same",CheckHelper_UnitFuelOne.getVolumen() == 20.2);
        assertEquals("",CheckHelper_UnitFuelOne.time_data(),"00-00-0000 | 00:00:00");
        assertNull(CheckHelper_UnitFuelOne.getPlaceholder_picture());

        assertTrue("Check if the first entry is in the table", list.contains(UnitFuelOne));
        assertTrue("Check if the second entry is in the table", list.contains(UnitFuelTwo));
        assertTrue("Check if the third entry is in the table", list.contains(UnitFuelThree));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);

        assertEquals("Check Database Test if fails",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore);
    }

    @Test
    public void Test_UpdateFuel(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        OneFuelLoad UnitFuel = new OneFuelLoad();
        UnitFuel.setCarID(UnitAuto.getId());
        UnitFuel.setTime_data("00-00-0000 | 00:00:00");
        UnitFuel.setVolumen(20.0);
        UnitFuel.setPreis(20.0);

        mUnitDatabaseHelper.addFuel(UnitFuel);


        UnitFuel.setTime_data("01-00-0100 | 00:01:00");
        UnitFuel.setVolumen(80.0);
        UnitFuel.setPreis(10.0);

        Cursor cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());
        ArrayList<OneFuelLoad> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertFalse("Tests if the changed Fuel is not in the unupdated Database", list.contains(UnitFuel));

        mUnitDatabaseHelper.UpdateFuel(UnitFuel);

       cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Tests if the changed Fuel is in the updated Database", list.contains(UnitFuel));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
    }

    @Test
    public void TestDistinguishFuelsSimple(){
        Auto UnitAuto = new Auto();
        UnitAuto.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAuto);

        OneFuelLoad UnitFuelOne = new OneFuelLoad();
        UnitFuelOne.setCarID(UnitAuto.getId());
        UnitFuelOne.setPreis(20.0);
        UnitFuelOne.setVolumen(20.2);
        UnitFuelOne.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelOne.setPlaceholder_picture(null);

        OneFuelLoad UnitFuelTwo = new OneFuelLoad();
        UnitFuelTwo.setCarID(UnitAuto.getId());
        UnitFuelTwo.setPreis(20.0);
        UnitFuelTwo.setVolumen(20.2);
        UnitFuelTwo.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelTwo.setPlaceholder_picture(null);

        OneFuelLoad UnitFuelThree = new OneFuelLoad();
        UnitFuelThree.setCarID(UnitAuto.getId());
        UnitFuelThree.setPreis(20.0);
        UnitFuelThree.setVolumen(20.2);
        UnitFuelThree.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelThree.setPlaceholder_picture(null);

        int valueBefore = mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount();

        mUnitDatabaseHelper.addFuel(UnitFuelOne);

        assertEquals("Check if there is now one more entries than before in the Database",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore+1);

        Cursor cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());
        ArrayList <OneFuelLoad>list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitFuelOne));
        assertFalse("Check if one of the three entries, which was not added, are not in the database",list.contains(UnitFuelTwo));
        assertFalse("Check if one of the three entries, which was not added, are not in the database",list.contains(UnitFuelThree));

        mUnitDatabaseHelper.addFuel(UnitFuelTwo);

        assertEquals("Check if there are now two more entries than in the beginning in the Database",mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore+2);

        cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitFuelOne));
        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitFuelTwo));
        assertFalse("Check if one of the three entries, which was not added, are not in the database",list.contains(UnitFuelThree));

        mUnitDatabaseHelper.addFuel(UnitFuelThree);

        assertEquals("Check if there are now three more entries than in the beginning in the Database", mUnitDatabaseHelper.getFuelData(UnitAuto.getId()).getCount(),valueBefore+3);

        cursor = mUnitDatabaseHelper.getFuelData(UnitAuto.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitFuelOne));
        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitFuelTwo));
        assertTrue("Check if one of the three entries, which was added, is in the database",list.contains(UnitFuelThree));

        mUnitDatabaseHelper.deleteAuto(UnitAuto);
    }

    @Test
    public void TestDistinguishFuelsAcrossCars(){

        Auto UnitAutoOne = new Auto();
        UnitAutoOne.setAnzeigename("UnitAuto");
        Auto UnitAutoTwo = new Auto();
        UnitAutoTwo.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAutoOne);
        mUnitDatabaseHelper.addData(UnitAutoTwo);

        OneFuelLoad UnitFuelOne = new OneFuelLoad();
        UnitFuelOne.setCarID(UnitAutoOne.getId());
        UnitFuelOne.setPreis(20.0);
        UnitFuelOne.setVolumen(20.2);
        UnitFuelOne.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelOne.setPlaceholder_picture(null);

        OneFuelLoad UnitFuelTwo = new OneFuelLoad();
        UnitFuelTwo.setCarID(UnitAutoOne.getId());
        UnitFuelTwo.setPreis(20.0);
        UnitFuelTwo.setVolumen(20.2);
        UnitFuelTwo.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelTwo.setPlaceholder_picture(null);

        OneFuelLoad UnitFuelThree = new OneFuelLoad();
        UnitFuelThree.setCarID(UnitAutoOne.getId());
        UnitFuelThree.setPreis(20.0);
        UnitFuelThree.setVolumen(20.2);
        UnitFuelThree.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelThree.setPlaceholder_picture(null);

        int valueBeforeOne = mUnitDatabaseHelper.getFuelData(UnitAutoOne.getId()).getCount();
        int valueBeforeTwo = mUnitDatabaseHelper.getFuelData(UnitAutoTwo.getId()).getCount();

        mUnitDatabaseHelper.addFuel(UnitFuelOne);
        int FuelOneCarOneID = UnitFuelOne.getId();
        mUnitDatabaseHelper.addFuel(UnitFuelTwo);
        int FuelTwoCarOneID = UnitFuelTwo.getId();
        mUnitDatabaseHelper.addFuel(UnitFuelThree);
        int FuelThreeCarOneID = UnitFuelThree.getId();

        UnitFuelOne.setCarID(UnitAutoTwo.getId());
        UnitFuelTwo.setCarID(UnitAutoTwo.getId());
        UnitFuelThree.setCarID(UnitAutoTwo.getId());

        mUnitDatabaseHelper.addFuel(UnitFuelOne);
        int FuelOneCarTwoID = UnitFuelOne.getId();
        mUnitDatabaseHelper.addFuel(UnitFuelTwo);
        int FuelTwoCarTwoID = UnitFuelTwo.getId();
        mUnitDatabaseHelper.addFuel(UnitFuelThree);
        int FuelThreeCarTwoID = UnitFuelThree.getId();

        assertEquals(FuelTwoCarTwoID,UnitFuelTwo.getId());

        Cursor cursor = mUnitDatabaseHelper.getFuelData(UnitAutoOne.getId());
        ArrayList<OneFuelLoad> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertFalse("Test if non of the Fuels are in the Entries of the first car, which is expected because the fuels have different foreign key ids", list.contains(UnitFuelOne));
        assertFalse("Test if non of the Fuels are in the Entries of the first car, which is expected because the fuels have different foreign key ids", list.contains(UnitFuelTwo));
        assertFalse("Test if non of the Fuels are in the Entries of the first car, which is expected because the fuels have different foreign key ids", list.contains(UnitFuelThree));

        UnitFuelOne.setCarID(UnitAutoOne.getId());
        UnitFuelOne.setId(FuelOneCarOneID);
        UnitFuelThree.setCarID(UnitAutoOne.getId());
        UnitFuelThree.setId(FuelThreeCarOneID);

        assertTrue("The fuel has now the ID of the first car, it is expected that is is now part of the entries", list.contains(UnitFuelOne));
        assertFalse("This fuel should be still no part of the entries of the first car", list.contains(UnitFuelTwo));
        assertTrue("The fuel has now the ID of the first car, it is expected that is is now part of the entries", list.contains(UnitFuelThree));

        assertEquals("Test if both cars have the three entries more in the database then in the beginning", mUnitDatabaseHelper.getFuelData(UnitAutoOne.getId()).getCount(),valueBeforeOne+3);
        assertEquals("Test if both cars have the three entries more in the database then in the beginning", mUnitDatabaseHelper.getFuelData(UnitAutoTwo.getId()).getCount(),valueBeforeTwo+3);


        mUnitDatabaseHelper.deleteAuto(UnitAutoTwo);

        UnitFuelOne.setCarID(UnitAutoTwo.getId());

        UnitFuelOne.setId(FuelOneCarTwoID);
        UnitFuelThree.setCarID(UnitAutoTwo.getId());
        UnitFuelThree.setId((FuelThreeCarTwoID));

        cursor = mUnitDatabaseHelper.getFuelData(UnitAutoTwo.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertFalse("Test if car two still have fuel entries in the database after the delete of the car", list.contains(UnitFuelOne));
        assertFalse("Test if car two still have fuel entries in the database after the delete of the car", list.contains(UnitFuelTwo));
        assertFalse("Test if car two still have fuel entries in the database after the delete of the car", list.contains(UnitFuelThree));

        cursor = mUnitDatabaseHelper.getFuelData(UnitAutoOne.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        UnitFuelOne.setCarID(UnitAutoOne.getId());
        UnitFuelOne.setId(FuelOneCarOneID);
        UnitFuelTwo.setCarID(UnitAutoOne.getId());
        UnitFuelTwo.setId(FuelTwoCarOneID);
        UnitFuelThree.setCarID(UnitAutoOne.getId());
        UnitFuelThree.setId(FuelThreeCarOneID);

        assertTrue("Test if the first car still has all of his three entries, which is expected",list.contains(UnitFuelOne));
        assertTrue("Test if the first car still has all of his three entries, which is expected",list.contains(UnitFuelTwo));
        assertTrue("Test if the first car still has all of his three entries, which is expected",list.contains(UnitFuelThree));

       mUnitDatabaseHelper.deleteAuto(UnitAutoOne);
    }

    @Test
    public void TestDistinguishFuelsAcrossCarsUpdateF(){
        Auto UnitAutoFirst = new Auto();
        UnitAutoFirst.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAutoFirst);

        OneFuelLoad UnitFuelOne = new OneFuelLoad();
        UnitFuelOne.setCarID(UnitAutoFirst.getId());
        UnitFuelOne.setPreis(20.0);
        UnitFuelOne.setVolumen(20.2);
        UnitFuelOne.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelOne.setPlaceholder_picture(null);

        OneFuelLoad UnitFuelTwo = new OneFuelLoad();
        UnitFuelTwo.setCarID(UnitAutoFirst.getId());
        UnitFuelTwo.setPreis(20.0);
        UnitFuelTwo.setVolumen(20.2);
        UnitFuelTwo.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelTwo.setPlaceholder_picture(null);

        Auto UnitAutoSecond = new Auto();
        UnitAutoSecond.setAnzeigename("UnitAuto");

        mUnitDatabaseHelper.addData(UnitAutoSecond);

        OneFuelLoad UnitFuelOneSecondCar = new OneFuelLoad();
        UnitFuelOneSecondCar.setCarID(UnitAutoSecond.getId());
        UnitFuelOneSecondCar.setPreis(20.0);
        UnitFuelOneSecondCar.setVolumen(20.2);
        UnitFuelOneSecondCar.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelOneSecondCar.setPlaceholder_picture(null);

        OneFuelLoad UnitFuelTwoSecondCar = new OneFuelLoad();
        UnitFuelTwoSecondCar.setCarID(UnitAutoSecond.getId());
        UnitFuelTwoSecondCar.setPreis(20.0);
        UnitFuelTwoSecondCar.setVolumen(20.2);
        UnitFuelTwoSecondCar.setTime_data("00-00-0000 | 00:00:00");
        UnitFuelTwoSecondCar.setPlaceholder_picture(null);

        mUnitDatabaseHelper.addFuel(UnitFuelOne);
        mUnitDatabaseHelper.addFuel(UnitFuelTwo);
        mUnitDatabaseHelper.addFuel(UnitFuelOneSecondCar);
        mUnitDatabaseHelper.addFuel(UnitFuelTwoSecondCar);

        Cursor cursor = mUnitDatabaseHelper.getFuelData(UnitAutoFirst.getId());
        ArrayList<OneFuelLoad> list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Check if cars are in the table of the correct car",list.contains(UnitFuelOne));
        assertTrue("Check if cars are in the table of the correct car",list.contains(UnitFuelTwo));
        assertFalse("Check if cars are not in the table of the false car",list.contains(UnitFuelOneSecondCar));
        assertFalse("Check if cars are not in the table of the false car",list.contains(UnitFuelTwoSecondCar));

        cursor = mUnitDatabaseHelper.getFuelData(UnitAutoSecond.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Check if cars are in the table of the correct car",list.contains(UnitFuelOneSecondCar));
        assertTrue("Check if cars are in the table of the correct car",list.contains(UnitFuelTwoSecondCar));
        assertFalse("Check if cars are not in the table of the false car",list.contains(UnitFuelOne));
        assertFalse("Check if cars are not in the table of the false car",list.contains(UnitFuelTwo));

        UnitFuelTwoSecondCar.setPreis(44.0);
        UnitFuelTwoSecondCar.setVolumen(50.2);
        UnitFuelTwoSecondCar.setTime_data("10-00-0000 | 00:10:00");

        cursor = mUnitDatabaseHelper.getFuelData(UnitAutoSecond.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertFalse("Check if the changed car is not in the table of the correct car",list.contains(UnitFuelTwoSecondCar));

        mUnitDatabaseHelper.UpdateFuel(UnitFuelTwoSecondCar);

        cursor = mUnitDatabaseHelper.getFuelData(UnitAutoSecond.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertTrue("Check if the changed car is now in the table of the correct car",list.contains(UnitFuelTwoSecondCar));

        UnitFuelTwoSecondCar.setPreis(20.0);
        UnitFuelTwoSecondCar.setVolumen(20.2);
        UnitFuelTwoSecondCar.setTime_data("00-00-0000 | 00:00:00");

        assertFalse("Check if the changed car with its old values is not in the table of the correct car",list.contains(UnitFuelTwoSecondCar));

        cursor = mUnitDatabaseHelper.getFuelData(UnitAutoFirst.getId());
        list = new ArrayList<>();

        while(cursor.moveToNext())
            list.add(new OneFuelLoad(cursor));

        assertFalse("Check if the changed car with its old values is not in the table of the false car",list.contains(UnitFuelTwoSecondCar));

        UnitFuelTwoSecondCar.setPreis(20.0);
        UnitFuelTwoSecondCar.setVolumen(20.2);
        UnitFuelTwoSecondCar.setTime_data("00-00-0000 | 00:00:00");

        assertFalse("Check if the changed car is not in the table of the false car",list.contains(UnitFuelTwoSecondCar));

        mUnitDatabaseHelper.deleteAuto(UnitAutoFirst);
        mUnitDatabaseHelper.deleteAuto(UnitAutoSecond);
    }
}
