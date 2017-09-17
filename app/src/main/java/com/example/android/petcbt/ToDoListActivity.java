package com.example.android.petcbt;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ToDoListActivity extends AppCompatActivity {

    private ArrayList<String> todos;
    //private CheckBoxAdapter adapter;
    private ArrayAdapter adapter;
    private int score;
    public TextView scoreboard;
    private final static int TODO_POINT_VALUE = 100;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    @Override
    protected void onDestroy() {
        String todosToString = todos.toString();
        // convert todos to string
        mEditor.putString("tag", todosToString).commit();
        mEditor.putString("score", Integer.valueOf(score).toString()).commit();

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Shared Preferences
        mPrefs = getSharedPreferences("label", 0);
        mEditor = mPrefs.edit();

        // convert string to arraylist
        String mString = mPrefs.getString("tag", null);
        if (mString != null && mString.length() > 2) {
            String mStringWithoutBrackets = mString.substring(1, mString.length()-1);
            todos = new ArrayList<>(Arrays.asList(mStringWithoutBrackets.split(",")));
        } else {
            todos = new ArrayList<>();
        }

        // retrieve saved store
        String mScore = mPrefs.getString("score", "0");
        score = Integer.parseInt(mScore);

        // load strings
        SharedPreferences mPrefsTodo = getSharedPreferences("todos_to_be_added", 0);
        SharedPreferences.Editor mPrefsTodoEditor = mPrefsTodo.edit();
        // iterate over all
        Map<String, ?> keys = mPrefsTodo.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            todos.add(entry.getKey());
        }
        // delete all
        mPrefsTodoEditor.clear().commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //adapter = new CheckBoxAdapter(this, todos);
        adapter = new ArrayAdapter<>(this, R.layout.todo_list_item, todos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText taskEditText = new EditText(ToDoListActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(ToDoListActivity.this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                todos.add(task);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView todoListView = (ListView) findViewById(R.id.todo_list);
        todoListView.setDivider(null);
        todoListView.setDividerHeight(0);
        todoListView.setAdapter(adapter);
//        todoListView.setItemsCanFocus(false);


        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                todos.remove(i);
                adapter.notifyDataSetChanged();
                score += TODO_POINT_VALUE;
                updateScoreboard();

            }
        });



//        CheckBox checkedItem = (CheckBox) findViewById(R.id.toDoItem);
//        checkedItem.setOnClickListener(new AdapterView.OnClickListener() {
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////
////                todos.remove(i);
////                adapter.notifyDataSetChanged();
////                points += TODO_POINT_VALUE;
////                updateScoreboard();
////
////            }
//
//
//            @Override
//            public void onClick(View view) {
//                todos.remove(i);
//                adapter.notifyDataSetChanged();
//                points += TODO_POINT_VALUE;
//                updateScoreboard();
//            }
//        });


        scoreboard = (TextView) findViewById(R.id.scoreboard);
        updateScoreboard();

    }

    void updateScoreboard() {
        scoreboard.setText("Score: " + Integer.valueOf(score).toString());
    }
}
