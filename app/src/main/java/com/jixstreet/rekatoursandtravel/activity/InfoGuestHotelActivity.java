package com.jixstreet.rekatoursandtravel.activity;

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
import com.jixstreet.rekatoursandtravel.flight.model.RequestedField;
import com.jixstreet.rekatoursandtravel.flight.model.RequiredField;
import com.jixstreet.rekatoursandtravel.model.Country;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class InfoGuestHotelActivity extends AppCompatActivity {
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
    private boolean isExpressCustomer = false;


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
        isExpressCustomer = bundle.getBoolean(CommonConstants.IS_EXPRESS_CUSTOMER);
        bookUri = bundle.getString(CommonConstants.BOOKURI);

//        getCountry();
        setValue();
        setCallBack();
    }

    private void setValue() {
        try {
            requiredObject = new JSONObject(responseString).getJSONObject(CommonConstants.REQUIRED);
            Gson gson = new Gson();
            Iterator<String> iterator = requiredObject.keys();
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            while (iterator.hasNext()) {
                String key = iterator.next();

                final RequiredField requiredField;
                final JSONObject value = requiredObject.getJSONObject(key);
                requiredField = gson.fromJson(value.toString(), RequiredField.class);

                if (requiredField.category != null &&requiredField.category.equals(CommonConstants.SEPARATOR)) {
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
                            fieldET.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                                        JSONArray places = value.optJSONArray("resource");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(InfoGuestHotelActivity.this);
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


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCallBack() {
        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidated()) {
                    if (isExpressCustomer)
                        nextActivity();
                    else
                        addOrder();
                } else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfoGuestHotelActivity.this);
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
                Toast.makeText(InfoGuestHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON PASSANGER", errorResponse + "");
                ErrorException.getError(InfoGuestHotelActivity.this, errorResponse);
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

                if (isExpressCustomer) {
                    contactMap.put(requestedField.key, content);
                }
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
        Intent intent = new Intent(InfoGuestHotelActivity.this, ListOrderActivity.class);
        intent.putExtra(CommonConstants.WHAT_ORDER, "FLIGHT");
        intent.putExtra(CommonConstants.CONTACT_MAP, contactMap);
        intent.putExtra(CommonConstants.IS_HOTEL, isHotel);
        intent.putExtra(CommonConstants.IS_EXPRESS_CUSTOMER, isExpressCustomer);
        startActivity(intent);
    }

    private void showAlertDialog(String message, final String status) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                InfoGuestHotelActivity.this).create();

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

    private String getDummyJSONObject() {
        return "{\n" +
                "  \"diagnostic\": {\n" +
                "    \"status\": 200,\n" +
                "    \"elapsetime\": \"0.3221\",\n" +
                "    \"memoryusage\": \"6.53MB\",\n" +
                "    \"unix_timestamp\": 1460242960,\n" +
                "    \"confirm\": \"success\",\n" +
                "    \"lang\": \"id\",\n" +
                "    \"currency\": \"IDR\"\n" +
                "  },\n" +
                "  \"output_type\": \"json\",\n" +
                "  \"required\": {\n" +
                "    \"separator\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"text\",\n" +
                "      \"example\": \"\",\n" +
                "      \"FieldText\": \"Informasi Kontak yang Dapat Dihubungi\",\n" +
                "      \"category\": \"separator\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"conSalutation\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"combobox\",\n" +
                "      \"example\": \"Mr\",\n" +
                "      \"FieldText\": \"Titel\",\n" +
                "      \"category\": \"contact\",\n" +
                "      \"resource\": [\n" +
                "        {\n" +
                "          \"id\": \"Mr\",\n" +
                "          \"name\": \"Tuan\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"Mrs\",\n" +
                "          \"name\": \"Nyonya\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"Ms\",\n" +
                "          \"name\": \"Nona\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"conFirstName\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"Jane\",\n" +
                "      \"FieldText\": \"Nama Lengkap sesuai dengan KTP/Passport/SIM\",\n" +
                "      \"category\": \"contact\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"conLastName\": {\n" +
                "      \"mandatory\": 0,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"wacob\",\n" +
                "      \"FieldText\": \"Nama Belakang\",\n" +
                "      \"category\": \"contact\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"conPhone\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"+6285212345678\",\n" +
                "      \"FieldText\": \"No. Telepon\",\n" +
                "      \"category\": \"contact\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"conEmailAddress\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"example@email.com\",\n" +
                "      \"FieldText\": \"Kontak Email\",\n" +
                "      \"category\": \"contact\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"separator_adult1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"text\",\n" +
                "      \"example\": \"\",\n" +
                "      \"FieldText\": \"Penumpang Dewasa 1\",\n" +
                "      \"category\": \"separator\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"titlea1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"combobox\",\n" +
                "      \"example\": \"Mr\",\n" +
                "      \"FieldText\": \"Titel\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"resource\": [\n" +
                "        {\n" +
                "          \"id\": \"Mr\",\n" +
                "          \"name\": \"Tuan\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"Mrs\",\n" +
                "          \"name\": \"Nyonya\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"Ms\",\n" +
                "          \"name\": \"Nona\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"firstnamea1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"Jane\",\n" +
                "      \"FieldText\": \"Nama Lengkap sesuai dengan KTP/Passport/SIM\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"lastnamea1\": {\n" +
                "      \"mandatory\": 0,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"wacob\",\n" +
                "      \"FieldText\": \"Nama Belakang\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"birthdatea1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"datetime\",\n" +
                "      \"example\": \"1990-01-01\",\n" +
                "      \"FieldText\": \"Tanggal Lahir\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"passportnationalitya1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"combobox\",\n" +
                "      \"example\": \"ID\",\n" +
                "      \"FieldText\": \"Kewarganegaraan\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"resource\": \"https://api.tiket.com/general_api/listCountry\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"passportnoa1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"textbox\",\n" +
                "      \"example\": \"\",\n" +
                "      \"FieldText\": \"Nomor Passport\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"passportExpiryDatea1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"datetime\",\n" +
                "      \"example\": \"\",\n" +
                "      \"FieldText\": \"Tanggal Berakhir Passport\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"passportissuinga1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"combobox\",\n" +
                "      \"example\": \"\",\n" +
                "      \"FieldText\": \"Negara yang Mengeluarkan\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"resource\": \"https://api.tiket.com/general_api/listCountry\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"passportissueddatea1\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"datetime\",\n" +
                "      \"example\": \"\",\n" +
                "      \"FieldText\": \"Tanggal Penerbitan\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"disabled\": \"false\"\n" +
                "    },\n" +
                "    \"dcheckinbaggagea11\": {\n" +
                "      \"mandatory\": 1,\n" +
                "      \"type\": \"combobox\",\n" +
                "      \"example\": 0,\n" +
                "      \"FieldText\": \"Pilih Bagasi Penerbangan Pergi (CGK - BKK)\",\n" +
                "      \"category\": \"adult1\",\n" +
                "      \"resource\": [\n" +
                "        {\n" +
                "          \"id\": \"0\",\n" +
                "          \"name\": \"Tidak ada bagasi terdaftar\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"15\",\n" +
                "          \"name\": \"Bagasi 15 kg (+ IDR 367.505,00)\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"20\",\n" +
                "          \"name\": \"Bagasi 20 kg (+ IDR 406.190,00)\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"25\",\n" +
                "          \"name\": \"Bagasi 25 kg (+ IDR 483.558,00)\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"30\",\n" +
                "          \"name\": \"Bagasi 30 kg (+ IDR 725.338,00)\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"35\",\n" +
                "          \"name\": \"Bagasi 35 kg (+ IDR 967.118,00)\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"40\",\n" +
                "          \"name\": \"Bagasi 40 kg (+ IDR 1.353.966,00)\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"disabled\": \"false\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"departures\": {\n" +
                "    \"flight_id\": \"11007104\",\n" +
                "    \"airlines_name\": \"TIGER\",\n" +
                "    \"flight_number\": \"TR-2273/TR-2102\",\n" +
                "    \"flight_date\": \"2016-04-11\",\n" +
                "    \"stop\": \"1 Transit\",\n" +
                "    \"price_value\": \"1100229.00\",\n" +
                "    \"price_adult\": \"1100229.00\",\n" +
                "    \"price_child\": \"0.00\",\n" +
                "    \"price_infant\": \"0.00\",\n" +
                "    \"count_adult\": \"1\",\n" +
                "    \"count_child\": \"0\",\n" +
                "    \"count_infant\": \"0\",\n" +
                "    \"timestamp\": \"2016-04-07 06:26:55\",\n" +
                "    \"simple_departure_time\": \"19:55\",\n" +
                "    \"simple_arrival_time\": \"08:00\",\n" +
                "    \"long_via\": \"Singapore (SIN)\",\n" +
                "    \"full_via\": \"CGK - SIN (19:55 - 22:35), SIN - BKK (06:40 - 08:00)\",\n" +
                "    \"markup_price_string\": \"\",\n" +
                "    \"need_baggage\": 1,\n" +
                "    \"duration\": \"12 j 5 m\",\n" +
                "    \"image\": \"https://www.tiket.com/images/tiket2/icon_tiger_2.jpg\",\n" +
                "    \"flight_infos\": {\n" +
                "      \"flight_info\": [\n" +
                "        {\n" +
                "          \"flight_number\": \"TR-2273\",\n" +
                "          \"departure_city\": \"CGK\",\n" +
                "          \"arrival_city\": \"SIN\",\n" +
                "          \"simple_departure_time\": \"\",\n" +
                "          \"simple_arrival_time\": \"\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"flight_number\": \"TR-2102\",\n" +
                "          \"departure_city\": \"SIN\",\n" +
                "          \"arrival_city\": \"BKK\",\n" +
                "          \"simple_departure_time\": \"\",\n" +
                "          \"simple_arrival_time\": \"\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"sss_key\": null\n" +
                "  },\n" +
                "  \"login_status\": \"false\",\n" +
                "  \"token\": \"4242d367e99d7e90510cf5cbe5b4b85bfe4699d6\"\n" +
                "}";
    }

    private String getHotelCustomerField() {
        return getString(R.string.hotel_customer_field);
    }
}
