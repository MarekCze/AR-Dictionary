package ie.lit.ardictionary.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ie.lit.ardictionary.R;

public class EmailSignInFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private Button signInBtn;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_email_register, container, false);

        emailEditText = root.findViewById(R.id.emailEditTextSignInFragment);
        passwordEditText = root.findViewById(R.id.passwordEditTextSignInFragment);
        signInBtn = root.findViewById(R.id.signInBtn);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
