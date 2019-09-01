package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class MyWishList extends AppCompatActivity {
    ListView lv;
    private static ArrayList<Subject> itemsArrayList;
    LVAdapter adapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    String currentUserID;
    private static final String TAG = "MyWishList";


    DatabaseReference mDatabaseReference;


    public ArrayList<Subject> getItemsArrayList() {
        return itemsArrayList;
    }

    public void setItemsArrayList(ArrayList<Subject> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_list);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            Log.i(TAG, "User not logged in");
            // Not logged in, launch the Log In activity

        }
        else {
            lv = (ListView) findViewById(R.id.listview);
            itemsArrayList = new ArrayList<>();
            adapter = new LVAdapter(this, itemsArrayList);
            lv.setAdapter(adapter);
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            mDatabaseReference = database.getReference();


            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            // Log and toast
                            String msg = "MyWishList:token: " + token;
                            Log.d(TAG, msg);
                           // Toast.makeText(MyWishList.this, msg, Toast.LENGTH_LONG).show();
                        }
                    });


            /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopupMenu popupMenu = new PopupMenu(parent.getContext(), parent);
                    PopupMenu test = new PopupMenu(parent.getContext(), view, Gravity.RIGHT);
                    test.getMenuInflater().inflate(R.menu.menu_for_friends, test.getMenu());
                    test.show();
                }
            });*/

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        if(mDatabaseReference!=null) {

        mDatabaseReference.child("Users").child(currentUserID).child("wishlist").addValueEventListener(new ValueEventListener() { //valueEventListener is used to update the database in RT
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsArrayList.clear();
                for (DataSnapshot subjectDataSnapshot : dataSnapshot.getChildren()) {
                    Subject subject = subjectDataSnapshot.getValue(Subject.class);
                    itemsArrayList.add(subject);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            mDatabaseReference.child("Users").child(currentUserID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    itemsArrayList.clear();
                    for (DataSnapshot subjectDataSnapshot : dataSnapshot.getChildren()) {
                        Subject subject = subjectDataSnapshot.getValue(Subject.class);
                        itemsArrayList.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_option, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(getApplicationContext(), "Add Selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                Intent intent1 = new Intent(MyWishList.this, SearchInMyLocalWishList.class);
                intent1.putExtra("requestCode", R.id.search);
                startActivityForResult(intent1,0);
                return true;
            case R.id.sort:
                //write the code to start sort activity here.
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
