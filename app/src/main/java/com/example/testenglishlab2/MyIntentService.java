package com.example.testenglishlab2;

import android.app.IntentService;
import android.content.Intent;
import android.nfc.Tag;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MyIntentService extends IntentService {

    public static final String ACTION_RESPONSE_WORLD = BuildConfig.APPLICATION_ID + ".RESPONSE";
    public static final String EXTRA_MASS_QUEST = "EXTRA_MASS_QUEST";
    public static final String EXTRA_MASS_ANSWER = "EXTRA_MASS_ANSWER";

    public static String TAG = "IntentService";

    String[][] massQuest;
    String[][] massAnswer;


    public MyIntentService() {
        super(MyIntentService.class.getName());
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Start");

        assert intent != null;
        String action = intent.getAction();
        if (ACTION_RESPONSE_WORLD.equals(action)) {

            Log.d(TAG, Thread.currentThread().getName() + " " + Thread.currentThread().getId());
            HandlerThread handlerThread = new HandlerThread("CustomThread") {
                @Override
                public void run() {
                    Log.d(TAG, Thread.currentThread().getName() + " " + Thread.currentThread().getId());

                    String s = loadJSONFromAsset();

                    massWriteQuest(s);
                    massWriteAnswer(s);

                    Intent responseIntent = new Intent();
                    responseIntent.setAction(ACTION_RESPONSE_WORLD);
                    responseIntent.putExtra(EXTRA_MASS_QUEST, massQuest);
                    responseIntent.putExtra(EXTRA_MASS_ANSWER, massAnswer);

                    sendBroadcast(responseIntent);
                }
            };
            handlerThread.start();
        }

    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getApplicationContext().getAssets().open("Arrays.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void massWriteQuest(String s) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("massQuest");

            massQuest = new String[jsonArray.length()][jsonArray.getJSONArray(0).length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonObjectMass = jsonArray.getJSONArray(i);
                for (int a = 0; a < jsonObjectMass.length(); a++) {
                    massQuest[i][a] = jsonObjectMass.get(a).toString();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void massWriteAnswer(String s) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("massAnswer");

            massAnswer = new String[jsonArray.length()][jsonArray.getJSONArray(0).length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonObjectMass = jsonArray.getJSONArray(i);
                for (int a = 0; a < jsonObjectMass.length(); a++) {
                    massAnswer[i][a] = jsonObjectMass.get(a).toString();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
