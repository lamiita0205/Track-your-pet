package com.example.lam.ibeacon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Lam on 17-Nov-15.
 */
public class listener extends Activity {
private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_widget);

        button = (Button) findViewById(R.id.startbtn);
        button.setOnClickListener(click);


    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String string = button.getText().toString();

            if(string.equals("Start"))
            {
              button.setText("Stop");
            }
            else
            {
                button.setText("Start");
            }

        }
    };
}
