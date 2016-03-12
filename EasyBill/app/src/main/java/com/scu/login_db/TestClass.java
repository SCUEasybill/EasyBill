package com.scu.login_db;
import  static com.scu.login_db.ConstantsUtil.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import xyz.anmai.easybill.R;

/**
 * Created by nevermind on 2016/3/8.
 */
public class TestClass extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Button btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users("1","guyu","13678109397",null,0,null);
                Intent intent = new Intent(TestClass.this,UserInfo.class);
                intent.putExtra("user",user);
                intent.putExtra("islogin",NOLOGIN);
                startActivity(intent);
                finish();
            }
        });
    }
}
