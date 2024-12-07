package com.btec.fpt.campus_expense_manager.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.fragments.LoginFragment;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

public class ForgotPasswordFragment extends Fragment {

    private DatabaseHelper databaseHelper;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        databaseHelper = new DatabaseHelper(getContext());

        EditText edtEmail = view.findViewById(R.id.email);
        EditText edtNewPassword = view.findViewById(R.id.new_password);
        EditText edtConfirmPassword = view.findViewById(R.id.confirm_password);
        Button resetPasswordButton = view.findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    showToast("All fields must be filled!");
                    return;
                }

                if (newPassword.equals(confirmPassword)) {
                    if (databaseHelper.changePassword(email, newPassword)) {
                        showToast("Password changed successfully!");
                        loadFragment(new LoginFragment()); // Navigate back to LoginFragment
                    } else {
                        showToast("Email not found or password change failed.");
                    }
                } else {
                    showToast("Passwords do not match!");
                }
            }
        });

        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadFragment(Fragment fragment) {
        // Use getParentFragmentManager() if you are within a Fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
