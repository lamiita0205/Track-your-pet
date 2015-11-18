package com.example.lam.ibeacon;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectToPet extends Activity {

    private String TAG = "MyText";
    private static final String PREFS_NAME ="MyDirectory";
    private EditText txtDescription;
    private Button btn;
    private TextView text;
    private ImageView forward;
    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_pet);

        txtDescription = (EditText) findViewById(R.id.editText);

        forward = (ImageView)findViewById(R.id.forwardbtn);
        forward.setOnClickListener(putinto);



    }

    View.OnClickListener clicktoget = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String value = settings.getString("key", "");

            text.setText(value);

        }
    };
    View.OnClickListener putinto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("key", txtDescription.getText().toString());
            editor.commit();

        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
