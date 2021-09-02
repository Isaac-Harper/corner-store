/*
Author: Isaac Harper

Use: this is the fragment that displays help information, nothing else besides that
 */

package com.example.cornerstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpFragment extends Fragment {

    public HelpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //set the view
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}