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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ImageView catView;
    private ArrayList<String> allWords;
    private HashMap<String, String> statementsToRemarks;
    private HashMap<String, String> statementsToSuggestions;
    private HashMap<String, String> statementsToQualities;

    TextView feelingInputText, remarkText, suggestionText;
    AutoCompleteTextView feelingInput;
    Button backButton;
    LinearLayout replyContainer;
    RelativeLayout welcomeMessageContainer;

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

        populateEmotionStringList();

        feelingInputText = (TextView) findViewById(R.id.feeling_input_label);
        feelingInput = (AutoCompleteTextView) findViewById(R.id.feeling_input);
        remarkText = (TextView) findViewById(R.id.remark);
        suggestionText = (TextView) findViewById(R.id.suggestion);
        backButton = (Button) findViewById(R.id.backBtn);
        replyContainer = (LinearLayout) findViewById(R.id.reply_container);
        welcomeMessageContainer = (RelativeLayout) findViewById(R.id.welcome_message_container);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.emotion_list_autocomplete_item, allWords);
        feelingInput.setAdapter(adapter);

        feelingInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String feeling = allWords.get(i);
                String quality = statementsToQualities.get(feeling);
                String remark = statementsToRemarks.get(feeling);
                String suggestion = statementsToSuggestions.get(feeling);

                remarkText.setText(remark);
                suggestionText.setText(suggestion);

                hideFeelingInputs();
                setCatEmotion(quality);
            }
        });

    }

    public void hideFeelingInputs() {
        feelingInputText.setVisibility(View.GONE);
        feelingInput.setVisibility(View.GONE);
        welcomeMessageContainer.setVisibility(View.GONE);

        replyContainer.setVisibility(View.VISIBLE);
    }

    public void showFeelingInputs(View v) {
        feelingInputText.setVisibility(View.VISIBLE);
        feelingInput.setVisibility(View.VISIBLE);
        welcomeMessageContainer.setVisibility(View.VISIBLE);

        replyContainer.setVisibility(View.GONE);

    }

    public String[] toStringArray(JSONArray array) {
        if(array == null)
            return null;

        String[] arr = new String[array.length()];
        for(int i=0; i < arr.length; i++) {
            arr[i] = array.optString(i);
        }
        return arr;
    }

    void populateEmotionStringList() {
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
