package com.makokit.mako;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.io.File;

public class ChatActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chatView);
        linearLayout.setGravity(Gravity.BOTTOM);

        SQLiteDatabase chatDatabase = openOrCreateDatabase("mako",MODE_PRIVATE,null);
        chatDatabase.execSQL("DROP TABLE Chat");
        chatDatabase.execSQL("CREATE TABLE Chat(Chat VARCHAR);");
        String[] textArray = {"One", "Two", "Three", "Four"};
        for( int i = 0; i < textArray.length; i++ )
        {
            chatDatabase.execSQL("INSERT INTO Chat VALUES('" + textArray[i] + "');");
        }

        Cursor resultSet = chatDatabase.rawQuery("Select * from Chat", null);
        resultSet.moveToFirst();

        String text = resultSet.getString(0);
        Boolean left = true;
        createTextView(linearLayout, text, left);

        while (resultSet.moveToNext()) {
            text = resultSet.getString(0);
            left = !left;
            createTextView(linearLayout, text, left);
        }

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollChat);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtSend = (EditText) findViewById(R.id.txtSend);
                sendText(txtSend.getText().toString());
                txtSend.setText("");
                final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollChat);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }

            private void sendText(String txtSend) {
                SQLiteDatabase chatDatabase = openOrCreateDatabase("mako",MODE_PRIVATE,null);
                chatDatabase.execSQL("INSERT INTO Chat VALUES('" + txtSend + "');");

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chatView);
                linearLayout.removeAllViews();

                Cursor resultSet = chatDatabase.rawQuery("Select * from Chat", null);
                resultSet.moveToFirst();

                String text = resultSet.getString(0);
                Boolean left = true;
                createTextView(linearLayout, text, left);

                while (resultSet.moveToNext()) {
                    text = resultSet.getString(0);
                    left = !left;
                    createTextView(linearLayout, text, left);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    private void createTextView(LinearLayout ll, String text, Boolean left) {
        TextView textView = new TextView(this);
        textView.setText(text);
        if (left) {
            textView.setBackgroundResource(R.drawable.bubble);
            textView.setPadding(100, 0, 0, 0);
        }
        else {
            textView.setBackgroundResource(R.drawable.bubble2);
            textView.setPadding(0, 0, 100, 0);
            textView.setGravity(Gravity.RIGHT);
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        textView.setHeight(200);
        ll.addView(textView);

        Space space = new Space(this);
        space.setMinimumHeight(50);
        ll.addView(space);
    }
}
