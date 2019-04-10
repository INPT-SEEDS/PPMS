package Interface;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

class CritereWindow
{

    private static String[] list= {"Type1","Type2","Type3"};
    private static String[] ListSousCritereType = {"SousType1","SousType2","SousType3"};

    private static Label LabelNom1;
    private static Label LabelNom2;
    private static Label LabelNom3;
    private static Label LabelNom4;
    private static Label LabelNom5;
    private static Label[] LabelNomList = {LabelNom1,LabelNom2,LabelNom3,LabelNom4,LabelNom5};

    private static Label LabelType1;
    private static Label LabelType2;
    private static Label LabelType3;
    private static Label LabelType4;
    private static Label LabelType5;
    private static Label[] LabelTypeList =  {LabelType1,LabelType2,LabelType3,LabelType4,LabelType5};

    @SuppressWarnings("rawtypes")
    private static ComboBox ComboBox1,ComboBox2,ComboBox3,ComboBox4,ComboBox5;
    @SuppressWarnings("rawtypes")
    private static ComboBox[] ComboBoxList = {ComboBox1,ComboBox2,ComboBox3,ComboBox4,ComboBox5};

    private static TextField TextField1;
    private static TextField TextField2;
    private static TextField TextField3;
    private static TextField TextField4;
    private static TextField TextField5;
    private static TextField[] TextFieldList = {TextField1,TextField2,TextField3,TextField4,TextField5};

    private static int SousCritereCompteur = 1;

    private static Boolean FirstTime = true;

    @SuppressWarnings("rawtypes")
	static void  DisplayCritere() {
        Stage CritereStage = new Stage();
        CritereStage.initModality(Modality.APPLICATION_MODAL);
        CritereStage.setTitle("Creer un critere");
        CritereStage.setOnCloseRequest(e -> {
            SousCritereCompteur = 1;
            FirstTime = true;
        });


        Pane layout = new Pane();

        Label l1			=	Objects.NewLabel("Veuillez specifier les informations du critere a ajouter : ",20, 125, 5);						layout.getChildren().add(l1);

        Label nom			=	Objects.NewLabel("Nom : ",20, 20, 50);					layout.getChildren().add(nom);
        TextField t		    =	Objects.NewTextField(20, 150, 200, 50);				layout.getChildren().add(t);

        Label type = Objects.NewLabel("Type : ",20, 20, 100);                           layout.getChildren().add(type);
        ComboBox typeBox = Objects.NewComboBox(list,150,200,100);                              layout.getChildren().add(typeBox);

        Button Ajouter = Objects.NewButton("Ajouter Sous Critere",15,20,150);                           layout.getChildren().add(Ajouter);

        Button Supprimer = Objects.NewButton("Supprimer",15,0,0);

        Button Valider = Objects.NewButton("Valider",Color.color(0,0.5,0),700, 360);   layout.getChildren().add(Valider);



                Scene scene = new Scene (layout,800,400);
        CritereStage.setScene(scene);

        Valider.setOnAction(event -> {
            SousCritereCompteur = 1;
            FirstTime = true;
            CritereStage.close();
        });

        Ajouter.setOnAction(e -> {

            if(Ajouter.getLayoutY()<scene.getHeight()-100){

                SousCritere(layout, SousCritereCompteur,(int) Ajouter.getLayoutX(),(int) Ajouter.getLayoutY(),ListSousCritereType);
                SousCritereCompteur++;
                Ajouter.setLayoutY(Ajouter.getLayoutY()+100);
                Supprimer.setLayoutY(Supprimer.getLayoutY()+100);

            }
            else{
                if(Ajouter.getLayoutX()+Ajouter.getWidth()<scene.getWidth()-350){
                    Ajouter.setLayoutY(150);
                    Supprimer.setLayoutY(150);
                    Ajouter.setLayoutX(Ajouter.getLayoutX()+350);
                    Supprimer.setLayoutX(Supprimer.getLayoutX()+350);
                    SousCritere(layout, SousCritereCompteur,(int) Ajouter.getLayoutX(),(int) Ajouter.getLayoutY()-100,ListSousCritereType);
                    SousCritereCompteur++;
                }
                else{
                    //JOptionPane.showMessageDialog(null,"Nombre Maximum de sous critères atteint");
                    //AlertBox.display("Erreur","Nombre Maximum de sous critères possibles est atteint");
                    Ajouter.setDisable(true);
                }
            }
            if(SousCritereCompteur==2 && FirstTime){
                Supprimer.setLayoutY(Ajouter.getLayoutY());
                Supprimer.setLayoutX(Ajouter.getLayoutX()+Ajouter.getWidth()+5);
                layout.getChildren().add(Supprimer);
            }

            if(FirstTime){
                FirstTime = false;
            }

            if(Supprimer.isDisabled()){
                Supprimer.setDisable(false);
            }
            if(SousCritereCompteur==4) Ajouter.setDisable(false);
        });

        Supprimer.setOnAction(e -> {

            if(Ajouter.getLayoutY()>150 && SousCritereCompteur>1){

                layout.getChildren().removeAll(LabelNomList[SousCritereCompteur-2],LabelTypeList[SousCritereCompteur-2],ComboBoxList[SousCritereCompteur-2],TextFieldList[SousCritereCompteur-2]);

                SousCritereCompteur--;
                Ajouter.setLayoutY(Ajouter.getLayoutY()-100);
                Supprimer.setLayoutY(Supprimer.getLayoutY()-100);

            }
            else{
                if(Ajouter.getLayoutX()+Ajouter.getWidth()>scene.getWidth()-350){
                    Ajouter.setLayoutY(350);
                    Supprimer.setLayoutY(350);
                    Ajouter.setLayoutX(Ajouter.getLayoutX()-350);
                    Supprimer.setLayoutX(Supprimer.getLayoutX()-350);

                    layout.getChildren().removeAll(LabelNomList[SousCritereCompteur-2],LabelTypeList[SousCritereCompteur-2],ComboBoxList[SousCritereCompteur-2],TextFieldList[SousCritereCompteur-2]);
                    SousCritereCompteur--;
                }
                else{
                    //JOptionPane.showMessageDialog(null,"Nombre Minimum de sous critères atteint");
                    //AlertBox.display("Erreur","Il n'y a plus de sous critères à supprimer");
                    Supprimer.setDisable(true);
                }
            }

            if(Ajouter.isDisabled()){
                Ajouter.setDisable(false);
            }
            if(SousCritereCompteur==1)Supprimer.setDisable(true);

        });

        CritereStage.setResizable(false);
        CritereStage.centerOnScreen();
        CritereStage.showAndWait();
    }

    @SuppressWarnings("rawtypes")
	private static void SousCritere(Pane layout, int Compteur, int x, int y, String[] list){
        Label nom = Objects.NewLabel("Nom Sous Type " + Compteur + ":", 20, x, y);
        layout.getChildren().add(nom);
        TextField t = Objects.NewTextField(20, 150, x + 180, y);
        layout.getChildren().add(t);

        Label type = Objects.NewLabel("Type : ", 20, x, y + 50);
        layout.getChildren().add(type);
        ComboBox<String> typeBox = Objects.NewComboBox(list, 150, x + 180, y + 50);
        layout.getChildren().add(typeBox);

        LabelNomList[Compteur-1] = nom;
        TextFieldList[Compteur-1] = t;

        LabelTypeList[Compteur-1] = type;
        ComboBoxList[Compteur-1] = typeBox;
    }
}
