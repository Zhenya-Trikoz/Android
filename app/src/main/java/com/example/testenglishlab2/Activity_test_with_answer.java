package com.example.testenglishlab2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Activity_test_with_answer extends AppCompatActivity {

    private static final String PARAM_CHOOSE_LANGUAGE = "chooseLanguage";
    private static final String PARAM_NUMBER_QUEST = "numberQuest";
    private static final String PARAM_TIME_TO_ONE_QUEST = "timeToOneQuest";
    private static final String PARAM_LIST_NUMBER_QUEST = "listNumberQuest";

    private static final String PARAM_NUMBER_TRUE_QUEST = "numberTrueQuest";
    private static final String PARAM_NUMBER_FALSE_QUEST = "numberFalseQuest";

    private int chooseLanguage; // Вибір мови тесту
    private int numberQuest; // Кількість питань в тесті
    private int timeToOneQuest; // Час на одне питання
    private ArrayList<Integer> listNumberQuest; // Список номерів питань

    ///
    private static final String PARAM_COMPLETE_TEST = "completeTest";
    private static final String PARAM_TIME_LEFT_IN_MILLISECONDS = "timeLeftInMilliseconds";
    private static final String PARAM_TRUE_QUEST = "trueQuest";
    private static final String PARAM_FALSE_QUEST = "falseQuest";
    //

    // Для масивов
    private static final String PARAM_MASS_QUEST = "massQuest";
    private int completeTest = 0; // Кількість пройдених питань

    boolean flagQuest = false; // Флаг питання

    //Timer
    private TextView textTimer;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds;
    //

    //Component Fragment
    private TextView textView;
    private EditText editText;
    //
    int trueQuest = 0;
    int falseQuest = 0;
    //масив питань з відповіддями до них
    String[][] massQuest;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_COMPLETE_TEST, completeTest);
        outState.putLong(PARAM_TIME_LEFT_IN_MILLISECONDS, timeLeftInMilliseconds);
        outState.putInt(PARAM_TRUE_QUEST, trueQuest);
        outState.putInt(PARAM_FALSE_QUEST, falseQuest);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_with_answer);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            setChooseLanguage(arguments.getInt(PARAM_CHOOSE_LANGUAGE));
            setNumberQuest(arguments.getInt(PARAM_NUMBER_QUEST));
            setTimeToOneQuest(arguments.getInt(PARAM_TIME_TO_ONE_QUEST));
            setListNumberQuest(arguments.getIntegerArrayList(PARAM_LIST_NUMBER_QUEST));
            timeLeftInMilliseconds = getTimeToOneQuest() * 1000L + 1000L;

            //
            massQuest = (String[][]) arguments.getSerializable(PARAM_MASS_QUEST);
        }

        if (savedInstanceState != null) {
            completeTest = savedInstanceState.getInt(PARAM_COMPLETE_TEST);
            timeLeftInMilliseconds = savedInstanceState.getLong(PARAM_TIME_LEFT_IN_MILLISECONDS);
            trueQuest = savedInstanceState.getInt(PARAM_TRUE_QUEST);
            falseQuest = savedInstanceState.getInt(PARAM_FALSE_QUEST);
        }
        textTimer = findViewById(R.id.textTime);

        textView = findViewById(R.id.textWord);
        editText = findViewById(R.id.editTextAnswer);

    }

    @Override
    protected void onStart() {
        super.onStart();

        startTimer();
        writeQuest();

        findViewById(R.id.buttonContinueTestWithAnswer).setOnClickListener(v -> onClick());
        findViewById(R.id.buttonExit).setOnClickListener(v -> onExit());
    }

    private void writeQuest() {

        textView.setTextColor(Color.BLACK);
        textView.setText(massQuest[listNumberQuest.get(getCompleteTest())][0]);

        textTimer.setTextColor(Color.BLACK);

        editText.setText("");
        editText.setEnabled(true);
    }

    private void onClick() {
        if (flagQuest) {
            if (completeTest < numberQuest) {
                writeQuest();
                timeLeftInMilliseconds = getTimeToOneQuest() * 1000L + 1000L;
                startTimer();
                flagQuest = false;
            } else {

                Bundle bundle = new Bundle();
                CustomDialogFragment dialogFragment = new CustomDialogFragment();

                bundle.putInt(PARAM_NUMBER_QUEST, numberQuest);
                bundle.putInt(PARAM_NUMBER_TRUE_QUEST, trueQuest);
                bundle.putInt(PARAM_NUMBER_FALSE_QUEST, falseQuest);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "dialog");

            }
        } else {
            answerCheck();
            countDownTimer.cancel();

        }
    }

    private void onExit() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void answerCheck() {

        String s = editText.getText().toString();
        editText.setText("");
        editText.setEnabled(false);


        if (massQuest[listNumberQuest.get(completeTest)][chooseLanguage].equals(s)) {
            trueQuest++;
            textView.setTextColor(Color.GREEN);
        } else {
            falseQuest++;
            textView.setTextColor(Color.RED);
        }

        flagQuest = true;
        completeTest++;

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                textTimer.setTextColor(Color.RED);
                answerCheck();
            }
        }.start();


    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText = "";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        textTimer.setText(timeLeftText);
    }



    public int getTimeToOneQuest() {
        return timeToOneQuest;
    }


    public int getCompleteTest() {
        return completeTest;
    }


    public void setChooseLanguage(int chooseLanguage) {
        this.chooseLanguage = chooseLanguage;
    }

    public void setNumberQuest(int numberQuest) {
        this.numberQuest = numberQuest;
    }

    public void setTimeToOneQuest(int timeToOneQuest) {
        this.timeToOneQuest = timeToOneQuest;
    }

    public void setListNumberQuest(ArrayList<Integer> listNumberQuest) {
        this.listNumberQuest = listNumberQuest;
    }
}