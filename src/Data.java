import java.time.LocalDate;
import java.util.List;
public interface Data {
    boolean addUser(User user);
    boolean deleteUser(User user);
    List<User> getUsers();
    List<User> getUserCredentials();
    User getUserByEmail(String email);
    String getPassword(int userId);
    String getUserName(int userId);

    boolean addTaskList(TaskList taskList);
    boolean deleteTaskListByTaskListId(int taskListId);
    boolean deleteTaskListsByUserId(int userId);
    List<TaskList> getTaskListsByUserId(int userId);
    TaskList getTaskListByTaskListId(int taskListId);

    boolean addTask(Task task);
    Task getTaskByTaskId(int taskId);
    List<Task> getTasksByTaskListId(int taskListId );
    boolean deleteTaskByTaskId(int taskId);
    boolean deleteTasksByTaskListId(int taskListId);
    boolean updateTask(int taskId, String title, String description, int priority, Category category, LocalDate startDate,LocalDate endDate,int isCompleted);

    boolean addSharedTask(int taskId, int userId,int canEdit);
    boolean deleteSharedTask(int taskId, int userId);
    boolean deleteSharedTasksByUserId(int userId);
    boolean deleteSharedTasksByTaskId(int taskId);
    List<Integer> getSharedTaskIdsByUserId(int userId);
    Collaborator getCollaboratorForTask(int taskId,int userId);

    boolean addSharedTaskList(int taskListId,int userId,int canEdit);
    boolean deleteSharedTaskList(int taskListId,int userId);
    boolean deleteSharedTaskListsByUserId(int userId);
    boolean deleteSharedTaskListByTaskListId(int taskListId);
    List<Integer> getSharedTaskListIdsByUserId(int userId);
    Collaborator getCollaboratorForTaskList(int taskListId,int userId);
}
