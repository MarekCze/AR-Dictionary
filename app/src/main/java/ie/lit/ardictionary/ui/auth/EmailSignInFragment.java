package ie.lit.ardictionary.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ie.lit.ardictionary.R;

public class EmailSignInFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private Button signInBtn;
    private TextView errorTextView;
    private AuthViewModel authViewModel;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_email_sign_in, container, false);

        errorTextView = root.findViewById(R.id.signInFragmentErrorTextView);
        emailEditText = root.findViewById(R.id.emailEditTextSignInFragment);
        passwordEditText = root.findViewById(R.id.passwordEditTextSignInFragment);
        signInBtn = root.findViewById(R.id.signInBtn);

        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email.length() > 0 && password.length() > 0){
                    authViewModel.signInWithEmailAndPassword(email, password);
                } else {
                    errorTextView.setText("Wrong credentials");
                }
            }
        });
    }
}
