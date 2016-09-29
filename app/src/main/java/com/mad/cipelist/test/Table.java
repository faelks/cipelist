package com.mad.cipelist.test;

import android.provider.BaseColumns;

/**
 * Created by Felix on 21/09/16.
 *
 * Dud class used by the SQLite database
 */
public class Table {

    public Table() {

    }

    public static abstract class TableInfo implements BaseColumns {
        public static final String USER_NAME = "user_name";
        public static final String USER_PASS = "user_password";
        public static final String DATABASE_NAME = "user_info_database";
        public static final String TABLE_NAME = "user_info_table";
    }
}
