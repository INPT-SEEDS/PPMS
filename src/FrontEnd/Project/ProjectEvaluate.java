package FrontEnd.Project;

import BackEnd.Criterion.Criterion;
import BackEnd.Criterion.CriterionQueries;
import BackEnd.Evaluate.Evaluate;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.PortfolioCriteria.PortfolioCriteria;
import BackEnd.PortfolioCriteria.PortfolioCriteriaQueries;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

import static BackEnd.Utility.getDatetime;


public class ProjectEvaluate extends Pane
{
    static double scalex = Home.scalex;
    static double scaley = Home.scaley;

    private Paint black= Paint.valueOf("000000");
    private Paint grey= Paint.valueOf("E9E9E9");
    private Paint white= Paint.valueOf("FFFFFF");
    private Paint red= Paint.valueOf("FF0000");
    private Paint blue= Paint.valueOf("0050FF");
    private Paint lightOrange= Paint.valueOf("F77D50");
    private Paint lightBlue= Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    public ArrayList<ResourcePane> resourceList;

    public ProjectEvaluate(ProjectInterface parent, int idProject, int idPortfolio)
    {
        this.setPrefWidth(575*scalex);
        this.setPrefHeight(400*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        getChildren().add(JavaFX.NewLabel("Evaluation :",lightBlue,1,20,30,10));

        //-Criteria-----------------------------------------------------------------------------------------------------------------------------
        List<Evaluate> evaluationList=EvaluateQueries.getProjectEvaluation(idProject);
        List<PortfolioCriteria> portfolioCriteriaList=PortfolioCriteriaQueries.getCriteriaByPortfolio(idPortfolio);
        int size=portfolioCriteriaList.size();
        List<Criterion> criterionList= CriterionQueries.getCriteria();
        TextField[] valuesList=new TextField[size];

        int y=60;
        int index=0;
        for(Criterion c:criterionList)
        {
            if(index<size && c.getId()==portfolioCriteriaList.get(index).getId())
            {
                int x=15+(index%2)*300;
                PortfolioCriteria pc=portfolioCriteriaList.get(index);

                getChildren().add(JavaFX.NewLabel(c.getRef(),black,18,x,y));
                Button weight=JavaFX.NewButton(String.valueOf(pc.getWeight()),blue,15,x+100,y);

                if(c.getGenre().equals("négatif"))weight.setStyle("-fx-base: #"+red.toString().substring(2)+";");
                weight.setDisable(true);

                valuesList[index]=JavaFX.NewTextField(18,75,x+150,y-3);

                for(Evaluate e:evaluationList)
                {
                    if(e.getIdCritere()==c.getId())
                    {
                        valuesList[index].setText(String.valueOf(e.getValue()));
                    }
                }

                getChildren().addAll(weight,valuesList[index]);

                if(index%2==1) y+=45;
                index++;
            }
        }

        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,350);
        Button cancel=JavaFX.NewButton("Annuler",red,18,475,350);

        getChildren().addAll(confirm,cancel);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            EvaluateQueries.resetEvaluation(idProject);
            int i=0;
            for(TextField tf:valuesList)
            {
                int idCriterion= portfolioCriteriaList.get(i).getId();
                float value=Float.valueOf(tf.getText());
                int weight=portfolioCriteriaList.get(i).getWeight();
                EvaluateQueries.addToDatabase(idProject,idCriterion,getDatetime(),weight,value);
                i++;
            }

            ProjectStatueQueries.addToDatabase(idProject,"Evalué",1,getDatetime());
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
    }
}

