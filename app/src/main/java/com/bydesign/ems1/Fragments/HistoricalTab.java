package com.bydesign.ems1.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricalTab extends Fragment {

    TabHost tabHost;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HistoricalTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    static String graphDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historical_tab, container, false);

        return view;
    }

    ProgressDialog progressDialog;

    public void onStart() {
        super.onStart();
        Session_Management();

        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void Session_Management() {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.cleardata();
    }

    String lang;

    private void setupViewPager(final ViewPager viewPager) {

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        lang = Locale.getDefault().getLanguage();
        System.out.print("\n  language in history " + lang + "\n");
        //    Toast.makeText(getActivity(), "language "+lang, Toast.LENGTH_LONG).show();
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.setFlagForHistory(0);
        if (lang.equalsIgnoreCase("hi")) {
            adapter.addFragment(new HistoricalData_Table(), "पुराना डेटा(टेबल )");
            adapter.addFragment(new Graphical_HistoricalData(), "पुराना डेटा(ग्राफ )");

        } else {
            adapter.addFragment(new HistoricalData_Table(), "Historical Data(Table)");
            adapter.addFragment(new Graphical_HistoricalData(), " Historical Data(Graph)");

        }
        /*if (lang.equalsIgnoreCase("hi")) {
            progressDialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
        } else
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
        final Context application = getActivity();*/
        viewPager.setAdapter(adapter);

        ViewPager.OnPageChangeListener pagechangelistener = new ViewPager.SimpleOnPageChangeListener() {
            int scrollPossion = 0;

            @Override
            public void onPageSelected(int arg0) {

                //   viewPager.getAdapter().notifyDataSetChanged();
                viewPager.setCurrentItem(arg0);
                System.out.print("Historical tab inside OnPagechange Listener" + arg0);


            }

            @Override
            public void onPageScrolled(int possion, float arg1, int arg2) {
                if (possion == 0) {
                    scrollPossion = 0;
                   /* HistoricalData_Table historicalData_table = new HistoricalData_Table();
                    System.out.println( "tab histiry acess "+ historicalData_table.waitingText.getText());*/
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.setFlagForHistory(0);

                }
                if (possion == 1) {
                    /* HistoricalData_Table historicalData_table = new HistoricalData_Table();
                    System.out.println( "tab histiry acess "+ historicalData_table.waitingText.getText());*/

                    //yha se mujhe  check krna h ki table load hui h ya nahi
                    scrollPossion = 1;

                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.setFlagForHistory(1);
                }
                Log.d("", "Called second");

            }

            int PAGE = 2;
            /*viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (viewPager.getCurrentItem() == PAGE) {
                        viewPager.setCurrentItem(PAGE-1, false);
                        viewPager.setCurrentItem(PAGE, false);
                        return  true;
                    }
                    return false;
                }
*/
            public boolean onTouch(View v, MotionEvent event){
                if (viewPager.getCurrentItem() == PAGE) {
                    viewPager.setCurrentItem(PAGE-1, false);
                    viewPager.setCurrentItem(PAGE, false);
                    return  true;
                }
                return false;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == viewPager.SCROLL_STATE_IDLE) {
                    if (scrollPossion == 0) {

                        viewPager.getAdapter().notifyDataSetChanged();
                        // application.setActiveAction(false);
                    }

                    if (scrollPossion == 1) {

                        /* HistoricalData_Table historicalData_table = new HistoricalData_Table();
                         System.out.println( "tab histiry acess "+ historicalData_table.waitingText.getText());*/
                       /* if (lang.equalsIgnoreCase("hi")) {
                            progressDialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
                        } else
                            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);*/
                        //viewPager.notifyDataSetChanged();
                        viewPager.getAdapter().notifyDataSetChanged();//application.setArchiveAction(false);
                    }
                }
                /*if(){

                }*/
                Log.d("Called third", "");

            }
        };
        viewPager.addOnPageChangeListener(pagechangelistener);
        //setOnPageChangeListener(pagechangelistener);
        progressDialog.dismiss();
    }

    boolean _areLecturesLoaded = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            // loadLectures();
            _areLecturesLoaded = true;
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return PagerAdapter.POSITION_NONE;
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
