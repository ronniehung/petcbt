package com.example.android.petcbt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView catView;
//    private EditText describeFeeling;
    final private String[] negativeWords = {"miserable", "worthless"};
    final private String[] positiveWords = {"happy", "content"};
    private String[] allWords;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        catView = (ImageView) findViewById(R.id.cat);
//        describeFeeling = (EditText) findViewById(R.id.describe_feeling);

        populateEmotionStringList();
        populateEmotionListView();

    }

    public String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }

    void populateEmotionStringList() {
        // read json
        String json = null;
        try {
            InputStream is = getAssets().open("keys.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray strings = new JSONArray(json);
            allWords = toStringArray(strings);



        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        if (json != null) {
            // convert string into JSONArray[]

        }
    }


    void populateEmotionListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.emotion_list_item, allWords);

        ListView listView = (ListView) findViewById(R.id.emotion_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String feeling = allWords[i];
                String response = Cat.getResponseFromFeeling(feeling);
                TextView welcomeMessage = (TextView) findViewById(R.id.welcome_message);
                welcomeMessage.setText(response);
            }
        });
    }

    // read the text, if the string is in Negative or Positive, change the cat's appearance
    void evaluateFeeling(String text) {
        for (String negativeWord : negativeWords) {
            if (text == negativeWord) {
                setCatEmotionToNegative();
                return;
            }
        }

        for (String positiveWord : positiveWords) {
            if (text == positiveWord) {
                setCatEmotionToPositive();
                return;
            }
        }
    }

    void setCatEmotionToPositive() {
        catView = (ImageView) findViewById(R.id.cat);
        catView.setImageResource(R.drawable.happy_cat);
    }

    void setCatEmotionToNegative() {
        catView = (ImageView) findViewById(R.id.cat);
        catView.setImageResource(R.drawable.sad_cat);
    }




}
