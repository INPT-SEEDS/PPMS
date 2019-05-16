package FrontEnd.DatabaseManagement.Project;

import BackEnd.Criterion.Criterion;
import BackEnd.Criterion.CriterionQueries;
import BackEnd.Evaluate.Evaluate;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.PortfolioCriteria.PortfolioCriteria;
import BackEnd.PortfolioCriteria.PortfolioCriteriaQueries;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

import static BackEnd.Utility.getDatetime;


public class ProjectEvaluate extends ScrollPane
{
    static double scalex = Login.scalex;
    static double scaley = Login.scaley;

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("FF0000");
    private Paint blue= Paint.valueOf("0050FF");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    private int newUserValue;

    private String[] valuesString={"VVL","VL","L","ML","M","MH","H","VH","VVH","EH"};
    private int projectEvaluationsCount;

    private ArrayList<ResourcePane> resourceList;

    public ProjectEvaluate(ProjectInterface parent, int idProject, int idPortfolio,int idUser)
    {
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.setPrefWidth(590*scalex);
        this.setPrefHeight(400*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Pane Content=new Pane();

        Content.getChildren().add(JavaFX.NewLabel("Evaluation :",lightBlue,1,20,30,10));

        //-Criteria-----------------------------------------------------------------------------------------------------------------------------
        List<Evaluate> evaluationList=EvaluateQueries.getProjectEvaluation(idProject);
        List<PortfolioCriteria> portfolioCriteriaList=PortfolioCriteriaQueries.getCriteriaByPortfolio(idPortfolio);
        int size=portfolioCriteriaList.size();
        List<Criterion> criterionList= CriterionQueries.getCriteria();
        TextField[] valuesList=new TextField[size];
        ComboBox[] valuesBox=new ComboBox[size];

        projectEvaluationsCount=ProjectStatueQueries.getProjectEvaluationCount(idProject);

        int y=15;
        int index=0;
        for(Criterion c:criterionList)
        {
            if(index<size && c.getId()==portfolioCriteriaList.get(index).getId())
            {
                int x=12+(index%2)*288;
                if(index%2==0) y+=45;

                PortfolioCriteria pc=portfolioCriteriaList.get(index);

                Content.getChildren().add(JavaFX.NewLabel(c.getRef(),black,18,x,y));
                Button weight=JavaFX.NewButton(String.valueOf(pc.getWeight()),blue,15,x+50,y);

                if(c.getGenre().equals("négatif"))weight.setStyle("-fx-base: #"+red.toString().substring(2)+";");
                weight.setDisable(true);
                TextField valueField=JavaFX.NewTextField(18,75,x+90,y-3);

                valuesList[index]=valueField;
                valuesList[index].setEditable(false);

                valuesBox[index]=JavaFX.NewComboBox(valuesString,101,x+167,y-3);

                boolean userEvaluted=EvaluateQueries.isEvalutedByUser(idProject,idUser,c.getId());
                int oldUserValueIndex;
                if(userEvaluted)
                {
                    oldUserValueIndex=EvaluateQueries.getUserEvaluationIndex(idUser,idProject,c.getId());
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
                valuesBox[index].valueProperty().addListener(new ChangeListener()
                {
                    @Override
                    public void changed(ObservableValue observable, Object oldSelection, Object newSelection)
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
                    }
                });

                Content.getChildren().addAll(weight,valuesList[index],valuesBox[index]);

                index++;
            }
        }

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,y+50);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,y+50);

        Content.getChildren().addAll(confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            boolean userEvaluted=EvaluateQueries.isEvalutedByUser(idProject,idUser);
            EvaluateQueries.resetEvaluation(idProject,idUser);
            int i=0;
            for(TextField tf:valuesList)
            {
                int idCriterion= portfolioCriteriaList.get(i).getId();
                int value=(valuesBox[i].getSelectionModel().getSelectedIndex()+1)*10;
                int weight=portfolioCriteriaList.get(i).getWeight();
                EvaluateQueries.addToDatabase(idProject,idCriterion,idUser,getDatetime(),weight,value);
                i++;
            }
            int evaluationCount=ProjectStatueQueries.getProjectEvaluationCount(idProject);
            if(!userEvaluted)
            {
                evaluationCount++;
            }
            ProjectStatueQueries.addToDatabase(idProject,"Evalué",evaluationCount,getDatetime());
            this.setStyle("-fx-background-color: #f3f3f3;");
            parent.resetSelection();
            getChildren().clear();
            ResourcePane.count=0;
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            parent.resetSelection();
            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
            ResourcePane.count=0;
        });
        setContent(Content);
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

