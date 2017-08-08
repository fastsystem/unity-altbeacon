using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AltBeaconTestGUI : MonoBehaviour {

    AltBeaconClient beaconClient;
    List<AltBeaconNotifyEventArgs> beaconList;

	// Use this for initialization
	void Start () {
        beaconList = new List<AltBeaconNotifyEventArgs>();
        beaconClient = FindObjectOfType<AltBeaconClient>();
        if (beaconClient != null)
        {
            beaconClient.Notify += BeaconClient_Notify;
        }
	}

    // Update is called once per frame
    private void OnDestroy()
    {
        if (beaconClient != null)
        {
            beaconClient.Notify -= BeaconClient_Notify;
        }
    }

    private void BeaconClient_Notify(object sender, AltBeaconNotifyEventArgs e)
    {
        beaconList.Add(e);
    }

    private void OnGUI()
    {
        GUI.color = Color.black;

        GUI.Label(new Rect(0, 0, Screen.width, 35), "<size=30>スキャン数 : " + beaconList.Count + "</size>");

        int startPos = Math.Max(0, beaconList.Count - 20);
        for (int i = startPos; i < beaconList.Count; i++)
        {
            string str = string.Format("<size=30>[{0}] {1}</size>", i+1, beaconList[i]);
            GUI.Label(new Rect(0, 35 * (i + 1 - startPos), Screen.width, 35), str);
        }
    }
}
