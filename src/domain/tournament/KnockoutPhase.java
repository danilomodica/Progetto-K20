package domain.tournament;

import java.util.List;

import domain.element.Board;
import domain.element.Day;
import domain.element.TournamentElement;
import domain.match.Match;
import domain.team.Team;

/**
 * Knockout Phase Tournament class.
 * 
 */
public class KnockoutPhase extends Tournament {

	private TournamentElement board;

	/**
	 * Knockout Phase Tournament constructor.
	 * @param name
	 * 
	 */
	public KnockoutPhase(String name) {
		super(name);
		this.board = new Board();
	}

	@Override
	public void initBoard(List<Team> teamsList) {
		addTeams(teamsList);
		this.board.initTournamentElement();
	}
	
	@Override
	public void initGroup(List<Team> teamsList) {
		
	}

	@Override
	public boolean addTeamInTournament(Team team) {
		return this.board.addTeam(team);
	}
	
	@Override
	public List<Team> getTeamsList() {
		return this.board.getTeamsList();
	}

	@Override
	public boolean insertScore(int dayNumber, Match match, int homeScore, int awayScore) {
		return this.board.insertScore(dayNumber, match, homeScore, awayScore);
	}
	
	@Override
	public TournamentElement getGroup() {
		return null;
	}
	
	@Override
	public TournamentElement getBoard() {
		return this.board;
	}
	
	@Override
	public List<Day> getGroupSchedule() {
		return null;
	}

	@Override
	public void setGroupSchedule(List<Day> schedule) {
		
	}

	@Override
	public List<Day> getBoardSchedule() {
		return this.board.getSchedule();
	}
	
	@Override
	public void setBoardSchedule(List<Day> schedule) {
		this.board.setSchedule(schedule);
	}

	@Override
	public TournamentType getTournamentType() {
		return TournamentType.KNOCKOUT_PHASE;
	}
	
	@Override
	public String toString() {
		return super.toString() + String.format("Tournament type: %s\n%s", this.getTournamentType(), this.board.toString());
	}
}
