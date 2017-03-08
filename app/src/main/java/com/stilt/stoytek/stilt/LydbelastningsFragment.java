package com.stilt.stoytek.stilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class LydbelastningsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lydbelastningsview,container,false);


        //Graph stuff
        LineGraphSeries<DataPoint> series;

        //Graph stuff    https://youtu.be/zbTvJZX0UDk?t=3m47s
        double y;
        double x;

        x = 0;      // hvor på x-aksen grafen skal starte

        GraphView graph = (GraphView) view.findViewById(R.id.graphLyd);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 100; i++) {
            x = x + 0.1;
            y = Math.sin(x);    // y er funksjonen
            series.appendData(new DataPoint(x, y), true, 100);  // det siste tallet i appendData må være likt som antall loops i for-løkka
        }
        graph.addSeries(series);


        //fikser paddingen så alle tallene synes på y-aksen
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32); // should allow for 3 digits to fit on screen


        // graph test 2, ikke i bruk
//        GraphView graph2 = (GraphView) view.findViewById(R.id.graphLyd2);
//        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph2.addSeries(series2);


        final Button startMålingButton = (Button) view.findViewById(R.id.startMålingButton);
        startMålingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startMålingButton.setText("Stopp måling");
            }
        });

        return view;
    }
}