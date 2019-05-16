package FrontEnd.DatabaseManagement.User;

import BackEnd.Privilege.Privilege;
import BackEnd.Privilege.PrivilegeQueries;
import BackEnd.Profile.Profile;
import BackEnd.Profile.ProfileQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;


public class ProfileModify extends Pane
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

    private List<Privilege> privilegeList= PrivilegeQueries.getPriviliges();

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

        List<Integer> privilegesId=ProfileQueries.getPrivilegesById(id);
        CheckBox[] privilegesCheckBox=new CheckBox[privilegeList.size()];
        int x=0,y=0;
        int i=0;
        for(Privilege privilege:privilegeList)
        {
            privilegesCheckBox[i]=JavaFX.NewCheckBox(privilege.getName(),x+10,y+120);
            for(int privilegeId:privilegesId)
            {
                if(privilege.getId()==privilegeId)
                {
                    privilegesCheckBox[i].setSelected(true);
                }
            }
            getChildren().add(privilegesCheckBox[i]);
            x+=300;
            if(i%2==1)
            {
                y+=60;
                x=0;
            }
            i++;
        }

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,350);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,350);

        getChildren().addAll(refLB,refField,labelLB,labelField,confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            List<Integer> selectedPrivileges=new ArrayList<>();
            int index=0;
            for(CheckBox priviligeCheckBox:privilegesCheckBox)
            {
                if(priviligeCheckBox.isSelected())
                {
                    selectedPrivileges.add(index);
                }
                index++;
            }
            ProfileQueries.updatePrivileges(id,selectedPrivileges);
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
