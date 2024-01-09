package com.swe.garage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class Overview extends AppCompatActivity implements Tab1_Car.OnFragmentInteractionListener, Tab2_Route.OnFragmentInteractionListener, Tab3_GasStation.OnFragmentInteractionListene, Tab4_Calender.OnFragmentInteractionListene, Tab5_Calculate.OnFragmentInteractionListene, Tab6_Workshop.OnFragmentInteractionListene {

    private static final IntentHelperSendReceivData mIntentHelperSendReceivData = new IntentHelperSendReceivData();
    private static final String TAG = "Showing";
    private int startFragmentNumber = 0;
    DatabaseHelper mDatabaseHelper;


    private int[] image = {
            R.drawable.ic_baseline_directions_car_24,
            R.drawable.ic_baseline_horizontal_distribute_24,
            R.drawable.ic_baseline_local_gas_station_24,
            R.drawable.ic_baseline_calendar_today_24,
            R.drawable.ic_baseline_calculate_24,
            R.drawable.baseline_miscellaneous_services_24};

    static TextView header;
    ImageButton img_btn;
    Auto auto;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        // ------ changing status bar color -------- //
        Window window = Overview.this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(Overview.this,R.color.statusBar));

       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
*/
        // ----- active 3-dot-menu in toolbar ----- //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        header = findViewById(R.id.header);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(image[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(image[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(image[2]));
        tabLayout.addTab(tabLayout.newTab().setIcon(image[3]));
        tabLayout.addTab(tabLayout.newTab().setIcon(image[4]));
        tabLayout.addTab(tabLayout.newTab().setIcon(image[5]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // ----- moved from "onOptionsItemSelected" Method ------- //
        Intent receivedIntent = getIntent();
        // get all the values we passed as extra
        // Creates new Auto for the Fragment Tabs
        auto = mIntentHelperSendReceivData.receivIntentAuto(receivedIntent);
        //Gets the pos Number of the fragment needed(Default = 0)
        startFragmentNumber = mIntentHelperSendReceivData.receivStartFragmentNumber(receivedIntent);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        //PagerAdapter also needs now the Auto data of the selected car
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),auto);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(startFragmentNumber);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                    header.setText("Fahrzeugübersicht");
                else if(tab.getPosition() == 1)
                    header.setText("gefahrene Strecken");
                else if(tab.getPosition() == 2)
                    header.setText("Tankvorgänge");
                else if(tab.getPosition() == 3)
                    header.setText("Statistik");
                else if(tab.getPosition() == 4)
                    header.setText("Streckenprognose");
                else if(tab.getPosition() == 5)
                    header.setText("Werkstattbesuch");
                else
                    header.setText("Fahrzeugübersicht");

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // ----- active arrow back button to MainActivity ------ //
        img_btn = findViewById(R.id.img_btn);
        img_btn.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Overview.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent i;
        mDatabaseHelper = new DatabaseHelper(this);



        if (id == R.id.editData) {
            i = new Intent(Overview.this, EditData.class);
            new IntentHelperSendReceivData().prepareIntentForSendingAuto(i, auto);
            startActivity(i);
            
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Overview.this)
                    .setTitle("Dieses Fahrzeug wird gelöscht")
                    .setMessage("Wollen Sie es wirklick löschen?")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabaseHelper.deleteAuto(auto);
                            Intent intent = new Intent(Overview.this, MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            toastMessage("Dieses Fahrzeug wurde gelöscht.");
                        }
                    });
            builder.setNegativeButton("Nein", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage (String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

