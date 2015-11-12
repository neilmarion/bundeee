package com.salarium.bundy.view;

import com.salarium.bundy.R;

import android.app.Activity;
import android.widget.Button;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.IconTextView;
import android.widget.TextView;

public class MessageBoardV2
    extends LinearLayout {

    private Activity activity;
    private TextView contentText;
    private TextView headerText;
    private IconTextView headerIcon;
    protected View.OnClickListener onClickListener;
    protected ViewGroup messageBoardButtons;

    public MessageBoardV2
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

    protected void setPositiveButton(String positiveButtonLabel, int what) {
        Button positiveButton = new Button(activity);
        positiveButton.setTextColor(Color.parseColor("#ffffff"));
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        positiveButton.
            setBackgroundResource(R.drawable.action_button);
        positiveButton.setPadding(20, 20, 20, 20);
        positiveButton.setOnClickListener(onClickListener);
        positiveButton.setTag(what);
        positiveButton.setText(positiveButtonLabel);
        messageBoardButtons.addView(positiveButton);
    }

    protected void setNegativeButton(String negativeButtonLabel, int what) {
        Button negativeButton = new Button(activity);
        negativeButton.setTextColor(Color.parseColor("#ffffff"));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        negativeButton.
            setBackgroundResource(R.drawable.negative_action_button);
        negativeButton.setPadding(20, 20, 20, 20);
        negativeButton.setOnClickListener(onClickListener);
        negativeButton.setTag(what);
        negativeButton.setText(negativeButtonLabel);
        messageBoardButtons.addView(negativeButton);
    }
}
