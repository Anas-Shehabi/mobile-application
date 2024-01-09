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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab2_Route#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2_Route extends Fragment  {

    private static final IntentHelperSendReceivData mIntentHelperSendReceivData = new IntentHelperSendReceivData();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton floatingActionButton;
    private ListView mListView;

    Route route;
    Auto auto;
    DatabaseHelper mDatabaseHelper;

    public Tab2_Route() {
        // Required empty public constructor
    }

    public Tab2_Route(Auto auto) {
        this.auto = auto;
        Log.d("CHECK_ID_SELECTED_CAR","Hier die ID des Autos: " + auto.getId());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2_Route.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab2_Route newInstance(String param1, String param2) {
        Tab2_Route fragment = new Tab2_Route();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(auto == null)
            auto = mIntentHelperSendReceivData.receivIntentAuto(new Intent());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab2_route, container, false);

        floatingActionButton = rootView.findViewById(R.id.route_eingeben);
        floatingActionButton.bringToFront();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Tab2_Route.this.getActivity(), AddRoute.class);
                mIntentHelperSendReceivData.prepareIntentForSendingAuto(i, auto);
                startActivity(i);
            }
        });

        mListView = rootView.findViewById(R.id.route_listView);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        Cursor data = mDatabaseHelper.getRouteData(auto.getId());
        ArrayList<Route> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // get value from db then add it to Arraylist
            listData.add(new Route(data));
        }

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.row_for_listview, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                route = (Route) parent.getItemAtPosition(position);
                if(mDatabaseHelper.ReturnDetailsLastEntry(auto.getId(),"route",route.getId())){
                    Intent intent = new Intent(Tab2_Route.this.getActivity(),EditRoute.class);
                    mIntentHelperSendReceivData.prepareIntentForSendingRoute(intent, route);
                    mIntentHelperSendReceivData.prepareIntentForSendingAuto(intent, auto);
                    startActivity(intent);
                }
                else
                    toastMessage("Nur der letzter Eintrag ist bearbeitbar!");
            }
        });

        return  rootView;
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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage (String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
