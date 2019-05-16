package FrontEnd.DatabaseManagement.Portfolio;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.User.User;
import BackEnd.Utility;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class PortfolioInterface extends Pane
{
	static double scalex = Login.scalex;
	static double scaley = Login.scaley;

	private Paint black=Paint.valueOf("000000");
	private Paint red=Paint.valueOf("F04040");
	private Paint lightOrange=Paint.valueOf("F77D50");
	private Paint lightBlue=Paint.valueOf("11BAF8");
	private Paint lightGreen=Paint.valueOf("50be96");

	private Button bar,modify, evaluate;
	private Button add;

	private TableView tvPortfolio;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PortfolioInterface(User user, double x, double y)
	{
		this.setLayoutX(x*scalex);
		this.setLayoutY(y*scaley);

		Image showIcon= new Image("file:res/icon/portfolio/ViewProject.png");
		Image modifyIcon= new Image("file:res/icon/portfolio/Modify.png");
		Image evaluateIcon=new Image("file:res/icon/portfolio/Evaluate.png");

		//-Portolio-Table---------------------------------------------------------------------------------------------------------------------------------------
		bar=JavaFX.NewButton("",black,2, 1080, 85,350,10);
		modify=JavaFX.NewButton("Modifier", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,74 , 130, 32);
		evaluate =JavaFX.NewButton("Prioriser et Optimiser", evaluateIcon, ContentDisplay.LEFT, lightGreen, 16,1250 ,74 , 225, 32);

		getChildren().add(bar);
		getChildren().add(modify);
		getChildren().add(evaluate);

		modify.setVisible(false);
		evaluate.setVisible(false);
		bar.setVisible(false);
		
		tvPortfolio=JavaFX.NewTableView(PortfolioQueries.getResultSet(), 100,75, 1000, 850);
		Pane additionalOptions=new Pane();

		tvPortfolio.setRowFactory(tv->
		{
			TableRow row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					setActionBar(true);
					additionalOptions.getChildren().clear();
				}
			});
			return row;
		});

		getChildren().add(tvPortfolio);

		evaluate.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActive(false);
			setActionBar(false);
			Object row=tvPortfolio.getSelectionModel().getSelectedItems().get(0);
			int IdPortfolio = Integer.valueOf(row.toString().split(",")[0].substring(1));
			PortfolioTreat portfolioTreat=new PortfolioTreat(this,IdPortfolio);
			additionalOptions.getChildren().add(portfolioTreat);
		});

		additionalOptions.setLayoutX(1125*scalex);
		additionalOptions.setLayoutY(75*scaley);
		modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(2))
			{
				setActive(false);
				setActionBar(false);
				int portfolioId=Integer.valueOf(Utility.getSelectedRowColumn(tvPortfolio,0));
				PortfolioModify portfolioModify=new PortfolioModify(this, portfolioId);
				additionalOptions.getChildren().add(portfolioModify);
			}
			else JavaFX.privilegeAlert();
		});
		getChildren().add(additionalOptions);

		//-Add-Portolio------------------------------------------------------------------------------------------------------------------------------------------
		add=JavaFX.NewButton("Ajouter",18,7,950);
		getChildren().add(add);

		Pane addPane=new Pane();

		TextField idField=JavaFX.NewTextField(18,333,100,950);
		idField.setText(String.valueOf(tvPortfolio.getItems().size()));
		idField.setEditable(false);
		TextField refField=JavaFX.NewTextField(18,333,433,950);
		TextField libField=JavaFX.NewTextField(18,333,766,950);
		Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
		Button cancel=JavaFX.NewButton("Annuler",red,18,1230,950);
		Button addBar=JavaFX.NewButton("",black,2, 93, 961,1200,10);

		add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(2))
			{
				setActive(false);
				setActionBar(false);
				addPane.getChildren().addAll(addBar,idField,refField,libField,confirm,cancel);
				idField.setText(String.valueOf(tvPortfolio.getItems().size()));
				tvPortfolio.getSelectionModel().clearSelection();
			}
			else JavaFX.privilegeAlert();

		});
		confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(idField.getText());
			String ref=refField.getText();
			String label=libField.getText();
			if(ref.length()>0 && label.length()>0)
			{
				PortfolioQueries.addToDatabase(id,ref,label);
				refreshTable();
				refField.clear();libField.clear();
				addPane.getChildren().clear();
				setActive(true);
			}
			else
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText(null);
				alert.setContentText("Veuillez remplir tous les champs");
				alert.showAndWait();
			}

		});

		cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			addPane.getChildren().clear();
			setActive(true);
		});

		getChildren().add(addPane);
	}

	private void setActionBar(boolean visbility)
	{
		evaluate.setVisible(visbility);
		modify.setVisible(visbility);
		bar.setVisible(visbility);
	}

	public void resetSelection()
	{
		setActive(true);
		tvPortfolio.getSelectionModel().clearSelection();
	}

	private void setActive(boolean bool)
	{
		add.setDisable(!bool);
		tvPortfolio.setDisable(!bool);
	}

	public void refreshTable()
	{
		JavaFX.updateTable(tvPortfolio,PortfolioQueries.getResultSet());
	}
}