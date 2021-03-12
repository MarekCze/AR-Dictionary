package ie.lit.ardictionary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import ie.lit.ardictionary.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import ie.lit.ardictionary.model.User;
import ie.lit.ardictionary.ui.auth.AuthFragment;
import ie.lit.ardictionary.ui.auth.AuthViewModel;
import ie.lit.ardictionary.ui.auth.EmailSignInFragment;
import ie.lit.ardictionary.ui.camera.CameraViewModel;
import ie.lit.ardictionary.ui.word.WordViewModel;

import com.firebase.ui.auth.AuthUI;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String[] REQUIRED_PERMISSIONS = new String[] {Manifest.permission.CAMERA, Manifest.permission.INTERNET};
    public static final int REQUEST_CODE_PERMISSIONS = 1001;
    public static final int RC_SIGN_IN = 1123;
    private AppBarConfiguration mAppBarConfiguration;
    public WordViewModel wordViewModel;
    private AuthViewModel authViewModel;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private User user;
    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init Firebase
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        // init ViewModels
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // init navigation
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_camera, R.id.nav_search_result, R.id.nav_authentication)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // check permissions
        if(!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // observe authViewModel
        authViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                FragmentManager fm = getSupportFragmentManager();

                Toast text;
                switch(user.getType()){
                    case "email":
                        if(!user.isNewUser()){
                            Fragment signInFragment = fm.findFragmentByTag("Email sign in fragment");
                            Fragment registerFragment = fm.findFragmentByTag("Email register fragment");
                            fm.beginTransaction()
                                    .remove(signInFragment)
                                    .remove(registerFragment)
                                    .commit();
                            fm.popBackStack();
                        } else {
                            fm.popBackStackImmediate();
                        }

                        text = Toast.makeText(getApplicationContext(), "Signed in with email", Toast.LENGTH_SHORT);
                        text.show();
                        break;
                    case "google":
                        fm.popBackStackImmediate();
                        text = Toast.makeText(getApplicationContext(), "Signed in with Google", Toast.LENGTH_SHORT);
                        text.show();
                        break;
                    case "anonymous":
                        fm.popBackStackImmediate();
                        text = Toast.makeText(getApplicationContext(), "Signed in as guest", Toast.LENGTH_SHORT);
                        text.show();
                        break;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // pass db instance to WordViewModel


        if(currentUser == null){
            Fragment authFragment = new AuthFragment();
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, authFragment, "Auth Fragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    } // END allPermissionsGranted method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                Log.d(TAG, "all permissions granted");
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    } // END onRequestPermissionResult method
}