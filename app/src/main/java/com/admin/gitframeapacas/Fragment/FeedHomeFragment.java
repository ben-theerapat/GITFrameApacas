package com.admin.gitframeapacas.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.gitframeapacas.R;
import com.admin.gitframeapacas.SQLite.DBUser;
import com.admin.gitframeapacas.Service.SetGasService;
import com.admin.gitframeapacas.Views.GraphGasActivity;
import com.admin.gitframeapacas.Views.RecommendActivity;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static com.admin.gitframeapacas.Views.HomeActivity.MQTTRunning;


/**
 * Created by Admin on 12/11/2559.
 */

public class FeedHomeFragment extends Fragment {


    private static String TAG = "BENFeedHomeFragment";
    private BarChart mBarChart;
    private FloatingActionButtonPlus mActionButtonPlus;
    private CustomGauge gauge;
    private TextView txtAQI;
    private Button mRandomGas;

    public FeedHomeFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        mBarChart = (BarChart) v.findViewById(R.id.barchart);
        mActionButtonPlus = (FloatingActionButtonPlus) v.findViewById(R.id.ActionButtonPlus);
        gauge = (CustomGauge) v.findViewById(R.id.gaugeMaster);
        txtAQI = (TextView) v.findViewById(R.id.txtAQI);
        mRandomGas = (Button) v.findViewById(R.id.btnRandomGas);
        final ConstraintLayout view = (ConstraintLayout) v.findViewById(R.id.fragment_home);
        final ViewGroup parent = (ViewGroup) view.getParent();
        loadData();
        DBUser db = new DBUser(getActivity());
        String user_type = db.getUserType();
        if (user_type.equals("member")) {
            Log.i(TAG, "You are member");


        } else {//user
            Log.i(TAG, "You are user");

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            alertDialogBuilder.setTitle("APARCAS System");
            alertDialogBuilder
                    .setMessage("คุณมีอุปกรณ์เซ็นเซอร์ตรวจจับสภาพอากาศหรือไม่")
                    .setCancelable(false)
                    .setPositiveButton("มี", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Log.i(TAG, "มีเซ็นเซอร์");
                            gauge.setVisibility(View.GONE);
                            txtAQI.setVisibility(View.GONE);
                            dialog.cancel();

                        }
                    })
                    .setNegativeButton("ไม่มี", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Log.i(TAG, "ไม่มีเซ็เนซอร์");
                            dialog.cancel();
                            Snackbar snackbar = Snackbar.make(view, "หากคุณมีเซ็นเซอร์ คุณสามารถเข้าไปเปิดการใช้งานที่ตั้งค่าได้ในภายหลัง", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
        return v;
    }
    private void loadData() {

        Log.i(TAG, "loadData");
        // final int[] random = {40, 60, 110, 210, 400};
        mRandomGas.setOnClickListener(new View.OnClickListener() {
            //    int count = 0;
            @Override
            public void onClick(View view) {
                //MQTTRunning = MQTTRunning == false;

                if (MQTTRunning) {
                    getActivity().stopService(new Intent(getActivity(), SetGasService.class));
                    MQTTRunning = false;
                } else {
                    getActivity().startService(new Intent(getActivity(), SetGasService.class));
                    MQTTRunning = true;
                }




                /*
                if (count > 4) {
                    count = 0;
                }
                int value = random[count];
                gauge.setValue(value);
                txtAQI.setText("AQI: " + value);
                count++;
                if (gauge.getValue() > 0) {
                    gauge.setPointStartColor(Color.parseColor("#91a7ff"));

                }
                if (gauge.getValue() > 50) {
                    gauge.setPointStartColor(Color.parseColor("#42bd41"));
                }
                if (gauge.getValue() > 100) {
                    gauge.setPointStartColor(Color.parseColor("#fff176"));

                }
                if (gauge.getValue() > 200) {
                    gauge.setPointStartColor(Color.parseColor("#ffb74d"));
                }
                if (gauge.getValue() > 300) {
                    gauge.setPointStartColor(Color.parseColor("#f36c60"));
                }*/

            }


        });

        mActionButtonPlus.setPosition(FloatingActionButtonPlus.POS_RIGHT_TOP);
        mActionButtonPlus.clearAnimation();

        mActionButtonPlus.setOnItemClickListener(new FloatingActionButtonPlus.OnItemClickListener() {

            @Override
            public void onItemClick(FabTagLayout tagView, int position) {

                switch (position) {
                    case 0:
                        Toast.makeText(getActivity(), "Tips", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getActivity(), RecommendActivity.class);
                        startActivity(intent2);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "Chart", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), GraphGasActivity.class);
                        startActivity(intent);
                        break;

                }

            }
        });

        mBarChart.addBar(new BarModel("CO", 2f, Color.parseColor("#91a7ff")));
        mBarChart.addBar(new BarModel("NO2", 3f, Color.parseColor("#42bd41")));
        mBarChart.addBar(new BarModel("O3", 4f, Color.parseColor("#fff176")));
        mBarChart.addBar(new BarModel("SO2", 5f, Color.parseColor("#ffb74d")));
        mBarChart.addBar(new BarModel("PM2.5", 3f, Color.parseColor("#f36c60")));
        mBarChart.addBar(new BarModel("Radio", 6f, Color.parseColor("#ba68c8")));
        mBarChart.startAnimation();


    }


}
