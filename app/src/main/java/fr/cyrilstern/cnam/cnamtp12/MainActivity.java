package fr.cyrilstern.cnam.cnamtp12;

import android.content.Intent;
import android.content.IntentFilter;
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


    }

}
