package fr.cyrilstern.cnam.cnamtp12;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("android.nfc.actiob.NDEF_DISCOVERED");
        intentFilter1.addCategory("android.intent.category.DEFAULT");
        try {
            intentFilter1.addDataType("text/palin");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.nfc.action.TECH_DISCOVERED");
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.nfc.action.TAG_DISCOVERED");
        intentFilter3.addCategory("android.intent.category.DEFAULT");
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

       private void processIntent(Intent intent) {
           Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
           byte[] tag_id = detectedTag.getId();
           Ndef ndef = Ndef.get(detectedTag);
           int maxSize = ndef.getMaxSize();
           Parcelable[] rawMsgs =
                   intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
           if (rawMsgs != null) {
               NdefMessage[] messages = new NdefMessage[rawMsgs.length];
               for (int i = 0; i < rawMsgs.length; i++) {
                   messages[i] = (NdefMessage) rawMsgs[i];
                   String str = new String(messages[i].getRecords()[0].getPayload());

               }


           }
       }





    private BroadcastReceiver broadcastReceiverNFC = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            intent.getExtras();
        }
    };

}
