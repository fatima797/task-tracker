package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.service.TaskWriteService;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles the 'mark-done' CLI command.
 * 
 * Marks a task as DONE by delegating to {@link TaskWriteService#markTaskAsDone(int)}.
 * 
 * <p>This class is a concrete implementation of the {@link Command} interface as 
 * part of the Command pattern.</p>
 */

public class MarkDoneCommand implements Command {
	private final TaskWriteService writeService;
	private final ConsoleView view;

	public MarkDoneCommand(TaskWriteService writeService, ConsoleView view) {
		super();
		this.writeService = writeService;
		this.view = view;
	}


	@Override
	public void execute(String[] args) {
		if(args.length < 2) {
			view.showError("mark-done requires a task ID");
			return;
		}
		
		try {
			int id = Integer.parseInt(args[1]);
			
			writeService.markTaskAsDone(id);
		} catch (NumberFormatException e) {
			view.showError("ID must be a number.");
			e.printStackTrace();
		}
		
	}

}
