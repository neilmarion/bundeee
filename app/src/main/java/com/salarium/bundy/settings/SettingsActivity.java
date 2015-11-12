package com.salarium.bundy.settings;

import com.salarium.bundy.R;
import com.salarium.bundy.values.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;
import android.widget.Button;
import java.util.List;

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (hasHeaders()) {
            //Button button = new Button(this);
            //button.setText("Some action");
            //setListFooter(button);
        //}
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        System.out.println(fragmentName);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(
            Values.General.ACTIVITY_CLASS_NAME, this.getClass().getName());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
