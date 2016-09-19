package com.jixstreet.rekatoursandtravel.hotel.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.Session.CountrySessionManager;
import com.jixstreet.rekatoursandtravel.activity.ListOrderActivity;
import com.jixstreet.rekatoursandtravel.model.Country;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import cz.msebera.android.httpclient.Header;

public class HotelInputDataActivity extends AppCompatActivity {

    private CountrySessionManager countrySessionManager;
    private ArrayList<Country> countries = new ArrayList<>();
    private String[] countryCollection;
    private String[] titleCollection = {"Tuan", "Nyonya", "Nona"};
    private String nationality = "id";
    private String title = "Mr";
    private String guestTitle = "Mr";

    EditText titleEt;
    EditText nameEt;
    EditText lastNameEt;
    EditText phoneEt;
    EditText emailEt;
    EditText nationalityEt;

    EditText guestTitleEt;
    EditText guestFullNameEt;
    EditText guestPhoneEt;
    EditText guestEmailEt;

    Button nextBtn;
    private RequestParams requestParams;
    private String bookUri;
    private String responseString;

    private HashMap<String, String> contactMap = new HashMap<>();
    private HashMap<String, String> hotelCustomerMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_data_input);

        handleIntent();

        countrySessionManager = new CountrySessionManager(this);

        getCountry();
        initUI();
    }

    private void handleIntent() {
        Intent bundle = getIntent();
        bookUri = bundle.getStringExtra(CommonConstants.BOOKURI);
    }

    private void initUI() {
        titleEt = (EditText) findViewById(R.id.title_et);
        nameEt = (EditText) findViewById(R.id.name_et);
        lastNameEt = (EditText) findViewById(R.id.last_name_et);
        phoneEt = (EditText) findViewById(R.id.phone_et);
        emailEt = (EditText) findViewById(R.id.email_et);
        nationalityEt = (EditText) findViewById(R.id.nationality_et);

        guestTitleEt = (EditText) findViewById(R.id.guest_title_et);
        guestFullNameEt = (EditText) findViewById(R.id.guest_full_name_et);
        guestPhoneEt = (EditText) findViewById(R.id.guest_phone_et);
        guestEmailEt = (EditText) findViewById(R.id.guest_email_et);

        nextBtn = (Button) findViewById(R.id.next_btn);

        //callback
        nationalityEt.setOnTouchListener(onNationalityTouchedListener);
        titleEt.setOnTouchListener(onTitleTouchedListener);
        guestTitleEt.setOnTouchListener(onTitleTouchedListener);
        nextBtn.setOnClickListener(onNextBtnClickedListener);
    }

    private void getCountry() {
        if (countrySessionManager.getCountry(this) != null) {
            countries = countrySessionManager.getCountry(this);
            String[] countryTemp = new String[countries.size()];

            for (int i = 0; i < countries.size(); i++) {
                countryTemp[i] = countries.get(i).countryName;
            }

            this.countryCollection = countryTemp;
        }
    }

    View.OnClickListener onNextBtnClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isValidated()) {
                addOrder();
            } else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HotelInputDataActivity.this);
                alertDialog.setMessage(getString(R.string.field_not_completed));
                alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alertDialog.create().show();
            }
        }
    };

    private boolean isValidated() {
        requestParams = new RequestParams();
        int count = 0;
        if (isFilled(titleEt)) {
            requestParams.put("conSalutation", title);
            contactMap.put("conSalutation", title);
            count++;
        }

        if (isFilled(nameEt)) {
            requestParams.put("conFirstName", nameEt.getText().toString());
            contactMap.put("conFirstName", nameEt.getText().toString());
            count++;
        }

        if (isFilled(lastNameEt)) {
            requestParams.put("conLastName", lastNameEt.getText().toString());
            contactMap.put("conLastName", lastNameEt.getText().toString());
            count++;
        }

        if (isFilled(phoneEt)) {
            requestParams.put("conPhone", phoneEt.getText().toString());
            contactMap.put("conPhone", phoneEt.getText().toString());
            count++;
        }

        if (isFilled(emailEt)) {
            requestParams.put("conEmailAddress", emailEt.getText().toString());
            contactMap.put("conEmailAddress", emailEt.getText().toString());
            count++;
        }

        if (isFilled(guestTitleEt)) {
            requestParams.put("express_salutation", guestTitleEt.getText().toString());
            count++;
        }

        if (isFilled(nationalityEt)) {
            requestParams.put("country", nationality);
            count++;
        }

        if (isFilled(guestFullNameEt)) {
            requestParams.put("express_fullname", guestFullNameEt.getText().toString());
            count++;
        }

        if (isFilled(guestEmailEt)) {
            requestParams.put("express_email", guestEmailEt.getText().toString());
            count++;
        }

        if (isFilled(guestPhoneEt)) {
            requestParams.put("express_phone", guestPhoneEt.getText().toString());
            count++;
        }

        hotelCustomerMap = contactMap;

        return count == 10;
    }

    private boolean isFilled(EditText view) {
        if (view.getText().toString().isEmpty()) {
            view.setError(getString(R.string.needed));
            return false;
        }

        return true;
    }


    View.OnTouchListener onNationalityTouchedListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HotelInputDataActivity.this);
                builder.setItems(countryCollection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        nationalityEt.setText(countryCollection[item]);
                        nationality = countries.get(item).countryId;
                    }
                }).create().show();
            }
            return true;
        }
    };

    View.OnTouchListener onTitleTouchedListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent motionEvent) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HotelInputDataActivity.this);
                builder.setItems(titleCollection, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (v == titleEt) {
                            titleEt.setText(titleCollection[item]);
                            title = getTitleId(item);
                        } else {
                            guestTitleEt.setText(titleCollection[item]);
                            guestTitle = getTitleId(item);
                        }
                    }
                }).create().show();
            }
            return true;
        }
    };

    private String getTitleId(int item) {
        switch (item) {
            case 0:
                return "Mr";
            case 1:
                return "Mrs";
            default:
                return "Ms";
        }
    }

    private void addOrder() {
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        String url = bookUri;

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(300000);
        client.get(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("JSON PASSANGER", response.toString() + "");
                responseString = response.toString();

                try {
                    JSONObject errorMsgs = response.getJSONObject(CommonConstants.diagnostic);
                    String status = errorMsgs.getString(CommonConstants.status);

                    if (status.equals("200")) {
                        nextActivity();
                    } else {
                        String message = errorMsgs.getString(CommonConstants.error_msgs);
                        showAlertDialog(message, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(HotelInputDataActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON PASSANGER", errorResponse + "");
                ErrorException.getError(HotelInputDataActivity.this, errorResponse);
            }
        });
    }

    private void showAlertDialog(String message, final String status) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                HotelInputDataActivity.this).create();

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void nextActivity() {
        Intent intent = new Intent(HotelInputDataActivity.this, ListOrderActivity.class);
        intent.putExtra(CommonConstants.CONTACT_MAP, contactMap);
        intent.putExtra(CommonConstants.HOTE_CUSTOMER_MAP, hotelCustomerMap);
        intent.putExtra(CommonConstants.IS_HOTEL, true);
        startActivity(intent);
    }
}
