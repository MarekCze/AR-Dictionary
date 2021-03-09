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

public class EmailRegisterFragment extends Fragment {

    private EditText emailEditText, nameEditText, passwordEditText, confirmPasswordEditText;
    private TextView errorTextView, loginTextView;
    private Button createUserBtn;
    private AuthViewModel authViewModel;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_email_register, container, false);

        errorTextView = root.findViewById(R.id.registerFragmentErrorTextView);
        loginTextView = root.findViewById(R.id.loginTextView);
        emailEditText = root.findViewById(R.id.emailEditTextRegisterFragment);
        nameEditText = root.findViewById(R.id.nameEditTextRegisterFragment);
        passwordEditText = root.findViewById(R.id.passwordEditTextRegisterFragment);
        confirmPasswordEditText = root.findViewById(R.id.confirmPasswordEditTextRegisterFragment);
        createUserBtn = root.findViewById(R.id.registerBtn);

        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if(password.length() > 7){
                    if(email.length() > 0){
                        if(password.equals(confirmPassword)){
                            authViewModel.createUserWithEmailAndPassword(email, password);
                        } else {
                            passwordEditText.setText("");
                            confirmPasswordEditText.setText("");
                            errorTextView.setText("Passwords must match");
                        }
                    } else {
                        errorTextView.setText("Email field cannot be empty");
                    }
                } else {
                    passwordEditText.setText("");
                    confirmPasswordEditText.setText("");
                    errorTextView.setText("Password must be 8 or more characters long");
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment emailSignInFragment = new EmailSignInFragment();
                String fragmentTag = "Email sign in fragment";
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.emailRegisterConstraintLayout, emailSignInFragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .commit();
            }
        });
    }
}
