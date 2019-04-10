package FrontEnd.Project;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.Project.ProjectQueries;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import BackEnd.ProjectType.ProjectTypeQueries;
import FrontEnd.Home;
import Interface.JavaFX;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;

import static BackEnd.Utility.getDatetime;

public class ProjectInterface extends Pane
{
	static double scalex = Home.scalex;
	static double scaley = Home.scaley;

	private Paint black=Paint.valueOf("000000");
	private Paint red=Paint.valueOf("F04040");
	private Paint lightOrange=Paint.valueOf("F77D50");
	private Paint lightBlue=Paint.valueOf("5096be");
	private Paint lightGreen=Paint.valueOf("50be96");

	private Button bar,viewPortfolio,modify,evaluate;
	private Button bar2,modify2;
	private Button add,add2;

	private TableView tvProject,tvType;
	private ComboBox typeField ,porField;

	@SuppressWarnings({ "unchecked","rawtypes" })
	public ProjectInterface(double x,double y)
	{
		this.setLayoutX(x*scalex);
		this.setLayoutY(y*scaley);

		Image viewPortfolioIcon= new Image("file:res/icon/portfolio/Modify.png");
		Image modifyIcon= new Image("file:res/icon/portfolio/Modify.png");
		Image evaluateIcon=new Image("file:res/icon/portfolio/Evaluate.png");

		bar=JavaFX.NewButton("",black,2, 780, 95,800,10);
		viewPortfolio=JavaFX.NewButton("Voir le portefeuille", viewPortfolioIcon, ContentDisplay.LEFT, lightOrange, 16,1110 ,95 , 220, 32);
		modify=JavaFX.NewButton("Modifier", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1340 ,95 , 130, 32);
		evaluate=JavaFX.NewButton("Evaluer", evaluateIcon, ContentDisplay.LEFT, lightGreen, 16,1480 ,95 , 125, 32);


		getChildren().add(bar);
		getChildren().add(viewPortfolio);
		getChildren().add(modify);
		getChildren().add(evaluate);

		viewPortfolio.setVisible(false);
		modify.setVisible(false);
		evaluate.setVisible(false);
		bar.setVisible(false);

		tvProject=JavaFX.NewTableView(ProjectQueries.getResultSet(), 100,75, 1000, 400);

		Pane additionalOptions=new Pane();
		additionalOptions.setLayoutX(1125*scalex);
		additionalOptions.setLayoutY(75*scaley);
		modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			Object row=tvProject.getSelectionModel().getSelectedItems().get(0);
			int idProject = Integer.valueOf(row.toString().split(",")[0].substring(1));
			ProjectModify projectModify=new ProjectModify(this,idProject);
			additionalOptions.getChildren().add(projectModify);
		});

		evaluate.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			Object row=tvProject.getSelectionModel().getSelectedItems().get(0);
			int idProject = Integer.valueOf(row.toString().split(",")[0].substring(1));
			int idPortfolio = Integer.valueOf(row.toString().split(",")[2].substring(1));
			ProjectEvaluate projectEvaluate =new ProjectEvaluate(this,idProject,idPortfolio);
			additionalOptions.getChildren().add(projectEvaluate);
		});

		tvProject.setRowFactory(tv->
		{
			TableRow<Object> row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					additionalOptions.getChildren().clear();
					setActionBar(true,(int) (95+row.getIndex()*25/scaley));
					setActionBar2(false,0);
					tvType.getSelectionModel().clearSelection();
				}
			});
			return row;
		});

		getChildren().add(tvProject);
		getChildren().add(additionalOptions);

		add=JavaFX.NewButton("Ajouter",18,7,482);
		getChildren().add(add);

		//-Add-Project-------------------------------------------------------------------------------------------------------------------------------------
		Pane addPane=new Pane();
		List<String> portfolios= PortfolioQueries.getPortfoliosRef();
		List<String> types=ProjectTypeQueries.getProjectTypesRef();

		TextField idField=JavaFX.NewTextField(18,250,100,482);
		idField.setEditable(false);
		TextField refField=JavaFX.NewTextField(18,250,350,482);
		porField=JavaFX.NewComboBox(portfolios,250,600,482);
		typeField=JavaFX.NewComboBox(types,250,850,482);

		Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
		Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
		Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

		add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			idField.setText(String.valueOf(tvProject.getItems().size()));
			setActionBar(false,0);
			setActionBar2(false,0);
			tvProject.getSelectionModel().clearSelection();
			setActive(false);
			addPane.getChildren().addAll(addBar,idField,refField,porField,typeField,confirm,cancel);
			tvProject.getSelectionModel().clearSelection();
			tvType.getSelectionModel().clearSelection();
		});

		confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(idField.getText());
			String ref=refField.getText();
			int porId=porField.getSelectionModel().getSelectedIndex();
			int typeId=typeField.getSelectionModel().getSelectedIndex();
			ProjectQueries.addToDatabase(id,ref,porId,typeId);
			ProjectStatueQueries.addToDatabase(id,"Non Evalué",0,getDatetime());
			refField.setText("");
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

		//-ProjectType-Table---------------------------------------------------------------------------------------------------------------------------------
		bar2=JavaFX.NewButton("",black,2, 780, 95,550,10);
		modify2=JavaFX.NewButton("Modifier la Catégorie", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,95 , 240, 32);

		getChildren().add(bar2);
		getChildren().add(modify2);

		setActionBar2(false,0);

		tvType=JavaFX.NewTableView(ProjectTypeQueries.getResultSet(), 100,525, 1000, 420);

		Pane additionalOptions2=new Pane();
		additionalOptions2.setLayoutX(1125*scalex);
		additionalOptions2.setLayoutY(525*scaley);
		modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			setActive(false);
			CategoryModify categoryModify=new CategoryModify(this,0);
			additionalOptions2.getChildren().add(categoryModify);
		});
		getChildren().add(additionalOptions2);

		tvType.setRowFactory(tv->
		{
			TableRow<Object> row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					additionalOptions2.getChildren().clear();
					setActionBar2(true,(int) (545+row.getIndex()*25/scaley));
					setActionBar(false,0);
					tvProject.getSelectionModel().clearSelection();
				}
			});
			return row;
		});

		getChildren().add(tvType);
		//-Add-ProjectType---------------------------------------------------------------------------------------------------------------------------------
		add2=JavaFX.NewButton("Ajouter",18,7,950);
		getChildren().add(add2);

		Pane addPane2=new Pane();

		TextField id2Field=JavaFX.NewTextField(18,333,100,950);
		id2Field.setText(String.valueOf(tvType.getItems().size()));
		id2Field.setEditable(false);
		TextField ref2Field=JavaFX.NewTextField(18,333,433,950);
		TextField lib2Field=JavaFX.NewTextField(18,333,766,950);
		Button confirm2=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
		Button cancel2=JavaFX.NewButton("Annuler",red,18,1230,950);
		Button addBar2=JavaFX.NewButton("",black,2, 93, 961,1200,10);

		add2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false,0);
			setActionBar2(false,0);
			tvType.getSelectionModel().clearSelection();
			setActive(false);
			addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
			tvProject.getSelectionModel().clearSelection();
			tvType.getSelectionModel().clearSelection();
		});

		confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(id2Field.getText());
			String ref=ref2Field.getText();
			String label=lib2Field.getText();
			ProjectTypeQueries.addToDatabase(id,ref,label);
			refreshTable2();

			typeField.getItems().add(ref);
			typeField.getSelectionModel().select(0);

			addPane2.getChildren().clear();
			setActive(true);
		});

		cancel2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			addPane2.getChildren().clear();
			setActive(true);
		});
		getChildren().add(addPane2);
	}

	private void setActionBar(boolean visbility,int position)
	{
		viewPortfolio.setLayoutY(position*scaley);
		evaluate.setLayoutY(position*scaley);
		modify.setLayoutY(position*scaley);
		bar.setLayoutY((position+11)*scaley);

		viewPortfolio.setVisible(visbility);
		evaluate.setVisible(visbility);
		modify.setVisible(visbility);
		bar.setVisible(visbility);
	}

	private void setActionBar2(boolean visbility,int position)
	{
		modify2.setLayoutY(position*scaley);
		bar2.setLayoutY((position+11)*scaley);

		modify2.setVisible(visbility);
		bar2.setVisible(visbility);
	}

	public void resetSelection()
	{
		setActive(true);
		tvProject.getSelectionModel().clearSelection();
		tvType.getSelectionModel().clearSelection();
	}

	public void setActive(boolean bool)
	{
		add.setDisable(!bool);
		add2.setDisable(!bool);
		tvProject.setDisable(!bool);
		tvType.setDisable(!bool);
	}

	public void refreshTable()
	{
		JavaFX.updateTable(tvProject,ProjectQueries.getResultSet());
	}
	public void refreshTable2()
	{
		JavaFX.updateTable(tvType,ProjectTypeQueries.getResultSet());
	}
}
