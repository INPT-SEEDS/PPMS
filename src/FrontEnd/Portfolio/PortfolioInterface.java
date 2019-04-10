package FrontEnd.Portfolio;

import BackEnd.Portfolio.PortfolioQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class PortfolioInterface extends Pane
{
	static double scalex = Home.scalex;
	static double scaley = Home.scaley;

	private Paint black=Paint.valueOf("000000");
	private Paint red=Paint.valueOf("F04040");
	private Paint lightOrange=Paint.valueOf("F77D50");
	private Paint lightBlue=Paint.valueOf("5096be");
	private Paint lightGreen=Paint.valueOf("50be96");

	private Button bar,viewProject,modify, evaluate;
	private Button add;

	private TableView tvPortfolio;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PortfolioInterface(double x,double y)
	{
		this.setLayoutX(x*scalex);
		this.setLayoutY(y*scaley);
				
		Image showIcon= new Image("file:res/icon/portfolio/ViewProject.png");
		Image modifyIcon= new Image("file:res/icon/portfolio/Modify.png");
		Image evaluateIcon=new Image("file:res/icon/portfolio/Evaluate.png");

		//-Portolio-Table---------------------------------------------------------------------------------------------------------------------------------------
		bar=JavaFX.NewButton("",black,2, 1080, 95,500,10);
		viewProject=JavaFX.NewButton("Voir les projets", showIcon, ContentDisplay.LEFT, lightOrange, 16,1110 ,95 , 200, 32);
		modify=JavaFX.NewButton("Modifier", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1320 ,95 , 130, 32);
		evaluate =JavaFX.NewButton("Prioriser et Optimiser", evaluateIcon, ContentDisplay.LEFT, lightGreen, 16,1460 ,95 , 225, 32);
		
		getChildren().add(bar);
		getChildren().add(viewProject);
		getChildren().add(modify);
		getChildren().add(evaluate);

		viewProject.setVisible(false);
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
					setActionBar(true, (int) (95+row.getIndex()*25/scaley));
					additionalOptions.getChildren().clear();
				}
			});
			return row;
		});

		getChildren().add(tvPortfolio);

		evaluate.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActive(false);
			setActionBar(false,0);
			Object row=tvPortfolio.getSelectionModel().getSelectedItems().get(0);
			int IdPortfolio = Integer.valueOf(row.toString().split(",")[0].substring(1));
			PortfolioTreat portfolioTreat=new PortfolioTreat(this,IdPortfolio);
			additionalOptions.getChildren().add(portfolioTreat);
		});

		additionalOptions.setLayoutX(1125*scalex);
		additionalOptions.setLayoutY(75*scaley);
		modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActive(false);
			setActionBar(false,0);
			PortfolioModify portfolioModify=new PortfolioModify(this,0);
			additionalOptions.getChildren().add(portfolioModify);
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
			setActive(false);
			setActionBar(false,0);
			addPane.getChildren().addAll(addBar,idField,refField,libField,confirm,cancel);
			tvPortfolio.getSelectionModel().clearSelection();
		});
		confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(idField.getText());
			String ref=refField.getText();
			String label=libField.getText();
			PortfolioQueries.addToDatabase(id,ref,label);
			refreshTable();
			addPane.getChildren().clear();
			setActive(true);
		});

		cancel.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			addPane.getChildren().clear();
			setActive(true);
		});

		getChildren().add(addPane);
	}

	private void setActionBar(boolean visbility,int position)
	{
		viewProject.setLayoutY(position*scaley);
		evaluate.setLayoutY(position*scaley);
		modify.setLayoutY(position*scaley);
		bar.setLayoutY((position+11)*scaley);

		viewProject.setVisible(visbility);
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