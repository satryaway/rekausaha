package com.jixstreet.rekatoursandtravel.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jixstreet.rekatoursandtravel.R;


public class AboutFragment extends Fragment {

    private LinearLayout hyperlinkWrapper;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        hyperlinkWrapper = (LinearLayout) view.findViewById(R.id.hyperlink_wrapper);
        View webView = hyperlinkWrapper.getChildAt(0);
        webView.setOnClickListener(onWebClicked);
        View fbView = hyperlinkWrapper.getChildAt(1);
        fbView.setOnClickListener(onFbClicked);
        View emailView = hyperlinkWrapper.getChildAt(2);
        emailView.setOnClickListener(onEmailClicked);
        return view;
    }

    View.OnClickListener onWebClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rekatoursntravel.com"));
            startActivity(browserIntent);
        }
    };

    View.OnClickListener onFbClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/rekatours"));
            startActivity(browserIntent);
        }
    };

    View.OnClickListener onEmailClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@rekatoursntravel.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT   , "");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    };
}