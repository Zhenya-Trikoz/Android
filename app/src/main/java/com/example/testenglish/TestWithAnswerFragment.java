package com.example.testenglish;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;


public class TestWithAnswerFragment extends Fragment {

    public TestWithAnswerFragment() {
        super(R.layout.fragment_test_with_answer);
    }

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
    String[][] massQuest = {
            {" ", " "},
            {"Я", "I"},
            {"Працювати", "Work"},
            {"Вчитися", "Study"},
            {"Відпочивати", "Relax"},
            {"Ноутбук", "Laptop"},
            {"Встати", "Get up"},
            {"Купити", "To buy"},
            {"Зроблено", "Is made"},
            {"Змінити", "Change"},
            {"Ліжко", "Bed"},
            {"Відкрито", "Opened"}
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_COMPLETE_TEST, completeTest);
        outState.putLong(PARAM_TIME_LEFT_IN_MILLISECONDS, timeLeftInMilliseconds);
        outState.putInt(PARAM_TRUE_QUEST, trueQuest);
        outState.putInt(PARAM_FALSE_QUEST, falseQuest);
    }


    public static TestWithAnswerFragment newInstance(int chooseLanguage, int numberQuest, int timeToOneQuest, ArrayList<Integer> listNumberQuest) {
        TestWithAnswerFragment testWithAnswerFragment = new TestWithAnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_CHOOSE_LANGUAGE, chooseLanguage);
        bundle.putInt(PARAM_NUMBER_QUEST, numberQuest);
        bundle.putInt(PARAM_TIME_TO_ONE_QUEST, timeToOneQuest);
        bundle.putIntegerArrayList(PARAM_LIST_NUMBER_QUEST, listNumberQuest);
        testWithAnswerFragment.setArguments(bundle);
        return testWithAnswerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setChooseLanguage(getArguments().getInt(PARAM_CHOOSE_LANGUAGE));
            setNumberQuest(getArguments().getInt(PARAM_NUMBER_QUEST));
            setTimeToOneQuest(getArguments().getInt(PARAM_TIME_TO_ONE_QUEST));
            setListNumberQuest(getArguments().getIntegerArrayList(PARAM_LIST_NUMBER_QUEST));
            timeLeftInMilliseconds = getTimeToOneQuest() * 1000L + 1000L;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_with_answer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            completeTest = savedInstanceState.getInt(PARAM_COMPLETE_TEST);
            timeLeftInMilliseconds = savedInstanceState.getLong(PARAM_TIME_LEFT_IN_MILLISECONDS);
            trueQuest = savedInstanceState.getInt(PARAM_TRUE_QUEST);
            falseQuest = savedInstanceState.getInt(PARAM_FALSE_QUEST);
        }

        textTimer = view.findViewById(R.id.textTime);

        textView = view.findViewById(R.id.textWord);
        editText = view.findViewById(R.id.editTextAnswer);

        startTimer();
        writeQuest();

        view.findViewById(R.id.buttonContinueTestWithAnswer).setOnClickListener(v -> {
            onClick(view);
        });
        view.findViewById(R.id.buttonExit).setOnClickListener(v -> {
            onExit(view);
        });
    }


    private void writeQuest() {

        textView.setTextColor(Color.BLACK);
        textView.setText(massQuest[listNumberQuest.get(getCompleteTest())][0]);

        textTimer.setTextColor(Color.BLACK);

        editText.setText("");
        editText.setEnabled(true);
    }

    private void onClick(View view) {
        if (flagQuest) {
            if (completeTest < numberQuest) {
                writeQuest();
                timeLeftInMilliseconds = getTimeToOneQuest() * 1000L + 1000L;
                startTimer();
                flagQuest = false;
            } else {

                Bundle bundle = new Bundle();
                DialogFragment dialogFragment = new DialogFragment();

                bundle.putInt(PARAM_NUMBER_QUEST, numberQuest);
                bundle.putInt(PARAM_NUMBER_TRUE_QUEST, trueQuest);
                bundle.putInt(PARAM_NUMBER_FALSE_QUEST, falseQuest);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "dialog");

                SettingFragment settingFragment = (SettingFragment) SettingFragment.newInstance();
                settingFragment.setTargetFragment(this, 0);
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragmentContainerView, settingFragment)
                        .commit();
            }
        } else {
            answerCheck();
            countDownTimer.cancel();

        }
    }

    private void onExit(View view) {
        SettingFragment settingFragment = (SettingFragment) SettingFragment.newInstance();
        settingFragment.setTargetFragment(this, 0);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, settingFragment)
                .commit();
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