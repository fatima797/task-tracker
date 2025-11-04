package com.github.fatima797.tasktracker;

public class Main {

	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		switch (args.length) {
		case 0:
			System.out.println("Usage: java -jar task-tracker.jar <command> [arguments]");
			break;

		case 2:
			// Handle command with 1 argument (e.g., add "desc", delete ID, list STATUS)
			String command2 = args[0].toLowerCase();

			switch(command2) {
			case "add":
				taskManager.addTask(args[1]);
				break;

			case "delete":
				// If input is non-numeric, add error handling
				try {
					taskManager.deleteTask(Integer.parseInt(args[1]));
				}catch (NumberFormatException e) {
					System.out.println("Error: Delete command requires integer ID.");
				}
				break;

			case "list":
				taskManager.listByStatus(args[1]);
				break;

			default:
				System.out.println("Unknown command '" + command2 + "'.");
			}
			break;

		case 3:
			// Handle commands with 2 arguments (e.g., update ID "desc")
			String command3 = args[0].toLowerCase();

			switch(command3) {
			
			case "update":
				try {
					taskManager.updateTask(Integer.parseInt(args[1]), args[2]);
				}catch (NumberFormatException e) {
					System.out.println("Update command requires integer ID.");
				}
				break;
				
			default:
				System.out.println("Unknown command '" + command3 + "'.");
			}
			break;
			
		default:
			System.out.println("Error: Invalid number of arguments. Check usage for help." );
		}
	}

}
