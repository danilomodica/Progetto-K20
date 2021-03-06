package interfaces.manager.gui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import domain.team.Player;
import domain.team.PlayerPositionType;
import domain.team.Stadium;
import domain.team.Team;
import domain.tournament.Tournament;
import interfaces.manager.gui.util.GraphicControlsHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import services.persistence.dao.impl.FacadeImpl;

/**
 * Controller for edit the informations of a team that participates into a particular tournament
 * @see Initializable
 * @see Tournament
 * @see Team
 * @see Player
 * @see Stadium
 */

public class EditTeamController implements Initializable {

	@FXML
	private ComboBox<String> cmbBoxTournament,cmbBoxTeam,cmbBoxStadium, cmbBoxPlayerPosition;
	@FXML
	private TextField txtFldStadiumName,txtFldStadiumCity,txtFldStadiumCapacity,txtFldPlayerName,txtFldPlayerSurname;
	@FXML
	private Button btnAddStadium,btnAddPlayer,btnEditPlayer,btnDeletePlayer;
	@FXML
	private Spinner<Integer> spinnerPlayerNumber;
	@FXML
	private TableView<Player> tblViewPlayer;
	@FXML
	private TableColumn<Player, String> tblClmnPlayerName,tblClmnPlayerSurname,tblClmnPlayerNumber,tblClmnPlayerPosition;
	
