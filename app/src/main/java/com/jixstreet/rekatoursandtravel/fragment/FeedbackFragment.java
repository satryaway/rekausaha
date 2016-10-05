package com.jixstreet.rekatoursandtravel.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jixstreet.rekatoursandtravel.R;
import com.jixstreet.rekatoursandtravel.utils.GmailSender;


public class FeedbackFragment extends Fragment {

    private EditText kritikSaranET;
    private EditText emailET;
    private Button sendBtn;
    private View view;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback, container, false);

        initUI();
        setCallBack();

        return view;
    }

    private void initUI() {
        emailET = (EditText) view.findViewById(R.id.email_et);
        kritikSaranET = (EditText) view.findViewById(R.id.kritik_saran_et);
        sendBtn = (Button) view.findViewById(R.id.send_btn);
    }

    private void setCallBack() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GmailSender sender = new GmailSender("reka.toursntravel02@gmail.com", "Rekaproject02");
                    sender.sendMail("Hi, this is subject",
                            emailET.getText().toString() + " - " +
                            kritikSaranET.getText().toString(),
                            "reka.toursntravel02@gmail.com",
                            "info@rekatoursntravel.com");

                    Toast.makeText(getActivity(), "Feedback sent", Toast.LENGTH_SHORT).show();
                    emailET.setText("");
                    kritikSaranET.setText("");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        });
    }
}