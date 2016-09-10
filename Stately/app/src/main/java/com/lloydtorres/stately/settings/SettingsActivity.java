/**
 * Copyright 2016 Lloyd Torres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lloydtorres.stately.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lloydtorres.stately.R;
import com.lloydtorres.stately.core.SlidrActivity;
import com.lloydtorres.stately.dto.UserLogin;
import com.lloydtorres.stately.helpers.SparkleHelper;
import com.lloydtorres.stately.login.LoginActivity;

/**
 * Created by Lloyd on 2016-01-27.
 * An activity to show app settings.
 */
public class SettingsActivity extends SlidrActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String SETTING_AUTOLOGIN = "setting_autologin";
    public static final String SETTING_ISSUECONFIRM = "setting_issueconfirm";
    public static final String SETTING_EXITCONFIRM = "setting_exitconfirm";
    public static final String SETTING_CRASHREPORT = "setting_crashreport";
    public static final String SETTING_THEME = "setting_theme";

    public static final int THEME_VERT = 0;
    public static final int THEME_NOIR = 1;
    public static final int THEME_BLEU = 2;
    public static final int THEME_ROUGE = 3;
    public static final int THEME_VIOLET = 4;

    private static AlertDialog.Builder dialogBuilder;

    private SharedPreferences storage;
    private boolean isChangeThemeTriggered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setToolbar(toolbar);

        storage = PreferenceManager.getDefaultSharedPreferences(this);
        storage.registerOnSharedPreferenceChangeListener(this);

        dialogBuilder = new AlertDialog.Builder(this, R.style.MaterialDialog);
    }

    private void setToolbar(Toolbar toolbar)
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupNewTheme() {
        UserLogin curUser = SparkleHelper.getActiveUser(this);
        Intent loginActivityLaunch = new Intent(SettingsActivity.this, LoginActivity.class);
        loginActivityLaunch.putExtra(LoginActivity.USERDATA_KEY, curUser);
        loginActivityLaunch.putExtra(LoginActivity.NOAUTOLOGIN_KEY, true);
        loginActivityLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityLaunch);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (isChangeThemeTriggered) {
                    setupNewTheme();
                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isChangeThemeTriggered) {
            setupNewTheme();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        storage.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SETTING_CRASHREPORT:
                dialogBuilder.setMessage(getString(R.string.warn_crashreport)).setPositiveButton(getString(R.string.got_it), null).show();
                break;
            case SETTING_THEME:
                isChangeThemeTriggered = true;
                break;
        }
    }

    public static boolean getAutologinSetting(Context c) {
        SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(c);
        return storage.getBoolean(SettingsActivity.SETTING_AUTOLOGIN, true);
    }

    public static boolean getConfirmIssueDecisionSetting(Context c) {
        SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(c);
        return storage.getBoolean(SettingsActivity.SETTING_ISSUECONFIRM, true);
    }

    public static boolean getConfirmExitSetting(Context c) {
        SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(c);
        return storage.getBoolean(SettingsActivity.SETTING_EXITCONFIRM, true);
    }

    public static boolean getCrashReportSetting(Context c) {
        SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(c);
        return storage.getBoolean(SettingsActivity.SETTING_CRASHREPORT, true);
    }

    public static int getTheme(Context c) {
        SharedPreferences storage = PreferenceManager.getDefaultSharedPreferences(c);
        int theme = THEME_VERT;
        try {
            theme = Integer.valueOf(storage.getString(SettingsActivity.SETTING_THEME, String.valueOf(THEME_VERT)));
        }
        catch (Exception e) {
            SparkleHelper.logError(e.toString());
        }
        return theme;
    }
}
