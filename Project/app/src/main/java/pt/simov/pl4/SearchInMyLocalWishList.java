package pt.simov.pl4;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SearchInMyLocalWishList extends Activity {

    ListView lv;
    ArrayList<Subject> mItemList;
    LVAdapter adapter;
    Bundle extras;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    String currentUserID;

    EditText search_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_in_database);

        search_text = (EditText) findViewById(R.id.search_text);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        lv = (ListView) findViewById(R.id.listView);
        mItemList = new ArrayList<>();
        adapter = new LVAdapter(this, mItemList);
        lv.setAdapter(adapter);
        //extras = getIntent().getExtras();
        MyWishList mwl = new MyWishList();
        ArrayList<Subject> existingItems;
        existingItems = mwl.getItemsArrayList();
        for (Subject sub : existingItems){
            Subject mSubject = sub;
            mItemList.add(mSubject);
        }
        adapter.notifyDataSetChanged();



        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do here

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (SearchInMyLocalWishList.this).adapter.filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }
}
