package com.test.romain.fanta_stat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MainActivity extends Activity implements  com.github.mikephil.charting.listener.OnChartGestureListener {

    Button b1 = null;
    CombinedChart chart = null;
    //private int idNumber = 100;
    private int nbStat = 0;
    private String startFile = "startFile.txt";
    //private String filename = "SampleFile.txt";
    private String filepath = "MyFileStorage";
    //private List<View> mViews= new ArrayList<View>();
    private List<Count> listCount = new ArrayList<>();
    public static String newline = System.getProperty("line.separator");
    private long reference_timeStamp = 1515880450757L;
    private List<IBarDataSet> dataSetsBar = new ArrayList<>();
    private List<ILineDataSet> dataSetsLine = new ArrayList<>();
    private CombinedData data = new CombinedData();

    private ArrayList<Integer> saveIndex = new ArrayList<>();
    //test



    LinearLayout ll = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if bundle != null
        if(savedInstanceState != null){

        }else{
            //premier onCreate()-->load savefile
            //l'application est demarée
            start();
            createStats();
        }


        b1 = findViewById(R.id.buttonFinal);
        ll = findViewById(R.id.layout1);

        b1.setOnClickListener(clickListenerBouton1);

        //test chart
        chart = findViewById(R.id.chart);

        //test of sinus data
        float mydata[][] = new float[2][50];
        for(int i=0; i<50; i++){
            mydata[1][i] = (float) Math.sin(i*0.1);
            mydata[0][i] = i;
        }


        List<Entry> entries = new ArrayList<>();
        //mydata[0].length
        for (int i=0; i<1; i++) {

            // turn your data into Entry objects
            //entries.add(new Entry(mydata[0][i], mydata[1][i]));
            entries.add(new Entry(0,0));
        }
        //test combiend data






        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSetsLine.add(dataSet);
        LineData lineData = new LineData(dataSetsLine);


        //data.setData(barData());
        //ajoute le sinus
        data.setData(lineData);
        //data.setData(barData());

        chart.setData(data);
        chart.invalidate(); // refresh


        chart.setOnChartGestureListener(this);

        XAxis x = chart.getXAxis();
        YAxis y = chart.getAxisLeft();
        y.setAxisMinimum(0);
        x.setAxisMinimum(0);
        x.setAxisMaximum(24);
        HourAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter();

        x.setValueFormatter(xAxisFormatter);
        //chart.setVisibleXRangeMaximum(5);

    }

    @Override
    public void onStop(){

        //saveDataApp(listCount);
        super.onStop();
    }

    /* Bouton principal, ajoute les stats*/
    private View.OnClickListener clickListenerBouton1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            Intent secondeActivite = new Intent(MainActivity.this, SecondActivity.class);
            //give the list of stats name to the second activity
            secondeActivite.putExtra("NumberOfNames", listCount.size());
            for(int i=0; i<listCount.size(); i++){
                secondeActivite.putExtra("name_"+i,listCount.get(i).getName());
            }


            startActivityForResult(secondeActivite, 1);
            //saveDataApp(listCount);
        }
    };

    /* appelé a la fin de la seconde activite, creation des stats*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");

                //createStat(strEditText); // creation des boutons
                Count count = new Count(strEditText, getApplicationContext());
                listCount.add(count);
                createOneStat(count);


            }
        }
    }

    /*Listener for adding one to the stats, refresh chart*/
    private View.OnClickListener onClickListenerAdd = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int nb = ((int) v.getId()- 100)/2;

            //si le nomnbre vaut zero et passe a un on insere le bon indice
            // on compte le nombre de stats avec 0 (indice -2)
            if(listCount.get(nb).getNumber() == 0){
                int nbStatZero=0;
                for(int i=0; i<saveIndex.size(); i++){
                    if(saveIndex.get(i) == -2) nbStatZero++;
                }
                saveIndex.set(nb, saveIndex.size()-nbStatZero);
            }

            listCount.get(nb).addNumber();
            listCount.get(nb).saveInFIle();
            TextView myText = findViewById((int) v.getId()-1);
            myText.setText(String.valueOf(listCount.get(nb).getNumber()));

            //Ajouter au graphe

            if(listCount.get(nb).getNumber()==1){
                //create a dataset

                BarEntry entrie = new BarEntry(listCount.get(nb).getLastSaved().getHour()+0.01f*listCount.get(nb).getLastSaved().getMinutes()+1, 1) ;
                //BarEntry entrie = new BarEntry(1, 1) ;
                ArrayList<BarEntry> entryList = new ArrayList<>();
                entryList.add(entrie);
                BarDataSet dataSet = new BarDataSet(entryList, "Label"); // add entries to dataset
                //listCount.get(nb).setDataset(dataSet);


                dataSetsBar.add(dataSet);

                BarData barData = new BarData(dataSetsBar);
                data.setData(barData);
                chart.setData(data);



            }else{

                dataSetsBar.get(nb).addEntry(new BarEntry(listCount.get(nb).getLastSaved().getHour()+0.01f*listCount.get(nb).getLastSaved().getMinutes()+1,1 ));
                BarData barData = new BarData(dataSetsBar);
                data.setData(barData);

            }

            chart.notifyDataSetChanged();
            chart.invalidate();



        }
    };

    //listener for the checkBox
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            @SuppressLint("ResourceType") int nb = buttonView.getId()-500;
            Log.d("nb", String.valueOf(nb));
            //ne fait rien si la stat vaut 0
            if(listCount.get(nb).getNumber() !=0 ){
                //la case a été cochée, on affiche (on recree)
                if (isChecked){


                    listCount.get(nb).setChecked(true);
                    /****Recreation du graphe quand la checkBox est cochee****/
                    Date today = new Date(Calendar.getInstance().getTime().toString());
                    ArrayList<Date> liste = listCount.get(nb).findByDay(today.getDayNumber(), today.getYear(), today.getMonth());
                    ArrayList<BarEntry> entryList = new ArrayList<>();

                    for(Date date : liste){
                        entryList.add(new BarEntry(date.getHour()+0.01f*date.getMinutes()+1,1 ));
                    }

                    BarDataSet dataSet = new BarDataSet(entryList, "Label"); // add entries to dataset

                    dataSetsBar.add(dataSet);
                    BarData barData = new BarData(dataSetsBar);
                    data.setData(barData);
                    chart.setData(data);
                    chart.notifyDataSetChanged();
                    chart.invalidate();


                    /***Gestion des indices necessaires pour afficher/cacher***/
                    //gestions indices
                    //pas uniquement se mettre a la derniere place mais aussi regarder les non checked (-1)
                    int nbMoins = 0;
                    for(int i=0; i<saveIndex.size(); i++){
                        if(saveIndex.get(i) == -1 || saveIndex.get(i) == -2){
                            nbMoins ++;
                        }
                    }
                    saveIndex.set(nb, saveIndex.size()-nbMoins);



                }else {

                    /***Destruction des graphes quand la checkBox est decochee***/
                    listCount.get(nb).setChecked(false);
                    //hide dataset
                    dataSetsBar.remove(dataSetsBar.get(saveIndex.get(nb)));
                    BarData barData = new BarData(dataSetsBar);
                    data.setData(barData);

                    chart.notifyDataSetChanged();
                    chart.invalidate();

                    /***Gestion des indices necessaires pour afficher/cacher***/
                    /** Indices : -2 stat vaut 0, -1 case decochee **/
                    saveIndex.set(nb, -1);
                    for(int i=nb; i<saveIndex.size(); i++){
                        if(saveIndex.get(i)>nb && saveIndex.get(i) != -1 && saveIndex.get(i)!= -2){
                            saveIndex.set(i, saveIndex.get(i)-1);
                        }
                    }

                }
            }
        }
    };

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
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

            Count count = new Count(savedInstanceState.getString("name_"+String.valueOf(i)), getApplicationContext());


            listCount.add(count);

            LayoutInflater vi = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.template_view, null);

            // fill in any details dynamically here
            TextView textView = v.findViewById(R.id.templateText1);
            textView.setText(savedInstanceState.getString("name_"+String.valueOf(i)));
            //count.setName(savedInstanceState.getString("name_"+String.valueOf(i)));

            TextView textView2 = v.findViewById(R.id.templateText2);
            textView2.setText(String.valueOf(savedInstanceState.getInt("Stat_"+String.valueOf(i))));
            count.setNumber(savedInstanceState.getInt("Stat_"+String.valueOf(i)));
            textView2.setId(99+2*i);
            count.setIdText2(R.id.templateText2);


            Button myButton = v.findViewById(R.id.magic_button);
            myButton.setId(99+2*i+1);
            count.setIdButton(R.id.magic_button);
            myButton.setOnClickListener(onClickListenerAdd);

            CheckBox checkBox = v.findViewById(R.id.check);
            checkBox.setId(500+i);
            checkBox.setOnCheckedChangeListener(onCheckedChangeListener);

            // insert into main view
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.layout1);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

    }


    /*
    Fonction a appeler à la creation
    check si le fichier existe
    puis le lis ou le cree
    première ligne nombre de stats sauvegardées

     */

    /*recupere les données dans le fichier ou le cree*/
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
                int nbLignes =0;
                while ((strLine = br.readLine()) != null) {

                    myData = myData + strLine;
                    //recupere le nb de stat sur la première ligne
                    if(nbLignes==0){
                        nbStat = Integer.parseInt(strLine);
                    }else {
                        //save data lignes suivantes
                        //lignes : Name number
                        String[] parts = strLine.split(" ");
                        Count count = new Count(parts[0], getApplicationContext());
                        count.setNumber(Integer.parseInt(parts[1]));
                        listCount.add(count);
                    }
                    nbLignes++;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //recuperer texte
        }
        else{
            //creation du fichier avec 0 stats
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


    /*
    Sauve les data à la destruction de l'app
    ecrit tout dans le fichier de save
    première ligne nb de stats
    ensuite Name number\n
     */
    public void saveDataApp(List<Count> list){
        File myExternalFile;
        myExternalFile = new File(getExternalFilesDir(filepath), startFile);

        if (isExternalStorageWritable()){
            try {

                FileOutputStream fos = new FileOutputStream(myExternalFile);
                fos.write((String.valueOf(list.size())+newline).getBytes());//nombre d'elements
                //boucle sur le nombre de stats
                for(int i=0; i< list.size(); i++){
                    if(i==0){
                        fos.write((list.get(i).getName()+" ").getBytes());
                        fos.write(String.valueOf(list.get(i).getNumber()).getBytes());
                    }else{
                        fos.write((newline+list.get(i).getName()+" ").getBytes());

                        fos.write(String.valueOf(list.get(i).getNumber()).getBytes());

                    }
                }
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*creation des stats apres demarage de l'app*/
    public void createStats(){
        for(int i=0; i<listCount.size(); i++){
            createOneStat(listCount.get(i), i);
        }
    }

    public void createOneStat(Count count){
        LayoutInflater vi = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.template_view, null);

        // fill in any details dynamically here
        TextView textView = v.findViewById(R.id.templateText1);
        textView.setText(count.getName());

        TextView textView2 = v.findViewById(R.id.templateText2);
        textView2.setText(String.valueOf(count.getNumber()));

        textView2.setId(99+2*listCount.size()-1);
        count.setIdText2(R.id.templateText2);

        CheckBox checkBox = v.findViewById(R.id.check);
        checkBox.setId(500+listCount.size()-1);


        Button myButton = v.findViewById(R.id.magic_button);
        myButton.setId(99+2*listCount.size());
        count.setIdButton(R.id.magic_button);
        myButton.setOnClickListener(onClickListenerAdd);

        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        saveIndex.add(-2);

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.layout1);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    //to use for modifyng id
    public void createOneStat(Count count, int i){
        LayoutInflater vi = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.template_view, null);

        // fill in any details dynamically here
        TextView textView = v.findViewById(R.id.templateText1);
        textView.setText(count.getName());

        TextView textView2 = v.findViewById(R.id.templateText2);
        textView2.setText(String.valueOf(count.getNumber()));
        textView2.setId(99+2*i);
        count.setIdText2(R.id.templateText2);


        Button myButton = v.findViewById(R.id.magic_button);
        myButton.setId(99+2*i+1);
        count.setIdButton(R.id.magic_button);
        myButton.setOnClickListener(onClickListenerAdd);

        CheckBox checkBox = v.findViewById(R.id.check);
        checkBox.setId(500+i);

        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        saveIndex.add(-2);

        // insert into main view
        ViewGroup insertPoint = findViewById(R.id.layout1);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private ArrayList<String> getXAxisValues() {
            ArrayList<String> labels = new ArrayList<>();
            labels.add("JAN");
            labels.add("FEB");
            labels.add("MAR");
            labels.add("APR");
            labels.add("MAY");
            labels.add("JUN");
        return labels;
    }

    public LineData lineData(){
            ArrayList<Entry> line = new ArrayList<>();
            line.add(new Entry(2f, 0));
            line.add(new Entry(4f, 1));
            line.add(new Entry(3f, 2));
            line.add(new Entry(6f, 3));
            line.add(new Entry(9f, 4));
            line.add(new Entry(4f, 5));
       LineDataSet lineDataSet = new LineDataSet(line, "Brand 2");
                lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        LineData lineData = new LineData(lineDataSet);
       return lineData;
    }
    // this method is used to create data for Bar graph
    public BarData barData(){
       ArrayList<BarEntry> group1 = new ArrayList<>();
                group1.add(new BarEntry(4f, 0));
                group1.add(new BarEntry(8f, 1));
                group1.add(new BarEntry(6f, 2));
                group1.add(new BarEntry(12f, 3));
                group1.add(new BarEntry(18f, 4));
                group1.add(new BarEntry(9f, 5));
       BarDataSet barDataSet = new BarDataSet(group1, "Brand 1");
                //barDataSet.setColor(Color.rgb(0, 155, 0));
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                BarData barData = new BarData(barDataSet);
        return barData;
   }
}
