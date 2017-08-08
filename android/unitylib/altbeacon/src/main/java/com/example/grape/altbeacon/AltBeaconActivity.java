package com.example.grape.altbeacon;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AltBeaconActivity extends UnityPlayerActivity implements BeaconConsumer  {

    private static final String LOG_TAG = "Android";
    private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private BeaconManager beaconManager;
    private List<AltBeaconClient> clients;

    public  AltBeaconActivity()
    {
        Log.d(LOG_TAG, "AltBeaconActivity");
        beaconManager = null;
        clients = new ArrayList<AltBeaconClient>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "AltBeaconActivity.onCreate");

        // activity_main.xml にUIコンポーネントを配置する
        super.onCreate(savedInstanceState);

        // beaconManagerのインスタンスを取得
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        // beaconManager.setForegroundBetweenScanPeriod(1000);
        // beaconManager.setBackgroundBetweenScanPeriod(1000);
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "AltBeaconActivity.onDestroy");
        super.onDestroy();
        AltBeaconManagerBind(BindType.Unbind);
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "AltBeaconActivity.onPause");
        super.onPause();
        AltBeaconManagerBind(BindType.Unbind);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "AltBeaconActivity.onResume");
        super.onResume();
        AltBeaconManagerBind(BindType.Bind);
    }

    // implements BeaconConsumer
    @Override
    public void onBeaconServiceConnect() {
        Log.d(LOG_TAG, "AltBeaconActivity.onBeaconServiceConnect");

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // 検出したビーコンの情報を全部Logに書き出す
                for(Beacon beacon : beacons) {
                    NotiyIBeacon(beacon);
                }
            }
        });

        try {
            // ビーコン情報の監視を開始
            beaconManager.startRangingBeaconsInRegion(new Region("unique-id-001", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void BindClient(AltBeaconClient client)
    {
        Log.d(LOG_TAG, "AltBeaconActivity.BindClient");
        clients.add(client);
        AltBeaconManagerBind(BindType.Auto);
    }

    public void UnbindClient(AltBeaconClient client)
    {
        Log.d(LOG_TAG, "AltBeaconActivity.UnBindClient");
        clients.remove(client);
        AltBeaconManagerBind(BindType.Auto);
    }

    private boolean AltBeaconBindStatus = false;
    private void AltBeaconManagerBind(BindType type)
    {
        Log.d(LOG_TAG, "AltBeaconActivity.AltBeaconManagerBind type:" + type.toString());

        switch (type)
        {
            case Bind:
                AltBeaconBindStatus = true;
            case Auto:
                if(AltBeaconBindStatus)
                {
                    if (clients.size() == 0) {
                        Log.d(LOG_TAG, "AltBeaconActivity.AltBeaconManagerBind unbind");
                        beaconManager.unbind(this);
                    }
                    else {
                        Log.d(LOG_TAG, "AltBeaconActivity.AltBeaconManagerBind bind");
                        beaconManager.bind(this);
                    }
                }
                break;

            case Unbind:
                Log.d(LOG_TAG, "AltBeaconActivity.AltBeaconManagerBind unbind");
                beaconManager.unbind(this);
                AltBeaconBindStatus = false;
                break;
        }
    }

    private void NotiyIBeacon(Beacon b)
    {
        // "UUID:" + b.getId1()
        // "major:" + b.getId2()
        // "minor:" + b.getId3()
        // "RSSI" + b.getRssi()
        // "Name" + b.getBluetoothName();
        // "Address" + b.getBluetoothAddress();
        String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s", System.currentTimeMillis(), b.getId1(), b.getId2(), b.getId3(), b.getTxPower(), b.getRssi(), b.getBluetoothName(), b.getBluetoothAddress());

        // ログの出力
        Log.i(LOG_TAG, "AltBeaconActivity.NotiyIBeacon " + data);

        // 各クライアントに受診したビーコン情報をブロードキャスト
        {
            for (int i = 0; i < clients.size(); i++)
                clients.get(i).OnNotify(data);
        }
    }

    private enum BindType
    {
        Auto,
        Bind,
        Unbind,
    };
}
