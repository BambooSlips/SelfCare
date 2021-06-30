package xyz.moment.selfcare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import xyz.moment.selfcare.database.HabitLab;
import xyz.moment.selfcare.model.Habit;
import xyz.moment.selfcare.model.User;
import xyz.moment.selfcare.database.UserLab;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private ImageButton theEye;
    private Button btnLogin;
    private CheckBox cbRememberMe;
    private TextView tvRegister;

    private UserLab mUserLab;
    private HabitLab mHabitLab;
    private User user;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏导航栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_login);

        if(isAccountSaved()) {
            Log.d(TAG, "onCreate: username="+user.getUsername());
            passUserInfo();
        }
        else {
            initViews();
            initData();
            //login();
        }
    }

    public boolean isAccountSaved() {
        String[] userInfo = getUserInfo();
        if(userInfo[0]!= null && !"".equals(userInfo[0])) {
            user = new User(userInfo[0],userInfo[1],userInfo[2],userInfo[3],userInfo[4],
                    Float.parseFloat(userInfo[5]),Float.parseFloat(userInfo[6]));
            return true;
        }
        else
            return false;
    }

    public void passUserInfo() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("UID", user.getUID());
        intent.putExtra("username", user.getUsername());
        intent.putExtra("password", user.getPassword());
        intent.putExtra("gender", user.getGender());
        intent.putExtra("birthday", user.getBirthday());
        intent.putExtra("height", user.getHeight());
        intent.putExtra("weight", user.getWeight());
        startActivity(intent);
    }

    public void initViews() {
        this.theEye = (ImageButton) findViewById(R.id.eye_visible);
        this.etUsername = (EditText) findViewById(R.id.username);
        this.etPassword = (EditText) findViewById(R.id.password);
        this.btnLogin = (Button) findViewById(R.id.butLogin);
        this.cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);
        this.tvRegister = (TextView) findViewById(R.id.tvRegister);

        //设置密码显示/隐藏
        theEye.setOnClickListener(new View.OnClickListener() {
            boolean open = false;
            @Override
            public void onClick(View v) {
                if(!open) {
                    theEye.setImageResource(R.drawable.eye_open);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    open = true;
                }
                else {
                    theEye.setImageResource(R.drawable.eye_closed);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    open = false;
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                login();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initData() {
        //创建数据库
        mUserLab = UserLab.get(this);
        mHabitLab = HabitLab.get(this);

    }

    public void login() {
        if("".equals(username) || username == null) {
            Toast.makeText(LoginActivity.this, "用户名不可为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if("".equals(password) || password == null) {
            Toast.makeText(LoginActivity.this, "密码不可为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mUserLab.isAUser(username, password)) {
            if(cbRememberMe.isChecked()) {
                user = mUserLab.getUser(username, password);
                saveUserInfo(user);
                //Toast.makeText(LoginActivity.this, "我记住你了！",Toast.LENGTH_SHORT).show();
            }
            else {
                removeUserInfo();
                //Toast.makeText(LoginActivity.this, "我要忘记你～",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(LoginActivity.this, "欢迎回来，"+username+"!",Toast.LENGTH_SHORT).show();
            passUserInfo();
        }
        else {
            Toast.makeText(LoginActivity.this, "用户名或密码错误!", Toast.LENGTH_SHORT).show();
        }
    }

    //将用户信息存储在SharedPreferences中
    private void saveUserInfo(User user) {
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.OnSharedPreferenceChangeListener changeListener =
                new SharedPreferences.OnSharedPreferenceChangeListener(){
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                    }
                };

        userInfo.registerOnSharedPreferenceChangeListener(changeListener);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("UID", user.getUID());
        editor.putString("username",user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("gender",user.getUsername());
        editor.putString("birthday", user.getPassword());
        editor.putString("height",""+user.getHeight());
        editor.putString("weight", ""+user.getWeight());
        editor.commit();
        Log.d(TAG, "saveUserInfo: 用户信息已保存！");
    }

    //从SharedPreferences中获取用户信息 0-6
    private String[] getUserInfo() {
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String UID = userInfo.getString("UID", null);
        String username = userInfo.getString("username", null);
        String password = userInfo.getString("password", null);
        String gender = userInfo.getString("gender", null);
        String birthday = userInfo.getString("birthday", null);
        String height = userInfo.getString("height", null);
        String weight = userInfo.getString("weight", null);

        Log.d(TAG, "getUserInfo: 读取到用户信息！");
        Log.d(TAG, "getUserInfo: [username:"+username+"]--[password:"+password+"]");

        return new String[]{UID,username,password,gender,birthday, height, weight};
    }

    //从SharedPreferences中移除用户密码
    private void removeUserInfo() {
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        //editor.remove("username");
        editor.remove("password");
        editor.commit();
        Log.d(TAG, "removeUserInfo: 已移除用户密码！");
    }

    //从SharedPreferences中清除用户信息
    private void clearUserInfo() {
        SharedPreferences userInfo = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.clear();
        editor.commit();
    }
}