package FrontEnd.User;

import BackEnd.Profile.Profile;
import BackEnd.Profile.ProfileQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;


public class ProfileModify extends Pane
{
    static double scalex = Home.scalex;
    static double scaley = Home.scaley;

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("F04040");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    public ProfileModify(UserInterface parent, int id)
    {
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(420*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Profile profile=ProfileQueries.getProfileById(id);

        Label refLB=JavaFX.NewLabel("Reference",lightBlue,1,18,10,10);
        TextField refField=JavaFX.NewTextField(18,200,10,35);
        refField.setText(profile.getRef());

        Label labelLB=JavaFX.NewLabel("Libelle",lightBlue,1,18,250,10);
        TextField labelField=JavaFX.NewTextField(18,200,250,35);
        labelField.setText(profile.getLabel());

        getChildren().add(JavaFX.NewLabel("Privilège",lightBlue,1,20,10,90));

        CheckBox admin=JavaFX.NewCheckBox("Administrateurs",10,120);
        CheckBox projectManager=JavaFX.NewCheckBox("Gestionnaires de projets",10,180);
        CheckBox portfolioManager=JavaFX.NewCheckBox("Gestionnaires de portefeuilles",10,240);
        CheckBox resourceManager=JavaFX.NewCheckBox("Gestionnaires des ressources",10,300);
        CheckBox outilManager=JavaFX.NewCheckBox("Générateur d’outils",310,120);
        CheckBox criteria=JavaFX.NewCheckBox("Générateur de critères",310,180);
        CheckBox director=JavaFX.NewCheckBox("Directeur",310,240);

        getChildren().addAll(admin,projectManager,portfolioManager,resourceManager,outilManager,criteria,director);


        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,350);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,350);

        getChildren().addAll(refLB,refField,labelLB,labelField,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
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
