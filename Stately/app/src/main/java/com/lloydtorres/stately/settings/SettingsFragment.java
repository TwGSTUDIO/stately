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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lloydtorres.stately.BuildConfig;
import com.lloydtorres.stately.R;
import com.lloydtorres.stately.helpers.RaraHelper;
import com.lloydtorres.stately.zombie.NightmareHelper;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by Lloyd on 2016-01-27.
 * The fragment within the Settings activity.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        Preference appVersionSetting = findPreference(SettingsActivity.SETTING_APP_VERSION);
        appVersionSetting.setTitle(String.format(Locale.US, getString(R.string.app_version), BuildConfig.VERSION_NAME));

        // Disable theme options and show warning on Z-Day
        if (getContext() != null) {
            if (NightmareHelper.getIsZDayActive(getContext())) {
                Preference themeSetting = findPreference(SettingsActivity.SETTING_THEME);
                themeSetting.setEnabled(false);
                themeSetting.setSummary(getString(R.string.setting_desc_zombie));
            }
            if (RaraHelper.getSpecialDayStatus(getContext()) == RaraHelper.DAY_APRIL_FOOLS) {
                Preference governmentSetting = findPreference(SettingsActivity.SETTING_GOVERNMENT);
                governmentSetting.setEnabled(false);
                governmentSetting.setSummary(getString(R.string.setting_category_april));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
