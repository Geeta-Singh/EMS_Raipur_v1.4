package com.bydesign.ems1.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.ems1.R;
import com.bydesign.ems1.services.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
/*
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
*/

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TableLayout tl, tg,th;
    TableRow tr;
    Spinner spinner;
    String Selecteddevice;
    TextView Date;
    TextView Time;
    TextView Parameter,A_PPM;
    CardView cv,cv2;
    String PDFString,notification,file;
    private RelativeLayout mainLayout;
    LinearLayout l1;
    long table_date;
    String tablegraph="table";
    private Handler handler;
    public CurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //return inflater.inflate(R.layout.fragment_current, container, false);
        View view= inflater.inflate(R.layout.fragment_current, container, false);
      //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return  view;
    }
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity(). getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    FloatingActionButton save,share;

    public void onPause(){

        super.onPause();

       // pdialog.dismiss();
    }
    public void onResume(){
        super.onResume();
       // pdialog.dismiss();
    }
//String lang;
    public void onStart() {
        super.onStart();
        Runtime.getRuntime().gc();
        System.gc();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
       /* navigationdrawer nv=new navigationdrawer();
        if(nv.toolbar.getTitle()!="Latest Data")
        nv.toolbar.setTitle("Latest Data");*/
        //getActionBar().setTitle("Your Title");
      //  SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        notification="";
        save=(FloatingActionButton)getView().findViewById(R.id.savePdf);
        share=(FloatingActionButton)getView().findViewById(R.id.share);
        save.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);

        TextView devicename=(TextView)getView().findViewById(R.id.devicename);
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());

        tl = (TableLayout) getView().findViewById(R.id.maintable);
        th = (TableLayout) getView().findViewById(R.id.maintableH);
        dm=(TextView)getView().findViewById(R.id.dmMag);
        msg=(TextView)getView().findViewById(R.id.dee);
        PDFString=null;
        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);
        Devices = sharedPreferences.getString("device", null);
        Devids= sharedPreferences.getString("devid","nodevice");
     //   lang=sharedPreferences.getString("Language","");
     //   spinner = (Spinner)getView(). findViewById(R.id.spinner);
        cv=(CardView)getView().findViewById(R.id.card_view1);
        cv.setVisibility(View.INVISIBLE);
        cv2=(CardView)getView().findViewById(R.id.card_view2);
        cv2.setVisibility(View.INVISIBLE);
        // Spinner click listener
      //  spinner.setOnItemSelectedListener(this);
//
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories=getDevice(Devices);



       // System.out.print("devid list"+ devidList);
        //categories.add("bpcl_mahul");

        // Creating adapter for spinner
      /*  ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);*/
        //   gauge.setVisibility(View.INVISIBLE);
        /*tl = (TableLayout) getView().findViewById(R.id.maintable);
         th = (TableLayout) getView().findViewById(R.id.maintableH);*/
       // l1=(LinearLayout)getView().findViewById(R.id.l1);
        tl.removeAllViews();

        th.removeAllViews();

        tablegraph=sharedPreferences.getString("tablegraphcurrent", null);

        devicename.setText(sharedPreferences.getString("devices",null));
        if (isConnected()) {
            tl.removeAllViews();

            th.removeAllViews();

            HttpAsyncTask asyncTask= new HttpAsyncTask();
            language=  Locale.getDefault().getLanguage();
            //  Toast.makeText(getActivity(),language,Toast.LENGTH_LONG).show();

            // if(sharedPreferences.getInt("tableflag", 0)==0)
            if(language.equalsIgnoreCase("hi")){
                pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
            }
            else
                pdialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
               pdialog.setCancelable(true);
            // else{ }
            asyncTask.execute();
        }
        else{

            Snackbar snackbar;
            snackbar = Snackbar.make(getView(),R.string.internet, Snackbar.LENGTH_LONG);
            snackbar.show();

        }
      //  System.out.println("@Table and graphe data " + tablegraph + " \n   *  " );
        if(  sharedPreferences.getString("devices", "nodevice").equalsIgnoreCase("nodevice")){
            System.out.println("@   no data 5678888*&&^^ & * * ");
        }
        else {
            System.out.println("hu lalalalalalalalalal back from graph  ");
            try {
                /*SessionManager   sessionManagerp = new SessionManager(getActivity());
                sessionManagerp.setTableContaintcurrent(null);*/
               /* System.out.println("FECTCHED DEVICE ID  :"+sharedPreferences.getString("devices", null));
                ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition = myAdap.getPosition(sharedPreferences.getString("devices",null));
                spinner.setSelection(spinnerPosition);*/
             //   System.out.print("spinner " + spinner.getSelectedItem() + " pos " + spinnerPosition);
                if(language.equalsIgnoreCase("hi")){
                    msg.setText("उपकरण के लिए नवीनतम डेटा : " +sharedPreferences.getString("devices",null));
                }else
                msg.setText("LATEST DATA FOR DEVICE: " + sharedPreferences.getString("devices",null));
            }catch (NullPointerException n){
                n.printStackTrace();
            }
        }

     /***************************************/

