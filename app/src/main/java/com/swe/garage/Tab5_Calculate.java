package com.swe.garage;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tab5_Calculate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab5_Calculate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Auto auto = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    public SeekBar streckenleange_seekbar,inn_seekbar,kom_seekabr,auss_seekbar;
    private TextView streckenleange_editText,inn_editText,kom_editText,auss_editText,kraftstoffeverbrauch_text,Kraftstoffkosten_text,Anzahl_der_Tankvorgeange_text,Ausstoss_text;
    private int streckenleange_seekbar_int,inn_seekbar_int,kom_seekbar_int,auss_seekbar_int;



    public Tab5_Calculate() {
        // Required empty public constructor
    }

    public  Tab5_Calculate(Auto auto){this.auto = auto;
        Log.d("CHECK_ID_SELECTED_CAR","Hier die ID des Autos: " + auto.getId());}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab5_Calculate.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab5_Calculate newInstance(String param1, String param2) {
        Tab5_Calculate fragment = new Tab5_Calculate();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_tab5_calculate, container, false);

        streckenleange_editText = (TextView) v.findViewById(R.id.streckenleange_editText);
        streckenleange_seekbar = (SeekBar) v.findViewById(R.id.streckenleange_seekbar);

        streckenleange_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                streckenleange_seekbar_int = progress;
                streckenleange_editText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        inn_editText = (TextView) v.findViewById(R.id.inn_editText);
        inn_seekbar = (SeekBar) v.findViewById(R.id.inn_seekbar);
        inn_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                inn_seekbar_int = progress;
                inn_editText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        kom_editText = (TextView) v.findViewById(R.id.kom_editText);
        kom_seekabr = (SeekBar) v.findViewById(R.id.kom_seekabr);
        kom_seekabr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                kom_seekbar_int = progress;
                kom_editText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        auss_editText = (TextView) v.findViewById(R.id.auss_editText);
        auss_seekbar = (SeekBar) v.findViewById(R.id.auss_seekbar);
        auss_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                auss_seekbar_int = progress;
                auss_editText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        kraftstoffeverbrauch_text = (TextView) v.findViewById(R.id.kraftstoffeverbrauch_text);
        Kraftstoffkosten_text = (TextView) v.findViewById(R.id.Kraftstoffkosten_text);
        Anzahl_der_Tankvorgeange_text = (TextView) v.findViewById(R.id.Anzahl_der_Tankvorgeange_text);
        Ausstoss_text = (TextView) v.findViewById(R.id.Ausstoss_text);
        Button berchnen = v.findViewById(R.id.button_berchnen);

        berchnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Kraftstoffverbrauch,kraftstoffkosten,Anzahl_der_Tankvorgeange,CO2_Ausstoss , akteulle_liters_im_Auto;

                if(inn_seekbar_int + kom_seekbar_int + auss_seekbar_int == 100){

                    inn_editText.setTextColor(Color.BLACK);
                    auss_editText.setTextColor(Color.BLACK);
                    kom_editText.setTextColor(Color.BLACK);

                    Kraftstoffverbrauch = (((inn_seekbar_int*streckenleange_seekbar_int)/100)*auto.getKraftstoff_inner())/100 + (((auss_seekbar_int*streckenleange_seekbar_int)/100)*auto.getKraftstoff_ausser())/100 + (((kom_seekbar_int*streckenleange_seekbar_int)/100)*auto.getKraftstoff_komb())/100;
                    kraftstoffeverbrauch_text.setText(String.valueOf(Round(Kraftstoffverbrauch,2)));

                    kraftstoffkosten = Kraftstoffverbrauch * 1.2;
                    Kraftstoffkosten_text.setText(String.valueOf(Round(kraftstoffkosten,2)));

                    akteulle_liters_im_Auto = (auto.getTankstand()*auto.getTankvolumen())/100;//liters im Benzintank
                    double y = akteulle_liters_im_Auto - Kraftstoffverbrauch ;
                    if(y >= 0)
                        Anzahl_der_Tankvorgeange_text.setText(String.valueOf(0));
                    else{
                        Anzahl_der_Tankvorgeange =  y * -1  / auto.getTankvolumen();

                        Anzahl_der_Tankvorgeange_text.setText(String.valueOf(Round_Anzahl(Anzahl_der_Tankvorgeange)));
                    }

                    CO2_Ausstoss = (auto.getCo2_aus() * streckenleange_seekbar_int)/100;
                    Ausstoss_text.setText(String.valueOf( Round(CO2_Ausstoss,2)));

                    CO2_Ausstoss = (auto.getCo2_aus() * streckenleange_seekbar_int)/100;
                    Ausstoss_text.setText(String.valueOf( Round(CO2_Ausstoss,2)));


                } else {

                    inn_editText.setTextColor(Color.RED);
                    auss_editText.setTextColor(Color.RED);
                    kom_editText.setTextColor(Color.RED);
                    Toast.makeText(getActivity().getApplicationContext(), "Bitte geben Sie richtige Eingabe ein", Toast.LENGTH_LONG).show();
                }

            }
        });

        return v;


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


    private double Round(double value, int decimalPoints) {
        double d = Math.pow(10, decimalPoints);
        return Math.round(value * d) / d;
    }


    private double Round_Anzahl(double Anzahl_der_Tankvorgeange){
        double x ;

        if(Anzahl_der_Tankvorgeange == (int) Anzahl_der_Tankvorgeange)
            x = Anzahl_der_Tankvorgeange;
        else{
            int a = (int) Anzahl_der_Tankvorgeange;
            a++;
            x=a;
        }
        return x;
    }

}