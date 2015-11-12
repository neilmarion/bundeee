package com.salarium.bundy.view;

import android.app.Activity;
import android.view.View;

public class DecisionMessageBoardV2
    extends MessageBoardV2 {

    public DecisionMessageBoardV2
        (Activity activity,
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener) {

        super(activity, icon, headerMessage, message, onClickListener);
        setPositiveButton(positiveButtonLabel, positiveWhat);
        setNegativeButton(negativeButtonLabel, negativeWhat);
    }
}
