package com.github.fatima797.tasktracker;

public class Main {

	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		switch (args.length) {
			case 0:
				// Handles 0 arguments: java -jar task-tracker.jar 
				System.out.println("Usage: java -jar task-tracker.jar <command> [arguments]");
				break;

			case 1:
				// Handles 1 argument
				String command1 = args[0].toLowerCase();
				
				switch (command1) {
				case "list":
					// Command: java -jar task-tracker.jar list
					taskManager.listAll();
					break;
				default:
					System.out.println("Error: Command '" + command1 + "' is unknown or requires additional arguments.");
				}
				break;

			case 2:
				// Handles 2 arguments
				String command2 = args[0].toLowerCase();

				switch(command2) {
				case "add":
					// Command: java -jar task-tracker.jar add "description"
					taskManager.addTask(args[1]);
					break;

				case "delete":
					// Command: java -jar task-tracker.jar delete ID
					try {
						taskManager.deleteTask(Integer.parseInt(args[1]));
					} catch (NumberFormatException e) {
						System.out.println("Error: Delete command requires an integer ID.");
					}
					break;

				case "list":
					// Command: java -jar task-tracker.jar list STATUS
					taskManager.listByStatus(args[1]);
					break;

				default:
					System.out.println("Error: Unknown command '" + command2 + "'.");
				}
				break;

			case 3:
				// Handles 3 arguments
				String command3 = args[0].toLowerCase();

				switch(command3) {
				case "update":
					// Command: java -jar task-tracker.jar update ID "description"
					try {
						taskManager.updateTask(Integer.parseInt(args[1]), args[2]);
					} catch (NumberFormatException e) {
						System.out.println("Error: Update command requires an integer ID as the second argument.");
					}
					break;

				default:
					System.out.println("Error: Unknown command '" + command3 + "'.");
				}
				break;

			default:
				System.out.println("Error: Invalid number of arguments. Check usage for help." );
		}
	}

}
