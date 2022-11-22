package com.example.testenglishlab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    int chooseLanguage = 0;
    int numberQuest = 0;
    int timeToOneQuest = 0;

    private MyBroadcastReceiver mMyBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Запускаем свой IntentService
        Intent intentMyIntentService = new Intent(this, MyIntentService.class);
        intentMyIntentService.setAction(MyIntentService.ACTION_RESPONSE_WORLD);
        startService(intentMyIntentService);

        mMyBroadcastReceiver = new MyBroadcastReceiver();
        // регистрируем BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(
                MyIntentService.ACTION_RESPONSE_WORLD);
//        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mMyBroadcastReceiver, intentFilter);
        //////////////////////
        RadioGroup radioGroupChooseLanguage = findViewById(R.id.radioGroupChooseLanguage);
        radioGroupChooseLanguage.setOnCheckedChangeListener((new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.buttonChooseLanguageEnglish:
                        chooseLanguage = 1;
                        break;
                    case R.id.buttonChooseLanguageGerman:
                        chooseLanguage = 2;
                        break;
                    default:
                        break;
                }

            }
        }));

        RadioGroup radioGroupChooseNumberQuestion = findViewById(R.id.radioGroupChooseNumberQuestion);
        radioGroupChooseNumberQuestion.setOnCheckedChangeListener((new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.buttonChooseNumberQuestionFive:
                        numberQuest = 5;
                        break;
                    case R.id.buttonChooseNumberQuestionTen:
                        numberQuest = 10;
                        break;
                    default:
                        break;
                }

            }
        }));

        RadioGroup radioGroupTimeToOneQuestion = findViewById(R.id.radioGroupTimeToOneQuestion);
        radioGroupTimeToOneQuestion.setOnCheckedChangeListener((new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.buttonTimeToOneQuestion20S:
                        timeToOneQuest = 20;
                        break;
                    case R.id.buttonTimeToOneQuestion30S:
                        timeToOneQuest = 30;
                        break;
                    case R.id.buttonTimeToOneQuestion1M:
                        timeToOneQuest = 60;
                        break;
                    default:
                        break;
                }

            }
        }));

        findViewById(R.id.buttonTestWithAnswer).setOnClickListener(v -> {
            onTestWithAnswer();
        });

        findViewById(R.id.buttonTestWithChoice).setOnClickListener(v -> {
            onTestWithChoice();
        });

    }

    private static final String PARAM_CHOOSE_LANGUAGE = "chooseLanguage";
    private static final String PARAM_NUMBER_QUEST = "numberQuest";
    private static final String PARAM_TIME_TO_ONE_QUEST = "timeToOneQuest";
    private static final String PARAM_LIST_NUMBER_QUEST = "listNumberQuest";


    // Для масивов
    private static final String PARAM_MASS_QUEST = "massQuest";
    private static final String PARAM_MASS_ANSWER = "massAnswer";
    String[][] massQuest;
    String[][] massAnswer;

    private void onTestWithChoice() {
        Intent intent = new Intent(this, Activity_test_with_choice.class);
        intent.putExtra(PARAM_CHOOSE_LANGUAGE, chooseLanguage);
        intent.putExtra(PARAM_NUMBER_QUEST, numberQuest);
        intent.putExtra(PARAM_TIME_TO_ONE_QUEST, timeToOneQuest);
        intent.putExtra(PARAM_LIST_NUMBER_QUEST, randomListNumberQuest());
        //
        intent.putExtra(PARAM_MASS_QUEST, massQuest);
        intent.putExtra(PARAM_MASS_ANSWER, massAnswer);

        startActivity(intent);
    }

    private void onTestWithAnswer() {
        Intent intent = new Intent(this, Activity_test_with_answer.class);
        intent.putExtra(PARAM_CHOOSE_LANGUAGE, chooseLanguage);
        intent.putExtra(PARAM_NUMBER_QUEST, numberQuest);
        intent.putExtra(PARAM_TIME_TO_ONE_QUEST, timeToOneQuest);
        intent.putExtra(PARAM_LIST_NUMBER_QUEST, randomListNumberQuest());

        intent.putExtra(PARAM_MASS_QUEST, massQuest);
        startActivity(intent);
    }


    // метод створення рандомного списку питань
    public ArrayList<Integer> randomListNumberQuest() {
        int a = 0;
        boolean flag = true;
        int count = 0;

        ArrayList<Integer> listNumberQuest = new ArrayList<>();

        for (int i = 0; i < numberQuest; i++) {
            while (flag) {
                a = (int) ((Math.random() * 10) + 1);
                for (int b = 0; b < listNumberQuest.size(); b++) {
                    if (listNumberQuest.get(b) == a) {
                        count++;
                    }
                }
                if (count == 0) {
                    flag = false;
                }
                count = 0;
            }
            listNumberQuest.add(a);
            flag = true;
        }

        return listNumberQuest;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyBroadcastReceiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            massQuest = (String[][]) intent.getSerializableExtra(MyIntentService.EXTRA_MASS_QUEST);
            massAnswer = (String[][]) intent.getSerializableExtra(MyIntentService.EXTRA_MASS_ANSWER);

        }
    }
}