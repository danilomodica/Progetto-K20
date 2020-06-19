package mvc.view.manager.textui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import database.dao.impl.FacadeImpl;
import mvc.model.team.Team;
import mvc.model.tournament.*;

public class ManagerTextUI {

	private Scanner scanner;
	private String inputString;
	private Tournament tournament;
	FacadeImpl facade;

	public ManagerTextUI() {
		scanner = new Scanner(System.in);
		this.facade = new FacadeImpl();
	}

	/* Metodo di avvio. */
	public void start() throws SQLException {
		while (true) {
			System.out.println("** Welcome to the reserved area for managers. **");
			System.out.println("Enter \"1\" to sign up, \"2\" to sign in.");
			System.out.println("Enter \"e\" to exit, \"b\" to go back.\n");

			System.out.println("Input: ");
			inputString = scanner.nextLine();

			if (inputString.equalsIgnoreCase("e")) {
				System.out.println("Closing app...");
				System.exit(0);
			}
			if (inputString.equalsIgnoreCase("b")) {
				System.out.println("You're already on the main menu.\n");
				continue;
			}

			try {
				switch (Integer.parseInt(inputString)) {
				case 1:
					this.login();
					break;
				case 2:
					this.registration();
					break;
				default:
					System.out.println("Unavailable input.\n");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.\n");
			}
		}

	}

	private void login() throws SQLException {
		while (true) {
			System.out.println("\nSign up.");
			System.out.println("Username: ");
			String usernameString = scanner.nextLine();
			System.out.println("Password: ");
			String passwordString = scanner.nextLine();

			if (facade.checkManagerLogin(usernameString, passwordString)) {
				System.out.println("Signed up\n");
				break;
			}	
			else
				System.out.println("Invalid username or password");
		}
		this.menu();
	}

	private void registration() throws SQLException {
		while (true) {
			System.out.println("\nSign in.");
			System.out.println("Name: ");
			String managerName = scanner.nextLine();
			System.out.println("Surname: ");
			String managerSurname = scanner.nextLine();
			System.out.println("Username: ");
			String managerUsername = scanner.nextLine();
			System.out.println("Password:");
			String managerPassword = scanner.nextLine();
			System.out.println("Repeat password: ");
			String managerRepeatPassword = scanner.nextLine();

			if (!(managerPassword.equals(managerRepeatPassword))) {
				System.out.println("Passwords were different. Please retry. ");
				continue;
			}

			if (!this.facade.checkUnique(managerUsername)) {
				System.out.println("Username already used. Please change it. ");
				continue;
			}

			if (this.facade.storeManager(managerUsername, managerName, managerSurname, managerPassword)) {
				System.out.println("Signed in\n");
				break;
			}
			else
				System.out.println("Registration failed.");
		}
		this.login();
	}

	private void menu() {
		while (true) {
			System.out.println("** Main menu. Enter the number relate to your preference. **\n");
			System.out.println("1 - Get tournaments list.");
			System.out.println("2 - Create tournament.");
			System.out.println("Enter \"e\" to exit, \"l\" to log out.\n");

			System.out.print("Input: ");
			inputString = scanner.nextLine();

			if (inputString.contentEquals("e")) {
				System.out.println("Closing app...");
				System.exit(0);
			}
			if (inputString.contentEquals("l"))
				break;

			try {
				switch (Integer.parseInt(inputString)) {
				case 1:
					this.getTournamentsList();
					break;
				case 2:
					this.createTournament();
					break;
				default:
					System.out.println("Unavailable input.\n");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.\n");
			}
		}
	}

	private void getTournamentsList() {
		// lista ...
	}

	private void createTournament() {
		while (true) {
			System.out.println("Choose tounament type: \n");
			System.out.println("1 - LEAGUE");
			System.out.println("2 - KNOCKOUT PHASE");
			System.out.println("3 - MIXED\n");
			System.out.println("Enter \"e\" to exit, \"b\" to go back.\n");

			System.out.print("Input: ");
			inputString = scanner.nextLine();

			if (inputString.contentEquals("e")) {
				System.out.println("Closing app...");
				System.exit(0);
			}
			if (inputString.contentEquals("b"))
				break;

			try {

				switch (Integer.parseInt(inputString)) {
				case 1:
					createLeagueTournament();
					break;
				case 2:
					createKnockoutPhaseTournament();
					break;
				case 3:
					createMixedTournament();
					break;
				default:
					System.out.println("Unavailable input.\n");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input.\n");
			}
		}

	}

	private void createMixedTournament() {
		System.out.println("Enter tournament name: ");
		inputString = scanner.nextLine();

		ArrayList<Team> teams = new ArrayList<>();

		// poi andr� creato un ciclo per inserire tutte le squadre (questione del numero
		// limite in sospeso)
		System.out.println("Enter team name: ");
		String teamName = scanner.nextLine();
		Team team = new Team(teamName);
		teams.add(team);

		this.tournament = new MixedTournament(inputString);

	}

	private void createKnockoutPhaseTournament() {
		System.out.println("Enter tournament name: ");
		inputString = scanner.nextLine();

		ArrayList<Team> teams = new ArrayList<>();

		// poi andr� creato un ciclo per inserire tutte le squadre (questione del numero
		// limite in sospeso)
		System.out.println("Enter team name: ");
		String teamName = scanner.nextLine();
		Team team = new Team(teamName);
		teams.add(team);

		this.tournament = new KnockoutPhase(inputString);

	}

	private void createLeagueTournament() {
		System.out.println("Enter tournament name: ");
		inputString = scanner.nextLine();

		ArrayList<Team> teams = new ArrayList<>();

		// poi andr� creato un ciclo per inserire tutte le squadre (questione del numero limite in sospeso)
		System.out.println("Enter team name: ");
		String teamName = scanner.nextLine();
		Team team = new Team(teamName);
		teams.add(team);

		this.tournament = new League(inputString);

	}
}
