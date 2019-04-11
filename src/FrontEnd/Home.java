package FrontEnd;

import Interface.JavaFX;
import FrontEnd.Criterion.CriterionInterface;
import FrontEnd.Portfolio.PortfolioInterface;
import FrontEnd.Project.ProjectInterface;
import FrontEnd.Resource.ResourceInterface;
import FrontEnd.User.UserInterface;
import javafx.application.Application;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.DriverManager;


public class Home  extends Application
{
    private static Connection connection;
    public static double scalex,scaley;

	static double screenWidth;
	static double screenHeight;

	private Paint white=Paint.valueOf("FFFFFF");
	private Paint grey=Paint.valueOf("303030");
	private Paint lightGrey=Paint.valueOf("646464");
	private Paint darkerBlue=Paint.valueOf("202C33");
	private Paint lightBlue=Paint.valueOf("5096be");
	private Paint darkBlue=Paint.valueOf("233F4E");

    private Pane Content;

    private Button evaluation,planification,monitoring;
    private Button portfolio, project, user, resource,criterion;

	public static void main(String[] args)
	{
		Screen screen = Screen.getPrimary();
		screenWidth=screen.getVisualBounds().getWidth();
		screenHeight=screen.getVisualBounds().getHeight();

		scalex = screenWidth/1920;
		scaley = screenHeight/1080;

		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:./priorisation.db");
		}
		catch(Exception e)	
		{
			e.printStackTrace();
		}
		launch(args);		
	}
	
	public void start(Stage stage)
	{
		stage.setTitle("Home");
		stage.initStyle(StageStyle.UNDECORATED);

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
		Button minimize=JavaFX.NewButton("─", lightGrey, 20, 1800,0, 60, 54);

		titleBar.getChildren().addAll(evaluation,planification,monitoring);
		titleBar.getChildren().add(close);
		titleBar.getChildren().add(minimize);

		close.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> stage.close());
		
		minimize.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> stage.setIconified(true));
		
		
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


		portfolio=	JavaFX.NewButton("Portefeuilles",portfolioIcon,ContentDisplay.TOP, lightBlue, 21, 0, 0, 192, 128);
		project=	JavaFX.NewButton("Projets",	projectIcon,ContentDisplay.	TOP, darkBlue, 21, 0, 128, 192, 128);
		user=		JavaFX.NewButton("Utilisateurs",userIcon,ContentDisplay.TOP, darkBlue, 21, 0, 2*128, 192, 128);
		resource=	JavaFX.NewButton("Ressources",	resourceIcon,ContentDisplay.TOP, darkBlue, 21, 0, 3*128, 192, 128);
		criterion= 	JavaFX.NewButton("Critères",	criterionIcon,ContentDisplay.TOP, darkBlue, 21, 0, 4*128, 192, 128);

		tabsBar.getChildren().add(portfolio);
		tabsBar.getChildren().add(project);
		tabsBar.getChildren().add(user);
		tabsBar.getChildren().add(resource);
		tabsBar.getChildren().add(criterion);

		portfolio.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(1));
		project.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(2));
		user.		addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(3));
		resource.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(4));
		criterion.	addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setSelectedTab(5));
		
		//------------------------------------------Content-------------------------------------------------------------
        Content=new Pane();
        Content.setLayoutX(screenWidth/10);
        Content.setLayoutY(screenHeight/35);
        PortfolioInterface portfolioInterface=new PortfolioInterface(0,0);
        Content.getChildren().add(portfolioInterface);

        layout.getChildren().addAll(titleBar,tabsBar,Content);
		
		Scene scene = new Scene (layout,screenWidth,screenHeight);
		stage.sizeToScene();
		stage.setScene(scene);
		stage.show();	
	}

	public static Connection getConnection()
	{
		return connection;
	}

	private void setSelectedTab(int index)
    {
        Content.getChildren().clear();

        portfolio.  setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
        project.    setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
        user.       setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
		resource.   setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");
		criterion.	setStyle("-fx-base: #"+darkBlue.toString().substring(2)+";");

        switch (index)
        {
            case 1:
                portfolio.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                PortfolioInterface portfolioInterface=new PortfolioInterface(0,0);
                Content.getChildren().add(portfolioInterface);
                break;
            case 2:
                project.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                ProjectInterface projectInterface=new ProjectInterface(0,0);
                Content.getChildren().add(projectInterface);
                break;
            case 3:
                user.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
                UserInterface userInterface=new UserInterface(0,0);
                Content.getChildren().add(userInterface);
                break;
            case 4:
				resource.setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
				ResourceInterface resourceInterface=new ResourceInterface(0,0);
				Content.getChildren().add(resourceInterface);
				break;
			case 5:
				criterion.	setStyle("-fx-base: #"+lightBlue.toString().substring(2)+";");
				CriterionInterface criterionInterface=new CriterionInterface(0,0);
				Content.getChildren().add(criterionInterface);
				break;

		}
    }
}
