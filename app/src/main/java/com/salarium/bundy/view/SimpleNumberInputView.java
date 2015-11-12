package com.salarium.bundy.view;

import com.salarium.bundy.R;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.SimpleNumberInputViewCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.Arrays;

public class SimpleNumberInputView
    extends LinearLayout
    implements DialogInterface.OnDismissListener {

    private final Activity activity;
    private final Dialog dialog;
    private final String blank = "";
    private MediaPlayer buttonMediaPlayer;
    private Button clearButton;
    private Button backspaceButton;
    private Button confirmButton;
    private Button keypadButtonOne;
    private Button keypadButtonTwo;
    private Button keypadButtonThree;
    private Button keypadButtonFour;
    private Button keypadButtonFive;
    private Button keypadButtonSix;
    private Button keypadButtonSeven;
    private Button keypadButtonEight;
    private Button keypadButtonNine;
    private Button keypadButtonZero;
    private ArrayList<Button> keypadButtons;
    private EditText display;
    private String displayString = blank;
    private SimpleNumberInputViewCallback cb;
    private String message;
    private boolean isDisplayStringJammed = false;

    // with a dialog
    public SimpleNumberInputView
        (Activity activity,
        SimpleNumberInputViewCallback callback,
        Dialog dialog,
        String message) {

        super((Context) activity);
        this.activity = activity;
        this.dialog = dialog;
        this.message = message;

        LayoutInflater inflater =
            (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout =
            (LinearLayout) inflater.inflate(R.layout.simple_number_input, null);

        cb = callback;
        setup(layout);
    }

    public void disable() {
        disableButtons();
        disableDisplay();
    }

    private void disableButtons() {
        for(Button keypadButton : keypadButtons){
            keypadButton.setEnabled(false);
            keypadButton.setTextColor(
                activity.getResources().getColor(R.color.grey_lighter));
        }

        clearButton.setEnabled(false);
        clearButton.setTextColor(
            activity.getResources().getColor(R.color.grey_lighter));
        backspaceButton.setEnabled(false);
        backspaceButton.setTextColor(
            activity.getResources().getColor(R.color.grey_lighter));
        confirmButton.setEnabled(false);
        confirmButton.setBackgroundColor(
            activity.getResources().getColor(R.color.flat_green_lighter));
    }

    private void disableDisplay() {
        display.setFocusable(false);
    }

    // without a dialog
    public SimpleNumberInputView
        (Activity activity,
        SimpleNumberInputViewCallback callback,
        String message) {

        super((Context) activity);
        this.activity = activity;
        this.dialog = null;
        this.message = message;

        LayoutInflater inflater =
            (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout layout =
            (LinearLayout) inflater.inflate(R.layout.simple_number_input, null);

        cb = callback;
        setup(layout);
    }

    private EditText getEditText() {
        return (EditText) findViewById(R.id.simple_number_input_edittext);
    }

    private void setEditTextListener(EditText simpleTextInputEditText) {
        simpleTextInputEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            cb.receiveSimpleNumberInput(getText());
                            clearDisplayString();
                            break;
                        default:
                            break;
                    }
                }

                return false;
            }
        });
    }

    public void clearDisplayString() {
        displayString = blank;
        display.setText(displayString);
    }

    public void onDismiss(DialogInterface dialog) {
        clearDisplayString();
    }

    private void setup(LinearLayout layout) {
        addView(layout);

        confirmButton =
            (Button) findViewById(R.id.simple_number_input_enter_btn);
        display =
            (EditText) findViewById(R.id.simple_number_input_edittext);
        TextView messageTextView =
            (TextView) findViewById(R.id.simple_number_input_message);
        messageTextView.setText(message);

        setKeyPadButtons();
        setClearButton();
        setBackspaceButton();
        setEnterButton();
        setEditTextListener(getEditText());
    }

    private View.OnClickListener getKeypadEnterButtonOnClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cb.receiveSimpleNumberInput(displayString);
                if (dialog != null) {
                    dialog.cancel();
                }
                clearDisplayString();
            }
        };
    }

    private View.OnClickListener getKeypadOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //playButtonSound();
                updateDisplayString(((Button) v).getText().toString());
            }
        };
    }

    private void setEnterButton() {
        confirmButton.setOnClickListener(getKeypadEnterButtonOnClickListener());
    }

    private void setClearButton() {
        clearButton = (Button) findViewById(R.id.clear_btn);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDisplayString();
            }
        });
    }

    private void setBackspaceButton() {
        backspaceButton = (Button) findViewById(R.id.backspace_btn);
        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDisplayStringLastCharacter();
            }
        });
    }

    private void setKeyPadButtons() {
        keypadButtonOne = (Button) findViewById(R.id.keypad_btn_one);
        keypadButtonTwo = (Button) findViewById(R.id.keypad_btn_two);
        keypadButtonThree = (Button) findViewById(R.id.keypad_btn_three);
        keypadButtonFour = (Button) findViewById(R.id.keypad_btn_four);
        keypadButtonFive = (Button) findViewById(R.id.keypad_btn_five);
        keypadButtonSix = (Button) findViewById(R.id.keypad_btn_six);
        keypadButtonSeven = (Button) findViewById(R.id.keypad_btn_seven);
        keypadButtonEight = (Button) findViewById(R.id.keypad_btn_eight);
        keypadButtonNine = (Button) findViewById(R.id.keypad_btn_nine);
        keypadButtonZero = (Button) findViewById(R.id.keypad_btn_zero);

        keypadButtons = new ArrayList<Button>(Arrays.asList(keypadButtonOne,
            keypadButtonTwo, keypadButtonThree, keypadButtonFour,
            keypadButtonFive, keypadButtonSix, keypadButtonSeven,
            keypadButtonEight, keypadButtonNine, keypadButtonZero));

        setKeypadButtonListeners();
    }

    private void setKeypadButtonListeners() {
        for (Button keypadButton : keypadButtons) {
            keypadButton.setOnClickListener(getKeypadOnClickListener());
            keypadButton.setSoundEffectsEnabled(false);
        }
    }

    private void updateDisplayString(String i) {
        displayString = displayString + i;
        if (isDisplayStringJammed) {
            display.setText(displayString.replaceAll("(?s).", " •"));
        } else {
            display.setText(displayString);
        }
    }

    private void removeDisplayStringLastCharacter() {
        if (displayString.length() > 0) {
            displayString = displayString.substring(0, displayString.length()-1);

            if (isDisplayStringJammed) {
                display.setText(displayString.replaceAll("(?s).", " •"));
            } else {
                display.setText(displayString);
            }
        }
    }

    private String getText() {
        return getEditText().getText().toString();
    }

    private void setupSoundFX() {
        buttonMediaPlayer = MediaPlayer.create(activity, R.raw.button);
        buttonMediaPlayer.setOnPreparedListener(
            new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        buttonMediaPlayer.setOnCompletionListener(
            new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }

    private void playButtonSound() {
        setupSoundFX();
    }

    public void setDisplayJammed() {
        isDisplayStringJammed = true;
    }
}
