package com.salarium.bundy.settings;

import android.app.Activity;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import org.json.JSONObject;

/** A Settings class that manages admin account settings
 * @author Neil Marion dela Cruz
 */
public class AdminAccountSettings extends Settings {
    private final Activity activity;
    private final SimplePersistence simplePersistence;

    //pref_account_database_name
    public static final int ACCOUNT_TOKEN_KEY_STRING_ID =
        R.string.pref_account_token_settings_id;

    //pref_chosen_company_id
    public static final int CHOSEN_COMPANY_ID_KEY_STRING_ID =
        R.string.pref_chosen_company_id_settings_id;

    //pref_chosen_company_name
    public static final int CHOSEN_COMPANY_NAME_KEY_STRING_ID =
        R.string.pref_chosen_company_name_settings_id;

    //pref_account_companies
    public static final int ACCOUNT_COMPANIES_KEY_STRING_ID =
        R.string.pref_account_companies_id;

    public static final int ACCOUNT_EMAIL_KEY_STRING_ID =
        R.string.pref_account_email_id;

    public AdminAccountSettings
        (SimplePersistence simplePersistence, Activity activity) {

        this.simplePersistence = simplePersistence;
        this.activity = activity;
    }

    @Override
    public boolean complete() {
        return isAccountTokenSet() && isCompanyIdSet();
    }

    public String getAccountToken () {
        String accountToken = simplePersistence.getPersistence(
            activity.getString(ACCOUNT_TOKEN_KEY_STRING_ID)
        );
        return accountToken;
    }

    public void setAccountToken (String token) {
        simplePersistence.persistString(
            activity.getString(ACCOUNT_TOKEN_KEY_STRING_ID), token
        );
    }

    public String getChosenCompanyId () {
        String chosenCompany = simplePersistence.getPersistence(
            activity.getString(CHOSEN_COMPANY_ID_KEY_STRING_ID)
        );
        return chosenCompany;
    }

    public String getChosenCompanyName () {
        String chosenCompany = simplePersistence.getPersistence(
            activity.getString(CHOSEN_COMPANY_NAME_KEY_STRING_ID)
        );
        return chosenCompany;
    }

    public void setChosenCompanyId (String id) {
        simplePersistence.persistString(
            activity.getString(CHOSEN_COMPANY_ID_KEY_STRING_ID), id
        );
    }

    public void setChosenCompanyName (String name) {
        simplePersistence.persistString(
            activity.getString(CHOSEN_COMPANY_NAME_KEY_STRING_ID), name
        );
    }

    public JSONObject getCompanies () {
        return simplePersistence.getJSONObjectPersistence(
            activity.getString(ACCOUNT_COMPANIES_KEY_STRING_ID)
        );
    }

    public boolean isSalariumAccountSet() {
        return isAccountTokenSet();
    }

    public boolean isAccountTokenSet () {
        return !getAccountToken().isEmpty();
    }

    public boolean isCompanyIdSet() {
        return !getChosenCompanyId().isEmpty();
    }

    public void setAccountEmail(String email) {
        simplePersistence.persistString(
            activity.getString(ACCOUNT_EMAIL_KEY_STRING_ID), email
        );
    }

    public String getAccountEmail() {
        return simplePersistence.getPersistence(
            activity.getString(ACCOUNT_EMAIL_KEY_STRING_ID)
        );
    }

    public void resetSalariumAccountSettings() {
        simplePersistence.removePersistence(
            activity.getString(ACCOUNT_TOKEN_KEY_STRING_ID));
        simplePersistence.removePersistence(
            activity.getString(CHOSEN_COMPANY_ID_KEY_STRING_ID));
        simplePersistence.removePersistence(
            activity.getString(CHOSEN_COMPANY_NAME_KEY_STRING_ID));
        simplePersistence.removePersistence(
            activity.getString(ACCOUNT_COMPANIES_KEY_STRING_ID));
        simplePersistence.removePersistence(
            activity.getString(ACCOUNT_EMAIL_KEY_STRING_ID));
    }
}
