import java.time.LocalDate;
import java.util.List;
final  class Task {
    private final int taskId;
    private final int taskListId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int priority;
    private  Category category;
    private final RecurringTasks period;
    private final List<Collaborator> collaborator;
    private  int isCompleted;
    public Task(int taskId, int taskListId, String title, String description, LocalDate startDate, LocalDate endDate, int priority, Category category,RecurringTasks period, List<Collaborator> collaborator , int isCompleted) {
        this.taskId = taskId;
        this.taskListId = taskListId;
        this.title = title;
        this.description = description;
        this.startDate=startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.category = category;
        this.period = period;
        this.collaborator=collaborator;
        this.isCompleted = isCompleted;
    }
    public int getTaskId() {
        return taskId;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getTaskListId() {
        return taskListId;
    }
    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }
    public int getIsCompleted() {
        return isCompleted;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Category getCategory() {
        return category;
    }
    public RecurringTasks getPeriod() {
        return period;
    }
    public String getTitle() {
        return title;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public int getPriority() {
        return priority;
    }
    public  List<Collaborator> getCollaborator() {
        return collaborator;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return String.valueOf(taskId);
    }
}
