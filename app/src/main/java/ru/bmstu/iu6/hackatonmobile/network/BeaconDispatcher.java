package ru.bmstu.iu6.hackatonmobile.network;

import android.content.Context;

/**
 * Created by mikrut on 05.12.15.
 */
public class BeaconDispatcher {
    Context context;

    public BeaconDispatcher(Context context) {
        this.context = context;
    }

    public void dispatchBeacon(String UUID, String major, String minor) {
        switch(major) {

        }
    }
}
