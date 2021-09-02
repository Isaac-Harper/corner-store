/*
Author: Isaac Harper

Use: this is the fragment for the shopping cart where we can delete items or save them to
the fridge
 */

package com.example.cornerstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {

    View v;
    ArrayList<String> search_list = new ArrayList<>();
    ShoppingListAdapter arrayAdapterShopping;

    public ShoppingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //set the view
        v = inflater.inflate(R.layout.fragment_shopping, container, false);

        // set the adapter for the listview
        ListView lvShopping = v.findViewById(R.id.shoppingList);
        arrayAdapterShopping = new ShoppingListAdapter(new ArrayList<>(), getContext(), getActivity());
        lvShopping.setAdapter(arrayAdapterShopping);
        search_list = arrayAdapterShopping.getList();
        arrayAdapterShopping.setList(search_list);
        arrayAdapterShopping.notifyDataSetChanged();

        return v;
    }

    // onPause we save the list
    @Override
    public void onPause() {
        arrayAdapterShopping.saveList(arrayAdapterShopping.getList());
        super.onPause();
    }

    // onResume we set the shopping list to the saved list
    @Override
    public void onResume() {
        arrayAdapterShopping.setList(arrayAdapterShopping.getList());
        arrayAdapterShopping.notifyDataSetChanged();
        super.onResume();
    }

}