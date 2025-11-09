package ro.pub.cs.systems.eim.test02;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MyService extends Service {

    private  int counter;

    // cele 3 acțiuni diferite pentru broadcast
    public static final String ACTION_1 = "com.example.examplepracticaltest01.ACTION_1";
    public static final String ACTION_2 = "com.example.examplepracticaltest01.ACTION_2";
    public static final String ACTION_3 = "com.example.examplepracticaltest01.ACTION_3";

    // intervalul de 10 secunde (în ms)
    public static final long INTERVAL_MS = 2_000L;

    // un Random ca să alegem una din cele 3 acțiuni
    private final Random random = new Random();

    private Handler handler;
    private Runnable broadcastRunnable;
    private boolean isRunning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // e un service "started", nu "bound", deci returnăm null
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // putem fi porniți din nou cu alte numere
        if (intent != null) {
            counter = intent.getIntExtra("COUNTER", 0);
        }

        // pornim runnable-ul doar o dată
        if (!isRunning) {
            isRunning = true;
            handler = new Handler();
            broadcastRunnable = new Runnable() {
                @Override
                public void run() {
                    sendRandomBroadcast();
                    // reprogramăm peste 10 secunde
                    handler.postDelayed(this, INTERVAL_MS);
                }
            };
            // pornim prima execuție imediat
            handler.post(broadcastRunnable);
        }

        // dacă service-ul e ucis, să fie repornit cu ultimul intent
        return START_REDELIVER_INTENT;
    }

    private void sendRandomBroadcast() {
        // 1. alegem acțiunea
        String action;
        int choice = random.nextInt(3); // 0,1,2
        if (choice == 0) {
            action = ACTION_1;
        } else if (choice == 1) {
            action = ACTION_2;
        } else {
            action = ACTION_3;
        }


        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // 4. construim intentul de broadcast
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra("timestamp", timestamp);
        broadcastIntent.putExtra("counter", counter);

        // 👇 ADĂUGĂ ASTA
        broadcastIntent.setPackage(getApplicationContext().getPackageName());

        // 5. îl trimitem
        sendBroadcast(broadcastIntent);

        // 🧩 DEBUG: mesaj vizual pe ecran (Toast)
//        android.widget.Toast.makeText(
//                getApplicationContext(),
//                "Broadcast sent!\n" +
//                        "Action: " + action + "\n" +
//                        "Arith: " + arithmeticMean + "  Geom: " + geometricMean,
//                android.widget.Toast.LENGTH_LONG
//        ).show();

        // 🧩 DEBUG: mesaj și în logcat
//        android.util.Log.d("PracticalTest01Service", "Broadcast trimis cu actiunea: " + action);
    }

    @Override
    public void onDestroy() {
        // oprim runnable-ul ca să nu mai trimită mesaje
        if (handler != null && broadcastRunnable != null) {
            handler.removeCallbacks(broadcastRunnable);
        }
        isRunning = false;
        super.onDestroy();
    }
}