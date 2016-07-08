package com.test.foodpanda.anibal.foodpandatest.dagger.component;

import com.test.foodpanda.anibal.foodpandatest.dagger.module.MainActivityModule;
import com.test.foodpanda.anibal.foodpandatest.dagger.scope.ActivityScope;
import com.test.foodpanda.anibal.foodpandatest.main.MainActivity;

import dagger.Subcomponent;

/**
 * Created by Anibal on 13/5/16.
 */
@ActivityScope
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivityComponent {

    void inject(MainActivity activity);
}
