package com.test.romain.fanta_stat;

import java.util.ArrayList;

/**
 * Created by Romain on 10/01/2018.
 */

public class Count {
    String name;
    int number;
    ArrayList<String> liste;
    int idButton, idText1, idText2, idCheckBox;

    public Count(String name){
        this.name = name;
        this.number = 0;
        liste = new ArrayList<>();
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

}
