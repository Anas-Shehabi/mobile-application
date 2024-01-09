package com.swe.garage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YOURCAR_DATABASE";

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "vehicle_table";
    private static final String COL0 = "ID";
    private static final String COL1 = "name";
    private static final String COL2 = "verbrauch_inner";
    private static final String COL3 = "verbrauch_ausser";
    private static final String COL4 = "verbrauch_kombiniert";
    private static final String COL5 = "kilometer_stand";
    private static final String COL6 = "tank_stand";
    private static final String COL7 = "tank_volumen";
    private static final String COL8 = "co2_ausstoss";

    private static final String TAG_FUELS = "DatabaseHelperForFuels";
    private static final String TABLE_NAME_FUELS = "fuel_table";
    private static final String COL0FUELS = "id";
    private static final String COL1FUELS = "volumen";
    private static final String COL2FUELS = "preis";
    private static final String COL3FUELS = "time_data";
    private static final String COL4FUELS = "picture";
    private static final String COL5FUELS = "carID";

    private static final String TAG_ROUTES = "DatabaseHelperForRoute";
    private static final String TABLE_NAME_ROUTES = "route_table";
    private static final String COL0_ROUTES = "id";
    private static final String COL1_ROUTES = "km";
    private static final String COL2_ROUTES = "tank";
    private static final String COL3_ROUTES = "route_type";
    private static final String COL4_ROUTES = "time_data";
    private static final String COL5_ROUTES = "carID";
    private static final String COL6_ROUTES = "travel_type";
    private static final String COL7_ROUTES = "CO2_ausstoß";
    private static final String COL8_ROUTES = "länge_in_km";
    private static final String COL9_ROUTES = "kraftstoffverbrauch";

    private static final String TAG_WORKSHOP = "DatabaseHelperWorkshops";
    private static final String TABLE_NAME_WORKSHOP = "workshop_table";
    private static final String COL0_WORKSHOP = "id";
    private static final String COL1_WORKSHOP = "description";
    private static final String COL2_WORKSHOP = "time_data";
    private static final String COL3_WORKSHOP = "placeholder_picture";
    private static final String COL4_WORKSHOP = "carID";

    private static final String TABLE_NAME_NEWEST_ENTRY = "DatabaseHelperEntry";
    private static final String COL0_ENTRY = "carID";
    private static final String COL1_ENTRY = "type";
    private static final String COL2_ENTRY = "ID_Entry";
    private static final String COL3_ENTRY = "KM_before";
    private static final String COL4_ENTRY = "Tankstand_before";


    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableVehicle = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +  COL1 + " TEXT,"
                + COL2 + " REAL," + COL3 + " REAL," + COL4 + " REAL," + COL5 + " INTEGER," + COL6 + " INTEGER,"
                + COL7 + " INTEGER," + COL8 + " INTEGER)";
        db.execSQL(createTableVehicle);

        String createTableFuels = "CREATE TABLE " + TABLE_NAME_FUELS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +  COL1FUELS + " INTEGER,"
                + COL2FUELS + " REAL," + COL3FUELS + " TEXT," +  COL4FUELS + " BLOB," + COL5FUELS + " INTEGER, CONSTRAINT fuel_to_car FOREIGN KEY("+COL5FUELS+") REFERENCES vehicle_table(ID) ON DELETE CASCADE);";
        db.execSQL(createTableFuels);

        String createTableRoutes = "CREATE TABLE " + TABLE_NAME_ROUTES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +  COL1_ROUTES + " INTEGER,"
                + COL2_ROUTES + " INTEGER," + COL3_ROUTES + " TEXT," + COL4_ROUTES + " TEXT," + COL5_ROUTES + " INTEGER," + COL6_ROUTES + " TEXT," + COL7_ROUTES +" REAL," + COL8_ROUTES +" INTEGER, " + COL9_ROUTES + " REAL, CONSTRAINT route_to_car FOREIGN KEY (" + COL5_ROUTES + ") REFERENCES vehicle_table(ID) ON DELETE CASCADE);";
        db.execSQL(createTableRoutes);

        String createTable = "CREATE TABLE " + TABLE_NAME_WORKSHOP + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +  COL1_WORKSHOP + " TEXT,"
                + COL2_WORKSHOP + " TEXT, " +  COL3_WORKSHOP  + " BLOB, " + COL4_WORKSHOP + " INTEGER, " + "CONSTRAINT worskhop_to_car FOREIGN KEY (" + COL4_WORKSHOP + ") REFERENCES vehicle_table(ID) ON DELETE CASCADE)";
        db.execSQL(createTable);

        String createTableEntries = "CREATE TABLE " + TABLE_NAME_NEWEST_ENTRY + "(" + COL0_ENTRY + " INTEGER PRIMARY KEY, " + COL1_ENTRY + " TEXT, " + COL2_ENTRY + " INTEGER, "+ COL3_ENTRY + " INTEGER, " + COL4_ENTRY + " INTEGER, CONSTRAINT entry_to_car FOREIGN KEY (" + COL0_ENTRY + ") REFERENCES vehicle_table(ID) ON DELETE CASCADE)";
        db.execSQL(createTableEntries);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FUELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WORKSHOP);
        onCreate(db);
    }


    public boolean addData (Auto auto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1, auto.getAnzeigename());
        contentValues.put(COL2, auto.getKraftstoff_inner());
        contentValues.put(COL3, auto.getKraftstoff_ausser());
        contentValues.put(COL4, auto.getKraftstoff_komb());
        contentValues.put(COL5, auto.getKilometerstand());
        contentValues.put(COL6, auto.getTankstand());
        contentValues.put(COL7, auto.getTankvolumen());
        contentValues.put(COL8, auto.getCo2_aus());

        Log.d(TAG, "addData: Adding " + auto.getAnzeigename() + ", " + auto.getKraftstoff_inner() + ", " + auto.getKraftstoff_ausser() + ", " + auto.getKraftstoff_komb() + ", "
                + auto.getKilometerstand() + ", " + auto.getTankstand() + ", " + auto.getTankvolumen() + ", " + auto.getCo2_aus() + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        auto.setId((int)result);

        if (result == -1)
            return false;
        else
            return true;
    }

    /**
     * returns all data from db
     * @return
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }


    public Cursor getAutoID (Auto auto) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL0 + " FROM " + TABLE_NAME +
                " WHERE " + COL1 + " = '" + auto.getAnzeigename() + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void updateName(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL1 +
                " = '" + autoNew.getAnzeigename() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL1 + " = '" + autoOld.getAnzeigename() + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name " + autoOld.getAnzeigename() + " to " + autoNew.getAnzeigename());
        db.execSQL(query);
    }

    public void updateKraftStoffInner(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + autoNew.getKraftstoff_inner() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL2 + " = '" + autoOld.getKraftstoff_inner() + "'";

        Log.d(TAG, "updateKraftStoffInner: query: " + query);
        Log.d(TAG, "updateKraftStoffInner: Setting kraftStoffInner " + autoOld.getKraftstoff_inner() + " to " + autoNew.getKraftstoff_inner());
        db.execSQL(query);
    }

    public void updateKraftStoffAusser(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 +
                " = '" + autoNew.getKraftstoff_ausser() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL3 + " = '" + autoOld.getKraftstoff_ausser() + "'";

        Log.d(TAG, "updateKraftStoffAusser: query: " + query);
        Log.d(TAG, "updateKraftStoffAusser: Setting kraftStoffAusser " + autoOld.getKraftstoff_ausser() + " to " + autoNew.getKraftstoff_ausser());
        db.execSQL(query);
    }

    public void updateKraftStoffKombi(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL4 +
                " = '" + autoNew.getKraftstoff_komb() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL4 + " = '" + autoOld.getKraftstoff_komb() + "'";

        Log.d(TAG, "updateKraftStoffKombi: query: " + query);
        Log.d(TAG, "updateKraftStoffKombi: Setting kraftStoffKombi " + autoOld.getKraftstoff_komb() + " to " + autoNew.getKraftstoff_komb());
        db.execSQL(query);
    }

    public void updateKm(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL5 +
                " = '" + autoNew.getKilometerstand() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL5 + " = '" + autoOld.getKilometerstand() + "'";

        Log.d(TAG, "updateKm: query: " + query);
        Log.d(TAG, "updateKm: Setting KmStand " + autoOld.getKilometerstand() + " to " + autoNew.getKilometerstand());
        db.execSQL(query);
    }

    public void updateTankStand(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL6 +
                " = '" + autoNew.getTankstand() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL6 + " = '" + autoOld.getTankstand() + "'";

        Log.d(TAG, "updateTankStand: query: " + query);
        Log.d(TAG, "updateTankStand: Setting tankStand " + autoOld.getTankstand() + " to " + autoNew.getTankstand());
        db.execSQL(query);
    }

    public void updateTankVolumen(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL7 +
                " = '" + autoNew.getTankvolumen() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL7 + " = '" + autoOld.getTankvolumen() + "'";

        Log.d(TAG, "updateTankVolumen: query: " + query);
        Log.d(TAG, "updateTankVolumen: Setting tankVolumen " + autoOld.getTankvolumen() + " to " + autoNew.getTankvolumen());
        db.execSQL(query);
    }

    public void updateCO2(Auto autoNew, Auto autoOld) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL8 +
                " = '" + autoNew.getCo2_aus() + "' WHERE " + COL0 + " = '" + autoOld.getId() + "'" +
                " AND " + COL8 + " = '" + autoOld.getCo2_aus() + "'";

        Log.d(TAG, "updateCO2: query: " + query);
        Log.d(TAG, "updateCO2: Setting co2 " + autoOld.getCo2_aus() + " to " + autoNew.getCo2_aus());
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param auto
     */
    public void deleteAuto(Auto auto){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL0 + " = '" + auto.getId() + "'" +
                " AND " + COL1 + " = '" + auto.getAnzeigename() + "'";
        Log.d(TAG, "deleteAuto: query: " + query);
        Log.d(TAG, "deleteAuto: Deleting " + auto.getAnzeigename() + " from database.");
        db.execSQL(query);
    }

    public void clearTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
        String query_fuels = "DELETE FROM " + TABLE_NAME_FUELS;
        db.execSQL(query_fuels);
        String query_routes = "DELETE FROM " + TABLE_NAME_ROUTES;
        db.execSQL(query_routes);
    }


    /**
     *
     *
     *      Fuel Methods
     *
     */

    public Cursor getFuelData(int carID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_FUELS +" WHERE "+ COL5FUELS +" = '" + Integer.toString(carID) + "' ORDER BY " + COL0FUELS + " DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getAllFuelData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_FUELS +" ORDER BY " + COL0FUELS + " DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addFuel(OneFuelLoad fuel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1FUELS, fuel.getVolumen());
        contentValues.put(COL2FUELS, fuel.getPreis());
        contentValues.put(COL3FUELS, fuel.time_data());
        contentValues.put(COL4FUELS, fuel.getPlaceholder_picture());
        contentValues.put(COL5FUELS, fuel.getCarID());

        long result = db.insert(TABLE_NAME_FUELS, null, contentValues);

        fuel.setId((int) result);

        Log.d(TAG_FUELS, "addData: Adding Fuel with ID: " + fuel.getId() + ", " + fuel.getVolumen() + ", " + fuel.getPreis() + ", " + fuel.time_data() + ", "
                + fuel.getCarID() + " to " + TABLE_NAME_FUELS);
        return result != -1;
    }

    public void UpdateFuel(OneFuelLoad fuel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_FUELS + " WHERE " + COL0FUELS + " = " + String.valueOf(fuel.getId());
        db.execSQL(query);
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL0FUELS, fuel.getId());
        contentValues.put(COL1FUELS, fuel.getVolumen());
        contentValues.put(COL2FUELS, fuel.getPreis());
        contentValues.put(COL3FUELS, fuel.time_data());
        contentValues.put(COL4FUELS, fuel.getPlaceholder_picture());
        contentValues.put(COL5FUELS, fuel.getCarID());
        db.insert(TABLE_NAME_FUELS, null, contentValues);
        Log.d(TAG_FUELS, "EditData: Updating Fuel with ID: " + fuel.getId() + ", " + fuel.getVolumen() + ", " + fuel.getPreis() + ", " + fuel.time_data() + ", "
                + fuel.getCarID() + " to " + TABLE_NAME_FUELS);
    }


    /**
     *
     *
     *      Route Methods
     *
     */

    public void updateRoute(Route currentRoute){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_ROUTES + " WHERE " + COL0_ROUTES + " = " + String.valueOf(currentRoute.getId());
        db.execSQL(query);

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL0_ROUTES, currentRoute.getId());
        contentValues.put(COL1_ROUTES, currentRoute.getKm());
        contentValues.put(COL2_ROUTES, currentRoute.getTank());
        contentValues.put(COL3_ROUTES, currentRoute.getRoute_type());
        contentValues.put(COL4_ROUTES, currentRoute.getTime_data());
        contentValues.put(COL5_ROUTES, currentRoute.getCarID());
        contentValues.put(COL6_ROUTES, currentRoute.getTravel_type());
        contentValues.put(COL7_ROUTES, currentRoute.getCO2_ausstoß());
        contentValues.put(COL8_ROUTES, currentRoute.getLänge_in_km());
        contentValues.put(COL9_ROUTES, currentRoute.getKraftstoffverbrauch());
        db.insert(TABLE_NAME_ROUTES, null, contentValues);
    }

    public Cursor getRouteData(int carID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_ROUTES + " WHERE " + COL5_ROUTES + " = '" + Integer.toString(carID) + "' ORDER BY " + COL0_ROUTES + " DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addRoute(Route route){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1_ROUTES, route.getKm());
        contentValues.put(COL2_ROUTES, route.getTank());
        contentValues.put(COL3_ROUTES, route.getRoute_type());
        contentValues.put(COL4_ROUTES, route.getTime_data());
        contentValues.put(COL5_ROUTES, route.getCarID());
        contentValues.put(COL6_ROUTES, route.getTravel_type());
        contentValues.put(COL7_ROUTES, route.getCO2_ausstoß());
        contentValues.put(COL8_ROUTES, route.getLänge_in_km());
        contentValues.put(COL9_ROUTES, route.getKraftstoffverbrauch());


        Log.d(TAG_ROUTES, "addData: Adding Route with ID: " + route.getId() + ", " + route.getKm() + ", " + route.getTank()
                + ", " + route.getRoute_type() + ", " + route.getTime_data() + ", " + route.getCarID() + " to " + TABLE_NAME_ROUTES);

        long result = db.insert(TABLE_NAME_ROUTES, null, contentValues);

        route.setId((int)result);

        return result != -1;
    }

    /**
     *
     *
     *      Workshop Methods
     *
     */

    public Cursor getWorkshopData(int carID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_WORKSHOP + " WHERE " + COL4_WORKSHOP + " = '" + Integer.toString(carID) + "' ORDER BY " + COL0_WORKSHOP + " DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addWorkshop(Workshop workshop){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1_WORKSHOP, workshop.getDescription());
        contentValues.put(COL2_WORKSHOP, workshop.getTime_data());
        contentValues.put(COL3_WORKSHOP, workshop.getPlaceholder_picture());
        contentValues.put(COL4_WORKSHOP, workshop.getCarID());


        Log.d(TAG_WORKSHOP, "addData: Adding Workshop with ID: " + workshop.getId() + ", "
                + workshop.getDescription() + ", "+ workshop.getTime_data() + ", " + workshop.getCarID() + " to " + TABLE_NAME_WORKSHOP);

        long result = db.insert(TABLE_NAME_WORKSHOP, null, contentValues);

        workshop.setId((int)result);

        return result != -1;
    }

    public void upDateWorkshop(Workshop workshop)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME_WORKSHOP + " WHERE " + COL0_WORKSHOP + " = " + String.valueOf(workshop.getId());
        db.execSQL(query);


        ContentValues contentValues = new ContentValues();

        contentValues.put(COL0_WORKSHOP, workshop.getId());
        contentValues.put(COL1_WORKSHOP, workshop.getDescription());
        contentValues.put(COL2_WORKSHOP, workshop.getTime_data());
        contentValues.put(COL3_WORKSHOP, workshop.getPlaceholder_picture());
        contentValues.put(COL4_WORKSHOP, workshop.getCarID());
        db.insert(TABLE_NAME_WORKSHOP, null, contentValues);
        Log.d(TAG_WORKSHOP, "EditData: Updating Workshop with ID: " + workshop.getId());
    }


    /**
     *  Entry methods
     */


    /**
     *
     * @param AutoID ID of the car of which the last entry is needed
     * @param type  route or fuel for the entry
     * @param ID_Entry  id of the entry
     * @return returns true if this entry is the lastest
     */
    public boolean ReturnDetailsLastEntry(int AutoID, String type, int ID_Entry){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_NEWEST_ENTRY + " WHERE " + COL0_ENTRY + " = " + String.valueOf(AutoID);
        Cursor data = db.rawQuery(query, null);

        if (data.moveToFirst())
            return type.equals(data.getString(1)) && ID_Entry == data.getInt(2);
        return false;
    }

    public void LatestEntry(int AutoID, String type, int ID_Entry, int optinal_km, int optional_tankstand)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME_NEWEST_ENTRY + " WHERE " + COL0_ENTRY + " = " + String.valueOf(AutoID);
        db.execSQL(query);

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL0_ENTRY, AutoID);
        contentValues.put(COL1_ENTRY, type);
        contentValues.put(COL2_ENTRY, ID_Entry);
        contentValues.put(COL3_ENTRY, optinal_km);
        contentValues.put(COL4_ENTRY, optional_tankstand);
        db.insert(TABLE_NAME_NEWEST_ENTRY, null, contentValues);
    }

    public Cursor getCursorOfLastEntry(int AutoID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_NEWEST_ENTRY + " WHERE " + COL0_ENTRY + " = " + AutoID;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}