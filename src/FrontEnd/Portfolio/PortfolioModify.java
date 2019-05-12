package FrontEnd.Portfolio;

import BackEnd.Criterion.CriterionQueries;
import BackEnd.Evaluate.EvaluateQueries;
import BackEnd.Portfolio.Portfolio;
import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.PortfolioCriteria.PortfolioCriteria;
import BackEnd.PortfolioCriteria.PortfolioCriteriaQueries;
import BackEnd.Project.Project;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import BackEnd.ResToPortfolio.ResToPortfolioQueries;
import BackEnd.Resource.AssignedResource;
import BackEnd.Utility;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PortfolioModify extends ScrollPane
{
    static double scalex = Login.scalex;
    static double scaley = Login.scaley;

    private Paint black= javafx.scene.paint.Paint.valueOf("000000");
    private Paint grey= javafx.scene.paint.Paint.valueOf("E9E9E9");
    private Paint white= javafx.scene.paint.Paint.valueOf("FFFFFF");
    private Paint red= javafx.scene.paint.Paint.valueOf("F04040");
    private Paint lightOrange= javafx.scene.paint.Paint.valueOf("F77D50");
    private Paint lightBlue= javafx.scene.paint.Paint.valueOf("5096be");
    private Paint lightGreen= Paint.valueOf("50be96");

    public ArrayList<ResourcePane> resourcePaneList;

    Pane criteriaPane;

    public PortfolioModify(PortfolioInterface parent, int idPortfolio)
    {
        Portfolio portfolio= PortfolioQueries.getPortfolioById(idPortfolio);
        ResourcePane.count=0;

        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.setPrefWidth(590*scalex);
        this.setPrefHeight(850*scaley);
        this.setStyle("-fx-background-color: #"+grey.toString().substring(2)+";");

        Pane Content=new Pane();

        Label refLB=JavaFX.NewLabel("Reference",lightBlue,1,20,10,10);
        TextField refField=JavaFX.NewTextField(18,200,10,35);
        refField.setText(portfolio.getRef());
        Label libLB=JavaFX.NewLabel("Libellé",lightBlue,1,20,250,10);
        TextField libField=JavaFX.NewTextField(18,300,250,35);
        libField.setText(portfolio.getLabel());



        Content.getChildren().addAll(refLB,refField,libLB,libField);

        //-Criteria-----------------------------------------------------------------------------------------------------------------------------
        Content.getChildren().add(JavaFX.NewLabel("Critères",lightBlue,1,20,10,100));

        List<PortfolioCriteria> criteriaList=PortfolioCriteriaQueries.getCriteriaByPortfolio(idPortfolio);

        List<String> criteria= CriterionQueries.getCriteriaRef();
        CheckBox[] criteriaBox=new CheckBox[criteria.size()];
        TextField[] weightsList=new TextField[criteria.size()];
        int y=100;
        int criteriaIndex=0;
        for(String criterion:criteria)
        {
            int x=50+(criteriaIndex%2)*250;
            if(criteriaIndex%2==0)
            {
                y+=50;
            }
            CheckBox criterionBox=JavaFX.NewCheckBox(criterion,x,y);
            weightsList[criteriaIndex]=JavaFX.NewTextField(18,60,x+150,y);

            weightsList[criteriaIndex].setVisible(false);
            for(PortfolioCriteria pc:criteriaList)
            {
                if(pc.getId()==criteriaIndex)
                {
                    criterionBox.setSelected(true);
                    weightsList[criteriaIndex].setVisible(true);
                    weightsList[criteriaIndex].setText(String.valueOf(pc.getWeight()));
                    // force the field to be numeric only
                    TextField textField=weightsList[criteriaIndex];
                    textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                    {
                        if (!newValue.matches("\\d*"))
                        {
                            textField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }});
                    weightsList[criteriaIndex]=textField;
                    break;
                }
            }

            criteriaBox[criteriaIndex]=criterionBox;
            int finalCriteriaIndex = criteriaIndex;
            criteriaBox[criteriaIndex].setOnAction((event) ->
            {
                boolean selected = criterionBox.isSelected();
                weightsList[finalCriteriaIndex].setVisible(selected);
            });

            Content.getChildren().addAll(criteriaBox[criteriaIndex],weightsList[criteriaIndex]);

            criteriaIndex++;
        }

        //-Resource-----------------------------------------------------------------------------------------------------------------------------
        y+=50;
        Content.getChildren().add(JavaFX.NewLabel("Ressources",lightBlue,1,20,10,y));
        List<AssignedResource> resourcesList= ResToPortfolioQueries.getResourceByPortfolio(idPortfolio);
        Pane resourcePanes=new Pane();
        resourcePanes.setLayoutY((y+50)*scaley);
        resourcePaneList= new ArrayList<>();

        int resoureIndex=0;
        for(AssignedResource resource:resourcesList)
        {
            ResourcePane rp=new ResourcePane(resourcePanes,resourcePaneList,resource);
            resourcePaneList.add(rp);
            resourcePanes.getChildren().add(rp);
        }

        Content.getChildren().add(resourcePanes);

        //-Confirm-And-Cancel----------------------------------------------------------------------------------------------------------------------
        Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,350,75+y+resourcesList.size()*50);
        Button cancel=JavaFX.NewButton("Annuler",red,18,465,75+y+resourcesList.size()*50);
        Content.getChildren().addAll(confirm,cancel);
        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            PortfolioCriteriaQueries.resetPortfolio(idPortfolio);
            String ref=refField.getText();
            String lib=libField.getText();

            if(!ref.equals(portfolio.getRef()) || !lib.equals(portfolio.getLabel()))
            {
                PortfolioQueries.updatePortfolio(idPortfolio,ref,lib);
                System.out.println("Updated");
                parent.refreshTable();
            }

            int index=0;
            boolean didReset=false;//To not reset the projects statues many times ( to not fill database and for better performance)
            for(CheckBox cb:criteriaBox)
            {
                boolean wasPresent=false;//if this criterion was already present in the portfolio
                List<Project> projects=PortfolioQueries.getProjectsByPortfolio(idPortfolio);

                for(PortfolioCriteria criterion:criteriaList)
                {
                    if(index==criterion.getId())
                    {
                        wasPresent=true;
                    }
                }
                if(cb.isSelected())
                {
                    if(!wasPresent && !didReset)
                    {
                        for(Project project:projects)
                        {
                            ProjectStatueQueries.addToDatabase(project.getId(),"Non Evalué",0,Utility.getDatetime());
                        }
                        didReset=true;
                    }
                    int weight=Integer.valueOf(weightsList[index].getText());
                    PortfolioCriteriaQueries.addToDatabase(index,idPortfolio,weight, Utility.getDatetime(),"null");
                }
                else
                {
                    if(wasPresent)
                    {
                        for(Project project:projects)
                        {
                            EvaluateQueries.deleteCriterionEvaluation(project.getId(),index);
                        }
                    }
                }
                index++;
            }

            ResToPortfolioQueries.resetTable(idPortfolio);
            for(ResourcePane rp:resourcePaneList)
            {
                String date=new Timestamp(60*1000*(System.currentTimeMillis()/(1000*60))).toString();
                ResToPortfolioQueries.addToDatabase(idPortfolio,rp.getIdResource(),date.substring(0,date.length()-5),rp.getQuantity());
            }

            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
            parent.resetSelection();
            ResourcePane.count=0;
        });

        cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            getChildren().clear();
            this.setStyle("-fx-background-color: #f3f3f3;");
            parent.resetSelection();
            ResourcePane.count=0;
        });

        this.setContent(Content);
    }
}

