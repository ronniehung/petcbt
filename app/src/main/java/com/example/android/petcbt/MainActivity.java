package com.example.android.petcbt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {

    private ImageView catView;
//    private EditText describeFeeling;
    private ArrayList<String> allWords;
    private HashMap<String, String> statementsToRemarks;
    private HashMap<String, String> statementsToSuggestions;
    private HashMap<String, String> statementsToQualities;

//    final private String[] sampleListForAutoComplete = {
//            "I feel bad about myself",
//            "I'm enjoying life"
//    };


    TextView feelingInputText, remarkText, suggestionText;
    AutoCompleteTextView feelingInput;
    ExpandedListView emotionList;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        catView = (ImageView) findViewById(R.id.cat);

        allWords = new ArrayList<>();
        statementsToRemarks = new HashMap<>();
        statementsToSuggestions = new HashMap<>();
        statementsToQualities = new HashMap<>();
//        describeFeeling = (EditText) findViewById(R.id.describe_feeling);

        populateEmotionStringList();
        populateEmotionListView();

        // ignore all this for now

        feelingInputText = (TextView) findViewById(R.id.feeling_input_label);
        feelingInput = (AutoCompleteTextView) findViewById(R.id.feeling_input);
        emotionList = (ExpandedListView) findViewById(R.id.emotion_list);
        remarkText = (TextView) findViewById(R.id.remark);
        suggestionText = (TextView) findViewById(R.id.suggestion);
        backButton = (Button) findViewById(R.id.backBtn);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.emotion_list_autocomplete_item, allWords);
        feelingInput.setAdapter(adapter);


    }

    public void hideFeelingInputs() {
        feelingInputText.setVisibility(View.GONE);
        feelingInput.setVisibility(View.GONE);
        emotionList.setVisibility(View.GONE);

        remarkText.setVisibility(View.VISIBLE);
        suggestionText.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
    }

    public void showFeelingInputs(View v) {
        feelingInputText.setVisibility(View.VISIBLE);
        feelingInput.setVisibility(View.VISIBLE);
        emotionList.setVisibility(View.VISIBLE);

        remarkText.setVisibility(View.GONE);
        suggestionText.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
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

            for (int i = 0; i < strings.length(); i++) {
                String statement = strings.getJSONObject(i).getString("statement");
                String remark = strings.getJSONObject(i).getString("remark");
                String suggestion = strings.getJSONObject(i).getString("suggestion");
                String quality = strings.getJSONObject(i).getString("quality");

                allWords.add(statement);
                statementsToRemarks.put(statement, remark);
                statementsToSuggestions.put(statement, suggestion);
                statementsToQualities.put(statement, quality);
            }

        } catch (org.json.JSONException e) {
            e.printStackTrace();
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
                String feeling = allWords.get(i);
                String quality = statementsToQualities.get(feeling);
                String remark = statementsToRemarks.get(feeling);
                String suggestion = statementsToSuggestions.get(feeling);
                //String response = Cat.getResponseFromFeeling(feeling);
                //String response = "Test";
                //TextView welcomeMessage = (TextView) findViewById(R.id.welcome_message);
                //welcomeMessage.setText(response);

                remarkText.setText(remark);
                suggestionText.setText(suggestion);

                hideFeelingInputs();
                setCatEmotion(quality);
            }
        });
    }

    // read the text, if the string is in Negative or Positive, change the cat's appearance
    void setCatEmotion(String quality) {
        if (quality.equals("positive")) {
            setCatEmotionToPositive();
        } else if (quality.equals("negative")) {
            setCatEmotionToNegative();
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

    void goBack(View view) {
        LinearLayout hideLayout = (LinearLayout) findViewById(R.id.reply_container);
        RelativeLayout showLayout = (RelativeLayout) findViewById(R.id.welcome_message_container);
        hideLayout.setVisibility(View.GONE);
        showLayout.setVisibility(View.VISIBLE);
    }
}
