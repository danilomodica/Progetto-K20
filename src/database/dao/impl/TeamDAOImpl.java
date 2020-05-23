package database.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import database.dao.ITeamDAO;
import database.util.DBConnection;
import mvc.model.team.Player;
import mvc.model.team.PlayerPositionType;
import mvc.model.team.Stadium;
import mvc.model.team.Team;
import mvc.model.tournament.Tournament;

public class TeamDAOImpl implements ITeamDAO {

	private Connection conn;

	@Override
	public boolean storeTeam(Team team, Tournament t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;

		String query = "INSERT INTO team(Name, IDTournament, Stadium, Points, GoalsScored, GoalsConceded, team.Group, Board) VALUES(?,?,?,?,?,?,?,?)";
		ps = conn.prepareStatement(query);
		ps.setString(1, team.getName());
		ps.setInt(2, t.getId());
		ps.setNull(3, Types.INTEGER);
		ps.setInt(4, team.getPoints());
		ps.setInt(5, team.getGoalsScored());
		ps.setInt(6, team.getGoalsConceded());

		// group
		if (t.getTournamentElement().getTournamentElementType().ordinal() == 1) {
			ps.setInt(7, t.getTournamentElement().getId());
			ps.setNull(8, Types.INTEGER);
		}
		// board
		else {
			ps.setNull(7, Types.INTEGER);
			ps.setInt(8, t.getTournamentElement().getId());
		}

		rs = ps.execute();
		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}

		DBConnection.closeConnection(conn);
		return false;
	}

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

	@Override
	public boolean removePlayer(Player p, Team t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		boolean rs;

		int IDTeam = t.getId();

		String query = "DELETE from player where Surname=? and Number=? and IDTeam=?";
		ps = conn.prepareStatement(query);
		ps.setString(1, p.getSurname());
		ps.setInt(2, p.getNumber());
		ps.setInt(3, IDTeam);
		rs = ps.execute();

		if (!rs) {
			DBConnection.closeConnection(conn);
			return true;
		}

		DBConnection.closeConnection(conn);
		return false;
	}

	@Override
	public List<Player> getPlayersByTeam(Team t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		ResultSet rs;

		List<Player> players = new ArrayList<>();
		int IDTeam = t.getId();

		String query = "SELECT Name, Surname, Number, Position from player p, player_position_type pp where IDTeam=? and p.PlayerPositionType=pp.IDPlayerPositionType";
		ps = conn.prepareStatement(query);
		ps.setInt(1, IDTeam);

		rs = ps.executeQuery();

		while (rs.next()) {
			String name = rs.getString(1);
			String surname = rs.getString(2);
			int number = rs.getInt(3);
			PlayerPositionType type = PlayerPositionType.valueOf(rs.getString(4));
			players.add(new Player(name, surname, number, type));
		}

		DBConnection.closeConnection(conn);
		return players;
	}

	@Override
	public List<Team> getTeamsByTournament(Tournament t) throws SQLException {
		conn = DBConnection.startConnection(conn);
		PreparedStatement ps;
		PreparedStatement ps2;
		ResultSet rs;
		ResultSet rs2;

		List<Team> teams = new ArrayList<>();
		Team team = null;

		String query = "SELECT Name, Stadium, Points, GoalsScored, GoalsConceded from team where IDTournament=?";
		ps = conn.prepareStatement(query);
		ps.setInt(1, t.getId());

		rs = ps.executeQuery();

		while (rs.next()) {
			String name = rs.getString(1);
			String stadium = rs.getString(2);

			int points = rs.getInt(3);
			int goalsScored = rs.getInt(4);
			int goalsConceded = rs.getInt(5);
			team = new Team(name);
			
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
	
			teams.add(team);
		}

		DBConnection.closeConnection(conn);
		return teams;
	}
}
