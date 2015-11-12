package com.salarium.bundy.settings;

import com.salarium.bundy.api.SalariumAdminAccountAPI;
import com.salarium.bundy.api.SalariumAPIConnection;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.AdminAccountSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

import android.app.Activity;
import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailAndPasswordSettingsPreference extends EditTextPreference {
    private static final Logger log =
        Logger.getLogger(EmailAndPasswordSettingsPreference.class.getName());
    private SimplePersistence simplePersistence;
    private Context context;
    private EditText email;
    private EditText password;
    private View view;
    private SalariumAdminAccountAPI salariumAdminAccountAPI;
    private ViewManager viewManager;
    private AdminAccountSettings adminAccountSettings;

    public EmailAndPasswordSettingsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setDialogLayoutResource(R.layout.email_and_password_settings);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        simplePersistence = new SimplePersistence(context);
        adminAccountSettings =
            new AdminAccountSettings(simplePersistence, (Activity) context);
        salariumAdminAccountAPI = new SalariumAdminAccountAPI(
            adminAccountSettings
        );

        setDialogIcon(null);

        if (adminAccountSettings.isAccountTokenSet()) {
            this.setSummary(adminAccountSettings.getAccountEmail());
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        JSONObject response = null;
        if (positiveResult) {
            try {
                response = salariumAdminAccountAPI.registerAdminAcount(
                    getEmail(), getPassword()
                );

                if (response.get(Values.API.Keys.STATUS_KEY).
                    equals(SalariumAPIConnection.SUCCESS)
                ) {
                    persistEmail(getEmail());
                    persistCompanies((JSONArray) response.get(
                        Values.API.Keys.COMPANIES_KEY)
                    );
                    persistAccountToken(response.get(
                        Values.API.Keys.ACCOUNT_TOKEN_KEY).toString()
                    );
                    getCompanySettingsPreference().setEnabled(true);
                    getResetSalariumAccountSettingsPreference().
                        setEnabled(true);
                    ViewManager.showAlertDialog(
                        context,
                        Values.Messages.Neutral.ACCOUNT_REGISTERED_ON_DEVICE
                    );
                    this.setSummary(adminAccountSettings.getAccountEmail());
                } else {
                    ViewManager.showAlertDialog(
                        context,
                        Values.Messages.Error.WRONG_EMAIL_OR_PASSWORD
                    );
                }
            } catch (IOException e) {
                log.log(Level.SEVERE, e.toString(), e);
                e.printStackTrace(System.out); // remove
                ViewManager.showAlertDialog(
                    context,
                    Values.Messages.Error.NETWORK_PROBLEM
                );
            } catch (JSONException e) {
                log.log(Level.SEVERE, e.toString(), e);
                e.printStackTrace(System.out); // remove
                ViewManager.showAlertDialog(
                    context,
                    Values.Messages.Error.DATA_RETURNED_BY_SERVER_PROBLEM
                );
            }
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.view = view;

        getEmailEditText().setText(getEmailPersistedValue());
    }

    private EditText getEmailEditText() {
        return (EditText) view.
            findViewById(R.id.pref_email_salarium_account_settings);
    }

    private String getEmail() {
        return ((EditText) view.
                findViewById(R.id.pref_email_salarium_account_settings)).
                    getText().toString();
    }

    private String getPassword() {
        return ((EditText) view.
            findViewById(R.id.pref_password_salarium_account_settings)).
                getText().toString();
    }

    private String getEmailPersistedValue() {
        return adminAccountSettings.getAccountEmail();
    }

    private Preference getCompanySettingsPreference() {
        return getPreferenceManager().findPreference(
            CompanySettingsPreference.COMPANY_SETTINGS_PREFERENCE_ID);
    }

    private Preference getResetSalariumAccountSettingsPreference() {
        return getPreferenceManager().findPreference(
            CompanySettingsPreference.
                RESET_SALARIUM_ACCOUNT_SETTINGS_PREFERENCE_ID);
    }

    private void persistEmail(String email) {
        adminAccountSettings.setAccountEmail(email);
    }

    private void persistCompanies(JSONArray companies) {
        try {
            JSONObject comps = new JSONObject();
            for(int i = 0; i < companies.length(); i++) {
                comps.put(
                    companies.getJSONObject(i).
                        get(Values.API.Keys.COMPANY_NAME_KEY).toString(),
                    companies.getJSONObject(i).
                        get(Values.API.Keys.COMPANY_ID_KEY)
                );
            }

            simplePersistence.persistJSONObject(context.
                getString(
                    AdminAccountSettings.ACCOUNT_COMPANIES_KEY_STRING_ID),
                    comps);
        } catch (JSONException e) {
            e.printStackTrace(System.out);
            ViewManager.showAlertDialog(
                context,
                Values.Messages.Error.DATA_RETURNED_BY_SERVER_PROBLEM
            );
        }
    }

    private void persistAccountToken(String token) {
        adminAccountSettings.setAccountToken(token);
    }
}
