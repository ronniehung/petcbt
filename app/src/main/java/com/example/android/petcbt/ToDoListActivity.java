package com.example.android.petcbt;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    private ArrayList<String> todos;
    private ArrayAdapter<String> adapter;
    private int points;
    private TextView scoreboard;
    private final static int TODO_POINT_VALUE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        todos = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,
                R.layout.todo_list_item, todos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                todos.remove(i);
                adapter.notifyDataSetChanged();
                points += TODO_POINT_VALUE;
                updateScoreboard();

            }
        });

        points = 0;
        scoreboard = (TextView) findViewById(R.id.scoreboard);
        updateScoreboard();
    }

    void updateScoreboard() {
        scoreboard.setText(Integer.valueOf(points).toString());
    }




}
