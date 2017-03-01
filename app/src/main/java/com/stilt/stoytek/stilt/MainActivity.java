package com.stilt.stoytek.stilt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.media.AudioRecord;
import android.media.AudioFormat;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {


    //Graph stuff
    LineGraphSeries<DataPoint> series;
    

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("audiorec");
    }


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


        // graph test 2, ikke i bruk
//        GraphView graph2 = (GraphView) findViewById(R.id.graphLyd2);
//        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph2.addSeries(series2);

    }

    /* Check for valid sample rates, pass highest possible samplerate to createAudioEngine */
    /* TODO: This might be possible to do with OpenSLES
     *  SLAudioIODeviceCapabilitiesItf_->QuerySampleFormatsSupported(SLAudioIODeviceCapabilitiesItf self,
	 *	SLuint32 deviceId,
	 *	SLmilliHertz samplingRate,
	 *	SLint32 *pSampleFormats,
	 *	SLint32 *pNumOfSampleFormats);
     */

    private int getMaxSupportedSampleRate() {

        final int validSampleRates[] = new int[] { 8000, 11025, 16000, 22050,
                32000, 37800, 44056, 44100};

        for (int i = 0; i < validSampleRates.length; i++) {
            int result = AudioRecord.getMinBufferSize(validSampleRates[i],
                    AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_DEFAULT);
            if (result != AudioRecord.ERROR
                    && result != AudioRecord.ERROR_BAD_VALUE && result > 0) {
                return validSampleRates[i];
            }
        }

        return -1;
    }

}
