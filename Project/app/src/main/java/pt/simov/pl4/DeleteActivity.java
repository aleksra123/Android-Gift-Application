package pt.simov.pl4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeleteActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        final Bundle extras = getIntent().getExtras();

        DBAdapter adapter = new DBAdapter(getApplicationContext());

        final int id = extras.getInt("ID");

        Person person = adapter.getPerson(id);

        if(person != null) {
            final TextView person_id = (TextView) findViewById(R.id.textView);
            person_id.setText(Integer.toString(person.getId()));
            TextView first_name = (TextView) findViewById(R.id.textView2);
            first_name.setText(person.getFirstName());
            TextView last_name = (TextView) findViewById(R.id.textView3);
            last_name.setText(person.getLastName());


            Button btDel = (Button) findViewById(R.id.buttonDelete);
            Button btCancel = (Button) findViewById(R.id.buttonCancel);

            btDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    extras.putInt("ID", id);

                    Intent i = new Intent();
                    i.putExtras(extras);
                    setResult(Activity.RESULT_OK, i);
                    finish();

                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.putExtras(extras);
                    setResult(Activity.RESULT_CANCELED, i);
                    finish();
                }
            });

        }

    }
}
