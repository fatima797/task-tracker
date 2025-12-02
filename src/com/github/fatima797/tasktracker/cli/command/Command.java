package com.github.fatima797.tasktracker.cli.command;

/**
 * Represents a single executable command in the CLI.
 * 
 * Part of the Command pattern implementation.
 * 
 * <p>Implementations receive the CLI argument array and are
 * responsible for validating their own usage and parameters.</p>
 */

public interface Command {
	void execute(String[] args);
}
