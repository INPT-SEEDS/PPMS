package FrontEnd.Resource;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.ResToPortfolio.ResToPortfolioQueries;
import Interface.JavaFX;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.sql.Timestamp;
import java.util.List;

public class AssignToPortfolio extends Pane
{
    private Paint black=Paint.valueOf("000000");

    AssignToPortfolio(int resId,double x,double y,ResourceInterface parent)
    {
        setLayoutX(x);
        setLayoutY(y);

        List<String> portfolios= PortfolioQueries.getPortfoliosRef();
        Button bar=JavaFX.NewButton("",black,18,120,31,10,125);
        ComboBox portfoliosBox= JavaFX.NewComboBox(portfolios,200,25,40);
        TextField numberField= JavaFX.NewTextField(18,100,75,80);
        Button confirm=JavaFX.NewButton("Confirmer",18,70,120);

        confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
        {
            int porId=portfoliosBox.getSelectionModel().getSelectedIndex();
            ResToPortfolioQueries.resetTable(porId,resId);
            String date=new Timestamp(60*1000*(System.currentTimeMillis()/(1000*60))).toString();
            int quantity=Integer.valueOf(numberField.getText());
            ResToPortfolioQueries.addToDatabase(porId,resId,date.substring(0,date.length()-5),quantity);
            parent.editing=false;
            this.getChildren().clear();
        });

        getChildren().addAll(bar,portfoliosBox,numberField,confirm);
    }
}
