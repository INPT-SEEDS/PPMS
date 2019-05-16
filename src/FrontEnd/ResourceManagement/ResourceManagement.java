package FrontEnd.ResourceManagement;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.Project.Project;
import BackEnd.ResToPortfolio.ResToPortfolioQueries;
import BackEnd.ResToProject.ResToProjectQueries;
import BackEnd.Resource.ResourceQueries;
import BackEnd.Utility;
import FrontEnd.Home;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

public class ResourceManagement extends Pane
{
    int selectedPortfolio=-1;
    public ResourceManagement()
    {
        getChildren().add(gestion(0));
    }

    private Pane gestion(int idResource)
    {
        Pane gestion=new Pane();

        ImageView end=JavaFX.NewEnd();
        end.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            Home.Options.setVisible(true);
        });

        ScrollPane portfoliosScroll=new ScrollPane();
        ScrollPane portfolioPane=new ScrollPane();

        portfolioPane.setStyle("-fx-background:#0FABD4;");
        portfolioPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        portfolioPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        portfolioPane.setLayoutX(315* Login.scalex);
        portfolioPane.setLayoutY(100* Login.scaley);
        portfolioPane.setPrefWidth(980* Login.scalex);
        portfolioPane.setPrefHeight(525*Login.scaley);

        portfoliosScroll.setStyle("-fx-background:#0FABD4;");
        portfoliosScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        portfoliosScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        portfoliosScroll.setLayoutX(50* Login.scalex);
        portfoliosScroll.setLayoutY(100* Login.scaley);
        portfoliosScroll.setPrefWidth(267* Login.scalex);
        portfoliosScroll.setPrefHeight(525*Login.scaley);

        Pane portfolioContent=new Pane();
        List<String> portfoliosRef= PortfolioQueries.getPortfoliosRef();

        int index=0;
        for(String portfolioRef:portfoliosRef)
        {
            Button portfolio=JavaFX.NewButton(portfolioRef,Color.INDIANRED,22,0,index*50,250,50);
            int idPortfolio=index;
            portfolio.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
            {
                portfolioPane.setContent(null);
                portfolioPane.setContent(portfolioResource(idResource,idPortfolio,portfolioRef,end));
                selectedPortfolio=idPortfolio;
            });
            if(index==selectedPortfolio)
            {
                portfolioPane.setContent(null);
                portfolioPane.setContent(portfolioResource(idResource,selectedPortfolio,portfolioRef,end));
            }
            portfolioContent.getChildren().add(portfolio);
            index++;
        }
        portfoliosScroll.setContent(portfolioContent);

        gestion.getChildren().addAll(portfoliosScroll,portfolioPane,end);


        gestion.getChildren().add(JavaFX.NewLabel("Gestion des ressources", Color.WHITE,1,40,460,50));
        gestion.getChildren().add(JavaFX.NewLabel("Sélectionnez la ressource", Color.WHITE,1,25,450,125));
        ComboBox resourcesBox=JavaFX.NewComboBox(ResourceQueries.getResourcesRef(),150,25,765,120);
        resourcesBox.getSelectionModel().select(idResource);
        resourcesBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) ->
        {
            getChildren().clear();
            getChildren().add(gestion(resourcesBox.getSelectionModel().getSelectedIndex()));
        });

        gestion.getChildren().add(resourcesBox);

        return gestion;
    }
    private Pane portfolioResource(int idResource,int idPortfolio,String ref,ImageView end)
    {
        Pane portfolioResource=new Pane();
        int resourceCount=ResToPortfolioQueries.getResourceCount(idPortfolio,idResource);
        portfolioResource.getChildren().add(JavaFX.NewLabel("Ressources disponible dans le portfeuille :",Color.WHITE,0,22,25,103));
        portfolioResource.getChildren().add(JavaFX.NewLabel(ref,Color.WHITE,1,22,475,103));
        TextField resField=JavaFX.NewTextField(20,100,700,103);
        resField.setDisable(true);
        resField.setText(String.valueOf(resourceCount));

        Button change=JavaFX.NewButton("Changer",Color.SKYBLUE,17,800,100,150,40);
        change.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            if(change.getText().equals("Enregistrer"))
            {
                ResToPortfolioQueries.addToDatabase(idPortfolio,idResource, Utility.getDatetime(),Integer.valueOf(resField.getText()));

                change.setText("Changer");
                change.setStyle("-fx-base: #87CEEB;");
                resField.setDisable(true);
            }
            else
            {
                change.setText("Enregistrer");
                change.setStyle("-fx-base: #98FB98;");
                resField.setDisable(false);
            }
        });

        ScrollPane projectsScroll=new ScrollPane();

        projectsScroll.setStyle("-fx-background:#0FABD4;");
        projectsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        projectsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        projectsScroll.setLayoutX(0* Login.scalex);
        projectsScroll.setLayoutY(150* Login.scaley);
        projectsScroll.setPrefWidth(980* Login.scalex);
        projectsScroll.setPrefHeight(375*Login.scaley);

        Pane projectContent=new Pane();

        List<Project> projects= PortfolioQueries.getProjectsByPortfolio(idPortfolio);

        int index=0;
        for(Project project:projects)
        {
            Button projectButton=JavaFX.NewButton(project.getLabel(),Color.DEEPSKYBLUE,20,50,10+index*50,200,40);
            int idProje=index;

            int projectResourceCount= ResToProjectQueries.getResourceCount(project.getId(),idResource);
            projectContent.getChildren().add(JavaFX.NewLabel("Ressources requises pour ce projet ",Color.WHITE,0,22,270,index*50+15));
            TextField projectResField=JavaFX.NewTextField(20,100,650,index*50+10);
            projectResField.setDisable(true);
            projectResField.setText(String.valueOf(projectResourceCount));

            Button change2=JavaFX.NewButton("Changer",Color.SKYBLUE,17,750,index*50+10,150,40);
            change2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
            {
                if(change2.getText().equals("Enregistrer"))
                {
                    ResToProjectQueries.addToDatabase(project.getId(),idResource,Utility.getDatetime(),Integer.valueOf(projectResField.getText()));

                    change2.setText("Changer");
                    change2.setStyle("-fx-base: #87CEEB;");
                    projectResField.setDisable(true);
                }
                else
                {
                    change2.setText("Enregistrer");
                    change2.setStyle("-fx-base: #98FB98;");
                    projectResField.setDisable(false);
                }
            });

            projectContent.getChildren().addAll(projectResField,projectButton,change2);
            index++;
        }
        projectsScroll.setContent(projectContent);

        portfolioResource.getChildren().addAll(resField,change,projectsScroll);



        return portfolioResource;
    }
}
