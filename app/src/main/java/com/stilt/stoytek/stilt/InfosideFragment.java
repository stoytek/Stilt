package com.stilt.stoytek.stilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfosideFragment extends Fragment {

    // inside Activity
    TextView text11;
    TextView text21;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.infoside,container,false);

        // hide until its title is clicked
        text11 = (TextView) view.findViewById(R.id.text11);
        text11.setVisibility(View.GONE);


        final TextView title1 = (TextView) view.findViewById(R.id.title1);
        title1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(text11.isShown()){
                    Fx.slide_up(getActivity(), text11);
                    text11.setVisibility(View.GONE);
                }
                else{
                    text11.setVisibility(View.VISIBLE);
                    Fx.slide_down(getActivity(), text11);
                }
            }
        });

        // hide until its title is clicked
        text21 = (TextView) view.findViewById(R.id.text21);
        text21.setVisibility(View.GONE);

        final TextView title2 = (TextView) view.findViewById(R.id.title2);
        title2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(text21.isShown()){
                    Fx.slide_up(getActivity(), text21);
                    text21.setVisibility(View.GONE);
                }
                else{
                    text21.setVisibility(View.VISIBLE);
                    Fx.slide_down(getActivity(), text21);
                }
            }
        });

        return view;
    }


}
