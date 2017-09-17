package com.example.android.petcbt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView catView;
//    private EditText describeFeeling;
    private String[] negativeWords = {"miserable", "worthless"};
    private String[] positiveWords = {"happy", "content"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catView = (ImageView) findViewById(R.id.cat);
//        describeFeeling = (EditText) findViewById(R.id.describe_feeling);
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
        // change image to positive image
    }

    void setCatEmotionToNegative() {
        // change image to negative image
    }


}
