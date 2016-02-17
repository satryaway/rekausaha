package com.reka.tour.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.reka.tour.R;
import com.reka.tour.fragment.AboutFragment;
import com.reka.tour.fragment.FeedbackFragment;
import com.reka.tour.fragment.HomeFragment;
import com.reka.tour.fragment.NewsFragment;
import com.reka.tour.model.Steps;
import com.reka.tour.utils.CommonConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar_icon)
    ImageView toolbarIcon;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = HomeFragment.newInstance(this);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }

        setTest();

    }

    private void setTest() {
        String example =
                "    [\n" +
                        "        \"Pilih Bahasa\",\n" +
                        "        \"Masukkan PIN\",\n" +
                        "        \"Pilih 'Transaksi Lainnya'\",\n" +
                        "        \"Pilih 'Pembayaran'\",\n" +
                        "        \"Pilih 'Lain Lain'\",\n" +
                        "        \"Pilih 'Pembayaran Virtual Account'\",\n" +
                        "        \"Masukkan 16 Nomor Virtual Account (8734 0000  22 361 651)\",\n" +
                        "        \"Muncul Layar Konfirmasi yang berisi Nomor Virtual Account dan Order Id, bila sudah benar pilih 'Benar'.\",\n" +
                        "        \"Pilih Rekening Anda yang akan digunakan untuk pembayaran.\",\n" +
                        "        \"Selesai\"\n" +
                        "      ]\n";

        String example2 = "{\n" +
                "  \"diagnostic\": {\n" +
                "    \"status\": 200,\n" +
                "    \"elapsetime\": \"0.4884\",\n" +
                "    \"memoryusage\": \"8.36MB\",\n" +
                "    \"unix_timestamp\": 1455723126,\n" +
                "    \"confirm\": \"success\",\n" +
                "    \"lang\": \"id\",\n" +
                "    \"currency\": \"IDR\"\n" +
                "  },\n" +
                "  \"output_type\": \"json\",\n" +
                "  \"orderId\": \"22361673\",\n" +
                "  \"vanumber\": \"8734 0000  22 361 673\",\n" +
                "  \"result\": {\n" +
                "    \"payment_subsider_tiket\": 4000,\n" +
                "    \"payment_charge_subsider\": 0,\n" +
                "    \"currency_to_be_converted\": \"IDR\",\n" +
                "    \"from_another_currency\": false,\n" +
                "    \"reseller_id\": \"21258951\",\n" +
                "    \"payment_discount\": 7600,\n" +
                "    \"order_types\": [\n" +
                "      \"flight\"\n" +
                "    ],\n" +
                "    \"order_id\": \"22361673\",\n" +
                "    \"currency\": \"IDR\",\n" +
                "    \"payment_charge\": 0,\n" +
                "    \"giftPromo\": false,\n" +
                "    \"sub_total\": 427000,\n" +
                "    \"unique_code\": 0,\n" +
                "    \"grand_total\": 427000,\n" +
                "    \"grand_subtotal\": 434600,\n" +
                "    \"orders\": [\n" +
                "      {\n" +
                "        \"order_detail_id\": \"12687371\",\n" +
                "        \"order_name\": \"CGK (Jakarta - Cengkareng) - DPS (Denpasar, Bali)\",\n" +
                "        \"order_name_detail\": \"Lion (JT-10 - Depart)\",\n" +
                "        \"currency\": \"IDR\",\n" +
                "        \"price\": \"427000.00\",\n" +
                "        \"flight_date\": \"2016-02-18\",\n" +
                "        \"departure_city\": \"CGK\",\n" +
                "        \"departure_time\": \"2016-02-18 20:40:00\",\n" +
                "        \"arrival_city\": \"DPS\",\n" +
                "        \"arrival_time\": \"2016-02-18 23:30:00\",\n" +
                "        \"adult\": \"1\",\n" +
                "        \"child\": \"0\",\n" +
                "        \"infant\": \"0\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"confirm_page_mobile\": false,\n" +
                "    \"gaq\": \"\",\n" +
                "    \"payment_type\": 35,\n" +
                "    \"is_confirmation\": false,\n" +
                "    \"type\": \"\",\n" +
                "    \"checkout_url\": \"https://api-sandbox.tiket.com/checkout/checkout_payment/35\",\n" +
                "    \"arrOrderType\": [\n" +
                "      \"flight\"\n" +
                "    ],\n" +
                "    \"tiket_point\": 0,\n" +
                "    \"tiket_point_worth\": 0,\n" +
                "    \"tiket_point_notes\": \"\",\n" +
                "    \"tiket_point_status\": \"\",\n" +
                "    \"tiket_point_words\": \"\"\n" +
                "  },\n" +
                "  \"steps\": [\n" +
                "    {\n" +
                "      \"name\": \"Pembayaran melalui ATM BCA/Bank sejenis dalam Jaringan PRIMA\",\n" +
                "      \"step\": [\n" +
                "        \"Masukkan PIN\",\n" +
                "        \"Pilih 'Transaksi Lainnya'\",\n" +
                "        \"Pilih 'Transfer'\",\n" +
                "        \"Pilih 'Ke Rek Bank Lain'\",\n" +
                "        \"Masukkan kode sandi Bank Permata (013) kemudian tekan 'Benar'\",\n" +
                "        \"Masukkan Jumlah pembayaran sesuai dengan yang ditagihkan dalam tiket (Jumlah yang ditransfer harus sama persis dengan yang ada pada itinerary, tidak lebih dan tidak kurang).Penting: Jumlah nominal yang tidak sesuai dengan tagihan pada itinerary akan menyebabkan transaksi gagal.\",\n" +
                "        \"Masukkan 16 Nomor Virtual Account (8734 0000  22 361 673)\",\n" +
                "        \"Muncul Layar Konfirmasi Transfer yang berisi nomor rekening tujuan dan Order Id beserta jumlah yang dibayar, jika sudah benar, Tekan 'Benar'.\",\n" +
                "        \"Selesai\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Pembayaran melalui ATM Mandiri/Bank sejenis dalam Jaringan ATM Bersama\",\n" +
                "      \"step\": [\n" +
                "        \"Pilih Bahasa\",\n" +
                "        \"Masukkan PIN\",\n" +
                "        \"Pilih 'Transaksi Lainnya'\",\n" +
                "        \"Pilih 'Transfer'\",\n" +
                "        \"Pilih 'Ke Rekening Bank Lain ATM Bersama/Link'\",\n" +
                "        \"Masukkan kode bank (013) + 16 Nomor Virtual Account (8734 0000  22 361 673)\",\n" +
                "        \"Masukkan Jumlah pembayaran sesuai dengan yang ditagihkan dalam tiket (Jumlah yang ditransfer harus sama persis dengan yang ada pada itinerary, tidak lebih dan tidak kurang).Penting: Jumlah nominal yang tidak sesuai dengan tagihan pada itinerary akan menyebabkan transaksi gagal.\",\n" +
                "        \"Kosongkan nomor referensi transfer kemudian tekan 'Benar'.\",\n" +
                "        \"Muncul Layar Konfirmasi Transfer yang berisi nomor rekening tujuan dan Order Id beserta jumlah yang dibayar, jika sudah benar, Tekan 'Benar'.\",\n" +
                "        \"Selesai\"\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Pembayaran melalui ATM Bank Permata\",\n" +
                "      \"step\": [\n" +
                "        \"Pilih Bahasa\",\n" +
                "        \"Masukkan PIN\",\n" +
                "        \"Pilih 'Transaksi Lainnya'\",\n" +
                "        \"Pilih 'Pembayaran'\",\n" +
                "        \"Pilih 'Lain Lain'\",\n" +
                "        \"Pilih 'Pembayaran Virtual Account'\",\n" +
                "        \"Masukkan 16 Nomor Virtual Account (8734 0000  22 361 673)\",\n" +
                "        \"Muncul Layar Konfirmasi yang berisi Nomor Virtual Account dan Order Id, bila sudah benar pilih 'Benar'.\",\n" +
                "        \"Pilih Rekening Anda yang akan digunakan untuk pembayaran.\",\n" +
                "        \"Selesai\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"message\": \"Silakan transfer pembayaran untuk memesan. Anda memiliki waktu  13 menit 45 Detik  untuk melakukan pembayaran\",\n" +
                "  \"grand_total\": 427000,\n" +
                "  \"login_status\": \"true\",\n" +
                "  \"guest_id\": \"167023\",\n" +
                "  \"token\": \"19d0ceaca45f9ee27e3c51df52786f1d904280f9\"\n" +
                "}";


        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(example);

        List<String> arrays = Arrays.asList(example);
        Log.e("mJSONArray", arrays.size() + "");
        Log.e("mJSONArray", " " + jsonArray.get(0).toString());


        ArrayList<Steps> stepses = new ArrayList<>();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(example2);
            JSONArray stepsArray = jsonObject.getJSONArray(CommonConstants.STEPS);
            for (int i = 0; i < stepsArray.length(); i++) {
                stepses.add(gson.fromJson(stepsArray.getJSONObject(i).toString(), Steps.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("stepsArray", " " + stepses.get(0).step[0]);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_beranda) {
            toolbarIcon.setVisibility(View.VISIBLE);
            fragment = HomeFragment.newInstance(this);
        } else if (id == R.id.nav_kabar_berita) {
            toolbarIcon.setVisibility(View.VISIBLE);
            fragment = NewsFragment.newInstance();
        } else if (id == R.id.nav_kritik_saran) {
            toolbarIcon.setVisibility(View.VISIBLE);
            fragment = FeedbackFragment.newInstance();
        } else if (id == R.id.nav_tentang) {
            toolbarIcon.setVisibility(View.GONE);
            fragment = AboutFragment.newInstance();
        }


        if (fragment != null) {
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
