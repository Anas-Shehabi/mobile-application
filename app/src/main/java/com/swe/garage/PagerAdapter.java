package com.swe.garage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    Auto auto = null;


    //Construktor also needs the Auto which data are shown in the tabs
    public PagerAdapter(FragmentManager fm, int NumberOfTabs, Auto auto)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.auto = auto;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
              Tab1_Car tab1 = new Tab1_Car(auto);
                return tab1;
            case 1:
               Tab2_Route tab2 = new Tab2_Route(auto);
               return tab2;
            case 2:
                Tab3_GasStation tab3 = new Tab3_GasStation(auto);
                return tab3;
            case 3:
                Tab4_Calender tab4 = new Tab4_Calender();
                return tab4;
            case 4:
                 Tab5_Calculate tab5 = new Tab5_Calculate(auto);
                return tab5;
            case 5:
                Tab6_Workshop tab6 = new Tab6_Workshop(auto);
                return tab6;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
