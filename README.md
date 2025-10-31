# Task Tracker

A simple command-line Task Tracker application built in Java. This tool allows users to manage their tasks directly from the terminal — including adding, updating, deleting, and marking tasks as done or in progress. Tasks are stored persistently in a JSON file.

---

## 📦 Features

- Add tasks
- Update tasks
- Delete tasks
- Mark tasks as `in progress`
- Mark tasks as `done`
- List all tasks
- List only completed tasks
- List tasks in progress
- List tasks not yet started

---

## 🚀 Getting Started

Follow these steps to run the project:

### 1. Clone the repository

```bash
git clone https://github.com/fatima797/task-tracker.git
cd task-tracker
```

### 2. Run the JAR file

To run the project, use:

```bash
java -jar task-tracker.jar <command> [arguments]
```

💡 Example Usage

### Add a task

```bash
java -jar task-tracker.jar add "Buy groceries"
```

### Updating and deleting tasks

```bash
java -jar task-tracker.jar update 1 "Buy groceries and cook dinner"
java -jar task-tracker.jar delete 1
```

### Mark a task as in progress or done

```bash
java -jar task-tracker.jar mark-in-progress 1
java -jar task-tracker.jar mark-done 1
```

### List all tasks

```bash
java -jar task-tracker.jar list
```

### List only by status

```bash
java -jar task-tracker.jar list done
java -jar task-tracker.jar list todo
java -jar task-tracker.jar list in-progress
```

---

## 🟢 Project Status: In Progress

### ✅ Completed Features:

- **Add** new tasks
- **Delete** task by ID and update json file
- **Persist** tasks to JSON file
- **Load** existing tasks from JSON file
- Ensure ID continuity by loading the highest ID and assigning incremental IDs to new tasks.

### Next Feature:

Implement `updateTask()` method to allow task editing.

---

## 🌐 Project Repository

You can view the project repository here:
https://github.com/fatima797/task-tracker
