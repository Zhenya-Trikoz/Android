package com.example.testenglish;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    //масив з варіантами відповідей
    String[][] massAnswer = {
            {" ", " ", " "},
            {"I", "I`m", "Me"},
            {"Work", "Working", "will work"},
            {"Studying", "Study", "Learn"},
            {"Vacation", "Lazy", "Relax"},
            {"Laptop", "PC", "PS5"}
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

        writeQuest(view);


        view.findViewById(R.id.buttonContinueTestWithAnswer).setOnClickListener(v -> {
            onClick(view);
        });
    }

    public void writeQuest(View view) {
        TextView textView = view.findViewById(R.id.textWord);

        RadioButton radioButtonOne = view.findViewById(R.id.buttonAnswerOne);
        RadioButton radioButtonTwo = view.findViewById(R.id.buttonAnswerTwo);
        RadioButton radioButtonTree = view.findViewById(R.id.buttonAnswerTree);

        radioButtonOne.setTextColor(Color.BLACK);
        radioButtonTwo.setTextColor(Color.BLACK);
        radioButtonTree.setTextColor(Color.BLACK);

        radioButtonOne.setEnabled(true);
        radioButtonTwo.setEnabled(true);
        radioButtonTree.setEnabled(true);


        textView.setText(massQuest[orderQuest[getCompleteTest()]][0]);

        radioButtonOne.setText(massAnswer[orderQuest[getCompleteTest()]][0]);
        radioButtonTwo.setText(massAnswer[orderQuest[getCompleteTest()]][1]);
        radioButtonTree.setText(massAnswer[orderQuest[getCompleteTest()]][2]);

    }

    private void onClick(View view) {
        if (flagQuest) {
            if (completeTest < numberQuest) {
                writeQuest(view);
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
            RadioGroup radioGroupQuest = view.findViewById(R.id.radioGroupQuest);
            int id = radioGroupQuest.getCheckedRadioButtonId();

            answerCheck(view, id);

            completeTest++;
            flagQuest = true;
        }
    }

    public void answerCheck(View view, int id) {
        RadioButton radioButton = view.findViewById(id);
        if (massQuest[orderQuest[completeTest]][chooseLanguage].contentEquals(radioButton.getText())) {
            trueQuest++;
        } else {
            falseQuest++;
            radioButton.setTextColor(Color.RED);
        }

        RadioButton radioButtonOne = view.findViewById(R.id.buttonAnswerOne);
        RadioButton radioButtonTwo = view.findViewById(R.id.buttonAnswerTwo);
        RadioButton radioButtonTree = view.findViewById(R.id.buttonAnswerTree);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupQuest);
        radioGroup.clearCheck();

        if (massQuest[orderQuest[completeTest]][chooseLanguage].contentEquals(radioButtonOne.getText())) {
            radioButtonOne.setTextColor(Color.GREEN);
        } else if (massQuest[orderQuest[completeTest]][chooseLanguage].contentEquals(radioButtonTwo.getText())) {
            radioButtonTwo.setTextColor(Color.GREEN);
        } else if (massQuest[orderQuest[completeTest]][chooseLanguage].contentEquals(radioButtonTree.getText())) {
            radioButtonTree.setTextColor(Color.GREEN);
        }
        radioButtonOne.setEnabled(false);
        radioButtonTwo.setEnabled(false);
        radioButtonTree.setEnabled(false);

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