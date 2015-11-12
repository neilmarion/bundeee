package com.salarium.bundy.view;

import com.salarium.bundy.R;
import android.content.Context;

import android.app.AlertDialog;
import android.widget.Button;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.app.Activity;

public class NonExpiringDismissDialog extends BundyAlertDialog {
    NonExpiringDismissDialog(Activity activity, int state) {
        super(activity, state);
        setup(activity);
    }

    NonExpiringDismissDialog(Activity activity) {
        super(activity);
        setup(activity);
    }

    private void setup(Activity activity) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        LayoutInflater inflater =
            (LayoutInflater) activity.getApplicationContext().
                getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(
            R.layout.bundy_alert_dialog, null);
        setView(view);
    }

    protected void setPositiveButton() { // TODO: put this in superclass
        Button positiveButton = getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#ffffff"));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        positiveButton.
            setBackgroundResource(R.drawable.action_button);
        positiveButton.setPadding(20, 20, 20, 20);
    }

    protected void setNegativeButton() { // TODO: put this in superclass
        Button negativeButton = getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#ffffff"));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        negativeButton.
            setBackgroundResource(R.drawable.negative_action_button);
        negativeButton.setPadding(20, 20, 20, 20);
    }
}
