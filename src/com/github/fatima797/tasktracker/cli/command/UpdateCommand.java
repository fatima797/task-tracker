package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.service.TaskWriteService;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles the 'update' CLI command.
 * 
 * <p>This class is a concrete implementation of the {@link Command} interface as 
 * part of the Command pattern. 
 * 
 * <p>It validates arguments and updates the description of a task using
 * {@link TaskWriteService}. Ensures that both an ID and new
 * description are provided.</p>
 */

public class UpdateCommand implements Command {
	private final TaskWriteService writeService;
	private final ConsoleView view;

	public UpdateCommand(TaskWriteService writeService, ConsoleView view) {
		super();
		this.writeService = writeService;
		this.view = view;
	}


	@Override
	public void execute(String[] args) {
		if(args.length < 3) {
			view.showError("Update task usage: update <id> \"new description\"");
			return;
		}
		
		try {
			int id = Integer.parseInt(args[1]);
			String description = args[2];
			writeService.updateTask(id, description);
		} catch (NumberFormatException e) {
			view.showError("ID must be an integer");
			e.printStackTrace();
		}
		
	}

}
