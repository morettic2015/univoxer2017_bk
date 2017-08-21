package com.univoxer.android.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.univoxer.android.R;

/**
 * Created by Gustavo on 21/07/2016.
 * Util
 */
public class Util {

    public static int getFlagResource(int id) {
        switch (id) {
            case 1:
                return R.drawable.ic_flag_pt;
            case 2:
                return R.drawable.ic_flag_en;
            case 3:
                return R.drawable.ic_flag_es;
            case 4:
                return R.drawable.ic_flag_de;
            case 5:
                return R.drawable.ic_flag_fr;
            case 6:
                return R.drawable.ic_flag_cn;
            case 7:
                return R.drawable.ic_flag_it;
            case 8:
                return R.drawable.ic_flag_ru;
            default:
                return R.drawable.ic_flag_pt;
        }
    }

    public static String getTokenByIdLanguage(int id) {
        switch (id) {
            case 1:
                return "PT";
            case 2:
                return "EN";
            case 3:
                return "ES";
            case 4:
                return "DE";
            case 5:
                return "FR";
            case 6:
                return "CN";
            case 7:
                return "IT";
            case 8:
                return "RU";
            default:
                return "PT";
        }
    }

    public static void showAlert(String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static String formatTime(long seconds) {
        long ss = seconds % 60;
        seconds /= 60;
        long min = seconds % 60;
        seconds /= 60;
        long hh = seconds % 24;
        return concatZero(hh) + ":" + concatZero(min) + ":" + concatZero(ss);
    }

    private static String concatZero(long n)
    {
        if(n < 10)
            return "0" + String.valueOf(n);
        return String.valueOf(n);
    }
}