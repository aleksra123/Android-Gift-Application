package pt.simov.pl4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

public class SearchActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    EditText search_text;
    Bundle extras;
    ArrayList<Person> people;
    DBAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_text = (EditText) findViewById(R.id.search_text);
        listView = (ListView) findViewById(R.id.listView);

        adapter = new DBAdapter(this);
        people = new ArrayList<Person>();
        people.addAll(adapter.getPeople());
        listViewAdapter = new ListViewAdapter(people);
        listView.setAdapter(listViewAdapter);

        extras = getIntent().getExtras();

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // nothing to do here

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //listViewAdapter.getFilter().filter(s);
                //listViewAdapter.changeDataSource(people);

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Person> people1 = adapter.getPeople(search_text.getText().toString());//method to search on the database for people with such name
                listViewAdapter.changeDataSource(people1);

            }
        });

    }

}
