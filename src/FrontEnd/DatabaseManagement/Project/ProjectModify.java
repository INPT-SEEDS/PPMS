package FrontEnd.DatabaseManagement.Project;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.Project.Project;
import BackEnd.Project.ProjectQueries;
import BackEnd.ProjectType.ProjectTypeQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.Optional;


public class ProjectModify extends ScrollPane
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


    public ProjectModify(ProjectInterface parent, int idProject)
    {
        Project project= ProjectQueries.getProjectById(idProject);

        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.setPrefWidth(590*scalex);
        this.setPrefHeight(225*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Pane Content=new Pane();

        Label libLB=JavaFX.NewLabel("Libellé",lightBlue,1,20,10,10);
        TextField libField=JavaFX.NewTextField(18,200,10,35);
        libField.setText(project.getLabel());

        Label porLB=JavaFX.NewLabel("Portefeuille",lightBlue,1,20,250,10);
        ComboBox porBox=JavaFX.NewComboBox(PortfolioQueries.getPortfoliosRef(),250,250,35);
        porBox.getSelectionModel().select(project.getIdPortfolio());

        Label typeLB=JavaFX.NewLabel("Type",lightBlue,1,20,10,75);
        ComboBox typeBox=JavaFX.NewComboBox(ProjectTypeQueries.getProjectTypesRef(),200,10,100);
        typeBox.getSelectionModel().select(project.getIdType());

        Content.getChildren().addAll(libLB,libField,porLB,porBox,typeLB,typeBox);

        int y=110;

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,75+y);
        Button cancel=JavaFX.NewButton("Annuler",red,18,465,75+y);
        Content.getChildren().addAll(confirm,cancel);
        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            String label=libField.getText();
            int idPor=porBox.getSelectionModel().getSelectedIndex();
            int idType=typeBox.getSelectionModel().getSelectedIndex();
            if(!label.equals(project.getLabel()) || idType!=project.getIdType() || idPor!=project.getIdPortfolio())
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Attention");
                alert.setContentText("Voulez-vous enregistrer ces modifications ?");
                alert.setHeaderText(null);
                if(idPor!=project.getIdPortfolio())
                {
                    alert.setContentText("Changer le portefeuille réinitialisera tout les évaluations du projet, Voulez-vous enregistrer ces modifications ?");
                }
                Optional<ButtonType> result = alert.showAndWait();
                if (ButtonType.OK == result.get())
                {
                    ProjectQueries.resetProject(idProject);
                    ProjectQueries.updateProject(idProject,label,idPor,idType);
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

        setContent(Content);
    }

}