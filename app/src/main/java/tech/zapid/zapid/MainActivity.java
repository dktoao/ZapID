package tech.zapid.zapid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    // Used to load the 'native-lib' library on application startup.
    //static {
    //    System.loadLibrary("native-lib");
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void verify(View view) {

        IntentIntegrator scanner = new IntentIntegrator(this);
        scanner.setBeepEnabled(false);
        scanner.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addKeyWord(View view) {

        textView = findViewById(R.id.mainScreenText);
        textView.setText("addKeyWord Called!");
    }

    public void addKeyCode(View view) {
        textView = findViewById(R.id.mainScreenText);
        textView.setText("addKeyCode Called!");
    }

    ///**
    // * A native method that is implemented by the 'native-lib' native library,
    // * which is packaged with this application.
    // */
    //public native String stringFromJNI();
}
