package com.bydesign.ems1;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by user on 2/22/2017.
 */
public class AlarmService extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    Context contx;
    JSONArray Faulty;
    JSONArray No_Info;
    JSONArray Working;
    JSONArray CalibrationMode;
    JSONArray Maintenance;
    private String[] xData;
    private float[] yData;
   // static  int flag=0;
    static ArrayList<String> oldStates;
    // int id = 100;
  //  static Notification notification;
    // static Context context;//=getApplicationContext();
   // static NotificationManager notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        // System.out.print("inside alarm manager ");
        sharedPreferences = context.getSharedPreferences("EMS", Context.MODE_PRIVATE);
        contx=context;
        //  Toast.makeText(context,"service running", Toast.LENGTH_LONG).show();
        System.out.println("Notification called fron alarm manager");
        new HttpAsyncTaskForNotificatin().execute();
        // new DeviceConditionFragment.HttpAsyncTask().execute();
        //   Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
    }
    private class HttpAsyncTaskForNotificatin extends AsyncTask<String, Void, String> {
        int i=0;
        String resul="";
        @Override
        protected String doInBackground(String... urls) {
            resul=  dataCondition();
            return resul;

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            System.out.println("data condtion in notification :" + status);

            try {
                if (status.equalsIgnoreCase("Error : Few parameters are missing")) {
                   // Toast.makeText(this, "Error : Few parameters are missing", Toast.LENGTH_LONG).show();
                   // pdialog.dismiss();
                }else if(status.equalsIgnoreCase("Invalid")){

                }else if(status.equalsIgnoreCase("[]")){

                }
                else {
                    checkRedDevice(status);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }catch (OutOfMemoryError e){
                e.printStackTrace();
            }
        }
    }

    public String dataCondition() {
        System.out.println("Inside Data Condition");
        InputStream inputStream = null;
        String res = "";
        String result = null;

        try {
            String token = sharedPreferences.getString("token", null);
            String URL=sharedPreferences.getString("url", null);
            System.out.println("token inside data condition :"+token +URL);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(URL + "/dataCondition");

            String sid = "All",did="All",org="All";
            String details;
            String json = "";
           /* details= sharedPreferences.getString("deviceDetails", null);
            System.out.print(" \n@#$ sedice details"+details);*/

            try{
                details= sharedPreferences.getString("deviceDetails", "null");
                System.out.print(" \n@#$ sedice details"+details);

                if(!details.equalsIgnoreCase("null")) {
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

            }catch (NullPointerException e){
                System.out.println("im in exception of fetching state id dist and org id");
                // pdialog.dismiss();
                e.printStackTrace();
            }
            System.out.print(" distcrit  \n sid "+sid+"       did  "+did+"        org"+org);

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
            // httpPost.setHeader("Content-Length", se.getContentLength()+"");

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            System.out.println("F22222"+inputStream );
            result = convertInputStreamToString(inputStream);
        } catch (ClientProtocolException cpe) {
            System.out.println("First Exception caz of HttpResponese :" + cpe);
            cpe.printStackTrace();} catch (Exception e) {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
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

    static int flag=0;
    static ArrayList<String>OldR,tempR;
    public void checkRedDevice(String data){
        JSONObject job = null;
        ArrayList<String>newR=new ArrayList<String>();
        String notiMsg="";
        try {
            job = new JSONObject(data);
            int r=job.getInt("r");
            Red = job.getJSONArray("rlist");

            System.out.print("no red devices out side if  " + Red + r);
            for(int i=0;i<Red.length();i++)
            {
                // System.out.println("add new R device " + Red.length() + Red.getJSONObject(i).getString("dname"));
                newR.add(Red.getJSONObject(i).getString("dname"));
            }
            tempR=newR;
            //  System.out.println(" new RED "+newR);
            if(r==0){
                //  System.out.print("no red devices "+Red+r);

            }else {
                if (flag == 0) {
                    OldR=tempR;
                    //  System.out.println(" inside flag 0 "+flag);
                    for (int i = 0; i < Red.length(); i++) {
                        //  System.out.println(" inside flag 0 for "+ i);
                        int t = i + 1;
                        notiMsg += "\n " + t + ". " + Red.getJSONObject(i).getString("dname");
                    }
                    callNotification(notiMsg);
                    flag++;
                } else {
                    System.out.println(" inside flag 1 "+flag);
                    newR.removeAll(OldR);
                    System.out.println(" new RED after removing old " + newR);
                    if(newR.size()==0){

                    }else{
                        for (int i = 0; i < newR.size(); i++) {
                            // System.out.println(" inside flag 1 for  "+i);
                            int t = i + 1;
                            notiMsg += "\n " + t + ". " +newR.get(i);
                        }
                        OldR=tempR;
                        //  System.out.println(" OLd RED "+OldR);
                        callNotification(notiMsg);
                    }

                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }catch (OutOfMemoryError e){

            e.printStackTrace();

        }

    }
    JSONArray Red;

    static NotificationManager notificationManager;
    int id = 100;
    static Notification notification;
    static Context context;//=getApplicationContext();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void callNotification(String message) {
        String msgText="";
        System.out.println("Notification");
        notificationManager = (NotificationManager) contx.getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")
        Intent notificationIntent;
        notificationIntent = new Intent(contx,navigationdrawer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(contx, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(contx);
        builder.setAutoCancel(true);
        builder.setTicker("Notification ");
       String lang=  Locale.getDefault().getLanguage();
        if(lang.equalsIgnoreCase("hi")){
            builder.setContentTitle("प्रदुषण मॉनिटर सूचना");
            builder.setContentText("चेतावनी नवीनतम डेटा  ");
            msgText = "चेतावनी : उपकरण डेटा लिमिट के ऊपर \n" + message ;
        }
        else{
            builder.setContentTitle("Pollution Monitor Notification");
            builder.setContentText("Alert Current data ");
            msgText = "Alert: Devices having Data higher then threshold \n" + message ;
        }
        builder.setSmallIcon(R.drawable.ems_notification);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
        builder.build();
        notification = new Notification.BigTextStyle(builder)
                .bigText(msgText).build();
        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify((int)System.currentTimeMillis(), notification);//(int)System.currentTimeMillis()

    }

}
