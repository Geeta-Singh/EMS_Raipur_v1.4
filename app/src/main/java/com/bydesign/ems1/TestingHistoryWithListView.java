package com.bydesign.ems1;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bydesign.ems1.Fragments.OOM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestingHistoryWithListView extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner2;
    String selectGranu;
    String token;
    SharedPreferences sharedPreferences;
    TableLayout tl, tg, th;
    TableRow tr;

    ImageButton to, from;

    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    int flag = 0, flagForcheck = 0;
    TextView Date;
    TextView Time;
    TextView Parameter, A_PPM, t1, t2, t3;
    private EditText fromText, totext;
    private RelativeLayout mainLayout;
    LinearLayout l1;
    Spinner spinner;
    String Selecteddevice;
    CardView cardGraph;
    String fromd, tod, PDFString;
    String tablegraph = "table";
    String Devices, Devids;
    String devicename;
    FloatingActionButton save, share;
    ProgressDialog pdialog = null;
    int devidSpinner;
    long table_date;
    String lang;
    CardView cv;
    ProgressBar pb;
    TextView waitingText;

    public TestingHistoryWithListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testing_history_with_list_view, container, false);
    }

    public List<String> getDevice(String device) {
        List<String> categories = new ArrayList<String>();
        try {

            StringTokenizer st = new StringTokenizer(device, ",");
            int t = 0;
            while (st.hasMoreTokens()) {
                categories.add(st.nextToken());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Snackbar snackbar;
            //Initializing snackbar using Snacbar.make() method
            snackbar = Snackbar.make(getView(), R.string.noDevice, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
            //Displaying the snackbar using the show method()
            snackbar.show();
            //  Toast.makeText(getActivity(),"Device not found",Toast.LENGTH_LONG).show();
        }
        return categories;
    }


    public void onStart() {
        super.onStart();
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
        sharedPreferences = this.getActivity().getSharedPreferences("EMS", Context.MODE_PRIVATE);

        int historyFlag = sharedPreferences.getInt("Historyflag", -1);
        Devices = sharedPreferences.getString("device", null);
        Devids = sharedPreferences.getString("devid", null);
        lang = Locale.getDefault().getLanguage();
        List<String> categories = new ArrayList<String>();
        categories = getDevice(Devices);

        spinner = (Spinner) getView().findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        cv = (CardView) getView().findViewById(R.id.card_view5);
        cv.setVisibility(View.INVISIBLE);
        devicename = sharedPreferences.getString("hisDevice", "nodevice");
        System.out.println("device inside histor y " + devicename);

        tl = (TableLayout) getView().findViewById(R.id.maintable);
        th = (TableLayout) getView().findViewById(R.id.maintableH);
        System.out.println("history flag deactivated    " + historyFlag);
        tablegraph = sharedPreferences.getString("tablegraphsession", "nodata");

        System.out.println("@Table and graphe data " + tablegraph + " \n   *  " + sharedPreferences.getString("devices", null) + devicename);

        if (tablegraph.equalsIgnoreCase("nodata") && devicename.equalsIgnoreCase("nodevice")) {
            System.out.println("@   no data 5678888*&&^^ & * * ");

            // pdialog.dismiss();
        } else {

            System.out.println("hu lalalalalalalalalal back from graph  ");
            System.out.println("FECTCHED DEVICE ID  :" + devicename);
            ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
            int spinnerPosition = myAdap.getPosition(devicename);
            spinner.setSelection(spinnerPosition);
            System.out.print("spinner " + spinner.getSelectedItem() + " pos " + spinnerPosition);
            fromText = (EditText) getView().findViewById(R.id.editText1);
            fromText.setText(sharedPreferences.getString("shtime", null));
            cv.setVisibility(View.VISIBLE);
             addDataToListView(tablegraph);
        }


    }

    /********************************* dropdown selection methods**********************/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // String item = parent.getItemAtPosition(position).toString();
        devidSpinner = position;
        Selecteddevice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /********************************* dropdown selecion methods ends**********************/
    /****************************** STRING TOKENIZER ******************************************/
    public ArrayList<String> stringToken(String string) {
        ArrayList<String> paramt = new ArrayList<>();//String[param.length()];
        StringTokenizer st = new StringTokenizer(string, ",[,]\"");
        int t = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            // System.out.println("\n" + token);
            paramt.add(token);

               /* S_Status = token;
                break;*/
        }
        return paramt;
    }
    /******************************** STRING TOKENIZER END************************************************/
    /******************************** ADDING DATA INTO LIST VIEW ******************************************/
    public void addDataToListView(String serverResponce){
        try{
            JSONArray jsonObject = new JSONArray(serverResponce);
            JSONObject object = jsonObject.getJSONObject(0);
            JSONArray value = (JSONArray) object.get("values");
            JSONArray param = (JSONArray) object.get("param");
            JSONArray alarm = (JSONArray) object.get("alarm");
            ArrayList<String> paramt = stringToken(param.toString());
            if (value.length() == 0) {
                tl.removeAllViews();
                th.removeAllViews();
                Snackbar snackbar;
                snackbar = Snackbar.make(getView(), R.string.Nodata, Snackbar.LENGTH_LONG);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.argb(255, 48, 59, 68));
                snackbar.show();

            }
            
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
