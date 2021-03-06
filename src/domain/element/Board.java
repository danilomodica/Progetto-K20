package domain.element;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import domain.match.Match;
import domain.team.Team;
import domain.util.ScheduleGenerator;

/**
 * Board class.
 * 
 */
public class Board extends TournamentElement {

	public Board() {}

	@Override
	public void initTournamentElement() {
		schedule = ScheduleGenerator.getInstance().generateSchedule(teamsList, 1);
	}
	
	@Override
	public boolean insertScore(int dayNumber, Match match, int homeScore, int awayScore) {
		if (super.insertScore(dayNumber, match, homeScore, awayScore)) {
			if (schedule.get(schedule.size() - 1).getMatchesList().size() == 1)
				return true;
			
			if (isCompleted())
				return addNextDay();
			
			return true;
		}
		return false;
	}
	
	/**
	 * Method to add the next day of the board, whit new matches to play.
	 * 
	 */
	private boolean addNextDay() {
		int prevDayNumber = schedule.size() - 1;
		Day prevDay = schedule.get(prevDayNumber);
		Date date = incrementDate(prevDay.getDate()).getTime();
		
		if (prevDay.isCompleted()) {
			List<Team> prevDayWinnersList = getDayWinnersList(prevDay);			
			List<Match> matchesList = new ArrayList<>();
			if(prevDayWinnersList.size()>1) {
				for (int i = 0; i < prevDayWinnersList.size(); i += 2)
					matchesList.add(new Match(date, prevDayWinnersList.get(i), prevDayWinnersList.get(i+1)));				
				Day nextDay = new Day(prevDayNumber + 2, matchesList, date);					
				return schedule.add(nextDay);	
			}
		}
		return false;
	}
	
	private Calendar incrementDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		return calendar;
	}
	
	/**
	 * Method to get the list of winner teams of a day.
	 * 
	 * @param day 
	 * 
	 * @return dayWinnersList
	 * 
	 */
	private List<Team> getDayWinnersList(Day day){
		List<Match> matchesList=day.getMatchesList();
		List<Team> dayWinnersList=new ArrayList<>();
		for(Match m: matchesList)
			if(m.isPlayed())
				dayWinnersList.add(m.getWinner());
		return dayWinnersList;
	}
	
	@Override
	public ElementType getTournamentElementType() {
		return ElementType.BOARD;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Team Name" + "\n");
		for (Team team : teamsList) {
			sb.append(team + "\n");
		}

		return sb.toString();
	}
}
