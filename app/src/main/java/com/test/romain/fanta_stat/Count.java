package com.test.romain.fanta_stat;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Romain on 10/01/2018.
 */

public class Count {
    String name;
    int number;
    ArrayList<String> liste;
    int idButton, idText1, idText2, idCheckBox;
    Context context;
    String filepath = "MyFileStorage";
    String savefile;

    public Count(String name, Context context){
        this.name = name;
        this.number = 0;
        this.context = context;
        liste = new ArrayList<>();

        this.savefile = this.name + ".txt";

        //create file here
        //need to check if a file with this name is already here

        File file = new File(context.getExternalFilesDir(filepath), savefile);
        if(file.exists()){
            //lecture du fichier
        }
        else{
            File myExternalFile;
            String myData = this.name+"\n";
            myExternalFile = new File(context.getExternalFilesDir(filepath), savefile);


            try {
                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write(myData.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public int getNumber() {
        return number;
    }

    public int getIdButton() {
        return idButton;
    }

    public int getIdCheckBox() {
        return idCheckBox;
    }

    public int getIdText1() {
        return idText1;
    }

    public int getIdText2() {
        return idText2;
    }

    public void setIdButton(int idButton) {
        this.idButton = idButton;
    }

    public void setIdCheckBox(int idCheckBox) {
        this.idCheckBox = idCheckBox;
    }

    public void setIdText1(int idText1) {
        this.idText1 = idText1;
    }

    public void setIdText2(int idText2) {
        this.idText2 = idText2;
    }

    public void addNumber(){
        number++;
    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void saveInFIle(){
        //check is file is here
        //ecrire a la fin la date et l'heure
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("name", currentTime.toString());

        File file = new File(context.getExternalFilesDir(filepath), savefile);
        if(file.exists()){

            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);


            try {
                outputStreamWriter.append(currentTime.toString()+"\n");
                outputStreamWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{


        }
    }


}
