
using System;

public class AltBeaconDataConvertor
{
    public static DateTime ToDatetime(string[] block, int idx)
    {
        if (idx < block.Length)
        {
            // Java側のベース時間が 1970/1/1 なのでこれにミリ秒を加算する
            DateTime b = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
            b = b.AddMilliseconds(long.Parse(block[idx]));
            b = b.ToLocalTime();
            return b;
        }
        else
        {
            return DateTime.MinValue;
        }
    }

    public static string ToString(string[] block, int idx)
    {
        string str = (idx < block.Length) ? block[idx] : "";
        if (str == "null")
            return "";
        else
            return str;
    }

    public static Int32 ToInt32(string[] block, int idx)
    {
        if (idx < block.Length)
            return Int32.Parse(block[idx]);
        else
            return 0;
    }

    public static SByte ToSByte(string[] block, int idx)
    {
        if (idx < block.Length)
            return SByte.Parse(block[idx]);
        else
            return 0;
    }
}