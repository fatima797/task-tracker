package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.service.TaskWriteService;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles the 'add' CLI command.
 *
 * <p>This class is a concrete implementation of the {@link Command} interface as part of the Command pattern. 
 * Creates a new task using {@link TaskWriteService}. Validates that a task description is provided before 
 * delegating to the write service.</p>
 */

public class AddCommand implements Command{
	private final TaskWriteService writeService;
	private final ConsoleView view;

	public AddCommand(TaskWriteService writeService, ConsoleView view) {
		this.writeService = writeService;
		this.view = view;
	}

	@Override
	public void execute(String[] args) {
		if(args.length < 2) {
			view.showError("'add' requires a description.");
			return;
		}
		
		writeService.addTask(args[1]);
	}

}
