package FrontEnd;

import BackEnd.User.User;
import FrontEnd.Criterion.CriterionInterface;
import FrontEnd.Portfolio.PortfolioInterface;
import FrontEnd.Project.ProjectInterface;
import FrontEnd.Resource.ResourceInterface;
import FrontEnd.User.UserInterface;
import Interface.JavaFX;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Manage
{
    private static Paint white=Paint.valueOf("FFFFFF");
    private static Paint grey=Paint.valueOf("303030");
    private static Paint lightGrey=Paint.valueOf("646464");
    private static Paint lightBlue=Paint.valueOf("11BAF8");
    private static Paint darkBlue=Paint.valueOf("0773BF");
    private static Paint darkerBlue=Paint.valueOf("09436E");

    //5096be
    //233F4E
    //202C33

    private static Pane Content;
    private static User user;

    private static Button evaluation,planification,monitoring;
    private static Button portfolios, projects, users, resources,criteria;

    public static void start(User user,double screenWidth, double screenHeight)
    {
        Manage.user=user;
        Stage primaryStage=new Stage();
        primaryStage.setTitle("Login");
        Pane layout = new Pane();

        //------------------------------------------TitleBar------------------------------------------------------------
        Pane titleBar=new Pane();
        titleBar.setLayoutX(0);
        titleBar.setLayoutY(0);
        titleBar.setPrefSize(screenWidth, screenHeight/20);
        titleBar.setBackground(new Background(new BackgroundFill(grey, CornerRadii.EMPTY, Insets.EMPTY)));

        titleBar.getChildren().add(JavaFX.NewLabel("     Project Portfolio \nManagement System",white,1, 18,2, 2));

        evaluation=	JavaFX.NewButton("Evaluation", lightBlue, 21, 192, 0, 192, 54);
        planification=	JavaFX.NewButton("Planification", darkBlue, 21, 2*192, 0, 192, 54);
        monitoring=		JavaFX.NewButton("Monitoring", darkBlue, 21, 3*192, 0, 192, 54);

        Button close=JavaFX.NewButton("X", lightBlue, 20, 1860,0, 60, 54);
        Button minimize= JavaFX.NewButton("─", lightGrey, 20, 1800,0, 60, 54);


        close.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> primaryStage.close());
        minimize.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> primaryStage.setIconified(true));

        titleBar.getChildren().addAll(evaluation,planification,monitoring);
        titleBar.getChildren().add(close);
        titleBar.getChildren().add(minimize);
        layout.getChildren().add(titleBar);

        //------------------------------------------Tabs----------------------------------------------------------------
        Pane tabsBar=new Pane();
        tabsBar.setLayoutX(0);
        tabsBar.setLayoutY(screenHeight/20);
        tabsBar.setPrefSize(screenWidth/10, screenHeight*19/20);
        tabsBar.setBackground(new Background(new BackgroundFill(darkerBlue, CornerRadii.EMPTY, Insets.EMPTY)));

        Image portfolioIcon = new Image("file:res/icon/portfolio/portfolio.png");
        Image projectIcon = new Image("file:res/icon/project/project.png");
        Image userIcon= new Image("file:res/icon/user/user.png");
        Image resourceIcon=new Image("file:res/icon/resource/resource.png");
        Image criterionIcon=new Image("file:res/icon/criteria/criteria.png");


        portfolios=	JavaFX.NewButton("Portefeuilles",portfolioIcon, ContentDisplay.TOP, lightBlue, 21, 0, 0, 192, 128);
        projects=	JavaFX.NewButton("Projets",	projectIcon,ContentDisplay.	TOP, darkBlue, 21, 0, 128, 192, 128);
        users=		JavaFX.NewButton("Utilisateurs",userIcon,ContentDisplay.TOP, darkBlue, 21, 0, 2*128, 192, 128);
        resources=	JavaFX.NewButton("Ressources",	resourceIcon,ContentDisplay.TOP, darkBlue, 21, 0, 3*128, 192, 128);
        criteria= 	JavaFX.NewButton("Critères",	criterionIcon,ContentDisplay.TOP, darkBlue, 21, 0, 4*128, 192, 128);

        tabsBar.getChildren().add(portfolios);
        tabsBar.getChildren().add(projects);
        tabsBar.getChildren().add(users);
        tabsBar.getChildren().add(resources);
        tabsBar.getChildren().add(criteria);

        portfolios.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(1));
        projects.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(2));
        users.		addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(3));
        resources.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(4));
        criteria.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(5));
        layout.getChildren().add(tabsBar);

        //------------------------------------------Content-------------------------------------------------------------
        Content=new Pane();
        Content.setLayoutX(screenWidth/10);
        Content.setLayoutY(screenHeight/35);
        PortfolioInterface portfolioInterface=new PortfolioInterface(user,0,0);
        Content.getChildren().add(portfolioInterface);

        layout.getChildren().add(Content);


        primaryStage.setScene(new Scene(layout,screenWidth,screenHeight));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    private static void setSelectedTab(int index)
    {
        Content.getChildren().clear();

        portfolios.  setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
        projects.    setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
        users.       setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
        resources.   setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
        criteria.	setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");

        switch (index)
        {
            case 1:
                portfolios.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                PortfolioInterface portfolioInterface=new PortfolioInterface(user,0,0);
                Content.getChildren().add(portfolioInterface);
                break;
            case 2:
                projects.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                ProjectInterface projectInterface=new ProjectInterface(user,0,0);
                Content.getChildren().add(projectInterface);
                break;
            case 3:
                users.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                UserInterface userInterface=new UserInterface(user,0,0);
                Content.getChildren().add(userInterface);
                break;
            case 4:
                resources.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                ResourceInterface resourceInterface=new ResourceInterface(user,0,0);
                Content.getChildren().add(resourceInterface);
                break;
            case 5:
                criteria.	setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                CriterionInterface criterionInterface=new CriterionInterface(user,0,0);
                Content.getChildren().add(criterionInterface);
                break;

        }
    }
    public static void setSelectedPhase(int index)
    {
        Content.getChildren().clear();
        evaluation.setStyle("-fx-base: #202C33;");
        planification.setStyle("-fx-base: #202C33;");
        monitoring.setStyle("-fx-base: #202C33;");

        switch(index)
        {
            case 0:
                evaluation.setStyle("-fx-base: #5096be;");
                break;
            case 1:
                planification.setStyle("-fx-base: #5096be;");
                break;
            case 2:
                monitoring.setStyle("-fx-base: #5096be;");
                break;
        }
    }

    public static User getUser()
    {
        return user;
    }
}
