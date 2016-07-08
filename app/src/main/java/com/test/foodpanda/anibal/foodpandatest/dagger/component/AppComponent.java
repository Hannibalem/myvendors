package com.test.foodpanda.anibal.foodpandatest.dagger.component;

import com.google.gson.Gson;
import com.test.foodpanda.anibal.foodpandatest.dagger.module.AppModule;
import com.test.foodpanda.anibal.foodpandatest.dagger.module.MainActivityModule;
import com.test.foodpanda.anibal.foodpandatest.dagger.module.ServiceModule;
import com.test.foodpanda.anibal.foodpandatest.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anibal on 13/5/16.
 */
@Singleton
@Component(modules = {ServiceModule.class, AppModule.class})
public interface AppComponent {

    Gson provideGson();

    MainActivityComponent plus(MainActivityModule postActivityModule);
}
