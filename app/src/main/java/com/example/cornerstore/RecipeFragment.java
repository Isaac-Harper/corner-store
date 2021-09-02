/*
Author: Isaac Harper

Use: this is the fragment for new recipes where we use an api call to get recipes that have the
 ingredients that we want.
 */

package com.example.cornerstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeFragment extends Fragment {

    View v;
    ArrayList<String> out_list = new ArrayList<>();
    ArrayList<String> ingredientList;
    RecipeListAdapter recipeListAdapter;
    ListView lvRecipe;

    public RecipeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getActivity().getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("FridgeList", "");
        if (!itemsString.equals("")) {
            ingredientList = new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { ingredientList = new ArrayList<>(); }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_recipe, container, false);

        //set the listview adapter
        lvRecipe = v.findViewById(R.id.recipeList);
        recipeListAdapter = new RecipeListAdapter(new ArrayList<>(), getContext(), getActivity());
        lvRecipe.setAdapter(recipeListAdapter);
        recipeListAdapter.notifyDataSetChanged();

        if (ingredientList.size() != 0) {
            new RecipeFragment.RecipeTask().execute(listToSearch(ingredientList));
        }

        return v;
    }

    //turns a list into a usable search string for the api
    public String listToSearch(ArrayList<String> inList) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < inList.size(); i++) {
            out.append(inList.get(i)).append(",+");
        }
        out = new StringBuilder(out.substring(0, out.length() - 2));

        return out.toString();
    }

    // to get the json data from the api link
    // API KEY: a69b46814b364f79ab70b20dce4f8dec
    private class RecipeTask extends AsyncTask<String, Void, JSONObject> {

        // gets the api link and returns it to be used elsewhere
        @Override
        protected JSONObject doInBackground(String...pages) {
            try {
                StringBuilder json = new StringBuilder("{  \"results\": ");
                String line;

                URL url = new URL("https://api.spoonacular.com/recipes/findByIngredients?apiKey=a69b46814b364f79ab70b20dce4f8dec&ranking=2&number=10&ingredients="+pages[0]);

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((line = in.readLine()) != null) {
                    json.append(line);
                }
                in.close();
                json.append("}");
                return new JSONObject(json.toString());
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }

        // gets all of the recipe names and saves them to be displayed
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                out_list = new ArrayList<>();

                JSONArray recipeList = json.getJSONArray("results");
                for (int i = 0; i < recipeList.length(); i++) {
                    JSONObject ing = recipeList.getJSONObject(i);
                    out_list.add(ing.getString("title") + "{" + ing.getString("id") + "}");
                }

                recipeListAdapter.clear();
                recipeListAdapter.setList(out_list);
                recipeListAdapter.notifyDataSetChanged();

            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}