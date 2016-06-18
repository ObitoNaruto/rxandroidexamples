package kurtis.rx.androidexamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class Example1Activity extends AppCompatActivity {

    RecyclerView mColorListView;
    SimpleStringAdapter mSimpleStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureLayout();
        createObservable();
    }

    private void createObservable() {
        //创建被观察者
        Observable<List<String>> listObservable = Observable.just(getColorList());//just里面不能是耗时操作，否则会立即执行并阻塞UI线程
        //Observer一旦订阅这个Observable就会立即调用onNext()方法并传入Observale.just()的参数，而后因为Observable没有数据可以发送了，OnComplete()才会被调用
        listObservable.subscribe(new Observer<List<String>>() {//订阅

            @Override
            public void onCompleted() {
                //这里我们不关心Observable何时完成数据的传输,所以不用早onComplete方法中写代码
            }

            @Override
            public void onError(Throwable e) {
                //这里也不会有异常抛出，所以也不用管onError方法
            }

            @Override
            public void onNext(List<String> colors) {
                mSimpleStringAdapter.setStrings(colors);
            }
        });

    }

    private void configureLayout() {
        setContentView(R.layout.activity_example_1);
        mColorListView = (RecyclerView) findViewById(R.id.color_list);
        mColorListView.setLayoutManager(new LinearLayoutManager(this));
        mSimpleStringAdapter = new SimpleStringAdapter(this);
        mColorListView.setAdapter(mSimpleStringAdapter);
    }

    private static List<String> getColorList() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("blue");
        colors.add("green");
        colors.add("red");
        colors.add("chartreuse");
        colors.add("Van Dyke Brown");
        return colors;
    }
}
