package com.salarium.bundy.view;

import android.app.Activity;
import android.view.View;

public class NegativeMessageBoardV2
    extends MessageBoardV2 {

    public NegativeMessageBoardV2
        (Activity activity,
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener) {

        super(activity, icon, headerMessage, message, onClickListener);
        setNegativeButton(negativeButtonLabel, what);
    }
}
