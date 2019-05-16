package FrontEnd.DatabaseManagement.Criterion;

import BackEnd.Criterion.Criterion;
import BackEnd.Criterion.CriterionQueries;
import BackEnd.CriterionType.CriterionTypeQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.Optional;


public class CriterionModify extends Pane
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

    private String[] genres={"positif","négatif"};

    public CriterionModify(CriterionInterface parent, int id)
    {
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(260*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Criterion criterion= CriterionQueries.getCriterionById(id);

        Label refLB=JavaFX.NewLabel("Reference",lightBlue,1,18,10,10);
        TextField refField=JavaFX.NewTextField(18,200,10,50);
        refField.setText(criterion.getRef());

        Label libLB=JavaFX.NewLabel("Libellé",lightBlue,1,18,250,10);
        TextField labelField=JavaFX.NewTextField(18,300,250,50);
        labelField.setText(criterion.getLabel());

        Label genreLB=JavaFX.NewLabel("Genre",lightBlue,1,18,10,100);
        ComboBox<String> genreBox=JavaFX.NewComboBox(genres,200,10,140);
        int temp=0;
        if(criterion.getGenre().equals("négatif"))
        {
            temp=1;
        }
        int index=temp;
        genreBox.getSelectionModel().select(index);

        Label typeLB=JavaFX.NewLabel("Type",lightBlue,1,18,250,100);
        ComboBox<String> typeBox=JavaFX.NewComboBox(CriterionTypeQueries.getCriteriaTypeRef(),200,250,140);
        typeBox.getSelectionModel().select(criterion.getIdType());

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,210);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,210);

        getChildren().addAll(genreLB,genreBox,typeLB,typeBox);
        getChildren().addAll(refLB,refField,libLB,labelField,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            String ref=refField.getText();
            String label=labelField.getText();
            int genreIndex=genreBox.getSelectionModel().getSelectedIndex();
            int typeIndex=typeBox.getSelectionModel().getSelectedIndex();

            if(!label.equals(criterion.getLabel()) || !ref.equals(criterion.getRef()) || genreIndex!=index || typeIndex!=criterion.getIdType())
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attention");
                alert.setContentText("Voulez-vous enregistrer ces modifications ?");
                alert.setHeaderText(null);
                Optional<ButtonType> result = alert.showAndWait();
                if (ButtonType.OK == result.get())
                {
                    CriterionQueries.update(id, ref, label, genres[genreIndex], typeIndex);
                }
            }
            parent.refreshTable();
            this.setStyle("-fx-background-color: #f3f3f3;");
            parent.resetSelection();
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
