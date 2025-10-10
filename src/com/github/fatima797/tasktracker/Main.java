package com.github.fatima797.tasktracker;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		TaskManager taskManager = new TaskManager();

		if(args.length == 2) {
			if(args[0].equals("add")) {
				taskManager.addTask(args[1]);
			}else {
				System.out.println("Unknown command: " + args[0]);
			}
		}else if(args.length == 0) {
			System.out.println("Usage: java -jar task-cli.jar <command> [arguments]");
		}else if(args.length == 1) {
			System.out.println("Missing task description. Usage: 'add \\\"<description>\\\"'");
		}else {
			System.out.println("Invalid number of arguments");
		}


	}

}
