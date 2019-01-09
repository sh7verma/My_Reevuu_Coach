package com.myreevuuCoach.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.myreevuuCoach.R;

/**
 * Created by dev on 30/11/18.
 */

public class AlertDialogs {

    public static void tryAgainDialog(Context mContext, String buttonMess, String message,
                                      final DialogTryAgainClick tryAgainClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonMess, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tryAgainClick.tryAgain(dialog);
                    }
                });
        builder.create().show();
    }

    public static void confirmYesNoDialog(Context mContext, String message, final DialogClick dialogClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialogClick.yes(dialog);
                    }
                })
                .setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogClick.no(dialog);
                    }
                });
        builder.create().show();

    }

    public interface DialogClick {
        void yes(DialogInterface dialog);

        void no(DialogInterface dialog);
    }

    public interface DialogTryAgainClick {
        void tryAgain(DialogInterface dialog);
    }
}
