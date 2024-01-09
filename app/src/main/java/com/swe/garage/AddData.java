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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddData extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    ImageButton img_btn, img_add;
    EditText editName, editInner, editAusser, editKombi, editKm, editTankStand, editTankVolum, editCO2;
    Auto auto;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data);

        // ------ changing status bar color --------
        Window window = AddData.this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(AddData.this,R.color.statusBar));

        editName = findViewById(R.id.editName);
        editInner = findViewById(R.id.editInner);
        editAusser = findViewById(R.id.editAusser);
        editKombi = findViewById(R.id.editKombi);
        editKm = findViewById(R.id.editKm);
        editTankStand = findViewById(R.id.editTankStand);
        editTankVolum = findViewById(R.id.editTankVolum);
        editCO2 = findViewById(R.id.editCO2);

        mDatabaseHelper = new DatabaseHelper(this);

        // ---- active arrow back button to MainActivity ------ //
        img_btn = findViewById(R.id.img_btn);
        img_btn.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        img_add = findViewById(R.id.checkCircle);
        img_add.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  editName.getText() != null &&
                     !editInner.getText().toString().equals("") &&
                     !editAusser.getText().toString().equals("") &&
                     !editKombi.getText().toString().equals("") &&
                     !editKm.getText().toString().equals("") &&
                     !editTankStand.getText().toString().equals("") &&
                     !editTankVolum.getText().toString().equals("") &&
                     !editCO2.getText().toString().equals("")){

                    String name = editName.getText().toString();
                    double inner = Double.parseDouble(editInner.getText().toString());
                    double ausser = Double.parseDouble(editAusser.getText().toString());
                    double kombi = Double.parseDouble(editKombi.getText().toString());
                    int km = Integer.parseInt(editKm.getText().toString());
                    int tankStand = Integer.parseInt(editTankStand.getText().toString());
                    int tankVolum = Integer.parseInt(editTankVolum.getText().toString());
                    int co2 = Integer.parseInt(editCO2.getText().toString());

                    if (name.length() != 0 &&
                            (0 < inner && inner <= 15) &&
                            (0 < ausser && ausser <= 15) &&
                            (0 < kombi && kombi <= 15) &&
                            (0 < km && km <= 70000) &&
                            (0 < tankStand && tankStand <= 100) &&
                            (0 < tankVolum && tankVolum <= 60) &&
                            (0 < co2 && co2 <= 10)) {
                        auto = new Auto(-1, name, inner, ausser, kombi, km, tankStand, tankVolum, co2);
                        AddData(auto);
                        toastMessage(auto.toString() + " Fahrzeug hinzugefügt.");

                        // ---- clear editText after adding ------ //
                        editName.setText("");
                        editInner.setText("");
                        editAusser.setText("");
                        editKombi.setText("");
                        editKm.setText("");
                        editTankStand.setText("");
                        editTankVolum.setText("");
                        editCO2.setText("");
                    }
                    else
                        toastMessage("Bitte überprüfen Sie Ihre Eingaben!");
                }
                else
                    toastMessage("Bitte überprüfen Sie Ihre Eingaben!");

                Intent i = new Intent(AddData.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void AddData (Auto auto) {
        boolean insertData = mDatabaseHelper.addData(auto);
        if(insertData)
            toastMessage(auto.toString() + " Fahrzeug hinzugefügt.");
        else
            toastMessage("Bitte überprüfen Sie Ihre Eingaben!");
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}