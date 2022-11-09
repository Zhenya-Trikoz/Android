package com.example.testenglish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;


public class SettingFragment extends Fragment {

    int chooseLanguage = 0;
    int numberQuest = 0;
    int timeToOneQuest = 0;

    public static Fragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        RadioGroup radioGroupChooseLanguage = view.findViewById(R.id.radioGroupChooseLanguage);
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

        RadioGroup radioGroupChooseNumberQuestion = view.findViewById(R.id.radioGroupChooseNumberQuestion);
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
        RadioGroup radioGroupTimeToOneQuestion = view.findViewById(R.id.radioGroupTimeToOneQuestion);
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

        view.findViewById(R.id.buttonTestWithAnswer).setOnClickListener(v -> {
            onTestWithAnswer();
        });

        view.findViewById(R.id.buttonTestWithChoice).setOnClickListener(v -> {
            onTestWithChoice();
        });

    }


    private void onTestWithAnswer() {
        TestWithAnswerFragment testWithAnswerFragment = TestWithAnswerFragment.newInstance(chooseLanguage, numberQuest, timeToOneQuest, randomListNumberQuest());
        testWithAnswerFragment.setTargetFragment(this, 0);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, testWithAnswerFragment)
                .commit();
    }

    private void onTestWithChoice() {
        TestWithChoiceFragment testWithChoiceFragment = TestWithChoiceFragment.newInstance(chooseLanguage, numberQuest, timeToOneQuest, randomListNumberQuest());
        testWithChoiceFragment.setTargetFragment(this, 0);
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, testWithChoiceFragment)
                .commit();
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
}