import java.util.List;
final class TaskList {
    private final int userId;
    private final int taskListId;
    private final List<Task> tasks;
    private final List<Collaborator> collaborators;
    public TaskList(int userId, int taskListId,List<Task> tasks,List<Collaborator> collaborators) {
        this.userId = userId;
        this.taskListId = taskListId;
        this.tasks = tasks;
        this.collaborators=collaborators;
    }
    public int getTaskListId() {
        return taskListId;
    }
    public int getUserId() {
        return userId;
    }
    public List<Collaborator> getCollaborator() {
        return collaborators;
    }
    public List<Task> getTasks() {
        return tasks;
    }
}
