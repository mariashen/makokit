package com.makokit.mako;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import com.makokit.mako.MakoContract.*;

public class LessonActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);


        LinearLayout lessonList = (LinearLayout) findViewById(R.id.layout_lessons);
        final MakoDatabase makoDb = new MakoDatabase(this);
        final SQLiteDatabase db = makoDb.getReadableDatabase();

        Button btnRefreshDb = (Button) findViewById(R.id.btnRefreshDb);
        btnRefreshDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makoDb.refreshDatabase(db);
                new AsyncTaskGetLessons().execute(getApplicationContext());
            }
        });

        String[] projection = {
                LessonsEntry.COLUMN_NAME_NAME,
                LessonsEntry.COLUMN_NAME_LESSON_ID};

        final Cursor lessonsCursor = db.query(LessonsEntry.TABLE_NAME, projection, null, null, null, null, null);

        lessonsCursor.moveToFirst();

        String lessonName = lessonsCursor.getString(lessonsCursor.getColumnIndexOrThrow(LessonsEntry.COLUMN_NAME_NAME));
        Button newLessonButton = new Button(this);
        newLessonButton.setText(lessonName);
        newLessonButton.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        newLessonButton.setTextColor(Color.WHITE);
        newLessonButton.setTextSize(25);
        newLessonButton.setBackgroundResource(R.drawable.cook);
        newLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(LessonActivity.this, ChatActivity.class);
                startActivity(k);
            }
        });
        lessonList.addView(newLessonButton);
        int count = 0;

        while (lessonsCursor.moveToNext()) {
            lessonName = lessonsCursor.getString(lessonsCursor.getColumnIndexOrThrow(LessonsEntry.COLUMN_NAME_NAME));
            newLessonButton = new Button(this);
            newLessonButton.setText(lessonName);
            newLessonButton.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
            newLessonButton.setTextColor(Color.WHITE);
            newLessonButton.setTextSize(25);
            if (count++ == 0) newLessonButton.setBackgroundResource(R.drawable.wheelbarrow);
            else newLessonButton.setBackgroundResource(R.drawable.camera);
            newLessonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent k = new Intent(LessonActivity.this, ChatActivity.class);
                    startActivity(k);
                }
            });
            lessonList.addView(newLessonButton);
        }
    }


    public class AsyncTaskGetInstructions extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int lessonID = params[0];

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://makokit.herokuapp.com/api/lessons/" + Integer.toString(lessonID));

            try {
                HttpResponse response = httpclient.execute(httpGet);
                String responseString = EntityUtils.toString(response.getEntity());
                Log.d("respLes", responseString);

                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject instructionObjects = jsonObject.getJSONObject("instructions");
                JSONObject answerObjects = jsonObject.getJSONObject("answers");
                JSONObject jumpObjects = jsonObject.getJSONObject("jumps");

                MakoDatabase makoDb = new MakoDatabase(getApplicationContext());
                SQLiteDatabase db = makoDb.getReadableDatabase();

                if (instructionObjects != null && instructionObjects.names() != null) {
                    for (int i = 0; i < instructionObjects.names().length(); i++) {
                        JSONObject instruction = instructionObjects.getJSONObject(instructionObjects.names().getString(i));
                        ContentValues values = new ContentValues();
                        values.put(InstructionsEntry.COLUMN_NAME_INSTRUCTION_ID, instruction.optInt("id", -1));
                        values.put(InstructionsEntry.COLUMN_NAME_LESSON_ID, instruction.optInt("lesson_id", -1));
                        values.put(InstructionsEntry.COLUMN_NAME_TEXT, instruction.optString("text", ""));
                        values.put(InstructionsEntry.COLUMN_NAME_IMAGE_URL, instruction.optString("image_url", ""));
                        values.put(InstructionsEntry.COLUMN_NAME_VIDEO_URL, instruction.optString("video_url", ""));
                        values.put(InstructionsEntry.COLUMN_NAME_NEXT_INSTRUCTION_ID, instruction.optInt("next_instruction_id", -1));

                        // Insert the new row, returning the primary key value of the new row
                        Log.d("respInst", instruction.getString("text"));
                        db.insert(InstructionsEntry.TABLE_NAME, null, values);
                    }
                }

                if (answerObjects != null && answerObjects.names() != null) {
                    for (int i = 0; i < answerObjects.names().length(); i++) {
                        JSONObject answer = answerObjects.getJSONObject(answerObjects.names().getString(i));
                        ContentValues values = new ContentValues();
                        values.put(AnswersEntry.COLUMN_NAME_ANSWER_ID, answer.optInt("id", -1));
                        values.put(AnswersEntry.COLUMN_NAME_INSTRUCTION_ID, answer.optInt("instruction_id", -1));
                        values.put(AnswersEntry.COLUMN_NAME_TEXT, answer.optString("text", ""));
                        values.put(AnswersEntry.COLUMN_NAME_IMAGE_URL, answer.optString("image_url", ""));
                        if (answer.optBoolean("correct", false))
                            values.put(AnswersEntry.COLUMN_NAME_CORRECT, 1);
                        else
                            values.put(AnswersEntry.COLUMN_NAME_CORRECT, 0);

                        // Insert the new row, returning the primary key value of the new row
                        Log.d("respInst", answer.getString("text"));
                        db.insert(AnswersEntry.TABLE_NAME, null, values);
                    }
                }

                if (jumpObjects != null && jumpObjects.names() != null) {
                    for (int i = 0; i < jumpObjects.names().length(); i++) {
                        JSONObject jump = jumpObjects.getJSONObject(jumpObjects.names().getString(i));
                        ContentValues values = new ContentValues();
                        values.put(JumpsEntry.COLUMN_NAME_JUMP_ID, jump.optInt("id", -1));
                        values.put(JumpsEntry.COLUMN_NAME_ANSWER_ID, jump.optInt("answer_id", -1));
                        values.put(JumpsEntry.COLUMN_NAME_INSTRUCTION_ID, jump.optInt("instruction_id", -1));

                        // Insert the new row, returning the primary key value of the new row
                        Log.d("respInst", jump.getString("created_at"));
                        db.insert(JumpsEntry.TABLE_NAME, null, values);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public class AsyncTaskGetLessons extends AsyncTask<Context, String, Integer[]> {


        @Override
        protected void onPreExecute() {}

        @Override
        protected Integer[] doInBackground(Context... params) {
//            String result = "";

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://makokit.herokuapp.com/api/lessons");
            Integer[] returnee = {0};

            try {
                HttpResponse response = httpclient.execute(httpGet);
                String responseString = EntityUtils.toString(response.getEntity());
                Log.d("respLes", responseString);

                JSONArray jsonArray = new JSONArray(responseString);

                MakoDatabase makoDb = new MakoDatabase(getApplicationContext());
                SQLiteDatabase db = makoDb.getReadableDatabase();

                returnee = new Integer[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject currentLesson = jsonArray.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(LessonsEntry.COLUMN_NAME_LESSON_ID, currentLesson.optInt("id", -1));
                    values.put(LessonsEntry.COLUMN_NAME_NAME, currentLesson.optString("name", ""));
                    values.put(LessonsEntry.COLUMN_NAME_DESCRIPTION, currentLesson.optString("description", ""));
                    values.put(LessonsEntry.COLUMN_NAME_VERSION, currentLesson.optInt("version", 0));
                    values.put(LessonsEntry.COLUMN_NAME_CATEGORY, currentLesson.optString("category", ""));

                    returnee[i] = currentLesson.optInt("id", -1);

                    // Insert the new row, returning the primary key value of the new row
                    Log.d("respLes", currentLesson.getString("name"));
                    db.insert(LessonsEntry.TABLE_NAME, null, values);

                }
//                JSONObject jsonObject = new JSONObject(responseString);

                Log.d("respLes2", jsonArray.getJSONObject(0).getString("name"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return returnee;
        }

        @Override
        protected void onPostExecute(Integer[] result) {
            super.onPostExecute(result);

            for (int i = 0; i < result.length; i++) {
                new AsyncTaskGetInstructions().execute(result[i]);
            }


        }
    }
}
