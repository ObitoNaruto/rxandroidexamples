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
        //�������۲���
        Observable<List<String>> listObservable = Observable.just(getColorList());//just���治���Ǻ�ʱ���������������ִ�в�����UI�߳�
        //Observerһ���������Observable�ͻ���������onNext()����������Observale.just()�Ĳ�����������ΪObservableû�����ݿ��Է����ˣ�OnComplete()�Żᱻ����
        listObservable.subscribe(new Observer<List<String>>() {//����

            @Override
            public void onCompleted() {
                //�������ǲ�����Observable��ʱ������ݵĴ���,���Բ�����onComplete������д����
            }

            @Override
            public void onError(Throwable e) {
                //����Ҳ�������쳣�׳�������Ҳ���ù�onError����
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
