package FrontEnd.ProjectEvaluation;

import BackEnd.Criterion.Criterion;
import BackEnd.Criterion.CriterionQueries;
import BackEnd.Evaluate.Evaluate;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.PortfolioCriteria.PortfolioCriteria;
import BackEnd.PortfolioCriteria.PortfolioCriteriaQueries;
import BackEnd.Project.ProjectQueries;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import BackEnd.User.User;
import FrontEnd.Home;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;

import static BackEnd.Utility.getDatetime;

public class ProjectEvaluation extends Pane
{
    private int newUserValue;
    private String[] valuesString={"VVL","VL","L","ML","M","MH","H","VH","VVH","EH"};
    private int projectEvaluationsCount;

    public ProjectEvaluation(User user)
    {
        getChildren().add(chooseProject(user));
    }

    private Pane chooseProject(User user)
    {
        Pane chooseProject=new Pane();

        chooseProject.getChildren().add(JavaFX.NewLabel("Evaluation", Color.WHITE,1,40,580,100));
        chooseProject.getChildren().add(JavaFX.NewLabel("Sélectionnez le projet à evaluer", Color.WHITE,1,28,450,200));
        ComboBox projectsBox=JavaFX.NewComboBox(ProjectQueries.getProjectsRef(),300,60,515,275);
        chooseProject.getChildren().add(projectsBox);

        ImageView next=JavaFX.NewNext();
        ImageView previous=JavaFX.NewPrevious();

        next.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            getChildren().add(evaluation(user,projectsBox.getSelectionModel().getSelectedIndex()));
        });
        previous.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            Home.Options.setVisible(true);
        });

        chooseProject.getChildren().addAll(next,previous);
        return chooseProject;
    }

    private Pane evaluation(User user,int idProject)
    {
        Pane evaluation=new Pane();

        evaluation.getChildren().add(JavaFX.NewLabel("Evaluation", Color.WHITE,1,40,580,100));
        ImageView previous=JavaFX.NewPrevious();
        previous.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            getChildren().clear();
            getChildren().add(chooseProject(user));
        });
        evaluation.getChildren().add(previous);

        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setStyle("-fx-background:#0FABD4;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(150* Login.scalex);
        scrollPane.setLayoutY(220* Login.scaley);
        scrollPane.setPrefWidth(1050* Login.scalex);
        scrollPane.setPrefHeight(375*Login.scaley);

        Pane Content=new Pane();


        //-Criteria-----------------------------------------------------------------------------------------------------------------------------
        List<Evaluate> evaluationList= EvaluateQueries.getProjectEvaluation(idProject);
        List<PortfolioCriteria> portfolioCriteriaList= PortfolioCriteriaQueries.getCriteriaByPortfolio(ProjectQueries.getPortfolioId(idProject));
        int size=portfolioCriteriaList.size();
        List<Criterion> criterionList= CriterionQueries.getCriteria();
        TextField[] valuesList=new TextField[size];
        ComboBox[] valuesBox=new ComboBox[size];

        projectEvaluationsCount= ProjectStatueQueries.getProjectEvaluationCount(idProject);
        boolean bool=EvaluateQueries.isEvalutedByUser(idProject,user.getId());
        int offset=335;
        String message="C'est votre première évaluation de ce projet";
        evaluation.getChildren().add(JavaFX.NewLabel("Ce Projet est evalué "+projectEvaluationsCount+" fois",Color.WHITE,2,22,540,150));
        if(bool)
        {
            offset=390;
            message="Vous avez déjà évalué ce projet";
        }
        evaluation.getChildren().add(JavaFX.NewLabel(message,Color.WHITE,2,19,offset+140,175));


        int y=0;
        int index=0;
        for(Criterion c:criterionList)
        {
            if(index<size && c.getId()==portfolioCriteriaList.get(index).getId())
            {
                int x=40+(index%3)*345;
                if(index%3==0) y+=45;

                PortfolioCriteria pc=portfolioCriteriaList.get(index);

                Label criterionRef=JavaFX.NewLabel(c.getRef(),Color.BLACK,18,x,y+2);
                criterionRef.setTooltip(new Tooltip(c.getLabel()));
                Content.getChildren().add(criterionRef);
                Button weight=JavaFX.NewButton(String.valueOf(pc.getWeight()),Color.TURQUOISE,15,x+50,y);
                weight.setTooltip(new Tooltip("le poids du critère"));

                if(c.getGenre().equals("négatif"))weight.setStyle("-fx-base: #"+Color.INDIANRED.toString().substring(2)+";");
                //weight.setDisable(true);
                TextField valueField=JavaFX.NewTextField(18,75,x+90,y-3);

                valuesList[index]=valueField;
                valuesList[index].setEditable(false);

                valuesBox[index]=JavaFX.NewComboBox(valuesString,101,x+167,y-3);
                valuesBox[index].setTooltip(new Tooltip("VVL: Very Very Low (10)\n" +
                        "VL : Very Low (20)\n"+
                        "L  : Low (30)\n"+
                        "ML : Medium Low (40)\n"+
                        "M  : Medium (50)\n"+
                        "MH : Medium High (60)\n"+
                        "H  : High (70)\n"+
                        "VH : Very High (80)\n"+
                        "VVH: Very Very High (90)\n"+
                        "EH : Extra High (100)"));

                boolean userEvaluted=EvaluateQueries.isEvalutedByUser(idProject,user.getId(),c.getId());
                int oldUserValueIndex;
                if(userEvaluted)
                {
                    oldUserValueIndex=EvaluateQueries.getUserEvaluationIndex(user.getId(),idProject,c.getId());
                    valuesBox[index].getSelectionModel().select(oldUserValueIndex);
                }
                else
                {
                    valuesBox[index].getSelectionModel().clearSelection();
                    oldUserValueIndex=0;
                }

                for(Evaluate e:evaluationList)
                {
                    if(e.getIdCritere()==c.getId())
                    {
                        valuesList[index].setText(String.valueOf(e.getValue()));
                    }
                }
                int tempOldValue;
                try
                {
                    tempOldValue= Integer.valueOf(valueField.getText());
                }
                catch(NumberFormatException e)
                {
                    tempOldValue=0;
                }

                int oldValue=tempOldValue;
                int evaluationCount=EvaluateQueries.evaluationCount(c.getId(),idProject);
                valuesBox[index].valueProperty().addListener((observable, oldSelection, newSelection) ->
                {
                    newUserValue=getIndex(newSelection.toString())*10;

                    if(userEvaluted)
                    {
                        int oldUserValue=(oldUserValueIndex+1)*10;
                        int newValue=oldValue+(newUserValue-oldUserValue)/evaluationCount;
                        System.out.println(evaluationCount);
                        valueField.setText(String.valueOf(newValue));
                    }
                    else
                    {
                        int newValue=(newUserValue+(evaluationCount)*oldValue)/(evaluationCount+1);
                        valueField.setText(String.valueOf(newValue));
                    }
                });

                Content.getChildren().addAll(weight,valuesList[index],valuesBox[index]);

                index++;
            }
        }

        ImageView end=JavaFX.NewEnd();

        end.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            boolean userEvaluted=EvaluateQueries.isEvalutedByUser(idProject,user.getId());
            EvaluateQueries.resetEvaluation(idProject,user.getId());
            int i=0;
            for(TextField tf:valuesList)
            {
                int idCriterion= portfolioCriteriaList.get(i).getId();
                int value=(valuesBox[i].getSelectionModel().getSelectedIndex()+1)*10;
                int weight=portfolioCriteriaList.get(i).getWeight();
                EvaluateQueries.addToDatabase(idProject,idCriterion,user.getId(),getDatetime(),weight,value);
                i++;
            }
            int evaluationCount=ProjectStatueQueries.getProjectEvaluationCount(idProject);
            if(!userEvaluted)
            {
                evaluationCount++;
            }
            ProjectStatueQueries.addToDatabase(idProject,"Evalué",evaluationCount,getDatetime());
            getChildren().clear();
            Home.Options.setVisible(true);
        });

        scrollPane.setContent(Content);
        evaluation.getChildren().addAll(scrollPane,end);
        return evaluation;
    }

    int getIndex(String value)
    {
        for(int i=0;i<valuesString.length;i++)
        {
            if(value.equals(valuesString[i]))
            {
                return i+1;
            }
        }
        return -1;
    }
}
