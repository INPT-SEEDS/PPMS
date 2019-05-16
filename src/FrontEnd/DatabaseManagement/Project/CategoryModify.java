package FrontEnd.DatabaseManagement.Project;

import BackEnd.ProjectType.ProjectType;
import BackEnd.ProjectType.ProjectTypeQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.Optional;


public class CategoryModify extends Pane
{
    static double scalex = Login.scalex;
    static double scaley = Login.scaley;

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("F04040");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    public CategoryModify(ProjectInterface parent, int id)
    {
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(150*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        ProjectType projectType= ProjectTypeQueries.getProjectTypeById(id);

        Label refLB=JavaFX.NewLabel("Reference",lightBlue,1,18,10,10);
        TextField refField=JavaFX.NewTextField(18,200,10,50);
        refField.setText(projectType.getRef());
        Label libLB=JavaFX.NewLabel("Libellé",lightBlue,1,18,250,10);
        TextField libField=JavaFX.NewTextField(18,300,250,50);
        libField.setText(projectType.getLabel());

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,110);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,110);

        getChildren().addAll(refLB,refField,libLB,libField,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {

            String ref=refField.getText();
            String label=libField.getText();

            if(!label.equals(projectType.getLabel()) || !ref.equals(projectType.getRef()))
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attention");
                alert.setContentText("Voulez-vous enregistrer ces modifications ?");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (ButtonType.OK == result.get())
                {
                    ProjectTypeQueries.update(id,ref,label);
                }
            }
            parent.refreshTable2();
            parent.resetSelection();
            this.setStyle("-fx-background-color: #f3f3f3;");
            getChildren().clear();
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
        });
    }

}
