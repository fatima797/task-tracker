package com.github.fatima797.tasktracker.cli;

import com.github.fatima797.tasktracker.cli.command.AddCommand;
import com.github.fatima797.tasktracker.cli.command.Command;
import com.github.fatima797.tasktracker.cli.command.CommandRegistry;
import com.github.fatima797.tasktracker.cli.command.DeleteCommand;
import com.github.fatima797.tasktracker.cli.command.ListCommand;
import com.github.fatima797.tasktracker.cli.command.MarkDoneCommand;
import com.github.fatima797.tasktracker.cli.command.MarkInProgressCommand;
import com.github.fatima797.tasktracker.cli.command.UpdateCommand;
import com.github.fatima797.tasktracker.repository.FileTaskRepository;
import com.github.fatima797.tasktracker.repository.TaskRepository;
import com.github.fatima797.tasktracker.service.TaskQueryService;
import com.github.fatima797.tasktracker.service.TaskWriteService;
import com.github.fatima797.tasktracker.view.ConsoleView;

public class Main {

	public static void main(String[] args) {
		// Dependencies
		ConsoleView view = new ConsoleView();
		TaskRepository repository = new FileTaskRepository(view);
		
		// Services
		TaskQueryService queryService = new TaskQueryService(repository, view);
		TaskWriteService writeService = new TaskWriteService(repository, view);
		
		// Create Command registry and register commands
		CommandRegistry registry = new CommandRegistry();
		
		registry.register("add", new AddCommand(writeService, view));
		registry.register("delete", new DeleteCommand(writeService, view));
		registry.register("update", new UpdateCommand(writeService, view));
		registry.register("mark-done", new MarkDoneCommand(writeService, view));
		registry.register("mark-in-progress", new MarkInProgressCommand(writeService, view));
		
		registry.register("list", new ListCommand(queryService, view));
		
		// If no argument provided
		if(args.length == 0) {
			view.showError("No command provided. Try: add, delete, list, update, mark-done, mark-in-progress.");
			return;
		}
		
		String commandName = args[0].toLowerCase();
		
		// If unknown command
		if(!registry.has(commandName)) {
			view.showError("Uknown command: " + commandName);
			return;
		}
		
		Command command = registry.get(commandName);
		command.execute(args);
	}
}
