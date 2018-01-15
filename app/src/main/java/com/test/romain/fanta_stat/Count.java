package com.test.romain.fanta_stat;

import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Romain on 10/01/2018.
 */

public class Count {
    private String name;
    private int number;
    private ArrayList<String> liste;
    private int idButton, idText1, idText2, idCheckBox;
    private Context context;
    private String filepath = "MyFileStorage";
    private String savefile;
    private long reference_timeStamp = 1515880450757L;
    private Date lastSaved;
    private ArrayList<Date> dates = new ArrayList<>();
    private boolean checked;

    Count(String name, Context context){
        this.name = name;
        this.number = 0;
        this.context = context;
        liste = new ArrayList<>();
        //this.dataset = null;
        this.checked = false;

        this.savefile = this.name + ".txt";

        //create file here
        //need to check if a file with this name is already here

        File file = new File(context.getExternalFilesDir(filepath), savefile);
        if(file.exists()){
            //lecture du fichier
            //remplis la liste de dates
            loadDates();
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
        //Calendar c = Calendar.getInstance();
        String currentTime = Calendar.getInstance().getTime().toString();

        Date currentDate = new Date(currentTime);
        dates.add(currentDate);
        lastSaved = currentDate;
        //long currentTime = System.currentTimeMillis();
        //currentTime = (currentTime - reference_timeStamp)/1;
        //lastSaved = currentTime;
        //Log.d("name", String.valueOf(currentTime));

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
                outputStreamWriter.append(String.valueOf(currentTime)+"\n");
                outputStreamWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{


        }
    }

    public Date getLastSaved(){
        return lastSaved;
    }


    /*charges les dates enregistrees dans le fichier dans la liste de dates   */
    private void loadDates(){

    }

    /*cherche les dates de la stats ayant eu lieu le jour a afficher*/
    public ArrayList<Date> findByDay(int day, int year, String month){
        ArrayList<Date> listD = new ArrayList<>();
        for(Date date : dates){
            if(date.getYear() == year && date.getMonth().equals(month) && date.getDayNumber() == day){
                listD.add(date);
            }
        }
        return listD;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
