package fr.cyrilstern.cnam.cnamtp12;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by sacsn02 on 26/05/2016.
 */
public class ServiceSendMessage extends Service implements NsdManager.RegistrationListener{


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServiceSendMessage(String name) {
        super(name);
        nsdManager  = (NsdManager) this.getSystemService(Context.NSD_SERVICE);                  //getSystemService(getApplicationContext().NSD_SERVICE);
    }
    private NsdManager nsdManager;
    private String SERVICE_NAME = "Deptinfo";
    private String SERVICE_TYPE = "_http._tcp";
    private int mLocalPort;
    private int sdk = android.os.Build.VERSION.SDK_INT;
    private NsdServiceInfo serviceInfo;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
        private ServerSocket mServerSocket;

        try {
            mServerSocket = new ServerSocket(0); //socket
        mLocalPort = mServerSocket.getLocalPort();  //register port find in local variable to give to the client whaen resolution
        if(sdk>= Build.VERSION_CODES.KITKAT){
            serviceInfo = new NsdServiceInfo();  //service provide to every one
            serviceInfo.setServiceName(SERVICE_NAME);
            serviceInfo.setServiceType(SERVICE_TYPE);
            serviceInfo.setPort(mLocalPort);
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, this);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

    }

    @Override
    public void onServiceRegistered(NsdServiceInfo serviceInfo) {

    }

    @Override
    public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

    }
}
