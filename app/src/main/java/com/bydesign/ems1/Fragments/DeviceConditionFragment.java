package com.bydesign.ems1.Fragments;

/*
* Created by Parikshit Sharma
*
* */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.ems1.R;
import com.bydesign.ems1.navigationdrawer;
import com.bydesign.ems1.services.SessionManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;

//import com.github.mikephil.charting.formatter.ValueFormatter;

//import com.github.mikephil.charting.formatter.ValueFormatter;

/*
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;
*/

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceConditionFragment extends Fragment {
    //Variables

    TextView TotalDevices;
    TextView FaultyDevices;
    TextView WorkingDevices;
    TextView MaintenanceModeDevices;
    TextView NoInfoDevices;
    TextView Calibration;
    TableLayout tl;
    TableRow tr;
    ProgressDialog pdialog;
    private RelativeLayout mainLayout;
    private PieChart mChart;
    Timer autoUpdate;
    static String token;
    String url;
    HttpAsyncTask BackgroundLoopTask;
    CardView cv1, cv2, cv3, cv4, cv5, cv6, cv7;
    // we're going to display pie chart for conditions of all active de
    private PendingIntent pendingIntent;

    private AlarmManager manager;
    //***************************************************
    private Random mRandom = new Random();
    private int mCounter;
    private int mMaxRepeat = 10;
    private Handler mHandler;
    private Runnable mRunnable;
    private int mInterval = 1400;
    private int mStartColor = Color.WHITE;
    private int mEndColor = 0;

    //  **************************************************
    /*
    Need to append data received from server

  */
    public DeviceConditionFragment() {
        // Required empty public constructor
    }

    Intent alarmIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_device, container, false);
    /*     alarmIntent = new Intent(getActivity(), com.bydesign.ems1.AlarmService.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);*/
        return v;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    SharedPreferences sharedPreferences;
    static NotificationManager notificationManager;
    String lang;

    public void onStart() {
        super.onStart();

        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());

        mHandler = new Handler();

        if (isConnected()) {


        } else {

            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        //-------------------------------------------------------------------

        tl = (TableLayout) getView().findViewById(R.id.maintable);
        TotalDevices = (TextView) getView().findViewById(R.id.total);
        WorkingDevices = (TextView) getView().findViewById(R.id.working);
        MaintenanceModeDevices = (TextView) getView().findViewById(R.id.maintenance);
        FaultyDevices = (TextView) getView().findViewById(R.id.faulty);
        Calibration = (TextView) getView().findViewById(R.id.calibration);
        NoInfoDevices = (TextView) getView().findViewById(R.id.noinfo);


        cv1 = (CardView) getView().findViewById(R.id.card_view1);
        cv1.setVisibility(View.INVISIBLE);
        cv2 = (CardView) getView().findViewById(R.id.card_view2);
        cv2.setVisibility(View.INVISIBLE);
        cv3 = (CardView) getView().findViewById(R.id.card_view3);
        cv3.setVisibility(View.INVISIBLE);
        cv4 = (CardView) getView().findViewById(R.id.card_view4);
        cv4.setVisibility(View.INVISIBLE);
        cv5 = (CardView) getView().findViewById(R.id.card_view5);
        cv5.setVisibility(View.INVISIBLE);
        cv6 = (CardView) getView().findViewById(R.id.card_view6);
        cv6.setVisibility(View.INVISIBLE);
        cv7 = (CardView) getView().findViewById(R.id.card_view7);
        cv7.setVisibility(View.INVISIBLE);

        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);
        lang = Locale.getDefault().getLanguage();
        url = sharedPreferences.getString("url", null);
        System.out.print("inside on start of device condition");

        if (isConnected()) {
            if (lang.equalsIgnoreCase("hi")) {
                pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
            } else
                pdialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
          //  pdialog.setCancelable(true);
            new HttpAsyncTask().execute();
           /* BackgroundLoopTask.execute();
            BackgroundLoopTask.cancel(true);
*/
            //  new HttpAsyncTask().execute();
        } else {

            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), R.string.internet, Snackbar.LENGTH_LONG);
            snackbar.show();

        }

        context = getActivity();
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        //*********************making pie chart*******************************

        mainLayout = (RelativeLayout) getView().findViewById(R.id.mainLayout);
        // mChart = new PieChart(getActivity());
        mChart = (PieChart) getActivity().findViewById(R.id.chart1);
        // add pie chart to main layout
        // mainLayout.addView(mChart);
        mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));


        // configure pie chart
        mChart.setUsePercentValues(false);
        //mChart.setDescription("");
        mChart.getDescription().setEnabled(false);
        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        //  mChart.setHoleColorTransparent(false);
        // mChart.setH
        mChart.setHoleRadius(55f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        // enable rotation of the chart by touch
        mChart.setRotationAngle(10);
        mChart.setRotationEnabled(true);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            List<String> mAnimals; //= new ArrayList<String>();

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // display msg when value selected
                try {
                    String string = new String();
                    if (e == null)
                        return;

                    else if (h.getX() == 3) {
                        mAnimals = new ArrayList<String>();
                        for (int i = 0; i < No_Info.length(); i++) {

                            string += "\n" + "STATE:  " + No_Info.getJSONObject(i).getString("sid") + "  DEVICE NAME: " + No_Info.getJSONObject(i).getString("dname");
                            // System.out.println(No_Info.getJSONObject(i).getString("sid") + "  " + No_Info.getJSONObject(i).getString("did"));

                            mAnimals.add(No_Info.getJSONObject(i).getString("dname"));//+"("+No_Info.getJSONObject(i).getString("did")+")"
                        }

                        //  Toast.makeText(getActivity(),string, Toast.LENGTH_SHORT).show();
                        dialigBox();

                    } else if (h.getX() == 0) {
                        mAnimals = new ArrayList<String>();
                        for (int i = 0; i < Faulty.length(); i++) {

                            string += "\n" + "STATE:  " + Faulty.getJSONObject(i).getString("sid") + "  DEVICE NAME:  " + Faulty.getJSONObject(i).getString("dname");
                            //System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));
                            //  mAnimals = new ArrayList<String>();
                            mAnimals.add(Faulty.getJSONObject(i).getString("dname"));
                        }
                        dialigBox();// Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    } else if (h.getX() == 1) {
                        mAnimals = new ArrayList<String>();
                        for (int i = 0; i < Working.length(); i++) {

                            string += "\n" + "STATE:  " + Working.getJSONObject(i).getString("sid") + "  DEVICE NAME:  " + Working.getJSONObject(i).getString("dname");
                            // System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));

                            mAnimals.add(Working.getJSONObject(i).getString("dname"));
                        }
                        dialigBox();
                        // Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    } else if (h.getX() == 4) {
                        mAnimals = new ArrayList<String>();
                        for (int i = 0; i < CalibrationMode.length(); i++) {

                            string += "\n" + "STATE:  " + CalibrationMode.getJSONObject(i).getString("sid") + "  DEVICE NAME:  " + CalibrationMode.getJSONObject(i).getString("dname");
                            // System.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));

                            mAnimals.add(CalibrationMode.getJSONObject(i).getString("dname"));
                        }
                        dialigBox();//  Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                    } else if (h.getX() == 2) {
                        mAnimals = new ArrayList<String>();
                        for (int i = 0; i < Maintenance.length(); i++) {

                            string += "\n" + "STATE:  " + Maintenance.getJSONObject(i).getString("sid") + "  DEVICE NAME:  " + Maintenance.getJSONObject(i).getString("dname");
                            //.out.println(Faulty.getJSONObject(i).getString("sid") + "  " + Faulty.getJSONObject(i).getString("did"));

                            mAnimals.add(Maintenance.getJSONObject(i).getString("dname"));
                        }
                        // Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                        dialigBox();

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();

                }

            }

            public void dialigBox() {
                // String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry","WebOS","Ubuntu","Windows7","Max OS X"};
                try {

                    final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    dialogBuilder.setTitle("Device IDs").setIcon(R.drawable.details);
                    dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int item) {
                            SessionManager sessionManager = new SessionManager(getActivity());
                            sessionManager.addSessionForCurrent(Animals[item].toString());
                            System.out.println("SELECTED DEVICE ID :" + Animals[item].toString());
                            TabFragment main = new TabFragment();
                            navigationdrawer nv = new navigationdrawer();
                            nv.toolbar.setTitle(R.string.title_activity_current);
                            android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.frame_container, main);
                            ft.commit();
                            mChart.highlightValues(null);
                            String selectedText = Animals[item].toString();  //Selected item in listview
                        }
                    });


                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mChart.highlightValues(null);
                        }
                    });
                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = dialogBuilder.create();
                    //Show the dialog
                    alertDialogObject.show();

                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }


            @Override
            public void onNothingSelected() {

            }
        });

        // add data


        // customize legends
        Legend l = mChart.getLegend();
        l.setEnabled(false);


    }


    /*************************
     * Save As Pdf
     *********************************/

    JSONArray Faulty;
    JSONArray No_Info;
    JSONArray Working;
    JSONArray CalibrationMode;
    JSONArray Maintenance;
    private String[] xData;
    private float[] yData;


    private void addData(String data) throws JSONException {
        JSONObject job = new JSONObject(data);

        Faulty = job.getJSONArray("faList");
        No_Info = job.getJSONArray("no_infoList");
        Working = job.getJSONArray("noList");
        Maintenance = job.getJSONArray("mmList");
        CalibrationMode = job.getJSONArray("cmList");
        yData = new float[]{Float.parseFloat(job.getString("fa")), Float.parseFloat(job.getString("no")), Float.parseFloat(job.getString("mm")), Float.parseFloat(job.getString("no_info")), Float.parseFloat(job.getString("cm"))};
        //xData = new String[]{"Faulty", "Working","Maintenance", "No Info", "Calibration"};
        xData = new String[]{"", "", "", "", ""};
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        ArrayList<String> xVals = new ArrayList<String>();


        for (int i = 0; i < yData.length; i++) {

            yVals1.add(new PieEntry(yData[i], i));
            xVals.add(xData[i]);


        }


        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(Color.argb(255, 194, 22, 13));
        colors.add(Color.argb(255, 0, 230, 118));
        colors.add(Color.argb(255, 233, 92, 90));
        colors.add(Color.argb(255, 99, 104, 99));
        colors.add(Color.argb(255, 255, 170, 0));


        // colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData dataV;
        for (int i = 0; i < xVals.size(); i++) {
            System.out.println("\n XVALs   " + xVals.get(i) + "\n" + " Yvals   " + yVals1.get(i));
        }
        dataV = new PieData(dataSet);
        dataV.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                DecimalFormat mf = new DecimalFormat();
                return mf.format(v);
            }

            /*@Override
            public String getFormattedValue(float value) {
                DecimalFormat mf = new DecimalFormat();
                return mf.format(value);
            }*/
        });

        dataV.setValueTextSize(11f);
        dataV.setValueTextColor(Color.WHITE);

        mChart.setData(dataV);
        System.out.print("\n data is " + dataV);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }


    /**************************************************************************/

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i = 0;
        String result;

        @Override
        protected String doInBackground(String... urls) {
            result = deviceCondition(url + "/devicecondition");
            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("device condtion :" + status);

            try {
                // Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                    Toast.makeText(getActivity(), "Error : Few parameters are missing", Toast.LENGTH_LONG).show();
                    pdialog.dismiss();
                } else if (status.equalsIgnoreCase("Invalid")) {
                    pdialog.dismiss();
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getView(), R.string.invalidUser, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                    snackbar.show();
                    SessionManager sessionManager = new SessionManager(getActivity());
                    //  new HttpForLogout().execute();
                    sessionManager.logoutUser();
                } else if (status.equalsIgnoreCase("[]")) {
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(), R.string.Nodata, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                    pdialog.dismiss();
                } else {
                    addDataTable(status);
                    addData(status);
                    addListItemToSession(status);
                    pdialog.dismiss();
                }
                //  Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String deviceCondition(String url) {
        System.out.println("Inside Device Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        try {

            System.out.println("tokenntokenn in navigation bar: :" + token);
            System.out.println("tokenntokenn in navigation bar: :" + token + url);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String sid = "All", did = "All", org = "All";
            String details;
            String json = "";
            details = sharedPreferences.getString("deviceDetails", "nodata");
            System.out.print(" \n@#$ sedice details" + details);

            try {
                if (!details.equalsIgnoreCase("nodata")) {
                    StringTokenizer st = new StringTokenizer(details, ",");

                    int i = 0;
                    while (st.hasMoreTokens()) {
                        //String h=st.nextToken();
                        if (i == 0)
                            sid = st.nextToken();
                        if (i == 1)
                            org = st.nextToken();
                        if (i == 2)
                            did = st.nextToken();
                        i++;
                        System.out.print(" \n sid " + sid + "       did  " + did + "        org" + org);
                    }
                }

            } catch (NullPointerException e) {
                e.printStackTrace();

            }
            System.out.print(" \n sid " + sid + "       did  " + did + "        org" + org);

            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);
            jsonObject2.accumulate("sid", sid);
            jsonObject2.accumulate("did", did);
            jsonObject2.accumulate("orgid", org);
            jsonObject2.accumulate("devtype", "CEMS");


            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("json " + jsonObject2);

            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            System.out.println("F22222" + inputStream);
            result = convertInputStreamToString(inputStream);
        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();
            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), R.string.server, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            snackbar.show();
            pdialog.dismiss();
        } catch (Exception e) {
            pdialog.dismiss();
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), R.string.server, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            snackbar.show();
        }


        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    @Override
    public void onDestroy() {
        /*getActivity().finish();*/
        /*bitmapImage = null;
        scaledBitmap = null;*/
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }


    public void addDataTable(String data) {
        try {
            System.out.print("Add data in table");
            JSONObject job = new JSONObject(data);
            JSONArray JOB1 = job.getJSONArray("noList");
            SessionDevice = "";
            for (int i = 0; i < JOB1.length(); i++) {
                JSONObject obj = JOB1.getJSONObject(i);
                // String tp=obj.getString("ts");
                String device = obj.getString("devid");
                SessionDevice = device + "," + SessionDevice;
                System.out.print("sssss  " + SessionDevice);
            }

            //Session_Management(SessionDevice);
            System.out.print("inside add data od device and no list " + JOB1);
            // JSONArray jsonArray = new JSONArray(job);


            TotalDevices.setText("" + job.getString("total") + " ");
            FaultyDevices.setText("" + job.getString("fa") + " ");
            WorkingDevices.setText("" + job.getString("no") + " ");
            MaintenanceModeDevices.setText("" + job.getString("mm") + " ");
            NoInfoDevices.setText("" + job.getString("no_info") + " ");
            Calibration.setText("" + job.getString("cm") + " ");

            cv1.setVisibility(View.VISIBLE);
            cv2.setVisibility(View.VISIBLE);
            cv3.setVisibility(View.VISIBLE);
            cv4.setVisibility(View.VISIBLE);
            cv5.setVisibility(View.VISIBLE);
            cv6.setVisibility(View.VISIBLE);
            cv7.setVisibility(View.VISIBLE);

            cv1.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));
            cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv3.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv4.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv5.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_out));

            cv6.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_up_in));

            cv7.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_in));
            pdialog.dismiss();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            pdialog.dismiss();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            pdialog.dismiss();
            System.out.print("index out of bound");
        } catch (NullPointerException e) {
            e.printStackTrace();
            pdialog.dismiss();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            pdialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            pdialog.dismiss();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), R.string.Nodata, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            //Displaying the snackbar using the show method()
            snackbar.show();
        }
    }

    static Context context;


    SessionManager sessionManager;
    String SessionDevice = "", SessionDevid = "";

    public void Session_ManagementforMap(String map) {
        //Session Manager
        SessionManager sessionManager1 = new SessionManager(getActivity());
        sessionManager1.addSessionForMap(map);
    }

    public void Session_Management(String device) {
        //Session Manager
        sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionData(device);
        // sessionManager.addDeviceDetail("All,All,All");
    }

    private void addListItemToSession(String status) {

        try {
            SessionDevice = "";
            SessionDevid = "";
            //   System.out.println("inside device@@@@ condition" +status);
            JSONObject job = new JSONObject(status);
            JSONArray JOB1 = job.getJSONArray("noList");
            String orgList = job.getString("orglist");
            Session_ManagementforMap(orgList);
            // System.out.p

            //  System.out.println("working id"+JOB1+"\nmap sata"+orgList);
            for (int i = 0; i < JOB1.length(); i++) {
                JSONObject obj = JOB1.getJSONObject(i);
                // String tp=obj.getString("ts");
                String device = obj.getString("dname");
                String devid = obj.getString("devid");
                if (i == 0) {
                    SessionDevid = devid;
                    SessionDevice = device;
                } else {
                    SessionDevice = device + "," + SessionDevice;
                    SessionDevid = devid + "," + SessionDevid;
                }
                //  System.out.print("sssss  "+SessionDevice);
            }
//no_infoList

            JSONArray JOB2 = job.getJSONArray("no_infoList");
            //   System.out.println("working id"+JOB2);
            for (int i = 0; i < JOB2.length(); i++) {
                JSONObject obj = JOB2.getJSONObject(i);


                String device = obj.getString("dname");//+":"+obj.getString("dname");
                String devid = obj.getString("devid");

                SessionDevice = device + "," + SessionDevice;
                SessionDevid = devid + "," + SessionDevid;
                //  System.out.print("no_info device   "+device);
            }

            JSONArray JOB3 = job.getJSONArray("faList");
            //    System.out.println("working id"+JOB3);
            for (int i = 0; i < JOB3.length(); i++) {
                JSONObject obj = JOB3.getJSONObject(i);

                String device = obj.getString("dname");
                String devid = obj.getString("devid");

                SessionDevice = device + "," + SessionDevice;
                SessionDevid = devid + "," + SessionDevid;

                //   System.out.print("sssss  "+SessionDevice);
            }

            JSONArray JOB4 = job.getJSONArray("cmList");
            System.out.println("working id" + JOB4);
            for (int i = 0; i < JOB4.length(); i++) {
                JSONObject obj = JOB4.getJSONObject(i);


                String device = obj.getString("dname");
                String devid = obj.getString("devid");

                SessionDevice = device + "," + SessionDevice;
                SessionDevid = devid + "," + SessionDevid;
                //  System.out.print("sssss  "+SessionDevice);
            }

            JSONArray JOB5 = job.getJSONArray("mmList");
            //  System.out.println("working id"+JOB5);
            for (int i = 0; i < JOB5.length(); i++) {
                JSONObject obj = JOB5.getJSONObject(i);


                String device = obj.getString("dname");
                String devid = obj.getString("devid");

                SessionDevice = device + "," + SessionDevice;
                SessionDevid = devid + "," + SessionDevid;

                //   System.out.print("sssss  "+SessionDevice);
            }
            Session_Management(SessionDevice);
            SessionManager sessionManager1 = new SessionManager(getActivity());
            sessionManager1.addSessionDevid(SessionDevid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

