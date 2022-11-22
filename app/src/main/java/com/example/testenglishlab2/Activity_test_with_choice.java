package com.example.testenglishlab2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Activity_test_with_choice extends AppCompatActivity {
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
    private static final String PARAM_MASS_ANSWER = "massAnswer";
    private int completeTest = 0; // Кількість пройдених питань

    boolean flagQuest = false; // Флаг питання

    //Timer
    private TextView textTimer;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds;
    //

    //Component Fragment
    private TextView textView;

    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    private RadioButton radioButtonTree;

    private RadioGroup radioGroupQuest;
    //

    int trueQuest = 0;
    int falseQuest = 0;
    //масив питань з відповіддями до них
    String[][] massQuest;
    //масив з варіантами відповідей
    String[][] massAnswer;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_COMPLETE_TEST, completeTest);
        outState.putLong(PARAM_TIME_LEFT_IN_MILLISECONDS, timeLeftInMilliseconds);
        outState.putInt(PARAM_TRUE_QUEST, trueQuest);
        outState.putInt(PARAM_FALSE_QUEST, falseQuest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_with_choice);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            setChooseLanguage(arguments.getInt(PARAM_CHOOSE_LANGUAGE));
            setNumberQuest(arguments.getInt(PARAM_NUMBER_QUEST));
            setTimeToOneQuest(arguments.getInt(PARAM_TIME_TO_ONE_QUEST));
            setListNumberQuest(arguments.getIntegerArrayList(PARAM_LIST_NUMBER_QUEST));
            timeLeftInMilliseconds = getTimeToOneQuest() * 1000L + 1000L;

            //
            massQuest = (String[][]) arguments.getSerializable(PARAM_MASS_QUEST);
            massAnswer = (String[][]) arguments.getSerializable(PARAM_MASS_ANSWER);
        }

        if (savedInstanceState != null) {
            completeTest = savedInstanceState.getInt(PARAM_COMPLETE_TEST);
            timeLeftInMilliseconds = savedInstanceState.getLong(PARAM_TIME_LEFT_IN_MILLISECONDS);
            trueQuest = savedInstanceState.getInt(PARAM_TRUE_QUEST);
            falseQuest = savedInstanceState.getInt(PARAM_FALSE_QUEST);
        }
        //
        textTimer = findViewById(R.id.textTime);

        textView = findViewById(R.id.textWord);

        radioButtonOne = findViewById(R.id.buttonChoiceOne);
        radioButtonTwo = findViewById(R.id.buttonChoiceTwo);
        radioButtonTree = findViewById(R.id.buttonChoiceTree);

        radioGroupQuest = findViewById(R.id.radioGroupQuest);
        //


    }

    @Override
    protected void onStart() {
        super.onStart();

        startTimer();
        writeQuest();

        findViewById(R.id.buttonContinueTestWithChoice).setOnClickListener(v -> {
            onClick();
        });
        findViewById(R.id.buttonExit).setOnClickListener(v -> {
            onExit();
        });
    }


    public void writeQuest() {

        textTimer.setTextColor(Color.BLACK);

        radioButtonOne.setTextColor(Color.BLACK);
        radioButtonTwo.setTextColor(Color.BLACK);
        radioButtonTree.setTextColor(Color.BLACK);

        radioButtonOne.setEnabled(true);
        radioButtonTwo.setEnabled(true);
        radioButtonTree.setEnabled(true);


        textView.setText(massQuest[listNumberQuest.get(getCompleteTest())][0]);

        radioButtonOne.setText(massAnswer[listNumberQuest.get(getCompleteTest())][0]);
        radioButtonTwo.setText(massAnswer[listNumberQuest.get(getCompleteTest())][1]);
        radioButtonTree.setText(massAnswer[listNumberQuest.get(getCompleteTest())][2]);

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

//                Intent intent = new Intent(this, SettingActivity.class);
//                startActivity(intent);
            }
        } else {
            int id = radioGroupQuest.getCheckedRadioButtonId();

            answerCheck(id);
            countDownTimer.cancel();

        }
    }

    public void answerCheck(int id) {

        System.out.println(id);
        if (id != -1) {
            RadioButton radioButton = findViewById(id);
            if (massQuest[listNumberQuest.get(getCompleteTest())][chooseLanguage].contentEquals(radioButton.getText())) {
                trueQuest++;
            } else {
                falseQuest++;
                radioButton.setTextColor(Color.RED);
            }
        } else {
            falseQuest++;
        }

        radioGroupQuest.clearCheck();

        if (massQuest[listNumberQuest.get(getCompleteTest())][chooseLanguage].contentEquals(radioButtonOne.getText())) {
            radioButtonOne.setTextColor(Color.GREEN);
        } else if (massQuest[listNumberQuest.get(getCompleteTest())][chooseLanguage].contentEquals(radioButtonTwo.getText())) {
            radioButtonTwo.setTextColor(Color.GREEN);
        } else if (massQuest[listNumberQuest.get(getCompleteTest())][chooseLanguage].contentEquals(radioButtonTree.getText())) {
            radioButtonTree.setTextColor(Color.GREEN);
        }
        radioButtonOne.setEnabled(false);
        radioButtonTwo.setEnabled(false);
        radioButtonTree.setEnabled(false);

        completeTest++;
        flagQuest = true;
    }

    private void onExit() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
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
                int id = radioGroupQuest.getCheckedRadioButtonId();

                answerCheck(id);
            }
        }.start();
    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText = "Time: ";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        textTimer.setText(timeLeftText);
    }

    public int getChooseLanguage() {
        return chooseLanguage;
    }

    public int getNumberQuest() {
        return numberQuest;
    }

    public int getTimeToOneQuest() {
        return timeToOneQuest;
    }

    public int getCompleteTest() {
        return completeTest;
    }

    public void setCompleteTest(int completeTest) {
        this.completeTest = completeTest;
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

    public ArrayList<Integer> getListNumberQuest() {
        return listNumberQuest;
    }

    public void setListNumberQuest(ArrayList<Integer> listNumberQuest) {
        this.listNumberQuest = listNumberQuest;
    }
}