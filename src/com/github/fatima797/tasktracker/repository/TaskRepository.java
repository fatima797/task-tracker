package com.github.fatima797.tasktracker.repository;

import java.util.List;

import com.github.fatima797.tasktracker.model.Task;

public interface TaskRepository {
	void save(List<Task> tasks);
	List<Task> load();

}
