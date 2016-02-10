package com.reka.tour.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.reka.tour.R;
import com.reka.tour.activity.InfoPassangerActivity;
import com.reka.tour.model.Passanger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by fachrifebrian on 9/2/15.
 */
public class PassangerAdapter extends ArrayAdapter<Passanger> {

    private int layoutResourceId;
    private Context context;
    private ArrayList<Passanger> passangers;
    private Passanger passanger;
    private String[] itemsData;
    private ViewHolder holder;
    private SimpleDateFormat dateDayFormatter;
    private Calendar newCalendar;

    public PassangerAdapter(Context context, ArrayList<Passanger> passangers, String[] itemsData) {
        super(context, R.layout.item_passanger, passangers);
        this.context = context;
        this.layoutResourceId = R.layout.item_passanger;
        this.itemsData = itemsData;
        this.passangers = passangers;
    }

    @Override
    public Passanger getItem(int position) {
        return passangers.get(position);
    }

    @Override
    public int getCount() {
        return passangers.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        passanger = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

//        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titlePassanger.setText(passanger.getType());

        setEvTitel(holder);
        setDate(holder);

        if (position == 0) {
            holder.checkboxDuplicate.setVisibility(View.VISIBLE);
            setCheckBox(holder);
        }

        if (!passanger.getType().toLowerCase().contains("dewasa")) {
            holder.evTanggaLahir.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void setEvTitel(final ViewHolder holder) {
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
                    holder.evFirstName.setText(passanger1.getFirstName());
                    holder.evLastName.setText(passanger1.getLastName());

                    holder.evTitel.setEnabled(false);
                    holder.evFirstName.setEnabled(false);
                    holder.evLastName.setEnabled(false);
                } else {
                    holder.evFirstName.setText(passanger.getFirstName());
                    holder.evLastName.setText(passanger.getLastName());

                    holder.evTitel.setEnabled(true);
                    holder.evFirstName.setEnabled(true);
                    holder.evLastName.setEnabled(true);
                }
            }
        });
    }

    private void setDate(final ViewHolder holder) {
        newCalendar = Calendar.getInstance();
        dateDayFormatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("ind", "IDN"));

        holder.evTanggaLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        holder.evTanggaLahir.setText(dateDayFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    static class ViewHolder {
        @Bind(R.id.ev_titel)
        EditText evTitel;
        @Bind(R.id.ev_first_name)
        EditText evFirstName;
        @Bind(R.id.ev_last_name)
        EditText evLastName;
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