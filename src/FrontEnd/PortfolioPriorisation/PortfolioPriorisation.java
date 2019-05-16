package FrontEnd.PortfolioPriorisation;

import BackEnd.Algorithms.Composition;
import BackEnd.Algorithms.Priorisation;
import BackEnd.Algorithms.Simple;
import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.Project.Project;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import FrontEnd.DatabaseManagement.Graph.Graph;
import FrontEnd.DatabaseManagement.Graph.Tree;
import FrontEnd.Home;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PortfolioPriorisation extends Pane
{
    public PortfolioPriorisation()
    {
        getChildren().add(choosePortfolio());
    }

    private Pane choosePortfolio()
    {
        Pane choosePortfolio=new Pane();

        choosePortfolio.getChildren().add(JavaFX.NewLabel("Evaluation", Color.WHITE,1,40,555,100));
        choosePortfolio.getChildren().add(JavaFX.NewLabel("Sélectionnez le portefeuille à prioriser", Color.WHITE,1,28,425,200));
        ComboBox portfoliosBox=JavaFX.NewComboBox(PortfolioQueries.getPortfoliosRef(),300,60,515,275);
        choosePortfolio.getChildren().add(portfoliosBox);

        ImageView next=JavaFX.NewNext();
        ImageView previous=JavaFX.NewPrevious();

        next.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            getChildren().add(selectProjects(portfoliosBox.getSelectionModel().getSelectedIndex()));
        });
        previous.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            Home.Options.setVisible(true);
        });
        choosePortfolio.getChildren().addAll(next,previous);
        return choosePortfolio;
    }

    private Pane selectProjects(int idPortfolio)
    {
        Pane selectProjects=new Pane();
        selectProjects.getChildren().add(JavaFX.NewLabel("Evaluation", Color.WHITE,1,40,555,100));
        selectProjects.getChildren().add(JavaFX.NewLabel("Sélectionnez les projets candidats", Color.WHITE,1,28,440,200));
        ImageView next=JavaFX.NewNext();
        ImageView previous=JavaFX.NewPrevious();

        previous.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            getChildren().add(choosePortfolio());
        });
        selectProjects.getChildren().add(previous);


        List<Project> projects=PortfolioQueries.getProjectsByPortfolio(idPortfolio);
        CheckBox[] projectsBox=new CheckBox[projects.size()];

        int x=0,y=0;
        int i=0;
        for(Project project:projects)
        {
            String stat= ProjectStatueQueries.getProjectStatue(project.getId());
            int evaluationCount=ProjectStatueQueries.getProjectEvaluationCount(project.getId());
            Color statColor=Color.LIGHTSLATEGREY;
            if(stat.equals("Non Evalué"))
            {
                statColor=Color.INDIANRED;
            }
            Button statIcon=JavaFX.NewButton(stat+" ("+evaluationCount+")",statColor,17,x+370,y+275,140,35);

            selectProjects.getChildren().add(statIcon);
            projectsBox[i]=JavaFX.NewCheckBox(project.getLabel(),25,x+435+75,y+275);
            selectProjects.getChildren().add(projectsBox[i]);
            x+=315;
            if(i%2==1)
            {
                y+=60;
                x=0;
            }
            i++;
        }

        next.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            List<Project> projectsList=new ArrayList<>();
            int index=0;
            int count=0;
            for(CheckBox projectBox:projectsBox)
            {
                if(projectBox.isSelected())
                {
                    projectsList.add(projects.get(index));
                    count++;
                }
                index++;
            }
            if(count<2)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Au moins deux projets doivent être sélectionnés");
                alert.showAndWait();
            }
            else
            {
                getChildren().clear();
                getChildren().add(evaluateProjects(idPortfolio,projectsList));
            }
        });
        selectProjects.getChildren().add(next);
        return selectProjects;
    }

    private Pane evaluateProjects(int idPortfolio,List<Project> projects)
    {
        Pane evaluateProjects=new Pane();
        Tree.CreateXML(new Tree(projects),projects.size());
        evaluateProjects.getChildren().add(JavaFX.NewLabel("Evaluation", Color.WHITE,1,40,575,100));
        evaluateProjects.getChildren().add(JavaFX.NewLabel("Liste des projets évalués classée ", Color.WHITE,1,28,460,200));
        ImageView next=JavaFX.NewNext();
        ImageView previous=JavaFX.NewPrevious();

        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setStyle("-fx-background:#0FABD4;");
        scrollPane.setLayoutX(375* Login.scalex);
        scrollPane.setLayoutY(250* Login.scaley);
        scrollPane.setPrefSize(Login.scalex*600,Login.scaley*300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Pane content=new Pane();

        Priorisation.algorithme(projects);
        int index=0;
        int y=20;
        for(Project project:projects)
        {
            content.getChildren().add(JavaFX.NewLabel(index+". "+project.getLabel(),1,20,125,y+3));
            Button projectValue=JavaFX.NewButton(String.valueOf(project.getTotalValue()),Color.DIMGREY,17,350,y,100,30);
            content.getChildren().add((projectValue));
            index++;
            y+=50;
        }
        scrollPane.setContent(content);

        next.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            getChildren().add(graphResult(idPortfolio,projects));
        });
        previous.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            getChildren().add(selectProjects(idPortfolio));
        });

        evaluateProjects.getChildren().addAll(scrollPane,previous,next);
        evaluateProjects.getChildren().add(JavaFX.NewLabel("Entropie de l'information",Color.WHITE,2,22,555,550));
        return evaluateProjects;
    }

    private Pane graphResult(int idPortfolio, List<Project> projects)
    {
        Pane grapheResult=new Pane();

        Pane graph= Graph.CreateGraphWithProjects(projects,550,325);
        graph.setLayoutX(50);
        graph.setLayoutY(225);
        graph.setStyle("-fx-border-color: #C0C7C9");
        grapheResult.getChildren().add(graph);

        grapheResult.getChildren().add(JavaFX.NewLabel("Planification", Color.WHITE,1,40,575,100));
        grapheResult.getChildren().add(JavaFX.NewLabel("Graph des planifications possible du portefeuille ", Color.WHITE,1,25,65,200));
        grapheResult.getChildren().add(JavaFX.NewLabel("Liste des Planifications du portefeuille classées", Color.WHITE,1,25,710,200));

        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setStyle("-fx-background:#0FABD4;");
        scrollPane.setLayoutX(695* Login.scalex);
        scrollPane.setLayoutY(255* Login.scaley);
        scrollPane.setPrefSize(Login.scalex*600,Login.scaley*369);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Pane content=new Pane();

        Composition[] compositions= Simple.simple(idPortfolio,projects);
        int y=25;
        for(Composition c:compositions)
        {
            float value= (float) ((int)(1000*c.GetValue()))/1000;
            Button compositionValue=JavaFX.NewButton(String.valueOf(value),Color.CORNFLOWERBLUE,18,10,y,100,40);
            content.getChildren().add(compositionValue);
            int x=100;
            for(Project project:c.getProjects())
            {
                Button pp=JavaFX.NewButton(project.getLabel(),Color.DARKSALMON,18,x+25,y,100,40);
                content.getChildren().add(pp);
                x+=105;
            }
            y+=45;
        }

        scrollPane.setContent(content);

        ImageView end=JavaFX.NewEnd();
        end.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            Home.Options.setVisible(true);
        });
        grapheResult.getChildren().addAll(scrollPane,end);
        grapheResult.getChildren().add(JavaFX.NewLabel("Branch and Bound",Color.WHITE,2,22,250,625));
        grapheResult.getChildren().add(JavaFX.NewLabel("Programmation mathématique",Color.WHITE,2,22,825,625));
        return grapheResult;
    }
}
