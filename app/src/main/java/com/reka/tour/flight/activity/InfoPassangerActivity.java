package com.reka.tour.flight.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.RekaApplication;
import com.reka.tour.Session.CountrySessionManager;
import com.reka.tour.activity.ListOrderActivity;
import com.reka.tour.flight.adapter.PassangerAdapter;
import com.reka.tour.flight.model.Departures;
import com.reka.tour.flight.model.DeparturesOrder;
import com.reka.tour.flight.model.Passanger;
import com.reka.tour.flight.model.RequestedField;
import com.reka.tour.flight.model.RequiredField;
import com.reka.tour.flight.model.Resource;
import com.reka.tour.model.Country;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.ErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class InfoPassangerActivity extends AppCompatActivity {
    /*@Bind(R.id.ev_titel)
    EditText evTitel;
    @Bind(R.id.ev_first_name)
    EditText evFirstName;
    @Bind(R.id.ev_last_name)
    EditText evLastName;
    @Bind(R.id.ev_notelp)
    EditText evNotelp;
    @Bind(R.id.ev_email)
    EditText evEmail;

    @Bind(R.id.spinner_number)
    Spinner spinnerNumber;

    @Bind(R.id.tv_info_kontak)
    TextView tvInfoKontak;

    @Bind(R.id.list_passanger)
    ListView listPassanger;

*/
    @Bind(R.id.passanger_info_wrapper)
    LinearLayout passengerInfoWrapper;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateDayFormatter;
    private Calendar newCalendar;

    private PassangerAdapter passangerAdapter;
    private ArrayList<Passanger> passangers = new ArrayList<>();
    private ArrayList<Resource> resources = new ArrayList<>();
    private DeparturesOrder departures;
    private String responseString;
    private Bundle bundle;
    private ArrayList<String> itemTitel = new ArrayList<>();
    private String countAdult;
    private String countChild;
    private String countInfant;

    private CountrySessionManager countrySessionManager;
    private ArrayList<Country> countries;
    private String countryAreacode;
    private boolean enableSpinnerCountry = false;
    private String dummyJSONObject;

    private ArrayList<CardView> cardFieldWrapperList = new ArrayList<>();
    private AbsListView spinnerNumber;
    private String[] countryCollection;
    private List<RequestedField> requestedFieldArrayList = new ArrayList<>();
    private RequestParams requestParams = new RequestParams();
    private JSONObject requiredObject;
    private String flightID;
    private String retFlightID;
    private boolean isReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_passanger);
        ButterKnife.bind(this);

        countrySessionManager = new CountrySessionManager(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        /*(findViewById(R.id.toolbar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evTitel.setText("Tuan");
                evFirstName.setText("Randi");
                evLastName.setText("Perma");
                evNotelp.setText("89931262955");
                evEmail.setText("reka.usaha@gmail.com");
            }
        });*/

        bundle = getIntent().getExtras();
        responseString = bundle.getString(CommonConstants.RESPONE);
//        responseString = getDummyJSONObject();
        flightID = bundle.getString(CommonConstants.FLIGHT_ID);
        retFlightID = bundle.getString(CommonConstants.RET_FLIGHT_ID);
        isReturn = bundle.getBoolean(CommonConstants.IS_RETURN);

        getCountry();
        parseJSONObject();
        setValue();
        setCallBack();
        //Util.setListview(listPassanger);
        //setCallBack();
    }

    private void parseJSONObject() {
        try {
            JSONObject departureObject = new JSONObject(responseString).getJSONObject(CommonConstants.DEPARTURES);
            countAdult = departureObject.getString("count_adult");
            countChild = departureObject.getString("count_child");
            countInfant = departureObject.getString("count_infant");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

                if (requiredField.category.equals(CommonConstants.SEPARATOR)) {
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
        requestParams.put(CommonConstants.FLIGHT_ID, flightID);
        if (isReturn)
            requestParams.put(CommonConstants.RET_FLIGHT_ID, retFlightID);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        String url = CommonConstants.BASE_URL + "order/add/flight?v=3";

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
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


//                try {
//                    Gson gson = new Gson();
//
//                    JSONObject departuresObject = response.getJSONObject(CommonConstants.DEPARTURES);
//                    departures = gson.fromJson(departuresObject.toString(), DeparturesOrder.class);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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
                validatedCount++;
            }
        }
        return validatedCount == requestedFieldArrayList.size();
    }

    /*private void setValue() {

        getCountry();

        try {
            Gson gson = new Gson();
            JSONObject requiredObject = new JSONObject(responseString).getJSONObject(CommonConstants.REQUIRED);

            JSONObject conSalutation = requiredObject.getJSONObject(CommonConstants.CONSALUTATION);
            JSONArray resourcesArray = conSalutation.getJSONArray(CommonConstants.RESOURCE);
            for (int i = 0; i < resourcesArray.length(); i++) {
                resources.add(gson.fromJson(resourcesArray.getJSONObject(i).toString(), Resource.class));
            }

            JSONObject departuresObject = new JSONObject(responseString).getJSONObject(CommonConstants.DEPARTURES);
            departures = gson.fromJson(departuresObject.toString(), DeparturesOrder.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (departures.airlinesName.toUpperCase().equals("AIRASIA")) {
            enableSpinnerCountry = true;
        }

        countAdult = Integer.parseInt(departures.countAdult);
        countChild = Integer.parseInt(departures.countChild);
        countInfant = Integer.parseInt(departures.countInfant);

        for (int i = 0; i < countAdult; i++) {
            passangers.add(new Passanger(
                    "Penumpang Dewasa " + (i + 1), "", "", "", i + 1
            ));

        }

        if (Integer.parseInt(departures.countChild) != 0) {
            for (int i = 0; i < Integer.parseInt(departures.countChild); i++) {
                passangers.add(new Passanger(
                        "Penumpang Anak " + (i + 1), "", "", "", i + 1
                ));
            }
        }
        if (Integer.parseInt(departures.countInfant) != 0) {
            for (int i = 0; i < Integer.parseInt(departures.countInfant); i++) {
                passangers.add(new Passanger(
                        "Penumpang Bayi " + (i + 1), "", "", "", i + 1
                ));
            }
        }

        for (int i = 0; i < resources.size(); i++) {
            itemTitel.add(resources.get(i).name);
        }

        passangerAdapter = new PassangerAdapter(InfoPassangerActivity.this, passangers, itemTitel, countries,enableSpinnerCountry);
        listPassanger.setAdapter(passangerAdapter);
    }*/

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
            //spinnerNumber.setAdapter(new CustomAdapter(this, R.layout.simple_spinner_item, listCountry, countries));
        }

        /*for (int i = 0; i < countries.size(); i++) {
            if ("+62".equals(countries.get(i).countryAreacode)) {
                spinnerNumber.setSelection(i);
                countryAreacode = countries.get(i).countryAreacode.replace("+", "%2B");
            }
        }
        spinnerNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                countryAreacode = parent.getItemAtPosition(position).toString().replace("+", "%2B");
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });*/
    }

    /*private void setCallBack() {
        evTitel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {

                    final String[] titelDewasa = new String[itemTitel.size()];
                    for (int i = 0; i < itemTitel.size(); i++) {
                        titelDewasa[i] = itemTitel.get(i);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoPassangerActivity.this);
                    builder.setItems(titelDewasa, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            evTitel.setText(titelDewasa[item]);
                        }
                    }).create().show();
                }
                return true;
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitOrder();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    /*public Passanger getProfile() {
        return new Passanger(
                "",
                evTitel.getText().toString(),
                evFirstName.getText().toString(),
                evLastName.getText().toString(),
                1
        );
    }*/

    /*private void submitOrder() throws ParseException {
        String url = CommonConstants.BASE_URL + "order/add/flight?v=3";

//        https://api-sandbox.tiket.com/order/add/flight?token=19d0ceaca45f9ee27e3c51df52786f1d904280f9&
// flight_id=2035957&child=1&adult=1&infant=1&conSalutation=Mrs&conFirstName=budianto&
// conLastName=wijaya&conPhone=%2B6287880182218&conEmailAddress=you_julin@yahoo.com&
// firstnamea1=susi&lastnamea1=wijaya&ida1=1116057107900001&titlea1=Mr&conOtherPhone=%2B628521342534&
// titlec1=Ms&firstnamec1=carreen&lastnamec1=athalia&birthdatec1=2005-02-02&titlei1=Mr&parenti1=1&
// firstnamei1=wendy&lastnamei1=suprato&birthdatei1=2015-06-29&output=json

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.FLIGHT_ID, departures.flightId);
        requestParams.put(CommonConstants.ADULT, departures.countAdult);
        requestParams.put(CommonConstants.CHILD, departures.countChild);
        requestParams.put(CommonConstants.INFRANT, departures.countInfant);
        requestParams.put(CommonConstants.CONSALUTATION, evTitel.getText().toString());
        requestParams.put(CommonConstants.CONFIRSTNAME, evFirstName.getText().toString());
        requestParams.put(CommonConstants.CONLASTNAME, evLastName.getText().toString());
        requestParams.put(CommonConstants.CONPHONE, countryAreacode + evNotelp.getText().toString());
        requestParams.put(CommonConstants.CONEMAILADDRESS, evEmail.getText().toString());

        for (int i = 0; i < passangerAdapter.getCount(); i++) {

            View view = listPassanger.getChildAt(i);
            EditText evTitel = (EditText) view.findViewById(R.id.ev_titel);
            EditText evFirstName = (EditText) view.findViewById(R.id.ev_first_name);
            EditText evLastName = (EditText) view.findViewById(R.id.ev_last_name);
            Spinner spinnerCountryId = (Spinner) view.findViewById(R.id.spinner_country);
            EditText evTanggalLahir = (EditText) view.findViewById(R.id.ev_tanggal_lahir);
            String id = String.valueOf(passangerAdapter.getItem(i).getId());

            newCalendar = Calendar.getInstance();
            dateDayFormatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("ind", "IDN"));
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

            if (i >= 0 && i < countAdult) {//0-2
                requestParams.put("titlea" + id, evTitel.getText().toString());
                requestParams.put("firstnamea" + id, evFirstName.getText().toString());
                requestParams.put("lastnamea" + id, evLastName.getText().toString());
                requestParams.put("birthdatea" + id, dateFormatter.format(dateDayFormatter.parse(evTanggalLahir.getText().toString())));

                if (enableSpinnerCountry) {
                    requestParams.put("passportnationalitya" + id, getCoutryId(spinnerCountryId.getSelectedItem().toString()));
                }

            } else {
                if (countChild != 0) {
                    if (i >= countAdult && i < countAdult + countChild) {//2-3
                        requestParams.put("titlec" + id, evTitel.getText().toString());
                        requestParams.put("firstnamec" + id, evFirstName.getText().toString());
                        requestParams.put("lastnamec" + id, evLastName.getText().toString());
                        requestParams.put("birthdatec" + id, dateFormatter.format(dateDayFormatter.parse(evTanggalLahir.getText().toString())));

                        if (enableSpinnerCountry) {
                            requestParams.put("passportnationalityc" + id, getCoutryId(spinnerCountryId.getSelectedItem().toString()));
                        }
                    }
                }

                if (countInfant != 0) {
                    if (i >= countAdult + countChild && i < countAdult + countChild + countInfant) {//3-4
                        requestParams.put("titlei" + id, evTitel.getText().toString());
                        requestParams.put("firstnamei" + id, evFirstName.getText().toString());
                        requestParams.put("lastnamei" + id, evLastName.getText().toString());
                        requestParams.put("birthdatei" + id, dateFormatter.format(dateDayFormatter.parse(evTanggalLahir.getText().toString())));

                        if (enableSpinnerCountry) {
                            requestParams.put("passportnationalityi" + id, getCoutryId(spinnerCountryId.getSelectedItem().toString()));
                        }
                    }
                }

            }
        }


        requestParams.put(CommonConstants.TOKEN, "19d0ceaca45f9ee27e3c51df52786f1d904280f9");
        requestParams.put(CommonConstants.OUTPUT, CommonConstants.JSON);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.setTimeout(10000);
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


//                try {
//                    Gson gson = new Gson();
//
//                    JSONObject departuresObject = response.getJSONObject(CommonConstants.DEPARTURES);
//                    departures = gson.fromJson(departuresObject.toString(), DeparturesOrder.class);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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
    }*/

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
        intent.putExtra(CommonConstants.WHAT_ORDER, "FLIGHT");
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

    private String getCoutryId(String countryName) {
        String countryId = "";
        for (int i = 0; i < countries.size(); i++) {
            if (countryName.toUpperCase().equals(countries.get(i).countryName.toUpperCase())) {
                countryId = countries.get(i).countryId;
            }
        }
        return countryId;
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
}
