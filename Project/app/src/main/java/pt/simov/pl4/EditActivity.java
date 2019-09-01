package pt.simov.pl4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Bundle extras = getIntent().getExtras();

        DBAdapter adapter = new DBAdapter(getApplicationContext());

        final int id = extras.getInt("ID");

        Person person = adapter.getPerson(id);

        if(person != null) {
            final EditText first_name = (EditText) findViewById(R.id.editView);
            first_name.setText(person.getFirstName());
            final EditText last_name = (EditText) findViewById(R.id.editView2);
            last_name.setText(person.getLastName());


            Button btEdit = (Button) findViewById(R.id.buttonEdit);
            Button btCancel = (Button) findViewById(R.id.buttonCancel);

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] contact = {"name","surename"};
                    contact[0] = first_name.getText().toString();
                    contact[1] = last_name.getText().toString();
                    extras.putStringArray("NAME", contact);
                    extras.putInt("ID",id);

                    Intent i = new Intent();
                    i.putExtras(extras);
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.putExtras(extras);
                    setResult(Activity.RESULT_CANCELED,i);
                    finish();
                }
            });
        }

    }
}
