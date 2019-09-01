package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView allUsersList;
    private DatabaseReference allDatabaseUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        allUsersList=(RecyclerView) findViewById(R.id.all_users_list);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

        allDatabaseUserReference=FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>
                (
                        AllUsers.class,
                        R.layout.all_users_display_layout,
                        AllUsersViewHolder.class,
                        allDatabaseUserReference

                )
        {
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder, AllUsers model, final int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setStatus(model.getStatus());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntent = new Intent(AllUsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        allUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class AllUsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public AllUsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setUsername(String username){
            TextView name = (TextView) mView.findViewById(R.id.all_users_username);
            name.setText(username);
        }

        public void setStatus(String status){
            TextView stat = (TextView) mView.findViewById(R.id.all_users_status);
            stat.setText(status);
        }
    }
}
