package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.service.TaskWriteService;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles the 'delete' CLI command.
 * 
 * <p>This class is a concrete implementation of the {@link Command} interface as 
 * part of the Command pattern. It validates that a numeric task ID is provided
 * and delegates the delete of a task to {@link TaskWriteService}.</p>
 */

public class DeleteCommand implements Command {
	private final TaskWriteService writeService;
	private final ConsoleView view;

	public DeleteCommand(TaskWriteService writeService, ConsoleView view) {
		super();
		this.writeService = writeService;
		this.view = view;
	}


	@Override
	public void execute(String[] args) {
		if(args.length < 2) {
			view.showError("delete command requires an integer task ID");
			return;
		}
		
		try {
			int id = Integer.parseInt(args[1]);
			writeService.deleteTask(id);
			
		} catch (NumberFormatException e) {
			view.showError("task ID must be a number.");
			e.printStackTrace();
		}
	}

}
