/*
Author: Isaac Harper

Use: this is the list adapter recipe search that handles saving the image, opeing the page, and
adding it to the saved recipe list
 */

package com.example.cornerstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class RecipeListAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;
    private Activity activity;
    String url;
    View view;

    public RecipeListAdapter(ArrayList<String> list, Context context, Activity activity) {
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
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recipe_row, null);
        }

        //get text and id of the row
        String text1 = list.get(position);
        String id1 = text1.substring(text1.indexOf("{")+1,text1.indexOf("}"));

        //set row list and image
        TextView listItemText = (TextView)view.findViewById(R.id.recipeItem);
        listItemText.setText(text1.replaceAll("\\{.*?\\}",""));
        new DownloadImageTask((ImageView) view.findViewById(R.id.recipeImage))
                .execute( "https://spoonacular.com/recipeImages/" + id1 + "-312x231.jpg");

        //set buttons
        Button saveBtn = (Button)view.findViewById(R.id.recipeSave);
        Button openButton = (Button)view.findViewById(R.id.recipeOpen);
        ImageView imgButton = (ImageView)view.findViewById(R.id.recipeImage);

        //save image
        imgButton.setOnClickListener( v -> {
            ImageView iv = (ImageView)v.findViewById(R.id.recipeImage);
            iv.setDrawingCacheEnabled(true);
            Bitmap b = iv.getDrawingCache();
            MediaStore.Images.Media.insertImage(activity.getContentResolver(), b, "", "");
        });

        //save recipe
        saveBtn.setOnClickListener(v -> {
            ArrayList<String> change = getList();

            change.add(list.get(position));
            saveList(change);
        });

        //open recipe
        openButton.setOnClickListener(v -> {
            String text = list.get(position);
            String id = text.substring(text.indexOf("{")+1,text.indexOf("}"));
            new RecipeListAdapter.UrlTask().execute(id);
            System.out.println(url);
        });
        return view;
    }

    // saves the given list
    public void saveList(ArrayList<String> change) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String saveString = change.toString();
        saveString = saveString.substring(1, saveString.length() - 1);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("savedList", saveString);
        editor.apply();
    }

    // gets the currently saved list
    public ArrayList<String> getList() {
        SharedPreferences sharedPref = activity.getPreferences((Context.MODE_PRIVATE));
        String itemsString = sharedPref.getString("savedList", "");
        if (!itemsString.equals("")) {
            return new ArrayList<>(Arrays.asList(itemsString.split("\\s*,\\s*")));
        } else { return  new ArrayList<>(); }
    }

    //clear all elements from list
    public void clear() {
        setList(new ArrayList<>());
        notifyDataSetChanged();
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

    // to get the json data from the api link
    // API KEY: a69b46814b364f79ab70b20dce4f8dec
    private class UrlTask extends AsyncTask<String, Void, JSONObject> {

        // gets the api link and returns it to be used elsewhere
        @Override
        protected JSONObject doInBackground(String...pages) {
            try {
                String json = "";
                String line;
                URL url = new URL("https://api.spoonacular.com/recipes/" + pages[0] + "/information?apiKey=a69b46814b364f79ab70b20dce4f8dec");
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

        // gets all of the recipe names and saves them to be displayed
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                url = json.getString("sourceUrl");

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                activity.startActivity(i);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // dowloads the given image and sets the image view to it.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}