package com.makokit.mako;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import java.io.File;
import java.util.logging.Handler;

import com.makokit.mako.MakoContract.*;

public class ChatActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final LinearLayout chatLayout = (LinearLayout) findViewById(R.id.chatView);
        chatLayout.setGravity(Gravity.BOTTOM);

        Instruction firstInstruction = getInstruction(-1, 3);
        Answer[] answers = getAnswers(firstInstruction.instructionId, firstInstruction.instructionNextId);

//        Log.d("haha", firstInstruction.instructionImage);
        createTextBubble(chatLayout, firstInstruction.instructionText, true, firstInstruction.instructionImage);

        LinearLayout answersLayout = (LinearLayout) findViewById(R.id.layoutAnswers);
        answersLayout.removeAllViews();

        createAnswers(chatLayout, answersLayout, answers, firstInstruction.instructionNextId);

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollChat);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
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

    private void createAnswers(final LinearLayout chatLayout, final LinearLayout answerLayout, final Answer[] currentAnswers,
                               final int instructionNextId) {

//        answerLayout.removeAllViews();
//        Log.d("haha", currentAnswers.toString());

        if (currentAnswers == null) {
            Button nextButton = new Button(this);
            nextButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            if (instructionNextId == -1) nextButton.setText("FINISH");
            else nextButton.setText("NEXT");
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (instructionNextId == -1) {
                        createTextBubble(chatLayout, "FINISH", false, "");
                        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollChat);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                        answerLayout.removeAllViews();
                    }
                    else {
                        createTextBubble(chatLayout, "NEXT", false, "");

                        Instruction instruction = getInstruction(instructionNextId, 3);
                        Answer[] answers = getAnswers(instruction.instructionId, instruction.instructionNextId);
                        createTextBubble(chatLayout, instruction.instructionText, true, instruction.instructionImage);
                        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollChat);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });

                        answerLayout.removeAllViews();
                        createAnswers(chatLayout, answerLayout, answers, instruction.instructionNextId);
                    }

                }
            });
            answerLayout.addView(nextButton);
        }
        else {
            for (int i = 0; i < currentAnswers.length; i++) {
//                Log.d("haha", currentAnswers[i].answerText);
                Button answerButton = new Button(this);
                answerButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                answerButton.setText(currentAnswers[i].answerText);
                final int finalI = i;
                answerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createTextBubble(chatLayout, currentAnswers[finalI].answerText, false, "");

                        Instruction instruction = getInstruction(currentAnswers[finalI].answerJumpId, 3);
                        Answer[] answers = getAnswers(instruction.instructionId, instruction.instructionNextId);

                        createTextBubble(chatLayout, instruction.instructionText, true, instruction.instructionImage);
                        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollChat);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });

                        answerLayout.removeAllViews();
                        createAnswers(chatLayout, answerLayout, answers, instruction.instructionNextId);
//                        answerLayout.invalidate();
                    }
                });
                answerLayout.addView(answerButton);
            }
        }
