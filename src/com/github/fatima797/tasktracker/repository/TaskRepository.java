package com.github.fatima797.tasktracker.repository;

import java.util.List;
import java.util.Optional;

import com.github.fatima797.tasktracker.model.Status;
import com.github.fatima797.tasktracker.model.Task;

/**
 * Abstraction for loading and saving tasks.
 * 
 * Defines the contract for repositories within the application.
 */

public interface TaskRepository {
	List<Task> findAll();
    Optional<Task> findById(int id);
    List<Task> findByStatus(Status status);
    void add(Task task);
    boolean update(Task task);
    boolean delete(int id);
    	
}
