package com.edu.beenergy.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.edu.beenergy.PingController;

public class PowerConnectionReceiver extends BroadcastReceiver {

    private final PingController pingController;

    public PowerConnectionReceiver(PingController pingController) {
        this.pingController = pingController;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.ACTION_POWER_CONNECTED".equals(intent.getAction())) {
            pingController.markPowerConnected(true);
        } else {
            pingController.markPowerConnected(false);
        }
    }
}
