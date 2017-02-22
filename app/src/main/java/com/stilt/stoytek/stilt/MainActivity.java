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
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lydbelastningsview);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

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


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();
}
