package com.mad.cipelist.model;

import com.mad.cipelist.model.Search;

import java.util.ArrayList;

/**
 * Created by Felix on 15/09/16.
 *
 * A user should be able to log in and
 * get access to previous searches and
 * change settings
 */
public class User {

    private String mName;
    private String mEmail;
    private ArrayList<Search> mSearches;
}
