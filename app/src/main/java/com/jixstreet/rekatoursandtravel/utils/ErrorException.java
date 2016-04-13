package com.jixstreet.rekatoursandtravel.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fachrifebrian on 2/23/16.
 */
public class ErrorException {
    public static void getError(Context context, JSONObject errorResponse) {
        if (errorResponse != null) {
            try {
                Toast.makeText(context,
                        errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS),
                        Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(context,
                    "Please try again",
                    Toast.LENGTH_SHORT).show();
            Log.e("errorResponse", errorResponse + "");
        }
    }
}
