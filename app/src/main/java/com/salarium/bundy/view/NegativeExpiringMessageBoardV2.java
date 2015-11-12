package com.salarium.bundy.view;

import android.app.Activity;
import android.view.View;

public class NegativeExpiringMessageBoardV2
    extends MessageBoardExpiringV2 {

    public NegativeExpiringMessageBoardV2
        (Activity activity,
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        super(activity, icon, headerMessage, message, onClickListener);
        setNegativeButton(negativeButtonLabel, what, true);
        runCountdown(secondsToExpire);
    }
}
