package com.bydesign.ems1.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bydesign.ems1.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

//import com.github.mikephil.charting.formatter.ValueFormatter;

//import com.github.mikephil.charting.utils.ValueFormatter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Graphical_HistoricalData extends Fragment implements OnChartValueSelectedListener {


    private LinearLayout m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9, txt10, txt11, txt12;
    ArrayList<Entry> entries;
    ArrayList<String> labels;
    private LineChart mChart;
    ImageButton Zoom_history;

    public Graphical_HistoricalData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graphical__historical_data, container, false);
        //    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return view;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    CardView cv, cv1, cv2, cv3, cv4, cv5, cv6, cv7, cv8, cv9, cv10, cv11, card;

    private TextView device, gran, sts, ets;
    String token, url, Device, opt;
    Long Sts, Ets;
    int Gran;
    String lang;
    LinearLayout ll;
    String status;

    public void onStart() {
        super.onStart();

        //________________________Declaration of views_________________
        m1 = (LinearLayout) getView().findViewById(R.id.mainLayout1);
        m2 = (LinearLayout) getView().findViewById(R.id.mainLayout2);
        m3 = (LinearLayout) getView().findViewById(R.id.mainLayout3);
        m4 = (LinearLayout) getView().findViewById(R.id.mainLayout4);
        m5 = (LinearLayout) getView().findViewById(R.id.mainLayout5);
        m6 = (LinearLayout) getView().findViewById(R.id.mainLayout6);
        m7 = (LinearLayout) getView().findViewById(R.id.mainLayout7);
        m8 = (LinearLayout) getView().findViewById(R.id.mainLayout8);
        m9 = (LinearLayout) getView().findViewById(R.id.mainLayout9);
        m10 = (LinearLayout) getView().findViewById(R.id.mainLayout10);
        m11 = (LinearLayout) getView().findViewById(R.id.mainLayout11);
        m12 = (LinearLayout) getView().findViewById(R.id.mainLayout12);


        txt1 = (TextView) getView().findViewById(R.id.text1);
        txt2 = (TextView) getView().findViewById(R.id.text2);
        txt3 = (TextView) getView().findViewById(R.id.text3);
        txt4 = (TextView) getView().findViewById(R.id.text4);
        txt5 = (TextView) getView().findViewById(R.id.text5);
        txt6 = (TextView) getView().findViewById(R.id.text6);
        txt7 = (TextView) getView().findViewById(R.id.text7);
        txt8 = (TextView) getView().findViewById(R.id.text8);
        txt9 = (TextView) getView().findViewById(R.id.text9);
        txt10 = (TextView) getView().findViewById(R.id.text10);
        txt11 = (TextView) getView().findViewById(R.id.text11);
        txt12 = (TextView) getView().findViewById(R.id.text12);


        cv = (CardView) getView().findViewById(R.id.card_viewp1);
        cv1 = (CardView) getView().findViewById(R.id.card_view2);
        cv2 = (CardView) getView().findViewById(R.id.card_viewp3);
        cv3 = (CardView) getView().findViewById(R.id.card_viewp4);
        cv4 = (CardView) getView().findViewById(R.id.card_viewp5);
        cv5 = (CardView) getView().findViewById(R.id.card_viewp6);
        cv6 = (CardView) getView().findViewById(R.id.card_viewp7);
        cv7 = (CardView) getView().findViewById(R.id.card_viewp8);
        cv8 = (CardView) getView().findViewById(R.id.card_viewp9);
        cv9 = (CardView) getView().findViewById(R.id.card_viewp10);
        cv10 = (CardView) getView().findViewById(R.id.card_viewp11);
        cv11 = (CardView) getView().findViewById(R.id.card_viewp12);
        card = (CardView) getView().findViewById(R.id.card_view1);
        // ll=(LinearLayout)getView().findViewById(R.id.hl);
        cv.setVisibility(View.INVISIBLE);
        cv1.setVisibility(View.INVISIBLE);
        cv2.setVisibility(View.INVISIBLE);
        cv3.setVisibility(View.INVISIBLE);
        cv4.setVisibility(View.INVISIBLE);
        cv5.setVisibility(View.INVISIBLE);
        cv6.setVisibility(View.INVISIBLE);
        cv7.setVisibility(View.INVISIBLE);
        cv8.setVisibility(View.INVISIBLE);
        cv9.setVisibility(View.INVISIBLE);
        cv10.setVisibility(View.INVISIBLE);
        cv11.setVisibility(View.INVISIBLE);
        card.setVisibility(View.INVISIBLE);
        // gran = (TextView) getView().findViewById(R.id.gran);
        //   sts = (TextView) getView().findViewById(R.id.sts);
        //  ets = (TextView) getView().findViewById(R.id.ets);
        //_____________________________________________________________
        Zoom_history = (ImageButton) getView().findViewById(R.id.zoom_history);
        Zoom_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(), R.string.zoom, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                snackbar.show();
            }
        });
        device = (TextView) getView().findViewById(R.id.sectdevice);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        int historyFalg = sharedPreferences.getInt("Historyflag", -1);
        lang = Locale.getDefault().getLanguage();
        Device = sharedPreferences.getString("hisDevice", "nodevice");


        System.out.println("history flag graph deactivated   " + historyFalg);
        if (historyFalg == 1) {

            System.out.println("history flag gra achistory flagtivated" + historyFalg);
            token = sharedPreferences.getString("token", null);

            url = sharedPreferences.getString("url", null);

//        Sts= sharedPreferences.getLong("sts", 0);
            //  Gran= sharedPreferences.getInt("gran", 0);
            //  Ets= sharedPreferences.getLong("ets", 0);
            // opt=sharedPreferences.getString("option", null);
            System.out.println("token :" + token + " url" + url + " device name of histor " + Device + "");

            System.out.print("inside on start of his graph");
            //OOM PROTECTION
            Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());

            if (Device.equalsIgnoreCase("nodevice")) {
                Snackbar snackbar;
                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(), R.string.enterDate, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                //Displaying the snackbar using the show method()
                snackbar.show();
            } else {
                //.setText(": " + Device );
                if (isConnected()) {
                    //  pdialog = ProgressDialog.show(getActivity(), "", "please wait..", true);
                    // new HttpAsyncTask().execute();
                    if (lang.equalsIgnoreCase("hi")) {
                        device.setText("उपकरण नाम  : " + Device);
                        pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
                    } else {
                        device.setText("Device Name : " + Device);
                        pdialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
                    }
                    card.setVisibility(View.VISIBLE);
                    status = sharedPreferences.getString("tablegraphsession", "nodata");
                    if (status.equalsIgnoreCase("nodata")) {
                  /* MapFragment main=new MapFragment();
                   android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                   ft.replace(R.id.frame_container, main);
                   ft.commit();*/
                        Snackbar snackbar;
                        //Initializing snackbar using Snacbar.make() method
                        snackbar = Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_LONG);
                        //Displaying the snackbar using the show method()
                        snackbar.show();
                        pdialog.dismiss();
                    } else if (status.equalsIgnoreCase("[]")) {
                        Snackbar snackbar;
                        //Initializing snackbar using Snacbar.make() method
                        snackbar = Snackbar.make(getView(), R.string.Nodata, Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68)); //Displaying the snackbar using the show method()
                        snackbar.show();
                       /* SessionManager sessionManage = new SessionManager(getActivity());
                        sessionManage.setFlagForHistory(0);*/
                        pdialog.dismiss();
                    } else {
                      /*  if (lang.equalsIgnoreCase("hi")) {
                            pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);

                        } else {
                            pdialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
                        }*/
                        new HttpAsyncTaskForHistoryTabCAlling().execute();
                       /* System.out.println("@Table and graphe data inside graph :" + status);
                        // Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
                        //    addHeaders(tablegraph);
                        getvaluesfromJsonObject(status);
                        System.out.print("\nSESSION SET FOR GRAPHR CHANGE");
                        cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                        cv1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                        cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                        pdialog.dismiss();*/
                    }
                    //  pdialog.dismiss();
                } else {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        } else {
           /* System.out.print("history flag inside else of graph");
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), R.string.enterDate, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            //Displaying the snackbar using the show method()
            snackbar.show();*/

            // pdialog.dismiss();
            //Toast.makeText(getActivity(), "Please enter date", Toast.LENGTH_LONG).show();

        }

    }


    /*********************************
     * start back to history table froom graph
     ***************************************/

    private class HttpAsyncTaskForHistoryTabCAlling extends AsyncTask<String, Void, String> {
        int i = 0;

        @Override
        protected String doInBackground(String... urls) {
            //  addHeaders(tablegraph);
            // addDataTable(tablegraph);

            return "suseess";

        }

        /*protected void onPreExecute() {

        }*/

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String Status) {
            System.out.println("@Table and graphe data inside graph :" + status);
            // Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
            //    addHeaders(tablegraph);
            getvaluesfromJsonObject(status);
            System.out.print("\nSESSION SET FOR GRAPHR CHANGE");
            cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
            cv1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
            cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
            pdialog.dismiss();

            //  pdialog.dismiss();
        }
    }


    /*********************************
     * end back to history table froom graph
     ***************************************/

    ProgressDialog pdialog = null;

    //*********************************************************************************

   /* public boolean check(String user, String pwd) {
        if (user.equalsIgnoreCase("") || pwd.equalsIgnoreCase("")) {
            return false;
        } else return true;
    }
*/

    //**************************** making graph *****************************
   /* void makeGraph(ArrayList<BarEntry> entries, ArrayList<String> labels,int i,String param,String alarm){
        BarDataSet dataset = new BarDataSet(entries, "# "+param+" and \n "+alarm+" # threshold value ");*/
    void makeGraph(ArrayList<Entry> entries, final ArrayList<String> labels, int i, String param, String alarm, String unit) {


        Zoom_history.setVisibility(View.VISIBLE);
        if (i == 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m1.getLayoutParams();
            params.height = 550;

            cv.setVisibility(View.VISIBLE);
            // mChart = (BarChart) getActivity().findViewById(R.id.barchart1);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m1.addView(mChart);
            txt1.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            // mChart.setMinimumHeight(490);
           /* mChart = new BarChart(getActivity());
            mChart.setMinimumWidth(550);
            mChart.setMinimumHeight(550);
            m1.addView(mChart);*/

        } else if (i == 1) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m2.getLayoutParams();
            params.height = 550;
            cv1.setVisibility(View.VISIBLE);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m2.addView(mChart);
           /* mChart = new BarChart(getActivity());
            mChart.setMinimumHeight(550);
            mChart.setMinimumWidth(500);
            m2.addView(mChart);*/
            //  mChart = (BarChart) getActivity().findViewById(R.id.barchart2);
            //  mChart.setMinimumHeight(490);
            txt2.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
        } else if (i == 2) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m3.getLayoutParams();
            params.height = 550;
            cv2.setVisibility(View.VISIBLE);
            // mChart = (BarChart) getActivity().findViewById(R.id.barchart3);
            txt3.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            //  mChart.setMinimumHeight(490);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m3.addView(mChart);
