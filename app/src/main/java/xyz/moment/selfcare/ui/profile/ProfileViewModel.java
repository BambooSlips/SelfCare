package xyz.moment.selfcare.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import xyz.moment.selfcare.databinding.FragmentHomeBinding;
import xyz.moment.selfcare.databinding.FragmentProfileBinding;
import xyz.moment.selfcare.model.User;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<User> mutableUser;

    public ProfileViewModel() {
        mutableUser = new MutableLiveData<>();

    }

    public void setMutableUser(User user) {
        mutableUser.setValue(user);
    }

    public LiveData<User> getMutableUser() {
        return mutableUser;
    }
}