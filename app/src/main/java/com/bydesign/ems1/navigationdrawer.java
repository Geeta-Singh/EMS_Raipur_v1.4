package com.bydesign.ems1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bydesign.ems1.Fragments.DataConditionFragment;
import com.bydesign.ems1.Fragments.DeviceConditionFragment;
import com.bydesign.ems1.Fragments.FilterFragment;
import com.bydesign.ems1.Fragments.LangaugeFragment;
import com.bydesign.ems1.Fragments.MapFragment;
import com.bydesign.ems1.Fragments.TabTestingWithoutPageview;
import com.bydesign.ems1.model.Token;
import com.bydesign.ems1.services.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;


public class navigationdrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Toolbar toolbar = null;
    SharedPreferences sharedPreferences;


    NavigationView navigationView = null;
    private PendingIntent pendingIntent;

    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Runtime.getRuntime().gc();
        System.gc();
        System.gc();

        System.out.print("\n language of app" + Locale.getDefault().getLanguage());
        //  loadLocale();
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_navigationdrawer);

        /* *******************notification start*********************************************/
        Intent alarmIntent = new Intent(this, AlarmService.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 300000;//120000;//60000
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        /* *******************notification end *********************************************/

        sharedPreferences = this.getSharedPreferences("EMS", Context.MODE_PRIVATE);
        DataConditionFragment main = new DataConditionFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, main);
        ft.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_dataCondition);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
       /* View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        headerView.findViewById(R.id.navigation_header_text);*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    DrawerLayout drawer;

    public void onDestroy() {
        super.onDestroy(); // Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        System.gc();
        System.gc();
    }

    @Override
    public void onBackPressed() {
        drawer.closeDrawer(Gravity.LEFT);
        Runtime.getRuntime().gc();
        System.gc();
        System.gc();
        DataConditionFragment main = new DataConditionFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, main);
        ft.commit();
        toolbar.setTitle(R.string.title_activity_dataCondition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigationdrawer, menu);
        // menu.add(0,R.menu.navigationdrawer, 0, "Add").setIcon(android.R.drawable.ic_dialog_email);
        return true;
    }

    public String logoutSession() {
        String status = "";

        InputStream inputStream = null;
        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            String url = sharedPreferences.getString("url", null);
            //made POST request to the given URL
            System.out.print("\nurl in navigation " + url);
            Token newToken = new Token();
            String token = newToken.getToken();
            String URL = newToken.getUrl();
            token = sharedPreferences.getString("token", null);
            System.out.print(" url " + URL);
            HttpPost httpPost = new HttpPost(url + "/logout");//http://220.227.124.134:8080/smartcity/gassensor
            // sharedPreferences = this.getSharedPreferences("EMS", Context.MODE_PRIVATE);
            // String token=sharedPreferences.getString("token",null);
            System.out.print(" logout tokrn b " + token + URL + "\n" + httpPost);
            String json = "";
            //JsonArray
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token);

            //converted JSONObject to JSON to String
            json = jsonObject2.toString();
            System.out.print("logout json " + jsonObject2);
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
            return convertInputStreamToString(inputStream);
        } catch (Exception e) {
            Log.d("IN UPDATE EXCEPTION ", "");
            e.printStackTrace();
            return "{}";

        }

        //  return status;
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

    /****************************************
     * lacolezation
     ***************************/
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private Locale myLocale;

    public void loadLocale() {
        //String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("EMS", Activity.MODE_PRIVATE);
        String language = prefs.getString("Language", "");
        changeLang(language);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        System.out.print("\n  loCAL langugae inside navigation drawer" + myLocale);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        String lang1 = Locale.getDefault().getLanguage();
        System.out.print("\n langugae inside navigation drawer" + lang1);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //  getApplicationContext().dismiss();
        //   updateTexts();
    }

    public void saveLocale(String lang) {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.setLanguage(lang);
    }

    /***********************************************************************************/
    class HttpForLogout extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return logoutSession();
        }

        protected void onPostExecute(String status) {
            System.out.println("#@$Logout api called" + status);
            try {
                JSONObject jsonObject=new JSONObject(status);
                String responce=jsonObject.getString("status");
                if(responce.equalsIgnoreCase("true")){
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.logoutUser();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(100);
                    notificationManager.cancel(101);
                    notificationManager.cancelAll();
                    Intent i = new Intent(navigationdrawer.this, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    manager.cancel(pendingIntent);
                    finish();
                    System.gc();
                    System.gc();

                }
                else{
                    Snackbar snackbar;
                    snackbar = Snackbar.make(getCurrentFocus(), R.string.server, Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68)); //Displaying the snackbar using the show method()
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar snackbar;
                snackbar = Snackbar.make(getCurrentFocus(), R.string.server, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68)); //Displaying the snackbar using the show method()
                snackbar.show();
            }
        }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
         automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml.*/

        int id = item.getItemId();
        if (id == R.id.filter) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            FilterFragment main = new FilterFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.filter);
        }

        return super.onOptionsItemSelected(item);
    }

    FloatingActionButton fabButton;
   /* @Override
    public void onDrawerSlide(View drawerView, float offset){
        fabButton.setAlpha(offset);
    }*/

   /* @Override
    public void onDestroy() {
        finish();
        *//*bitmapImage = null;
        scaledBitmap = null;*//*
        super.onDestroy();
        Runtime.getRuntime().gc();
        System.gc();
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String lang = "en";
        if (id == R.id.nav_camera) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            DataConditionFragment main = new DataConditionFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_dataCondition);

        } /*else if (id == R.id.nav_device) {
            DeviceDeatailsFragment main=new DeviceDeatailsFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Device Details");

        }*/ else if (id == R.id.lang) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            LangaugeFragment main = new LangaugeFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_language);


        } /*else if (id == R.id.nav_7) {
            DataExceedanceReportFragment main = new DataExceedanceReportFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle("Data exceedance report");

        }*/ else if (id == R.id.nav_5) {
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setDevicehis("nodevice");
            sessionManager.setFlagForHistory(-1);
            TabTestingWithoutPageview tabTestingWithoutPageview=new TabTestingWithoutPageview();
            tabTestingWithoutPageview.setTempForCheck(0);
            // SessionManager sessionManagerp = new SessionManager(getActivity());
            sessionManager.setTableContaint("nodata");
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            // HistoricalTab main=new HistoricalTab();
            TabTestingWithoutPageview main = new TabTestingWithoutPageview();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_history);


        } else if (id == R.id.radio_hindi) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            lang = "hi";
            changeLang(lang);
           /* overridePendingTransition(0, 0);
            Intent intent = getIntent();*/
            // finish();
            setContentView(R.layout.activity_navigationdrawer);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.title_activity_dataCondition);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            DataConditionFragment main = new DataConditionFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_dataCondition);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            // loadLocale();
            /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


            overridePendingTransition(0, 0);
            // st
            startActivity(intent);*/
            // this.onCreate();
            //  invalidateOptionsMenu();
           /*// setContentView(R.layout.activity_navigationdrawer);
            NavigationView navigationView = (NavigationView)  findViewById(R.id.nav_view);
            navigationView.notifyAll();*/
            // drawer.notifyDataSetInvalidated();
            /*finish();
            this.recreate();*/
            //  onRestart();
        } else if (id == R.id.radio_english) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            lang = "en";
            changeLang(lang);
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            setContentView(R.layout.activity_navigationdrawer);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(R.string.title_activity_dataCondition);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //  finish();
           /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);


            overridePendingTransition(0, 0);
           // st
            startActivity(intent);*/
            DataConditionFragment main = new DataConditionFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_dataCondition);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            //  loadLocale();
        } /*else if (id == R.id.nav_6) {
            AvgTabFragment main=new AvgTabFragment();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("Average Data");

        }*/ else if (id == R.id.nav_8) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            DeviceConditionFragment main = new DeviceConditionFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_analyser);


        } else if (id == R.id.nav_main) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            MapFragment main = new MapFragment();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, main);
            ft.commit();
            toolbar.setTitle(R.string.title_activity_maps);

        } /*else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download");
            intent.setDataAndType(uri, "text/csv");
            startActivity(Intent.createChooser(intent, "Open folder"));

        *///}
        else if (id == R.id.logout) {
            Runtime.getRuntime().gc();
            System.gc();
            System.gc();
            if (isConnected()) {

                SessionManager sessionManager = new SessionManager(getApplicationContext());
                new HttpForLogout().execute();
                /*sessionManager.logoutUser();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                notificationManager.cancel(101);
                notificationManager.cancelAll();
                Intent i = new Intent(this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                manager.cancel(pendingIntent);
                finish();

                System.gc();
                System.gc();
                //  finish();
                return true;*/
            } else {
                Toast.makeText(getApplicationContext(), R.string.internet, Toast.LENGTH_LONG).show();
            }
        }
        /*else if (id == R.id.nav_gallery) {
            AboutApp main=new AboutApp();
            android.support.v4.app.FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,main);
            ft.commit();
            toolbar.setTitle("About App");


        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*while changing langugage*/

}
