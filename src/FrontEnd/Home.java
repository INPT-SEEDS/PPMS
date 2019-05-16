package FrontEnd;

import BackEnd.User.User;
import BackEnd.User.UserQueries;
import FrontEnd.PortfolioPriorisation.PortfolioPriorisation;
import FrontEnd.ProjectEvaluation.ProjectEvaluation;
import FrontEnd.ResourceManagement.ResourceManagement;
import Interface.JavaFX;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.Optional;

public class Home
{
	public static Pane Options;

	private static Paint white=Paint.valueOf("FFFFFF");
	private static Paint grey=Paint.valueOf("303030");
	private static Paint lightGrey=Paint.valueOf("646464");
	private static Paint ProColor=Paint.valueOf("6699CC");
	private static Paint PortColor=Paint.valueOf("09C7FA");
	private static Paint lightBlue=Paint.valueOf("85E0FB");
	//ED7561
	private static Paint ResColor=Paint.valueOf("09C7FA");

	public static void start(User user, double screenWidth, double screenHeight)
	{
		Stage stage=new Stage();
		stage.setTitle("Home");
		Pane layout =new Pane();
		layout.getChildren().add(new ImageView("file:res/icon/background.png"));

		//------------------------------------------TitleBar------------------------------------------------------------
		Pane titleBar=new Pane();
		titleBar.setLayoutX(0);
		titleBar.setLayoutY(0);
		titleBar.setPrefSize(screenWidth, screenHeight/30);
		titleBar.setBackground(new Background(new BackgroundFill(grey, CornerRadii.EMPTY, Insets.EMPTY)));
		titleBar.getChildren().add(JavaFX.NewLabel("  Project Portfolio Management System",white,1, 22,2, 4));

		FlowPane CloseB=JavaFX.windowclose(Color.WHITE);
		CloseB.setAlignment(Pos.CENTER_RIGHT);
		CloseB.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent ->
		{
			new File("Branch.xml").delete();
			stage.close();
		});
		CloseB.setLayoutX(screenHeight-118);
		CloseB.setLayoutY(1);
		layout.getChildren().addAll(titleBar,CloseB);


		Options=new Pane();
		ImageView Port=JavaFX.NewImageButton("Por",300,200,200,200);
		ImageView Pro=JavaFX.NewImageButton("Pro",300,200,505,200);
		ImageView Res=JavaFX.NewImageButton("Res",300,200,810,200);
		ImageView DB=JavaFX.NewImageButton("DB",615,125,350,405);
		Options.getChildren().addAll(Port,Pro,Res,DB);
		layout.getChildren().add(Options);

		Pane Content=new Pane();
		layout.getChildren().add(Content);

		Port.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(2))
			{
				Options.setVisible(false);
				Content.getChildren().add(new PortfolioPriorisation());
			}
			else{JavaFX.privilegeAlert();}
		});
		Pro.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(1))
			{
				Options.setVisible(false);
				Content.getChildren().add(new ProjectEvaluation(user));
			}
			else{JavaFX.privilegeAlert();}

		});
		Res.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(3))
			{
				Options.setVisible(false);
				Content.getChildren().add(new ResourceManagement());
			}
			else{JavaFX.privilegeAlert();}

		});

		ImageView passwordChange=JavaFX.NewImage("res/icon/Password.png",300,30,1000,700);
		Image org=passwordChange.getImage();
		ImageView hovered=JavaFX.NewImage("res/icon/PasswordHovered.png",300,30,0,0);
		passwordChange.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> passwordChange.setImage(hovered.getImage()));
		passwordChange.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent ->passwordChange.setImage(org));

		passwordChange.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Attention");
			alert.setContentText("Vous serez déconnecté afin de changer votre mot de passe, êtes-vous sûr de vouloir continuer ?");
			alert.setHeaderText(null);
			Optional<ButtonType> result = alert.showAndWait();
			if (ButtonType.OK == result.get())
			{
				UserQueries.updatePassword(user.getId(),"****");
				Platform.exit();
			}
		});

		Options.getChildren().add(passwordChange);

		Scene scene = new Scene (layout,screenWidth*0.7,screenHeight*0.7);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

}