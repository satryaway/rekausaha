package com.reka.tour.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.model.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fachrifebrian on 3/3/16.
 */
public class Builder {

    private static Builder mInstance = null;
    private Gson gson = new Gson();
    private Context context;

    private Builder(Context context) {
        this.context = context;
    }

    public static Builder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Builder(context);
        }
        return mInstance;
    }

    public void getCountry(final CallBackCountry callback) {
        final ArrayList<Country> countries = new ArrayList<>();

        String url = CommonConstants.BASE_URL + "general_api/listCountry";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("JSON Country", response.toString() + "");

                try {
                    JSONArray airportArrayList = response.getJSONArray(CommonConstants.listCountry);
                    for (int i = 0; i < airportArrayList.length(); i++) {
                        countries.add(gson.fromJson(airportArrayList.getJSONObject(i).toString(), Country.class));
                    }


                    if (callback != null) {
                        callback.onSucces(countries);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON ListOrder", errorResponse + "");
//                ErrorException.getError(context, errorResponse);
            }
        });

    }


    public interface CallBackCountry {
         void onSucces(ArrayList<Country> countries);
    }
}
