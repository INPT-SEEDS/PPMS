package FrontEnd.Resource;

import BackEnd.Resource.ResourceQueries;
import BackEnd.ResourceCategory.ResourceCategoryQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;

public class ResourceInterface extends Pane
{
    static double scalex = Home.scalex;
    static double scaley = Home.scaley;

    boolean editing;

    private Paint black=Paint.valueOf("000000");
    private Paint red=Paint.valueOf("F04040");
    private Paint lightBlue=Paint.valueOf("5096be");
    private Paint lightGreen=Paint.valueOf("50be96");
    private Paint lightOrange=Paint.valueOf("F77D50");

    private Button add,bar,assignToPortfolio,assignToProject,modify;
    private Button add2,bar2,modify2;
    private TableView tvResource,tvCategory;

    @SuppressWarnings({ "unchecked","rawtypes" })
    public ResourceInterface(double x,double y)
    {
        this.setLayoutX(x*scalex);
        this.setLayoutY(y*scaley);
        Image portfolioIcon=new Image("file:res/icon/resource/portfolio.png");
        Image projectIcon=new Image("file:res/icon/resource/project.png");
        Image modifyIcon= new Image("file:res/icon/resource/Modify.png");

        //-Resource-Table------------------------------------------------------------------------------------------------------------------------------------
        bar=JavaFX.NewButton("",black,2, 1100, 95,600,10);
        assignToPortfolio=JavaFX.NewButton("Affecter au portefeuille", portfolioIcon, ContentDisplay.LEFT, lightOrange, 16,1110 ,95 , 240, 32);
        assignToProject=JavaFX.NewButton("Affecter au projet", projectIcon, ContentDisplay.LEFT, lightOrange, 16,1360 ,95 , 220, 32);
        modify=JavaFX.NewButton("Modifier", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1590 ,95 , 125, 32);

        getChildren().add(bar);
        getChildren().add(assignToPortfolio);
        getChildren().add(assignToProject);
        getChildren().add(modify);

        setActionBar(false,0);

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
                    setActionBar2(false,0);
                    setActionBar(true,(int) (95+row.getIndex()*25/scaley));
                    additionalOptions.getChildren().clear();
                }
            });
            return row;
        });

        assignToPortfolio.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            if(!editing)
            {
                Object row=tvResource.getSelectionModel().getSelectedItems().get(0);
                int resId = Integer.valueOf(row.toString().split(",")[0].substring(1));

                AssignToPortfolio atp=new AssignToPortfolio(resId,assignToPortfolio.getLayoutX(),assignToPortfolio.getLayoutY(),this);
                getChildren().add(atp);
                editing=true;
            }
        });
        assignToProject.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            if(!editing)
            {
                Object row=tvResource.getSelectionModel().getSelectedItems().get(0);
                int resId = Integer.valueOf(row.toString().split(",")[0].substring(1));

                AssignToProject atp=new AssignToProject(resId,assignToProject.getLayoutX(),assignToProject.getLayoutY(),this);
                getChildren().add(atp);
                editing=true;
            }

        });

        modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            if(!editing)
            {
                setActionBar(false,0);
                setActive(false);
                ResourceModify resourceModify=new ResourceModify(this,0);
                additionalOptions.getChildren().add(resourceModify);
            }
        });

        //-Resource-Add------------------------------------------------------------------------------------------------------------------------------------
        add=JavaFX.NewButton("Ajouter",18,7,482);
        getChildren().add(add);

        Pane addPane=new Pane();
        List<String> Categories= ResourceCategoryQueries.getCategoriesRef();
        TextField idField=JavaFX.NewTextField(18,333,100,482);
        idField.setText(String.valueOf(tvResource.getItems().size()));
        idField.setEditable(false);
        TextField libField=JavaFX.NewTextField(18,333,433,482);
        ComboBox idCatField=JavaFX.NewComboBox(Categories,333,766,482);
        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
        Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
        Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

        add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActive(false);
            addPane.getChildren().addAll(addBar,idField,idCatField,libField,confirm,cancel);
            setActionBar(false,0);
            setActionBar2(false,0);
            tvCategory.getSelectionModel().clearSelection();
            tvResource.getSelectionModel().clearSelection();
        });

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(idField.getText());
            String label=libField.getText();
            int idCat=idCatField.getSelectionModel().getSelectedIndex();
            ResourceQueries.addToDatabase(id,label,idCat);
            refreshTable();
            idField.setText("");libField.setText("");
            addPane.getChildren().clear();
            setActive(true);
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            addPane.getChildren().clear();
            setActive(true);
        });
        getChildren().add(addPane);


        //-Category-Table------------------------------------------------------------------------------------------------------------------------------------
        bar2=JavaFX.NewButton("",black,2, 780, 95,550,10);
        modify2=JavaFX.NewButton("Modifier la Catégorie", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,95 , 240, 32);

        getChildren().add(bar2);
        getChildren().add(modify2);

        setActionBar2(false,0);

        tvCategory=JavaFX.NewTableView(ResourceCategoryQueries.getResultSet(), 100,525, 1000, 420);
        Pane additionalOptions2=new Pane();
        additionalOptions2.setLayoutX(1125*scalex);
        additionalOptions2.setLayoutY(525*scaley);
        modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActionBar(false,0);
            setActionBar2(false,0);
            setActive(false);
            CategoryModify categoryModify=new CategoryModify(this,0);
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
                    setActionBar2(true,(int) (545+row.getIndex()*25/scaley));
                    setActionBar(false,0);
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
        id2Field.setText(String.valueOf(tvCategory.getItems().size()));
        id2Field.setEditable(false);
        TextField ref2Field=JavaFX.NewTextField(18,333,433,950);
        TextField lib2Field=JavaFX.NewTextField(18,333,766,950);
        Button confirm2=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
        Button cancel2=JavaFX.NewButton("Annuler",red,18,1230,950);
        Button addBar2=JavaFX.NewButton("",black,2, 93, 961,1200,10);

        add2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            setActive(false);
            addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
            setActionBar(false,0);
            setActionBar2(false,0);
            tvCategory.getSelectionModel().clearSelection();
            tvResource.getSelectionModel().clearSelection();
        });

        confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            int id=Integer.valueOf(id2Field.getText());
            String ref=ref2Field.getText();
            String label=lib2Field.getText();
            ResourceCategoryQueries.addToDatabase(id,ref,label);
            refreshTable2();
            idCatField.getItems().add(ref);
            idCatField.getSelectionModel().select(0);
            id2Field.setText("");ref2Field.setText("");lib2Field.setText("");
            addPane2.getChildren().clear();
            setActive(true);
        });

        cancel2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            addPane2.getChildren().clear();
            setActive(true);
        });
        getChildren().add(addPane2);

    }

    private void setActionBar(boolean visbility,int position)
    {
        assignToPortfolio.setLayoutY(position*scaley);
        assignToProject.setLayoutY(position*scaley);
        modify.setLayoutY(position*scaley);
        bar.setLayoutY((position+11)*scaley);

        assignToPortfolio.setVisible(visbility);
        assignToProject.setVisible(visbility);
        modify.setVisible(visbility);
        bar.setVisible(visbility);
    }


    private void setActionBar2(boolean visbility,int position)
    {
        modify2.setLayoutY(position*scaley);
        bar2.setLayoutY((position+11)*scaley);

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
        JavaFX.updateTable(tvCategory, ResourceCategoryQueries.getResultSet());
    }
}