	private String username;
	private ObservableList<String> tournaments,teams,stadiums;
	private ObservableList<Player> players;
	private SpinnerValueFactory<Integer> numbers;
	private FacadeImpl facadeImpl;
	private List<Tournament> tournamentsList;
	private List<Team> teamsList;
	private List<Player>playersList;
	private List<Stadium> stadiumsList;
	private Tournament tournament;
	private Team team;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		facadeImpl= FacadeImpl.getInstance();
		tournaments= FXCollections.observableArrayList();
		players=FXCollections.observableArrayList();
		setDisableStadiumTxtFields();
		cmbBoxPlayerPosition.setItems(FXCollections.observableArrayList("GK","CB","MF","CF"));	
		setSpinner();
		tblViewPlayer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Player>() {

			@Override
			public void changed(ObservableValue<? extends Player> arg0, Player arg1, Player arg2) {
				Player player=tblViewPlayer.getSelectionModel().getSelectedItem();
				if(player!=null) {
					txtFldPlayerName.setText(player.getName());
					txtFldPlayerSurname.setText(player.getSurname());
					spinnerPlayerNumber.getValueFactory().setValue(player.getNumber());
					cmbBoxPlayerPosition.setValue(player.getPosition().name());
				}
				
			}
		});

	}
	
	public void setUsername(String username) {		
		this.username=username;
	}
	
	public void setTournamentsList(List<Tournament> tournamentsList) {
		this.tournamentsList=tournamentsList;
	}
	
	public void populateCmbBoxTournament(ObservableList<String> tournaments) {
		this.tournaments=tournaments;
		this.cmbBoxTournament.setItems(tournaments);
	}
	
	private ObservableList<String> getTeamsList(){
		ObservableList<String> teams=FXCollections.observableArrayList();
		try {
			if(cmbBoxTournament.getValue()!=null) {
				tournament=getTournament(cmbBoxTournament.getSelectionModel().getSelectedItem());
				teamsList=facadeImpl.getTeamsByTournament(tournament);
				}
			for(Team t: teamsList) 
				teams.add(t.getName());
		} catch (Exception ex) {
			ex.printStackTrace();			
		}
		return teams;
	}
	
	private Tournament getTournament(String name) {
		for(Tournament tournament:tournamentsList) {
			if(tournament.getName().equals(name))
				return tournament;
		}
		return null;
	}
	
	private Team getTeam(String name) {
		for(Team team:teamsList)
			if(team.getName().equals(name))
				return team;
		return null;
		
	}
	
	public void setStadium(ActionEvent event) {
		try {
			Stadium stadium=getStadium(cmbBoxStadium.getSelectionModel().getSelectedItem());
			team.setStadium(stadium);
			facadeImpl.updateTeam(team);
			new Alert(AlertType.INFORMATION,"Stadium successfully setted or updated.",ButtonType.OK).show();		
		}
		catch(NullPointerException npe) {
			new Alert(AlertType.ERROR,"Please select an option.",ButtonType.OK).show();
		}
		catch (Exception e) {
			new Alert(AlertType.ERROR,e.getMessage(),ButtonType.OK).show();
		}
		
	}
	
	public void addPlayer(ActionEvent event) {
		try {
			Player player= new Player(txtFldPlayerName.getText(), txtFldPlayerSurname.getText(),spinnerPlayerNumber.getValue(),getPlayerPosition());
			team.insertPlayer(player);		
			facadeImpl.storePlayer(player, team);
			players.add(player);
			restorePlayerComponents();
		}
		catch(NullPointerException npe) {
			new Alert(AlertType.ERROR,"Please select an option.",ButtonType.OK).show();
		}
		catch(NumberFormatException nfe) {
			new Alert(AlertType.ERROR,"Invalid input.",ButtonType.OK).show();
		}
		catch (Exception e) {
			new Alert(AlertType.ERROR,e.getMessage(),ButtonType.OK).show();
		}
		
	}
	
	public void deletePlayer(ActionEvent event) {
		try {
			Player player= tblViewPlayer.getSelectionModel().getSelectedItem();
			team.removePlayer(player);			
			facadeImpl.removePlayer(player);
			players.remove(player);
			restorePlayerComponents();
		}
		catch(NullPointerException npe) {
			new Alert(AlertType.ERROR,"Please select an option.",ButtonType.OK).show();
		}
		catch(NumberFormatException nfe) {
			new Alert(AlertType.ERROR,"Invalid input.",ButtonType.OK).show();
		}
		catch (Exception e) {
			new Alert(AlertType.ERROR,e.getMessage(),ButtonType.OK).show();
		}
		
	}
	
	public void editPlayer(ActionEvent event) {
		try {
			Player player= tblViewPlayer.getSelectionModel().getSelectedItem();
			player.setName(txtFldPlayerName.getText());
			player.setSurname(txtFldPlayerSurname.getText());
			player.setNumber(spinnerPlayerNumber.getValue());
			player.setPosition(getPlayerPosition());
			facadeImpl.updatePlayer(player);
			players.clear();
			playersList=team.getPlayers();
			for(Player p:playersList) {
				players.add(p);
			}
			tblViewPlayer.setItems(players);
			restorePlayerComponents();
			
		}
		catch(NullPointerException npe) {
			new Alert(AlertType.ERROR,"Please select an option.",ButtonType.OK).show();
		}
		catch(NumberFormatException nfe) {
			new Alert(AlertType.ERROR,"Invalid input.",ButtonType.OK).show();
		}
		catch (Exception e) {
			new Alert(AlertType.ERROR,e.getMessage(),ButtonType.OK).show();
		}
	}
	
	private PlayerPositionType getPlayerPosition() {
		PlayerPositionType position=null;
		switch (cmbBoxPlayerPosition.getSelectionModel().getSelectedIndex()) {
		case 0:
			position=PlayerPositionType.GK;
			break;
		case 1:
			position=PlayerPositionType.CB;
			break;
		case 2:
			position=PlayerPositionType.MF;
			break;
		case 3:
			position=PlayerPositionType.CF;
			break;
		default:
			position=null;
			break;
		}
		return position;
	}
	
	private void restorePlayerComponents() {
		GraphicControlsHandler.resetTextField(this.txtFldPlayerName, "Name");
		GraphicControlsHandler.resetTextField(this.txtFldPlayerSurname, "Surname");
		GraphicControlsHandler.resetSpinner(this.spinnerPlayerNumber);
		GraphicControlsHandler.resetComboBox(cmbBoxPlayerPosition, "Position");
	}
	
	private void restoreStadiumComponents() {
		GraphicControlsHandler.resetComboBox(cmbBoxStadium, "Select");
		GraphicControlsHandler.resetTextField(txtFldStadiumName,"Name");
		GraphicControlsHandler.resetTextField(txtFldStadiumCity,"City");
		GraphicControlsHandler.resetTextField(txtFldStadiumCapacity,"Capacity");
	}
		
	private ObservableList<String> getStadiumsList(){
		ObservableList<String> stadiums=FXCollections.observableArrayList();
		try {
			if(cmbBoxTeam.getValue()!=null) {
				stadiumsList=facadeImpl.getStadiums();
				}
			for(Stadium s: stadiumsList) 
				stadiums.add(s.getName());
		} catch (Exception ex) {
			ex.printStackTrace();			
		}
		return stadiums;
	}
	
	private Stadium getStadium(String name) {
		for(Stadium s:stadiumsList)
			if(s.getName().equals(name))
				return s;
		return null;
	}
	
	public void setCmbBoxTeam(ActionEvent event) {
		players.clear();
		GraphicControlsHandler.resetComboBox(cmbBoxTeam, "Select Team");
		restoreStadiumComponents();
		teams=getTeamsList();
		if(teams !=null)
			this.cmbBoxTeam.setItems(teams);
	}
	
	private void setDisableStadiumTxtFields() {
		txtFldStadiumName.setDisable(true);;
		txtFldStadiumCity.setDisable(true);
		txtFldStadiumCapacity.setDisable(true);
	}
	
	private void setSpinner() {
		numbers= new SpinnerValueFactory.IntegerSpinnerValueFactory(0,99);
		spinnerPlayerNumber.setValueFactory(numbers);
		spinnerPlayerNumber.setEditable(true);	
	}
	
	public void setTeamInformations(ActionEvent event) {
		team= getTeam(cmbBoxTeam.getSelectionModel().getSelectedItem());
		Stadium stadium=null;
		if(team!=null) {
			stadiums=getStadiumsList();
			cmbBoxStadium.setItems(stadiums);
			stadium=team.getStadium();	
			if(stadium!=null) {			
				cmbBoxStadium.setValue(team.getStadium().getName());
				txtFldStadiumName.setText(stadium.getName());
				txtFldStadiumCity.setText(stadium.getCity());
				txtFldStadiumCapacity.setText(Integer.toString(stadium.getCapacity()));
			}
			else if(stadium==null) 
				restoreStadiumComponents();
			populateTblViewPlayer();		
		}
	}
	
	private void populateTblViewPlayer() {
		players.clear();
		playersList=team.getPlayers();
		for(Player p:playersList) {
			players.add(p);
		}				
		tblClmnPlayerName.setCellValueFactory(new PropertyValueFactory<Player, String>("Name"));
		tblClmnPlayerSurname.setCellValueFactory(new PropertyValueFactory<Player, String>("Surname"));
		tblClmnPlayerNumber.setCellValueFactory(new PropertyValueFactory<Player, String>("Number"));
		tblClmnPlayerPosition.setCellValueFactory(new PropertyValueFactory<Player, String>("Position"));
		tblViewPlayer.setItems(players);
	}
	
	public void setStadiumInformations(ActionEvent event) {
		Stadium stadium=getStadium(cmbBoxStadium.getSelectionModel().getSelectedItem());
		if(stadium!=null) {
			txtFldStadiumName.setText(stadium.getName());
			txtFldStadiumCity.setText(stadium.getCity());
			txtFldStadiumCapacity.setText(Integer.toString(stadium.getCapacity()));
		}
	}
}
