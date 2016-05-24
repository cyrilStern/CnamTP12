package fr.cyrilstern.cnam.cnamtp12;

import android.content.Context;
import android.net.LocalServerSocket;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;

public class Main2ActivityDNS extends AppCompatActivity {
    private TextView tvServiceInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity_dns);
        init();
    }

    private void init(){
        tvServiceInfo = (TextView) findViewById(R.id.textViewServiceinfo);
    }
    private String SERVICE_NAME = "Deptinfo";
    private String SERVICE_TYPE = "_http._tcp";
    private ServerSocket mServerSocket;
    private int mLocalPort;
    private int sdk = android.os.Build.VERSION.SDK_INT;

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void startServiceDerverDNS_SD(){

        NsdManager nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        try {
            mServerSocket = new ServerSocket(0);
        } catch (IOException e) {
            tvServiceInfo.setText("NSD Service Error:\n" + e.toString());
            e.printStackTrace();
        }
        mLocalPort = mServerSocket.getLocalPort();
        if(sdk< Build.VERSION.SDK_INT)
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(mLocalPort);


    }
}