//            mChart = new BarChart(getActivity());
//            mChart.setMinimumHeight(550);
//            mChart.setMinimumWidth(550);
//            m3.addView(mChart);
        } else if (i == 3) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m4.getLayoutParams();
            params.height = 550;
            cv3.setVisibility(View.VISIBLE);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m4.addView(mChart);
           /* mChart = new BarChart(getActivity());
            m4.addView(mChart);*/
            // mChart = (BarChart) getActivity().findViewById(R.id.barchart4);
            txt4.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
        } else if (i == 4) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m5.getLayoutParams();
            params.height = 550;
            cv4.setVisibility(View.VISIBLE);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m5.addView(mChart);
           /* mChart = new BarChart(getActivity());
            m5.addView(mChart);*/
            //  mChart = (BarChart) getActivity().findViewById(R.id.barchart5);
            txt5.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
        } else if (i == 5) {
            //  mChart = new BarChart(getActivity());
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m6.getLayoutParams();
            params.height = 550;
            cv5.setVisibility(View.VISIBLE);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m6.addView(mChart);
            //  m6.addView(mChart);
            //  mChart = (BarChart) getActivity().findViewById(R.id.barchart6);
            txt6.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
        } else if (i == 6) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m7.getLayoutParams();
            params.height = 550;

            cv6.setVisibility(View.VISIBLE);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m7.addView(mChart);
          /*  mChart = new BarChart(getActivity());
            m7.addView(mChart);*/
            //  mChart = (BarChart) getActivity().findViewById(R.id.barchart7);
            txt7.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
        } else if (i == 7) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m8.getLayoutParams();
            params.height = 550;
            cv7.setVisibility(View.VISIBLE);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m8.addView(mChart);
           /* mChart = new BarChart(getActivity());
            m8.addView(mChart);*/
            // mChart = (BarChart) getActivity().findViewById(R.id.barchart8);
            txt8.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
        } else if (i == 8) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m9.getLayoutParams();
            params.height = 550;
            cv8.setVisibility(View.VISIBLE);

           /* mChart = new BarChart(getActivity());
            m9.addView(mChart);*/
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m9.addView(mChart); // mChart = (BarChart) getActivity().findViewById(R.id.barchart9);
            txt9.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
        } else if (i == 9) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m10.getLayoutParams();
            params.height = 550;
            cv9.setVisibility(View.VISIBLE);
            // mChart = (BarChart) getActivity().findViewById(R.id.barchart10);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m10.addView(mChart);
            txt10.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
           /* mChart = new BarChart(getActivity());
            m10.addView(mChart);*/
        } else if (i == 10) {
            cv10.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m11.getLayoutParams();
            params.height = 550;
            //  mChart = (BarChart) getActivity().findViewById(R.id.barchart11);
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m11.addView(mChart);
            txt11.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
            mChart.setMinimumHeight(490);
            /*mChart = new BarChart(getActivity());
            m11.addView(mChart);*/
        } else if (i == 11) {
            cv11.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) m12.getLayoutParams();
            params.height = 550;
            BarChart.LayoutParams chart = new BarChart.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);//mChart.getLayoutParams();
            mChart = new LineChart(getActivity());
            mChart.setLayoutParams(chart);
            m12.addView(mChart);
          /*  mChart = new BarChart(getActivity());
            m12.addView(mChart);*/
          /*  mChart = (BarChart) getActivity().findViewById(R.id.barchart12);
            mChart.setMinimumHeight(490);*/
            txt12.setText(param + " (" + unit + ")  , Threshold value: " + alarm);
        }


        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        // add data
        // setData(200, 30);

        mChart.animateX(2500);
        //---------------------line thresold----------------------------
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        IAxisValueFormatter xAxisFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                System.out.println(value);
                if (((int) value) < labels.size()) {
                    return (labels.get((int) value));
                } else {
                    return " ";
                }
            }
        };
        xAxis.setValueFormatter(xAxisFormatter);
        mChart.getAxisRight().setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(200f);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

       /* LimitLine ll = new LimitLine(120f, "11");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setTextColor(Color.BLUE);
        ll.setTextSize(10f);
        leftAxis.addLimitLine(ll);*/


        Legend l = mChart.getLegend();
        l.setEnabled(false);

        LimitLine ll = new LimitLine(Float.parseFloat(alarm), "");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(1f);
        ll.setTextColor(Color.BLUE);
        ll.setTextSize(10f);
        leftAxis.addLimitLine(ll);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int j = 0; j < labels.size() - 1; j++) {
        colors.add(Color.argb(255, 124, 204, 241));
        // }
        // colors.add(Color.WHITE);//colors.add(Color.WHITE);

        LineDataSet set1;
        // LineDataSet barDataSet1 = new LineDataSet(entries, "Bar Group 1");

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            System.out.println("if me hu");
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            System.out.println("else me hu");

            set1 = new LineDataSet(entries, "The year 2017");
            set1.setColors(colors);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColors(colors);
            set1.setCircleColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(2f);
          /*  ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);*/
            LineData data = new LineData(set1);
            data.setValueTextSize(10f);
            data.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                    DecimalFormat mf = new DecimalFormat("###,###,##0.00");
                    return mf.format(v);
                }


            });

            mChart.setData(data);
        }


        // **************** end thresold line ***********************************
    }


    void getvaluesfromJsonObject(String status) {
        JSONArray jsonObject = null;

        try {
            jsonObject = new JSONArray(status);

            if (jsonObject.length() == 0) {
                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(), R.string.Nodata, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                //Displaying the snackbar using the show method()
                snackbar.show();
                m1.removeAllViews();
                m2.removeAllViews();
                ;
                m3.removeAllViews();
                m4.removeAllViews();
                m5.removeAllViews();
                m6.removeAllViews();
                m7.removeAllViews();
                m8.removeAllViews();
                m9.removeAllViews();
                m10.removeAllViews();
                m11.removeAllViews();
                m12.removeAllViews();

            }
            for (int i = 0; i < jsonObject.length(); i++) {

                JSONObject object = jsonObject.getJSONObject(i);

                System.out.print("\n jsonobject in history  " + jsonObject + "\n object   " + object + "\n length " + jsonObject.length() + "\n");

                JSONArray value = (JSONArray) object.get("values");
                JSONArray paramArray = (JSONArray) object.get("param");
                JSONArray alarmArray = (JSONArray) object.get("alarm");
                JSONArray unitArray = (JSONArray) object.get("units");

                if (value.length() == 0) {
                    Snackbar snackbar;

                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), R.string.Nodata, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                    //   m1.addView(null);
                    m1.removeAllViews();
                    m2.removeAllViews();
                    ;
                    m3.removeAllViews();
                    m4.removeAllViews();
                    m5.removeAllViews();
                    m6.removeAllViews();
                    m7.removeAllViews();
                    m8.removeAllViews();
                    m9.removeAllViews();
                    m10.removeAllViews();
                    m11.removeAllViews();
                    m12.removeAllViews();

                }

                for (int p = 0; p < paramArray.length(); p++) {
                    labels = new ArrayList<String>();
                    entries = new ArrayList<>();
                    System.out.println(paramArray.get(p));
                    for (int k = 0; k < value.length(); k++) {
                        JSONObject valueobject = value.getJSONObject(k);

                        String param = "";
                        try {
                            param = valueobject.get(paramArray.get(p).toString().toUpperCase()).toString();
                        } catch (JSONException e) {
                            try {
                                param = valueobject.get(paramArray.get(p).toString().toLowerCase()).toString();
                            } catch (JSONException e1) {
                                try {
                                    param = valueobject.get(paramArray.get(p).toString()).toString();//.toUpperCase();
                                } catch (JSONException e2) {
                                    e2.printStackTrace();
                                    System.out.print("normal also");
                                }
                                System.out.print("Lowerr case not working");
                                e1.printStackTrace();
                                //System.out.print("index out of bound");
                            }
                            System.out.print("UPPER case not working");
                            e.printStackTrace();

                        }

                        Long ts1 = (Long) valueobject.get("ts");
                        String ts = "";
                        String timestamp = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(ts1));
                        if (param.isEmpty()) {
                            param = "0";
                        }

                        StringTokenizer st = new StringTokenizer(timestamp, " ");
                        while (st.hasMoreTokens()) {
                            ts = st.nextToken();
                            //    System.out.print("\ndate time... : "+dt[i]);

                        }
                        // entries.add(new BarEntry(4f, 0));
                        entries.add(new BarEntry(k, Float.parseFloat(param)));
                        //  System.out.println(" value of param  " + Float.parseFloat(param));
                        labels.add(ts);
                        //  System.out.println("param " + param + "   ts " + ts + "  k  " + k + "\n");

                    }
                    /*entries.add(new BarEntry(value.length() + 1, Float.parseFloat(alarmArray.get(p).toString())));
                    labels.add("");*/
                    makeGraph(entries, labels, p, paramArray.get(p).toString(), alarmArray.get(p).toString(), unitArray.get(p).toString());
                    System.out.println();
                }
              /*  SessionManager sessionManager=new SessionManager(getActivity());
                sessionManager.setFlagForHistory(0);*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), "Out of memory", Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            //Displaying the snackbar using the show method()
            snackbar.show();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
    }

    @Override
    public void onNothingSelected() {

    }

    //public void setUser
}

