package com.example.menyimpandanmembacadata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.menyimpandanmembacadata.adapter.TemanAdapter;
import com.example.menyimpandanmembacadata.database.Teman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TemanAdapter adapter;
    private ArrayList<Teman> temanArrayList = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String url_select = "https://20200140143.praktikumtiumy.com/bacateman.php";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_TELPON = "telpon";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingBtn);

        BacaData();
        adapter = new TemanAdapter(temanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TambahTeman.class);
                startActivity(intent);
            }
        });
    }

    public void BacaData(){
        temanArrayList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Teman item = new Teman();
                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setTelpon(obj.getString(TAG_TELPON));

                        temanArrayList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "error: "+error.getMessage());
                error.printStackTrace();
                Toast.makeText(MainActivity.this,"gagal", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);
    }
}