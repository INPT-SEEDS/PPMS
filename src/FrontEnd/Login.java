package FrontEnd;

import BackEnd.User.User;
import BackEnd.User.UserQueries;
import BackEnd.Utility;
import Interface.JavaFX;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.DriverManager;

import static BackEnd.Queries.connect;


public class Login extends Application
{
    private static Connection connection;
    public static double scalex,scaley;
    public static int idUser;

    private Stage stage;

    static double screenWidth;
    static double screenHeight;

    private Paint white=Paint.valueOf("FFFFFF");
    private Paint grey=Paint.valueOf("303030");
    private Paint lightGrey=Paint.valueOf("646464");
    private Paint darkerBlue=Paint.valueOf("202C33");
    private Paint lightBlue=Paint.valueOf("5096be");
    private Paint darkBlue=Paint.valueOf("233F4E");

    private static Pane Content;

    private boolean messageShown;

    private static Button evaluation,planification,monitoring;
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
        //-------------Form
        float fieldX = (float) (400 * scalex);
        float fieldY = (float) (25 * scaley);

        Label l1 = new Label("  ");
        l1.setStyle("-fx-font-family: Montserrat;-fx-font-weight: bold;-fx-font-size: 10;");
        Label l2 = new Label("Nom d'utilisateur");
        Label l3 = new Label("Mot de passe      ");
        Label l5 = new Label("Confirmer           ");
        l2.setStyle("-fx-font-family: Montserrat;-fx-font-size: 20;");

        TextField username = new TextField();
        username.setPromptText("Votre nom d'utilisateur...");
        username.setMinSize(fieldX, fieldY);
        username.setStyle("-fx-background-color: transparent;-fx-border-width: 0 0 1 0;-fx-font-size: 16;-fx-text-fill: black;-fx-prompt-text-fill: #005492;");


        l3.setStyle("-fx-font-family: Montserrat;-fx-font-size: 20;");
        PasswordField passwd = new PasswordField();
        passwd.setPromptText("Votre mot de passe...");
        passwd.setMinSize(fieldX, fieldY);
        passwd.setStyle("-fx-background-color: transparent;-fx-text-fill: black;-fx-font-size: 16;-fx-prompt-text-fill: #005492;");


        l5.setStyle("-fx-font-family: Montserrat;-fx-font-size: 20;");
        PasswordField passwd2 = new PasswordField();
        passwd2.setPromptText("Confirmer votre mot de passe");
        passwd2.setMinSize(fieldX, fieldY);
        passwd2.setStyle("-fx-background-color: transparent;-fx-text-fill: black;-fx-font-size: 16;-fx-prompt-text-fill: #005492;");

        Label l4 = new Label("");
        l4.setStyle("-fx-font-size: 15;");
        l4.setAlignment(Pos.CENTER);

        ImageView imageView =new ImageView ("file:res/icon/login/login.png");
        Button logIn = JavaFX.Bwsh("Log in", imageView);
        logIn.setStyle(" -fx-padding: 8 15 15 15;" +
                "    -fx-background-insets: 0,0 0 3 0, 0 0 4 0, 0 0 5 0;" +
                "    -fx-background-radius: 8;" +
                "    -fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #09436e 0%, #183c56 100%)," +
                "        #055590," +
                "        #1478c2," +
                "        radial-gradient(center 50% 50%, radius 100%, #0f73bd, #095eb1);" +
                "    -fx-font-weight: bold;");
        logIn.setTextFill(Paint.valueOf("#FFFFFF"));
        logIn.setMinSize(80, 40);

        //Icons
        ImageView un = new ImageView ("file:res/icon/login/un.png");
        un.setFitHeight(fieldY);
        un.setFitWidth(fieldY);
        ImageView impw = new ImageView ("file:res/icon/login/pwd.png");
        impw.setFitHeight(fieldY);
        impw.setFitWidth(fieldY);
        ImageView impw2 = new ImageView ("file:res/icon/login/pwd.png");
        impw2.setFitHeight(fieldY);
        impw2.setFitWidth(fieldY);

