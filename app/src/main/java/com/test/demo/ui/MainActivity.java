package com.test.demo.ui;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.demo.R;
import com.test.demo.bean.HotelBean;
import com.test.demo.constants.Constants;
import com.test.demo.constants.HotelColumns;
import com.test.demo.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    String hotelName;
    String hotelCity;
    String hotelAddress;
    String checkInDate;
    String checkOutDate;
    String roomType;
    String roomPrice;
    String hotelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final EditText hotelCityEt = (EditText) findViewById(R.id.hotel_city);
        final EditText roomPriceEt = (EditText) findViewById(R.id.room_price);
        final EditText hotelIdEt = (EditText) findViewById(R.id.hotel_id);

        initCheckInPicker();
        initCheckOutPicker();
        initRoomType();
        initHotelName();
        initHotelAddress();

        Button insertBt = (Button) findViewById(R.id.insert);
        Button updateBt = (Button) findViewById(R.id.update);
        Button deleteBt = (Button) findViewById(R.id.delete);
        Button queryBt = (Button) findViewById(R.id.query);
        Button queryAllBt = (Button) findViewById(R.id.query_all);

        insertBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "insertBt OnClickListener");
                if (TextUtils.isEmpty(hotelName)) {
                    Toast.makeText(MainActivity.this, "hotel name cant be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                hotelCity = hotelCityEt.getText().toString();
                roomPrice = roomPriceEt.getText().toString();

                ContentValues values = new ContentValues();
                values.put(HotelColumns.HOTEL_NAME, hotelName);
                values.put(HotelColumns.HOTEL_CITY, hotelCity);
                values.put(HotelColumns.HOTEL_ADDRESS, hotelAddress);
                values.put(HotelColumns.CHECK_IN_DATE, checkInDate);
                values.put(HotelColumns.CHECK_OUT_DATE, checkOutDate);
                values.put(HotelColumns.ROOM_TYPE, roomType);
                values.put(HotelColumns.ROOM_PRICE, roomPrice);
                ContentResolver cr = getContentResolver();
                cr.insert(HotelColumns.CONTENT_URI, values);
            }
        });

        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "updateBt OnClickListener");
                hotelId = hotelIdEt.getText().toString();
                if (TextUtils.isEmpty(hotelId)) {
                    Toast.makeText(MainActivity.this, "hotel id cant be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                if (!TextUtils.isEmpty(hotelName)) {
                    values.put(HotelColumns.HOTEL_NAME, hotelName);
                }
                hotelCity = hotelCityEt.getText().toString();
                if (!TextUtils.isEmpty(hotelCity)) {
                    values.put(HotelColumns.HOTEL_CITY, hotelCity);
                }
                if (!TextUtils.isEmpty(hotelAddress)) {
                    values.put(HotelColumns.HOTEL_ADDRESS, hotelAddress);
                }
                if (!TextUtils.isEmpty(checkInDate)) {
                    values.put(HotelColumns.CHECK_IN_DATE, checkInDate);
                }
                if (!TextUtils.isEmpty(checkOutDate)) {
                    values.put(HotelColumns.CHECK_OUT_DATE, checkOutDate);
                }
                if (!TextUtils.isEmpty(roomType)) {
                    values.put(HotelColumns.ROOM_TYPE, roomType);
                }
                roomPrice = roomPriceEt.getText().toString();
                if (!TextUtils.isEmpty(roomPrice)) {
                    values.put(HotelColumns.ROOM_PRICE, roomPrice);
                }
                int tipType = Utils.stringToInt(((EditText) findViewById(R.id.tip_type)).getText().toString());
                if (-1 != tipType) {
                    values.put(HotelColumns.TIP_TYPE, tipType);
                }
                ContentResolver cr = getContentResolver();
                cr.update(HotelColumns.CONTENT_URI, values, HotelColumns._ID + " = ?", new String[]{hotelId});
            }
        });

        deleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "deleteBt OnClickListener");
                hotelId = hotelIdEt.getText().toString();
                if (TextUtils.isEmpty(hotelId)) {
                    Toast.makeText(MainActivity.this, "hotel id cant be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentResolver cr = getContentResolver();
                cr.delete(HotelColumns.CONTENT_URI, HotelColumns._ID + " = ?", new String[]{hotelId});
            }
        });

        final TextView idsTv = (TextView) findViewById(R.id.ids);
        queryBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "queryBt OnClickListener");
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(HotelColumns.CONTENT_VALID_URI, null, null, null, null);
                StringBuilder sb = new StringBuilder();
                while (null != cursor && cursor.moveToNext()) {
                    sb.append(new HotelBean(cursor).toString()).append("\n");
                }
                idsTv.setText(sb);
            }
        });

        queryAllBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "queryAllBt OnClickListener");
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(HotelColumns.CONTENT_URI, null, null, null, null);
                StringBuilder sb = new StringBuilder();
                while (null != cursor && cursor.moveToNext()) {
                    sb.append(new HotelBean(cursor).toString()).append("\n");
                }
                idsTv.setText(sb);
            }
        });
    }

    private void initHotelAddress() {
        final Spinner addressSpinner = (Spinner) findViewById(R.id.hotel_address);
        final String[] addressArray = getResources().getStringArray(R.array.address);
        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hotelAddress = addressArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initHotelName() {
        final Spinner hotelNameSinner = (Spinner) findViewById(R.id.hotel_name);
        final String[] nameArray = getResources().getStringArray(R.array.hotel_name);
        hotelNameSinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hotelName = nameArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRoomType() {
        final Spinner roomTypeSpinner = (Spinner) findViewById(R.id.room_type);
        final String[] typeArray = getResources().getStringArray(R.array.room_type);
        roomTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roomType = typeArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCheckOutPicker() {
        Log.e(Constants.DEBUG_TAG, "initCheckOutPicker");
        final Calendar calendar = Calendar.getInstance();
        final TextView checkOutTimeTv = (TextView) findViewById(R.id.check_out_time);
        final DatePickerDialog.OnDateSetListener outListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                checkOutDate = sdf.format(calendar.getTime());
                checkOutTimeTv.setText(checkOutDate);
            }
        };
        checkOutTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "checkOutTimeTv.setOnClickListener");
                DatePickerDialog pickerDialog = new DatePickerDialog(MainActivity.this, outListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        });
    }

    private void initCheckInPicker() {
        Log.e(Constants.DEBUG_TAG, "initCheckInPicker");
        final TextView checkInTimeTv = (TextView) findViewById(R.id.check_in_time);
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener inListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                checkInDate = sdf.format(calendar.getTime());
                checkInTimeTv.setText(checkInDate);
            }
        };
        checkInTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(Constants.DEBUG_TAG, "checkInTimeTv.setOnClickListener");
                DatePickerDialog pickerDialog = new DatePickerDialog(MainActivity.this, inListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        });
    }
}
