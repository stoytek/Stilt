package com.stilt.stoytek.stilt;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;

public class TtsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ttsview,container,false);

        GraphView graph = (GraphView) view.findViewById(R.id.graphLyd);

        //fikser paddingen så alle tallene synes på y-aksen
        GridLabelRenderer glr = graph.getGridLabelRenderer();
        glr.setPadding(32); // should allow for 3 digits to fit on screen

        //setter texten bold og underllned
        TextView textTTS = (TextView) view.findViewById(R.id.ttsText);
        textTTS.setTypeface(null, Typeface.BOLD);
        String htmlString="<u>TTS</u>";
        textTTS.setText(Html.fromHtml(htmlString));

        return view;
    }


}