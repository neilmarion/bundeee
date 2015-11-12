package com.salarium.bundy.view;

import android.view.View;
import android.view.ViewGroup;

public interface MessageReceiver {
    public static int POSITIVE = 1;
    public static int NEUTRAL = 0;
    public static int NEGATIVE = -1;

    public void receiveMessage (
        String icon,
        String headerMessage,
        String message);

    public void receivePositiveMessage (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener);

    public ViewGroup receiveExpiringPositiveMessage (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire
    );

    public void receiveNegativeMessage (
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener
    );

    public ViewGroup receiveExpiringNegativeMessage (
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire
    );

    public void receiveDecisionMessage (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener
    );

    public ViewGroup receiveExpiringDecisionMessageWithNegativeAsAccepted (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener,
        int secondsToExpire
    );

    public ViewGroup receiveExpiringDecisionMessageWithPositiveAsAccepted (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener,
        int secondsToExpire
    );
}
