package com.salarium.bundy.view;

import android.app.Activity;
import android.view.View;

public class PositiveExpiringMessageBoardV2
    extends MessageBoardExpiringV2 {

    public PositiveExpiringMessageBoardV2
        (Activity activity,
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        super(activity, icon, headerMessage, message, onClickListener);
        setPositiveButton(positiveButtonLabel, what, true);
        runCountdown(secondsToExpire);
    }
}
