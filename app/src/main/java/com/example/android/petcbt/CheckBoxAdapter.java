package com.example.android.petcbt;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;


public class CheckBoxAdapter extends ArrayAdapter {
        Context context;
        List<String> checkboxItems;

        public CheckBoxAdapter(Context context, ArrayList<String> resource) {
            super(context, R.layout.item_todo, resource);

            this.context = context;
            this.checkboxItems = resource;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_todo, parent, false);
//        TextView textView = (TextView) convertView.findViewById(R.id.textView1);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.toDoItem);

            cb.setText(checkboxItems.get(position));
            return convertView;
        }
    }
