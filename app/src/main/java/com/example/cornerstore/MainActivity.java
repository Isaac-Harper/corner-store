/*
Author: Isaac Harper

Purpose: this activity handles the users jumping through the different fragments and is also where
we check if the user is using a phone or a tablet so we are able to account for that. We also
check to make sure we have the right permissions to be able to save images correctly
 */

package com.example.cornerstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Context mContext=MainActivity.this;
    private static final int REQUEST = 112;
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSize();

        // check for permissions
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
        }

        if (size == 2) { // normal phone
            setContentView(R.layout.activity_main);

            HomeFragment homeFrag = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.homeScreen, homeFrag);
            transaction.commit();
        } else { // tablet
            reloadMain();

            IngredientFragment saveFrag = new IngredientFragment();
            FragmentManager managerSave = getSupportFragmentManager();
            FragmentTransaction transactionSave = managerSave.beginTransaction();
            transactionSave.replace(R.id.homeScreenTwo, saveFrag);
            transactionSave.addToBackStack(null);
            transactionSave.commit();

        }
    }

    // reloads the main view for a tablet
    public void reloadMain() {
        setContentView(R.layout.activity_main_large);

        HomeFragment homeFrag = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.homeScreenOne, homeFrag);
        transaction.commit();
    }

    // check what size device we have
    public void checkSize() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            size = 2;
        }
    }

    // when we request permisons
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(mContext, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
            }
        }
    }

    // check whether the app has the right permissions
    private static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //start shopping cart
    public void shoppingCart(View v) {
        ShoppingFragment shopFrag = new ShoppingFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (size == 2) { //phone
            transaction.replace(R.id.homeScreen, shopFrag);
            transaction.addToBackStack(null);
        } else { // tablet
            transaction.replace(R.id.homeScreenTwo, shopFrag);
            reloadMain();
        }
        transaction.commit();
    }

    //start add new
    public void addNew(View v) {
        IngredientFragment ingrFrag = new IngredientFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (size == 2) { //phone
            transaction.replace(R.id.homeScreen, ingrFrag);
            transaction.addToBackStack(null);
        } else { //tablet
            transaction.replace(R.id.homeScreenTwo, ingrFrag);
            reloadMain();
        }
        transaction.commit();
    }

    //start recipe search
    public void recipeSearch(View v) {
        RecipeFragment reciFrag = new RecipeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (size == 2) { //phone
            transaction.replace(R.id.homeScreen, reciFrag);
            transaction.addToBackStack(null);
        } else { //tablet
            transaction.replace(R.id.homeScreenTwo, reciFrag);
            reloadMain();
        }
        transaction.commit();
    }

    //start recipe search
    public void savedRecipes(View v) {
        SavedFragment saveFrag = new SavedFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (size == 2) { //phone
            transaction.replace(R.id.homeScreen, saveFrag);
            transaction.addToBackStack(null);
        } else { //tablet
            transaction.replace(R.id.homeScreenTwo, saveFrag);
            reloadMain();
        }
        transaction.commit();
    }

    //start recipe search
    public void helpUser(View v) {
        HelpFragment helpFrag = new HelpFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (size == 2) { //phone
            transaction.replace(R.id.homeScreen, helpFrag);
            transaction.addToBackStack(null);
        } else { //tablet
            transaction.replace(R.id.homeScreenTwo, helpFrag);
            reloadMain();
        }
        transaction.commit();
    }


}