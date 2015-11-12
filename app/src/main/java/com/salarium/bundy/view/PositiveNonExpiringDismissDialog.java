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

public class PositiveNonExpiringDismissDialog extends NonExpiringDismissDialog {
    private String message;
    private String icon;

    public PositiveNonExpiringDismissDialog(
        Activity activity,
        String positiveButtonLabel,
        String message,
        String icon,
        int state,
        DialogInterface.OnClickListener onClickListener) {

        super(activity, state);
        setup(message, icon, positiveButtonLabel, onClickListener);
    }

    public PositiveNonExpiringDismissDialog(
        Activity activity,
        String positiveButtonLabel,
        String message,
        String icon) {

        super(activity);
        setup(message, icon, positiveButtonLabel, null);
    }

    private void setup(
        String message,
        String icon,
        String positiveButtonLabel,
        DialogInterface.OnClickListener onClickListener) {

        setButton(AlertDialog.BUTTON_POSITIVE,
            positiveButtonLabel,
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
        setPositiveButton();

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
