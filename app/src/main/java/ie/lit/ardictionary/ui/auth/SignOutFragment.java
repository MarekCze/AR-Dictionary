package ie.lit.ardictionary.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ie.lit.ardictionary.R;

public class SignOutFragment extends Fragment {
    private Button signOutBtn;
    private View root;
    private AuthViewModel authViewModel;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sign_out, container, false);
        signOutBtn = root.findViewById(R.id.logoutBtn);
        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);
        context = getActivity();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authViewModel.signOut(context);
            }
        });
    }
}
