package com.salarium.bundy.view;

import android.app.Activity;
import android.view.View;

public class DecisionExpiringMessageBoardV2
    extends MessageBoardExpiringV2 {

    public DecisionExpiringMessageBoardV2(
        Activity activity,
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener,
        int toBeAccepted,
        int secondsToExpire) {

        super(activity, icon, headerMessage, message, onClickListener);
        if (toBeAccepted == positiveWhat) {
            setPositiveButton(positiveButtonLabel, positiveWhat, true);
            setNegativeButton(negativeButtonLabel, negativeWhat, false);
        } else {
            setPositiveButton(positiveButtonLabel, positiveWhat, false);
            setNegativeButton(negativeButtonLabel, negativeWhat, true);
        }
        runCountdown(secondsToExpire);
    }
}
