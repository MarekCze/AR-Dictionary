package ie.lit.ardictionary.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public AuthRepository(Application application){
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collection = db.collection("Users");
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setFirebaseUserMutableLiveData(MutableLiveData<FirebaseUser> firebaseUserMutableLiveData) {
        this.firebaseUserMutableLiveData = firebaseUserMutableLiveData;
    }

    public void signInWithGoogle(Context context, GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            createUser(firebaseUser, "google");
                            firebaseUserMutableLiveData.postValue(firebaseUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    public void createUserWithEmailAndPassword(User user){
        Log.w("TAG", "signInWithEmail:failure");
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    createUser(firebaseUser, "email");
                    firebaseUserMutableLiveData.postValue(firebaseUser);
                } else {
                    Log.d(TAG, "signInWithEmail:failure");
                }
            }
        });
    }

    public void signInWithEmailAndPassword(User user){
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    firebaseUserMutableLiveData.postValue(firebaseUser);
                    getUser(firebaseUser);
                } else {
                    Log.d(TAG, "signInWithEmail:failure");
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
                            Log.d(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void signOut(Context context){
        AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(null);
                    userMutableLiveData.postValue(null);
                }
            }
        });
    }

    private void createUser(FirebaseUser firebaseUser, String type){
        User user = new User();

        user.setUid(firebaseUser.getUid());
        user.setType(type);
        user.setNewUser(true);
        if(!type.equalsIgnoreCase("anonymous")){
            user.setName(firebaseUser.getDisplayName());
            user.setEmail(firebaseUser.getEmail());
        }

        collection.document(user.getUid()).set(user);
        userMutableLiveData.postValue(user);
    }

    private void getUser(FirebaseUser firebaseUser){
        collection.document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();

                    User user = doc.toObject(User.class);

                    if(user.isNewUser()){
                        user.setNewUser(false);
                    }

                    userMutableLiveData.postValue(user);
                }
            }
        });
    }
}
