package BackEnd;

import javafx.scene.control.TableView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class Utility
{
    public static String getDatetime()
    {
        String date=new Timestamp(60*1000*(System.currentTimeMillis()/(1000*60))).toString();
        return date.substring(0,date.length()-5);
    }

    public static String getSelectedRowColumn(TableView tv, int columnId)
    {
        Object row=tv.getSelectionModel().getSelectedItems().get(0);
        return row.toString().split(",")[columnId].substring(1);
    }

    public static String EncryptPassword(String password)
    {
        String generatedPassword = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
            {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
