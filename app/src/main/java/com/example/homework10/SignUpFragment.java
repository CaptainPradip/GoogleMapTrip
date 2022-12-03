package com.example.homework10;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework10.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Homework 08
 * SignUpFragment.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentSignUpBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoLogin();
            }
        });


        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextName.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                if(name.isEmpty()){
                    MyAlertDialog.show(getContext(), "Error", "Enter valid name!");
                } else if(email.isEmpty()){
                    MyAlertDialog.show(getContext(), "Error", "Enter valid email!");
                } else if (password.isEmpty()){
                    MyAlertDialog.show(getContext(), "Error", "Enter valid password!");
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();

                                        mAuth.getCurrentUser().updateProfile(profileUpdates)
                                                .addOnFailureListener(getActivity(), new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        MyAlertDialog.show(getContext(), "Error", e.getMessage());
                                                    }
                                                });

                                        Log.d("demo", "onComplete: " + mAuth.getCurrentUser().getDisplayName());

                                        ArrayList<String> conversations = new ArrayList<>();
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("userId", mAuth.getCurrentUser().getUid());
                                        map.put("userName", name);
                                        map.put("isOnline", true);
                                        map.put("conversations", conversations);

                                        db.collection("users")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .set(map)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                mListener.gotoMyChat();
                                                            }
                                                        });
                                    } else {
                                        MyAlertDialog.show(getContext(), "Sign Up Unsuccessful", task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        });

        getActivity().setTitle("Sign Up");

    }

    SignUpListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (SignUpListener) context;
    }

    interface SignUpListener {
        void gotoMyChat();
        void gotoLogin();
    }
}