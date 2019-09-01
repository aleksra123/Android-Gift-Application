package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetupActivity extends AppCompatActivity{

    private EditText UserName, FullName;
    private Button SaveInformation;
    private String[] UserWishList;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference usersRef;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        UserName = (EditText) findViewById(R.id.Username);
        FullName = (EditText) findViewById(R.id.FullName);
        SaveInformation = (Button) findViewById(R.id.SaveButton);


        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        SaveInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSetupInformation();
            }
        });
        Toast.makeText(this, "Please insert your account details",Toast.LENGTH_LONG).show();


    }

    private void SaveSetupInformation(){

        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        List<String[]> wishlist = new ArrayList<>();


        if(TextUtils.isEmpty(username)){

            Toast.makeText(this, "Please write your username..", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname)){

            Toast.makeText(this, "Please write your full name..", Toast.LENGTH_SHORT).show();
        }
        else{

            String DeviceToken = FirebaseInstanceId.getInstance().getToken();
            HashMap usermap = new HashMap();
            usermap.put("username", username);
            usermap.put("full_name", fullname);
            usermap.put("status", "Hey there, I am using the Wish List app and I love it!");
            usermap.put("wishlist", wishlist);
            usermap.put("device_token",DeviceToken);
            usersRef.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SetupActivity.this, "Your account was created successfully", Toast.LENGTH_LONG).show();
                        SendUserToHomeActivity();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occured " + message, Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

    private void SendUserToHomeActivity() {
        Intent homeIntent = new Intent(SetupActivity.this , Home.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

}
