package com.test.romain.fanta_stat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Romain on 10/01/2018.
 */

public class SecondActivity extends Activity {

    Button mButton1 = null;
    EditText editText1 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        mButton1 = findViewById(R.id.button2);
        editText1 = findViewById(R.id.editText);

        //get the list of names already here
        Intent myIntent = getIntent();
        int numberOfNames = myIntent.getIntExtra("NumberOfNames", -1);
        final ArrayList<String> listNames = new ArrayList<>();

        for(int i=0; i<numberOfNames; i++){
            listNames.add(myIntent.getStringExtra("name_" + i));

        }


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editText1.getText().toString();

                if(listNames.contains(newName)){

                    Toast.makeText(getApplicationContext(), "Nom déja utilisé", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("editTextValue", editText1.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

}
