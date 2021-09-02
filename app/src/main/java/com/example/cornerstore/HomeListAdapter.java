/*
Author: Isaac Harper

Use: this is the list adapter for the home fridge that handles the deletion of elements and holds
them as ingridient names
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
import java.util.List;

public class HomeListAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private final Context context;
    private final Activity activity;

    public HomeListAdapter(List<String> list, Context context, Activity activity) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.activity = activity;
    }

    // sets the list to a given list
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
            view = inflater.inflate(R.layout.fridge_row, null);
        }

        TextView listItemText = view.findViewById(R.id.fridgeItem);
        Button delBtn = view.findViewById(R.id.fridgeDelete);

        listItemText.setText(list.get(position));

        //watches for the delete button
        delBtn.setOnClickListener(v -> {
            ArrayList<String> change = getList();
            change.remove(listItemText.getText().toString());
            saveList(change);
            setList(getList());
            notifyDataSetChanged();
        });

        return view;
    }

    //save the given list
    public void saveList(ArrayList<String> change) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String saveString = change.toString();
        saveString = saveString.substring(1, saveString.length() - 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FridgeList", saveString);
        editor.apply();
    }

    //get the current saved list
    public ArrayList<String> getList() {
        SharedPreferences sharedPref = activity.getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("FridgeList", "");
        if (!itemsString.equals("")) {
            return new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { return  new ArrayList<>(); }
    }

    // not needed
    @Override
    public long getItemId(int position) {
        return 0;
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
}