//        answerLayout.invalidate();
    }

    private Instruction getInstruction(int instructionId, int lessonId) {
        MakoDatabase makoDb = new MakoDatabase(this);
        SQLiteDatabase db = makoDb.getReadableDatabase();
        Instruction currentInstruction;
        String[] projection = {
                InstructionsEntry.COLUMN_NAME_INSTRUCTION_ID,
                InstructionsEntry.COLUMN_NAME_TEXT,
                InstructionsEntry.COLUMN_NAME_IMAGE_URL,
                InstructionsEntry.COLUMN_NAME_VIDEO_URL,
                InstructionsEntry.COLUMN_NAME_NEXT_INSTRUCTION_ID};
        String[] selectionArgs;
        Cursor instructionsCursor;

        if (instructionId == -1) {
            selectionArgs = new String[]{Integer.toString(lessonId)};
            instructionsCursor = db.query(InstructionsEntry.TABLE_NAME, projection,
                    InstructionsEntry.COLUMN_NAME_LESSON_ID + "=?", selectionArgs, null, null, null, "1");
        }
        else {
            selectionArgs = new String[]{Integer.toString(instructionId)};
            instructionsCursor = db.query(InstructionsEntry.TABLE_NAME, projection,
                    InstructionsEntry.COLUMN_NAME_INSTRUCTION_ID + "=?", selectionArgs, null, null, null, "1");
        }

        instructionsCursor.moveToFirst();

        currentInstruction = new Instruction(instructionsCursor.getInt(instructionsCursor.getColumnIndexOrThrow(InstructionsEntry.COLUMN_NAME_INSTRUCTION_ID)),
                instructionsCursor.getString(instructionsCursor.getColumnIndexOrThrow(InstructionsEntry.COLUMN_NAME_TEXT)),
                instructionsCursor.getString(instructionsCursor.getColumnIndexOrThrow(InstructionsEntry.COLUMN_NAME_IMAGE_URL)),
                instructionsCursor.getString(instructionsCursor.getColumnIndexOrThrow(InstructionsEntry.COLUMN_NAME_VIDEO_URL)),
                instructionsCursor.getInt(instructionsCursor.getColumnIndexOrThrow(InstructionsEntry.COLUMN_NAME_NEXT_INSTRUCTION_ID)));

        instructionsCursor.close();

        return currentInstruction;
    }

    private Answer[] getAnswers(int instructionId, int instructionNextId) {
        MakoDatabase makoDb = new MakoDatabase(this);
        SQLiteDatabase db = makoDb.getReadableDatabase();
        String[] answerProjection = {
                AnswersEntry.COLUMN_NAME_ANSWER_ID,
                AnswersEntry.COLUMN_NAME_INSTRUCTION_ID,
                AnswersEntry.COLUMN_NAME_TEXT,
                AnswersEntry.COLUMN_NAME_IMAGE_URL,
                AnswersEntry.COLUMN_NAME_CORRECT};
        String[] answerSelectionArgs = {Integer.toString(instructionId)};
        Cursor answersCursor = db.query(AnswersEntry.TABLE_NAME, answerProjection,
                AnswersEntry.COLUMN_NAME_INSTRUCTION_ID + "=?", answerSelectionArgs, null, null, null);

        if (answersCursor.getCount() == 0) return null;

        answersCursor.moveToFirst();
        Answer[] answers = new Answer[answersCursor.getCount()];

        for (int i = 0; i < answers.length; i++) {
            int answerID = answersCursor.getInt(answersCursor.getColumnIndexOrThrow(AnswersEntry.COLUMN_NAME_ANSWER_ID));
            String[] jumpProjection = {
                    JumpsEntry.COLUMN_NAME_JUMP_ID,
                    JumpsEntry.COLUMN_NAME_ANSWER_ID,
                    JumpsEntry.COLUMN_NAME_INSTRUCTION_ID
            };

            String[] jumpSelectionArgs = {Integer.toString(answerID)};

            Cursor jumpCursor = db.query(JumpsEntry.TABLE_NAME, jumpProjection, JumpsEntry.COLUMN_NAME_ANSWER_ID + "=?",
                    jumpSelectionArgs, null,null, null);

            int jumpInsId = instructionNextId;

            if (jumpCursor.getCount() != 0) {
                jumpCursor.moveToFirst();
                jumpInsId = jumpCursor.getInt(jumpCursor.getColumnIndexOrThrow(JumpsEntry.COLUMN_NAME_INSTRUCTION_ID));
            }

            Answer answer = new Answer(answersCursor.getInt(answersCursor.getColumnIndexOrThrow(AnswersEntry.COLUMN_NAME_ANSWER_ID)),
                    answersCursor.getInt(answersCursor.getColumnIndexOrThrow(AnswersEntry.COLUMN_NAME_INSTRUCTION_ID)),
                    answersCursor.getString(answersCursor.getColumnIndexOrThrow(AnswersEntry.COLUMN_NAME_TEXT)),
                    answersCursor.getString(answersCursor.getColumnIndexOrThrow(AnswersEntry.COLUMN_NAME_IMAGE_URL)),
                    answersCursor.getInt(answersCursor.getColumnIndexOrThrow(AnswersEntry.COLUMN_NAME_CORRECT)), jumpInsId);

//            Log.d("haha2", answer.answerText);
            answers[i] = answer;
            answersCursor.moveToNext();
        }

        return answers;
    }

    private void createTextBubble(LinearLayout ll, String text, Boolean left, String pictureUrl) {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(17);

        RelativeLayout pictureLayout = new RelativeLayout(this);

//        Log.d("haha", pictureUrl);
        RelativeLayout.LayoutParams layoutParamsPicture = new RelativeLayout.LayoutParams(400, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsPicture.addRule(RelativeLayout.CENTER_IN_PARENT, 1);

        WebView wv = new WebView(this);
        wv.setLayoutParams(layoutParamsPicture);
        wv.loadUrl(pictureUrl);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        if (pictureUrl != "") {
            Log.d("haha", "I'm here");
            pictureLayout.addView(wv);

        }

        if (left) {
            linearLayout.setBackgroundResource(R.drawable.bubble);
            textView.setPadding(100, 0, 50, 0);
            textView.setTextColor(Color.WHITE);
//            wv.setPadding(100, 0, 20, 0);
        }
        else {
            linearLayout.setBackgroundResource(R.drawable.bubble2);
            textView.setPadding(0, 0, 100, 0);
//            wv.setPadding(20, 0, 100, 0);
            textView.setGravity(Gravity.RIGHT);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
        }

        Space space1 = new Space(this);
        space1.setMinimumHeight(25);
        linearLayout.addView(space1);

        linearLayout.addView(textView);
        linearLayout.addView(pictureLayout);
//        }

        Space space = new Space(this);
        space.setMinimumHeight(25);
        linearLayout.addView(space);

//        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
//        textView.setHeight(200);
        ll.addView(linearLayout);

        Space space2 = new Space(this);
        space2.setMinimumHeight(50);
        ll.addView(space2);
    }

    class Instruction {
        int instructionId;
        String instructionText;
        String instructionImage;
        String instructionVideo;
        int instructionNextId;

        Instruction(int insId, String insText, String insImage, String insVideo,
                    int insNextId) {
            instructionId = insId;
            instructionText = insText;
            instructionImage = insImage;
            instructionVideo = insVideo;
            instructionNextId = insNextId;
        }

    }

    class Answer {
        int answerId;
        int answerInstructionId;
        String answerText;
        String answerImage;
        int answerCorrect;
        int answerJumpId;

        Answer(int ansId, int ansInsId, String ansText, String ansImage, int ansCorrect, int jumpToId) {
            answerId = ansId;
            answerInstructionId = ansInsId;
            answerText = ansText;
            answerImage = ansImage;
            answerCorrect = ansCorrect;
            answerJumpId = jumpToId;
        }
    }
}
