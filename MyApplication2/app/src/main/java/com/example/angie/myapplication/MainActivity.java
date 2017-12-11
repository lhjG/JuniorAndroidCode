package com.example.angie.myapplication;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextInputLayout mNumberText;
    TextInputLayout mPassText;
    EditText mNumberEdit;
    EditText mPassEdit;
    ImageView image;
    RadioGroup mRadioGroup;
    RadioButton mRB1;
    RadioButton mRB2;
    Button mLogin;
    Button mRegister;
    String message1 = "拍摄";
    String message2="从相册选择";
    CharSequence title = "上传头像";
    String[] items = {message1,message2};


    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNumberText = (TextInputLayout)findViewById(R.id.more_number);
        mPassText = (TextInputLayout)findViewById(R.id.more_password);
        mRadioGroup=(RadioGroup)findViewById(R.id.RadioGroup);
        mRB1 = (RadioButton)findViewById(R.id.RB1);
        mRB2 = (RadioButton)findViewById(R.id.RB2);
        mLogin = (Button)findViewById(R.id.id9);
        mRegister = (Button)findViewById(R.id.id10);
        mNumberEdit=mNumberText.getEditText();
        mPassEdit=mPassText.getEditText();
        image = (ImageView)findViewById(R.id.id1);
        final AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbuilder.setTitle(title) //alertDialog.setMessage(message1);
//                alertDialog.setMessage(message2);
                            .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialoginterface,int i) {
                                    Toast.makeText(MainActivity.this, "您选择了[取消]", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setItems(items,new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialoginterface,int i) {
                                    Toast.makeText(MainActivity.this, "您选择了" + items[i], Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                }
            });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId) {
                if(checkedId == mRB1.getId()) {
                    Snackbar.make(mRadioGroup,"您选择了学生",Snackbar.LENGTH_SHORT)
                            .setAction("确认",new View.OnClickListener() {
                                @Override
                                public  void onClick(View v){
                                    Toast.makeText(MainActivity.this,"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else if(checkedId==mRB2.getId())
                {
                    Snackbar
                            .make(mRadioGroup,"您选择了教职工",Snackbar.LENGTH_SHORT)
                            .setAction("确认",new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v){
                                    Toast.makeText(MainActivity.this,"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String number = mNumberEdit.getText().toString();
                String password = mPassEdit.getText().toString();
                if(number.isEmpty()) {
                    mNumberText.setErrorEnabled(true);
                    mNumberText.setError("学号不能为空");
                }
                else
                    mNumberText.setErrorEnabled(false);
                if(password.isEmpty()) {
                    mPassText.setErrorEnabled(true);
                    mPassText.setError("密码不能为空");
                }
                else
                    mPassText.setErrorEnabled(false);
                if(number.equals("123456") && password.equals("6666"))
                {
                    Snackbar
                            .make(mLogin,"登录成功",Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener()
                            {
                                @Override
                                public  void onClick(View v){}
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else
                {
                    if(!(password.isEmpty())&&!(number.isEmpty()))
                    {
                        Snackbar
                            .make(mLogin,"学号或密码错误",Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener() {
                                @Override
                                public void onClick(View v){}
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                    }
                }
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(int i=0;i<mRadioGroup.getChildCount();i++)
                {
                    RadioButton RadioGroupChild = (RadioButton)mRadioGroup.getChildAt(i);
                    if(RadioGroupChild.isChecked())
                    {
                        if(i==0)
                        {
                            Snackbar
                                    .make(mRegister,"学生注册功能尚未启用",Snackbar.LENGTH_SHORT)
                                    .setAction("确定",new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v){}
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                                    .setDuration(5000)
                                    .show();
                        }
                        else {
                            Snackbar
                                    .make(mRegister,"教职工注册功能尚未启用",Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }
            }
        });
    }
}
