package pt.simov.pl4;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//import javax.security.auth.Subject;

public class AddActivity extends AppCompatActivity {
    EditText title, description;
    Button btAdd;
    TextView reserved;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference userRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);


        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        btAdd = (Button) findViewById(R.id.add);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject subject = new Subject();
                subject.setUid(userRef.push().getKey());
                subject.setTitle(title.getText().toString());
                subject.setDescription(description.getText().toString());
                userRef.child("wishlist").child(subject.getUid()).setValue(subject);
                finish();
            }
        });


    }
}

