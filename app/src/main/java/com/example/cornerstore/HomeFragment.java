/*
Author: Isaac Harper

Use: this is the fragment for the home screen that allows you to navigate between the different
fragments and is also where the fridge contents are displayed
 */
package com.example.cornerstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeListAdapter fridgeAdaptor;

    public HomeFragment() {}

    // when view is created we set up the adaptor for the listview and start it working
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //get list view, make an adapter, set the adaptor
        ListView lvFridge = v.findViewById(R.id.fridgeList);
        fridgeAdaptor = new HomeListAdapter(new ArrayList<>(), getContext(), getActivity());
        lvFridge.setAdapter(fridgeAdaptor);

        return v;
    }

    //when we start we set the list to the saved list
    @Override
    public void onResume() {
        fridgeAdaptor.setList(fridgeAdaptor.getList());
        fridgeAdaptor.notifyDataSetChanged();
        super.onResume();
    }

    // onPause we save the list
    @Override
    public void onPause() {
        fridgeAdaptor.saveList(fridgeAdaptor.getList());
        super.onPause();
    }
}