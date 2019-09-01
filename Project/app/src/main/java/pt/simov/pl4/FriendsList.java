package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsList extends AppCompatActivity {

    private RecyclerView FriendsList;
    private DatabaseReference FriendsRef;
    private DatabaseReference UsersRef;
    private FirebaseAuth mFirebaseAuth;
    public Bundle bundle;
    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        System.out.println("current user id" + currentUserID);

        FriendsList= (RecyclerView) findViewById(R.id.friends_list);
        FriendsList.setHasFixedSize(true);
        FriendsList.setLayoutManager(new LinearLayoutManager(this));


        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        bundle = new Bundle();
        DisplayAllFriends();
    }

    private void DisplayAllFriends() {
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                        (
                                Friends.class,
                                R.layout.friends_display,
                                FriendsViewHolder.class,
                                FriendsRef
                        )
                {
                    @Override
                    protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends model, int position) {

                        final String userIDs = getRef(position).getKey();
                        UsersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    final String fullName = dataSnapshot.child("full_name").getValue().toString();
                                    final String username = dataSnapshot.child("username").getValue().toString();

                                    viewHolder.setUsername(username);
                                    viewHolder.setFullName(fullName);
                                    bundle.putString("id", dataSnapshot.getKey().toString());
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            PopupMenu pm = new PopupMenu(FriendsList.getContext(), v, Gravity.RIGHT);
                                            pm.getMenuInflater().inflate(R.menu.menu_for_friends, pm.getMenu());
                                            pm.show();

                                            pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch (item.getItemId()) {
                                                        case R.id.view_wishlist:
                                                            Toast.makeText(getApplicationContext(), "Going to wish list", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(FriendsList.this, FriendsWishlist.class);
                                                            intent.putExtras(bundle);
                                                            startActivity(intent);
                                                            return true;
                                                        case R.id.unfriend:
                                                            return true;
                                                        default:
                                                            return true;
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                };
                FriendsList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setUsername(String username){
            TextView name = (TextView) mView.findViewById(R.id.friends_username);
            name.setText(username);
        }

        public void setFullName(String fullName){
            TextView name = (TextView) mView.findViewById(R.id.friends_fullName);
            name.setText("(" + fullName + ")");
        }


    }
}
