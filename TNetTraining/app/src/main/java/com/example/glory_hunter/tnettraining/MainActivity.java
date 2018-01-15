package com.example.glory_hunter.tnettraining;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String MY_PREFS_NAME = "language";
    private Button bt1, btMap, btLanguage;
    private Spinner spLanguage;
    private Locale locale;
    private static String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Select language");
        listSpinner.add("English");
        listSpinner.add("Deutsch");
        listSpinner.add("Viet Nam");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                listSpinner
        );

        spLanguage.setAdapter(adapter);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("spinner", "onItemSelected: " + i);


                switch (i){
                    case 1:
                        setLocale("en");
                        language = "en";
                        break;
                    case 2:
                        setLocale("de");
                        language = "de";
                        break;
                    case 3:
                        setLocale("vi");
                        language = "vi";
                        break;

                        default: break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onclick", "onClick: " + language);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("language", language);
                editor.apply();
            }
        });

    }

    private void setupUI() {
        bt1 = findViewById(R.id.bt_bt1);
        btMap = findViewById(R.id.bt_map);
        btLanguage = findViewById(R.id.bt_language);
        spLanguage = findViewById(R.id.sp_language);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setLocale(String lang) {

        locale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }
}
