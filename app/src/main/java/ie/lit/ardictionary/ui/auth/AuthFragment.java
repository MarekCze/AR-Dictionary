package ie.lit.ardictionary.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ie.lit.ardictionary.R;

public class AuthFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button googleBtn, fbBtn, emailBtn, anonBtn;
    View root;

    public AuthFragment(FirebaseFirestore db, FirebaseAuth mAuth){
        this.db = db;
        this.mAuth = mAuth;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.fragment_search_result, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
