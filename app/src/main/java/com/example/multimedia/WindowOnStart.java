package com.example.multimedia;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WindowOnStart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_on_start);
    }

    public void openKotlin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Button button = (Button) findViewById(R.id.button_kotlin);
        startActivity(intent);
    }

    public void openUnity(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.unity.arfoundation.samples");
        startActivity(intent);
    }
}