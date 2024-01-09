package com.swe.garage;

import android.database.Cursor;

/**
 * Klasse die als Grundlage fuer das hinzufuegen eines Autos fungiert.
 * @author Florian Dohmen
 * @version 1.0
 */
public class Auto {

    /**
     * Wert des ID
     */
    private int id;

    /**
     * Wert des Anzeigenamens
     */
    private String anzeigename;

    /**
     * Wert des inneroertlichen Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    private double kraftstoff_inner;

    /**
     * Wert des kombinierten Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    private double kraftstoff_komb;

    /**
     * Wert des ausseroertlichen Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    private double kraftstoff_ausser;

    /**
     * Wert des Kilometerstandes (in Kilometer)
     */
    private int kilometerstand;

    /**
     * Wert des Tankstandes (in Prozent)
     */
    private int tankstand;

    /**
     * Wert des Tankvolumens (in Liter/kWh)
     */
    private int tankvolumen;

    /**
     * Wert des CO2-Ausstosses (in kg/100km)
     */
    private int co2_aus;


    /**
     * Kontruktor zur erstellung eines neuen Objekts der Klasse Auto
     * @param anzeigename zukuenftiger Wert des Anzeigenamens
     * @param kraftstoff_inner zukuenfitger Wert des inneroertlichen Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     * @param kraftstoff_komb zukuenftiger Wert des kombinierten Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     * @param kraftstoff_ausser zukuenftiger Wert des ausseroertlichen Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     * @param kilometerstand zukuenftiger Wert des Kilometerstandes (in Kilometer)
     * @param tankstand zukuenftiger Wert des Tankstandes (in Prozent)
     * @param tankvolumen zukuenftiger Wert des Tankvolumens (in Liter/kWh)
     * @param co2_aus zukuenftiger Wert des CO2-Ausstosses (in kg/100km)
     */
    public Auto(int id, String anzeigename, double kraftstoff_inner, double kraftstoff_ausser, double kraftstoff_komb, int kilometerstand, int tankstand, int tankvolumen, int co2_aus) {
        this.id = id;
        this.anzeigename = anzeigename;
        this.kraftstoff_inner = kraftstoff_inner;
        this.kraftstoff_ausser = kraftstoff_ausser;
        this.kraftstoff_komb = kraftstoff_komb;
        this.kilometerstand = kilometerstand;
        this.tankstand = tankstand;
        this.tankvolumen = tankvolumen;
        this.co2_aus = co2_aus;
    }

    public Auto (Cursor auto) {
        this.id = Integer.valueOf(auto.getString(0));
        this.anzeigename = auto.getString(1);
        this.kraftstoff_inner = Double.valueOf(auto.getString(2));
        this.kraftstoff_ausser = Double.valueOf(auto.getString(3));
        this.kraftstoff_komb = Double.valueOf(auto.getString(4));
        this.kilometerstand = Integer.valueOf((auto.getString(5)));
        this.tankstand = Integer.valueOf((auto.getString(6)));
        this.tankvolumen = Integer.valueOf((auto.getString(7)));
        this.co2_aus = Integer.valueOf((auto.getString(8)));
    }

    public Auto(Auto auto)
    {
        this.id = auto.id;
        this.anzeigename = auto.anzeigename;
        this.kraftstoff_inner = auto.kraftstoff_inner;
        this.kraftstoff_ausser = auto.kraftstoff_ausser;
        this.kraftstoff_komb = auto.kraftstoff_komb;
        this.kilometerstand = auto.kilometerstand;
        this.tankstand = auto.tankstand;
        this.tankvolumen = auto.tankvolumen;
        this.co2_aus = auto.co2_aus;
    }

    public Auto() {
    }

