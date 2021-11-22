package com.google.ar.core.examples.java.helloar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ModelingActivity extends Activity {

    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modeling);

        okButton=(Button)findViewById(R.id.okButton);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ModelingActivity.this,HelloArActivity.class);
                startActivity(intent);

            }
        });

    }


}
