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


public class TestWithAnswerFragment extends Fragment {

    public TestWithAnswerFragment() {
        super(R.layout.fragment_test_with_answer);
    }

    private static final String PARAM_CHOOSE_LANGUAGE = "chooseLanguage";
    private static final String PARAM_NUMBER_QUEST = "numberQuest";
    private static final String PARAM_TIME_TO_ONE_QUEST = "timeToOneQuest";

    private int chooseLanguage;
    private int numberQuest;
    private int timeToOneQuest;

    private int[] orderQuest = new int[getNumberQuest()];
    private int completeTest = 0;

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
            {"Ноутбук", "Laptop"}
    };

    public static TestWithAnswerFragment newInstance(int chooseLanguage, int numberQuest, int timeToOneQuest) {
        TestWithAnswerFragment testWithAnswerFragment = new TestWithAnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_CHOOSE_LANGUAGE, chooseLanguage);
        bundle.putInt(PARAM_NUMBER_QUEST, numberQuest);
        bundle.putInt(PARAM_TIME_TO_ONE_QUEST, timeToOneQuest);
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
        setOrderQuest(randomMassNumberQuest());
        textTimer = view.findViewById(R.id.textTime);

        textView = view.findViewById(R.id.textWord);
        editText = view.findViewById(R.id.editTextAnswer);

        startTimer();
        writeQuest();

        view.findViewById(R.id.buttonContinueTestWithAnswer).setOnClickListener(v -> {
            onClick();
        });
        view.findViewById(R.id.buttonExit).setOnClickListener(v -> {
            onExit(view);
        });
    }


    private void writeQuest() {

        textView.setTextColor(Color.BLACK);
        textView.setText(massQuest[orderQuest[getCompleteTest()]][0]);

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


        if (massQuest[orderQuest[completeTest]][chooseLanguage].equals(s)) {
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

    public int[] getOrderQuest() {
        return orderQuest;
    }

    public int getCompleteTest() {
        return completeTest;
    }

    public void setCompleteTest(int completeTest) {
        this.completeTest = completeTest;
    }

    public void setOrderQuest(int[] orderQuest) {
        this.orderQuest = orderQuest;
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
}