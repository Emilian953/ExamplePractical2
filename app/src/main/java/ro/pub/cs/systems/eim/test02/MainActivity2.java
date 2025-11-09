package ro.pub.cs.systems.eim.test02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private Button register, cancel;
    private EditText direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity2_test02_main);

        register = findViewById(R.id.register);
        cancel = findViewById(R.id.cancel);

        direction = findViewById(R.id.direction);

        String result = getIntent().getStringExtra("DIRECTIONS_KEY");   // parinte -> copil
        direction.setText(result);

        Intent intentToParent = new Intent(); // copil -> parinte

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                intentToParent.putExtra("RESULT_MSG", "Registered");
                setResult(RESULT_OK, intentToParent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                intentToParent.putExtra("RESULT_MSG", "Cancel");
                setResult(RESULT_CANCELED, intentToParent);
                finish();
            }
        });
    }
}