package com.swe.garage;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditRefueling extends AppCompatActivity {

        private static final IntentHelperSendReceivData mIntentHelperSendReceivData= new IntentHelperSendReceivData();

        private Auto auto;
        private Auto oldAuto; //for Database update
        private OneFuelLoad fuel;
        private Bitmap bitmap = null;
        private Button imageButton = null;
        private ImageButton img_btn_left, img_btn_right;
        private ImageView imageView;
        private EditText volumenText, preisText;
        private DatabaseHelper mDatabaseHelper;
        private boolean errFirstInput = false, errSecondInput = false, photoTaken = false;
        boolean editable;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_refueling);

            // ------ changing status bar color -------- //
            Window window = com.swe.garage.EditRefueling.this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(com.swe.garage.EditRefueling.this, R.color.statusBar));

            //Get Data from the "Tab3_GasSation"
            auto = mIntentHelperSendReceivData.receivIntentAuto(getIntent());
            fuel = mIntentHelperSendReceivData.receivIntentFuel(getIntent());
            //Backup for Database Update
            oldAuto = new Auto(auto);

            //grants access to the database which saves each refueling and Auto
            mDatabaseHelper = new DatabaseHelper(this);

            editable = mDatabaseHelper.ReturnDetailsLastEntry(auto.getId(),"fuel", fuel.getId());

            //calculates the highest Value which can fit in the fuel
            int maxTankVolumen = (int) (auto.getTankvolumen()*((100-auto.getTankstand())+(fuel.getVolumen()/auto.getTankvolumen())*100)*0.01);


            volumenText = findViewById(R.id.fuelQuantityText);
            volumenText.setHint("Bitte Wert bis "+ maxTankVolumen);
            volumenText.setText(String.valueOf(fuel.getVolumen()));

            preisText = findViewById(R.id.preisText);
            preisText.setHint("Bitte eingabe vornehmen");
            preisText.setText(String.valueOf(fuel.getPreis()));

            imageButton = findViewById(R.id.imagebutton);

            imageView = findViewById(R.id.imageview);

            if(!editable){
                volumenText.setEnabled(false);
                preisText.setEnabled(false);
                toastMessage("Nur Foto ist bearbeitbar");
            }

            if(fuel.getPlaceholder_picture()!=null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(fuel.getPlaceholder_picture(), 0, fuel.getPlaceholder_picture().length));

            //Creates the Action that the Return button returns the App to the Overview Activity
            img_btn_left = findViewById(R.id.img_btn_left);
            img_btn_left.setImageResource(R.drawable.ic_baseline_arrow_back_24);
            img_btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            /**
             * create the onClick action for the confirm button in the top right corner
             */
            img_btn_right = findViewById(R.id.checkCircle);
            img_btn_right.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            img_btn_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(volumenText.getText().toString().equals("")||Double.parseDouble(volumenText.getText().toString())<=0||volumenText.getText().toString().isEmpty()||maxTankVolumen<Double.parseDouble(volumenText.getText().toString())) {
                        errFirstInput = true;
                        volumenText.setTextColor(Color.RED);
                        toastMessage("Bitte Tankvolumen überprüfen!");
                    }
                    else {
                        volumenText.setTextColor(Color.BLACK);
                        errFirstInput = false;
                    }

                    //Preis can be a double value
                    if(preisText.getText().toString().equals("")||Double.parseDouble(preisText.getText().toString())<=0||volumenText.getText().toString().isEmpty()){
                        errSecondInput = true;
                        preisText.setTextColor(Color.RED);
                        toastMessage("Bitte Preis überprüfen!");
                    }
                    else {
                        preisText.setTextColor(Color.BLACK);
                        errSecondInput = false;
                    }

                    if(!errFirstInput&&!errSecondInput) {

                        //calculates the additional Percent for the fuel
                        double newfuelPercent = ((Double.parseDouble(volumenText.getText().toString()) / auto.getTankvolumen()) * 100.0 - (fuel.getVolumen() / auto.getTankvolumen()) * 100.0);

                        auto.setTankstand((int) ((newfuelPercent) + auto.getTankstand()));

                        fuel.setVolumen(Double.parseDouble(volumenText.getText().toString()));
                        fuel.setPreis(Double.parseDouble(preisText.getText().toString()));
                        fuel.setPlaceholder_picture(getBitmapAsByteArray(bitmap, photoTaken));
                        //Database Updates
                        mDatabaseHelper.UpdateFuel(fuel);
                        mDatabaseHelper.updateTankStand(auto, oldAuto);

                        Intent i = new Intent(com.swe.garage.EditRefueling.this, Overview.class);

                        mIntentHelperSendReceivData.prepareIntentForSendingAuto(i, auto);
                        mIntentHelperSendReceivData.setStartFragmentNumber(i, 2);
                        startActivity(i);

                        toastMessage("Tankvorgang bearbeitet: " + fuel.toString());

                    }
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,100);
                }
            });

            if(ContextCompat.checkSelfPermission(com.swe.garage.EditRefueling.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(com.swe.garage.EditRefueling.this, new String[]{
                        Manifest.permission.CAMERA
                },100);
            }
        }

        /**
         * handle requestCodes for example, if a picture is take(code 100) then it will be displayed
         * @param requestCode
         * @param resultCode
         * @param data
         */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
            super.onActivityResult(requestCode,resultCode,data);
            if(requestCode == 100&&data!=null&&data.getExtras()!=null) {
                if(data.getExtras()!=null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    photoTaken = true;
                }
                else
                    photoTaken = false;
            }
        }


        /**
         *
         * @param bitmap Picture taken
         * @return Picture in byte[] format
         */
        public static byte[] getBitmapAsByteArray(Bitmap bitmap,boolean photoTaken) {
            if(photoTaken) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                return outputStream.toByteArray();
            }
            return null;
        }


        /**
         * customizable toast
         * @param message
         */
        private void toastMessage (String message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

}