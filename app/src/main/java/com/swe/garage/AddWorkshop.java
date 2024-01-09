package com.swe.garage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddWorkshop extends AppCompatActivity {
    private static final IntentHelperSendReceivData mIntentHelperSendReceivData= new IntentHelperSendReceivData();

    ImageButton back_btn, img_add;
    EditText description_input;
    Button photoButton;
    ImageView photoView;

    private boolean photoTaken = false;
    private Bitmap bitmap = null;
    private Auto auto;
    private DatabaseHelper mDatabaseHelper;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workshop);

        // ------ changing status bar color -------- //
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(AddWorkshop.this,R.color.statusBar));

        //Get Data from the "Tab6_Workshop"
        auto = mIntentHelperSendReceivData.receivIntentAuto(getIntent());

        //grants access to the database which saves each route
        mDatabaseHelper = new DatabaseHelper(this);

        description_input = findViewById(R.id.description_input);
        description_input.setMaxLines(8);
        description_input.setVerticalScrollBarEnabled(true);
        description_input.setMovementMethod(new ScrollingMovementMethod());

        photoView = findViewById(R.id.imageviewWS);

        photoButton = findViewById(R.id.imagebuttonWS);

        photoButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        if(ContextCompat.checkSelfPermission(AddWorkshop.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddWorkshop.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }


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
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy | HH:mm:ss", Locale.getDefault());
                Workshop currentWorkshop = new Workshop(
                        description_input.getText().toString(),
                        df.format(c),
                        auto.getId(),
                        getBitmapAsByteArray(bitmap,photoTaken)
                );

                mDatabaseHelper.addWorkshop(currentWorkshop);

                /**
                 * TO DO: user must be returned to correct fragment (here: Tab2) not Overview.class
                 */
                Intent i = new Intent (AddWorkshop.this, Overview.class);
                mIntentHelperSendReceivData.prepareIntentForSendingAuto(i, auto);
                mIntentHelperSendReceivData.setStartFragmentNumber(i,5);
                startActivity(i);

                toastMessage("Werkstattbesuchvorgang hinzugefügt: " + currentWorkshop.toString());
            }
        });


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
            photoView.setImageBitmap(bitmap);
            photoTaken = true;
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param bitmap Picture taken
     * @return Picture in byte[] format
     */
    public static byte[] getBitmapAsByteArray(Bitmap bitmap, boolean photoTaken) {
        if(photoTaken) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
        return null;
    }
}
