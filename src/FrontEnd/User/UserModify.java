package FrontEnd.User;

import BackEnd.Profile.ProfileQueries;
import BackEnd.User.User;
import BackEnd.User.UserQueries;
import FrontEnd.Login;
import FrontEnd.Manage;
import Interface.JavaFX;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.Optional;


public class UserModify extends Pane
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


    public UserModify(UserInterface parent,int id)
    {
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(250*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        User user= UserQueries.getUserById(id);

        Label usrLB=JavaFX.NewLabel("Nom d'utilisateur",lightBlue,1,18,10,10);
        TextField usrField=JavaFX.NewTextField(18,200,10,35);
        usrField.setDisable(true);
        usrField.setText(user.getUsername());

        Label fnLB=JavaFX.NewLabel("Nom",lightBlue,1,18,250,10);
        TextField fnField=JavaFX.NewTextField(18,200,250,35);
        fnField.setText(user.getFirstname());

        Label lsLB=JavaFX.NewLabel("Prenom",lightBlue,1,18,10,80);
        TextField lsField=JavaFX.NewTextField(18,200,10,105);
        lsField.setText(user.getLastname());

        Label phLB=JavaFX.NewLabel("Telephone",lightBlue,1,18,250,80);
        TextField phField=JavaFX.NewTextField(18,200,250,105);
        phField.setText(user.getPhone());
        phField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    phField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Label profileLB=JavaFX.NewLabel("Profile",lightBlue,1,18,10,150);
        ComboBox profileField=JavaFX.NewComboBox(ProfileQueries.getProfilesRef(),200,10,172);
        profileField.getSelectionModel().select(user.getIdProfile());

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,200);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,200);

        getChildren().addAll(usrLB,usrField,fnLB,fnField,lsField,lsLB,phLB,phField,profileField,profileLB,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            String fn=fnField.getText();
            String ln=lsField.getText();
            String ph=phField.getText();
            int profileId=profileField.getSelectionModel().getSelectedIndex();

            if(fn.length()>0 && ln.length()>0 && ph.length()>0)
            {
                if(profileId!=user.getIdProfile() && user.getId()== Manage.getUser().getId())
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Attention");
                    alert.setHeaderText(null);
                    alert.setContentText("Vous serez déconnecté si vous changez votre profil");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK)
                    {
                        UserQueries.update(user.getId(),fn,ln,ph,profileId);
                        Platform.exit();
                    }
                }
                else
                {
                    UserQueries.update(user.getId(),fn,ln,ph,profileId);
                    parent.refreshTable();
                    parent.resetSelection();
                    this.setStyle("-fx-background-color: #f3f3f3;");
                    getChildren().clear();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs");
                alert.showAndWait();
            }

        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
        });
    }

}


