package pt.simov.pl4;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    ArrayList<Person> people;
    DBAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new DBAdapter(this);
        people = new ArrayList<Person>();
        people.addAll(adapter.getPeople());
        listViewAdapter = new ListViewAdapter(people);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // CHANGE THIS TO BE WHATEVER WE WANT IT TO BE, MAYBE TO GO TO THEIR PROFILE? ---------------
                PopupMenu popupMenu = new PopupMenu(parent.getContext(), parent);
                PopupMenu test = new PopupMenu(parent.getContext(), view, Gravity.RIGHT);
                test.getMenuInflater().inflate(R.menu.menu_for_friends, test.getMenu());
                test.show();
            }
        });
        registerForContextMenu(listView);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                intent.putExtra("requestCode", R.id.add);
                startActivityForResult(intent, 0);
                return true;
            case R.id.search:
                Intent intent1 = new Intent(MainActivity.this,SearchActivity.class);
                intent1.putExtra("requestCode", R.id.search);
                startActivityForResult(intent1,0);
                return true;
             case R.id.sort:
                 //write the code to start sort activity here.
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = info.position;
        Person p = (Person) listViewAdapter.getItem(pos);

        Bundle extras = new Bundle();

        extras.putInt("ID", p.getId());
        extras.putInt("User_Pos", pos);

        switch (item.getItemId()) {
            case R.id.delete:
                Intent intent1 = new Intent(MainActivity.this,DeleteActivity.class);
                intent1.putExtra("requestCode", R.id.delete);
                intent1.putExtras(extras);
                startActivityForResult(intent1,0);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();

        int rqCode = (int) extras.get("requestCode");
        String[] contact = extras.getStringArray("NAME");
        int user_id = extras.getInt("ID");


        switch(rqCode) {
            case R.id.add:
                if (resultCode == Activity.RESULT_OK) {
                    adapter.insertPerson(contact[0], contact[1]);
                    listViewAdapter.changeDataSource(adapter.getPeople());
                    Toast.makeText(MainActivity.this, "New Person Added",Toast.LENGTH_LONG).show();
                }else{
                    if(resultCode == Activity.RESULT_CANCELED){
                        Toast.makeText(MainActivity.this, "Cancelled",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.delete:
                if(resultCode == Activity.RESULT_OK){
                    adapter.removePerson(user_id);
                    listViewAdapter.changeDataSource(adapter.getPeople());
                }else{
                    if(resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.edit:
                if(resultCode == Activity.RESULT_OK){
                    adapter.updatePerson(user_id,contact[0],contact[1]);
                    listViewAdapter.changeDataSource(adapter.getPeople());
                }else{
                    if(resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                Toast.makeText(MainActivity.this, "Nothing happend", Toast.LENGTH_SHORT).show();

        }

    }

}