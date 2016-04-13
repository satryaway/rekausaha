package com.jixstreet.rekatoursandtravel.hotel.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.activity.ListPaymentActivity;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.ErrorException;

import org.json.JSONObject;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class InfoCustomerHotelActivity extends AppCompatActivity {
    @Bind(R.id.ev_titel) EditText evTitel;
    @Bind(R.id.field_et) EditText evFirstName;
    @Bind(R.id.ev_last_name) EditText evLastName;
    @Bind(R.id.ev_notelp) EditText evNotelp;
    @Bind(R.id.ev_email) EditText evEmail;

    @Bind(R.id.ev_titel_tamu) EditText evTitelTamu;
    @Bind(R.id.ev_first_name_tamu) EditText evFirstNameTamu;
    @Bind(R.id.ev_last_name_tamu) EditText evLastNameTamu;
    @Bind(R.id.ev_notelp_tamu) EditText evNotelpTamu;
    @Bind(R.id.ev_email_tamu) EditText evEmailTamu;

    @Bind(R.id.switch_not_customer) Switch switchNotCustomer;
    @Bind(R.id.layout_tamu) CardView layoutTamu;


    @Bind(R.id.next_btn) TextView tvNext;
    private Bundle bundle;
    private String[] titelDewasa = {"Mr", "Mrs", "Ms"};
    private String detailId;

    private String titelTamu;
    private String firstNameTamu;
    private String lastNameTamu;
    private String notelpTamu;
    private String emailTamu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_customer_hotel);
        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        (findViewById(R.id.toolbar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evTitel.setText("Mr");
                evFirstName.setText("Randi");
                evLastName.setText("Perma");
                evNotelp.setText("6289931262955");
                evEmail.setText("reka.usaha@gmail.com");
            }
        });

        bundle = getIntent().getExtras();
        detailId = bundle.getString(CommonConstants.DETAIL_ID);

        setCallBack();
    }

    private void setCallBack() {
        evTitel.setText(titelDewasa[0]);
        evTitel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoCustomerHotelActivity.this);
                    builder.setItems(titelDewasa, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            evTitel.setText(titelDewasa[item]);
                        }
                    }).create().show();
                }
                return true;
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getInfoCustomer();
                    submitOrder();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        switchNotCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutTamu.setVisibility(View.VISIBLE);
                    titelTamu = evTitelTamu.getText().toString();
                    firstNameTamu = evFirstNameTamu.getText().toString();
                    lastNameTamu = evLastNameTamu.getText().toString();
                    notelpTamu = evNotelpTamu.getText().toString();
                    emailTamu = evEmailTamu.getText().toString();
                } else {
                    layoutTamu.setVisibility(View.GONE);
                    getInfoCustomer();
                }
            }
        });

    }

    private void getInfoCustomer() {
        titelTamu = evTitel.getText().toString();
        firstNameTamu = evFirstName.getText().toString();
        lastNameTamu = evLastName.getText().toString();
        notelpTamu = evNotelp.getText().toString();
        emailTamu = evEmail.getText().toString();
    }

    private void submitOrder() throws ParseException {
        String url = CommonConstants.BASE_URL + "checkout/checkout_customer";

//        String url = "https://api-sandbox.tiket.com/checkout/checkout_customer?" +
//                "token=19d0ceaca45f9ee27e3c51df52786f1d904280f9&" +
//                "salutation=Mrs&firstName=ba&lastName=ca&emailAddress=bibi@yahoocom&" +
//                "phone=%2B628888843&conSalutation=Mrs&conFirstName=a&conLastName=a&" +
//                "conEmailAddress=bibi@yahoocom&conPhone=%2B628888843&detailId=12688255&output=json&country=id";

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.SALUTATION, evTitel.getText().toString());
        requestParams.put(CommonConstants.FIRSTNAME, evFirstName.getText().toString());
        requestParams.put(CommonConstants.LASTNAME, evLastName.getText().toString());
        requestParams.put(CommonConstants.PHONE, "%2B" + evNotelp.getText().toString());
        requestParams.put(CommonConstants.EMAILADDRESS, evEmail.getText().toString());
        requestParams.put(CommonConstants.CONSALUTATION, titelTamu);
        requestParams.put(CommonConstants.CONFIRSTNAME, firstNameTamu);
        requestParams.put(CommonConstants.CONLASTNAME, lastNameTamu);
        requestParams.put(CommonConstants.CONPHONE, "%2B" + notelpTamu);
        requestParams.put(CommonConstants.CONEMAILADDRESS, emailTamu);
        requestParams.put(CommonConstants.DETAIL_ID, detailId);
        requestParams.put(CommonConstants.COUNTRY, "id");
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
                Log.e("JSON CUSTOMER", response.toString() + "");

                Intent intentDeparture = new Intent(InfoCustomerHotelActivity.this, ListPaymentActivity.class);
                startActivity(intentDeparture);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(InfoCustomerHotelActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("JSON InfoCustomerHotel", errorResponse + "");
                ErrorException.getError(InfoCustomerHotelActivity.this, errorResponse);
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