package com.stilt.stoytek.stilt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {


    //Graph stuff
    LineGraphSeries<DataPoint> series;
    

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("audio-recorde-jni");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lydbelastningsview);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText("Hello world!");


        //Graph stuff    https://youtu.be/zbTvJZX0UDk?t=3m47s
        double y;
        double x;

        x = 0;      // hvor på x-aksen grafen skal starte

        GraphView graph = (GraphView) findViewById(R.id.graphLyd);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 100; i++) {
            x = x + 0.1;
            y = Math.sin(x);    // y er funksjonen
            series.appendData(new DataPoint(x, y), true, 100);  // det siste tallet i appendData må være likt som antall loops i for-løkka
        }
        graph.addSeries(series);


        // graph test 2
//        GraphView graph2 = (GraphView) findViewById(R.id.graphLyd2);
//        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series2);

    }






}
