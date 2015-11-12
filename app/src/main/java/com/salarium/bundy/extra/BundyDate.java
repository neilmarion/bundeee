package com.salarium.bundy.extra;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.salarium.bundy.BundyActivity;
import com.salarium.bundy.R;
import java.lang.Runnable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** Date utility
 *
 * @author Neil Marion dela Cruz
 */
public class BundyDate {
    private int viewId;
    private TextView textView;
    private View view;
    private DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
    private DateFormat dayOfWeekFormat = new SimpleDateFormat("E");
    private String dayOfWeek;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            runDate();
        }
    };

    public BundyDate(View view, int viewId) {
        this.view = view;
        this.viewId = viewId;
        textView = (TextView) view.findViewById(viewId);
    }

    public void run() {
        runnable.run();
    }

    private void runDate() {
        textView.setText(getCurrentDate());
        handler.postDelayed(runnable, 1000);
    }

    private String getCurrentDate() {
        Date date = new Date();
        return dayOfWeekFormat.format(date) + ", " + dateFormat.format(date);
    }
}
