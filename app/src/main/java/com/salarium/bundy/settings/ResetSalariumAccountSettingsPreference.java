package com.salarium.bundy.settings;

import com.salarium.bundy.api.SalariumAdminAccountAPI;
import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.view.ViewManager;

import android.app.Activity;
import android.content.Context;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONObject;

public class ResetSalariumAccountSettingsPreference extends DialogPreference {
    private SimplePersistence simplePersistence;
    private Context context;
    private EditText email;
    private EditText password;
    private View view;
    private SalariumAdminAccountAPI salariumAdminAccountAPI;
    private ViewManager viewManager;
    private AdminAccountSettings adminAccountSettings;

    public static final int COMPANY_SETTINGS_SUMMARY_KEY_STRING_ID =
        R.string.pref_company_settings_summary;
    public static final int SALARIUM_ACCOUNT_SUMMARY_KEY_STRING_ID =
        R.string.pref_salarium_account_summary;

    public ResetSalariumAccountSettingsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setDialogLayoutResource(R.layout.reset_salarium_account_settings);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        simplePersistence = new SimplePersistence(context);
        adminAccountSettings =
            new AdminAccountSettings(simplePersistence, (Activity) context);
        salariumAdminAccountAPI = new SalariumAdminAccountAPI(
            adminAccountSettings
        );

        setDialogIcon(null);
        if (!adminAccountSettings.isSalariumAccountSet()) {
            setEnabled(false);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            resetSalariumAccountSettings();
            resetSummaries();
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.view = view;
    }

    private Preference getCompanySettingsPreference() {
        return getPreferenceManager().findPreference(
            CompanySettingsPreference.COMPANY_SETTINGS_PREFERENCE_ID);
    }

    private Preference getEmailAndPasswordSettingsPreference() {
        return getPreferenceManager().findPreference(
            CompanySettingsPreference.SALARIUM_ACCOUNT_SETTINGS_PREFERENCE_ID);
    }

    private void resetSummaries() {
        getEmailAndPasswordSettingsPreference().
            setSummary(((Activity) context).
                getString(SALARIUM_ACCOUNT_SUMMARY_KEY_STRING_ID));
        getCompanySettingsPreference().
            setSummary(((Activity) context).
                getString(COMPANY_SETTINGS_SUMMARY_KEY_STRING_ID));

         getCompanySettingsPreference().setEnabled(false); // set disabled
         setEnabled(false); // set disabled
    }

    private void resetSalariumAccountSettings() {
        adminAccountSettings.resetSalariumAccountSettings();
    }
}
