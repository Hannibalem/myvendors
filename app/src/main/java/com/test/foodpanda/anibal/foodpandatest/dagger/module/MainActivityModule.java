package com.test.foodpanda.anibal.foodpandatest.dagger.module;

import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.test.foodpanda.anibal.foodpandatest.api.VendorsResponse;
import com.test.foodpanda.anibal.foodpandatest.dagger.scope.ActivityScope;
import com.test.foodpanda.anibal.foodpandatest.main.MainActivity;
import com.test.foodpanda.anibal.foodpandatest.model.AppDatabase;
import com.test.foodpanda.anibal.foodpandatest.model.Vendor;
import com.test.foodpanda.anibal.foodpandatest.service.TestService;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Anibal on 13/5/16.
 */
@Module
public class MainActivityModule {

    private final String TAG = getClass().getName();

    private MainActivity mActivity;

    public MainActivityModule(MainActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    PublishSubject<Map<String, Object>> provideGetPublishSubject() {
        PublishSubject<Map<String, Object>> subject = PublishSubject.create();
        return subject;
    }

    @Provides
    @ActivityScope
    Subscription provideGetSubscription(final TestService service,
                                         final PublishSubject<Map<String, Object>> subject) {
        return subject
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Map<String,Object>, Observable<VendorsResponse>>() {
                    @Override
                    public Observable<VendorsResponse> call(final Map<String, Object> body) {
                        return service.getVendors(body)
                                .observeOn(AndroidSchedulers.mainThread())
                                .onErrorResumeNext(new Func1<Throwable, Observable<? extends VendorsResponse>>() {
                                    @Override
                                    public Observable<? extends VendorsResponse> call(Throwable throwable) {
                                        mActivity.onShowError();
                                        return Observable.empty();
                                    }
                                })
                                .observeOn(Schedulers.io())
                                .map(new Func1<VendorsResponse, VendorsResponse>() {
                                    @Override
                                    public VendorsResponse call(VendorsResponse response) {
                                        FlowManager.getDatabase(AppDatabase.class)
                                                .executeTransaction(
                                                        new ProcessModelTransaction.Builder<>(
                                                                new ProcessModelTransaction.ProcessModel<Vendor>() {
                                                                    @Override
                                                                    public void processModel(
                                                                            Vendor model) {
                                                                        model.save();
                                                                    }
                                                                })
                                                                .addAll(response.data.items)
                                                                .build());
                                        return response;
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VendorsResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "on error");
                        e.printStackTrace();
                        mActivity.onShowError();
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "on completed");
                    }

                    @Override
                    public void onNext(VendorsResponse response) {
                        mActivity.loadVendorsDB();
                    }
                });
    }
}
