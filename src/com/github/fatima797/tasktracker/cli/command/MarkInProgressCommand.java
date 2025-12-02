package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.service.TaskWriteService;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles the 'mark-in-progress' CLI command.
 *
 * Marks a task as IN_PROGRESS using {@link TaskWriteService#markInProgress(int)}.
 *
 * <p>This class is a concrete implementation of the {@link Command} interface as 
 * part of the Command pattern.</p>
 */

public class MarkInProgressCommand implements Command {
	private final TaskWriteService writeService;
	private final ConsoleView view;

	public MarkInProgressCommand(TaskWriteService writeService, ConsoleView view) {
		super();
		this.writeService = writeService;
		this.view = view;
	}



	@Override
	public void execute(String[] args) {
		if(args.length < 2) {
			view.showError("mark-in-progress requires a task ID");
			return;
		}
		
		try {
			int id = Integer.parseInt(args[1]);
			writeService.markInProgress(id);
			
		} catch (NumberFormatException e) {
			view.showError("task ID must be a number.");
			e.printStackTrace();
		}
		
	}

}
