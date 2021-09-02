/*
Author: Isaac Harper

Use: this is the fragment for the ingredient search that we can then add to the shopping cart
or fridge. handles the async task for the api call
 */

package com.example.cornerstore;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class IngredientFragment extends Fragment implements View.OnClickListener {
    View v;
    ArrayList<String> out_list = new ArrayList<>();
    IngredientListAdapter ingredientListAdapter;
    EditText searchItem;

    public IngredientFragment() {}

    // hides the keyboard
    public void hideKeyboard() {
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_ingredient, container, false);

        searchItem = v.findViewById(R.id.searchTerm);

        ListView lvFridge = v.findViewById(R.id.searchResults);
        ingredientListAdapter = new IngredientListAdapter(out_list, getActivity().getBaseContext(), getActivity());
        lvFridge.setAdapter(ingredientListAdapter);

        Button searchItemButton = v.findViewById(R.id.searchButton);
        searchItemButton.setOnClickListener(this);

        return v;
    }

    // starts a new ingredient search
    @Override
    public void onClick(View v) {
        hideKeyboard();
        new DownloadTask().execute(searchItem.getText().toString());
    }

    // to get the json data from the api link
    // API KEY: a69b46814b364f79ab70b20dce4f8dec
    private class DownloadTask extends AsyncTask<String, Void, JSONObject> {

        // gets the api link and returns it to be used elsewhere
        @Override
        protected JSONObject doInBackground(String...pages) {
            try {
                String json = "";
                String line;

                URL url = new URL("https://api.spoonacular.com/food/ingredients/search?apiKey=a69b46814b364f79ab70b20dce4f8dec&number=100&query="+pages[0]);

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((line = in.readLine()) != null) {
                    json += line;
                }
                in.close();

                JSONObject jsonObject = new JSONObject(json);
                return jsonObject;
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }

        //this gets the name of every search result and sets the adapter to the results
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                out_list = new ArrayList<>();
                JSONArray ingredientList = json.getJSONArray("results");
                for (int i = 0; i < ingredientList.length(); i++) {
                    JSONObject ing = ingredientList.getJSONObject(i);
                    out_list.add(ing.getString("name"));
                }

                ingredientListAdapter.clear();
                ingredientListAdapter.setList(out_list);
                ingredientListAdapter.notifyDataSetChanged();

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}