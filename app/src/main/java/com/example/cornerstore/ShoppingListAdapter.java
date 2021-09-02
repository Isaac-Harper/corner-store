/*
Author: Isaac Harper

Purpose: this adapter is shopping cart and handles deleting items from it or saving them
to the main fridge
 */

package com.example.cornerstore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ShoppingListAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;
    private Activity activity;

    public ShoppingListAdapter(ArrayList<String> list, Context context, Activity activity) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.activity = activity;
    }

    // sets the list to a given lise
    public void setList(ArrayList<String> list) {
        this.list = new ArrayList<>(list);
    }

    // sets the text elements in the listView by the array List and has a listener for the delete
    // button
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.shopping_row, null);
        }

        //set text
        TextView listItemText = (TextView)view.findViewById(R.id.shoppingItem);
        listItemText.setText(list.get(position));

        //set buttons
        Button deleteBtn = (Button)view.findViewById(R.id.shoppingDelete);
        Button listAddBtn = (Button)view.findViewById(R.id.shoppingListAdd);

        //add button to fridfe
        listAddBtn.setOnClickListener(v -> {
            ArrayList<String> change = getList();
            ArrayList<String> changeFridge = getListFridge();
            changeFridge.add(change.get(position));
            change.remove(position);
            saveListFridge(changeFridge);
            saveList(change);
            setList(getList());
            notifyDataSetChanged();
        });

        //when delete button is pressed
        deleteBtn.setOnClickListener(v -> {
            ArrayList<String> change = getList();
            change.remove(position);
            saveList(change);
            setList(getList());
            notifyDataSetChanged();
        });
        return view;
    }

    // saves the given list
    public void saveList(ArrayList<String> change) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String saveString = change.toString();
        saveString = saveString.substring(1, saveString.length() - 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("shoppingList", saveString);
        editor.apply();
    }

    // gets the currently saved list
    public ArrayList<String> getList() {
        SharedPreferences sharedPref = activity.getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("shoppingList", "");
        if (!itemsString.equals("")) {
            return new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { return  new ArrayList<>(); }
    }
    //gets currently saved fridge list
    public ArrayList<String> getListFridge() {
        SharedPreferences sharedPref = activity.getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("FridgeList", "");
        if (!itemsString.equals("")) {
            return new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { return  new ArrayList<>(); }
    }


    // saves the given fridge list
    public void saveListFridge(ArrayList<String> change) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String saveString = change.toString();
        saveString = saveString.substring(1, saveString.length() - 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FridgeList", saveString);
        editor.apply();
    }

    // not needed
    @Override
    public int getCount() {
        return list.size();
    }
    // not needed
    @Override
    public Object getItem(int position) {
        return null;
    }
    // not needed
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
