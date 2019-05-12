package FrontEnd.User;

import BackEnd.Profile.ProfileQueries;
import BackEnd.User.User;
import BackEnd.User.UserQueries;
import FrontEnd.Login;
import Interface.JavaFX;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserInterface extends Pane
{
	static double scalex = Login.scalex;
	static double scaley = Login.scaley;

	private Paint black=Paint.valueOf("000000");
	private Paint red=Paint.valueOf("F04040");
	private Paint lightBlue=Paint.valueOf("11BAF8");
	private Paint lightGreen=Paint.valueOf("50be96");

	private Button add,bar,modify,disable;
	private Button add2,bar2,modify2;

	private ComboBox idProField;

	private TableView tvUser,tvProfile;

	@SuppressWarnings({ "unchecked","rawtypes" })
	public UserInterface(User user,double x, double y)
	{
		this.setLayoutX(x*scalex);
		this.setLayoutY(y*scaley);
		
		Image modifyIcon= new Image("file:res/icon/user/Modify.png");
		Image disableIcon= new Image("file:res/icon/user/Disable.png");

		//--User-Table--------------------------------------------------------------------------------------------------------------------------------
		bar=JavaFX.NewButton("",black,2, 780, 85,775,10);
		modify=JavaFX.NewButton("Modifier l'utilisateur", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,74 , 220, 32);
		disable=JavaFX.NewButton("Desactiver l'utilisateur", disableIcon, ContentDisplay.LEFT, red, 16,1340 ,74 , 235, 32);
		
		getChildren().add(bar);
		getChildren().add(modify);
		getChildren().add(disable);

		setActionBar(false);
	
		tvUser=JavaFX.NewTableView(UserQueries.getResultSet(), 100,75, 1000, 400);
		Pane additionalOptions=new Pane();

		tvUser.setRowFactory(tv->
		{
			TableRow row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					setActionBar2(false);
					setActionBar(true);
					additionalOptions.getChildren().clear();
					tvProfile.getSelectionModel().clearSelection();
				}
			});
			return row;
		});
		additionalOptions.setLayoutX(1125*scalex);
		additionalOptions.setLayoutY(75*scaley);

		modify.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false);
			setActionBar2(false);
			setActive(false);
			Object row=tvUser.getSelectionModel().getSelectedItems().get(0);
			int id = Integer.valueOf(row.toString().split(",")[0].substring(1));
			UserModify userModify=new UserModify(this,id);
			additionalOptions.getChildren().add(userModify);
		});
		getChildren().add(tvUser);
		getChildren().add(additionalOptions);

		//-Add-User-----------------------------------------------------------------------------------------------------------------------------------------
		add=JavaFX.NewButton("Ajouter",18,7,482);
		getChildren().add(add);

		Pane addPane=new Pane();
		List<String> Profiles=ProfileQueries.getProfilesRef();
		TextField idField=JavaFX.NewTextField(18,166,100,482);
		TextField usrField=JavaFX.NewTextField(18,167,266,482);
		TextField fsField=JavaFX.NewTextField(18,167,433,482);
		TextField lsField=JavaFX.NewTextField(18,167,600,482);
		TextField phField=JavaFX.NewTextField(18,167,767,482);
		phField.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				if (!newValue.matches("\\d*"))
				{
					phField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		idProField=JavaFX.NewComboBox(Profiles,167,934,482);
		idField.setText(String.valueOf(tvUser.getItems().size()));
		idField.setEditable(false);
		Button confirm=JavaFX.NewButton("Confirmer",lightGreen,18,1110,482);
		Button cancel=JavaFX.NewButton("Annuler",red,18,1230,482);
		Button addBar=JavaFX.NewButton("",black,2, 93, 493,1200,10);

		add.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false);
			setActionBar2(false);
			setActive(false);
			addPane.getChildren().addAll(addBar,idField,usrField,fsField,lsField,phField,idProField,confirm,cancel);
			tvUser.getSelectionModel().clearSelection();
			tvProfile.getSelectionModel().clearSelection();
		});

		confirm.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			boolean correct=true;
			int id=Integer.valueOf(idField.getText());
			String username=usrField.getText();
			String firstname=fsField.getText();
			String lastname=lsField.getText();

			if(username.length()>0 && firstname.length()>0 && lastname.length()>0)
			{
				ResultSet rs=UserQueries.getResultSet();
				try
				{
					while(rs.next())
					{
						String usr=rs.getString(2);
						if(username.equals(usr))
						{
							correct=false;
						}
					}
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}

				String phone=phField.getText();
				int idProfile=idProField.getSelectionModel().getSelectedIndex();
				if(correct)
				{
					UserQueries.addToDatabase(id,username,firstname,lastname,phone,idProfile);
					refreshTable();
					addPane.getChildren().clear();
					setActive(true);
				}
				else
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Erreur");
					alert.setHeaderText(null);
					alert.setContentText("Ce nom d'utilisateur existe déjà");
					alert.showAndWait();
				}
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

		//--Profile-Table--------------------------------------------------------------------------------------------------------------------------------
		bar2=JavaFX.NewButton("",black,2, 780, 535,500,10);
		modify2=JavaFX.NewButton("Modifier le profile", modifyIcon, ContentDisplay.LEFT, lightBlue, 16,1110 ,524 , 200, 32);

		getChildren().add(bar2);
		getChildren().add(modify2);

		setActionBar2(false);

		tvProfile=JavaFX.NewTableView(ProfileQueries.getResultSet(), 100,525, 1000, 420);

		Pane additionalOptions2=new Pane();
		additionalOptions2.setLayoutX(1125*scalex);
		additionalOptions2.setLayoutY(525*scaley);
		modify2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent ->
		{
			setActionBar(false);
			setActionBar2(false);
			setActive(false);
			Object row=tvProfile.getSelectionModel().getSelectedItems().get(0);
			int id = Integer.valueOf(row.toString().split(",")[0].substring(1));
			ProfileModify profileModify=new ProfileModify(this,id);
			additionalOptions2.getChildren().add(profileModify);
		});
		getChildren().add(additionalOptions2);
		tvProfile.setRowFactory(tv->
		{
			TableRow row = new TableRow<>();
			row.setOnMousePressed(event->
			{
				if (!row.isEmpty())
				{
					setActionBar2(true);
					setActionBar(false);
					tvUser.getSelectionModel().clearSelection();
					additionalOptions2.getChildren().clear();
				}
			});
			return row;
		});
		
		getChildren().add(tvProfile);

		//-Add-Profile-----------------------------------------------------------------------------------------------------------------------------------------
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
			id2Field.setText(String.valueOf(tvProfile.getItems().size()));
			setActionBar(false);
			setActionBar2(false);
			setActive(false);
			addPane2.getChildren().addAll(addBar2,id2Field,ref2Field,lib2Field,confirm2,cancel2);
			tvUser.getSelectionModel().clearSelection();
			tvProfile.getSelectionModel().clearSelection();
		});

		confirm2.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent->
		{
			int id=Integer.valueOf(id2Field.getText());
			String ref=ref2Field.getText();
			String label=lib2Field.getText();
			if(ref.length()>0 && label.length()>0)
			{
				ProfileQueries.addToDatabase(id,ref,label);
				refreshTable2();
				ref2Field.setText("");lib2Field.setText("");
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
		disable.setVisible(visbility);
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
		tvUser.getSelectionModel().clearSelection();
		tvProfile.getSelectionModel().clearSelection();
	}

	public void setActive(boolean bool)
	{
		add.setDisable(!bool);
		add2.setDisable(!bool);
		tvUser.setDisable(!bool);
		tvProfile.setDisable(!bool);
	}

	public void refreshTable()
	{
		JavaFX.updateTable( tvUser,UserQueries.getResultSet());
	}
	public void refreshTable2()
	{
		ObservableList<String> observableList= FXCollections.observableArrayList(ProfileQueries.getProfilesRef());
		idProField.setItems(observableList);
		idProField.getSelectionModel().selectFirst();
		JavaFX.updateTable( tvProfile,ProfileQueries.getResultSet());
	}
}
