package xyz.moment.selfcare;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import xyz.moment.selfcare.model.User;
import xyz.moment.selfcare.database.UserLab;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Context mContext;
    private SQLiteDatabase mDataBase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏导航栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_register);

        Button register = (Button)findViewById(R.id.register);
        TextView textView_username = findViewById(R.id.username_reg);
        TextView textView_password1 = findViewById(R.id.password1_reg);
        TextView textView_password2 = findViewById(R.id.password2_reg);
        EditText date_birth = findViewById(R.id.date_birth);
        RadioGroup radioGroup_genderGroup = findViewById(R.id.gender);
        CheckBox checkBox_agree = findViewById(R.id.agree);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get needed info
                String username = textView_username.getText().toString();
                String password1 = textView_password1.getText().toString();
                String password2 = textView_password2.getText().toString();
                String birthday = date_birth.getText().toString();
                int cg = radioGroup_genderGroup.getCheckedRadioButtonId();
                RadioButton chosenGender = null;
                if(cg >= 0)
                    chosenGender = findViewById(radioGroup_genderGroup.getCheckedRadioButtonId());
                String gender = chosenGender==null ? "" : chosenGender.getText().toString();
                boolean agreed =  checkBox_agree.isChecked();
                Log.d(TAG, "onClick: "+username+password1+password2+birthday+gender+agreed);

                //check the form
                if(agreed) {
                    if(!"".equals(username) && username!=null) {
                        if(!"".equals(birthday) && birthday!=null) {
                            if(!"".equals(gender) && gender != null) {
                                if( password1 != null && !"".equals(password1)) {
                                    if(password1.equals(password2)) {
                                        Toast.makeText(RegisterActivity.this, "欢迎～", Toast.LENGTH_SHORT).show();
                                        User user = new User(username, password1, gender, birthday);
                                        UserLab mUserLab = UserLab.get(RegisterActivity.this);
                                        mUserLab.addUser(user);
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "两次密码输入不同", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "密码不可为空", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "性别不可为空", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "你是60后吗？", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "用户名是不可为空", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "请查看用户协议...", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}