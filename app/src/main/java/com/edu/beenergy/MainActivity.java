package com.edu.beenergy;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.edu.beenergy.listener.PowerConnectionReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PingController controller = new PingController(this);
        getApplicationContext().registerReceiver(new PowerConnectionReceiver(controller), new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        getApplicationContext().registerReceiver(new PowerConnectionReceiver(controller), new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        runPinger(controller);
    }

    private void runPinger(PingController controller) {
        TextView output = findViewById(R.id.ping_output);
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        EditText delay = findViewById(R.id.delay);
                        Thread.sleep(Integer.parseInt(delay.getText().toString()) * 1000);
                        String respCode = controller.doPing();
                        runOnUiThread(() -> output.setText("Response Code:" + respCode + ", Time:" + Calendar.getInstance().getTime()));
                    } catch (Exception e) {
                        System.out.println("Error occurred" + e);
                        runOnUiThread(() -> output.setText("Error occurred"));
                    }
                }
            }
        }.start();

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(57 * 1000);
                        controller.doCheckin();
                    } catch (Exception e) {
                        System.out.println("Error occurred" + e);
                        runOnUiThread(() -> output.setText("Error occurred"));
                    }
                }
            }
        }.start();


    }
}