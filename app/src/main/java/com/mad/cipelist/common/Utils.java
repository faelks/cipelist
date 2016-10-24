package com.mad.cipelist.common;

import android.content.Context;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

/**
 * Created by Felix on 19/10/2016.
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

}
