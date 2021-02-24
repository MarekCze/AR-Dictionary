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

public class EmailRegisterFragment extends Fragment {

    private EditText emailEditText, nameEditText, passwordEditText, confirmPasswordEditText;
    private Button createUserBtn;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_email_register, container, false);

        emailEditText = root.findViewById(R.id.emailEditTextRegisterFragment);
        nameEditText = root.findViewById(R.id.nameEditTextRegisterFragment);
        passwordEditText = root.findViewById(R.id.passwordEditTextRegisterFragment);
        confirmPasswordEditText = root.findViewById(R.id.confirmPasswordEditTextRegisterFragment);
        createUserBtn = root.findViewById(R.id.registerBtn);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
