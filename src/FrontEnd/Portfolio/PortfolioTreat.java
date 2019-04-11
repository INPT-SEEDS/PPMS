package FrontEnd.Portfolio;

import BackEnd.Algorithms.Composition;
import BackEnd.Algorithms.Priorisation;
import BackEnd.Algorithms.Simple;
import BackEnd.Project.Project;
import BackEnd.Project.ProjectQueries;
import BackEnd.ProjectStatue.ProjectStatue;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;


public class PortfolioTreat extends ScrollPane
{
    static double scalex = Home.scalex;
    static double scaley = Home.scaley;

    private int stage;
    private int y;
    private boolean canEvaluate;

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("FF0000");
    private Paint blue= Paint.valueOf("0050FF");
    private Paint green=Paint.valueOf("00A000");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");


    public PortfolioTreat(PortfolioInterface parent, int idPortfolio)
    {
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.setPrefWidth(590*scalex);
        this.setPrefHeight(850*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");
        canEvaluate=true;

        stage=0;

        Label title=JavaFX.NewLabel("Priorisation :",lightBlue,1,20,30,10);
        Pane Content=new Pane();
        Content.getChildren().add(title);

        Pane cardPane=new Pane();
        Content.getChildren().add(cardPane);

        //-Criteria-----------------------------------------------------------------------------------------------------------------------------
        List<Project> projectsList= ProjectQueries.getProjectsByPortfolio(idPortfolio);
        List<ProjectStatue> projectStatueList= ProjectStatueQueries.getProjectStatue();


        y=60;
        for(ProjectStatue projectStatue:projectStatueList)
        {
            for(Project project:projectsList)
            {
                if(projectStatue.getIdProject()==project.getId())
                {
                    cardPane.getChildren().add(JavaFX.NewLabel(project.getLabel(),1,18,50,y+3));
                    Button projectStatueLabel=JavaFX.NewButton(projectStatue.getStatue(),green,18,250,y,300,40);
                    if(projectStatue.getStatue().equals("Partiellement évalué"))
                    {
                        projectStatueLabel.setStyle("-fx-base: #"+blue.toString().substring(2)+";");
                        canEvaluate=false;
                    }
                    else if(projectStatue.getStatue().equals("Non Evalué"))
                    {
                        projectStatueLabel.setStyle("-fx-base: #"+red.toString().substring(2)+";");
                        canEvaluate=false;
                    }
                    projectStatueLabel.setDisable(true);
                    cardPane.getChildren().add(projectStatueLabel);
                    y+=50;
                }
            }
        }

        Button treat=JavaFX.NewButton("Prioriser",lightBlue,18,350,y);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,y);

        Content.getChildren().addAll(treat,cancel);

        y=60;
        treat.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {

            if(stage==0 && canEvaluate)
            {
                cardPane.getChildren().clear();

                /*double[][] matrix= new double[projectsList.size()][1];
                int index=0;
                for(Project project:projectsList)
                {
                    project.initProjectEvaluation();
                    matrix[index]=project.getCtriteriaValues();
                    index++;
                }
                double[] values= Priorisation.algorithme(matrix,11);*/
                Priorisation.algorithme(projectsList);
                int index=0;
                for(Project project:projectsList)
                {
                    /*float value= (float) ((int)(1000*values[index]))/1000;
                    project.setTotalValue(value);*/
                    cardPane.getChildren().add(JavaFX.NewLabel(project.getLabel(),1,18,50,y+3));
                    Button projectValue=JavaFX.NewButton(String.valueOf(project.getTotalValue()),lightOrange,18,375,y,100,40);
                    cardPane.getChildren().add((projectValue));
                    index++;
                    y+=50;
                }
                treat.setLayoutY(y);
                cancel.setLayoutY(y);
                treat.setText("Optimiser");
                title.setText("Optimisation");
                stage=1;
            }
            else if(stage==1)
            {
                cardPane.getChildren().clear();

                Composition[] compositions=Simple.simple(idPortfolio,projectsList);
                int y=60;
                for(Composition c:compositions)
                {
                    float value= (float) ((int)(1000*c.GetValue()))/1000;
                    Button compositionValue=JavaFX.NewButton(String.valueOf(value),lightOrange,18,10,y,100,40);
                    cardPane.getChildren().add(compositionValue);
                    int x=100;
                    for(Project project:c.getProjects())
                    {
                        Button pp=JavaFX.NewButton(project.getLabel(),lightBlue,18,x+20,y,50,40);
                        cardPane.getChildren().add(pp);
                        x+=55;
                    }
                    y+=45;
                }
                treat.setLayoutY(y-45);
                cancel.setLayoutY(y-45);
                treat.setText("Terminer");
                stage=2;
            }
            else if(stage==2)
            {
                parent.resetSelection();
                getChildren().clear();
                this.setStyle("-fx-background-color: #f3f3f3;");
                ResourcePane.count=0;
            }
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
            ResourcePane.count=0;
        });

        this.setContent(Content);
    }

}

