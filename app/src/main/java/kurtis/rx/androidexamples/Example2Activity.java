package kurtis.rx.androidexamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 异步加载
 */
public class Example2Activity extends AppCompatActivity {

    private Subscription mTvShowSubscription;
    private RecyclerView mTvShowListView;
    private ProgressBar mProgressBar;
    private SimpleStringAdapter mSimpleStringAdapter;
    private RestClient mRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestClient = new RestClient(this);
        configureLayout();
        createObservable();
    }

    private void createObservable() {
        //使用Observable.fromCallable方法有两点好处：
        //1.获取要发送的数据的代码只会在有Observer订阅之后执行 2.获取数据的代码可以在子线程中执行
        Observable<List<String>> tvShowObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return mRestClient.getFavoriteTvShows();//模拟网络耗时操作
            }
        });


        mTvShowSubscription = tvShowObservable
                .subscribeOn(Schedulers.io())//通过subscribeOn方法，使用call这个回调方法在io线程中执行
                .observeOn(AndroidSchedulers.mainThread())//通过observeOn方法，使得可以在UI线程中观察这个Observale，也就是OnNext在UI线程中执行
                .subscribe(//订阅tvShowObservable这个Observable
                        new Observer<List<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<String> tvShows) {
                                //更新UI
                                displayTvShows(tvShows);
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTvShowSubscription != null && !mTvShowSubscription.isUnsubscribed()) {
            //通过调用unsubscribe方法告诉Observable它所发送的数据不再被Observer所接收。即，在调用unsubscribe方法后，我们创建的Observer就不会受到数据了，
            // 解决了Activity执行结束后线程才结束所引发的内存泄漏以及空指针异常
            mTvShowSubscription.unsubscribe();
        }
    }

    private void displayTvShows(List<String> tvShows) {
        mSimpleStringAdapter.setStrings(tvShows);
        mProgressBar.setVisibility(View.GONE);
        mTvShowListView.setVisibility(View.VISIBLE);
    }

    private void configureLayout() {
        setContentView(R.layout.activity_example_2);
        mProgressBar = (ProgressBar) findViewById(R.id.loader);
        mTvShowListView = (RecyclerView) findViewById(R.id.tv_show_list);
        mTvShowListView.setLayoutManager(new LinearLayoutManager(this));
        mSimpleStringAdapter = new SimpleStringAdapter(this);
        mTvShowListView.setAdapter(mSimpleStringAdapter);
    }
}
