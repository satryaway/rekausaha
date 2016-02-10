package com.reka.tour.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reka.tour.R;
import com.reka.tour.adapter.PassangerAdapter;
import com.reka.tour.model.DeparturesOrder;
import com.reka.tour.model.Passanger;
import com.reka.tour.model.Resource;
import com.reka.tour.utils.CommonConstants;
import com.reka.tour.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class InfoPassangerActivity extends AppCompatActivity {
    @Bind(R.id.ev_titel)
    EditText evTitel;
    @Bind(R.id.ev_first_name)
    EditText evFirstName;
    @Bind(R.id.ev_last_name)
    EditText evLastName;
    @Bind(R.id.ev_notelp)
    EditText evNotelp;
    @Bind(R.id.ev_email)
    EditText evEmail;

    @Bind(R.id.tv_info_kontak)
    TextView tvInfoKontak;

    @Bind(R.id.list_passanger)
    ListView listPassanger;

    @Bind(R.id.tv_next)
    TextView tvNext;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateDayFormatter;
    private Calendar newCalendar;

    private PassangerAdapter passangerAdapter;
    private ArrayList<Passanger> passangers = new ArrayList<>();
    private ArrayList<Resource> resources = new ArrayList<>();
    private DeparturesOrder departures;
    private String responeString;
    private Bundle bundle;
    private String[] itemTitel;
    private int countAdult;
    private int countChild;
    private int countInfant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_passanger);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        bundle = getIntent().getExtras();
        responeString = bundle.getString(CommonConstants.RESPONE);


        setValue();
        Util.setListview(listPassanger);
        setCallBack();
    }


    private void setValue() {
        try {
            Gson gson = new Gson();
            JSONObject requiredObject = new JSONObject(responeString).getJSONObject(CommonConstants.REQUIRED);

            JSONObject conSalutation = requiredObject.getJSONObject(CommonConstants.CONSALUTATION);
            JSONArray resourcesArray = conSalutation.getJSONArray(CommonConstants.RESOURCE);
            for (int i = 0; i < resourcesArray.length(); i++) {
                resources.add(gson.fromJson(resourcesArray.getJSONObject(i).toString(), Resource.class));
            }


            JSONObject departuresObject = new JSONObject(responeString).getJSONObject(CommonConstants.DEPARTURES);
            departures = gson.fromJson(departuresObject.toString(), DeparturesOrder.class);

        } catch (JSONException e) {
            e.printStackTrace();
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

        itemTitel = new String[resources.size()];
        for (int i = 0; i < resources.size(); i++) {
            itemTitel[i] = resources.get(i).name;
        }

        passangerAdapter = new PassangerAdapter(this, passangers, itemTitel);
        listPassanger.setAdapter(passangerAdapter);
    }

    private void setCallBack() {
        evTitel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoPassangerActivity.this);
                    builder.setItems(itemTitel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            evTitel.setText(itemTitel[item]);
                        }
                    }).create().show();
                }
                return true;
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentDeparture = new Intent(InfoPassangerActivity.this, MethodPaymentActivity.class);
////                intentDeparture.putExtra(CommonConstants.DEPARTURES, depAirportArrayList.get(position));
////                intentDeparture.putExtra(CommonConstants.SEARCH_QUARIES, searchQueries);
//                startActivity(intentDeparture);

                try {
                    submitOrder();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Passanger getProfile() {
        return new Passanger(
                "",
                evTitel.getText().toString(),
                evFirstName.getText().toString(),
                evLastName.getText().toString(),
                1
        );
    }

    private void submitOrder() throws ParseException {
        String url = CommonConstants.BASE_URL + "order/add/flight";

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
        requestParams.put(CommonConstants.CONPHONE, "%2B" + evNotelp.getText().toString());
        requestParams.put(CommonConstants.CONEMAILADDRESS, evEmail.getText().toString());

        for (int i = 0; i < passangerAdapter.getCount(); i++) {

            View view = listPassanger.getChildAt(i);
            EditText evTitel = (EditText) view.findViewById(R.id.ev_titel);
            EditText evFirstName = (EditText) view.findViewById(R.id.ev_first_name);
            EditText evLastName = (EditText) view.findViewById(R.id.ev_last_name);
            EditText evTanggalLahir = (EditText) view.findViewById(R.id.ev_tanggal_lahir);
            String id = String.valueOf(passangerAdapter.getItem(i).getId());

            newCalendar = Calendar.getInstance();
            dateDayFormatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("ind", "IDN"));
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

            if (i >= 0 && i < countAdult) {//0-2
                requestParams.put("titlea" + id, evTitel.getText().toString());
                requestParams.put("firstnamea" + id, evFirstName.getText().toString());
                requestParams.put("lastnamea" + id, evLastName.getText().toString());
            } else {
                if (countChild != 0) {
                    if (i >= countAdult && i < countAdult + countChild) {//2-3
                        requestParams.put("titlec" + id, evTitel.getText().toString());
                        requestParams.put("firstnamec" + id, evFirstName.getText().toString());
                        requestParams.put("lastnamec" + id, evLastName.getText().toString());
                        requestParams.put("birthdatec" + id, dateFormatter.format(dateDayFormatter.parse(evTanggalLahir.getText().toString())));
                    }
                }

                if (countInfant != 0) {
                    if (i >= countAdult + countChild && i < countAdult + countChild + countInfant) {//3-4
                        requestParams.put("titlei" + id, evTitel.getText().toString());
                        requestParams.put("firstnamei" + id, evFirstName.getText().toString());
                        requestParams.put("lastnamei" + id, evLastName.getText().toString());
                        requestParams.put("birthdatei" + id, dateFormatter.format(dateDayFormatter.parse(evTanggalLahir.getText().toString())));
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
                responeString = response.toString();

                Intent intentDeparture = new Intent(InfoPassangerActivity.this, MethodPaymentActivity.class);
//                intentDeparture.putExtra(CommonConstants.DEPARTURES, depAirportArrayList.get(position));
//                intentDeparture.putExtra(CommonConstants.SEARCH_QUARIES, searchQueries);
                startActivity(intentDeparture);

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
                try {
                    Toast.makeText(InfoPassangerActivity.this, errorResponse.getJSONObject(CommonConstants.DIAGNOSTIC).getString(CommonConstants.ERROR_MSGS), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
}
