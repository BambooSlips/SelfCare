package xyz.moment.selfcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import xyz.moment.selfcare.database.UserLab;
import xyz.moment.selfcare.databinding.ActivityMainBinding;
import xyz.moment.selfcare.model.User;
import xyz.moment.selfcare.ui.profile.ProfileViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private NavigationView navView ;
    private ImageView ivPhoto;
    private TextView tvUsername;
    private TextView tvSubtitle;

    private ProfileViewModel profileViewModel;
    private UserLab userLab;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLab = UserLab.get(this);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("UID");
        user = userLab.getUser(uid);
        /*String username = intent.getStringExtra("username");
        String gender = intent.getStringExtra("gender");
        String birthday = intent.getStringExtra("birthday");
        String password = intent.getStringExtra("password");
        float height = intent.getFloatExtra("height", 0);
        float weight = intent.getFloatExtra("weight", 0);
        User user = new User(uid, username, password, gender, birthday, height, weight);*/
        profileViewModel.setMutableUser(user);

        Log.d(TAG, "onCreate: profileViewModel.getMutableUser().getValue().getUsername()="+profileViewModel.getMutableUser().getValue().getUsername());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_habit, R.id.nav_record, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void initView() {

        /*Intent intent = getIntent();
        String uid = intent.getStringExtra("UID");
        String username = intent.getStringExtra("username");
        String gender = intent.getStringExtra("gender");
        String birthday = intent.getStringExtra("birthday");
        String password = intent.getStringExtra("password");
        float height = intent.getFloatExtra("height", 0);
        float weight = intent.getFloatExtra("weight", 0);*/

        navView = findViewById(R.id.nav_view);
        if (navView.getHeaderCount() > 0){
            //获取NavigationView中header布局中的view
            View header = navView.getHeaderView(0);

            //获取header中的控件，而不是直接findViewById
            tvUsername = header.findViewById(R.id.tvUsername);
            ivPhoto = header.findViewById(R.id.ivPhoto);
            //tvSubtitle = header.findViewById(R.id.tvSubtitle);

            tvUsername.setText(user.getUsername());
            //tvSubtitle.setText("");
        }
    }

    public void initData() {

    }

}