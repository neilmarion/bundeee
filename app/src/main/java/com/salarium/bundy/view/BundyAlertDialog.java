package com.salarium.bundy.view;

import com.salarium.bundy.R;
import android.view.View;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;

import android.view.LayoutInflater;
import android.app.AlertDialog;

public class BundyAlertDialog extends AlertDialog {
    private Activity activity;
    protected int state;

    public BundyAlertDialog(Activity activity, int state) {
        super(activity);
        this.state = state;
    }

    public BundyAlertDialog(Activity activity) {
        super(activity);
    }

    public int getState() {
        return state;
    }
}
