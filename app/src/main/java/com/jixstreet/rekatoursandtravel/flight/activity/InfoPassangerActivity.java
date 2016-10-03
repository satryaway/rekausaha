package com.jixstreet.rekatoursandtravel.flight.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.RekaApplication;
import com.jixstreet.rekatoursandtravel.Session.CountrySessionManager;
import com.jixstreet.rekatoursandtravel.activity.ListOrderActivity;
import com.jixstreet.rekatoursandtravel.flight.model.RequestedField;
import com.jixstreet.rekatoursandtravel.flight.model.RequiredField;
import com.jixstreet.rekatoursandtravel.model.Country;
import com.jixstreet.rekatoursandtravel.utils.APIAgent;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class InfoPassangerActivity extends AppCompatActivity {
    @Bind(R.id.passanger_info_wrapper)
    LinearLayout passengerInfoWrapper;
    private SimpleDateFormat dateFormatter;
    private String responseString;
    private Bundle bundle;

    private CountrySessionManager countrySessionManager;
    private ArrayList<Country> countries;
    private ArrayList<CardView> cardFieldWrapperList = new ArrayList<>();
    private AbsListView spinnerNumber;
    private String[] countryCollection;
    private List<RequestedField> requestedFieldArrayList = new ArrayList<>();
    private RequestParams requestParams = new RequestParams();
    private JSONObject requiredObject;
    private String flightID;
    private String retFlightID;
    private boolean isReturn;
    private boolean isHotel = false;
    private HashMap<String, String> contactMap = new HashMap<>();
    private String bookUri;
    private HashMap<String, String> hotelCustomerMap = new HashMap<>();
    private List<RequiredField> requiredFields = new ArrayList<>();
    private List<RequiredField> finalRequiredFields = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_passanger);
        ButterKnife.bind(this);

        countrySessionManager = new CountrySessionManager(this);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        bundle = getIntent().getExtras();
        responseString = bundle.getString(CommonConstants.RESPONE);
        flightID = bundle.getString(CommonConstants.FLIGHT_ID);
        retFlightID = bundle.getString(CommonConstants.RET_FLIGHT_ID);
        isReturn = bundle.getBoolean(CommonConstants.IS_RETURN);
        isHotel = bundle.getBoolean(CommonConstants.IS_HOTEL);
        bookUri = bundle.getString(CommonConstants.BOOKURI);

        getCountry();
        setValue();
        setCallBack();
    }

    private void setValue() {
        try {
            requiredObject = new JSONObject(responseString).getJSONObject(CommonConstants.REQUIRED);
            Gson gson = new Gson();
            Iterator<String> iterator = requiredObject.keys();
            layoutInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            Iterator<String> tempIterator = requiredObject.keys();

            if (!tempIterator.next().equals(CommonConstants.SEPARATOR)) {
                collectField(requiredObject);
            } else {
                while (iterator.hasNext()) {
                    String key = iterator.next();

                    final RequiredField requiredField;
                    final JSONObject value = requiredObject.getJSONObject(key);
                    requiredField = gson.fromJson(value.toString(), RequiredField.class);

                    if (requiredField.category != null && requiredField.category.equals(CommonConstants.SEPARATOR)) {
                        CardView cardWrapper = (CardView) layoutInflater.inflate(R.layout.item_card_field_box, null);
                        LinearLayout fieldWrapper = (LinearLayout) cardWrapper.findViewById(R.id.field_wrapper);
                        TextView separatorTitleTV = (TextView) layoutInflater.inflate(R.layout.item_separator_field, null);
                        separatorTitleTV.setText(requiredField.fieldText);
                        fieldWrapper.addView(separatorTitleTV);
                        cardFieldWrapperList.add(cardWrapper);
                    } else {
                        if (cardFieldWrapperList.size() != 0) {
                            TextView fieldTV;
                            final EditText fieldET;
                            ViewGroup editTextWrapper;

                            if (requiredField.type.equals(CommonConstants.COMBOBOX)) {
                                editTextWrapper = (ViewGroup) layoutInflater.inflate(R.layout.item_text_field_with_spinner, null);
                                fieldET = (EditText) editTextWrapper.getChildAt(1);
                                final JSONArray places = value.optJSONArray("resource");
                                if (places == null) {
                                    requiredField.value = "id";
                                    fieldET.setText("Indonesia");
                                }
                                fieldET.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(InfoPassangerActivity.this);
                                            if (places == null) {
                                                builder.setItems(countryCollection, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int item) {
                                                        fieldET.setText(countryCollection[item]);
                                                        requiredField.value = countries.get(item).countryId;
                                                    }
                                                }).create().show();
                                            } else {
                                                final String[] adultTitle = new String[places.length()];
                                                for (int i = 0; i < places.length(); i++) {
                                                    try {
                                                        JSONObject jsonObject = places.getJSONObject(i);
                                                        adultTitle[i] = places.getJSONObject(i).getString("name");
                                                        requiredField.value = jsonObject.getString("id");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                builder.setItems(adultTitle, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int item) {
                                                        fieldET.setText(adultTitle[item]);
                                                    }
                                                }).create().show();
                                            }
                                        }
                                        return true;
                                    }
                                });
                                fieldET.setTag(key);
                            } else {
                                editTextWrapper = (ViewGroup) layoutInflater.inflate(R.layout.item_text_field, null);
                                fieldET = (EditText) editTextWrapper.getChildAt(1);
                                fieldET.setTag(key);
                                if (key.toLowerCase().contains("date")) {
                                    fieldET.setFocusable(false);
                                    fieldET.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {
                                            new CalendarDatePickerDialogFragment().setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                                    Calendar takenDate = Calendar.getInstance();
                                                    takenDate.set(year, monthOfYear, dayOfMonth);
                                                    ((EditText) v).setText(dateFormatter.format(takenDate.getTime()));
                                                }
                                            }).show(getSupportFragmentManager(), "DATEPICKER");
                                        }
                                    });
                                } else if (key.toLowerCase().contains("phone")) {
                                    fieldET.setInputType(InputType.TYPE_CLASS_PHONE);
                                } else if (key.toLowerCase().contains("no")) {
                                    fieldET.setInputType(InputType.TYPE_CLASS_NUMBER);
                                } else if (key.toLowerCase().contains("email")) {
                                    fieldET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                                }

                                if (key.toLowerCase().contains("passportno")) {
                                    fieldET.setInputType(InputType.TYPE_CLASS_TEXT);
                                }
                            }

                            fieldTV = (TextView) editTextWrapper.getChildAt(0);
                            fieldTV.setHint(requiredField.fieldText);

                            ViewGroup cardWrapper = cardFieldWrapperList.get(cardFieldWrapperList.size() - 1);
                            LinearLayout fieldWrapper = (LinearLayout) cardWrapper.getChildAt(0);
                            fieldWrapper.addView(editTextWrapper);
                            requestedFieldArrayList.add(new RequestedField(requiredField, editTextWrapper, key));
                        }
                    }

                }

                for (int i = 0; i < cardFieldWrapperList.size(); i++) {
                    passengerInfoWrapper.addView(cardFieldWrapperList.get(i));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void collectField(JSONObject requiredObject) throws JSONException {
        Iterator<String> iterator = requiredObject.keys();
        Gson gson = new Gson();
        requiredFields = new ArrayList<>();
        finalRequiredFields = new ArrayList<>();
        keys = new ArrayList<>();

        while (iterator.hasNext()) {
            String key = iterator.next();

            final RequiredField requiredField;
            final JSONObject value = requiredObject.getJSONObject(key);
            requiredField = gson.fromJson(value.toString(), RequiredField.class);
            requiredField.key = key;
            requiredFields.add(requiredField);
            keys.add(key);
        }

        //Get the separator, EQUALS
        iterateObject(CommonConstants.SEPARATOR, false);

        //Collect the "con", CONTAINS
        List<RequiredField> required = sortFields(getIteratedObject("con", true));
        finalRequiredFields.addAll(required);

        //Collect adult
        checkExistence("a");

        //Collect child
        checkExistence("c");

        //Collect infant
        checkExistence("i");

        requiredFields = finalRequiredFields;

        makeFields();
    }

    private void makeFields() throws JSONException {
        for (int i = 0; i < requiredFields.size(); i++) {
            final RequiredField requiredField = requiredFields.get(i);

            if (requiredField.category != null && requiredField.category.equals(CommonConstants.SEPARATOR)) {
                CardView cardWrapper = (CardView) layoutInflater.inflate(R.layout.item_card_field_box, null);
                LinearLayout fieldWrapper = (LinearLayout) cardWrapper.findViewById(R.id.field_wrapper);
                TextView separatorTitleTV = (TextView) layoutInflater.inflate(R.layout.item_separator_field, null);
                separatorTitleTV.setText(requiredField.fieldText);
                fieldWrapper.addView(separatorTitleTV);
                cardFieldWrapperList.add(cardWrapper);
            } else {
                if (cardFieldWrapperList.size() != 0) {
                    TextView fieldTV;
                    final EditText fieldET;
                    ViewGroup editTextWrapper;

                    if (requiredField.type.equals(CommonConstants.COMBOBOX)) {
                        editTextWrapper = (ViewGroup) layoutInflater.inflate(R.layout.item_text_field_with_spinner, null);
                        fieldET = (EditText) editTextWrapper.getChildAt(1);
                        JSONObject val = requiredObject.getJSONObject(requiredField.key);
                        final JSONArray places = val.optJSONArray("resource");
                        if (places == null) {
                            requiredField.value = "id";
                            fieldET.setText("Indonesia");
                        }
                        fieldET.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoPassangerActivity.this);
                                    if (places == null) {
                                        builder.setItems(countryCollection, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                fieldET.setText(countryCollection[item]);
                                                requiredField.value = countries.get(item).countryId;
                                            }
                                        }).create().show();
                                    } else {
                                        final String[] adultTitle = new String[places.length()];
                                        for (int i = 0; i < places.length(); i++) {
                                            try {
                                                JSONObject jsonObject = places.getJSONObject(i);
                                                adultTitle[i] = places.getJSONObject(i).getString("name");
                                                requiredField.value = jsonObject.getString("id");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        builder.setItems(adultTitle, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                fieldET.setText(adultTitle[item]);
                                            }
                                        }).create().show();
                                    }
                                }
                                return true;
                            }
                        });
                        fieldET.setTag(requiredField.key);
                    } else {
                        editTextWrapper = (ViewGroup) layoutInflater.inflate(R.layout.item_text_field, null);
                        fieldET = (EditText) editTextWrapper.getChildAt(1);
                        fieldET.setTag(requiredField.key);
                        if (requiredField.key.toLowerCase().contains("date")) {
                            fieldET.setFocusable(false);
                            fieldET.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {
                                    new CalendarDatePickerDialogFragment().setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                            Calendar takenDate = Calendar.getInstance();
                                            takenDate.set(year, monthOfYear, dayOfMonth);
                                            ((EditText) v).setText(dateFormatter.format(takenDate.getTime()));
                                        }
                                    }).show(getSupportFragmentManager(), "DATEPICKER");
                                }
                            });
                        } else if (requiredField.key.toLowerCase().contains("phone")) {
                            fieldET.setInputType(InputType.TYPE_CLASS_PHONE);
                        } else if (requiredField.key.toLowerCase().contains("no")) {
                            fieldET.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else if (requiredField.key.toLowerCase().contains("email")) {
                            fieldET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }

                        if (requiredField.key.toLowerCase().contains("passportno")) {
                            fieldET.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    }

                    fieldTV = (TextView) editTextWrapper.getChildAt(0);
                    fieldTV.setHint(requiredField.fieldText);

                    ViewGroup cardWrapper = cardFieldWrapperList.get(cardFieldWrapperList.size() - 1);
                    LinearLayout fieldWrapper = (LinearLayout) cardWrapper.getChildAt(0);
                    fieldWrapper.addView(editTextWrapper);
                    requestedFieldArrayList.add(new RequestedField(requiredField, editTextWrapper, requiredField.key));
                }
            }
        }

        for (int i = 0; i < cardFieldWrapperList.size(); i++) {
            passengerInfoWrapper.addView(cardFieldWrapperList.get(i));
        }
    }

    void checkExistence(String type) {
        int iterator = 1;
        boolean isExisted = true;
        String s = type + iterator;
        String kind = "adult";
        switch (type) {
            case "c":
                kind = "child";
                break;
            case "i":
                kind = "infant";
                break;
        }

        List<RequiredField> requiredFieldList;

        while (isExisted) {
            requiredFieldList = new ArrayList<>();
            String fullKind = "separator_" + kind + iterator;
            iterateObject(fullKind, false);
            for (int i = 0; i < requiredFields.size(); i++) {
                String s1 = requiredFields.get(i).key;
                if (s1.contains(s)) {
                    requiredFieldList.addAll(getIteratedObject(s1, true));
                }
            }

            requiredFieldList = sortFields(requiredFieldList);
            finalRequiredFields.addAll(requiredFieldList);

            iterator++;
            s = type + iterator;
            boolean isFound = false;
            for (int i = 0; i < requiredFields.size() && !isFound; i++) {
                String s1 = requiredFields.get(i).key;
                if (s1.contains(s)) {
                    isFound = true;
                }
            }

            isExisted = isFound;
        }
    }

    List<RequiredField> sortFields(List<RequiredField> requiredFieldList) {
        for (int i = 0; i < requiredFieldList.size(); i++) {
            RequiredField requiredField = requiredFieldList.get(i);
            if (requiredField.key.toLowerCase().contains("title") || requiredField.key.toLowerCase().contains("salutation")) {
                requiredField.groupingPosition = 0;
            } else if (requiredField.key.toLowerCase().contains("firstname")) {
                requiredField.groupingPosition = 1;
            } else if (requiredField.key.toLowerCase().contains("lastname")) {
                requiredField.groupingPosition = 2;
            } else if (requiredField.key.toLowerCase().contains("birthdate")) {
                requiredField.groupingPosition = 3;
            } else {
                requiredField.groupingPosition = 99;
            }
        }

        Collections.sort(requiredFieldList, new Comparator<RequiredField>() {
            @Override
            public int compare(RequiredField o1, RequiredField o2) {
                return o1.groupingPosition - o2.groupingPosition;
            }
        });

        return requiredFieldList;
    }

    void iterateObject(String key, boolean isContains) {
        List<Integer> removedPositions = new ArrayList<>();
        for (int i = 0; i < requiredFields.size(); i++) {
            RequiredField requiredField = requiredFields.get(i);
            if (isContains) {
                if (requiredField.key.contains(key)) {
                    finalRequiredFields.add(requiredField);
                    removedPositions.add(i);
//                    removeItem(requiredField);
                }
            } else {
                if (requiredField.key.equals(key)) {
                    finalRequiredFields.add(requiredField);
                    removedPositions.add(i);
//                    removeItem(requiredField);
                }
            }
        }

        /*for (int removedPosition : removedPositions) {
            requiredFields.remove(removedPosition);
        }*/
    }

    List<RequiredField> getIteratedObject(String key, boolean isContains) {
        List<RequiredField> field = new ArrayList<>();
        for (int i = 0; i < requiredFields.size(); i++) {
            RequiredField requiredField = requiredFields.get(i);
            if (isContains) {
                if (requiredField.key.contains(key)) {
//                    finalRequiredFields.add(requiredField);
                    field.add(requiredField);
                }
            } else {
                if (requiredField.key.equals(key)) {
//                    finalRequiredFields.add(requiredField);
                    field.add(requiredField);
                }
            }
        }

        return field;
    }

    private void removeItem(RequiredField key) {
        List<RequiredField> fields = requiredFields;
        for (int i = 0; i < requiredFields.size(); i++) {
            if (key.key.equals(requiredFields.get(i).key)) {
                requiredFields.remove(i);
                return;
            }
        }
    }

    private void setCallBack() {
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidated()) {
                    addOrder();
                } else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoPassangerActivity.this);
                    alertDialog.setMessage(getString(R.string.field_not_completed));
                    alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    alertDialog.create().show();
                }
            }
        });
    }

    private void addOrder() {
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);
        requestParams.put(CommonConstants.TOKEN, RekaApplication.getInstance().getToken());

        if (!isHotel) {
            requestParams.put(CommonConstants.FLIGHT_ID, flightID);
            if (isReturn)
                requestParams.put(CommonConstants.RET_FLIGHT_ID, retFlightID);
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        String url = isHotel ? bookUri : CommonConstants.BASE_URL + "order/add/flight?v=3";

        APIAgent client = new APIAgent();
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
                Toast.makeText(InfoPassangerActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON PASSANGER", errorResponse + "");
                ErrorException.getError(InfoPassangerActivity.this, errorResponse);
            }
        });
    }

    private boolean isValidated() {
        requestParams = new RequestParams();
        int validatedCount = 0;
        for (int i = 0; i < requestedFieldArrayList.size(); i++) {
            RequestedField requestedField = requestedFieldArrayList.get(i);
            ViewGroup fieldWrapper = requestedField.fieldWrapper;
            EditText editText = (EditText) fieldWrapper.findViewWithTag(requestedField.key);
            String content = editText.getText().toString();

            if (content.isEmpty()) {
                editText.setError(getString(R.string.needed));
            } else {
                if (requestedField.requiredField.value != null)
                    content = requestedField.requiredField.value;

                requestParams.add(requestedField.key, content);
                if (requestedField.key.contains("con")) {
                    contactMap.put(requestedField.key, content);
                }

                hotelCustomerMap.put(requestedField.key, content);

                validatedCount++;
            }
        }
        return validatedCount == requestedFieldArrayList.size();
    }

    private void getCountry() {
        if (countrySessionManager.getCountry(this) != null) {
            countries = countrySessionManager.getCountry(this);
            final List<String> listCountry = new ArrayList<>();
            String[] countryTemp = new String[countries.size()];

            for (int i = 0; i < countries.size(); i++) {
                listCountry.add(countries.get(i).countryAreacode);
                countryTemp[i] = countries.get(i).countryName;
            }

            this.countryCollection = countryTemp;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void nextActivity() {
        Intent intent = new Intent(InfoPassangerActivity.this, ListOrderActivity.class);
        intent.putExtra(CommonConstants.CONTACT_MAP, contactMap);
        intent.putExtra(CommonConstants.HOTE_CUSTOMER_MAP, hotelCustomerMap);
        intent.putExtra(CommonConstants.IS_HOTEL, isHotel);
        startActivity(intent);
    }

    private void showAlertDialog(String message, final String status) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                InfoPassangerActivity.this).create();

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                if (status.equals("211")) {
                    nextActivity();
                } else {
                    alertDialog.dismiss();
                }
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
