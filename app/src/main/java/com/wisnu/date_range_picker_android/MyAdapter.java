package com.wisnu.date_range_picker_android;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    String array[] = new String[] { "tushar", "pandey", "almora", "hello",
            "tushar", "pandey", "almora", "hello", "tushar", "pandey",
            "almora", "hello" };

    Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }



    public int getCount() {
        // TODO Auto-generated method stub
        return array.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView text = new TextView(context);
        text.setHeight(60);
        text.setPadding(10, 8, 0, 0);
        text.setTextColor(Color.BLACK);
        text.setText(array[position]);
        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("clicked", "tushar");
            }
        });
        return text;
    }
}