class ResourcePane extends Pane
{
    static double scaley = Login.scaley;

    public static int count=0;
    public int index;

    private int idResource;
    private int quantity;
    private TextField resourcesCountField;

    private Paint lightOrange= javafx.scene.paint.Paint.valueOf("F77D50");

    ResourcePane(Pane resourcePane,ArrayList<ResourcePane> resourceList,AssignedResource resource)
    {
        index=count;
        count++;

        this.idResource=resource.getIdResource();
        String resourceName=resource.getLabel();
        this.quantity=resource.getQuantity();

        setLayoutY((index*50)*scaley);
        getChildren().add(JavaFX.NewLabel(resourceName,18,50,0));
        resourcesCountField=JavaFX.NewTextField(18,75,380,0);
        resourcesCountField.setText(String.valueOf(quantity));
        resourcesCountField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    resourcesCountField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        Button delete=JavaFX.NewButton("Supprimer",lightOrange,15,460,3);

        delete.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
        {
            resourcePane.getChildren().clear();
            resourceList.remove(this);
            int i=0;
            for(ResourcePane rp:resourceList)
            {
                rp.index=i;
                rp.setLayoutY(i*50);
                resourcePane.getChildren().add(rp);
                i++;
            }
            count--;
        });
        getChildren().addAll(resourcesCountField,delete);
    }

    public int getIdResource()
    {
        return idResource;
    }

    public int getQuantity()
    {
        return Integer.valueOf(resourcesCountField.getText());
    }
}