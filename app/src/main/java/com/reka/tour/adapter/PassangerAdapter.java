package com.reka.tour.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.activity.InfoPassangerActivity;
import com.reka.tour.model.Passanger;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class PassangerAdapter extends ArrayAdapter<Passanger> {

    private int layoutResourceId;
    private Context context;
    private Passanger passanger;
//    private ViewHolder holder;

    public PassangerAdapter(Context context, ArrayList<Passanger> passangers) {
        super(context, R.layout.item_passanger, passangers);
        this.context = context;
        this.layoutResourceId = R.layout.item_passanger;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
      passanger = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titlePassanger.setText(passanger.getType());
        holder.evName.setText(passanger.getName());

        setEvTitel(holder);

        if (position == 0) {
            holder.checkboxDuplicate.setVisibility(View.VISIBLE);
            setCheckBox(holder);
        }

        return convertView;
    }

    private void setEvTitel(final ViewHolder holder) {
        final String[] itemsData = {"Pilih Titel","Tuan","Nyonya","Nona"};
        holder.evTitel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(itemsData, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            holder.evTitel.setText(itemsData[item]);
                        }
                    }).create().show();
                }
                return true;
            }
        });
    }

    private void setCheckBox(final ViewHolder holder) {
        holder.checkboxDuplicate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Passanger passanger1 = ((InfoPassangerActivity) getContext()).getProfile();
                    holder.evTitel.setText(passanger1.getTitel());
                    holder.evName.setText(passanger1.getName());

                    holder.evTitel.setEnabled(false);
                    holder.evName.setEnabled(false);
                    holder.evNotelp.setEnabled(false);
                } else {
                    holder.evName.setText(passanger.getName());

                    holder.evTitel.setEnabled(true);
                    holder.evName.setEnabled(true);
                    holder.evNotelp.setEnabled(true);
                }
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.ev_titel)
        EditText evTitel;
        @Bind(R.id.ev_name)
        EditText evName;
        @Bind(R.id.ev_notelp)
        EditText evNotelp;
        @Bind(R.id.ev_tanggal_lahir)
        EditText evTanggaLahir;

        @Bind(R.id.title_passanger)
        TextView titlePassanger;
        @Bind(R.id.checkboxDuplicate)
        CheckBox checkboxDuplicate;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}