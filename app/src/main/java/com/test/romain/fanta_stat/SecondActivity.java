package com.test.romain.fanta_stat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("editTextValue", editText1.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
