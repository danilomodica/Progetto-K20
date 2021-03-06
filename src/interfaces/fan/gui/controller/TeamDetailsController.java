package interfaces.fan.gui.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import domain.element.Day;
import domain.match.Match;
import domain.team.Player;
import domain.team.PlayerPositionType;
import domain.team.Team;
import domain.tournament.Tournament;
import interfaces.fan.gui.util.StageLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import services.persistence.dao.impl.FacadeImpl;

/**
 * Controller for the team details
 * @see Initializable
 * @see Team
 *
 */
public class TeamDetailsController implements Initializable {
	@FXML
	TableView<Player> table;
	@FXML
	TableColumn<Player, String> name;
	@FXML
	TableColumn<Player, String> surname;
	@FXML
	TableColumn<Player, Integer> number;
	@FXML
	TableColumn<Player, PlayerPositionType> position;
	@FXML
	ListView<String> matchesPlayed;
	@FXML
	ListView<String> matchesNotPlayed;
	@FXML
	Button backButton;
	@FXML
	Text text;

	FacadeImpl facade = FacadeImpl.getInstance();
	ObservableList<Player> players = FXCollections.observableArrayList();
	Tournament tournamentB;

	public void passingData(Tournament tournamentPass, Team teamPass) {
		text.setText(teamPass.getName());
		tournamentB = tournamentPass;

		try {
			for (Day day : facade.getGroupSchedule(tournamentPass)) {
				for (Match match : day.getMatchesList()) {
					if ((match.getHomeTeam().getId() == teamPass.getId()
							|| match.getAwayTeam().getId() == teamPass.getId()) && match.isPlayed()) {
						matchesPlayed.getItems().add(match.toString());
					} else if (match.getHomeTeam().getId() == teamPass.getId()
							|| match.getAwayTeam().getId() == teamPass.getId()) {
						matchesNotPlayed.getItems().add(match.toString());
					}
				}
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}

		try {
			for (Team team : facade.getTeamsByTournament(tournamentPass)) {
				if (team.getId() == teamPass.getId()) {
					for (Player player : team.getPlayers()) {
						players.add(player);
					}
				}
			}
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
		}

	}

	public void backButtonClicked(ActionEvent event) {
		StageLoader SLB = new StageLoader();
		SLB.backToLeague(event, tournamentB);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		name.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
		surname.setCellValueFactory(new PropertyValueFactory<Player, String>("surname"));
		number.setCellValueFactory(new PropertyValueFactory<Player, Integer>("number"));
		position.setCellValueFactory(new PropertyValueFactory<Player, PlayerPositionType>("position"));
		table.setItems(players);

	}

}
