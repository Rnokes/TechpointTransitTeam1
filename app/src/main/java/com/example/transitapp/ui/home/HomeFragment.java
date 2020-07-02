package com.example.transitapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.transitapp.R;

public class HomeFragment extends Fragment {

    public static final String EMAIL = "com.example.transitapp.home.MESSAGE";
    public static final String PASS = "com.example.transitapp.home.MESSAGE";

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        */
        return root;
    }

    public void signIn(View view) {
        Intent intent;

        EditText editText = (EditText) view.findViewById(R.id.editTextTextEmailAddress);
        String email = editText.getText().toString();

        editText = (EditText) view.findViewById(R.id.editTextTextPassword);
        String pass = editText.getText().toString();


        /*
            Input a call to database here, using the email and pass variables
            if they are correct, then make a startActivity call to the bus pass
            area of the application; otherwise display an error message to user
            if time allows, implement an allowance of tries, so that it locks
            out after say 5 attempts
            TODO: Database call and check
            TODO: Make more secure if time allows

         if ( sql call succeeds ) {

            intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EMAIL, email);
            intent.putExtra(PASS, pass);

            startActivity(intent);
         }
         else {

            intent = new Intent(this, DisplayErrorMessageActivity.class);
         }

         */

    }
}