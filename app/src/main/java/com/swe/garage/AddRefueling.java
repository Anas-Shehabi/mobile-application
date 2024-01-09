package com.swe.garage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 *  Activity for the ADDING of a new Refueling, this Activity is NOT used for editing a already existing Fuel
 */
public class AddRefueling extends AppCompatActivity {

    private static final IntentHelperSendReceivData mIntentHelperSendReceivData= new IntentHelperSendReceivData();

    private Auto auto;
    private Auto oldAuto; //for Database update
    private Bitmap bitmap = null;
    private Button imageButton = null;
    private ImageButton img_btn_left, img_btn_right;
    private ImageView imageView;
    private EditText volumenText, preisText;
    private DatabaseHelper mDatabaseHelper;
    private boolean errFirstInput = false, errSecondInput = false, photoTaken = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_refueling);

        // ------ changing status bar color -------- //
        Window window = AddRefueling.this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(AddRefueling.this, R.color.statusBar));

        //Get Data from the "Tab3_GasSation"
        auto = mIntentHelperSendReceivData.receivIntentAuto(getIntent());
        //Backup for Database Update
        oldAuto = new Auto(auto);

        //grants access to the database which saves each refueling and Auto

        mDatabaseHelper = new DatabaseHelper(this);

        //calculates the highest Value which can fit in the fuel
        int maxTankVolumen = (int) (auto.getTankvolumen()*((100-auto.getTankstand())*0.01));


        volumenText =findViewById(R.id.fuelQuantityText);
        volumenText.setHint("Bitte Wert bis "+ maxTankVolumen);

        preisText = findViewById(R.id.preisText);
        preisText.setHint("Bitte eingabe vornehmen");

        imageButton = findViewById(R.id.imagebutton);

        imageView = findViewById(R.id.imageview);

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
                if(preisText.getText().toString().equals("")||Double.parseDouble(preisText.getText().toString())<=0||volumenText.getText().toString().isEmpty()||preisText.getText()==null){
                    errSecondInput = true;
                    preisText.setTextColor(Color.RED);
                    toastMessage("Bitte Preis überprüfen!");
                }
                else {
                    preisText.setTextColor(Color.BLACK);
                    errSecondInput = false;
                }

                if(!errFirstInput&&!errSecondInput) {

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy | HH:mm:ss", Locale.getDefault());

                    OneFuelLoad currentFuel = new OneFuelLoad(
                            Double.parseDouble(volumenText.getText().toString()),
                            Double.parseDouble(preisText.getText().toString()),
                            df.format(c),
                            getBitmapAsByteArray(bitmap,photoTaken),
                            auto.getId()
                    );


                    //calculates the additional Percent for the Fuel
                    double newFuelPercent = (Double.parseDouble(volumenText.getText().toString())/auto.getTankvolumen())*100.0;

                    auto.setTankstand((int) ((newFuelPercent)+auto.getTankstand()));

                    //Database Updates
                    mDatabaseHelper.addFuel(currentFuel);
                    System.out.println("DIE ID: "+ currentFuel.getId());
                    mDatabaseHelper.updateTankStand(auto, oldAuto);

                    mDatabaseHelper.LatestEntry(auto.getId(),"fuel", currentFuel.getId(),-1,-1);

                    Intent i = new Intent(AddRefueling.this, Overview.class);

                    mIntentHelperSendReceivData.prepareIntentForSendingAuto(i, auto);
                    mIntentHelperSendReceivData.setStartFragmentNumber(i,2);

                    startActivity(i);

                    toastMessage("Tankvorgang hinzugefügt: " + currentFuel.toString());
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

        if(ContextCompat.checkSelfPermission(AddRefueling.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddRefueling.this, new String[]{
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
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            photoTaken = true;
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
