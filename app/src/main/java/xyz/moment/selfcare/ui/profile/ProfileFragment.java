package xyz.moment.selfcare.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import xyz.moment.selfcare.database.UserLab;
import xyz.moment.selfcare.databinding.FragmentProfileBinding;
import xyz.moment.selfcare.model.User;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private RelativeLayout rlUserPhoto;
    private ImageView ivUserPhoto;
    private RelativeLayout rlUserName;
    private TextView tvUsername;
    private RelativeLayout rlPassword;
    private RelativeLayout rlGender;
    private TextView tvGender;
    private RelativeLayout rlBirthday;
    private TextView tvBirthday;
    private RelativeLayout rlHeight;
    private TextView tvHeight;
    private RelativeLayout rlWeight;
    private TextView tvWeight;
    private Button btnLogout;

    private User mUser;
    private UserLab userLab;
    private static final String PREFS_NAME = "MyPrefsFile";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        userLab = UserLab.get(getContext());
        mUser = profileViewModel.getMutableUser().getValue();

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initView(getContext());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initView(Context context) {
        rlUserPhoto = binding.rlUserPhoto;
        ivUserPhoto = binding.ivUserPhoto;
        rlUserName = binding.rlUserName;
        tvUsername = binding.tvUsernameProfile;
        rlPassword = binding.rlPassword;
        rlGender = binding.rlGender;
        tvGender = binding.tvGender;
        rlBirthday = binding.rlBirthday;
        tvBirthday = binding.tvBirthday;
        rlHeight = binding.rlHeight;
        tvHeight = binding.tvHeight;
        rlWeight = binding.rlWeight;
        tvWeight = binding.tvWeight;
        btnLogout = binding.btnLogout;

        tvUsername.setText(mUser.getUsername());
        tvBirthday.setText(mUser.getBirthday());
        tvGender.setText(mUser.getGender());
        tvWeight.setText(String.valueOf(mUser.getWeight()));
        tvHeight.setText(String.valueOf(mUser.getHeight()));

        rlUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

        rlUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername(context);
            }
        });

        rlPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(context);
            }
        });

        rlGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGender(context);
            }
        });

        rlBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBirthday(context);
            }
        });

        rlHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHeight(context);
            }
        });

        rlWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWeight(context);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUserInfo();
                getActivity().finish();
            }
        });
    }

    public void changePhoto() {
        //TODO
    }

    public void changeUsername(Context context) {
        showInputDialog(context, "修改用户名");
    }

    public void changeGender(Context context) {
        showInputDialog(context, "修改性别");
    }

    public void changePassword(Context context) {
        showInputDialog(context, "修改密码");
    }

    public void changeBirthday(Context context) {
        showInputDialog(context, "修改生日");
    }

    public void changeHeight(Context context) {
        showInputDialog(context, "修改身高");
    }

    public void changeWeight(Context context) {
        showInputDialog(context, "修改体重");
    }

    private void showInputDialog(Context context, String title) {
        //装入一个EditView
        final EditText editText = new EditText(context);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
        inputDialog.setTitle(title).setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(title.equals("修改用户名")) {
                            mUser.setUsername(editText.getText().toString());
                            tvUsername.setText(mUser.getUsername().trim());
                        }
                        else if(title.equals("修改性别")){
                            mUser.setGender(editText.getText().toString());
                            tvGender.setText(mUser.getGender());
                        }
                        else if(title.equals("修改生日")) {
                            editText.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                            mUser.setBirthday(editText.getText().toString());
                            tvBirthday.setText(mUser.getBirthday());
                        }
                        else if(title.equals("修改密码")) {
                            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mUser.setPassword(editText.getText().toString());
                        }
                        else if(title.equals("修改身高")) {
                            mUser.setHeight(Float.parseFloat(editText.getText().toString()));
                            tvHeight.setText(String.valueOf(mUser.getHeight()));
                        }
                        else if(title.equals("修改体重")) {
                            mUser.setWeight(Float.parseFloat(editText.getText().toString()));
                            tvWeight.setText(String.valueOf(mUser.getWeight()));
                        }

                        userLab.updateUser(mUser);
                        profileViewModel.setMutableUser(mUser);

                        clearUserInfo();
                        saveUserInfo(mUser);
                        Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ).show();
    }

    //将用户信息存储在SharedPreferences中
    private void saveUserInfo(User user) {
        SharedPreferences userInfo = getContext().getSharedPreferences(PREFS_NAME, 0);
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
        SharedPreferences userInfo = getContext().getSharedPreferences(PREFS_NAME, 0);
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
        SharedPreferences userInfo = getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = userInfo.edit();
        //editor.remove("username");
        editor.remove("password");
        editor.commit();
        Log.d(TAG, "removeUserInfo: 已移除用户密码！");
    }

    //从SharedPreferences中清除用户信息
    private void clearUserInfo() {
        SharedPreferences userInfo = getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.clear();
        editor.commit();
    }
}