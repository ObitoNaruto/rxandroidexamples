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
 * �첽����
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
        //ʹ��Observable.fromCallable����������ô���
        //1.��ȡҪ���͵����ݵĴ���ֻ������Observer����֮��ִ�� 2.��ȡ���ݵĴ�����������߳���ִ��
        Observable<List<String>> tvShowObservable = Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return mRestClient.getFavoriteTvShows();//ģ�������ʱ����
            }
        });


        mTvShowSubscription = tvShowObservable
                .subscribeOn(Schedulers.io())//ͨ��subscribeOn������ʹ��call����ص�������io�߳���ִ��
                .observeOn(AndroidSchedulers.mainThread())//ͨ��observeOn������ʹ�ÿ�����UI�߳��й۲����Observale��Ҳ����OnNext��UI�߳���ִ��
                .subscribe(//����tvShowObservable���Observable
                        new Observer<List<String>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<String> tvShows) {
                                //����UI
                                displayTvShows(tvShows);
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTvShowSubscription != null && !mTvShowSubscription.isUnsubscribed()) {
            //ͨ������unsubscribe��������Observable�������͵����ݲ��ٱ�Observer�����ա������ڵ���unsubscribe���������Ǵ�����Observer�Ͳ����ܵ������ˣ�
            // �����Activityִ�н������̲߳Ž������������ڴ�й©�Լ���ָ���쳣
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
