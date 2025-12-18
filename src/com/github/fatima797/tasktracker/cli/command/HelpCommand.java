package com.github.fatima797.tasktracker.cli.command;

import com.github.fatima797.tasktracker.view.ConsoleView;

public class HelpCommand implements Command {
	private final ConsoleView view;

	public HelpCommand(ConsoleView view) {
		super();
		this.view = view;
	}

	@Override
	public void execute(String[] args) {
		view.showHelp();
		
	}	
}
