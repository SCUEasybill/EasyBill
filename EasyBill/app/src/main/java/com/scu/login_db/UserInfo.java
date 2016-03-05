package com.scu.login_db;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/5.
 */
public class UserInfo extends AppCompatActivity{
    private static final String[] strs = new String[] {
        "first", "second", "third", "fourth", "fifth"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_userinfo);
        Toolbar toolbarUser = (Toolbar) findViewById(R.id.toolbar_userinfo);
        setSupportActionBar(toolbarUser);

        toolbarUser.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbarUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.this.finish();
            }
        });

//        ListView lv = (ListView) findViewById(R.id.lv_userinfo);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
//        lv.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, strs));
////        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
////            @Override
////            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////                return false;
////            }
////        });
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                   long arg3) {
//                //点击后在标题上显示点击了第几行
//                        setTitle("你点击了第"+arg2+"行");
//            }
//        });
    }

    @Override
    public void finish() {
        super.finish();
    }
}
