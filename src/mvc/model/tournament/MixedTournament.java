package mvc.model.tournament;

import java.util.List;

import mvc.model.element.Board;
import mvc.model.element.Day;
import mvc.model.element.Group;
import mvc.model.element.TournamentElement;
import mvc.model.match.Match;
import mvc.model.team.Team;

public class MixedTournament extends Tournament {

	private TournamentElement group,board;

	public MixedTournament(String name) {
		super(name);
		this.group= new Group();
		this.board= new Board();
	}

	public TournamentElement getBoard() {
		return this.board;
	}

	@Override
	public void initTournament(List<Team> teamsList) {
		addTeams(teamsList);
		this.group.initTournamentElement();	
	}

	public boolean isGroupCompleted() {
		return group.isCompleted();
	}

	@Override
	public boolean addTeamInTournament(Team team) {
		return group.addTeam(team);
	}
	
	public void initKnockoutPhase() {
		if (this.isGroupCompleted()) {
			List<Team> teamsList=getTeamsList();
			for(int i=0;i<teamsList.size()/2;i++)
				addTeamInBoard(teamsList.get(i));
			this.board.initTournamentElement();
		}
	}
	
	private boolean addTeamInBoard(Team team) {
			return board.addTeam(team);
	}

	@Override
	public boolean insertScore(int dayNumber, Match match, int homeScore, int awayScore) {
		return isGroupCompleted()?board.insertScore(dayNumber, match, homeScore, awayScore):group.insertScore(dayNumber, match, homeScore, awayScore);
	}

	@Override
	public List<Day> getSchedule() {
		return group.getSchedule();
	}
	
	@Override
	public void setSchedule(List<Day> schedule) {
		this.group.setSchedule(schedule);
	}
	
	public void setBoardSchedule(List<Day> schedule) {
		this.board.setSchedule(schedule);
	}
	
	public List<Day> getBoardSchedule(){
		return board.getSchedule();
	}
	
	@Override
	public TournamentType getTournamentType() {
		return TournamentType.MIXED;
	}
	
	@Override
	public TournamentElement getTournamentElement() {
		return group;
	}

	@Override
	public String toString() {
		return super.toString() + String.format("Tournament type: %s\n%s\n%s\n", this.getTournamentType(), this.group.toString(), this.board.toString());
	}
}
