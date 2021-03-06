package FrontEnd.DatabaseManagement.Project;

import BackEnd.Portfolio.PortfolioQueries;
import BackEnd.Project.ProjectQueries;
import BackEnd.ProjectStatue.ProjectStatueQueries;
import BackEnd.ProjectType.ProjectTypeQueries;
import BackEnd.User.User;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.util.List;

import static BackEnd.Utility.getDatetime;

public class ProjectInterface extends Pane
{
	static double scalex = Login.scalex;
	static double scaley = Login.scaley;

	private Paint black=Paint.valueOf("000000");
	private Paint red=Paint.valueOf("F04040");
	private Paint lightOrange=Paint.valueOf("F77D50");
	private Paint lightBlue=Paint.valueOf("11BAF8");
	private Paint lightGreen=Paint.valueOf("50be96");

	private Button bar,modify;
	private Button bar2,modify2;
	private Button add,add2;

	private TableView tvProject,tvType;
	private ComboBox typeField ,porField;

	@SuppressWarnings({"rawtypes" })
	public ProjectInterface(User user, double x, double y)
	{
		this.setLayoutX(x*scalex);
		this.setLayoutY(y*scaley);

		Image viewPortfolioIcon= new Image("file:res/icon/portfolio/Modify.png");
		Image modifyIcon= new Image("file:res/icon/portfolio/Modify.png");
		Image evaluateIcon=new Image("file:res/icon/portfolio/Evaluate.png");

		bar=JavaFX.NewButton("",black,2, 780, 85,400,10);
		modify=JavaFX.NewButton("Modifier le projet", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,74 , 225, 32);


		getChildren().add(bar);
		getChildren().add(modify);

		modify.setVisible(false);
		bar.setVisible(false);

		tvProject=JavaFX.NewTableView(ProjectQueries.getResultSet(), 100,75, 1000, 400);

		Pane additionalOptions=new Pane();
		additionalOptions.setLayoutX(1125*scalex);
		additionalOptions.setLayoutY(75*scaley);
		modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(1))
			{
				setActionBar(false);
				setActionBar2(false);
				setActive(false);
				Object row=tvProject.getSelectionModel().getSelectedItems().get(0);
				int idProject = Integer.valueOf(row.toString().split(",")[0].substring(1));
				ProjectModify projectModify=new ProjectModify(this,idProject);
				additionalOptions.getChildren().add(projectModify);
			}
			else JavaFX.privilegeAlert();

		});

		tvProject.setRowFactory(tv->
		{
			TableRow<Object> row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					additionalOptions.getChildren().clear();
					setActionBar(true);
					setActionBar2(false);
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
			if(user.isPrivilegedTo(1))
			{
				idField.setText(String.valueOf(tvProject.getItems().size()));
				setActionBar(false);
				setActionBar2(false);
				tvProject.getSelectionModel().clearSelection();
				setActive(false);
				addPane.getChildren().addAll(addBar,idField,refField,porField,typeField,confirm,cancel);
				tvProject.getSelectionModel().clearSelection();
				tvType.getSelectionModel().clearSelection();
			}
			else JavaFX.privilegeAlert();
		});

		confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(idField.getText());
			String ref=refField.getText();
			int porId=porField.getSelectionModel().getSelectedIndex();
			int typeId=typeField.getSelectionModel().getSelectedIndex();
			if(ref.length()>0)
			{
				ProjectQueries.addToDatabase(id,ref,porId,typeId);
				ProjectStatueQueries.addToDatabase(id,"Non Evalué",0,getDatetime());
				refField.setText("");
				refreshTable();
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

		//-ProjectType-Table---------------------------------------------------------------------------------------------------------------------------------
		bar2=JavaFX.NewButton("",black,2, 780, 535,550,10);
		modify2=JavaFX.NewButton("Modifier la Catégorie", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,524 , 240, 32);

		getChildren().add(bar2);
		getChildren().add(modify2);

		setActionBar2(false);

		tvType=JavaFX.NewTableView(ProjectTypeQueries.getResultSet(), 100,525, 1000, 420);

		Pane additionalOptions2=new Pane();
		additionalOptions2.setLayoutX(1125*scalex);
		additionalOptions2.setLayoutY(525*scaley);
		modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(1))
			{
				setActionBar(false);
				setActionBar2(false);
				setActive(false);
				Object row=tvType.getSelectionModel().getSelectedItems().get(0);
				int id = Integer.valueOf(row.toString().split(",")[0].substring(1));
				CategoryModify categoryModify=new CategoryModify(this,id);
				additionalOptions2.getChildren().add(categoryModify);
			}
			else JavaFX.privilegeAlert();

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
					setActionBar2(true);
					setActionBar(false);
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
		id2Field.setEditable(false);
		TextField ref2Field=JavaFX.NewTextField(18,333,433,950);
		TextField lib2Field=JavaFX.NewTextField(18,333,766,950);
		Button confirm2=JavaFX.NewButton("Confirmer",lightGreen,18,1110,950);
		Button cancel2=JavaFX.NewButton("Annuler",red,18,1230,950);
		Button addBar2=JavaFX.NewButton("",black,2, 93, 961,1200,10);

		add2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			if(user.isPrivilegedTo(1))
			{
				id2Field.setText(String.valueOf(tvType.getItems().size()));
				setActionBar(false);
				setActionBar2(false);
				tvType.getSelectionModel().clearSelection();
				setActive(false);
				addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
				tvProject.getSelectionModel().clearSelection();
				tvType.getSelectionModel().clearSelection();
			}
			else JavaFX.privilegeAlert();

		});

		confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(id2Field.getText());
			String ref=ref2Field.getText();
			String label=lib2Field.getText();
			if(ref.length()>0 && label.length()>0)
			{
				ProjectTypeQueries.addToDatabase(id,ref,label);
				refreshTable2();
				id2Field.setText("");ref2Field.setText("");lib2Field.setText("");
				addPane2.getChildren().clear();
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

		cancel2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			addPane2.getChildren().clear();
			setActive(true);
		});
		getChildren().add(addPane2);
	}

	private void setActionBar(boolean visbility)
	{
		modify.setVisible(visbility);
		bar.setVisible(visbility);
	}

	private void setActionBar2(boolean visbility)
	{
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
		ObservableList<String> observableList= FXCollections.observableArrayList(ProjectTypeQueries.getProjectTypesRef());
		typeField.setItems(observableList);
		typeField.getSelectionModel().selectFirst();
		JavaFX.updateTable(tvType,ProjectTypeQueries.getResultSet());
	}
}
