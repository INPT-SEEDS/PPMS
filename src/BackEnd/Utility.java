package BackEnd;

import javafx.scene.control.TableView;

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
}
