using System;
using UnityEngine;

public class AltBeaconClient : MonoBehaviour {

    private AndroidJavaObject altBeaconClient;

    public event EventHandler<AltBeaconErrorEventArgs> Error;

    public event EventHandler<AltBeaconNotifyEventArgs> Notify;

    private void Awake()
    {
        altBeaconClient = new AndroidJavaObject("com.example.grape.altbeacon.AltBeaconClient");
        Init(this.gameObject.name);
    }

    void Start() {
        Bind();
    }

    void Update() {
        // none
    }

    private void OnDestroy()
    {
        Unbind();
    }

    public void Init(string gameObject)
    {
        CallUiThread(() =>
        {
            altBeaconClient.Call("Init", gameObject, "OnNotifyCallback", "OnErrorCallback");
        });
    }

    public void Bind()
    {
        CallUiThread(() =>
        {
            altBeaconClient.Call("Bind");
        });
    }

    public void Unbind()
    {
        CallUiThread(() =>
        {
            altBeaconClient.Call("Unbind");
        });
    }

    private void CallUiThread(Action action)
    {
        // Context(Activity)オブジェクトを取得する
        AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        AndroidJavaObject context = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        context.Call("runOnUiThread", new AndroidJavaRunnable(() => {
            action();
        }));
    }

    public void OnNotifyCallback(string data)
    {
        Debug.Log("AltBeaconClient.OnNotifyCallback data:" + data);
        if (Notify != null)
        {
            AltBeaconNotifyEventArgs args = new AltBeaconNotifyEventArgs();
            string[] block = data.Split(',');
            args.Time = AltBeaconDataConvertor.ToDatetime(block, 0);
            args.UUID = AltBeaconDataConvertor.ToString(block, 1);
            args.Major = AltBeaconDataConvertor.ToInt32(block, 2);
            args.Minor = AltBeaconDataConvertor.ToInt32(block, 3);
            args.TxPower = AltBeaconDataConvertor.ToInt32(block, 4);
            args.RSSI = AltBeaconDataConvertor.ToInt32(block, 5);
            args.BluetoothName = AltBeaconDataConvertor.ToString(block, 6);
            args.BluetoothAddress = AltBeaconDataConvertor.ToString(block, 7);
            Notify(this, args);
        }
    }

    public void OnErrorCallback(string data)
    {
        Debug.Log("AltBeaconClient.OnErrorCallback data:" + data);
        if (Error != null)
        {
            AltBeaconErrorEventArgs args = new AltBeaconErrorEventArgs();
            args.Message = data;
            Error(this, args);
        }
    }
}
