package interfaces.fan.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import domain.element.Day;
import domain.tournament.Tournament;
import domain.tournament.TournamentType;
import interfaces.fan.gui.util.StageLoader;
import interfaces.fan.gui.util.TournamentUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import services.persistence.dao.impl.FacadeImpl;


public class KnockoutPhase8Controller implements Initializable {
	@FXML
	Button backButton;
	@FXML
	Text textBoard;
	@FXML
	Label label1;
	@FXML
	Label label2;
	@FXML
	Label label3;
	@FXML
	Label label4;
	@FXML
	Label label5;
	@FXML
	Label label6;
	@FXML
	Label label7;
	@FXML
	Label label8;
	@FXML
	Label label9;
	@FXML
	Label label10;
	@FXML
	Label label11;
	@FXML
	Label label12;
	@FXML
	Label label13;
	@FXML
	Label label14;
	@FXML
	Label label15;

	List<Label> labelDay1 = new ArrayList<>();
	List<Label> labelDay2 = new ArrayList<>();
	List<Label> labelDay3 = new ArrayList<>();

	FacadeImpl facade = FacadeImpl.getInstance();
	List<Day> days;
	Tournament tournament;

	public void passingDataToKnock8(Tournament k8) {
		tournament = k8;

		textBoard.setText(k8.getName());
		if (k8.getTournamentType() == TournamentType.MIXED) {
			try {
				k8.setGroupSchedule(facade.getGroupSchedule(k8));
			} catch (SQLException e) {
				new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
			}
			if (k8.getGroup().isCompleted()) {
				try {
					k8.setBoardSchedule(facade.getBoardSchedule(k8));
				} catch (SQLException e) {
					new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
				}
			}
		}
		if (k8.getTournamentType() == TournamentType.KNOCKOUT_PHASE) {
			try {
				k8.setBoardSchedule(FacadeImpl.getInstance().getBoardSchedule(k8));
			} catch (SQLException e) {
				new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
			}
		}

		labelDay1.add(label1);
		labelDay1.add(label2);
		labelDay1.add(label3);
		labelDay1.add(label4);
		labelDay1.add(label5);
		labelDay1.add(label6);
		labelDay1.add(label7);
		labelDay1.add(label8);
		TournamentUtil.populateBrackets(k8.getBoardSchedule(), 0, 0, labelDay1);

		labelDay2.add(label9);
		labelDay2.add(label10);
		labelDay2.add(label11);
		labelDay2.add(label12);
		TournamentUtil.populateBrackets(k8.getBoardSchedule(), 1, 1, labelDay2);

		labelDay3.add(label13);
		labelDay3.add(label14);
		TournamentUtil.populateBrackets(k8.getBoardSchedule(), 2, 2, labelDay3);

		if (k8.getBoardSchedule().size() > 2) {
			if (k8.getBoardSchedule().get(2).getMatchesList().get(0).isPlayed()) {
				label15.setText((k8.getBoardSchedule().get(2).getMatchesList().get(0).getWinner().getName()));
			}
		}

	}

	public void backButtonClicked(ActionEvent event) throws IOException, SQLException {
		StageLoader SLB = new StageLoader();
		SLB.backToFanMenuOrLeague(event, tournament);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
