package com.salarium.bundy.view;

import com.salarium.bundy.clocking.method.EmployeeIDMethod;
import com.salarium.bundy.clocking.method.QRCodeMethod;
import com.salarium.bundy.clocking.method.RFIDMethod;
import com.salarium.bundy.config.ConfigManager;
import com.salarium.bundy.extra.Clock;
import com.salarium.bundy.extra.BundyDate;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.values.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ViewManager
    implements DialogInterface.OnClickListener{

    private Activity activity;
    private ConfigManager configManager;
    private MessageReceiver messageReceiver;

    public static int INCOMPLETE_SETTINGS_MESSAGE =
        R.string.incomplete_settings_message;

    public ViewManager(Activity activity) {
        this.activity = activity;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
        messageReceiver = configManager.createMessageReceiver();
    }

    public SimpleNumberInputViewDialog createSimpleNumberInputViewDialog(
        SimpleNumberInputViewCallback callback,
        String message) {

        return new SimpleNumberInputViewDialog(activity, callback, message);
    }

    public SimpleNumberInputViewDialog createJammedSimpleNumberInputViewDialog(
        SimpleNumberInputViewCallback callback,
        String message) {

        SimpleNumberInputViewDialog simpleNumberInputViewDialog =
            createSimpleNumberInputViewDialog(callback, message);
        simpleNumberInputViewDialog.setDisplayJammed();
        return simpleNumberInputViewDialog;
    }

    public SimpleNumberInputView createSimpleNumberInputView(
        SimpleNumberInputViewCallback callback,
        String message) {

        return new SimpleNumberInputView(activity, callback, message);
    }

    // v2
    public SimpleNumberInputViewV2 createSimpleNumberInputViewV2(
        SimpleNumberInputViewCallback callback,
        String message) {

        return new SimpleNumberInputViewV2(activity, callback, message);
    }

    public DemoDialogView createDemoDialogView(DemoDialogViewCallback callback) {
        return new DemoDialogView(this.activity, callback);
    }

    public void showErrorDialog(String message) {
        NegativeNonExpiringDismissDialog errorDialog  =
            new NegativeNonExpiringDismissDialog(
                activity,
                Values.Labels.OK,
                message,
                Values.Icons.EXCLAMATION,
                States.GENERAL_ERROR,
                this);
        errorDialog.showDialog();
    }

    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(Values.Labels.OK,
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onClick(DialogInterface dialog, int which) {}

    // create a separate class for this
    public void createClockAndDateViewInBackground() {
        ViewGroup view = (ViewGroup) activity.getWindow().getDecorView();

        LinearLayout clockAndDate = new LinearLayout(activity);
        clockAndDate.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        clockAndDate.setLayoutParams(layoutParams);
        clockAndDate.setGravity(Gravity.CENTER);
        TextView clockLayout = new TextView(activity);
        clockLayout.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        clockLayout.setTextSize(Integer.parseInt(activity.getString(R.string.clock_size)));
        int viewId = 999;
        clockLayout.setId(viewId);
        clockLayout.setTextColor(
            activity.getResources().getColor(R.color.white));
        clockAndDate.addView(clockLayout);

        view.addView(clockAndDate);

        Clock clock = new Clock(clockAndDate, viewId);
        clock.run();
    }

    //v2
    public void createClockAndDateViewInMainLeft() {
        ViewGroup view = (ViewGroup) activity.findViewById(R.id.main_left);

        LinearLayout clockAndDate = new LinearLayout(activity);
        clockAndDate.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        clockAndDate.setLayoutParams(layoutParams);
        clockAndDate.setGravity(Gravity.CENTER);
        TextView clockLayout = new TextView(activity);
        clockLayout.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        clockLayout.setTextSize(Integer.parseInt(activity.getString(R.string.clock_size_v2)));
        int viewId = 9998;
        clockLayout.setId(viewId);
        clockLayout.setTextColor(
            activity.getResources().getColor(R.color.grey));
        clockAndDate.addView(clockLayout);

        TextView dateLayout = new TextView(activity);
        dateLayout.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        dateLayout.setTextSize(Integer.parseInt(activity.getString(R.string.date_size_v2)));
        int viewId2 = 9999;
        dateLayout.setId(viewId2);
        dateLayout.setTextColor(
            activity.getResources().getColor(R.color.grey));

        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = new Date();
        String dayOfWeek = (new SimpleDateFormat("E")).format(date);

        dateLayout.setText(dayOfWeek + ", " + dateFormat.format(date));

        clockAndDate.addView(dateLayout);

        view.addView(clockAndDate, 0);

        Clock clock = new Clock(clockAndDate, viewId);
        clock.run();

        BundyDate dateDisplay =
            new BundyDate(clockAndDate, viewId2);
        dateDisplay.run();
    }

    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }
}
