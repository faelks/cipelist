package com.mad.cipelist.common;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

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
     * Shows the soft keyboard

    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
     */

    /**
     * Removes duplicates from the list
     public List<String> removeDuplicates(List<String> ingredients) {
     Set<String> noDups = new HashSet<>();
     noDups.addAll(ingredients);
     return new ArrayList<>(noDups);
     }
     */

}
