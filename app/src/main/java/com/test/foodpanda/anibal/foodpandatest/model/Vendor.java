package com.test.foodpanda.anibal.foodpandatest.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Anibal on 13/5/16.
 */
@Table(database = AppDatabase.class)
public class Vendor extends BaseModel {

    @PrimaryKey()
    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    public long id;

    @Column()
    public String name;

    @Column()
    public int minimum_order_amount;

    @Column()
    public String logo;
}
