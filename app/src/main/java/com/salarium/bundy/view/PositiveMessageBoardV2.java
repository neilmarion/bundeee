package com.salarium.bundy.view;

import android.app.Activity;
import android.view.View;

public class PositiveMessageBoardV2
    extends MessageBoardV2 {

    public PositiveMessageBoardV2
        (Activity activity,
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener) {

        super(activity, icon, headerMessage, message, onClickListener);
        setPositiveButton(positiveButtonLabel, what);
    }
}
