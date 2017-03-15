package com.stilt.stoytek.stilt;

import android.graphics.Typeface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Random;

public class LydbelastningsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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



        // graph test 2, ikke i bruk
        GraphView graph = (GraphView) view.findViewById(R.id.graphLyd);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(3, 20),
                new DataPoint(4, 10),
                new DataPoint(5, 6)
        });
        graph.addSeries(series);

        //fikser paddingen så alle tallene synes på y-aksen
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32); // should allow for 3 digits to fit on screen

        // activate horizontal zooming and scrolling
//        graph.getViewport().setScalable(true);

        // activate horizontal scrolling
//        graph.getViewport().setScrollable(true);

        // activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);

        // activate vertical scrolling
//        graph.getViewport().setScrollableY(true);


//        // setter x-aksen manuelt
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0.5);
//        graph.getViewport().setMaxX(3.5);
//
//        // setter y-akssen manuelt
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY(3.5);
//        graph.getViewport().setMaxY(8);


        //setter texten bold og underllned
        TextView textLydbelastning = (TextView) view.findViewById(R.id.lydbelastningsText);
        textLydbelastning.setTypeface(null, Typeface.BOLD);
        String htmlString="<u>Lydbelastning</u>";
        textLydbelastning.setText(Html.fromHtml(htmlString));


        final Button startMålingButton = (Button) view.findViewById(R.id.startMålingButton);
        startMålingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMålingButton.setText("Stopp måling");
            }
        });


        String[] funfacts = {
                "hei dette er imperial march du du du dududuu dududuu",
                "sap sap ching chong it's high noon",
                "why so serious?"
        };

        TextView funfactText = (TextView) view.findViewById(R.id.funfactText);

        Random rand = new Random();
        int randomNum = rand.nextInt((2) + 1);

        funfactText.setText(funfacts[randomNum]);

        return view;
    }
}