package com.mad.cipelist.common;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Provides general utility functions that can be accessed from anywhere in the application.
 */
public class Utils {

    /**
     * Removes the old database and replaces it with a fresh, new one.
     */
    public static void resetDatabase(Context context) {
        // Snippet that deletes the ORM tables and creates new ones.
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(context);
        schemaGenerator.deleteTables(new SugarDb(context).getDB());
        SugarContext.init(context);
        schemaGenerator.createDatabase(new SugarDb(context).getDB());
    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Retrieve the current date in a set format
     * @return date
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(new Date());
    }

    /**
     * Since a lot of the image urls are appended by a size restricting parameter, this function was
     * created to remove that.
     *
     * @param url original url
     * @return formatted url
     */
    public static String removeUrlImageSize(String url) {
        if (url.substring(url.length() - 4, url.length()).equals("=s90")) {
            url = url.substring(0, url.length() - 4);
        }
        return url;
    }

    /**
     * Converts an integer to a minute format
     * @param seconds seconds to be converted
     * @return final string
     */
    public static String secondsToMinutes(int seconds) {
        int minutes = seconds / 60;
        return minutes + " min";
    }

}
