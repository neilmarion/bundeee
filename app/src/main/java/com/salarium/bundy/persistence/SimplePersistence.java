package com.salarium.bundy.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/** Persistence class that uses shared preferences as storage
 *
 * @author Neil Marion dela Cruz
 */
public class SimplePersistence {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SimplePersistence(Context context) {
        this.context = context;
        this.preferences =
            PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = preferences.edit();
    }

    public String getPersistence(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBooleanPersistence(String key) {
        return preferences.getBoolean(key, false);
    }

    public int getIntegerPersistence(String key) {
        return preferences.getInt(key, 0);
    }

    public String getJSONObjectStringPersistence(String key) {
        return preferences.getString(key, "{' ':' '}");
    }

    public JSONObject getJSONObjectPersistence(String key) {
        try {
            return new JSONObject(getJSONObjectStringPersistence(key));
        } catch (JSONException e) {
            e.printStackTrace(System.out);
        }

        return null;
    }

    public String[] getJSONObjectPersistenceKeys(String key) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator<String> keys = getJSONObjectPersistence(key).keys();

        while (keys.hasNext()) {
            arrayList.add((String)keys.next());
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }

    public void persistInteger(String key, int data) {
        editor.putInt(key, data);
        editor.commit();
    }

    public void persistString(String key, String data) {
        editor.putString(key, data);
        editor.commit();
    }

    public void persistJSONObject(String key, JSONObject json) {
        persistString(key, json.toString());
    }

    public void persistBoolean(String key, boolean data) {
        editor.putBoolean(key, data);
    }

    public void removePersistence(String key) {
        editor.remove(key);
        editor.commit();
    }
}
