package services.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import domain.team.Player;
import domain.team.PlayerPositionType;
import domain.team.Stadium;
import domain.team.Team;
import domain.tournament.Tournament;
import services.persistence.dao.ITeamDAO;
import services.persistence.util.DBConnection;

/**
 * Implementation of ITeamDAO
 * @param conn Connection object to manage the access to the db
 * @see ITeamDAO
 * @see Team
 * @see Player
 * @see Stadium
 * @see Tournament
 */

public class TeamDAOImpl implements ITeamDAO {

	private Connection conn;

	/**
	 * Stores a team into the db
	 * @param team
	 * @param t the tournament where team is playing
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean storeTeam(Team team, Tournament t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;

		String query = "INSERT INTO team(Name, IDTournament, Stadium, Points, GoalsScored, GoalsConceded, team.Group, Board) VALUES(?,?,?,?,?,?,?,?)";
		ps = conn.prepareStatement(query);
		ps.setString(1, team.getName());
		ps.setInt(2, t.getId());
		ps.setNull(3, Types.VARCHAR);
		ps.setInt(4, team.getPoints());
		ps.setInt(5, team.getGoalsScored());
		ps.setInt(6, team.getGoalsConceded());

		if (t.getGroup() == null)
			ps.setNull(7, Types.INTEGER);
		else 
			ps.setInt(7, t.getGroup().getId());
		
		if (t.getBoard() == null)
			ps.setNull(8, Types.INTEGER);
		else
			ps.setInt(8, t.getBoard().getId());

		rs = ps.execute();
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}

		DBConnection.closeConnection(conn);
		return false;
	}

	/**
	 * Gets the id of the last team stored
	 * @return team's id
	 */
	@Override
	public int getLastTeamID() throws SQLException {
		conn = DBConnection.startConnection(conn);
		
		ResultSet rs;

		String query = "SELECT MAX(IDTeam) FROM team";

		Statement st1;
		
		st1 = conn.createStatement();
		rs = st1.executeQuery(query);

		rs.next();
		int ID = rs.getInt(1);
			
		DBConnection.closeConnection(conn);
		return ID;
	}
	
	/**
	 * Updates a team
	 * @param team
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean updateTeam(Team t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;
		
		String query = "UPDATE team SET Name=?, Stadium=? WHERE IDTeam=?";
		ps = conn.prepareStatement(query);
		ps.setString(1, t.getName());
		ps.setString(2, t.getStadium().getName());
		ps.setInt(3, t.getId());
		rs = ps.execute();
		
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}	

		DBConnection.closeConnection(conn);
		return false;
	}
	
	/**
	 * Updates a board id of a team
	 * @param t tournament
	 * @param team
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean updateTeam(Tournament t , Team team) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;
		
		String query = "UPDATE team SET Board=? WHERE IDTeam=?";
		ps = conn.prepareStatement(query);
		ps.setInt(1, t.getBoard().getId());
		ps.setInt(2, team.getId());
		rs = ps.execute();
		
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}	

		DBConnection.closeConnection(conn);
		return false;
	}
	
	/**
	 * Stores a player into the db
	 * @param p the player to be stored
	 * @param t the team where player is playing
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean storePlayer(Player p, Team t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;

		int IDTeam = t.getId();

		String query = "INSERT INTO player(IDTeam, Number, Name, Surname, PlayerPositionType) VALUES(?,?,?,?,?)";
		ps = conn.prepareStatement(query);
		ps.setInt(1, IDTeam);
		ps.setInt(2, p.getNumber());
		ps.setString(3, p.getName());
		ps.setString(4, p.getSurname());
		ps.setInt(5, p.getPosition().ordinal() + 1);

		rs = ps.execute();
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}

		DBConnection.closeConnection(conn);
		return false;
	}
	
	/**
	 * Gets the id of the last player stored
	 * @return player's id
	 */
	@Override
	public int getLastPlayerID() throws SQLException {
		conn = DBConnection.startConnection(conn);
		
		ResultSet rs;

		String query = "SELECT MAX(IDPlayer) FROM player";

		Statement st1;
		
		st1 = conn.createStatement();
		rs = st1.executeQuery(query);

		rs.next();
		int ID = rs.getInt(1);
			
		DBConnection.closeConnection(conn);
		return ID;
	}

