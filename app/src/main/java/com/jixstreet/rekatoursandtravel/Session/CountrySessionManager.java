package com.jixstreet.rekatoursandtravel.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jixstreet.rekatoursandtravel.model.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountrySessionManager {

    // Sharedpref file name
    private static final String PREFER_NAME = "COUNTRY";
    // All Shared Preferences Keys
    private static final String LIST_BANK = "LIST_COUNTRY";
    // Shared Preferences reference
    SharedPreferences pref;
    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public CountrySessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveCountry(Context context, ArrayList<Country> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonCountry = gson.toJson(favorites);
        editor.putString(LIST_BANK, jsonCountry);
        editor.commit();
    }

    public ArrayList<Country> getCountry(Context context) {
        SharedPreferences settings;
        List<Country> favorites;

        settings = context.getSharedPreferences(PREFER_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(LIST_BANK)) {
            String jsonCountry = settings.getString(LIST_BANK, null);
            Gson gson = new Gson();
            Country[] favoriteItems = gson.fromJson(jsonCountry,
                    Country[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<>(favorites);
        } else
            return null;

        return (ArrayList<Country>) favorites;
    }

    public void clearCountry() {
        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
    }

}