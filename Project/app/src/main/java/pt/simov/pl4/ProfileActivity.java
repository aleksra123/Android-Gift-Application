package pt.simov.pl4;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {


    private Button SendtFriendrequestButton;
    private Button DeclineFriendrequestButton;
    private TextView ProfileName;
    private TextView ProfileStatus;

    private DatabaseReference UsersReference;

    private String CURRENT_STAT;
    private DatabaseReference friendRequestReference;
    private FirebaseAuth mAuth;
    String Sender_user_id;
    String receiver_user_id;

    private DatabaseReference FriendsReference;
    private DatabaseReference NotificationsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        friendRequestReference = FirebaseDatabase.getInstance().getReference().child("Friend_Req"); //her lages den nye noden  databasen
        friendRequestReference.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        Sender_user_id=mAuth.getCurrentUser().getUid();

        FriendsReference = FirebaseDatabase.getInstance().getReference().child("Friends");
        FriendsReference.keepSynced(true);

        NotificationsReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
        NotificationsReference.keepSynced(true);



        UsersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        receiver_user_id = getIntent().getExtras().get("visit_user_id").toString();


        SendtFriendrequestButton = (Button) findViewById(R.id.profile_visit_send_req_btn);
        DeclineFriendrequestButton = (Button) findViewById(R.id.profile_decline_friend_req_btn);
        ProfileName = (TextView) findViewById(R.id.profile_visit_username);
        ProfileStatus = (TextView) findViewById(R.id.profile_visit_user_status);

        CURRENT_STAT = "not_friends";

        UsersReference.child(receiver_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name= dataSnapshot.child("username").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();


                ProfileName.setText(name);
                ProfileStatus.setText(status);

                friendRequestReference.child(Sender_user_id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()){
                                   if (dataSnapshot.hasChild(receiver_user_id))
                                   {
                                       String req_type = (dataSnapshot).child(receiver_user_id).child("request_type").getValue().toString();

                                       if (req_type.equals("sent"))
                                       {
                                           CURRENT_STAT = "request_sent";
                                           SendtFriendrequestButton.setText("Cancel Friend Request");


                                           DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
                                           DeclineFriendrequestButton.setEnabled(false);

                                       }else if (req_type.equals("received")){
                                           CURRENT_STAT = "request_received";
                                           SendtFriendrequestButton.setText("Accept Friend Request");


                                           DeclineFriendrequestButton.setVisibility(View.VISIBLE);
                                           DeclineFriendrequestButton.setEnabled(true);

                                           DeclineFriendrequestButton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v)
                                               {
                                                    DeclineFriendRequest();    //Dette er metoden som skal stå for funksjonaliteten til Decline knappen
                                               }
                                           });
                                       }
                                   }
                               }
                               else
                               {
                                   FriendsReference.child(Sender_user_id)
                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                               {
                                                   if (dataSnapshot.hasChild(receiver_user_id))
                                                   {
                                                       CURRENT_STAT = "friends"; //dersom to personer er vennner..
                                                       SendtFriendrequestButton.setText("Unfriend This Person"); //..endrer vi knappen til unfriend


                                                       DeclineFriendrequestButton.setVisibility(View.INVISIBLE); //when two users are friends with each other, decline button will be invisible
                                                       DeclineFriendrequestButton.setEnabled(false);

                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError)
                                               {

                                               }
                                           });
                               }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
        DeclineFriendrequestButton.setEnabled(false);

       if (!Sender_user_id.equals(receiver_user_id))
       {
           SendtFriendrequestButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   SendtFriendrequestButton.setEnabled(false);

                   if (CURRENT_STAT.equals("not_friends")){

                       SendtFriendrequestToAPerson();
                   }
                   if (CURRENT_STAT.equals("request_sent"))
                   {
                       CancelFriendRequest();
                   }
                   if (CURRENT_STAT.equals("request_received"))
                   {
                       AcceptFriendRequest();
                   }
                   if (CURRENT_STAT.equals("friends"))
                   {
                       UnfriendAFriend();

                   }
               }
           });
       }else
           {
               DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
               SendtFriendrequestButton.setVisibility(View.INVISIBLE);
           }

    }

    private void DeclineFriendRequest()  //når noen trykker på declinefriendrequest skal requesten fjernes under request noden i firebase
    {
        friendRequestReference.child(Sender_user_id).child(receiver_user_id).removeValue() //når noen trykker på decline knappen vil man først fjerne sender og receiver user id
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            friendRequestReference.child(receiver_user_id).child(Sender_user_id)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        SendtFriendrequestButton.setEnabled(true);
                                        CURRENT_STAT="not_friends";
                                        SendtFriendrequestButton.setText("Send Friend Request");

                                        DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
                                        DeclineFriendrequestButton.setEnabled(false);
                                    }
                                }
                            });
                        }
                    }
                });

    }

    private void UnfriendAFriend()
    {
        FriendsReference.child(Sender_user_id).child(receiver_user_id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FriendsReference.child(receiver_user_id).child(Sender_user_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                SendtFriendrequestButton.setEnabled(true);
                                                CURRENT_STAT = "not_friends";
                                                SendtFriendrequestButton.setText("Send Friend Request");


                                                DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
                                                DeclineFriendrequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void AcceptFriendRequest()
    {
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(callForDate.getTime());

       FriendsReference.child(Sender_user_id).child(receiver_user_id).child("date").setValue(saveCurrentDate)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                        FriendsReference.child(receiver_user_id).child(Sender_user_id).child("date")
                                .setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                friendRequestReference.child(Sender_user_id).child(receiver_user_id).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    friendRequestReference.child(receiver_user_id).child(Sender_user_id)
                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                SendtFriendrequestButton.setEnabled(true);
                                                                CURRENT_STAT="friends";
                                                                SendtFriendrequestButton.setText("Unfriend this person");

                                                                DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
                                                                DeclineFriendrequestButton.setEnabled(false);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        });
                   }
               });




    }


    private void CancelFriendRequest()
    {

        friendRequestReference.child(Sender_user_id).child(receiver_user_id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful())
                         {
                             friendRequestReference.child(receiver_user_id).child(Sender_user_id)
                                     .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task)
                                 {
                                    if (task.isSuccessful())
                                    {
                                        SendtFriendrequestButton.setEnabled(true);
                                        CURRENT_STAT="not_friends";
                                        SendtFriendrequestButton.setText("Send Friend Request");

                                        DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
                                        DeclineFriendrequestButton.setEnabled(false);
                                    }
                                 }
                             });
                         }
                    }
                });


    }


    private void SendtFriendrequestToAPerson()
    {

        friendRequestReference.child(Sender_user_id).child(receiver_user_id)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            friendRequestReference.child(receiver_user_id).child(Sender_user_id)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                 if (task.isSuccessful())
                                                 {
                                                     HashMap<String, String> notificationsData = new HashMap<String, String>(); //denne koden og ned til if is.tasksucc... gjør at det blir lagt til en notificationsnode i databasen samt en node under for at det har blitt sendt en request
                                                     notificationsData.put("from", Sender_user_id);
                                                     notificationsData.put("type", "request");

                                                     NotificationsReference.child(receiver_user_id).push().setValue(notificationsData) //under notification noden har vi en receiveriD og under denne en random key for denne spesifikke notificationen
                                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<Void> task)
                                                                 {
                                                                  if (task.isSuccessful())
                                                                  {
                                                                      SendtFriendrequestButton.setEnabled(true);
                                                                      CURRENT_STAT = "request_sent";
                                                                      SendtFriendrequestButton.setText("Cancel Friend Request");

                                                                      DeclineFriendrequestButton.setVisibility(View.INVISIBLE);
                                                                      DeclineFriendrequestButton.setEnabled(false);
                                                                  }
                                                                 }
                                                             });



                                                 }
                                        }
                                    });
                        }
                    }
                });

    }
}
