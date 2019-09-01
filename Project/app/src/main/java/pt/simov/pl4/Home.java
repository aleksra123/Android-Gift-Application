package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Home extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFirebaseAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if(currentUser == null){
            SendUserToLoginActivity();
        }
        else{
            CheckUSerExistence();
        }
    }

    private void CheckUSerExistence() {

        final String currentUser_ID = mFirebaseAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(currentUser_ID)){
                    SendUserToSetUpActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSetUpActivity() {
        Intent setupIntent = new Intent(Home.this , SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(Home.this , LogInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    public void OpenAddFriends(View view){
        Intent startNewActivity= new Intent(this, AllUsersActivity.class);
        startActivity(startNewActivity);

    }


    public  void OpenFriendList(View view){
        Intent startNewActivity= new Intent(this, MainActivity.class);
        startActivity(startNewActivity);
    }

    public  void OpenWishList(View view){
        Intent startNewActivity1= new Intent(this, MyWishList.class);
        startActivity(startNewActivity1);
    }

    public void OpenActualFriends(View view){
        Intent startNewActivity2= new Intent(this, FriendsList.class);
        startActivity(startNewActivity2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_option_home_activity, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mFirebaseAuth.signOut();
                loadLogInView();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        //FLAG_ACTIVITY_NEW_TASK: this activity will become the start of a new task on this history stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //FLAG_ACTIVITY_CLEAR_TASK: this flag will cause any existing task that would be associated with the activity to be cleared before the activity is started
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}
