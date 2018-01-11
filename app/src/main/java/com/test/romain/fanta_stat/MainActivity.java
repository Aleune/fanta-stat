package com.test.romain.fanta_stat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    Button b1 = null;
    private int idNumber = 100;
    private int nbStat = 0;
    private String startFile = "startFile.txt";
    private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    private List<View> mViews= new ArrayList<View>();
    private List<Count> listCount = new ArrayList<>();

    LinearLayout ll = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        b1 = findViewById(R.id.buttonFinal);
        ll = findViewById(R.id.layout1);

        b1.setOnClickListener(clickListenerBouton1);
    }

    private View.OnClickListener clickListenerBouton1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Intent secondeActivite = new Intent(MainActivity.this, SecondActivity.class);

            startActivityForResult(secondeActivite, 1);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");

                //createStat(strEditText); // creation des boutons
                Count count = new Count(strEditText);
                listCount.add(count);

                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.template_view, null);

                // fill in any details dynamically here
                TextView textView = v.findViewById(R.id.templateText1);
                textView.setText(strEditText);

                TextView textView2 = v.findViewById(R.id.templateText2);
                textView2.setText(String.valueOf(count.getNumber()));
                textView2.setId(99+listCount.size());
                count.setIdText2(R.id.templateText2);
                Log.d("myTAG", String.valueOf(R.id.templateText2));

                Button myButton = v.findViewById(R.id.magic_button);
                myButton.setId(99+listCount.size()+1);
                count.setIdButton(R.id.magic_button);
                myButton.setOnClickListener(onClickListenerAdd);

                // insert into main view
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.layout1);
                insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            }
        }
    }


    private View.OnClickListener onClickListenerAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int nb = v.getId()- 101;
            Log.d("myTAGGGGG", String.valueOf(nb));
            listCount.get(nb).addNumber();
            TextView myText = findViewById(v.getId()-1);
            myText.setText(String.valueOf(listCount.get(nb).getNumber()));




            //Save Fichier

            /*File myExternalFile;
            String myData = "";
            myExternalFile = new File(getExternalFilesDir(filepath), filename);

            if (isExternalStorageWritable()){
                try {

                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write("test".getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            try {
                FileInputStream fis = new FileInputStream(myExternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            b1.setText(myData);*/





        }
    };

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("Nb_stats", listCount.size());
        for(int i=0; i<listCount.size(); i++){
            savedInstanceState.putInt("Stat_"+String.valueOf(i), listCount.get(i).getNumber());
            savedInstanceState.putString("name_"+String.valueOf(i), listCount.get(i).getName());
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        for(int i=0; i<savedInstanceState.getInt("Nb_stats"); i++){
            Count count = new Count(savedInstanceState.getString("name_"+String.valueOf(i)));
            listCount.add(count);

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.template_view, null);

            // fill in any details dynamically here
            TextView textView = v.findViewById(R.id.templateText1);
            textView.setText(savedInstanceState.getString("name_"+String.valueOf(i)));
            count.setName(savedInstanceState.getString("name_"+String.valueOf(i)));

            TextView textView2 = v.findViewById(R.id.templateText2);
            textView2.setText(String.valueOf(savedInstanceState.getInt("Stat_"+String.valueOf(i))));
            count.setNumber(savedInstanceState.getInt("Stat_"+String.valueOf(i)));
            textView2.setId(99+i);
            count.setIdText2(R.id.templateText2);


            Button myButton = v.findViewById(R.id.magic_button);
            myButton.setId(99+i+1);
            count.setIdButton(R.id.magic_button);
            myButton.setOnClickListener(onClickListenerAdd);

            // insert into main view
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.layout1);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

    }

    public void start(){
        File file = new File(getExternalFilesDir(filepath), startFile);
        if(file.exists()){
            //lecture du fichier
            try {
                File myExternalFile;
                String myData = "";
                myExternalFile = new File(getExternalFilesDir(filepath), startFile);

                FileInputStream fis = new FileInputStream(myExternalFile);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myData = myData + strLine;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //recuperer texte
        }
        else{
            //creation du fichier
            File myExternalFile;
            String myData = "0";
            myExternalFile = new File(getExternalFilesDir(filepath), startFile);

            if (isExternalStorageWritable()){
                try {

                    FileOutputStream fos = new FileOutputStream(myExternalFile);
                    fos.write(myData.getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void saveDataApp(List<Count> list){
        File myExternalFile;
        myExternalFile = new File(getExternalFilesDir(filepath), startFile);

        if (isExternalStorageWritable()){
            try {

                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write((String.valueOf(list.size())+"\n").getBytes());//nombre d'elements
                for(int i=0; i<= list.size(); i++){
                    fos.write((list.get(i).getName()+" ").getBytes());
                    fos.write((list.get(i).getNumber()+"\n").getBytes());
                }
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
