/*
Author: Isaac Harper

Use: this is the list adapter for the ingredient search which handles the addition of elements to
either the fridge or shopping cart. also deals with the animation of the elements.
 */

package com.example.cornerstore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientListAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;
    private Activity activity;
    View view;

    public IngredientListAdapter(List<String> list, Context context, Activity activity) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.activity = activity;
    }

    public void clear() {
        list = new ArrayList<>();
    }

    // sets the text elements in the listView by the array List and has a listener for the delete
    // button
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ingredient_row, null);
        }
        ViewFlipper viewFlipper = (ViewFlipper)parent.getRootView().findViewById(R.id.flipperid);
        ViewFlipper viewFlipper1 = (ViewFlipper)parent.getRootView().findViewById(R.id.flipperid2);

        TextView listItemText = view.findViewById(R.id.ingredientItem);
        listItemText.setText(list.get(position));

        Button addBtn = view.findViewById(R.id.ingredientAdd);
        Button addBtnShop = view.findViewById(R.id.ingredientAddShopping);

        addBtn.setOnClickListener(v -> {
            notifyDataSetChanged();

            startFlip(viewFlipper);

            saveItem(listItemText.getText().toString());
        });

        addBtnShop.setOnClickListener(v -> {
            notifyDataSetChanged();

            startFlip(viewFlipper1);

            saveItemShopping(listItemText.getText().toString());
        });

        return view;
    }

    // starts the given view Flipper and loops through it once before stopping
    public void startFlip(ViewFlipper viewFlipper) {
        viewFlipper.setInAnimation(context, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(context, android.R.anim.slide_out_right);
        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                int displayedChild = viewFlipper.getDisplayedChild();
                int childCount = viewFlipper.getChildCount();
                if (displayedChild == childCount - 1) {
                    viewFlipper.stopFlipping();
                    viewFlipper.showNext();
                }
            }
        });
        viewFlipper.startFlipping();
    }

    // saves one item to list
    public void saveItem(String item) {
        ArrayList<String> change = getList();
        change.add(item);
        saveList(change);
    }

    public void saveItemShopping(String item) {
        ArrayList<String> change = getListShopping();
        change.add(item);
        saveListShopping(change);
    }

    // sets the list to a given list
    public void setList(ArrayList<String> list) {
        this.list = new ArrayList<>(list);
    }

    // saves the given list
    public void saveList(ArrayList<String> change) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String saveString = change.toString();
        saveString = saveString.substring(1, saveString.length() - 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("FridgeList", saveString);
        editor.apply();
    }

    // gets the currently saved list
    public ArrayList<String> getList() {
        SharedPreferences sharedPref = activity.getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("FridgeList", "");
        if (!itemsString.equals("")) {
            return new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { return  new ArrayList<>(); }
    }

    // saves the given list
    public void saveListShopping(ArrayList<String> change) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String saveString = change.toString();
        saveString = saveString.substring(1, saveString.length() - 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("shoppingList", saveString);
        editor.apply();
    }

    // gets the currently saved list
    public ArrayList<String> getListShopping() {
        SharedPreferences sharedPref = activity.getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("shoppingList", "");
        if (!itemsString.equals("")) {
            return new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { return  new ArrayList<>(); }
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

