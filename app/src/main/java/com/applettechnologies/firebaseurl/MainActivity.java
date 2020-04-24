package com.applettechnologies.firebaseurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.applettechnologies.firebaseurl.Common.Config;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog dialog;

    private BroadcastReceiver mRegistrationBroadcastReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

        mRegistrationBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.STR_PUSH))
                    {
                    String message = intent.getStringExtra(Config.STR_MESSAGE);
                    showNotification("Vishvjeet", message);
                }
            }

        };

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super
        super.onNewIntent(intent);
        dialog = new ProgressDialog(this);
        if(intent.getStringExtra(Config.STR_KEY) != null)
        {
            dialog.show();
            dialog.setMessage("Please wait...");
            webView.loadUrl(intent.getStringExtra(Config.STR_KEY));
        }
    }

    private void showNotification(String title, String message){
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        intent.putExtra(Config.STR_KEY,message);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReciever);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReciever, new IntentFilter("registrationComplete"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReciever, new IntentFilter(Config.STR_PUSH));
    }
}