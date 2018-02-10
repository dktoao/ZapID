package tech.zapid.zapid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import tech.zapid.zaputil.InvalidIDCodeException;
import tech.zapid.zaputil.Util;

public class MainActivity extends AppCompatActivity {


    // Used to load the 'native-lib' library on application startup.
    //static {
    //    System.loadLibrary("native-lib");
    //}

    public static final String VALIDATION_MESSAGE = "VALIDATION_MESSAGE";
    public static final String VALIDATION_RESULT = "VALIDATION_RESULT";

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
                String message = "";
                boolean isValid;
                try {
                    message = Util.validateQRCode(Util.stringToByte(result.getContents()));
                    isValid = true;
                } catch (InvalidIDCodeException x) {
                    message = "";
                    isValid = false;
                }
                Intent intent = new Intent(this, DisplayActivity.class);
                intent.putExtra(VALIDATION_MESSAGE, message);
                intent.putExtra(VALIDATION_RESULT, isValid);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addKeyWord(View view) {

        Toast.makeText(this, "Function not available", Toast.LENGTH_LONG).show();
    }

    public void addKeyCode(View view) {

        Toast.makeText(this, "Function not available", Toast.LENGTH_LONG).show();
    }

    ///**
    // * A native method that is implemented by the 'native-lib' native library,
    // * which is packaged with this application.
    // */
    //public native String stringFromJNI();
}
