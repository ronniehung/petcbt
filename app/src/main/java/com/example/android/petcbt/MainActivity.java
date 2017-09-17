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

public class MainActivity extends AppCompatActivity {

    private ImageView catView;
//    private EditText describeFeeling;
    final private String[] negativeWords = {"miserable", "worthless"};
    final private String[] positiveWords = {"happy", "content"};
    final static private String[] allWords = {
            "miserable", "worthless", "happy", "content"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        catView = (ImageView) findViewById(R.id.cat);
//        describeFeeling = (EditText) findViewById(R.id.describe_feeling);

        populationEmotionList();

    }


    void populationEmotionList() {
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
