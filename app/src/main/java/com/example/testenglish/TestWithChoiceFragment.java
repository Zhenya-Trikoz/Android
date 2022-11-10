package com.example.testenglish;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TestWithChoiceFragment extends Fragment {

    public TestWithChoiceFragment() {
        super(R.layout.fragment_test_with_choice);
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

    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    private RadioButton radioButtonTree;

    private RadioGroup radioGroupQuest;
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
    //масив з варіантами відповідей
    String[][] massAnswer = {
            {" ", " ", " "},
            {"I", "I`m", "Me"},
            {"Work", "Working", "will work"},
            {"Studying", "Study", "Learn"},
            {"Vacation", "Lazy", "Relax"},
            {"Laptop", "PC", "PS5"},
            {"Get up", "Getting up", "Gets up"},
            {"Buying", "To buy", "Buy"},
            {"Makes", "Is making", "Is made"},
            {"Change", "Changing", "To change"},
            {"Bed", "Bad", "Baed"},
            {"Closed", "Open", "Opened"}
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PARAM_COMPLETE_TEST, completeTest);
        outState.putLong(PARAM_TIME_LEFT_IN_MILLISECONDS, timeLeftInMilliseconds);
        outState.putInt(PARAM_TRUE_QUEST, trueQuest);
        outState.putInt(PARAM_FALSE_QUEST, falseQuest);
    }

    public static TestWithChoiceFragment newInstance(int chooseLanguage, int numberQuest, int timeToOneQuest, ArrayList<Integer> listNumberQuest) {
        TestWithChoiceFragment testWithChoiceFragment = new TestWithChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_CHOOSE_LANGUAGE, chooseLanguage);
        bundle.putInt(PARAM_NUMBER_QUEST, numberQuest);
        bundle.putInt(PARAM_TIME_TO_ONE_QUEST, timeToOneQuest);
        bundle.putIntegerArrayList(PARAM_LIST_NUMBER_QUEST, listNumberQuest);
        testWithChoiceFragment.setArguments(bundle);
        return testWithChoiceFragment;
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
        return inflater.inflate(R.layout.fragment_test_with_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            completeTest = savedInstanceState.getInt(PARAM_COMPLETE_TEST);
            timeLeftInMilliseconds = savedInstanceState.getLong(PARAM_TIME_LEFT_IN_MILLISECONDS);
            trueQuest = savedInstanceState.getInt(PARAM_TRUE_QUEST);
            falseQuest = savedInstanceState.getInt(PARAM_FALSE_QUEST);
        }

        //
        textTimer = view.findViewById(R.id.textTime);

        textView = view.findViewById(R.id.textWord);

        radioButtonOne = view.findViewById(R.id.buttonChoiceOne);
        radioButtonTwo = view.findViewById(R.id.buttonChoiceTwo);
        radioButtonTree = view.findViewById(R.id.buttonChoiceTree);

        radioGroupQuest = view.findViewById(R.id.radioGroupQuest);
        //

        startTimer(view);
        writeQuest();

        view.findViewById(R.id.buttonContinueTestWithAnswer).setOnClickListener(v -> {
            onClick(view);
        });
        view.findViewById(R.id.buttonExit).setOnClickListener(v -> {
            onExit(view);
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

    private void onClick(View view) {
        if (flagQuest) {
            if (completeTest < numberQuest) {
                writeQuest();
                timeLeftInMilliseconds = getTimeToOneQuest() * 1000L + 1000L;
                startTimer(view);
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
            int id = radioGroupQuest.getCheckedRadioButtonId();

            answerCheck(view, id);
            countDownTimer.cancel();

        }
    }

    public void answerCheck(View view, int id) {

        System.out.println(id);
        if (id != -1) {
            RadioButton radioButton = view.findViewById(id);
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

    private void onExit(View view) {
        SettingFragment settingFragment = (SettingFragment) SettingFragment.newInstance();
        settingFragment.setTargetFragment(this, 0);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, settingFragment)
                .commit();
    }


    private void startTimer(View view) {
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

                answerCheck(view, id);
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

    // метод створення рандомного масиву питань
    public int[] randomMassNumberQuest() {
        int a = 0;
        boolean flag = true;
        int count = 0;
        int[] bufMass = new int[getNumberQuest()];
        for (int i = 0; i < bufMass.length; i++) {
            while (flag) {
                a = (int) ((Math.random() * getNumberQuest()) + 1);
                for (int b = 0; b < bufMass.length; b++) {
                    if (bufMass[b] == a) {
                        count++;
                    }
                }
                if (count == 0) {
                    flag = false;
                }
                count = 0;
            }
            bufMass[i] = a;
            flag = true;
        }
        return bufMass;
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