package BackEnd;

import java.sql.Timestamp;

public class Utility
{
    public static String getDatetime()
    {
        String date=new Timestamp(60*1000*(System.currentTimeMillis()/(1000*60))).toString();
        return date.substring(0,date.length()-5);
    }
}
