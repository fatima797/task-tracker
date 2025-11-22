package com.github.fatima797.tasktracker.model;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskTest {
	
	@Test
	void constructorShouldSetAllFieldsCorrectly() {
		LocalDateTime now = LocalDateTime.now();
		
		Task task = new Task(1, "Learn JUnit", Status.IN_PROGRESS, now, now);
		
		assertEquals(1, task.getId());
		assertEquals("Learn JUnit", task.getDescription());
		assertEquals(Status.IN_PROGRESS, task.getStatus());
		assertEquals(now, task.getCreatedAt());
		assertEquals(now, task.getUpdatedAt());
	
	}
	
	@Test
	void testSetDescription_updateFieldCorrectly() {
		String initialDesc = "Old desc";
		LocalDateTime initialUpdateTime = LocalDateTime.now().minusSeconds(1);
		String newDesc = "New desc";
		
		Task task = new Task(1, initialDesc, Status.TODO, initialUpdateTime, initialUpdateTime);
		
		task.setDescription(newDesc);
		
		assertEquals(newDesc, task.getDescription());
	}
	
	@Test
	void testUpdateTimestampEffect_afterSetDescription() {
		LocalDateTime initialUpdateTime = LocalDateTime.now().minusSeconds(1); 
	    Task task = new Task(1, "Desc", Status.TODO, initialUpdateTime, initialUpdateTime);
	    
	    task.setDescription("New Desc");
	    
	    assertTrue(task.getUpdatedAt().isAfter(initialUpdateTime), "The updatedAt timestamp must be newer than the initial time");
	}
	
	@Test
	void defaultConstructorShouldSetStatusToTodo() {
		Task task = new Task(1, "Test");
		
		assertEquals(Status.TODO, task.getStatus());
	}
	
	@Test
	void defaultConstructorShouldSetTimestampsNearNow() {
		LocalDateTime before = LocalDateTime.now();
		Task task = new Task(1, "Test");
		LocalDateTime after = LocalDateTime.now();
		
		assertTrue(!task.getCreatedAt().isBefore(before) && !task.getCreatedAt().isAfter(after));
		assertTrue(!task.getUpdatedAt().isBefore(before) && !task.getUpdatedAt().isAfter(after));
	}
	
	@Test
	void defaultConstructorShouldSetCreatedAtEqualToUpdatedAt() {
		Task task = new Task(1, "Test");
		
		assertEquals(task.getCreatedAt(), task.getUpdatedAt());
	}

}
