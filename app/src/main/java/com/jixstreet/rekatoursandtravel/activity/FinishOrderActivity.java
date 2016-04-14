package com.jixstreet.rekatoursandtravel.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.model.MyOrder;
import com.jixstreet.rekatoursandtravel.model.Step;
import com.jixstreet.rekatoursandtravel.utils.CommonConstants;
import com.jixstreet.rekatoursandtravel.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FinishOrderActivity extends AppCompatActivity {

    @Bind(R.id.tv_orderid)
    protected TextView orderIdTV;

    @Bind(R.id.order_wrapper)
    protected LinearLayout orderWrapper;

    @Bind(R.id.step_wrapper)
    protected LinearLayout stepWrapper;

    @Bind(R.id.next_btn)
    protected TextView nextTV;

    @Bind(R.id.tv_total)
    protected TextView totalTV;

    private LayoutInflater inflater;
    private ArrayList<MyOrder> orders = new ArrayList<>();
    private String responseObjectString;
    private ArrayList<Step> stepList = new ArrayList<>();
    private String message;
    private String orderId;
    private int grandSubtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handleIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        ButterKnife.bind(this);

        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        orders = ListOrderActivity.getMyOrders();

        parseObject();
        setValue();
        setCallBack();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        responseObjectString = intent.getStringExtra(CommonConstants.RESPONE);
    }

    private void parseObject() {
        try {
            JSONObject jsonObject = new JSONObject(responseObjectString);

            //parse step
            JSONArray places = jsonObject.optJSONArray(CommonConstants.STEPS);
            if (places != null) {
                JSONArray stepsArray = jsonObject.getJSONArray(CommonConstants.STEPS);
                for (int i = 0; i < stepsArray.length(); i++) {
                    JSONObject stepObj = stepsArray.getJSONObject(i);
                    ArrayList<String> steps = new ArrayList<>();
                    JSONArray stepArray = stepObj.getJSONArray(CommonConstants.STEP);
                    for (int j = 0; j < stepArray.length(); j++) {
                        steps.add(stepArray.getString(j));
                    }

                    Step step = new Step();
                    step.name = stepObj.getString(CommonConstants.NAME);
                    step.step = steps;

                    stepList.add(step);
                }
            } else {
                JSONObject stepObject = jsonObject.getJSONObject(CommonConstants.STEPS);
                JSONArray stepArray = stepObject.getJSONArray(CommonConstants.STEP);

                ArrayList<String> steps = new ArrayList<>();
                for (int j = 0; j < stepArray.length(); j++) {
                    steps.add(stepArray.getString(j));
                }

                Step step = new Step();
                step.name = "Informasi Penting";
                step.step = steps;

                stepList.add(step);
            }

            //parse message
            message = jsonObject.getString(CommonConstants.MESSAGE);

            //parse order id
            JSONObject object = jsonObject.getJSONObject(CommonConstants.RESULT);
            orderId = object.getString(CommonConstants.ORDER_ID);
            grandSubtotal = object.getInt(CommonConstants.GRAND_TOTAL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setValue() {
        for (int i = 0; i < orders.size(); i++) {
            MyOrder myOrder = orders.get(i);
            View view = inflater.inflate(R.layout.item_myorder, null);
            TextView tvOrderType = (TextView) view.findViewById(R.id.tv_order_type);
            TextView tvOrderName = (TextView) view.findViewById(R.id.tv_order_name);
            TextView tvOrderNameDetail = (TextView) view.findViewById(R.id.tv_order_name_detail);
            TextView tvOrderExpire = (TextView) view.findViewById(R.id.tv_order_expire);
            ImageView ivDeleteOrder = (ImageView) view.findViewById(R.id.iv_delete_order);
            ImageView ivOrder = (ImageView) view.findViewById(R.id.iv_order);
            TextView ivPriceOrder = (TextView) view.findViewById(R.id.tv_order_price);

            tvOrderType.setText(myOrder.orderType.toUpperCase());
            tvOrderName.setText(myOrder.orderName);
            tvOrderNameDetail.setText(myOrder.orderNameDetail);
            tvOrderExpire.setText("Expired until: " + myOrder.orderExpireDatetime);
            ivPriceOrder.setText(Util.toRupiahFormat(myOrder.subtotalAndCharge));
            Picasso.with(this).load(myOrder.orderPhoto).into(ivOrder);
            ivDeleteOrder.setVisibility(View.INVISIBLE);

            orderWrapper.addView(view);
        }

        for (int i = 0; i < stepList.size(); i++) {
            Step step = stepList.get(i);
            View view = inflater.inflate(R.layout.item_step_card, null);
            TextView nameTV = (TextView) view.findViewById(R.id.step_name_tv);
            LinearLayout stepByStepWrapper = (LinearLayout) view.findViewById(R.id.steps_by_step_wrapper);

            nameTV.setText(step.name);
            for (int y = 0; y < step.step.size(); y++) {
                TextView stepTV = (TextView) inflater.inflate(R.layout.item_step, null);
                stepTV.setText("\u2022 " + step.step.get(y));
                stepByStepWrapper.addView(stepTV);
            }

            stepWrapper.addView(view);
        }

        orderIdTV.setText(orderId);
        totalTV.setText("IDR " + grandSubtotal);

        showAlertDialog();
    }

    private void setCallBack() {
        nextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showAlertDialog() {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                FinishOrderActivity.this).create();

        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinishOrderActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
