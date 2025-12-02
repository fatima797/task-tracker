package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.service.TaskQueryService;
import com.github.fatima797.tasktracker.view.ConsoleView;

/**
 * Handles the 'list' CLI command.
 * 
 * <p>This class is a concrete implementation of the {@link Command} interface as 
 * part of the Command pattern. It delegates the display logic to {@link TaskQueryService}.</p>
 * 
 * <p>The 'list' command supports two modes of operation based on the 
 * number of arguments provided:</p>
 * <ul>
 *     <li><code>list</code> – lists all tasks</li>
 *     <li><code>list &lt;status&gt;</code> – filters tasks by status 
 *         (TODO, IN_PROGRESS, DONE)</li>
 * </ul>
 * 
 */

public class ListCommand implements Command {
	private final TaskQueryService queryService;
	private final ConsoleView view;

	public ListCommand(TaskQueryService queryService, ConsoleView view) {
		super();
		this.queryService = queryService;
		this.view = view;
	}

	@Override
	public void execute(String[] args) {
		if (args.length == 1) {
			queryService.listAll();
			return;
		}

		if (args.length == 2) {
			queryService.listByStatus(args[1]);
			return;
		}

		view.showError("Invalid list command.");
	}
}
