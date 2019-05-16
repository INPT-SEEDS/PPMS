package FrontEnd.DatabaseManagement.Resource;

import BackEnd.Resource.ResourceQueries;
import BackEnd.ResourceCategory.ResourceCategoryQueries;
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

public class ResourceInterface extends Pane
{
    static double scalex = Login.scalex;
    static double scaley = Login.scaley;

    boolean editing;

    private Paint black=Paint.valueOf("000000");
    private Paint red=Paint.valueOf("F04040");
    private Paint lightBlue=Paint.valueOf("11BAF8");
    private Paint lightGreen=Paint.valueOf("50be96");
    private Paint lightOrange=Paint.valueOf("F77D50");

    private Button add,bar,modify;
    private Button add2,bar2,modify2;

    private ComboBox idCatField;

    private TableView tvResource,tvCategory;

    @SuppressWarnings({ "unchecked","rawtypes" })
    public ResourceInterface(User user,double x, double y)
    {
        this.setLayoutX(x*scalex);
        this.setLayoutY(y*scaley);
        Image portfolioIcon=new Image("file:res/icon/resource/portfolio.png");
        Image projectIcon=new Image("file:res/icon/resource/project.png");
        Image modifyIcon= new Image("file:res/icon/resource/Modify.png");

        //-Resource-Table------------------------------------------------------------------------------------------------------------------------------------
        bar=JavaFX.NewButton("",black,2, 1100, 85,100,10);
        modify=JavaFX.NewButton("Modifier la ressource", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,74 , 225, 32);

        getChildren().add(bar);
        getChildren().add(modify);

        setActionBar(false);

        tvResource = JavaFX.NewTableView(ResourceQueries.getResultSet(), 100,75, 1000, 400);
        Pane additionalOptions=new Pane();
        additionalOptions.setLayoutX(1125*scalex);
        additionalOptions.setLayoutY(75*scaley);

        getChildren().add(additionalOptions);
        editing=false;
        tvResource.setRowFactory(tv->
        {
            TableRow row = new TableRow<>();
            row.setOnMousePressed(event->
            {
                if (!row.isEmpty() && !editing)
                {
                    setActionBar2(false);
                    setActionBar(true);
                    additionalOptions.getChildren().clear();
                }
            });
            return row;
        });

        modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            if(!editing)
            {
                setActionBar(false);
                setActive(false);

                Object row=tvResource.getSelectionModel().getSelectedItems().get(0);
                int resId = Integer.valueOf(row.toString().split(",")[0].substring(1));

                ResourceModify resourceModify=new ResourceModify(this,resId);
                additionalOptions.getChildren().add(resourceModify);
            }
        });

        //-Resource-Add------------------------------------------------------------------------------------------------------------------------------------
        add=JavaFX.NewButton("Ajouter",18,7,482);
        getChildren().add(add);

        Pane addPane=new Pane();
        List<String> Categories= ResourceCategoryQueries.getCategoriesRef();
        TextField idField=JavaFX.NewTextField(18,333,100,482);
        idField.setEditable(false);
        TextField libField=JavaFX.NewTextField(18,333,433,482);
        idCatField=JavaFX.NewComboBox(Categories,333,766,482);
        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
        Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
        Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

        add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            idField.setText(String.valueOf(tvResource.getItems().size()));
            setActive(false);
            addPane.getChildren().addAll(addBar,idField,idCatField,libField,confirm,cancel);
            setActionBar(false);
            setActionBar2(false);
            tvCategory.getSelectionModel().clearSelection();
            tvResource.getSelectionModel().clearSelection();
        });

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(idField.getText());
            String label=libField.getText();
            int idCat=idCatField.getSelectionModel().getSelectedIndex();
            if(label.length()>0)
            {
                ResourceQueries.addToDatabase(id,label,idCat);
                refreshTable();
                idField.setText("");libField.setText("");
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


        //-Category-Table------------------------------------------------------------------------------------------------------------------------------------
        bar2=JavaFX.NewButton("",black,2, 780, 535,550,10);
        modify2=JavaFX.NewButton("Modifier la Catégorie", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,524 , 240, 32);

        getChildren().add(bar2);
        getChildren().add(modify2);

        setActionBar2(false);

        tvCategory=JavaFX.NewTableView(ResourceCategoryQueries.getResultSet(), 100,525, 1000, 420);
        Pane additionalOptions2=new Pane();
        additionalOptions2.setLayoutX(1125*scalex);
        additionalOptions2.setLayoutY(525*scaley);
        modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActionBar(false);
            setActionBar2(false);
            setActive(false);

            Object row=tvCategory.getSelectionModel().getSelectedItems().get(0);
            int catId = Integer.valueOf(row.toString().split(",")[0].substring(1));

            CategoryModify categoryModify=new CategoryModify(this,catId);
            additionalOptions2.getChildren().add(categoryModify);
        });
        getChildren().add(additionalOptions2);

        tvCategory.setRowFactory(tv->
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

        getChildren().add(tvResource);
        getChildren().add(tvCategory);

        //-Category-Add------------------------------------------------------------------------------------------------------------------------------------
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
            id2Field.setText(String.valueOf(tvCategory.getItems().size()));
            setActive(false);
            addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
            setActionBar(false);
            setActionBar2(false);
            tvCategory.getSelectionModel().clearSelection();
            tvResource.getSelectionModel().clearSelection();
        });

        confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(id2Field.getText());
            String ref=ref2Field.getText();
            String label=lib2Field.getText();
            if(ref.length()>0 && label.length()>0)
            {
                ResourceCategoryQueries.addToDatabase(id,ref,label);
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
        tvCategory.setDisable(!bool);
        tvResource.setDisable(!bool);
    }

    public void resetSelection()
    {
        setActive(true);
        tvResource.getSelectionModel().clearSelection();
        tvCategory.getSelectionModel().clearSelection();
    }

    public void refreshTable()
    {
        JavaFX.updateTable( tvResource,ResourceQueries.getResultSet());
    }
    public void refreshTable2()
    {
        ObservableList<String> observableList= FXCollections.observableArrayList(ResourceCategoryQueries.getCategoriesRef());
        idCatField.setItems(observableList);
        idCatField.getSelectionModel().selectFirst();
        JavaFX.updateTable(tvCategory, ResourceCategoryQueries.getResultSet());
    }
}