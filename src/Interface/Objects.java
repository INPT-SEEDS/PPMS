package Interface;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.sql.ResultSet;

public class Objects {

    static float scalex = 1;
    static float scaley = 1;

    // -------------------NewLabel--------------------------------------------------------------------------------------
    public static Label NewLabel(String text, Color color, int size, int x, int y)
    {
        Label label = new Label(text);
        label.setFont(new Font("Century Gothic", (int) (scalex * size)));
        label.setTextFill(color);
        label.setLayoutX(scalex*x);
        label.setLayoutY(scaley*y);
        return label;
    }

    public static Label NewLabel(String text, int size, int x, int y)
    {
        return NewLabel(text, Color.BLACK, size, x, y);
    }


    // --------------------NewButton------------------------------------------------------------------------------------
    public static Button NewButton(String text, Color color, int size,int x, int y)
    {
        Button button = new Button(text);
        button.setStyle("-fx-base: rgb(" + color.getRed()*255 + "," + color.getGreen()*255  + ", " + color.getBlue()*255  + ");");
        button.setFont(new Font("Century Gothic", (int) (scalex * size)));
        button.setTextFill(Color.WHITE);
        button.setLayoutX(scalex*x);
        button.setLayoutY(scaley*y);

        return button;
    }

    public static Button NewButton(String test, Color color, int x, int y)
    {
        return NewButton(test, color, 20, x, y);
    }

    public static Button NewButton(String text, int size,int x, int y)
    {
        return NewButton(text,Color.rgb(224, 116, 60), size, x, y);
    }

    public static Button NewButton(String test, int x, int y)
    {
        return NewButton(test, 20, x, y);
    }

    // ----------------------NewTextPane--------------------------------------------------------------------------------
    public static TextField NewTextField(int size,int width, int x, int y)
    {
        TextField textField = new TextField();
        textField.setFont(new Font("Century Gothic",(int) (scalex *size )));
        textField.setPrefWidth(width);
        textField.setLayoutX(scalex*x);
        textField.setLayoutY(scaley*y);
        return textField;
    }

    public static PasswordField NewPasswordField(int size,int width, int x, int y)
    {
        PasswordField passwordField = new PasswordField();
        passwordField.setFont(new Font("Century Gothic", (int) (scalex * size)));
        passwordField.setPrefWidth(width);
        passwordField.setLayoutX(scalex*x);
        passwordField.setLayoutY(scaley*y);
        return passwordField;
    }

    // -------------------NewImage--------------------------------------------------------------------------------------
    public static ImageView NewImage(String path, int w, int h, int x, int y)
    {
        Image image=new Image("file:"+path);
        ImageView imageview=new ImageView();
        imageview.setImage(image);
        imageview.setFitWidth(scalex*w);
        imageview.setFitHeight(scaley*h);
        imageview.setLayoutX(scalex*x);
        imageview.setLayoutY(scaley*y);

        return imageview;
    }

    public static ImageView NewImage(String path, int x, int y)
    {
        Image image=new Image("file:"+path);
        return NewImage(path, (int)image.getWidth(),  (int)image.getHeight(), x, y);
    }

    // --------------------NewComboBox----------------------------------------------------------------------------------
    public static ComboBox<String> NewComboBox(String[] list, int w, int x, int y)
    {
        ObservableList<String> observableList=FXCollections.observableArrayList(list);
        ComboBox<String> comboBox = new ComboBox<>(observableList);
        comboBox.setStyle("-fx-font: "+(int) (scalex * 18)+"px \"Century Gothic\";");

        comboBox.setPrefWidth(scalex*w);

        comboBox.setLayoutX(scalex*x);
        comboBox.setLayoutY(scaley*y);
        return comboBox;
    }

    public static ComboBox<String> NewComboBox(String[] list, int x, int y)
    {
        return NewComboBox(list, 350, x, y);
    }

    // --------------------NewCheckBox----------------------------------------------------------------------------------

    public static CheckBox NewCheckBox(String text,int x,int y)
    {

        CheckBox checkBox = new CheckBox(text);
        checkBox.setFont(new Font("Century Gothic", (int) (scalex * 18)));
        checkBox.setLayoutX(scalex*x);
        checkBox.setLayoutY(scaley*y);
        return checkBox;
    }

    // --------------------NewTableView----------------------------------------------------------------------------------
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TableView NewTableView(ResultSet rs)
    {
        ObservableList<ObservableList> data;
        TableView tableview=new TableView();
        data = FXCollections.observableArrayList();
        try
        {
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++)
            {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                col.setCellValueFactory(
                        (Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                                new SimpleStringProperty(param.getValue().get(j).toString()));
                tableview.getColumns().addAll(col);
            }

            while (rs.next())
            {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            tableview.setItems(data);
            return tableview;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
