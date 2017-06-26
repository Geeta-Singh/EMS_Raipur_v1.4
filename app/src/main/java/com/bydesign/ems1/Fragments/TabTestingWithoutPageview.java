package com.bydesign.ems1.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabTestingWithoutPageview extends Fragment {
    private TabLayout tabLayout;
    private LinearLayout container;
    static int tempForCheck = 0;

    public TabTestingWithoutPageview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_testing_without_pageview, container, false);
    }

    // ProgressDialog pdialog = null;

    public void setTempForCheck(int temp){
        tempForCheck=temp;
    }

    public void onStart() {

        super.onStart();
        Runtime.getRuntime().gc();

        System.gc();
        System.gc();
        //   pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        container = (LinearLayout) getActivity().findViewById(R.id.fragment_containerL);

        final int Prograssicon = R.drawable.history;
        //  setSupportActionBar(toolbar);

        //create tabs title
        if (tempForCheck == 0) {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.historytdata));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.histrograph));
            // tabLayout.addTab(tabLayout.newTab().setText("Games"));
            tempForCheck++;
            //replace default fragment
            replaceFragment(new HistoricalData_Table());
        }
        //handling tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            String dfdf;

            //setPdialog(pdialog);
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              /*  if(tabLayout.getTabAt(0).isSelected()==true){
                    tabLayout.getTabAt(0).setIcon(Prograssicon);
                }
                else{
                    //tabLayout.getTabAt(0).setIcon(null);
                }*/
                if (tab.getPosition() == 0) {
                    Runtime.getRuntime().gc();

                    System.gc();
                    System.gc();
                    ;

                  /*  if (getPdialog() != null) {
                    //    pdialog.dismiss();
                    }*/
                    //pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
                    replaceFragment(new HistoricalData_Table());
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.setFlagForHistory(0);
                    //  pdialog.dismiss();
                } else if (tab.getPosition() == 1) {
                    Runtime.getRuntime().gc();

                    System.gc();
                    System.gc();
                    // tabLayout.getTabAt(0).setIcon(Prograssicon);
                    //tabLayout.getTabAt(0).setIcon(Prograssicon);
                    replaceFragment(new Graphical_HistoricalData());
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.setFlagForHistory(1);
                    // pdialog.dismiss();
                }
                //pdialog.dismiss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //  pdialog.dismiss();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //   pdialog.dismiss();


            }


        });
        //  pdialog.dismiss();
    }

    /* //public void setTabLayout(){
         tabLayout.getTabAt(0).setIcon(null);
     }*/
    private void replaceFragment(Fragment fragment) {
        // FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_containerL, fragment);
        transaction.commit();
        //  tabLayout.getTabAt(0).setIcon(null);
    }
}


