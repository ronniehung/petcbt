package com.example.android.petcbt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView catView;
//    private EditText describeFeeling;
    final private String[] negativeWords = {"miserable", "worthless"};
    final private String[] positiveWords = {"happy", "content"};
    private String[] allWords;

    final private String[] sampleListForAutoComplete = {
            "I feel bad about myself",
            "I'm enjoying life"
    };


    TextView feelingInputLabel, remarkText, suggestionText;
    AutoCompleteTextView feelingInput;
    ExpandedListView emotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        catView = (ImageView) findViewById(R.id.cat);
//        describeFeeling = (EditText) findViewById(R.id.describe_feeling);

        populateEmotionStringList();
        populateEmotionListView();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.emotion_list_autocomplete_item, sampleListForAutoComplete);

        AutoCompleteTextView autoCompleteView =
                (AutoCompleteTextView ) findViewById(R.id.feeling_input);
        autoCompleteView.setAdapter(adapter);

        // ignore all this for now
        TextView remark = (TextView) findViewById(R.id.remark);
        remark.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        TextView suggestion = (TextView) findViewById(R.id.suggestion);
        suggestion.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        feelingInputLabel = (TextView) findViewById(R.id.feeling_input_label);
        feelingInput = (AutoCompleteTextView) findViewById(R.id.feeling_input);
        emotionList = (ExpandedListView) findViewById(R.id.emotion_list);
        remarkText = (TextView) findViewById(R.id.remark);
        suggestionText = (TextView) findViewById(R.id.suggestion);

        hideFeelingInputs();
        showFeelingInputs();
    }

    void hideFeelingInputs() {
        feelingInputLabel.setVisibility(View.GONE);
        feelingInput.setVisibility(View.GONE);
        emotionList.setVisibility(View.GONE);

        remarkText.setVisibility(View.VISIBLE);
        suggestionText.setVisibility(View.VISIBLE);
    }

    void showFeelingInputs() {
        feelingInputLabel.setVisibility(View.VISIBLE);
        feelingInput.setVisibility(View.VISIBLE);
        emotionList.setVisibility(View.VISIBLE);

        remarkText.setVisibility(View.GONE);
        suggestionText.setVisibility(View.GONE);
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

    void goBack(View view) {
        LinearLayout hideLayout = (LinearLayout) findViewById(R.id.reply_container);
        RelativeLayout showLayout = (RelativeLayout) findViewById(R.id.welcome_message_container);
        hideLayout.setVisibility(View.GONE);
        showLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toDoBtn) {
            Log.d("####", "Ronnie is so cool right?");
            startActivity(new Intent(this, ToDoListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
