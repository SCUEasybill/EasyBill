package com.scu.login_db;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/5.
 */
public class UserInfo extends AppCompatActivity {

    private List<UserInfoDetails> userInfoDetailsList = new ArrayList<UserInfoDetails>();
    /*定义一个动态数组*/
    ArrayList<HashMap<String, Object>> listItem;
    HashMap<String, Object> map;

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

        initUserInfo();
        UserInfoAdapter userInfoAdapter = new UserInfoAdapter(UserInfo.this, R.layout.login_userinfo_details, userInfoDetailsList);
        ListView listView = (ListView) findViewById(R.id.lv_userinfo);
        listView.setAdapter(userInfoAdapter);

//        ListView lv = (ListView) findViewById(R.id.lv_userinfo);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
//        listItem = new ArrayList<HashMap<String, Object>>();//数组中存放数据
//            map = new HashMap<String, Object>();
//            //添加数据
//            map.put("item_key", "第1行");
//            map.put("item_value", "这是第1行");
//            listItem.add(map);
//            map = new HashMap<String, Object>();
//            map.put("item_key", "第2行");
//            map.put("item_value", "这是第2行");
//            listItem.add(map);
        //简单添加适配器
//        ArrayAdapter adapter =  new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, strs);
//        lv.setAdapter(adapter);
        //自定义添加适配器，baseAdapter
//        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
//        lv.setAdapter(mAdapter);//为ListView绑定Adapter /*为ListView添加点击事件*/
        //使用simpleAdapter 适配器
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItem, R.layout.login_userinfo_details, new String[]{"item_key", "item_value"}, new int[]{R.id.tv_userInfo_key, R.id.tv_userInfo_value});
//        lv.setAdapter(simpleAdapter);

        //添加长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "长按" + "parent" + parent + "position" + position + "id" + id, Toast.LENGTH_LONG).show();
                showModifyDialog(position);
                return true;
            }
        });
    }

    //初始化用户信息表
    private void initUserInfo() {
        UserInfoDetails phone = new UserInfoDetails("手机号:", "13678109397");
        userInfoDetailsList.add(phone);
        UserInfoDetails email = new UserInfoDetails("邮箱:", "972042723@qq.com");
        userInfoDetailsList.add(email);
    }

    public void showModifyDialog(int position) {
        String title = null;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.login_userinfo_modify_dialog, (ViewGroup) findViewById(R.id.layout_dialog));
        switch (position) {
            case 0:
                title = "请输入新手机号:";
                break;
            case 1:
                title = "请输入新邮箱号:";
                break;
            default:

        }
        new AlertDialog.Builder(UserInfo.this)
                .setView(layout)
                .setMessage(title)
                .setPositiveButton(
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //开启线程修改信息
                                Toast.makeText(getApplicationContext(), "可以进行修改信息", Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton(
                        "取消",
                        null)
                .show();
    }

    public class UserInfoDetails {
        private String key;
        private String value;

        public UserInfoDetails(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public class UserInfoAdapter extends ArrayAdapter<UserInfoDetails> {
        private int resourceId;

        public UserInfoAdapter(Context context, int textViewResourceId, List<UserInfoDetails> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UserInfoDetails userInfoDetails = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.key = (TextView) view.findViewById(R.id.tv_userInfo_key);
                viewHolder.value = (TextView) view.findViewById(R.id.tv_userInfo_value);
                view.setTag(viewHolder);//将ViewHolder存储到View中
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
            }
            viewHolder.key.setText(userInfoDetails.getKey());
            viewHolder.value.setText(userInfoDetails.getValue());
            return view;
        }

        class ViewHolder {
            TextView key;
            TextView value;
        }
    }


    @Override
    public void finish() {
        super.finish();
    }
}
