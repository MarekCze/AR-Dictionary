package ie.lit.ardictionary.ui.auth;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

import ie.lit.ardictionary.model.User;
import ie.lit.ardictionary.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository authRepository;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<User> userMutableLiveData;

    public AuthViewModel(@NonNull Application application){
        super(application);

        authRepository = new AuthRepository(application);
        firebaseUserMutableLiveData = authRepository.getFirebaseUserMutableLiveData();
        userMutableLiveData = authRepository.getUserMutableLiveData();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void signInWithGoogle(Context context, GoogleSignInAccount account){
        authRepository.signInWithGoogle(context, account);
    }

    public void setFirebaseUserMutableLiveData(FirebaseUser user) {
        firebaseUserMutableLiveData.postValue(user);
    }

    public void createUserWithEmailAndPassword(String email, String password){
        User user = new User(email, password);
        authRepository.createUserWithEmailAndPassword(user);
    }

    public void signInWithEmailAndPassword(String email, String password){
        User user = new User(email, password);
        authRepository.signInWithEmailAndPassword(user);
    }

    public void signInAnonymously(Context context){
        authRepository.signInAnonymously(context);
    }

}
