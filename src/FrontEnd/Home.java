package FrontEnd;

import BackEnd.User.User;
import Interface.JavaFX;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Home
{

	private static Paint white=Paint.valueOf("FFFFFF");
	private static Paint grey=Paint.valueOf("303030");
	private static Paint lightGrey=Paint.valueOf("646464");
	private static Paint darkerBlue=Paint.valueOf("202C33");
	private static Paint lightBlue=Paint.valueOf("14E1FF");
	private static Paint darkBlue=Paint.valueOf("233F4E");

	public static void start(User user, double screenWidth, double screenHeight)
	{
		Stage stage=new Stage();
		stage.setTitle("Home");
		Pane layout =new Pane();

		//------------------------------------------TitleBar------------------------------------------------------------
		Pane titleBar=new Pane();
		titleBar.setLayoutX(0);
		titleBar.setLayoutY(0);
		titleBar.setPrefSize(screenWidth, screenHeight/30);
		titleBar.setBackground(new Background(new BackgroundFill(grey, CornerRadii.EMPTY, Insets.EMPTY)));
		titleBar.getChildren().add(JavaFX.NewLabel("  Project Portfolio Management System",white,1, 25,2, 2));

		Button close=JavaFX.NewButton("X", lightBlue, 18, 1860,0, 60, 36);
		Button minimize= JavaFX.NewButton("─", lightGrey, 18, 1800,0, 60, 36);
		close.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> stage.close());
		minimize.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> stage.setIconified(true));

		titleBar.getChildren().addAll(close,minimize);
		layout.getChildren().add(titleBar);

		Scene scene = new Scene (layout,screenWidth,screenHeight);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}
}
