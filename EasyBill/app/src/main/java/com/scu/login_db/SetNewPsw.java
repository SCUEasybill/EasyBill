package com.scu.login_db;
import static com.scu.login_db.ConstantsUtil.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/1.
 */
public class SetNewPsw extends AppCompatActivity{
    //传入参数
    String action = null;
    String ph_emNum = null;

    EditText edtOldPsw, edtNewPsw1, edtNewPsw2;
    String oldPsw, newPsw1,newPsw2;
    ProgressDialog ps;
    MyConnector mc = null;
    Toolbar setPswTitlebar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//加入自定义标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_forget_findpsw);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");//获取登陆状态
        ph_emNum = intent.getStringExtra("ph_emNum");
        Toast.makeText(SetNewPsw.this,"action=="+action+ph_emNum,Toast.LENGTH_SHORT).show();
        //添加titlebar[开始]
        setPswTitlebar = (Toolbar) findViewById(R.id.register_titlebar);
        if(action.equals(REGISTER)){//没有登陆，不显示旧密码
            setPswTitlebar.setTitle("注册");
//            LinearLayout linearLayout_oldPsw = (LinearLayout) findViewById(R.id.layout_oldPsw);
            findViewById(R.id.layout_oldPsw).setVisibility(View.GONE);
            Toast.makeText(SetNewPsw.this,"不显示旧密码",Toast.LENGTH_SHORT).show();
        }else if(action.equals(FINDPASSWORD)){
            setPswTitlebar.setTitle("找回密码");
            findViewById(R.id.layout_name).setVisibility(View.GONE);
            Toast.makeText(SetNewPsw.this,"不显示昵称框",Toast.LENGTH_SHORT).show();
        }
//        setPswTitlebar.setSubtitle("修改密码");

        setSupportActionBar(setPswTitlebar);
        //返回上一层
        setPswTitlebar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        setPswTitlebar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setPswTitlebar.setOnMenuItemClickListener(onMenuitemClick);
        //添加titlebar[结束]
        init();
    }

    private Toolbar.OnMenuItemClickListener onMenuitemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String msg = "";
            switch (item.getItemId()){
                case R.id.menu_findpsw_finish:
                    oldPsw = edtOldPsw.getText().toString().trim();
                    newPsw1 = edtNewPsw1.getText().toString().trim();
                    newPsw2 = edtNewPsw2.getText().toString().trim();
                    if( newPsw1.equals("") || newPsw2.equals("")||(oldPsw.equals("")&& action.equals(FINDPASSWORD))){//当已经登陆并且未填写旧密码
                        Toast.makeText(SetNewPsw.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(!newPsw1.equals(newPsw2)){
                        Toast.makeText(SetNewPsw.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    ModifyPsw modifyPsw = new ModifyPsw();
                    if (action.equals(FINDPASSWORD)){
                        modifyPsw.execute(new String []{ph_emNum,newPsw1, oldPsw});
                    }else if(action.equals(REGISTER)){
                        modifyPsw.execute(new String[]{ph_emNum,newPsw1});
                    }
                default:
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_findpsw_titlebar, menu);
        return true;
    }
    private void init() {
        edtOldPsw = (EditText) findViewById(R.id.edt_oldPsw);
        edtNewPsw1 =(EditText) findViewById(R.id.edt_newPsw1);
        edtNewPsw2=(EditText) findViewById(R.id.edt_newPsw2);

    }

    /**
     * 处理修改密码
     * 参数：ph_emNum,newPsw1, oldPsw
     */
    class ModifyPsw extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ps.show(SetNewPsw.this,"重置密码","请稍后...",false,true);
        }
        @Override
        protected String doInBackground(String... params) {
            //执行后台操作
            String result = null;
            String msg = null;
            if(mc == null){
                mc = new MyConnector(SERVER_ADDRESS,SERVER_PORT);
            }
            try {
                if (action.equals(REGISTER)){
                    msg = "<#RGISTER#>"+params[0]+params[1];//传送手机号或者邮箱号，新密码
                } else if(action.equals(FINDPASSWORD)){
                    msg = "<#FINDPASSWORD#>"+params[0]+"|"+params[1]+"|"+params[2];//传送手机号或者邮箱号，新密码，旧密码
                }
                mc.dout.writeUTF(msg);
                String recvMsg = mc.din.readUTF();
                result = recvMsg;
            } catch (IOException e) {//数据操作失败
                e.printStackTrace();
                result = "fail";
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //对UI进行操作
        }

        @Override
        protected void onPostExecute(String result) {
            ps.dismiss();
            if(result.startsWith("<#FINDPASSWORDSUCCESS#>")){
                Toast.makeText(SetNewPsw.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetNewPsw.this, Login.class);
                startActivity(intent);
            }else if(result.startsWith("<#REG_SUCCESS#>")){
                Toast.makeText(SetNewPsw.this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetNewPsw.this,Login.class);
                startActivity(intent);
            }
        }
    }
}
