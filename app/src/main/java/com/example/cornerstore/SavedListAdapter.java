/*
Author: Isaac Harper

Purpose: this adapter is for the saved recipes and it handles their deletion, saving their image,
opening them up
 */

package com.example.cornerstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;;
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

public class SavedListAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;
    private Activity activity;
    String url;

    public SavedListAdapter(ArrayList<String> list, Context context, Activity activity) {
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
            view = inflater.inflate(R.layout.saved_row, null);
        }

        //get recipe title and id
        String text1 = list.get(position);
        String id1 = text1.substring(text1.indexOf("{")+1,text1.indexOf("}"));

        //set rows text and image
        TextView listItemText = (TextView)view.findViewById(R.id.savedItem);
        listItemText.setText(text1.replaceAll("\\{.*?\\}",""));
        new SavedListAdapter.DownloadImageTask((ImageView) view.findViewById(R.id.savedImage))
                .execute( "https://spoonacular.com/recipeImages/" + id1 + "-312x231.jpg");

        //set buttons
        Button deleteButton = (Button)view.findViewById(R.id.savedDelete);
        Button openButton = (Button)view.findViewById(R.id.savedOpen);
        ImageView imgButton = (ImageView)view.findViewById(R.id.savedImage);

        //save image
        imgButton.setOnClickListener( v -> {
            ImageView iv = (ImageView)v.findViewById(R.id.savedImage);
            iv.setDrawingCacheEnabled(true);
            Bitmap b = iv.getDrawingCache();
            MediaStore.Images.Media.insertImage(activity.getContentResolver(), b, "", "");
        });

        //delete recipe
        deleteButton.setOnClickListener(v -> {
            ArrayList<String> change = getList();
            change.remove(position);
            saveList(change);
            setList(getList());
            notifyDataSetChanged();
        });

        //open recipe
        openButton.setOnClickListener(v -> {
            ArrayList<String> change = getList();
            String text = change.get(position);
            String id = text.substring(text.indexOf("{")+1,text.indexOf("}"));
            new SavedListAdapter.UrlTask().execute(id);
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

    //saves the image to the given imageView
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