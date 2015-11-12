package com.salarium.bundy.view;

import com.salarium.bundy.R;
import com.salarium.bundy.view.DemoDialogViewCallback;
import android.view.View.OnClickListener;
import android.view.View;

import android.view.Window;
import android.app.Dialog;
import android.widget.Button;
import android.app.Activity;

public class DemoDialogView {
    private final Activity activity;
    private final Dialog dialog;
    private final Button confirmButton;

    public DemoDialogView
        (Activity activity,
        DemoDialogViewCallback callback) {

        this.activity = activity;

        this.dialog = new Dialog(activity);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(R.layout.demo_dialog);

        confirmButton = (Button) this.dialog.
            findViewById(R.id.demo_dialog_enter_btn);

        final DemoDialogViewCallback cb = callback;
        final Dialog d = this.dialog;

        confirmButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cb.proceed();
                d.cancel();
            }

        });
    }

    public void show() {
        this.dialog.show();
    }
}
