package com.test.foodpanda.anibal.foodpandatest.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Anibal on 13/5/16.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "FoodpandaTest";

    public static final int VERSION = 1;
}
