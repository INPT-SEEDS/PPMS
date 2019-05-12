package Interface;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public 	class Windowclose extends FlowPane {
    public Windowclose(Stage stage) {
        HBox h = new HBox();
        Label text=new Label("Fermer");
        Label cls = new Label("X");
        text.setTextFill(Color.BLACK);
        text.setStyle("-fx-font: 15 vedrana");
        cls.setTextFill(Color.BLACK);
        cls.setStyle("-fx-font: 20 verdana;-fx-font-weight: Bold; -fx-backgound-color: red;");
        h.setPadding(new Insets(3,3,3,3));

            h.getChildren().addAll(text,cls);

        //Coloring label with RED when the mouse cursor is on
        h.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        cls.setTextFill(Color.RED);
                        text.setTextFill(Color.RED);
                    }
                });
        //Removing the RED when the mouse cursor is off
        h.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        cls.setTextFill(Color.BLACK);
                        text.setTextFill(Color.BLACK);
                    }
                });
        /*h.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent ae) {
                                    if(Action.equals("Log out")) {
                                        stage.close();
                                        Login L = new Login();
                                        Stage s = new Stage();
                                        try {
                                            L.start(s);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        stage.close();
                                    }
                                }
                            }
        );*/
        this.getChildren().addAll(h);
        this.setAlignment(Pos.CENTER_RIGHT);
    }
}