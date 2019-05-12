package FrontEnd.Criterion;

import BackEnd.Criterion.CriterionQueries;
import BackEnd.CriterionType.CriterionTypeQueries;
import BackEnd.User.User;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;

public class CriterionInterface extends Pane
{
    static double scalex = Login.scalex;
    static double scaley = Login.scaley;

    private Paint black=Paint.valueOf("000000");
    private Paint red=Paint.valueOf("F04040");
    private Paint lightBlue=Paint.valueOf("11BAF8");
    private Paint lightGreen=Paint.valueOf("50be96");

    private Button add,bar,modify;
    private Button add2,bar2,modify2;

    private ComboBox typeField;

    private TableView tvCriteria, tvType;

    @SuppressWarnings({ "unchecked","rawtypes" })
    public CriterionInterface(User user,double x, double y)
    {
        this.setLayoutX(x*scalex);
        this.setLayoutY(y*scaley);

        Image modifyIcon= new Image("file:res/icon/resource/Modify.png");

        bar=JavaFX.NewButton("",black,2, 780, 85,550,10);
        modify=JavaFX.NewButton("Modifier le critère", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,74 , 220, 32);

        getChildren().add(bar);
        getChildren().add(modify);

        setActionBar(false);

        tvCriteria = JavaFX.NewTableView(CriterionQueries.getResultSet(), 100,75, 1000, 400);
        Pane additionalOptions=new Pane();
        additionalOptions.setLayoutX(1125*scalex);
        additionalOptions.setLayoutY(75*scaley);
        modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActionBar(false);
            setActionBar2(false);
            setActive(false);
            Object row=tvCriteria.getSelectionModel().getSelectedItems().get(0);
            int id = Integer.valueOf(row.toString().split(",")[0].substring(1));
            CriterionModify criterionModify=new CriterionModify(this,id);
            additionalOptions.getChildren().add(criterionModify);
        });
        getChildren().add(additionalOptions);
        tvCriteria.setRowFactory(tv->
        {
            TableRow row = new TableRow<>();
            row.setOnMousePressed(event->
            {
                if (!row.isEmpty())
                {
                    setActionBar2(false);
                    setActionBar(true);
                    additionalOptions.getChildren().clear();
                }
            });
            return row;
        });

        add=JavaFX.NewButton("Ajouter",18,7,482);
        getChildren().add(add);

        Pane addPane=new Pane();
        List<String> Types=CriterionTypeQueries.getCriteriaTypeRef();
        String[] genres={"positif","négatif"};
        TextField idField=JavaFX.NewTextField(18,200,100,482);

        idField.setEditable(false);
        TextField refField=JavaFX.NewTextField(18,200,300,482);
        TextField libField=JavaFX.NewTextField(18,200,500,482);
        ComboBox genreField=JavaFX.NewComboBox(genres,200,700,482);
        typeField=JavaFX.NewComboBox(Types,200,900,482);

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
        Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
        Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

        add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActive(false);
            idField.setText(String.valueOf(tvCriteria.getItems().size()));
            addPane.getChildren().addAll(addBar,idField,refField,libField,genreField,typeField,confirm,cancel);
            setActionBar(false);
            setActionBar2(false);
            tvCriteria.getSelectionModel().clearSelection();
            tvType.getSelectionModel().clearSelection();
        });

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(idField.getText());
            String ref=refField.getText();
            String label=libField.getText();
            String genre= (String) genreField.getSelectionModel().getSelectedItem();
            int idType=typeField.getSelectionModel().getSelectedIndex();
            if(ref.length()>0 && label.length()>0)
            {
                CriterionQueries.addToDatabase(id,ref,label,genre,idType);
                refreshTable();
                libField.setText("");refField.setText("");
                addPane.getChildren().clear();
                setActive(true);
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
            addPane.getChildren().clear();
            setActive(true);
        });
        getChildren().add(addPane);

        //-----------------------------------------------------------------------------------------------------------------------------------------------
        bar2=JavaFX.NewButton("",black,2, 780, 535,550,10);
        modify2=JavaFX.NewButton("Modifier le type", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,524 , 240, 32);

        getChildren().add(bar2);
        getChildren().add(modify2);

        setActionBar2(false);

        tvType =JavaFX.NewTableView(CriterionTypeQueries.getResultSet(), 100,525, 1000, 420);
        Pane additionalOptions2=new Pane();
        additionalOptions2.setLayoutX(1125*scalex);
        additionalOptions2.setLayoutY(525*scaley);
        modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActionBar(false);
            setActionBar2(false);
            setActive(false);
            Object row=tvType.getSelectionModel().getSelectedItems().get(0);
            int id = Integer.valueOf(row.toString().split(",")[0].substring(1));
            TypeModify typeModify=new TypeModify(this,id);
            additionalOptions2.getChildren().add(typeModify);
        });
        getChildren().add(additionalOptions2);
        tvType.setRowFactory(tv->
        {
            TableRow row = new TableRow<>();
            row.setOnMousePressed(event->
            {
                if (!row.isEmpty())
                {
                    setActionBar2(true);
                    setActionBar(false);
                    additionalOptions2.getChildren().clear();
                }
            });
            return row;
        });

        getChildren().add(tvCriteria);
        getChildren().add(tvType);

        //-----------------------------------------------------------------------------------------------------------------------------------------------
        add2=JavaFX.NewButton("Ajouter",18,7,950);
        getChildren().add(add2);

        Pane addPane2=new Pane();

        TextField id2Field=JavaFX.NewTextField(18,333,100,950);
        id2Field.setEditable(false);
        TextField ref2Field=JavaFX.NewTextField(18,333,433,950);
        TextField lib2Field=JavaFX.NewTextField(18,333,766,950);
        Button confirm2=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
        Button cancel2=JavaFX.NewButton("Annuler",red,18,1230,950);
        Button addBar2=JavaFX.NewButton("",black,2, 93, 961,1200,10);

        add2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            id2Field.setText(String.valueOf(tvType.getItems().size()));
            setActive(false);
            addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
            setActionBar(false);
            setActionBar2(false);
            tvCriteria.getSelectionModel().clearSelection();
            tvType.getSelectionModel().clearSelection();
        });

        confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(id2Field.getText());
            String ref=ref2Field.getText();
            String label=lib2Field.getText();
            if(ref.length()>0 && label.length()>0)
            {
                CriterionTypeQueries.addToDatabase(id,ref,label);
                refreshTable2();
                id2Field.setText("");ref2Field.setText("");lib2Field.setText("");
                addPane2.getChildren().clear();
                setActive(true);
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

        cancel2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            addPane2.getChildren().clear();
            setActive(true);
        });
        getChildren().add(addPane2);
    }

    private void setActionBar(boolean visbility)
    {
        modify.setVisible(visbility);
        bar.setVisible(visbility);
    }


    private void setActionBar2(boolean visbility)
    {
        modify2.setVisible(visbility);
        bar2.setVisible(visbility);
    }

    public void setActive(boolean bool)
    {
        add.setDisable(!bool);
        add2.setDisable(!bool);
        tvType.setDisable(!bool);
        tvCriteria.setDisable(!bool);
    }

    public void resetSelection()
    {
        setActive(true);
        tvCriteria.getSelectionModel().clearSelection();
        tvType.getSelectionModel().clearSelection();
    }

    public void refreshTable()
    {
        JavaFX.updateTable(tvCriteria,CriterionQueries.getResultSet());
    }
    public void refreshTable2()
    {
        ObservableList<String> observableList= FXCollections.observableArrayList(CriterionTypeQueries.getCriteriaTypeRef());
        typeField.setItems(observableList);
        typeField.getSelectionModel().selectFirst();
        JavaFX.updateTable(tvType,CriterionTypeQueries.getResultSet());
    }
}
