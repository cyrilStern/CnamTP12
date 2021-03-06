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
    private static final String HOST = "10.0.2.2";
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
    private ServiceServeurCommunication clientinformation;
    private ServerSocket socketServer;
    private int socketServerPort;
    private TextView textViewServiceinfo, ndsServiceStarted, DiscoveringStart, onServiceResolved;
    private String SERVICE_TYPE = "_http._tcp";
    private NsdManager nsdManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            ServiceServeurCommunication serviceServeurCommunication = (ServiceServeurCommunication) bundle.getSerializable("serveurInfo");
            if (bundle.getSerializable("serveurInfo") != null) {
                if (serviceServeurCommunication.getState()) stateserveur = "actif";
                else if (!serviceServeurCommunication.getState()) stateserveur = "inactif";
                if (serviceServeurCommunication != null) {
                    textViewServiceinfo.setText("le service à débuté sur le port : " + serviceServeurCommunication.getPort());
                    ndsServiceStarted.setText("le service est : " + stateserveur);
                    textViewMyRunnableservice.setText("le service de registration est : " + serviceServeurCommunication.getAdress());
                }

            }
            if (bundle.getString("serveurSetSocketMessage") != null) {

                textViewMessageerceivedFromClient.setText("Le message : " + bundle.getString("serveurSetSocketMessage"));

            }
        }
    };
    private Handler handlerClient = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            ServiceServeurCommunication serviceClientCommunication = (ServiceServeurCommunication) bundle.getSerializable("clientInfo");
            DiscoveringStart.setText("le service de decouverte a débuté ");
            socketServerPort = bundle.getInt("clientSocket");
            clientinformation = (ServiceServeurCommunication) bundle.getSerializable("clientInfo");
            Log.i("etouivasy2", String.valueOf(socketServerPort));
            onServiceResolved.setText("le service trouvé et connecté: " + clientinformation.getAdress());

            //  if(serviceClientCommunication.getState())  stateserveur = "actif";
            //  else if(!serviceClientCommunication.getState()) stateserveur = "inactif";
        }
    };
    private NsdManager nsdManagerClient;
    private Intent intentClient;

    private void initView(){
        onServiceResolved = (TextView) findViewById(R.id.onServiceResolved);
        DiscoveringStart = (TextView) findViewById(R.id.DiscoveringStart);
        ndsServiceStarted = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewServiceinfo = (TextView) findViewById(R.id.textViewServiceinfo);
        textViewNdsServiceStart = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewServiceRunning = (TextView) findViewById(R.id.serviceState);
        textViewOnServiceRegistered = (TextView) findViewById(R.id.onServiceRegistered);
        textViewMessageerceivedFromClient = (TextView) findViewById(R.id.messageReceivingFromClient);
        textViewMyRunnableservice = (TextView) findViewById(R.id.myRunnableServiceService);
        textViewNsdServiceStoped = (TextView) findViewById(R.id.ndsServiceStarted);
        textViewOnServiceRegisterd = (TextView) findViewById(R.id.onServiceUnregistered);
        textViewMyRuunnableServiceError = (TextView) findViewById(R.id.myrunnableServiceRunError);
    }


    ////////////////////////////////
    ////////////////////////////////
    ///////   SERVER  /////////////
    //////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity_dns);

        init();
        initView();
    }

    private void init() {

        tvServiceInfo = (TextView) findViewById(R.id.textViewServiceinfo);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startServiceDerverDNS_SD(View view) {
        Messenger messager = new Messenger(handler);
        Intent serviceServeurDSN = new Intent(this, ServerDSNservice.class);
        serviceServeurDSN.setAction("Start_SERVER_DSN");
        serviceServeurDSN.putExtra("message", messager);
        this.startService(serviceServeurDSN);

    }

    public void stopServiceServerDND_SD(View view) {
        Intent intent = new Intent(this, ServiceServeurCommunication.class);
        stopService(intent);
    }

    public void onUnregisterServiceD() {

        if (nsdManager != null) nsdManager.unregisterService(this);

    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {

    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }


    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////  CLIENT ///////////////////////
    /////////////////////////////////////////

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
                    Socket socket = new Socket(serviceInforetour.getHost(), serviceInforetour.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

    }

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

    public void sedMessage(View view){


        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        // Create Socket instance


                        Socket socket = new Socket("192.168.1.12",socketServerPort);
                            // Get output buffer
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream()));
                            // Write output
                        writer.write("Bonjour connection à votre service");
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
