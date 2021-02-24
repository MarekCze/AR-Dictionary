package ie.lit.ardictionary.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ie.lit.ardictionary.model.User;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class AuthRepository {

    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<User> userMutableLiveData;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference collection;
    private final static String TAG = "AUTHENTICATION";
    private final int RC_SIGN_IN = 1123;

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public void setFirebaseUserMutableLiveData(MutableLiveData<FirebaseUser> firebaseUserMutableLiveData) {
        this.firebaseUserMutableLiveData = firebaseUserMutableLiveData;
    }

    public AuthRepository(Application application){
        this.application = application;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collection = db.collection("Users");
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
    }

    public void signInWithGoogle(Context context, GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w("TAG", "signInWithCredential:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            createUser(firebaseUser, "google");
                            firebaseUserMutableLiveData.postValue(firebaseUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    public void signInWithEmail(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }
            }
        });
    }

    public void signInWithFacebook(){

    }

    public void signInAnonymously(Context context){
        mAuth.signInAnonymously()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            createUser(firebaseUser, "anonymous");
                            firebaseUserMutableLiveData.postValue(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void createUser(FirebaseUser firebaseUser, String type){
        User user = new User();

        user.setUid(firebaseUser.getUid());
        user.setType(type);
        if(!type.equalsIgnoreCase("anonymous")){
            user.setName(firebaseUser.getDisplayName());
            user.setEmail(firebaseUser.getEmail());
        }

        collection.document(user.getUid()).set(user);

    }
}
