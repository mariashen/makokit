package com.makokit.mako;

import android.provider.BaseColumns;

/**
 * Created by muhammadkhadafi on 4/7/16.
 */
public final class MakoContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MakoContract() {}

    /* Inner class that defines the table contents */
    public static abstract class LessonsEntry implements BaseColumns {
        public static final String TABLE_NAME = "lessons";
        public static final String COLUMN_NAME_LESSON_ID = "lesson_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_VERSION = "version";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static abstract class InstructionsEntry implements BaseColumns {
        public static final String TABLE_NAME = "instructions";
        public static final String COLUMN_NAME_INSTRUCTION_ID = "instruction_id";
        public static final String COLUMN_NAME_LESSON_ID = "lesson_id";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
        public static final String COLUMN_NAME_VIDEO_URL = "video_url";
        public static final String COLUMN_NAME_NEXT_INSTRUCTION_ID = "next_instruction_id";

    }

    public static abstract class AnswersEntry implements BaseColumns {
        public static final String TABLE_NAME = "answers";
        public static final String COLUMN_NAME_INSTRUCTION_ID = "instruction_id";
        public static final String COLUMN_NAME_ANSWER_ID = "answer_id";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
        public static final String COLUMN_NAME_CORRECT = "correct";
    }

    public static abstract class JumpsEntry implements BaseColumns {
        public static final String TABLE_NAME = "jumps";
        public static final String COLUMN_NAME_INSTRUCTION_ID = "instruction_id";
        public static final String COLUMN_NAME_ANSWER_ID = "answer_id";
        public static final String COLUMN_NAME_JUMP_ID = "jump_id";
    }
}
