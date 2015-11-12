package com.salarium.bundy.view;

import com.salarium.bundy.R;

import android.app.Activity;
import android.widget.Button;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.IconTextView;
import android.widget.TextView;

public class MessageBoardExpiringV2
    extends LinearLayout {

    private int secondsToExpire = 3;
    private Activity activity;
    private TextView contentText;
    private TextView headerText;
    private IconTextView headerIcon;
    private boolean positiveButtonToBeAccepted = false;
    private boolean negativeButtonToBeAccepted = false;
    private Button positiveButton;
    private Button negativeButton;
    private String positiveButtonLabel;
    private String negativeButtonLabel;
    private int secondsRemaining = secondsToExpire;
    private boolean stopCountdown = false;
    protected View.OnClickListener onClickListener;
    protected ViewGroup messageBoardButtons;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            doCountdown();
        }
    };
    private String m;

    public MessageBoardExpiringV2
        (Activity activity,
        String icon,
        String headerMessage,
        String message,
        View.OnClickListener onClickListener) {

        super((Context) activity);
        this.activity = activity;
        this.onClickListener = onClickListener;

        LayoutInflater inflater =
            (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout =
            (LinearLayout) inflater.inflate(R.layout.message_board, null);
        setup(layout, icon, headerMessage, message);
        m = headerMessage;
    }

    private void setup(
        LinearLayout layout, String icon, String headerMessage, String message) {

        addView(layout);
        messageBoardButtons = (ViewGroup) findViewById(R.id.message_board_buttons);
        headerIcon =
            (IconTextView) findViewById(R.id.message_board_header_icon);
        headerText =
            (TextView) findViewById(R.id.message_board_header_text);
        contentText =
            (TextView) findViewById(R.id.message_board_content_text);
        headerIcon.setText(icon);
        headerText.setText(headerMessage);
        contentText.setText(message);
    }

    protected void setPositiveButton(
        String positiveButtonLabel,
        int what,
        boolean toBeAccepted
    ) {
        this.positiveButtonLabel = positiveButtonLabel;
        positiveButton = new Button(activity);
        positiveButton.setTextColor(Color.parseColor("#ffffff"));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        positiveButton.
            setBackgroundResource(R.drawable.action_button);
        positiveButton.setPadding(20, 20, 20, 20);
        positiveButton.setOnClickListener(onClickListener);
        positiveButton.setTag(what);
        positiveButton.setText(positiveButtonLabel);
        messageBoardButtons.addView(positiveButton);
        if (toBeAccepted) {
            setPositiveButtonToBeAccepted();
        }
        //positiveButton.setEnabled(false);//temporary
    }

    protected void setNegativeButton(
        String negativeButtonLabel,
        int what,
        boolean toBeAccepted) {
        this.negativeButtonLabel = negativeButtonLabel;
        negativeButton = new Button(activity);
        negativeButton.setTextColor(Color.parseColor("#ffffff"));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        negativeButton.
            setBackgroundResource(R.drawable.negative_action_button);
        negativeButton.setPadding(20, 20, 20, 20);
        negativeButton.setOnClickListener(onClickListener);
        negativeButton.setTag(what);
        negativeButton.setText(negativeButtonLabel);
        messageBoardButtons.addView(negativeButton); if (toBeAccepted) {
            setNegativeButtonToBeAccepted();
        }
        //negativeButton.setEnabled(false);//temporary
    }

    private void setPositiveButtonToBeAccepted() {
        positiveButtonToBeAccepted = true;
        negativeButtonToBeAccepted = false;
    }

    private void setNegativeButtonToBeAccepted() {
        positiveButtonToBeAccepted = false;
        negativeButtonToBeAccepted = true;
    }

    private void doCountdown() {
        if (secondsRemaining > 0) {
            showCountdown();
            secondsRemaining--;
            handler.postDelayed(runnable, 1000);
        } else {
            if (!stopCountdown) {
                if (positiveButtonToBeAccepted) {
                    System.out.println("clicked positive");
                    onClickListener.onClick(positiveButton);
                } else {
                    System.out.println("clicked negative");
                    System.out.println(m);
                    onClickListener.onClick(negativeButton);
                }
            }
        }
    }

    private void showCountdown() {
        if (positiveButtonToBeAccepted) {
            positiveButton.setText(positiveButtonLabel + " (" + secondsRemaining + ") ");
        } else {
            negativeButton.setText(negativeButtonLabel + " (" + secondsRemaining + ") ");
        }
    }

    protected void runCountdown(int secondsToExpire) {
        this.secondsRemaining = secondsToExpire;
        runnable.run();
    }

    public void stopCountdown() {
        System.out.println("stop countdown = true");
        this.stopCountdown = true;
    }
}
