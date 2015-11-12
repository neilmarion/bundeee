package com.salarium.bundy.view;

import com.salarium.bundy.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleNumberInputViewDialog
    extends Dialog {
    private SimpleNumberInputView simpleNumberInputView;
    private String message;

    public SimpleNumberInputViewDialog(
        Activity activity,
        SimpleNumberInputViewCallback callback,
        String message) {

        super((Context) activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.message = message;
        simpleNumberInputView =
            new SimpleNumberInputView(activity, callback, this, message);
        ViewGroup.LayoutParams p =
            new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(simpleNumberInputView, p);
        setOnDismissListener(simpleNumberInputView);
    }

    public void setDisplayJammed() {
        simpleNumberInputView.setDisplayJammed();
    }

    public void setCancelable(boolean what) {
        setCancelable(what);
    }

    public void showDialog() {
        show();
    }
}
