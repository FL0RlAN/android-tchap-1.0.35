package org.piwik.sdk;

import android.content.SharedPreferences;
import java.util.Map.Entry;
import java.util.UUID;

public class LegacySettingsPorter {
    static final String LEGACY_PREF_FIRST_VISIT = "tracker.firstvisit";
    static final String LEGACY_PREF_OPT_OUT = "piwik.optout";
    static final String LEGACY_PREF_PREV_VISIT = "tracker.previousvisit";
    static final String LEGACY_PREF_USER_ID = "tracker.userid";
    static final String LEGACY_PREF_VISITCOUNT = "tracker.visitcount";
    private final SharedPreferences mLegacyPrefs;

    public LegacySettingsPorter(Piwik piwik) {
        this.mLegacyPrefs = piwik.getPiwikPreferences();
    }

    public void port(Tracker tracker) {
        SharedPreferences preferences = tracker.getPreferences();
        SharedPreferences sharedPreferences = this.mLegacyPrefs;
        String str = LEGACY_PREF_OPT_OUT;
        if (sharedPreferences.getBoolean(str, false)) {
            preferences.edit().putBoolean("tracker.optout", true).apply();
            this.mLegacyPrefs.edit().remove(str).apply();
        }
        SharedPreferences sharedPreferences2 = this.mLegacyPrefs;
        String str2 = LEGACY_PREF_USER_ID;
        if (sharedPreferences2.contains(str2)) {
            preferences.edit().putString(str2, this.mLegacyPrefs.getString(str2, UUID.randomUUID().toString())).apply();
            this.mLegacyPrefs.edit().remove(str2).apply();
        }
        SharedPreferences sharedPreferences3 = this.mLegacyPrefs;
        String str3 = LEGACY_PREF_FIRST_VISIT;
        if (sharedPreferences3.contains(str3)) {
            preferences.edit().putLong(str3, this.mLegacyPrefs.getLong(str3, -1)).apply();
            this.mLegacyPrefs.edit().remove(str3).apply();
        }
        SharedPreferences sharedPreferences4 = this.mLegacyPrefs;
        String str4 = LEGACY_PREF_VISITCOUNT;
        if (sharedPreferences4.contains(str4)) {
            preferences.edit().putLong(str4, (long) this.mLegacyPrefs.getInt(str4, 0)).apply();
            this.mLegacyPrefs.edit().remove(str4).apply();
        }
        SharedPreferences sharedPreferences5 = this.mLegacyPrefs;
        String str5 = LEGACY_PREF_PREV_VISIT;
        if (sharedPreferences5.contains(str5)) {
            preferences.edit().putLong(str5, this.mLegacyPrefs.getLong(str5, -1)).apply();
            this.mLegacyPrefs.edit().remove(str5).apply();
        }
        for (Entry entry : this.mLegacyPrefs.getAll().entrySet()) {
            if (((String) entry.getKey()).startsWith("downloaded:")) {
                preferences.edit().putBoolean((String) entry.getKey(), true).apply();
                this.mLegacyPrefs.edit().remove((String) entry.getKey()).apply();
            }
        }
    }
}
