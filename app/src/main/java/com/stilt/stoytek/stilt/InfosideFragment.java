package com.stilt.stoytek.stilt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InfosideFragment extends Fragment {

    // inside Activity
    TextView text11;
    TextView text21;





    String[] facts1 = {
            "Hvorfor fokus på lyd og hørsel?",
            "Fakta hentet fra Arbeidstilsynet",
            "Hørseltap som skyldes støy på arbeidsplassen er den vanligste yrkesskaden i Europa",
            "Bygg, anleggsindustrien og forsvaret er blant bransjene som melder flest støyskader til arbeidstilsynet"
    };

    String[] facts2 = {
            "Hvilke lover eksisterer for å motvirke hørselskader?",
            "Hørselvern vil normalt bare kunne benyttes som et midlertidlig hjelpemiddel til å beskytte arbeidstakere i mot støy. Der det er mulig skal støyen reduseres slik at hørselvern blir overflødig",
            "Arbeidsgiver skal sørge for at arbeidstakere og værneombud får løpende informasjon om risiko i forbindelse med støy og skal sikre at arbeidstaker får nødvendig informasjon",
            "Du utsettes for impulslyd: impulslyd er meget sterk og kortvarig støy som eksplosjoner, slaglyd, skudd fra gevær, spikerpistol og lignende. Regelverket setter en grense på 130 db (c)"
    };


    String[][] factsMainList = {
            facts1,
            facts2
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.infoside,container,false);

        for (int i; i < factsMainList.length; i++) {

        };

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
        text11 = (TextView) view.findViewById(R.id.text11);
        text11.setVisibility(View.GONE);


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

        // hide until its title is clicked
        text21 = (TextView) view.findViewById(R.id.text21);
        text21.setVisibility(View.GONE);

        return view;
    }


}
