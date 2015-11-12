package com.salarium.bundy.menu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.salarium.bundy.enrollment.EnrollmentActivity;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.SettingsActivity;
import com.salarium.bundy.settings.SettingsManager;
import com.salarium.bundy.values.*;
import com.salarium.bundy.values.States;
import com.salarium.bundy.view.NegativeNonExpiringDismissDialog;
import com.salarium.bundy.view.SimpleNumberInputViewCallback;
import com.salarium.bundy.view.SimpleNumberInputViewDialog;
import com.salarium.bundy.view.ViewManager;
import java.lang.Class;

/** Class that handles the menu
 * @author Neil Marion dela Cruz
 */
public class MenuManager
    implements SimpleNumberInputViewCallback,
                DialogInterface.OnClickListener
    {

    private Activity activity;
    private Context context;
    private int itemId;
    private SettingsManager settingsManager;
    private SimpleNumberInputViewDialog simpleNumberInputViewDialog = null;
    private ViewManager viewManager;

    public MenuManager(
        Context context,
        Activity activity,
        ViewManager viewManager,
        SettingsManager settingsManager
    ) {
        this.context = context;
        this.activity = activity;
        this.viewManager = viewManager;
        this.settingsManager = settingsManager;
        simpleNumberInputViewDialog =
            viewManager.createJammedSimpleNumberInputViewDialog(
                this, Values.Labels.DEVICE_PIN
            );
    }

    public void launchActivity(int itemId) {
        this.itemId = itemId;
        simpleNumberInputViewDialog.showDialog();
    }

    /** Receive pin code from Simple Number Input view callback
     *
     * @param number             pin code
     */
    public void receiveSimpleNumberInput(String number) {
        if (settingsManager.getDevicePinCodeSettings().getDevicePinCode().
            equals(number)
        ) {
            Intent i = null;
            switch (itemId) {
                case R.id.action_settings:
                    i = new Intent(context, SettingsActivity.class);
                    activity.startActivityForResult(i, 0);
                    break;
                case R.id.action_enrollment:
                    i = new Intent(context, EnrollmentActivity.class);
                    activity.startActivityForResult(i, 0);
                    break;
            }
        } else {
            showWrongPinDialog();
        }
    }

    /** Show wrong-pin error message
     */
    public void showWrongPinDialog() {
        NegativeNonExpiringDismissDialog errorDialog  =
            new NegativeNonExpiringDismissDialog(
                activity,
                Values.Labels.OK,
                Values.Messages.Error.WRONG_DEVICE_PIN,
                Values.Icons.EXCLAMATION,
                States.GENERAL_ERROR,
                this
            );
        errorDialog.showDialog();
    }

    public void onClick(DialogInterface dialog, int which) {}
}