	/**
	 * Stores a stadium into the db
	 * @param s the stadium to store
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean storeStadium(Stadium s) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;

		String query = "INSERT INTO stadium(Name, City, Capacity) VALUES(?,?,?)";
		ps = conn.prepareStatement(query);
		ps.setString(1, s.getName());
		ps.setString(2, s.getCity());
		ps.setInt(3, s.getCapacity());

		rs = ps.execute();
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}

		DBConnection.closeConnection(conn);
		return false;
	}
	/**
	 * Checks if a stadium name is already present into the db
	 * @param s the stadium to check
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean checkUniqueStadium(Stadium s) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		ResultSet rs;

		String query = "SELECT count(*) from stadium where Name=?";
		ps = conn.prepareStatement(query);
		ps.setString(1, s.getName());

		rs = ps.executeQuery();

		rs.next();
		int count = rs.getInt(1);
		
		if (count == 0) {
			DBConnection.closeConnection(conn);
			return true;
		}
			
		DBConnection.closeConnection(conn);
		return false;
	}
	
	/**
	 * Updates a stadium
	 * @param s the stadium to update
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean updateStadium(Stadium s) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;
		
		String query = "UPDATE stadium SET Name=?, City=?, Capacity=? WHERE Name=?";
		ps = conn.prepareStatement(query);
		ps.setString(1, s.getName());
		ps.setString(2, s.getCity());
		ps.setInt(3, s.getCapacity());
		ps.setString(4, s.getName());
		rs = ps.execute();
		
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}	

		DBConnection.closeConnection(conn);
		return false;
	}

	/**
	 * Removes a player from the db by id
	 * @param p the player to remove
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean removePlayer(Player p) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;

		String query = "DELETE from player where IDPlayer=?";
		ps = conn.prepareStatement(query);
		ps.setInt(1, p.getId());
		rs = ps.execute();

		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}

		DBConnection.closeConnection(conn);
		return false;
	}
	
	/**
	 * Updates a player
	 * @param p the player to update
	 * @return a boolean that indicates success/insuccess
	 */
	@Override
	public boolean updatePlayer(Player p) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;
		
		String query = "UPDATE player SET Number=?, Name=?, Surname=?, PlayerPositionType=? WHERE IDPlayer=?";
		ps = conn.prepareStatement(query);
		ps.setInt(1, p.getNumber());
		ps.setString(2, p.getName());
		ps.setString(3, p.getSurname());
		ps.setInt(4, p.getPosition().ordinal() + 1);
		ps.setInt(5, p.getId());
		rs = ps.execute();
		
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}	

		DBConnection.closeConnection(conn);
		return false;
	}

	/**
	 * Gets all players of a team
	 * @param t the team
	 * @return a list containing players
	 */
	private List<Player> getPlayersByTeam(Team t) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;

		List<Player> players = new ArrayList<>();
		Player p = null;
		int IDTeam = t.getId();

		String query = "SELECT IDPlayer, Name, Surname, Number, Position from player p, player_position_type pp where IDTeam=? and p.PlayerPositionType=pp.IDPlayerPositionType";
		ps = conn.prepareStatement(query);
		ps.setInt(1, IDTeam);

		rs = ps.executeQuery();

		while (rs.next()) {
			int ID = rs.getInt(1);
			String name = rs.getString(2);
			String surname = rs.getString(3);
			int number = rs.getInt(4);
			PlayerPositionType type = PlayerPositionType.valueOf(rs.getString(5));
			p = new Player(name, surname, number, type);
			p.setId(ID);
			players.add(p);
		}

		return players;
	}

	/**
	 * Gets all teams of a tournament
	 * @param t the tournament
	 * @return a list containing teams
	 */
	@Override
	public List<Team> getTeamsByTournament(Tournament t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		PreparedStatement ps2;
		ResultSet rs;
		ResultSet rs2;

		List<Team> teams = new ArrayList<>();
		Team team = null;

		String query = "SELECT IDTeam, Name, Stadium, Points, GoalsScored, GoalsConceded from team where IDTournament=?";
		ps = conn.prepareStatement(query);
		ps.setInt(1, t.getId());

		rs = ps.executeQuery();

		while (rs.next()) {
			int ID = rs.getInt(1);
			String name = rs.getString(2);
			String stadium = rs.getString(3);

			int points = rs.getInt(4);
			int goalsScored = rs.getInt(5);
			int goalsConceded = rs.getInt(6);
			team = new Team(name);
			
			team.setId(ID);
			team.setGoalsConceded(goalsConceded);
			team.setGoalsScored(goalsScored);
			team.setPoints(points);
			
			if (stadium != null) {
				String query2 = "SELECT City, Capacity from stadium where Name=?";
				ps2 = conn.prepareStatement(query2);
				ps2.setString(1, stadium);
				rs2 = ps2.executeQuery();
				rs2.next();
				
				String stadiumCity = rs2.getString(1);
				int stadiumCapacity = rs2.getInt(2);
				team.setStadium(new Stadium(stadium, stadiumCity, stadiumCapacity));
			}
			
			for (Player p: getPlayersByTeam(team))
				team.insertPlayer(p);
	
			teams.add(team);
		}

		DBConnection.closeConnection(conn);
		return teams;
	}

	/**
	 * Gets all stadiums
	 * @return a list containing stadiums
	 */
	@Override
	public List<Stadium> getStadiums() throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		ResultSet rs;

		List<Stadium> stadiums = new ArrayList<>();
		Stadium stadium = null;

		String query = "SELECT Name, City, Capacity from stadium";
		ps = conn.prepareStatement(query);

		rs = ps.executeQuery();

		while (rs.next()) {
			String name = rs.getString(1);
			String city = rs.getString(2);
			int capacity = rs.getInt(3);

			stadium = new Stadium(name, city, capacity);
			
			stadiums.add(stadium);
		}

		DBConnection.closeConnection(conn);
		return stadiums;
	}
}
