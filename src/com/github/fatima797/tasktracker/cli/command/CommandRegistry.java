package com.github.fatima797.tasktracker.cli.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Central registry for mapping CLI command names to their corresponding
 * {@link Command} implementations.
 */

public class CommandRegistry {
	private final Map<String, Command> commands = new HashMap<>();
	
	public void register(String name, Command command) {
		commands.put(name.toLowerCase(), command);
	}
	
	public Command get(String name) {
		return commands.get(name.toLowerCase());
	}
	
	public boolean has(String name) {
		return commands.containsKey(name.toLowerCase());
	}

}
