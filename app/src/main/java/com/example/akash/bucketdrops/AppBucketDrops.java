package com.example.akash.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import adapters.Filter;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by knighthood on 3/31/2016.
 */
public class AppBucketDrops extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * Save Selected Filter
     * @param filterOption
     */
    public static void save(Context context, int filterOption) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("filter", filterOption);
        editor.apply();
    }

    /**
     * Load Previously Selected Filter
     * @return
     */
    public static int load(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int filterOption = pref.getInt("filter", Filter.NONE);
        return filterOption;
    }
}
