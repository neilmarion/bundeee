package com.salarium.bundy.view;

import com.salarium.bundy.R;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MessageBoardManagerV2
    implements MessageReceiver {

    private ViewGroup mainRight;
    private LinearLayout currentMessageBoard;
    private LinearLayout.LayoutParams layoutParams;
    private Activity activity;

    public MessageBoardManagerV2(Activity activity) {
        this.activity = activity;
        mainRight = (ViewGroup) activity.findViewById(R.id.main_right);
    }

    public void receiveMessage(
        String icon,
        String headerMessage,
        String message) {

        removeLastMessageBoard();
        createMessageBoard(icon, headerMessage, message);
    }

    public void receivePositiveMessage (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener) {

        removeLastMessageBoard();
        createPositiveMessageBoard(
            icon, headerMessage, message, positiveButtonLabel, what, onClickListener);
    }

    public void receiveNegativeMessage (
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener) {

        removeLastMessageBoard();
        createNegativeMessageBoard(
            icon, headerMessage, message, negativeButtonLabel, what, onClickListener);
    }

    public void receiveDecisionMessage (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener) {

        removeLastMessageBoard();
        createDecisionMessageBoard(
            icon,
            headerMessage,
            message,
            positiveButtonLabel,
            negativeButtonLabel,
            positiveWhat,
            negativeWhat,
            onClickListener);
    }

    public ViewGroup receiveExpiringPositiveMessage (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        removeLastMessageBoard();
        return createExpiringPositiveMessageBoard(
            icon,
            headerMessage,
            message,
            positiveButtonLabel,
            what,
            onClickListener,
            secondsToExpire
        );
    }

    public ViewGroup receiveExpiringNegativeMessage (
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        removeLastMessageBoard();
        return createExpiringNegativeMessageBoard(
            icon,
            headerMessage,
            message,
            negativeButtonLabel,
            what,
            onClickListener,
            secondsToExpire
        );
    }

    public ViewGroup receiveExpiringDecisionMessageWithPositiveAsAccepted (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        removeLastMessageBoard();
        return createExpiringDecisionMessageBoard(
            icon,
            headerMessage,
            message,
            positiveButtonLabel,
            negativeButtonLabel,
            positiveWhat,
            negativeWhat,
            onClickListener,
            positiveWhat,
            secondsToExpire
        );
    }

    public ViewGroup receiveExpiringDecisionMessageWithNegativeAsAccepted (
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        removeLastMessageBoard();
        return createExpiringDecisionMessageBoard(
            icon,
            headerMessage,
            message,
            positiveButtonLabel,
            negativeButtonLabel,
            positiveWhat,
            negativeWhat,
            onClickListener,
            negativeWhat,
            secondsToExpire
        );
    }

    private void createMessageBoard(
        String icon,
        String headerMessage,
        String message) {

        currentMessageBoard =
            new MessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                null);

        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
    }

    private void createPositiveMessageBoard(
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener) {

        currentMessageBoard =
            new PositiveMessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                positiveButtonLabel,
                what,
                onClickListener);

        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
    }

    private ViewGroup createExpiringPositiveMessageBoard(
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        currentMessageBoard =
            new PositiveExpiringMessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                positiveButtonLabel,
                what,
                onClickListener,
                secondsToExpire);

        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
        return currentMessageBoard;
    }

    private void createNegativeMessageBoard(
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener) {

        currentMessageBoard =
            new NegativeMessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                negativeButtonLabel,
                what,
                onClickListener);

        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
    }

    private ViewGroup createExpiringNegativeMessageBoard(
        String icon,
        String headerMessage,
        String message,
        String negativeButtonLabel,
        int what,
        View.OnClickListener onClickListener,
        int secondsToExpire) {

        currentMessageBoard =
            new NegativeExpiringMessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                negativeButtonLabel,
                what,
                onClickListener,
                secondsToExpire);

        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
        return currentMessageBoard;
    }

    private void createDecisionMessageBoard(
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener) {

        currentMessageBoard =
            new DecisionMessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                positiveButtonLabel,
                negativeButtonLabel,
                positiveWhat,
                negativeWhat,
                onClickListener);

        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
    }

    private ViewGroup createExpiringDecisionMessageBoard(
        String icon,
        String headerMessage,
        String message,
        String positiveButtonLabel,
        String negativeButtonLabel,
        int positiveWhat,
        int negativeWhat,
        View.OnClickListener onClickListener,
        int toBeAccepted,
        int secondsToExpire
    ) {
        currentMessageBoard =
            new DecisionExpiringMessageBoardV2(
                activity,
                icon,
                headerMessage,
                message,
                positiveButtonLabel,
                negativeButtonLabel,
                positiveWhat,
                negativeWhat,
                onClickListener,
                toBeAccepted,
                secondsToExpire);
        setLayoutParams();
        mainRight.addView(currentMessageBoard, layoutParams);
        return currentMessageBoard;
    }

    private void setLayoutParams() {
        layoutParams =
            new LinearLayout.LayoutParams(
            600,
            600);
        layoutParams.gravity = Gravity.CENTER;
        currentMessageBoard.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void removeLastMessageBoard() {
        mainRight.removeView(currentMessageBoard);
    }
}
