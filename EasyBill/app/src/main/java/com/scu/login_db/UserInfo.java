package com.scu.login_db;

import static com.scu.login_db.ConstantsUtil.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.anmai.easybill.MainActivity;
import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/5.
 */
public class UserInfo extends AppCompatActivity {

    private List<UserInfoDetails> userInfoDetailsList = new ArrayList<UserInfoDetails>();
    /*定义一个动态数组*/
    ArrayList<HashMap<String, Object>> listItem;
    MyConnector mc = null;
    HashMap<String, Object> map;
    ProgressDialog ps;
    String edtInfo_dialog = null;
    UserInfoAdapter userInfoAdapter;
    //外传入数据
    String isLogin = null;
    Users user;
    Looper modifyLooper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_userinfo);
        Intent intent = getIntent();
        user = (Users) intent.getSerializableExtra("user");
        isLogin = intent.getStringExtra("islogin");
        Toast.makeText(UserInfo.this, "jieguo" + user.getUser_id() + isLogin, Toast.LENGTH_LONG).show();
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
        userInfoAdapter= new UserInfoAdapter(UserInfo.this, R.layout.login_userinfo_details, userInfoDetailsList);
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
//                initUserInfo();
//                userInfoAdapter.notifyDataSetChanged();
                return true;
            }
        });
        Button btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
//                new AlertDialog.Builder(UserInfo.this)
//                        .setTitle("退出")
//                        .setMessage("确认要退出吗？")
//                        .setPositiveButton(
//                                "确定",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        //退出登录
////                                        android.os.Process.killProcess(android.os.Process.myPid());        //结束进程，退出程序
//                                        Toast.makeText(getApplicationContext(), "退出登录", Toast.LENGTH_LONG).show();
//                                    }
//                                })
//                        .setNegativeButton(
//                                "取消",
//                                null)
//                        .show();
            }
        });
    }

    private void logout() {
        //传送未登录状态字给主页面
        Intent intent = new Intent(UserInfo.this, MainActivity.class);
        intent.putExtra("islogin", NOLOGIN);
        startActivity(intent);
    }

    //初始化用户信息表
    private void initUserInfo() {
//        UserInfoDetails name = new UserInfoDetails("昵称:", "guyu");
        UserInfoDetails name = new UserInfoDetails("昵称:", user.getUser_name());
        userInfoDetailsList.add(name);
        UserInfoDetails phone = new UserInfoDetails("手机号:", user.getUser_phone());
//        UserInfoDetails phone = new UserInfoDetails("手机号:", "13678109397");
        userInfoDetailsList.add(phone);
        UserInfoDetails email = new UserInfoDetails("邮箱:", user.getUser_email());
//        UserInfoDetails email = new UserInfoDetails("邮箱:", "972042723@qq.com");
        userInfoDetailsList.add(email);
        UserInfoDetails total_money = new UserInfoDetails("总金额:", Integer.toString(user.getUser_total_money()));//需要重新再加入一个listView
        userInfoDetailsList.add(total_money);
    }

    public void showModifyDialog(final int position) {
        String title = null;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.login_userinfo_modify_dialog, (ViewGroup) findViewById(R.id.layout_dialog));
        final EditText editText = (EditText) layout.findViewById(R.id.edt_dialog);
        switch (position) {
            case 0:
                title = "请输入昵称:";
                break;
            case 1:
                title = "请输入新手机号:";
                break;
            case 2:
                title = "请输入新邮箱号:";
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
                                edtInfo_dialog = editText.getEditableText().toString().trim();
//                                ps = ProgressDialog.show(getApplicationContext(),"正在修改","请稍后...",false,true);
                                modifyInfo(edtInfo_dialog, position);
                                Toast.makeText(getApplicationContext(), "可以进行修改信息" + user.getUser_id() + edtInfo_dialog, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton(
                        "取消",
                        null)
                .show();
    }

    /**
     * 在线程中处理后台信息，传入参数:用户ID，输入内容，邮箱或者手机（position）
     */
    private void modifyInfo(final String edtInfo_dialog, final int position) {
        switch (position) {
            case 1:
                if (Zhengzebiaoda.isMobilNO(edtInfo_dialog)) {
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (Zhengzebiaoda.isEmailNO(edtInfo_dialog)) {
                    Toast.makeText(getApplicationContext(), "请输入正确的邮箱号", Toast.LENGTH_LONG).show();
                }
                break;
            default:

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                modifyLooper = Looper.myLooper();
                String msg = null;
                if (mc == null) {
                    mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
                }
                try {
                    switch (position) {
                        case 0://昵称
                            msg = "<#MODIFYUSERINFO#>" + "nickName" + "|" + user.getUser_id() + "|" + edtInfo_dialog;
                            break;
                        case 1://手机号
                            msg = "<#MODIFYUSERINFO#>" + "phone" + "|" + user.getUser_id() + "|" + edtInfo_dialog;
                            break;
                        case 2://邮箱号
                            msg = "<#MODIFYUSERINFO#>" + "email" + "|" + user.getUser_id() + "|" + edtInfo_dialog;
                            break;
                        default:
                    }
                    mc.dout.writeUTF(msg);//向服务器发送修改的内容
                    String receiveMsg = mc.din.readUTF();//读取返回的消息
//                    ps.dismiss();
                    if (receiveMsg.startsWith("<#MODIFYUSERINFOSUCCESS#>")) {
                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        //更新用户信息 ,这部分可以进行再一部的短信或者手机验证
                        user.setUser_email("18428367678");
//                        Message msg_handle = new Message();
//                        msg_handle.what = 1;
//                        userHandler.sendMessage(msg_handle);
                    } else {
                        Toast.makeText(getApplicationContext(), "修改失败，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //在handle中处理UI
    public Handler userHandler = new Handler() {
        public void handlemessage(Message msg) {
            switch (msg.what) {
                case 1:

                default:
            }
        }
    };

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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modifyLooper != null) {
            modifyLooper.quit();
        }
        modifyLooper = null;
        if (mc != null) {
            mc.sayBye();
        }
    }
}
