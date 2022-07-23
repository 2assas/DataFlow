package com.example.dataflow.utils;

import android.annotation.SuppressLint;
import android.os.Build;

import java.lang.reflect.Method;

public class Device {
    /**
     * @return The device's serial number, visible to the user in {@code Settings > About phone/tablet/device > Status
     * > Serial number}, or {@code null} if the serial number couldn't be found
     */
    public static String getSerialNumber() {
        String serialNumber;

        try {
            @SuppressLint("PrivateApi") Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serialNumber = (String) get.invoke(c, "gsm.sn1");

            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");

            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");

            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");

            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;
            if (serialNumber.equals("unknown")){
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        serialNumber = Build.getSerial();
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            if (serialNumber.equals(Build.UNKNOWN))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }
}
