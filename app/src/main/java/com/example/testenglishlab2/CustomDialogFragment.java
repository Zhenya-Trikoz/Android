package com.example.testenglishlab2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    private static final String PARAM_NUMBER_QUEST = "numberQuest";
    private static final String PARAM_NUMBER_TRUE_QUEST = "numberTrueQuest";
    private static final String PARAM_NUMBER_FALSE_QUEST = "numberFalseQuest";

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int numberQuest = getArguments().getInt(PARAM_NUMBER_QUEST);
        int trueQuest = getArguments().getInt(PARAM_NUMBER_TRUE_QUEST);
        int falseQuest = getArguments().getInt(PARAM_NUMBER_FALSE_QUEST);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Результат тесту")
                .setMessage("Кількість питань: " + numberQuest +
                        "\n" +
                        "Правильних відповідей: " + trueQuest +
                        "\n" +
                        "Неправильних відповідей: " + falseQuest)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getActivity(), SettingActivity.class);
                        startActivity(intent);
                    }
                })
                .create();
    }


}
