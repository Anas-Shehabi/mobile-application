package com.swe.garage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddRoute extends AppCompatActivity {

    private static final IntentHelperSendReceivData mIntentHelperSendReceivData= new IntentHelperSendReceivData();

    ImageButton back_btn, img_add;
    EditText editKm, editTankStand;
    SeekBar seekBarKm, seekBarTank;
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioGroup TravelGroup;
    RadioButton TravelButton;


    private Auto auto;
    private Auto oldAuto;
    private DatabaseHelper mDatabaseHelper;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_route);

        // ------ changing status bar color -------- //
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(AddRoute.this,R.color.statusBar));

        //Get Data from the "Tab2_Route"
        auto = mIntentHelperSendReceivData.receivIntentAuto(getIntent());
        oldAuto = new Auto(auto);

        //grants access to the database which saves each route
        mDatabaseHelper = new DatabaseHelper(this);

        radioGroup = findViewById(R.id.radioGroup);
        TravelGroup=findViewById(R.id.TravelGroup);

        editKm = findViewById(R.id.editKm);
        editTankStand = findViewById(R.id.editTankStand);
        seekBarKm = findViewById(R.id.seekBarKm);
        seekBarTank = findViewById(R.id.seekBarTank);

        double smallest = auto.getKraftstoff_ausser();
        if (smallest > auto.getKraftstoff_inner()) smallest = auto.getKraftstoff_inner();
        if (smallest > auto.getKraftstoff_komb()) smallest = auto.getKraftstoff_komb();

        // ---- select all the number inputs -----
        editKm.setSelectAllOnFocus(true);
        editTankStand.setSelectAllOnFocus(true);

        //set default value for Range in editText
        int maxAllowedRange = (int) (((auto.getTankvolumen() * (auto.getTankstand()/100.0)) / smallest) * 110.0);  // 110 anstatt 100 da Toleranz falls hersteller Angaben ungenau
        maxAllowedRange += auto.getKilometerstand();
        editKm.setText(String.valueOf(maxAllowedRange));

        // set default value for Fuel in editText
        int maxAllowedFuel = auto.getTankstand();
        editTankStand.setText(String.valueOf(maxAllowedFuel));

        editKm.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "70000")});
        editTankStand.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "100")});

        // ----- here to active seekBar will be changed automatically while we're typing the value into editText ----- //
        int finalMaxAllowedRange = maxAllowedRange;
        int finalMaxAllowedFuel = maxAllowedFuel;

        // Default Value of Km Seekbar
        seekBarKm.setProgress(maxAllowedRange);

        // Default value of Tank Seekbar
        seekBarTank.setProgress(maxAllowedFuel);

        // Cursor always on the back
        editKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length          =   editKm.getText().length();
                editKm.setCursorVisible(true);
                editKm.setSelection(length);
            }
        });

        // Cursor always on the back
        editTankStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length          =   editTankStand.getText().length();
                editTankStand.setCursorVisible(true);
                editTankStand.setSelection(length);
            }
        });

        editKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarKm.setProgress(Integer.parseInt(s.toString()));
                    if(Integer.parseInt(s.toString()) > finalMaxAllowedRange){
                        String tmp = Integer.toString(finalMaxAllowedRange);
                        seekBarKm.setProgress(Integer.parseInt(tmp));
                    }
                } catch (Exception e) {}
            }
        });

        editTankStand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    seekBarTank.setProgress(Integer.parseInt(s.toString()));
                    if(Integer.parseInt(s.toString()) > finalMaxAllowedFuel){
                        String tmp = Integer.toString(finalMaxAllowedFuel);
                        seekBarTank.setProgress(Integer.parseInt(tmp));
                    }
                } catch (Exception e) {}
            }
        });

        // ----- here for the value in editText will be changed automatically while we're adjusting seekBar ----- //
        seekBarKm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editKm.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        // ----- here for the value in editText will be changed automatic while we adjusting seekBar ----- //
        seekBarTank.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editTankStand.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // ----- active arrow back button to previous fragment ------ //
        back_btn = findViewById(R.id.back_btn);
        back_btn.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // helps returning ro correct previous fragment (here: Tab2) instead of returning to Overview.class
            }
        });

        // ------ active add button -------- //
        img_add = findViewById(R.id.checkCircle);
        img_add.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        double finalBiggest = maxAllowedRange;
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TravelGroup.getCheckedRadioButtonId() != -1 && radioGroup.getCheckedRadioButtonId() != -1 && !editKm.getText().toString().equals("")&& Integer.parseInt(editKm.getText().toString())<= finalBiggest && !editTankStand.getText().toString().equals("")&&Integer.parseInt(editTankStand.getText().toString())<=auto.getTankstand()){
                    radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                    TravelButton = findViewById(TravelGroup.getCheckedRadioButtonId());

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    int KMStand = Integer.parseInt(editKm.getText().toString());
                    int StreckenLänge = KMStand - auto.getKilometerstand();
                    double co2 = (StreckenLänge/100) * auto.getCo2_aus();
                    double Tankverbrauch;
                    String Verbrauchsart = radioButton.getText().toString();

                    if(Verbrauchsart.equals("Innerorts"))
                        Tankverbrauch = (StreckenLänge/100) * auto.getKraftstoff_inner();
                    else if(Verbrauchsart.equals("Außerorts"))
                        Tankverbrauch = (StreckenLänge/100) * auto.getKraftstoff_ausser();
                    else
                        Tankverbrauch = (StreckenLänge/100) * auto.getKraftstoff_komb();

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy | HH:mm:ss", Locale.getDefault());
                    Route currentRoute = new Route(
                            KMStand,
                            Integer.parseInt(editTankStand.getText().toString()),
                            Verbrauchsart,
                            df.format(c),
                            auto.getId(),
                            TravelButton.getText().toString(),
                            co2,
                            StreckenLänge,
                            Tankverbrauch
                    );
                    mDatabaseHelper.addRoute(currentRoute);

                    mDatabaseHelper.LatestEntry(auto.getId(),"route", currentRoute.getId(), auto.getKilometerstand(), auto.getTankstand());

                    auto.setTankstand(Integer.parseInt(editTankStand.getText().toString()));
                    auto.setKilometerstand(Integer.parseInt(editKm.getText().toString()));


                    mDatabaseHelper.updateTankStand(auto, oldAuto);
                    mDatabaseHelper.updateKm(auto,oldAuto);

                    Intent i = new Intent (AddRoute.this, Overview.class);
                    mIntentHelperSendReceivData.setStartFragmentNumber(i, 1);
                    mIntentHelperSendReceivData.prepareIntentForSendingAuto(i, auto);
                    startActivity(i);

                    toastMessage("Streckenvorgang hinzugefügt: " + currentRoute.toString());
                }
                else if(editKm.getText().toString().equals("") || Integer.parseInt(editKm.getText().toString())> finalBiggest) {
                    toastMessage("Bitte Kilometereingabe überprüfen, Wert darf nicht " + finalBiggest + "km übersteigen");
                    editKm.setTextColor(Color.RED);
                }
                else if(editTankStand.getText().toString().equals("") || Integer.parseInt(editTankStand.getText().toString())>auto.getTankstand()){
                    toastMessage("Bitte Tankstand überprüfen, Wert darf nicht größer als " + auto.getTankstand() + " sein");
                    editKm.setTextColor(Color.BLACK);
                    editTankStand.setTextColor(Color.RED);
                }
                else{
                    toastMessage("Fehler");
                }
            }
        });


    }



    public void checkedButton (View v) {
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        TravelButton = findViewById(TravelGroup.getCheckedRadioButtonId());
    }
    public void checkedTravelButton (View v) {
        TravelButton = findViewById(TravelGroup.getCheckedRadioButtonId());
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}