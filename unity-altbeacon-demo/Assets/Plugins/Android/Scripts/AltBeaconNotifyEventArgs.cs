using System;

public class AltBeaconNotifyEventArgs : EventArgs
{
    public DateTime Time;

    public string UUID;

    public Int32 Major;

    public Int32 Minor;

    public Int32 TxPower;

    public Int32 RSSI;

    public string BluetoothName;

    public string BluetoothAddress;

    public override string ToString()
    {
        return string.Format(
                    "{0} uuid:{1} major:{2} minor:{3} power:{4} rssi:{5}",
                    Time.ToString("HH:mm:ss.fff"),
                    UUID,
                    Major,
                    Minor,
                    TxPower,
                    RSSI
        );
    }
}