/**************************************************/
       // createPDF = (Button)getView().findViewById(R.id.button1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*openFolder();// */ createPDF();
            }
        });
        share=(FloatingActionButton)getView().findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
              new  HttpAsyncTaskShareFile().execute();  //shareFile();
            }
        });
    }
    private Button createPDF;

    public Boolean getIsLoggedOut() {
        return isLoggedOut;
    }

    public void setIsLoggedOut(Boolean isLoggedOut) {
        this.isLoggedOut = isLoggedOut;
    }

    Boolean isLoggedOut=false;

//***************************** NOTIFIcation********************************


    int devidSpinner;
    String language;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
      //  devidSpinner=position;
        Selecteddevice=parent.getItemAtPosition(position).toString();
    /*  //  System.out.println(item + "  sekectedgfdfgd  "+Selecteddevice+"  dev pos "+devidSpinner);
        if (isConnected()) {
            tl.removeAllViews();

            th.removeAllViews();

                HttpAsyncTask asyncTask= new HttpAsyncTask();
           language=  Locale.getDefault().getLanguage();
          //  Toast.makeText(getActivity(),language,Toast.LENGTH_LONG).show();

               // if(sharedPreferences.getInt("tableflag", 0)==0)
           *//* if(language=="hi"){
                pdialog = ProgressDialog.show(getActivity(), "", "कृपया प्रतीक्षा करें", true);
            }
            else
                pdialog = ProgressDialog.show(getActivity(), "", "Please wait...", true);
               pdialog.setCancelable(true);*//*
               // else{ }
                asyncTask.execute();
        }
        else{

            Snackbar snackbar;
            snackbar = Snackbar.make(getView(),R.string.internet, Snackbar.LENGTH_LONG);
            snackbar.show();

        }*/
    }


    TextView dm;
    TextView msg;
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    ProgressDialog pdialog=null;
    //****************************
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/
            return CurrentDAta();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {

            System.out.println("current data  :" + status);
          try{
              if(status.equalsIgnoreCase("Error : Few parameters are missing")){
              // Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
                  Snackbar snackbar;
                  //Initializing snackbar using Snacbar.make() method
                  snackbar = Snackbar.make(getView(), ""+status, Snackbar.LENGTH_LONG);
                  View snackBarView = snackbar.getView();
                  snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                  //Displaying the snackbar using the show method()
                  snackbar.show();
                 pdialog.dismiss();
            }
              else if(status.equalsIgnoreCase(null)){
                  MapFragment main=new MapFragment();
                  android.support.v4.app.FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                  ft.replace(R.id.frame_container, main);
                  ft.commit();
                  Snackbar snackbar;
                  //Initializing snackbar using Snacbar.make() method
                  snackbar = Snackbar.make(getView(),R.string.internet, Snackbar.LENGTH_LONG);
                  //Displaying the snackbar using the show method()
                  snackbar.show(); pdialog.dismiss();
              }else if(status.equalsIgnoreCase("invalid")){
                  //pdialog.dismiss();
                  Snackbar snackbar;
                  snackbar = Snackbar.make(getView(),R.string.invalidUser, Snackbar.LENGTH_LONG);
                  View snackBarView = snackbar.getView();
                  snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                  snackbar.show();
                  SessionManager sessionManager = new SessionManager(getActivity());
                 // new HttpForLogout().execute();
                  sessionManager.logoutUser();
                  pdialog.dismiss();
              }else if(status.equalsIgnoreCase("[]")){
                  tl.removeAllViews();

                  th.removeAllViews();
                  msg.setText(R.string.latestdata + " : " + sharedPreferences.getString("devices", null));
                  /*SessionManager   sessionManagerp = new SessionManager(getActivity());
                  sessionManagerp.setTableContaintcurrent(status);*/

                  Snackbar snackbar;
                  snackbar = Snackbar.make(getView(),R.string.Nodata, Snackbar.LENGTH_LONG);
                  View snackBarView = snackbar.getView();
                  snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68)); //Displaying the snackbar using the show method()
                  snackbar.show();
                  pdialog.dismiss();
              }
            else {

             //   Toast.makeText(getActivity(),""+status,Toast.LENGTH_LONG).show();
                   JSONArray jsonObject = new JSONArray(status);
            //  JSONArray msg = (JSONArray) jsonObject.get("values");
              //JSONArray param=(JSONArray) jsonObject.get("values");
           //   System.out.print(msg);

              // l1=(LinearLayout)getView().findViewById(R.id.l1);
                  tl.removeAllViews();

                  th.removeAllViews();

                  if(language.equalsIgnoreCase("hi")){
                      msg.setText("उपकरण के लिए नवीनतम डेटा: " + sharedPreferences.getString("devices",null));
                  }else
                      msg.setText("LATEST DATA FOR DEVICE : " +sharedPreferences.getString("devices",null));
                  Session_Management(sharedPreferences.getString("devices",null));
                  SessionManager   sessionManagerp = new SessionManager(getActivity());
                  sessionManagerp.setTableContaintcurrent(status);
                  sessionManagerp.addFlage(0);
                  addHeaders(jsonObject);
                  addDataTable(jsonObject);
              //    pdialog.dismiss();
                  cv.setVisibility(View.VISIBLE);
                  cv2.setVisibility(View.VISIBLE);
                //  cv.setVisibility(View.VISIBLE);
                  cv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_in));
                  cv2.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_down_out));
                  save.setVisibility(View.VISIBLE);
                 share.setVisibility(View.VISIBLE);
                  pdialog.dismiss();
            }
           //   Toast.makeText(getActivity(),"return value is : "+status,Toast.LENGTH_LONG).show();
        } catch (Exception e)
          {
              pdialog.dismiss();
            //  pdialog.dismiss();
              e.printStackTrace();
          }catch (OutOfMemoryError e){
              pdialog.dismiss();
              getActivity().finish();
              e.printStackTrace();

          }
        }
    }
    String token;
    String Devices,Devids;
    public String CurrentDAta() {

        Log.d("UPDATE METHOD CALLED", "HI I M IN UPDATE");
        InputStream inputStream = null;
        String res = "";
        String result = null;
        try {

            token = sharedPreferences.getString("token", null);
            String url=sharedPreferences.getString("url", null);
          //  Devices = sharedPreferences.getString("device",null);
            System.out.println("token :"+token);//"http://220.227.124.134:8070/smartcity/gassensor/getdata"
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url+"/getdata");

            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);

            List<String> categories = new ArrayList<String>();
            categories=getDevice(Devices);

            devidSpinner=categories.indexOf(sharedPreferences.getString("devices",null));

            List<String> devidList;
            devidList=getDevice(Devids);

         //   System.out.print("devid list");
            /*for(int i=0;i<devidList.size();i++){
                System.out.println(devidList.get(i));
            }*/

       //     System.out.print(" api caling "+ devidSpinner);
            jsonObject2.accumulate("devid", devidList.get(devidSpinner));
            jsonObject2.accumulate("count", "10");

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
          //  httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString("rat#1:rat".getBytes(), Base64.DEFAULT));

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            result = convertInputStreamToString(inputStream);

        } catch (Exception e) {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
            Snackbar snackbar;
            snackbar = Snackbar.make(getView(), R.string.server, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            snackbar.show();
            pdialog.dismiss();
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

    public void Session_forSharePDf(String fileName){
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionFileName(fileName);
    }

    public void Session_Management(String device)
    {
        //Session Manager
        SessionManager sessionManager = new SessionManager(getActivity());
        sessionManager.addSessionForCurrent(device);
    }
    public List<String> getDevice(String device ){
        List<String> categories = new ArrayList<String>();
        try {

            StringTokenizer st = new StringTokenizer(device, ",");
            int t = 0;
            while (st.hasMoreTokens()) {
               // StringTokenizer st = new StringTokenizer(device, ",");
                categories.add(st.nextToken());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Snackbar snackbar;

            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),R.string.devicenotfound, Snackbar.LENGTH_LONG);

            //Displaying the snackbar using the show method()
            snackbar.show();
          //  Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return  categories;
    }




    public ArrayList<String>stringToken(String string){
        ArrayList<String>paramt =new ArrayList<>();//String[param.length()];
        StringTokenizer st = new StringTokenizer(string, ",[,]\"");
        int t=0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
           // System.out.println("\n" + token);
            paramt.add(token);

               /* S_Status = token;
                break;*/
        }
        return paramt;
    }

    //*****************************
    public void addHeaders(JSONArray jsonObject){


        try {
            if(jsonObject.length()==0){
                tl.removeAllViews();

                th.removeAllViews();

                Snackbar snackbar;

                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),R.string.Nodata, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                //Displaying the snackbar using the show method()
                snackbar.show();// Toast.makeText(getActivity(),"Data is not available ",Toast.LENGTH_LONG).show();
            }

                JSONObject object = jsonObject.getJSONObject(0);



                JSONArray param = (JSONArray) object.get("param");
                JSONArray unit = (JSONArray) object.get("units");

                ArrayList<String> paramt = stringToken(param.toString());
                ArrayList<String> units = stringToken(unit.toString());

                tr = new TableRow(getActivity());
                // tr.setBackgroundColor(Color.argb(196,18,86,136));
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                /** Creating a TextView to add to the row **/
                TextView Date = new TextView(getActivity());
                Date.setText("Date");
                Date.setTextColor(Color.WHITE);
                Date.setTextSize(13);
                Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Date.setPadding(5, 5, 5, 0);
                Date.setBackgroundResource(R.drawable.th);//cell.png);
                tr.addView(Date);  // Adding textView to tablerow.


                TextView Time = new TextView(getActivity());
                Time.setText("Time");
                Time.setBackgroundResource(R.drawable.th);
                Time.setTextSize(13);
                Time.setTextColor(Color.WHITE);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Time.setPadding(5, 5, 5, 0);
                tr.addView(Time);  // Adding textView to tablerow.


                for (int row = 0; row < paramt.size(); row++) {
                    TextView Co = new TextView(getActivity());
                  //  System.out.print(" param :" + paramt.get(row) + "\n" + units.get(row));
                    String uString=units.get(row).toString();
                    Co.setText("   " + param.get(row) + "\n" + "   " + unit.get(row));
                  //  System.out.println("  hi geeta unit test " + paramt.get(row) + "\n" + "   " + units.get(row).toString()+"geeta / singh"+uString+""+unit.get(row));
                    Co.setTextColor(Color.WHITE);
                    Co.setTextSize(13);
                    Co.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Co.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    Co.setPadding(5, 5, 5, 0);
                    Co.setBackgroundResource(R.drawable.th);
                    tr.addView(Co);
                    PDFString=PDFString +"   " + paramt.get(row) + "(" +  units.get(row)+")";

                    // Co. removeView();// Adding textView to tablerow.
                }

                A_PPM = new TextView(getActivity());
                A_PPM.setText("  Qcode ");
                A_PPM.setTextColor(Color.WHITE);
                A_PPM.setTextSize(13);
                A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_PPM.setPadding(5, 5, 5, 0);
                A_PPM.setBackgroundResource(R.drawable.th);
                tr.addView(A_PPM);


                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            String msg = object.get("msg").toString();
            if(language.equalsIgnoreCase("hi")){
                dm.setText("निदानकारी संदेश : " + msg);
            }else
                dm.setText("Diagnostic Message : " + msg);

            file=object.get("file").toString();
          //  PDFString=tr.toString()+"\n";
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (OutOfMemoryError e){
            getActivity().finish();
            e.printStackTrace();

        }
    }

    int flag=0;
    public void addDataTable(JSONArray jsonObject)
    {
        System.out.print("Add data in table");
        try{
            JSONObject object=jsonObject.getJSONObject(0);
            if(jsonObject.length()==0){
                tl.removeAllViews();
                th.removeAllViews();
                Snackbar snackbar;
                //Initializing snackbar using Snacbar.make() method
                snackbar = Snackbar.make(getView(),R.string.Nodata, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                //Displaying the snackbar using the show method()
                snackbar.show();
            }

           else if(object.length()==0){
                tl.removeAllViews();
                th.removeAllViews();
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(),R.string.Nodata, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                snackbar.show();
            }
            else {
                JSONArray value = (JSONArray) object.get("values");

                if (value.length() == 0) {
                    tl.removeAllViews();
                    th.removeAllViews();
                    Snackbar snackbar;
                    //Initializing snackbar using Snacbar.make() method
                    snackbar = Snackbar.make(getView(),R.string.Nodata, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                    //Displaying the snackbar using the show method()
                    snackbar.show();
                }
                JSONArray param = (JSONArray) object.get("param");
                JSONArray alarm = (JSONArray) object.get("alarm");
                ArrayList<String> paramt = stringToken(param.toString());
                flag=0;notification="";
                for (int row = 0; row < value.length(); row++) {
                    /** Create a TableRow dynamically **/
                    tr = new TableRow(getActivity());
                    tr.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    // System.out.print(" \nts:" + tp + "   " + i);
                    //********************* time and date*******************

                    JSONObject obj = value.getJSONObject(row);
                    String tp = obj.getString("ts");
                    Long ds = Long.parseLong(tp);
                    //System.out.print("string is "+tp+"    long is "+ds);
                    String[] dt = new String[2];
                    String timestamp = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(ds));
                    int i = 0;
                    StringTokenizer st = new StringTokenizer(timestamp, " ");
                    while (st.hasMoreTokens()) {
                        dt[i] = (st.nextToken());
                        //    System.out.print("\ndate time... : "+dt[i]);
                        i++;
                    }
                    //System.out.print("................inside while of history......................\n");

                    //******************************************
                    /** Creating a TextView to add to the row **/
                    Date = new TextView(getActivity());
                    Date.setText("  " + dt[0] + " ");
                    Date.setTextColor(Color.argb(255, 48, 59, 68));
                    Date.setTextSize(11);
                    Date.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Date.setPadding(5, 5, 5, 0);
                    Date.setBackgroundResource(R.drawable.tabler);
                    tr.addView(Date);  // Adding textView to tablerow.


                    /** Creating another textview **/
                    Time = new TextView(getActivity());
                    Time.setText(" " + dt[1] + " ");
                    Time.setTextColor(Color.argb(255, 48, 59, 68));
                    Time.setTextSize(11);
                    //  Time.setBackgroundResource(R.drawable.cell);
                    Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Time.setPadding(5, 5, 5, 0);

                    Time.setBackgroundResource(R.drawable.tabler);
                    Time.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    tr.addView(Time); // Adding textView to tablerow.



                    for (int col = 0; col < paramt.size(); col++) {
                        String val="";
                        try{
                             val = obj.getString(paramt.get(col).toUpperCase());
                        }catch (JSONException e){
                            try{
                                val = obj.getString(paramt.get(col).toLowerCase());
                            }catch (JSONException e1){
                                try{
                                    val = obj.getString(paramt.get(col));
                                }catch (JSONException e2){
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


                        /** Creating a TextView to add to the row **/
                        Parameter = new TextView(getActivity());

                        if(val.isEmpty())
                        {
                            val="0";
                        }
                        Parameter.setText("   " + val);
                       // else
                        //  System.out.println(" alarm  " + Double.parseDouble(alarm.get(col).toString()) + "            " + Double.parseDouble(val));
                        if (Double.parseDouble(val) > Double.parseDouble(alarm.get(col).toString())) {
                            Parameter.setTextColor(Color.RED);

                        } else {
                            Parameter.setTextColor(Color.argb(255, 48, 59, 68));
                        }
                        Parameter.setTextSize(11);
                        Parameter.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                        Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        Parameter.setPadding(5, 5, 5, 0);
                        Parameter.setBackgroundResource(R.drawable.tabler);
                        tr.addView(Parameter);

                    }

                    A_PPM = new TextView(getActivity());
                    A_PPM.setText("   " + obj.getString("QCode"));
                    A_PPM.setTextColor(Color.argb(255, 48, 59, 68));
                    A_PPM.setTextSize(11);
                    A_PPM.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    A_PPM.setPadding(5, 5, 5, 0);
                    A_PPM.setBackgroundResource(R.drawable.tabler);
                    tr.addView(A_PPM);

                    // Add the TableRow to the TableLayout
                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                   // PDFString=tr.toString()+"\n";
                }
              //  if(flag==1)
             //   createNotification(getView(),notification,Selecteddevice);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch  (IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.print("index out of bound");
        } catch (JSONException e) {

            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(),R.string.Nodata, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            //Displaying the snackbar using the show method()
            snackbar.show();
            e.printStackTrace();
        }catch (OutOfMemoryError e){
            getActivity().finish();
            e.printStackTrace();

        }
    }
    int shareFlag=0;
    public void createPDF  ()   {
       try {
           shareFlag = 1;
        /*https_val = https://220.227.124.134:8081;

Se
            inUrl =  https_val+"/smartcity/gassensor/getpdf?token="+token+"&file="+pdfname;*/
           Session_forSharePDf(file);
           String url = sharedPreferences.getString("url", null);
           String inURL = url + "/getpdf?token=" + token + "&file=" + URLEncoder.encode(file, "UTF-8");
           //   String  inURL="http://220.227.124.134:8070/smartcity/gassensor/getpdf?token="+token+"&file="+file;
           System.out.println(" file name " + file + "   url " + inURL);
           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
           startActivity(browserIntent);

           Snackbar snackbar;
           //Initializing snackbar using Snacbar.make() method
           snackbar = Snackbar.make(getView(), "PDF downloaded", Snackbar.LENGTH_LONG);
           View snackBarView = snackbar.getView();
           snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
           //Displaying the snackbar using the show method()
           snackbar.show(); //
       }catch (UnsupportedEncodingException e){
           e.printStackTrace();
       }
    }
    @Override
    public void onDestroy() {
       /* getActivity().finish();*/
        /*bitmapImage = null;
        scaledBitmap = null;*/
        super.onDestroy();

        Runtime.getRuntime().gc();
        System.gc();
        /*Runtime.getRuntime().gc();
        System.gc();*/
    }
    private String shareFile() {
        try {
            String fileName;
            fileName=sharedPreferences.getString("filename", "nofile");
            System .out.print("\n  in current file name"+fileName);
            if(fileName.equalsIgnoreCase("nofile")){
                return "notg";
            }else{
//
                System .out.print("\n  in current file name" + fileName);
                //Toast.makeText(getActivity(),"Last downloaded file "+fileName,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/"+fileName ;
                File dir = new File(path);
                //dir.mkdir();
                Log.d("PDFCreator", "PDF Path: " + path);

                File filet = new File(dir,file);
                intent.setType("message/rfc822");
                /*String path=getActivity().getApplicationContext().getFileStreamPath(file).getPath();
                File filey=new File(path);*/
                Uri uri = Uri.fromFile(dir);
                System.out.print("userghfjdghfhdgs" + uri);
             //   Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, "Share PDF file"));


        }
        } catch (Exception e) {

            Snackbar snackbar;
            snackbar = Snackbar.make(getView(),R.string.error, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            snackbar.show();
            e.printStackTrace();
           // Toast.makeText(getActivity(), "Error: Cannot open or share created PDF report.", Toast.LENGTH_SHORT).show();
            return "fail";
        }
        return "success";

    }

    private class HttpAsyncTaskShareFile extends AsyncTask<String, Void, String> {
        int i=0;

        @Override
        protected String doInBackground(String... urls) {
           /* String result=  Update("https://203.196.179.45:8081/smartcity/gassensor/login");
            System.out.println("result" + result);*/
            return shareFile();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.print(("satus file"+status));
            if(status.equalsIgnoreCase("success")){
             Toast.makeText(getActivity(),R.string.lastPdf+sharedPreferences.getString("filename", null),Toast.LENGTH_LONG).show();
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(),"File shared", Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                snackbar.show(); //
            }
            else if(status.equalsIgnoreCase("notg")){
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(),R.string.Plspdf, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                snackbar.show(); //
            }
            else
            {
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(),R.string.filenotshare, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                snackbar.show(); //
            }
        }
    }
    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Download");
        intent.setDataAndType(uri, "text/csv");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }
}




