/*
Author: Isaac Harper

Use: this is the fragment for saved recipes that we can open back up or delete
 */
package com.example.cornerstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    View v;
    ArrayList<String> save_list = new ArrayList<>();
    SavedListAdapter arrayAdapterSaved;

    public SavedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //set the view
        v = inflater.inflate(R.layout.fragment_saved, container, false);

        // set the adapter for the listview
        ListView lvSaved = v.findViewById(R.id.savedList);
        arrayAdapterSaved = new SavedListAdapter(new ArrayList<>(), getContext(), getActivity());
        lvSaved.setAdapter(arrayAdapterSaved);
        save_list = arrayAdapterSaved.getList();
        arrayAdapterSaved.setList(save_list);
        arrayAdapterSaved.notifyDataSetChanged();

        return v;
    }

    // onPause we save the list
    @Override
    public void onPause() {
        arrayAdapterSaved.saveList(arrayAdapterSaved.getList());
        super.onPause();
    }

    // onResume we set the shopping list to the saved list
    @Override
    public void onResume() {
        arrayAdapterSaved.setList(arrayAdapterSaved.getList());
        arrayAdapterSaved.notifyDataSetChanged();
        super.onResume();
    }

}