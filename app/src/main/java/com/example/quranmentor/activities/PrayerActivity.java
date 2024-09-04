package com.example.quranmentor.activities;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.quranmentor.Adapters.NamazTimingsAdapter;
import com.example.quranmentor.R;
import com.example.quranmentor.models.NamazTimings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrayerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NamazTimingsAdapter adapter;
    private List<NamazTimings> namazTimingsList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);
        recyclerView = findViewById(R.id.namaztimingRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NamazTimingsAdapter(this);
        recyclerView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {

        long millis = Calendar.getInstance().getTimeInMillis();
        String url = "https://api.aladhan.com/v1/calendarByCity?city=Lahore&country=Pakistan&method=2&month="+convertDate(millis,"MM")+
                "&year="+convertDate(millis , "yyyy");
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject jsonObject) {

               try {
                   adapter.clear();
                   String status =jsonObject.getString("status");
                   if (status.equals("OK")){
                       JSONArray dataArray = jsonObject.getJSONArray("data");
                       for (int i = 0; i<dataArray.length();i++){
                           JSONObject timingobject = dataArray.getJSONObject(i).getJSONObject("timings");
                           JSONObject dtaeobject = dataArray.getJSONObject(i).getJSONObject("date");

                           String fajr =timingobject.getString("Fajr");
                           String dhaur =timingobject.getString("Dhuhr");
                           String asr =timingobject.getString("Asr");
                           String maghrib =timingobject.getString("Maghrib");
                           String isha =timingobject.getString("Isha");
                           String date =dtaeobject.getString("readable");
                           NamazTimings namazTimings = new NamazTimings(fajr , dhaur , asr , maghrib , isha , date);
                           adapter.add(namazTimings);
                       }
                       adapter.notifyDataSetChanged();

                   }
               } catch (JSONException e) {
                   Toast.makeText(PrayerActivity.this, "Error" +e.getMessage(), Toast.LENGTH_SHORT).show();
                   throw new RuntimeException(e);
               }
           }
       },new Response.ErrorListener(){
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(PrayerActivity.this, "Error is:"+error.getMessage(), Toast.LENGTH_SHORT).show();
           }
    });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    public static String convertDate(long DatemiliSeconds , String dateformat){
        return DateFormat.format(dateformat , (DatemiliSeconds)).toString();
    }
}