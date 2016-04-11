package com.reka.tour;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.reka.tour.utils.CommonConstants;

public class CheckoutPaymentActivity extends AppCompatActivity {

    private static final String TAG = "webview";
    private WebView webview;
    private ProgressDialog progressBar;
    private String url;
    private String type;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handleIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_payment);

        ((Toolbar) findViewById(R.id.toolbar)).setTitle(text);
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationIcon(R.drawable.back);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        initUI();
        setCallBack();
        if (!url.contains("api"))
            setValue();
        else {
            Toast.makeText(CheckoutPaymentActivity.this, "Here", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra(CommonConstants.LINK);
        type = intent.getStringExtra(CommonConstants.TYPE);
        text = intent.getStringExtra(CommonConstants.TEXT);
    }

    private void initUI() {
        webview = (WebView) findViewById(R.id.web_view);
    }

    private void setCallBack() {

    }

    private void setValue() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webview.getSettings().setUserAgentString(newUA);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(CheckoutPaymentActivity.this, "Requesting", "Loading...");

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton(0, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });

        url += "&checkouttoken=19d0ceaca45f9ee27e3c51df52786f1d904280f9";
        webview.loadUrl(url);
    }
}
