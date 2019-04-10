package FrontEnd.Project;

import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;


public class CategoryModify extends Pane
{

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("F04040");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    public CategoryModify(ProjectInterface parent, int id)
    {
        this.setPrefWidth(575);
        this.setPrefHeight(420);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");


        Label refLB=JavaFX.NewLabel("Reference",lightBlue,1,18,10,10);
        TextField refField=JavaFX.NewTextField(18,200,10,50);
        Label libLB=JavaFX.NewLabel("Libellé",lightBlue,1,18,250,10);
        TextField libField=JavaFX.NewTextField(18,300,250,50);

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,350);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,350);

        getChildren().addAll(refLB,refField,libLB,libField,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            String ref=refField.getText();
            String lib=libField.getText();
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
