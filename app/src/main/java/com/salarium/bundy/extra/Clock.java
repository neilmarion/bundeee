package com.salarium.bundy.extra;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.salarium.bundy.BundyActivity;
import com.salarium.bundy.R;
import com.salarium.bundy.values.*;
import java.lang.Runnable;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Time utility class
 *
 * @author Neil Marion dela Cruz
 */
public class Clock {
    private final String format = "HH:mm:ss";
    private int viewId;
    private TextView textView;
    private View view;

    SimpleDateFormat sdf = new SimpleDateFormat(format);

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            runClock();
        }
    };

    public Clock() {}

    public Clock(View view, int viewId) {
        this.view = view;
        this.viewId = viewId;
        textView = (TextView) view.findViewById(viewId);
    }

    public void run() {
        runnable.run();
    }

    public String getCurrentTime() {
        Date dt = new Date();
        String s = sdf.format(dt);
        return s;
    }

    private void runClock() {
        textView.setText(getCurrentTime());
        handler.postDelayed(runnable, Values.Durations.ONE_THOUSAND_MILLISECONDS);
    }
}


