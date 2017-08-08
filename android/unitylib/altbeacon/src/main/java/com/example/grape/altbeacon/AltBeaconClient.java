package com.example.grape.altbeacon;

import android.util.Log;

import com.unity3d.player.UnityPlayer;

public class AltBeaconClient {

    private static final String LOG_TAG = "Android";
    private String gameObject;
    private String nitfyCallback;
    private String errorCallback;

    public void Init(String gameObject, String nitfyCallback, String errorCallback)
    {
        Log.d(LOG_TAG, "AltBeaconClient.Init");
        this.gameObject = gameObject;
        this.nitfyCallback = nitfyCallback;
        this.errorCallback = errorCallback;
    }

    public void Bind()
    {
        Log.d(LOG_TAG, "AltBeaconClient.Bind");
        AltBeaconActivity act = GetAltBeaconActivity();
        if(act == null) OnError("デフォルト Activity が AltBeaconActivity で初期化されていません。");

        act.BindClient(this);
    }

    public void Unbind()
    {
        Log.d(LOG_TAG, "AltBeaconClient.Unbind");
        AltBeaconActivity act = GetAltBeaconActivity();
        if(act == null) OnError("デフォルト Activity が AltBeaconActivity で初期化されていません。");

        act.UnbindClient(this);
    }

    private AltBeaconActivity GetAltBeaconActivity()
    {
        Log.d(LOG_TAG, "AltBeaconClient.GetAltBeaconActivity");
        if(UnityPlayer.currentActivity instanceof  AltBeaconActivity)
            return (AltBeaconActivity)UnityPlayer.currentActivity;
        else
            return null;
    }

    public void OnNotify(String data)
    {
        Log.i(LOG_TAG, "AltBeaconClient.OnNotify to gameObject=" + gameObject);
        UnityPlayer.UnitySendMessage(gameObject, nitfyCallback, data);
    }

    public void OnError(String errorMessage)
    {
        Log.i(LOG_TAG, "AltBeaconClient.OnError message:" + errorMessage);
        UnityPlayer.UnitySendMessage(gameObject, errorCallback, errorMessage);
    }
}
