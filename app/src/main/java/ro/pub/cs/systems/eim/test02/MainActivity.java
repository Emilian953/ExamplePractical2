package ro.pub.cs.systems.eim.test02;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button nord, sud, est, vest, secondActivity;
    private TextView direction, count;

    private static final int SECONDARY_ACTIVITY_REQUEST_CODE = 1;
    private boolean serviceStarted = false;
    private android.content.BroadcastReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test02_main);

        nord = findViewById(R.id.nord);
        sud = findViewById(R.id.sud);
        est = findViewById(R.id.est);
        vest = findViewById(R.id.vest);

        direction = findViewById(R.id.direction);
        count = findViewById(R.id.count);
        secondActivity = findViewById(R.id.secondActivity);

        nord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String result = direction.getText().toString();
                result += "Nord ";
                direction.setText(result);

                int counter = Integer.parseInt(count.getText().toString()) + 1;
                count.setText(String.valueOf(counter));

                checkAndStartService();
            }
        });

        sud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String result = direction.getText().toString();
                result += "Sud ";
                direction.setText(result);

                int counter = Integer.parseInt(count.getText().toString()) + 1;
                count.setText(String.valueOf(counter));

                checkAndStartService();
            }
        });

        est.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String result = direction.getText().toString();
                result += "Est ";
                direction.setText(result);

                int counter = Integer.parseInt(count.getText().toString()) + 1;
                count.setText(String.valueOf(counter));

                checkAndStartService();
            }
        });

        vest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String result = direction.getText().toString();
                result += "Vest ";
                direction.setText(result);

                int counter = Integer.parseInt(count.getText().toString()) + 1;
                count.setText(String.valueOf(counter));

                checkAndStartService();
            }
        });

        secondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,
                        MainActivity2.class);

                intent.putExtra("DIRECTIONS_KEY", direction.getText());

                startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
            }
        });

        messageReceiver = new android.content.BroadcastReceiver() {
            @Override
            public void onReceive(android.content.Context context, Intent intent) {
                if (intent == null || intent.getAction() == null) return;

                String action = intent.getAction();
                String timestamp = intent.getStringExtra("timestamp");
                int counter = intent.getIntExtra("counter", 0);

                // doar ca să vezi de unde a venit
                String msg = action + "\n" +
                        "time: " + timestamp + "\n" +
                        "counter: " + counter;

                android.util.Log.d("Test02", msg);

                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        };


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putCharSequence("KEY_COUNT", count.getText());
        savedInstanceState.putCharSequence("KEY_DIRECTION", direction.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        CharSequence keyLeft = savedInstanceState.getCharSequence("KEY_COUNT");
        CharSequence keyRight = savedInstanceState.getCharSequence("KEY_DIRECTION");

        count.setText(keyLeft);
        direction.setText(keyRight);
    }

    @Override
    @Deprecated
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "REGISTER", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
            }

            direction.setText("");
            count.setText("0");
        }
    }


    private void checkAndStartService() {
        int counter = Integer.parseInt(count.getText().toString());

        // folosim pragul definit în service
        if (counter > 4 && !serviceStarted) {
            Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
            // îi trimitem valorile actuale
            serviceIntent.putExtra("COUNTER", counter);
            startService(serviceIntent);
            serviceStarted = true;
            // doar ca feedback
            Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onResume() {
        super.onResume();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction(MyService.ACTION_1);
        filter.addAction(MyService.ACTION_2);
        filter.addAction(MyService.ACTION_3);
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(messageReceiver, filter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(messageReceiver, filter);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(getApplicationContext(), MyService.class));
        super.onDestroy();
    }

}