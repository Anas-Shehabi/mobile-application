package com.swe.garage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public FloatingActionButton floatingActionButton;
    private static final String TAG = "Showing";
    private static final IntentHelperSendReceivData mIntentHelperSendReceivData = new IntentHelperSendReceivData();
    private ListView mListView;
    DatabaseHelper mDatabaseHelper;
    Auto auto;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------ changing status bar color -------- //
        Window window = MainActivity.this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.statusBar));

        // ------- active fab -------- //
        floatingActionButton = findViewById(R.id.fab_add);
        floatingActionButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddData.class);
                startActivity(i);
            }
        }));

        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        populateListView();
    }


    private void populateListView() {

        Log.d(TAG, "populateListView: Showing all data");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<Auto> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get value from db then add it to ArrayList
            auto = new Auto(data);
            listData.add(auto);
        }

        //create list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.auto_row_for_listview, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickistener to the listView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                auto = (Auto) parent.getItemAtPosition(position);
                Log.d(TAG, "You clicked in " + auto);

                //Cursor data = mDatabaseHelper.getAutoID(auto);
                int ID = -1;                                            //Bei Fehlern kommentare Rückgängig machen
               // while (data.moveToNext())
                    ID = auto.getId();           //data.getInt(0);

                if (ID > -1) {
                    Log.d(TAG, "onItemClick: the autoID is: " + ID);
                    Intent intent = new Intent(MainActivity.this, Overview.class);
                    mIntentHelperSendReceivData.prepareIntentForSendingAuto(intent,auto);
                    startActivity(intent);
                } else
                    toastMessage("No autoID available!");

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