package tech.zapid.zapid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void verify(View view) {

        textView = findViewById(R.id.mainScreenText);
        textView.setText("verify Called!");
    }

    public void addKeyWord(View view) {

        textView = findViewById(R.id.mainScreenText);
        textView.setText("addKeyWord Called!");
    }

    public void addKeyCode(View view) {
        textView = findViewById(R.id.mainScreenText);
        textView.setText("addKeyCode Called!");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
