package pt.simov.pl4;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Main2Activity extends AppCompatActivity {

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
        // listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            /*!
             * Callback method to be invoked when an item in this
             * AdapterView has been clicked.
             * Implementers can call getItemAtPosition(position)
             * if they need to access the data associated with
             * the selected item.
             * Parameters
             * 	arg0 	The AdapterView where the click happened.
             *  arg1 	The view within the AdapterView that was clicked (this will be a view provided by the adapter)
             *  arg2 	The position of the view in the adapter.
             *  arg3 	The row id of the item that was clicked.
             */
           // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
             //   Intent intent = new Intent(Main2Activity.this, DetailsActivity.class);
              //  Person p = (Person) listViewAdapter.getItem(arg2);
             //   intent.putExtra("PERSON_ID", p.getId());
              //  startActivity(intent);
            //}
        //});
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent1 = new Intent(Main2Activity.this,SearchActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_for_friends, menu);
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
            case R.id.edit:
                Intent intent = new Intent(Main2Activity.this,EditActivity.class);
                intent.putExtra("requestCode", R.id.edit);
                intent.putExtras(extras);
                startActivityForResult(intent,0);
                return true;
            case R.id.delete:
                Intent intent1 = new Intent(Main2Activity.this,DeleteActivity.class);
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
                    Toast.makeText(Main2Activity.this, "New Person Added",Toast.LENGTH_LONG).show();
                }else{
                    if(resultCode == Activity.RESULT_CANCELED){
                        Toast.makeText(Main2Activity.this, "Cancelled",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.delete:
                if(resultCode == Activity.RESULT_OK){
                    adapter.removePerson(user_id);
                    listViewAdapter.changeDataSource(adapter.getPeople());
                }else{
                    if(resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(Main2Activity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.edit:
                if(resultCode == Activity.RESULT_OK){
                    adapter.updatePerson(user_id,contact[0],contact[1]);
                    listViewAdapter.changeDataSource(adapter.getPeople());
                }else{
                    if(resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(Main2Activity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                Toast.makeText(Main2Activity.this, "Nothing happend", Toast.LENGTH_SHORT).show();

        }

    }

}