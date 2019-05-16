package FrontEnd.DatabaseManagement.Resource;

import BackEnd.Project.ProjectQueries;
import BackEnd.ResToProject.ResToProjectQueries;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.sql.Timestamp;
import java.util.List;

public class AssignToProject extends Pane
{
    private Paint black=Paint.valueOf("000000");

    AssignToProject(int resId, double x, double y, ResourceInterface parent)
    {
        setLayoutX(x);
        setLayoutY(y);

        List<String> projects= ProjectQueries.getProjectsRef();
        Button bar=JavaFX.NewButton("",black,18,110,31,10,125);
        ComboBox projectsBox= JavaFX.NewComboBox(projects,200,15,40);
        TextField numberField= JavaFX.NewTextField(18,100,65,80);
        Button confirm=JavaFX.NewButton("Confirmer",18,60,120);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            int proId=projectsBox.getSelectionModel().getSelectedIndex();
            ResToProjectQueries.resetTable(proId,resId);
            String date=new Timestamp(60*1000*(System.currentTimeMillis()/(1000*60))).toString();
            int quantity=Integer.valueOf(numberField.getText());
            ResToProjectQueries.addToDatabase(proId,resId,date.substring(0,date.length()-5),quantity);
            parent.editing=false;
            this.getChildren().clear();
        });

        getChildren().addAll(bar,projectsBox,numberField,confirm);
    }
}