    @Override
    public boolean equals(Object obj)
    {
        Auto auto = (Auto) obj;
        if(this.id!=auto.id)
            return false;
        if(!this.anzeigename.equals(auto.anzeigename))
            return false;
        if(this.kraftstoff_inner!=auto.kraftstoff_inner)
            return false;
        if(this.kraftstoff_ausser!=auto.kraftstoff_ausser)
            return false;
        if(this.getKraftstoff_komb()!=auto.kraftstoff_komb)
            return false;
        if(this.kilometerstand!=auto.kilometerstand)
            return false;
        if(this.tankstand!=auto.tankstand)
            return false;
        if(this.tankvolumen!=auto.tankvolumen)
            return false;
        if(this.co2_aus!=auto.co2_aus)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return anzeigename; // for displaying in listView with names only
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter des Anzeigenamens
     * @param anz zukuenftiger Wert des Anzeigenamens
     */
    public void setAnzeigename(String anz) {
        this.anzeigename = anz;
    }

    /**
     * Getter des Anzeigenamens
     * @return Gibt den Anzeigenamen des Attributes zurueck
     */
    public String getAnzeigename() {
        return anzeigename;
    }

    /**
     * Setter fuer den inneroertlichen Kraftstoffverbrauch
     * @param kraft_inner zukuenftiger Wert des inneroertlichen Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    public void setKraftstoff_inner(double kraft_inner) {
        this.kraftstoff_inner = kraft_inner;
    }

    /**
     * Getter fuer den inneroertlichen Kraftstoffverbrauch
     * @return Gibt den inneroertlichen Kraftstoffverbrauch zurueck (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    public double getKraftstoff_inner(){
        return  kraftstoff_inner;
    }

    /**
     * Setter fuer den kombinierten Kraftstoffverbrauch
     * @param kraft_komb zukuenftiger Wert des kombinierten Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    public void setKraftstoff_komb(double kraft_komb) {
        this.kraftstoff_komb = kraft_komb;
    }

    /**
     * Getter fuer den kombinierten Kraftstoffverbrauch
     * @return Gibt den kombinierten Kraftstoffverbrauch zurueck (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    public double getKraftstoff_komb() {
        return kraftstoff_komb;
    }

    /**
     * Setter fuer den ausseroertlichen Kraftstoffverbrauch
     * @param kraft_ausser zukuenftiger Wert des ausseroertlichen Kraftstoffverbrauchs (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    public void setKraftstoff_ausser(double kraft_ausser){
        this.kraftstoff_ausser = kraft_ausser;
    }

    /**
     * Getter fuer den ausseroertlichen Kraftstoffverbrauch
     * @return Gibt den kombinierten Kraftstoffverbrauch zurueck (in Liter bzw. Kilowattstunden pro 100 Kilometer)
     */
    public double getKraftstoff_ausser() {
        return  kraftstoff_ausser;
    }

    /**
     * Setter fuer den Kilometerstand
     * @param kilom_stand zukuenftiger Wert fuer den Kilometerstand (in Kilometer)
     */
    public void setKilometerstand(int kilom_stand) {
        this.kilometerstand = kilom_stand;
    }

    /**
     * Getter fuer den Kilometerstand
     * @return Gibt den Kilometerstand zurueck (in Kilometer)
     */
    public int getKilometerstand() {
        return kilometerstand;
    }

    /**
     * Setter fuer den Tankstand
     * @param tank_stand zukuenftiger Wert fuer den Tankstand (in Prozent)
     */
    public void setTankstand(int tank_stand) {
        this.tankstand = tank_stand;
    }

    /**
     * Getter fuer den Tankstand
     * @return Gibt den Tankstand zurueck (in Prozent)
     */
    public int getTankstand() {
        return tankstand;
    }

    /**
     * Setter fuer das Tankvolumen
     * @param tank_volumen zukuenftiger Wert fuer das Tankvolumen (in Liter/kWh)
     */
    public void setTankvolumen(int tank_volumen) {
        this.tankvolumen = tank_volumen;
    }

    /**
     * Getter fuer das Tankvolumen
     * @return Gibt das Tankvolumen zurueck (in Liter/kWh)
     */
    public int getTankvolumen() {
        return  tankvolumen;
    }

    /**
     * Setter fuer den CO2-Ausstoss
     * @param co2_a zukuenftiger Wert fuer den CO2-Ausstoss (in kg/100km)
     */
    public void setCo2_aus(int co2_a) {
        this.co2_aus = co2_a;
    }

    /**
     * Getter fuer den CO2-Ausstoss
     * @return Gibt den CO2-Ausstoss zurueck (in kg/100km)
     */
    public int getCo2_aus() {
        return co2_aus;
    }
}
