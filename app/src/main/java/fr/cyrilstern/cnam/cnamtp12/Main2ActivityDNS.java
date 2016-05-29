package fr.cyrilstern.cnam.cnamtp12;

import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import fr.cyrilstern.cnam.cnamtp12.javaPojo.ServiceServeurCommunication;

public class Main2ActivityDNS extends AppCompatActivity  implements NsdManager.RegistrationListener, NsdManager.DiscoveryListener{
    private TextView tvServiceInfo;
    private NsdServiceInfo serviceInfo;
    private TextView textViewNdsServiceStart;
    private TextView textViewServiceRunning;
    private TextView textViewOnServiceRegistered;
    private TextView textViewMessageerceivedFromClient;
    private TextView textViewMyRunnableservice;
    private TextView textViewNsdServiceStoped;
    private TextView textViewOnServiceRegisterd;
    private TextView textViewMyRuunnableServiceError;
    private String stateserveur;
    private ServerSocket socketServer;
    private int socketServerPort;



    private void initView(){
        textViewNdsServiceStart = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewServiceRunning = (TextView) findViewById(R.id.serviceState);
        textViewOnServiceRegistered = (TextView) findViewById(R.id.onServiceRegistered);
        textViewMessageerceivedFromClient = (TextView) findViewById(R.id.messageReceivingFromClient);
        textViewMyRunnableservice = (TextView) findViewById(R.id.myRunnableServiceService);
        textViewNsdServiceStoped = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewOnServiceRegisterd = (TextView) findViewById(R.id.onServiceUnregistered);
        textViewMyRuunnableServiceError = (TextView) findViewById(R.id.myrunnableServiceRunError);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity_dns);

        init();
        initView();
    }

    private void init(){

        tvServiceInfo = (TextView) findViewById(R.id.textViewServiceinfo);

    }
    private String SERVICE_TYPE = "_http._tcp";

    @Override
    protected void onResume() {
        super.onResume();

    }
    private NsdManager nsdManager;
    public void startServiceDerverDNS_SD(View view){
        Messenger messager = new Messenger(handler);
        Intent serviceServeurDSN = new Intent(this,ServerDSNservice.class);
        serviceServeurDSN.setAction("Start_SERVER_DSN");
        serviceServeurDSN.putExtra("message",messager);
        this.startService(serviceServeurDSN);

    }
    public void stopServiceServerDND_SD(View view){
        Intent intent = new Intent(this,ServiceServeurCommunication.class);
        stopService(intent);
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
        Log.i("register", (String.valueOf(serviceInforetour.getPort())));
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
            ServiceServeurCommunication serviceServeurCommunication = (ServiceServeurCommunication) bundle.getSerializable("serveurInfo");
            if(serviceServeurCommunication.getState())  stateserveur = "actif";
            else if(!serviceServeurCommunication.getState()) stateserveur = "inactif";
            if(serviceServeurCommunication !=null){
            textViewNdsServiceStart.setText(stateserveur);
            textViewNdsServiceStart.setText("le service à débuté sur le port : " + serviceServeurCommunication.getPort());
            textViewMyRunnableservice.setText(serviceServeurCommunication.getAdress());
            }
        }
    };
    private Handler handlerClient = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            ServiceServeurCommunication serviceClientCommunication = (ServiceServeurCommunication) bundle.getSerializable("clientInfo");
            textViewMessageerceivedFromClient.setText("ouioui vas y " + bundle.getString("serveurSetSocketMessage"));
            socketServerPort = bundle.getInt("serverSocket");
            Log.i("etouivasy2", String.valueOf(socketServerPort));

          //  if(serviceClientCommunication.getState())  stateserveur = "actif";
          //  else if(!serviceClientCommunication.getState()) stateserveur = "inactif";
        }
    };


    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////  CLIENT ///////////////////////
    /////////////////////////////////////////

    private NsdManager nsdManagerClient;
    private Intent intentClient;
    public void startDiscoring(View view){
        Messenger messager = new Messenger(handlerClient);
        intentClient = new Intent();
        intentClient.setClass(this,ClientServiceDSN.class);
        intentClient.putExtra("message2", messager);
        startService(intentClient);
    }

    public void stopDiscovering(View view){
        if( intentClient != null) {
            intentClient = new Intent(this,ServiceServeurCommunication.class);
            stopService(intentClient);
        }
        if(nsdManagerClient != null) nsdManagerClient.stopServiceDiscovery(this);
    }
    private static final String HOST = "10.0.2.2";
    public void sedMessage(View view){


        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        // Create Socket instance
                        // Get input buffer

                            Log.i("etouivasy2", "ecrit à ce moment");
                            Socket socket = new Socket("192.168.1.12",socketServerPort);
                            // Get output buffer
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream()));
                            // Write output
                            writer.write("这是一段来自服务器的问候：Hello沃德！");
                            writer.flush();
                            writer.close();


                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



            }


        }).start();

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
