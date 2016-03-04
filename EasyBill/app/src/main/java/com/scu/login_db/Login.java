package com.scu.login_db;

import static com.scu.login_db.ConstantsUtil.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import xyz.anmai.easybill.MainActivity;
import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/1/29.
 */
public class Login extends AppCompatActivity {
    MyConnector mc;
    Button btn_login,btn_cancle;
    EditText edt_account,edt_pwd,etUid,etPwd;
    TextView tv_register,tv_findPswPh,tv_registerEm;
    ProgressDialog pd;
    String uid, pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        checkIfRemember();
        //登陆
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUid = (EditText)findViewById(R.id.edt_account);	//获得帐号EditText
                etPwd = (EditText)findViewById(R.id.edt_psw);	//获得密码EditText
                uid = etUid.getEditableText().toString().trim();	//获得输入的帐号
                pwd = etPwd.getEditableText().toString().trim();	//获得输入的密码
                if(uid.equals("") || pwd.equals("")){		//判断输入是否为空
                    Toast.makeText(Login.this, "请输入帐号或密码!", Toast.LENGTH_SHORT).show();//输出提示消息
                    return;
                }
                login();
            }
        });

        //手机注册
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(Login.this, FindPasswordPh_Em.class);
                intent.putExtra("action",REGISTER);
                startActivity(intent);
            }
        });

        //忘记密码,用手机号找回
        tv_findPswPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FindPasswordPh_Em.class);
                intent.putExtra("action",FINDPASSWORD);
                startActivity(intent);
            }
        });
        //邮箱注册
        tv_registerEm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FindPasswordPh_Em.class);
                intent.putExtra("action",FINDPASSWORD);
                startActivity(intent);
            }
        });
        //邮箱找回密码


    }

    /**
     * 初始化界面
     */
    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_cancle = (Button) findViewById(R.id.btn_cancel);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_findPswPh = (TextView) findViewById(R.id.tv_findPswPh);
        tv_registerEm = (TextView) findViewById(R.id.tv_registerEm);

    }
    /**
     * 登陆
     */
    public void login(){
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                try {
                    if (mc == null) {
                        mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
                    }
                    pd = ProgressDialog.show(Login.this, "请稍候", "正在连接服务器...", false, true);
                    String msg = "<#LOGIN#>" + uid + "|" + pwd;                    //组织要返回的字符串
                    mc.dout.writeUTF(msg);                                        //发出消息
                    String receivedMsg = mc.din.readUTF();        //读取服务器发来的消息
                    pd.dismiss();
                    if (receivedMsg.startsWith("<#LOGIN_SUCCESS#>")) {    //收到的消息为登录成功消息
                        receivedMsg = receivedMsg.substring(17);
                        String[] sa = receivedMsg.split("\\|");
                        CheckBox cb = (CheckBox) findViewById(R.id.cb_remember);        //获得CheckBox对象
                        if (cb.isChecked()) {
                            rememberMe(uid, pwd);
                        }
                        pd = ProgressDialog.show(Login.this, "请稍后...", "正在同步...", false, false);
                        initApp();
                        pd.dismiss();
                        //转到主界面
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("uno", sa[0]);//传入的是用户ID号sa[0]
                        startActivity(intent);
                        finish();
                    } else if (msg.startsWith("<#LOGIN_FAIL#>")) {                    //收到的消息为登录失败
                        Toast.makeText(Login.this, msg.substring(14), Toast.LENGTH_LONG).show();
                        Looper.loop();
                        Looper.myLooper().quit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //方法：初始化整个app
    private void initApp() {

    }

    //方法：将用户的id和密码存入Preferences
    public void rememberMe(String uid,String pwd){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
        SharedPreferences.Editor editor = sp.edit();			//获得Editor
        editor.putString("uid", uid);							//将用户名存入Preferences
        editor.putString("pwd", pwd);							//将密码存入Preferences
        editor.commit();
    }
    //方法：从Preferences中读取用户名和密码
    public void checkIfRemember(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);	//获得Preferences
        String uid = sp.getString("uid", null);
        String pwd = sp.getString("pwd", null);
        if(uid != null && pwd!= null){
            EditText etUid = (EditText)findViewById(R.id.edt_account);
            EditText etPwd = (EditText)findViewById(R.id.edt_psw);
            CheckBox cbRemember = (CheckBox)findViewById(R.id.cb_remember);
            etUid.setText(uid);
            etPwd.setText(pwd);
            cbRemember.setChecked(true);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if(mc != null){
            mc.sayBye();
        }
        super.onDestroy();
    }
}
