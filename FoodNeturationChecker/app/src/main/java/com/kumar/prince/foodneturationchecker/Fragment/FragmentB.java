package com.kumar.prince.foodneturationchecker.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kumar.prince.foodneturationchecker.Adapter.CustomAdapter;
import com.kumar.prince.foodneturationchecker.R;

import java.util.ArrayList;

/**
 * Created by prince on 14/9/17.
 */

public class FragmentB extends Fragment {


    ListView list;

    public FragmentB() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);



        return view;
    }
}
