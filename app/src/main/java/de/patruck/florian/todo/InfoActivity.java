package de.patruck.florian.todo;

import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private Button button;
    private TextView textViewVersionNumber;
    private int versionNumber;
    private final String VERSION_NUMBER_KEY = "versionNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        versionNumber = sharedPreferences.getInt(VERSION_NUMBER_KEY, 1);

        textViewVersionNumber = (TextView) findViewById(R.id.tv_versionNumber);
        textViewVersionNumber.setText(String.valueOf((float)versionNumber));

        button = (Button) findViewById(R.id.btn_version_inc);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++versionNumber;
                textViewVersionNumber.setText(String.valueOf((float)versionNumber));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VERSION_NUMBER_KEY, versionNumber);
        editor.apply();
    }
}
