package com.edu.beenergy;

import android.widget.Button;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class PingController {

    private final Button pingButton;
    private final EditText urlText;
    private final EditText prefireText;
    private volatile boolean powerConnected = false;

    private final OkHttpClient httpClient;


    volatile public boolean pingEnabled = false;

    public PingController(MainActivity mainActivity) {
        pingButton = mainActivity.findViewById(R.id.ping_button);
        this.pingButton.setOnClickListener(view -> {
            if (pingEnabled) {
                this.pingButton.setText("Start Ping");
            } else {
                this.pingButton.setText("Stop Ping");
            }
            this.pingEnabled = !pingEnabled;
        });
        this.urlText = mainActivity.findViewById(R.id.url_to_ping);
        this.prefireText = mainActivity.findViewById(R.id.url_to_prefire);
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }


    public String doPing() {
        if (!pingEnabled) {
            return "Ping is not enabled";
        }
        if (!powerConnected) {
            return "Power is not connected";
        }
        String uri = urlText.getText().toString();
        String prefire = prefireText.getText().toString();
        String result = executeHttpCall(uri);
        executeHttpCall(prefire);
        return result;
    }


    private String executeHttpCall(String uri) {
        try {
            Request request = new Request.Builder()
                    .url(uri)
                    .build();
            Call call = httpClient.newCall(request);
            Response response = call.execute();
            return String.valueOf(response.code());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void markPowerConnected(boolean isConnected) {
        this.powerConnected = isConnected;
    }
}
