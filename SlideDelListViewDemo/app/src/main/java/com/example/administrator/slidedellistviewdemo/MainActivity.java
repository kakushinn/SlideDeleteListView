package com.example.administrator.slidedellistviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlideDeleteListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (SlideDeleteListView) findViewById(R.id.id_listview);
        // 不要直接Arrays.asList
        mDatas = new ArrayList<String>(Arrays.asList("HelloWorld", "Welcome", "Java", "Android", "Servlet", "Struts",
                "Hibernate", "Spring", "HTML5", "Javascript", "Lucene"));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);

        mListView.setDeleteItemListener(new SlideDeleteListView.DeleteItemListener() {
            @Override
            public void deleteItem(int position) {
                mAdapter.remove(mAdapter.getItem(position));
            }
        });
//        {
//            @Override
//            public void clickHappend(final int position)
//            {
//                Toast.makeText(MainActivity.this, position + " : " + mAdapter.getItem(position), 1).show();
//                mAdapter.remove(mAdapter.getItem(position));
//            }
//        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(MainActivity.this, position + " : " + mAdapter.getItem(position), 1).show();
            }
        });
    }
}
