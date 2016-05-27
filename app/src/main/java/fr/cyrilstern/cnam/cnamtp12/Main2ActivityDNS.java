package fr.cyrilstern.cnam.cnamtp12;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.LocalServerSocket;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main2ActivityDNS extends AppCompatActivity  implements NsdManager.RegistrationListener, NsdManager.DiscoveryListener{
    private TextView tvServiceInfo;
    private NsdServiceInfo serviceInfo;
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
    private NsdManager nsdManager;
    public void startServiceDerverDNS_SD(){
        
        Intent serviceSendMessage = new Intent();
        serviceSendMessage.setAction("Start_SERVER_DSN");
        startService(serviceSendMessage);
        nsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        try {
            mServerSocket = new ServerSocket(0); //socket
        } catch (IOException e) {
            tvServiceInfo.setText("NSD Service Error:\n" + e.toString());
            e.printStackTrace();
        }
        mLocalPort = mServerSocket.getLocalPort();  //register port find in local variable to give to the client whaen resolution
        if(sdk>= Build.VERSION_CODES.KITKAT){
        serviceInfo = new NsdServiceInfo();  //service provide to ecveery one
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(mLocalPort);
        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, this);
        }



    }


    ////////////////////////////////
    ////////////////////////////////
    ///////   SERVER  /////////////
    //////////////////////////////

    public void onUnregisterServiceD(){

            if(nsdManager!=null) nsdManager.unregisterService(this);

    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {

    }


    @Override
    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onServiceRegistered(final NsdServiceInfo serviceInforetour) {
        serviceInfo.setServiceName(serviceInforetour.getServiceName());   //on recupere le name du service ou cas ou il change
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(serviceInforetour.getHost(),serviceInforetour.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            //bundle.get();
        }
    };


    ///////////////////////////////////////////
    //////////////////////////////////////////
    /////////  CLIENT//////////////////////////
    /////////////////////////////////////////

    private NsdManager nsdManagerClient;
    private Intent intent;
    public void startDiscoring(){
        nsdManagerClient = (NsdManager) getSystemService(Context.NSD_SERVICE);
        nsdManagerClient.discoverServices(SERVICE_TYPE,NsdManager.PROTOCOL_DNS_SD,this);
        Messenger messager = new Messenger(handler);
        intent = new Intent();
        intent.setClass(this,ServiceSendMessage.class);
        intent.putExtra("messager", messager);
        startService(intent);
    }

    public void stopDiscovering(){
        if( intent != null) {
            intent = new Intent(this,ServiceSendMessage.class);
            stopService(intent);
        }
        if(nsdManagerClient != null) nsdManagerClient.stopServiceDiscovery(this);
    }
    public void sedMessage(){

    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {

    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {

    }

    @Override
    public void onDiscoveryStarted(String serviceType) {

    }

    @Override
    public void onDiscoveryStopped(String serviceType) {

    }
    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {

        serviceInfo.getServiceName();
        serviceInfo.getServiceType();

        NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {

        }
        public void onServiceResolved(NsdServiceInfo serviceInfo) {

            serviceInfo.getServiceName();
            serviceInfo.getPort();
            serviceInfo.getHost().getHostAddress();

        }
    };
    nsdManagerClient.resolveService(serviceInfo, mResolveListener);
}

}
