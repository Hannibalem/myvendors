package com.test.foodpanda.anibal.foodpandatest;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.test.foodpanda.anibal.foodpandatest.dagger.component.AppComponent;
import com.test.foodpanda.anibal.foodpandatest.dagger.component.DaggerAppComponent;
import com.test.foodpanda.anibal.foodpandatest.dagger.module.AppModule;
import com.test.foodpanda.anibal.foodpandatest.dagger.module.ServiceModule;

/**
 * Created by Anibal on 13/5/16.
 */
public class TestApplication extends Application {

    public AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());

        mAppComponent = DaggerAppComponent.builder()
                //.appModule(new AppModule(this)).serviceModule(new ServiceModule()).build();
                .serviceModule(new ServiceModule()).build();


    }
}
