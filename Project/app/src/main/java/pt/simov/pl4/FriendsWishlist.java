package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsWishlist extends Activity {

    private RecyclerView FriendsWishlist;
    private DatabaseReference UsersRef;
    private DatabaseReference FriendsRef;
    private FirebaseAuth mFirebaseAuth;
    String currentUserID;
    String FriendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        System.out.println("current user id: " + currentUserID);

        FriendsWishlist = (RecyclerView) findViewById(R.id.friends_list);
        FriendsWishlist.setHasFixedSize(true);
        FriendsWishlist.setLayoutManager(new LinearLayoutManager(this));

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserID);
        Bundle bundle = getIntent().getExtras();
        FriendID = bundle.getString("id");

        DisplayAllItems();
    }

    private void DisplayAllItems() {

        FirebaseRecyclerAdapter<FriendsItems, FriendsWishlistViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FriendsItems, FriendsWishlistViewHolder>
                        (
                                FriendsItems.class,
                                R.layout.friends_wishlist_display,
                                FriendsWishlistViewHolder.class,
                                FriendsRef
                        )
                {
                    @Override
                    protected void populateViewHolder(final FriendsWishlistViewHolder viewHolder, FriendsItems model, int position) {

                        final String currentUserIDInPopulate = getRef(position).getKey();

                        if (currentUserIDInPopulate.equals(FriendID)) {
                            UsersRef.child(FriendID).child("wishlist").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                                        final String title = dss.child("title").getValue().toString();
                                        final String desc = dss.child("description").getValue().toString();
                                        viewHolder.setTitle(title);
                                        viewHolder.setDescription(desc);
                                    }
                                    System.out.println("SAME ID");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }



                };
                FriendsWishlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FriendsWishlistViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FriendsWishlistViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView name = (TextView) mView.findViewById(R.id.item_name);
            name.setText(title);
        }

        public void setDescription(String description) {
            TextView name = (TextView) mView.findViewById(R.id.item_description);
            name.setText("(" + description + ")");
        }
    }

}
