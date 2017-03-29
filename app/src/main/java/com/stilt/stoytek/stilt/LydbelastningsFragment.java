package com.stilt.stoytek.stilt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.stilt.stoytek.stilt.db.SoundlevelDataSource;
import com.stilt.stoytek.stilt.dtypes.SoundlevelMeasurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Random;

public class LydbelastningsFragment extends Fragment {


    TextView funfactText;
    OnLydbelastningListener mListener;
    Activity mActivity;
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    SoundlevelDataSource slSrc;


    private int counter = 0;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (funfactText != null) {
                slSrc.open();
                SoundlevelMeasurement slm = slSrc.getMostRecentMeasurement();

                funfactText.setText(""+slm.getdBval());
                slSrc.close();
            }
        }
        else {
            /* Do nothing */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        slSrc = new SoundlevelDataSource(this.getContext());

        View view = inflater.inflate(R.layout.lydbelastningsview,container,false);




//        //Graph stuff
//        LineGraphSeries<DataPoint> series;
//
//        //Graph stuff    https://youtu.be/zbTvJZX0UDk?t=3m47s
//        double y;
//        double x;
//
//        x = 0;      // hvor på x-aksen grafen skal starte

//        GraphView graph = (GraphView) view.findViewById(R.id.graphLyd);
//        series = new LineGraphSeries<DataPoint>();
//        for (int i = 0; i < 100; i++) {
//            x = x + 0.1;
//            y = Math.sin(x);    // y er funksjonen
//            series.appendData(new DataPoint(x, y), true, 100);  // det siste tallet i appendData må være likt som antall loops i for-løkka
//        }
//        graph.addSeries(series);
//
//



        // graph test 2
        graph = (GraphView) view.findViewById(R.id.graphLyd);
        series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(3, 20),
                new DataPoint(4, 10),
                new DataPoint(5, 6)
        });
        graph.addSeries(series);

        //fikser paddingen så alle tallene synes på y-aksen
//        GridLabelRenderer glr = graph.getGridLabelRenderer();
//        glr.setPadding(32); // should allow for 3 digits to fit on screen

        // activate horizontal zooming and scrolling
//        graph.getViewport().setScalable(true);

        // activate horizontal scrolling
//        graph.getViewport().setScrollable(true);

        // activate horizontal and vertical zooming and scrolling
//        graph.getViewport().setScalableY(true);

        // activate vertical scrolling
//        graph.getViewport().setScrollableY(true);


//        // setter x-aksen manuelt
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(160000);

//        // setter y-akssen manuelt
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(100);


        //setter texten bold og underllned
        TextView textLydbelastning = (TextView) view.findViewById(R.id.lydbelastningsText);
        textLydbelastning.setTypeface(null, Typeface.BOLD);
        String htmlString="<u>Lydbelastning</u>";
        textLydbelastning.setText(Html.fromHtml(htmlString));


        final Button startMaalingButton = (Button) view.findViewById(R.id.startMaalingButton);
        startMaalingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMaalingButton.setText("Stopp måling");
                ArrayList<SoundlevelMeasurement> randomData = getRandomData();
                Collections.sort(randomData);
                updateGraph(randomData);
            }
        });


        String[] funfacts = {
                "hei dette er imperial march du du du dududuu dududuu",
                "sap sap ching chong it's high noon",
                "why so serious?"
        };

        funfactText = (TextView) view.findViewById(R.id.funfactText);

        Random rand = new Random();
        int randomNum = rand.nextInt((2) + 1);

        funfactText.setText(funfacts[randomNum]);

        return view;
    }

    public void setFunFact(final String newText) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                funfactText.setText(newText);
            }
        });
    }

    public ArrayList<SoundlevelMeasurement> getRandomData() {
        SoundlevelDataSource soundlevelDataSrc = new SoundlevelDataSource(this.getActivity().getApplicationContext());
        soundlevelDataSrc.open();
        GregorianCalendar now = new GregorianCalendar();
        now.setTimeInMillis(System.currentTimeMillis());
        //ArrayList<SoundlevelMeasurement> list = soundlevelDataSrc.getSoundlevelMeasurementsFrom24HourWindowBeforeDate(now);
        ArrayList<SoundlevelMeasurement> randomData = soundlevelDataSrc.getRandomData();
        soundlevelDataSrc.close();
        Log.wtf("Lyd", "Random data list:" + randomData.get(0).getdBval());
        Log.wtf("Lyd", "Random data list:" + randomData.get(0).getTimestamp());
        return randomData;
    }

    public void updateGraph(final ArrayList<SoundlevelMeasurement> dataFromDB) {
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        GregorianCalendar now = new GregorianCalendar();
        now.setTimeInMillis(System.currentTimeMillis());
        Log.wtf("Lyd", "antall elementer: " + dataFromDB.size());
        Log.wtf("Lyd", "timestampinmillis: " + dataFromDB.get(0).getTimestampMillis());
        Log.wtf("Lyd", "timestampinmillis: " + dataFromDB.get(1).getTimestampMillis());
        Log.wtf("Lyd", "timestampinmillis: " + dataFromDB.get(2).getTimestampMillis()/100000000);
        Log.wtf("Lyd", "timestampinmillis: " + dataFromDB.get(3).getTimestampMillis()/100000000);
        for (int i = 0; i < dataFromDB.size(); i++) {
            //Log.wtf("Liste", "" + dataFromDB.get(i).getTimestampMillis());
            //series.appendData(new DataPoint(2 + i, 3 + i), true, 10000);
            //dataen på x-aksen på addes i stigende rekkefølge
            //dataene kan ikke være høyere enn int
            series.appendData(new DataPoint(now.getTimeInMillis() - dataFromDB.get(i).getTimestampMillis(), dataFromDB.get(i).getdBval()), true, 1000000000);
        }
        graph.addSeries(series);

        //den under kan brukes hvis det kræsjer

//        mTimer = new Runnable() {
//            @Override
//            public void run() {
//                graph.removeAllSeries();
//                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
//                for (int i = 0; i < dataFromDB.size(); i++) {
//                    series.appendData(new DataPoint(i, dataFromDB.get(i).getdBval()), true, 1000000000);
//                }
//                graph.addSeries(series);
//                mHandler.postDelayed(this, 300);
//            }
//        };
//        mHandler.postDelayed(mTimer, 300);
    }


    //API Level >= 23
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnLydbelastningListener) context;
    }

    //API Level < 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mListener = (OnLydbelastningListener) mActivity;
    }

    public interface OnLydbelastningListener {
        void setFunFactText();
    }



}