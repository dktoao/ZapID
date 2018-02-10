package tech.zapid.zapid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        boolean isValid = intent.getBooleanExtra(MainActivity.VALIDATION_RESULT, false);
        String message = intent.getStringExtra(MainActivity.VALIDATION_MESSAGE);

        if (isValid) {
            setContentView(R.layout.valid_screen);
            TextView tv = findViewById(R.id.textContents);
            tv.setText(message);
        } else {
            setContentView(R.layout.invalid_screen);
        }
    }
}
