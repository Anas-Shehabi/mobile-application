package com.swe.garage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author Hai Yen
 */
public class EditData extends AppCompatActivity {

    private static final String TAG = "EditData";
    ImageButton img_btn, img_update;
    EditText editName, editInner, editAusser, editKombi, editKm, editTankStand, editTankVolumen, editCO2;
    Auto autoNew, autoOld;

    IntentHelperSendReceivData mIntentHelper = new IntentHelperSendReceivData();
    DatabaseHelper mDatabaseHelper;

    String selectedName;
    double selectedInner, selectedAusser, selectedKombi;
    int selectedID, selectedKm, selectedTankStand, selectedTankVolumen, selectedCO2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data);

        // ------ changing status bar color -------- //
        Window window = EditData.this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(EditData.this,R.color.statusBar));

        img_update = findViewById(R.id.checkCircle);
        img_update.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        img_btn = findViewById(R.id.img_btn);
        img_btn.setImageResource(R.drawable.ic_baseline_arrow_back_24);

        editName = findViewById(R.id.editName);
        editAusser = findViewById(R.id.editAusser);
        editInner = findViewById(R.id.editInner);
        editKombi = findViewById(R.id.editKombi);
        editKm = findViewById(R.id.editKm);
        editTankStand = findViewById(R.id.editTankStand);
        editTankVolumen = findViewById(R.id.editTankVolum);
        editCO2 = findViewById(R.id.editCO2);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListDataActitvity
        Intent receivedIntent = getIntent();

        //get all the values we passed as extra
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("name");
        selectedInner = receivedIntent.getDoubleExtra("inner", 0);
        selectedAusser = receivedIntent.getDoubleExtra("ausser", 0);
        selectedKombi = receivedIntent.getDoubleExtra("kombi", 0);
        selectedKm = receivedIntent.getIntExtra("km", 0);
        selectedTankStand = receivedIntent.getIntExtra("tankStand", 0);
        selectedTankVolumen = receivedIntent.getIntExtra("tankVolum", 0);
        selectedCO2 = receivedIntent.getIntExtra("co2", 0);

        autoOld = new Auto (selectedID, selectedName, selectedInner, selectedAusser, selectedKombi, selectedKm, selectedTankStand, selectedTankVolumen, selectedCO2);

        //set the text to show the current selected value
        editName.setText(selectedName);
        editInner.setText(String.valueOf(selectedInner));
        editAusser.setText(String.valueOf(selectedAusser));
        editKombi.setText(String.valueOf(selectedKombi));
        editKm.setText(String.valueOf(selectedKm));
        editTankStand.setText(String.valueOf(selectedTankStand));
        editTankVolumen.setText(String.valueOf(selectedTankVolumen));
        editCO2.setText(String.valueOf(selectedCO2));

        img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean error = false;
                if(  !editName.getText().toString().equals("") &&
                    !editInner.getText().toString().equals("") &&
                    !editAusser.getText().toString().equals("") &&
                    !editKombi.getText().toString().equals("") &&
                    !editKm.getText().toString().equals("") &&
                    !editTankStand.getText().toString().equals("") &&
                    !editTankVolumen.getText().toString().equals("") &&
                    !editCO2.getText().toString().equals("")) {

                    autoNew = new Auto(selectedID, editName.getText().toString(), Double.parseDouble(editInner.getText().toString()), Double.parseDouble(editAusser.getText().toString()),
                            Double.parseDouble(editKombi.getText().toString()), Integer.parseInt(editKm.getText().toString()), Integer.parseInt(editTankStand.getText().toString()),
                            Integer.parseInt(editTankVolumen.getText().toString()), Integer.parseInt(editCO2.getText().toString()));


                    // next Activity is Overview and in Overview we'll get the intent extra
                    // => we have to putExtra new value after updating, otherwise no data will be saved if clicking "Edit" again.
                    Intent i = new Intent(EditData.this, Overview.class);
                    i.putExtra("id", autoNew.getId());

                    if (autoNew.getAnzeigename().length() != 0) {
                        i.putExtra("name", autoNew.getAnzeigename());
                        mDatabaseHelper.updateName(autoNew, autoOld);
                    } else {
                        i.putExtra("name", autoOld.getAnzeigename());
                        error = true;
                    }

                    if (0 < autoNew.getKraftstoff_inner() && autoNew.getKraftstoff_inner() <= 15) {
                        i.putExtra("inner", autoNew.getKraftstoff_inner());
                        mDatabaseHelper.updateKraftStoffInner(autoNew, autoOld);
                    } else {
                        i.putExtra("inner", autoOld.getKraftstoff_inner());
                        error = true;
                    }


                    if (0 < autoNew.getKraftstoff_ausser() && autoNew.getKraftstoff_ausser() <= 15) {
                        i.putExtra("ausser", autoNew.getKraftstoff_ausser());
                        mDatabaseHelper.updateKraftStoffAusser(autoNew, autoOld);
                    } else {
                        i.putExtra("ausser", autoOld.getKraftstoff_ausser());
                        error = true;
                    }


                    if (0 < autoNew.getKraftstoff_komb() && autoNew.getKraftstoff_komb() <= 15) {
                        i.putExtra("kombi", autoNew.getKraftstoff_komb());
                        mDatabaseHelper.updateKraftStoffKombi(autoNew, autoOld);
                    } else {
                        i.putExtra("kombi", autoOld.getKraftstoff_komb());
                        error = true;
                    }


                    if (0 <= autoNew.getKilometerstand() && autoNew.getKilometerstand() <= 70000) {
                        i.putExtra("km", autoNew.getKilometerstand());
                        mDatabaseHelper.updateKm(autoNew, autoOld);
                    } else {
                        i.putExtra("km", autoOld.getKilometerstand());
                        error = true;
                    }


                    if (0 < autoNew.getTankstand() && autoNew.getTankstand() <= 100) {
                        i.putExtra("tankStand", autoNew.getTankstand());
                        mDatabaseHelper.updateTankStand(autoNew, autoOld);
                    } else {
                        i.putExtra("tankStand", autoOld.getTankstand());
                        error = true;
                    }


                    if (0 < autoNew.getTankvolumen() && autoNew.getTankvolumen() <= 60) {
                        i.putExtra("tankVolum", autoNew.getTankvolumen());
                        mDatabaseHelper.updateTankVolumen(autoNew, autoOld);
                    } else {
                        i.putExtra("tankVolum", autoOld.getTankvolumen());
                        error = true;
                    }


                    if (0 < autoNew.getCo2_aus() && autoNew.getCo2_aus() <= 10) {
                        i.putExtra("co2", autoNew.getCo2_aus());
                        mDatabaseHelper.updateCO2(autoNew, autoOld);
                    } else {
                        i.putExtra("co2", autoOld.getCo2_aus());
                        error = true;
                    }


                    if (error)
                        toastMessage("Ihre Eingaben können nicht aktualisiert werden. Bitte versuche es erneut!");
                    else
                        toastMessage("Ihr Fahrzeug wurde aktualisiert.");

                    startActivity(i);
                }
                else
                {
                    toastMessage("Ihre Eingaben können nicht aktualisiert werden. Bitte versuche es erneut!");

                    Intent i = new Intent (EditData.this, Overview.class);
                    mIntentHelper.prepareIntentForSendingAuto(i,autoOld);
                    startActivity(i);
                }

            }
        });

        // ----- active button "Back" to Overview activity
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // helps returning to right fragment (here: Tab2) instead of returning to Overview.class
            }
        });
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}