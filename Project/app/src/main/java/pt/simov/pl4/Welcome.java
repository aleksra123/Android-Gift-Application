package pt.simov.pl4;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final EditText etUsername=(EditText) findViewById(R.id.etUsername);
        final EditText etPassword=(EditText) findViewById(R.id.etPassword);

        final Button bLogin= (Button) findViewById(R.id.bLogin);

        final TextView registerLink= (TextView) findViewById(R.id.tvRegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener() { //listens for clicks on the registerLink made in the .xml file
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Welcome.this, MainActivity.class);
                Welcome.this.startActivity(registerIntent);
            }
        });
    }

}
