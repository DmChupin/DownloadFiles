package com.psu.downloadfiles;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;


public class downloadService extends Service {

    private DownloadManager manager;
    private Long downloadID = -1L;
    private ArrayList<Long> downloadIDs = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Служба запущена",
                Toast.LENGTH_SHORT).show();
        startDownload( intent.getStringExtra("url"));

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        downloadIDs.forEach(id -> {
            manager.remove(id);
        });

        unregisterReceiver(onDownloadComplete);
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
    }


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                downloadIDs.remove(downloadID);
                Toast.makeText(downloadService.this, "Скачивание завершено",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void startDownload(String fileURI){
        Uri uri = Uri.parse(fileURI);

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        DownloadManager.Request request = new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(fileURI.substring(fileURI.lastIndexOf("/")))
                        .setVisibleInDownloadsUi (true)
                        .setDescription("Download using downloadManager")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                fileURI.substring(fileURI.lastIndexOf("/")));

        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID = manager.enqueue(request);
        downloadIDs.add(downloadID);
    }


}
