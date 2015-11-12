package com.salarium.bundy.view;

import com.salarium.bundy.R;

import android.view.View;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.IconTextView;

public class NegativeNonExpiringDismissDialog extends NonExpiringDismissDialog {
    private String message;
    private String icon;

    public NegativeNonExpiringDismissDialog(
        Activity activity,
        String negativeButtonLabel,
        String message,
        String icon,
        int state,
        DialogInterface.OnClickListener onClickListener) {

        super(activity, state);

        setButton(AlertDialog.BUTTON_NEGATIVE,
            negativeButtonLabel,
            onClickListener);

        this.message = message;
        this.icon = icon;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIcon(String message) {
        this.icon = icon;
    }

    public void showDialog() {
        show();
        styleButton();
    }

    private void styleButton() {
        setNegativeButton();

        TextView messageTextView =
            (TextView)
                findViewById(R.id.bundy_alert_dialog_message);
        messageTextView.setText(message);

        IconTextView iconTextView =
            (IconTextView)
                findViewById(R.id.bundy_alert_dialog_icon);
        iconTextView.setText(icon, TextView.BufferType.NORMAL);
    }


}
