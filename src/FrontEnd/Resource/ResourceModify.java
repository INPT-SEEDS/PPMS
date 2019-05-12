package FrontEnd.Resource;

import BackEnd.Resource.Resource;
import BackEnd.Resource.ResourceQueries;
import BackEnd.ResourceCategory.ResourceCategoryQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;


public class ResourceModify extends Pane
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

    public ResourceModify(ResourceInterface parent, int id)
    {
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(150*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Resource resource= ResourceQueries.getResourceTypeById(id);

        Label labelLB=JavaFX.NewLabel("Libellé",lightBlue,1,18,10,10);
        TextField labelField=JavaFX.NewTextField(18,200,10,50);
        labelField.setText(resource.getLabel());

        Label typeLB=JavaFX.NewLabel("Type de ressource",lightBlue,1,18,250,10);
        ComboBox<String> typeBox=JavaFX.NewComboBox(ResourceCategoryQueries.getCategoriesRef(),300,250,50);
        //TextField libField=JavaFX.NewTextField(18,300,250,50);

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,110);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,110);

        getChildren().addAll(labelLB,labelField,typeLB,typeBox,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            String label=labelField.getText();
            int idType=typeBox.getSelectionModel().getSelectedIndex();
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
