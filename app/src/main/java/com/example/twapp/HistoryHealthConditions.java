package com.example.twapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HistoryHealthConditions extends AppCompatActivity {
    ListView list_HistoryHealthConditions;
    OkHttpClient client = new OkHttpClient();
    List<String> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_health_conditions);
        list_HistoryHealthConditions = (ListView) findViewById(R.id.list_HistoryHealthConditions);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        list_HistoryHealthConditions.setAdapter(adapter);

        class getHistoryHealthConditionsTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... Void) {
                Request request = new Request.Builder()
                        .url("https://g8.minouo.eu.org/Condition/get/4")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.code() == 200) {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j =  jsonArray.getJSONObject(i);
                            values.add(String.format("Hearth Rhythm：%s     blood oxygen：%s \nElder State：%s",
                                    j.getString("heartrhythm"),
                                    j.getString("bloodyoxy"),
                                    j.getString("elder_state")

                            ));

                        }
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                ((ArrayAdapter<?>) adapter).notifyDataSetChanged();
            }
        }
        new getHistoryHealthConditionsTask().execute();

    }
}