package com.swe.garage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab3_GasStation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab3_GasStation extends Fragment {

    private static final String TAG = "Showing";
    private static final IntentHelperSendReceivData mIntentHelperSendReceivData= new IntentHelperSendReceivData();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton AddButton;
    private ListView mListView;
    private OneFuelLoad selectedFuel;

    Auto auto;
    DatabaseHelper mDatabaseHelper;

    public Tab3_GasStation() {
        // Required empty public constructor
    }

    public Tab3_GasStation(Auto auto) {
        this.auto = auto;
        Log.d("CHECK_ID_SELECTED_CAR","Hier die ID des Autos: " + auto.getId());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab3_GasSation.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab3_GasStation newInstance(String param1, String param2) {
        Tab3_GasStation fragment = new Tab3_GasStation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab3_gas_station, container, false);

        AddButton = (FloatingActionButton)view.findViewById(R.id.gs_add);
        AddButton.bringToFront();
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(Tab3_GasStation.this.getActivity(), AddRefueling.class);
                mIntentHelperSendReceivData.prepareIntentForSendingAuto(i, auto);
                startActivity(i);
            }
        });

        mListView = view.findViewById(R.id.g_liste_view);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        Cursor data = mDatabaseHelper.getFuelData(auto.getId());
        ArrayList<OneFuelLoad> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // get value from db then add it to ArrayList
            listData.add(new OneFuelLoad(data));
        }

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.row_for_listview, listData);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFuel = (OneFuelLoad) parent.getItemAtPosition(position);
                Log.d(TAG, "You clicked in " + selectedFuel);
                Intent intent = new Intent(Tab3_GasStation.this.getActivity(), EditRefueling.class);
                mIntentHelperSendReceivData.prepareIntentForSendingAuto(intent,auto);
                mIntentHelperSendReceivData.prepareIntentForSendingFuel(intent,selectedFuel);
                startActivity(intent);
            }
        });

        return view;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListene {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}