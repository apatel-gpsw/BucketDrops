package com.example.akash.bucketdrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    /**
     * Change font for a TextView
     * @param context
     * @param textView
     */
    public static void setFontFamily(Context context, TextView textView){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");
        textView.setTypeface(typeface);
    }

    /**
     * Change font for multiple TextViews
     * @param context
     * @param textViews
     */
    public static void setFontFamily(Context context, TextView... textViews){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/raleway_thin.ttf");
        for (TextView textView : textViews)
            textView.setTypeface(typeface);
    }
}