        //Layout
        HBox h1 = new HBox();
        h1.setSpacing(10);
        h1.getChildren().addAll(un, l2, username);
        h1.setAlignment(Pos.CENTER);
        HBox h2 = new HBox();
        h2.setSpacing(10);
        h2.getChildren().addAll(impw, l3, passwd);
        h2.setAlignment(Pos.CENTER);
        HBox h3 = new HBox();
        h3.setSpacing(10);
        h3.getChildren().addAll(impw2, l5, passwd2);
        h3.setAlignment(Pos.CENTER);
        h3.setVisible(false);

        VBox v1 = new VBox();
        v1.setPadding(new Insets(50, 20, 20, 20));
        v1.setAlignment(Pos.CENTER);
        v1.setSpacing(10);
        v1.getChildren().addAll(l1,h1, h2,h3,logIn, l4);
        messageShown=false;
        connection = connect();

        logIn.setDefaultButton(true);

        logIn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                logIn.setText("En cours de connexion...");
                logIn.setDisable(true);
                if (connection == null)
                {
                    l4.setText("Erreur de connexion avec la base de données");
                    l4.setTextFill(Color.RED);
                    logIn.setTextFill(Color.BROWN);
                    logIn.setText("Log in");
                    logIn.setDisable(false);
                }
                else
                {
                    User user= UserQueries.getUserByUsername(username.getText());

                    logIn.setDisable(false);

                    if(user==null)
                    {
                        l4.setText("Cette utilisateur n'existe pas.");
                        l4.setTextFill(Color.RED);
                        logIn.setText("Log in");
                        username.clear();
                        passwd.clear();
                    }
                    else
                    {
                        username.setDisable(true);
                        String password= Utility.EncryptPassword(passwd.getText());
                        if(user.getPassword().equals("****"))
                        {
                            logIn.setText("Mettre à jour le mot de pass");
                            if(messageShown)
                            {
                                if (passwd.getText().length() < 8)
                                {
                                    l4.setText("Le mot de passe doit comporter au moins 8 caractères.");
                                    l4.setTextFill(Color.RED);
                                    passwd.clear();
                                    passwd2.clear();
                                }
                                else if(!passwd.getText().equals(passwd2.getText()))
                                {
                                    l4.setText("Les 2 mots de passe ne sont pas identiques");
                                    l4.setTextFill(Color.RED);
                                }
                                else
                                {
                                    UserQueries.updatePassword(user.getId(),password);
                                    l4.setText("Mot de passe mis à jour avec succès");
                                    l4.setTextFill(Color.rgb(15,75,0));
                                    h3.setVisible(false);
                                    logIn.setText("Log in");
                                    username.setDisable(false);

                                    username.clear();
                                    passwd.clear();
                                    passwd2.clear();
                                }
                            }
                            else
                            {
                                l4.setText("         Vous êtes un nouvel utilisateur.\n Veuillez créer un nouveau mot de passe");
                                l4.setTextFill(Color.rgb(15,75,0));
                                h3.setVisible(true);
                                passwd.clear();
                                messageShown=true;
                            }

                        }
                        else if(user.getPassword().equals(password))
                        {
                            passwd.setDisable(true);
                            logIn.setText("Connecté avec succès");
                            l4.setText("");
                            stage.close();
                            Manage.start(user,screenWidth,screenHeight);
                        }
                        else
                        {
                            username.setDisable(false);
                            l4.setText("Mot de passe incorrect.");
                            l4.setTextFill(Color.RED);
                            logIn.setText("Log in");
                            logIn.setDisable(false);
                            passwd.clear();
                        }

                    }
                }
            }
        });

        FlowPane CloseB=JavaFX.windowclose();
        CloseB.setAlignment(Pos.CENTER_RIGHT);
        CloseB.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> stage.close());

        BorderPane bp = new BorderPane();
        Background bgr = new Background(new BackgroundFill(new ImagePattern(new Image("/res/icon/login/bgs.png")),
                CornerRadii.EMPTY, Insets.EMPTY));
        bp.setBackground(bgr);
        bp.setTop(CloseB);
        bp.setCenter(v1);

        //Scene
        Scene sc = new Scene(bp, 700 * scalex, 400 * scaley);
        stage.setScene(sc);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

        /*Pane p=new Pane();
        p.getChildren().add(JavaFX.NewLabel("WOOOW",18,0,0));
        primaryStage.setScene(new Scene(p,100,100));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();*/

    }

    public static Connection getConnection()
    {
        return connection;
    }
}
