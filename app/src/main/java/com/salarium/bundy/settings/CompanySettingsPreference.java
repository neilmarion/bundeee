package com.salarium.bundy.settings;

import com.salarium.bundy.api.SalariumAdminAccountAPI;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.AdminAccountSettings;

import com.salarium.bundy.R;
import android.preference.EditTextPreference;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;
import android.preference.Preference;
import android.app.Activity; import android.view.View;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

import org.json.JSONObject;
import java.util.ArrayList;

public class CompanySettingsPreference extends EditTextPreference {
    private SimplePersistence simplePersistence;
    private Context context;
    private View view;
    private Spinner dropdown;
    private AdminAccountSettings adminAccountSettings;
    private SalariumAdminAccountAPI salariumAdminAccountAPI;

    // this is tied to ids in company_settings.xml
    public static final String COMPANY_SETTINGS_PREFERENCE_ID =
        "pref_company_settings";
    public static final String SALARIUM_ACCOUNT_SETTINGS_PREFERENCE_ID =
        "pref_salarium_account_settings";
    public static final String RESET_SALARIUM_ACCOUNT_SETTINGS_PREFERENCE_ID =
        "pref_reset_salarium_account_settings";

    public CompanySettingsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setDialogLayoutResource(R.layout.company_settings);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        simplePersistence = new SimplePersistence(context);
        adminAccountSettings =
            new AdminAccountSettings(simplePersistence, (Activity) context);

        salariumAdminAccountAPI = new SalariumAdminAccountAPI(
            adminAccountSettings
        );

        setDialogIcon(null);

        if (!adminAccountSettings.isAccountTokenSet()) {
            setEnabled(false);
        } else {
            setEnabled(true);
        }

        if (adminAccountSettings.isCompanyIdSet()) {
            this.setSummary(adminAccountSettings.getChosenCompanyName());
        }

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            adminAccountSettings.
                setChosenCompanyName(
                    String.valueOf(dropdown.getSelectedItem()));

            this.setSummary(adminAccountSettings.getChosenCompanyName());

            try {

                adminAccountSettings.
                    setChosenCompanyId(
                        adminAccountSettings.getCompanies().
                            get(String.valueOf(dropdown.getSelectedItem())).
                                toString());

                salariumAdminAccountAPI.registerAdminCompany(
                    adminAccountSettings.getChosenCompanyId()
                );
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.view = view;

        // USE THIS:

        dropdown = (Spinner) view.
            findViewById(R.id.pref_company_company_settings);
        String[] items = simplePersistence.
            getJSONObjectPersistenceKeys(((Activity) context).
                getString(AdminAccountSettings.ACCOUNT_COMPANIES_KEY_STRING_ID));
        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, items);
        dropdown.setSelection(adapter.
            getPosition(adminAccountSettings.getChosenCompanyName()));
        dropdown.setAdapter(adapter);
    }
}
