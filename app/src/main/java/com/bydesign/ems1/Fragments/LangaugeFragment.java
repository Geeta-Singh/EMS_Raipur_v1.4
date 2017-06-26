package com.bydesign.ems1.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class LangaugeFragment extends Fragment {

    private Locale myLocale;
    public LangaugeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_langauge, container, false);
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                String lang = "en";
                switch (checkedId) {
                    case R.id.radio_hindi:
                        lang="hi";
                        Toast.makeText(getActivity(),"hindi", Toast.LENGTH_LONG).show(); // Pirates are the best // switch to fragment 1
                        break;
                    case R.id.radio_english:
                        lang="en";
                        Toast.makeText(getActivity(),"hindi", Toast.LENGTH_LONG).show(); // Pirates are the best// Fragment 2
                        break;

                }
                changeLang(lang);
                Intent intent = getActivity().getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();

                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);
               // getActivity().finish();
               /* DataConditionFragment main=new DataConditionFragment();
                android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container,main);
                ft.commit();
                navigationdrawer nv=new navigationdrawer();
                nv.toolbar.setTitle(R.string.title_activity_dataCondition);*/
            }
        });

        loadLocale();
        return view;
    }
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
           getActivity().getBaseContext().getResources().updateConfiguration(newConfig,getActivity(). getBaseContext().getResources().getDisplayMetrics());
        }
    }
    public void loadLocale()
    {
        //String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("EMS", Activity.MODE_PRIVATE);
        String language = prefs.getString("Language", "");
        changeLang(language);
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
     //   updateTexts();
    }
    public void saveLocale(String lang)
    {
        SessionManager sessionManager=new SessionManager(getActivity());
        sessionManager.setLanguage(lang);
    }

}
/* public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_hindi:
                if (checked)
                    Toast.makeText(getActivity(),"hindi", Toast.LENGTH_LONG).show(); // Pirates are the best
                    break;
            case R.id.radio_english:
                if (checked)
                    Toast.makeText(getActivity(),"english",Toast.LENGTH_LONG).show(); // Ninjas rule
                    break;
        }
    }*/