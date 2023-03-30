import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class DataList implements Data {
    private static Data data = DataList.getInstance();
    private DataList() {}
    public static Data getInstance() {
        if (data == null) data = new DataList();
        return data;
    }
    private static final List<User> users = new ArrayList<>();
    private static final List<TaskList> taskLists = new ArrayList<>();
    private static final List<Task> tasks = new ArrayList<>();
    private static final List<Collaborator> sharedTasks = new ArrayList<>();
    private static final List<Collaborator> sharedTaskLists = new ArrayList<>();
    public boolean addUser(User user) {
    users.add(user);
    return true;
    }
    public boolean deleteUser(User user) {
        users.remove(user);
        return true;
    }
    public List<User> getUsers() {return users;}
    public List<User> getUserCredentials() {return users;}
    public User getUserByEmail(String email) {
       for(User user:users){
           if(user.getEmail().equals(email)) return user;
       }
        return null;
    }
    public String getPassword(int userId){
        for(User user : users){
            if(user.getUserId()==userId) return user.getPassword();
        }return null;
    }
    public String  getUserName(int userId){
        for(User user : users){
            if(user.getUserId()==userId) return user.getUserName();
        }return null;
    }
    public boolean addTaskList(TaskList taskList) {
        taskLists.add(taskList);
        return true;
    }
    public boolean deleteTaskListByTaskListId(int taskListId) {
        taskLists.removeIf(taskList -> taskList.getTaskListId() == taskListId);
        return true;
    }
    public boolean deleteTaskListsByUserId(int userId) {
        taskLists.removeIf(taskList -> taskList.getUserId() == userId);
        return true;
    }
    public List<TaskList> getTaskListsByUserId(int userId) {
        List<TaskList> userTaskLists = new ArrayList<>();
        for(TaskList taskList :taskLists){
            if(taskList.getUserId()==userId) userTaskLists.add(taskList);
        }
        return userTaskLists;
    }
    public TaskList getTaskListByTaskListId(int taskListId) {
        for(TaskList taskList : taskLists){
            if(taskList.getTaskListId()==taskListId) return taskList;
        }
        return null;
    }
    public boolean addTask(Task task) {
        tasks.add(task);
        return true;
    }
    public Task getTaskByTaskId(int taskId) {
        for(Task task:tasks){
            if(task.getTaskId()==taskId) return task;
        }return null;
    }
    public List<Task> getTasksByTaskListId(int taskListId) {
       List<Task> userTasks = new ArrayList<>();
       for(Task task:tasks){
           if(task.getTaskListId()==taskListId) userTasks.add(task);
       }return userTasks;
    }
    public boolean deleteTaskByTaskId(int taskId) {
        tasks.removeIf(task -> task.getTaskId() == taskId);
        return true;
    }
    public boolean deleteTasksByTaskListId(int taskListId) {
        tasks.removeIf(task -> task.getTaskListId() == taskListId);
        return true;
    }
    public boolean updateTask(int taskId, String title, String description, int priority, Category category, LocalDate startDate , LocalDate endDate, int isCompleted) {
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setIsCompleted(isCompleted);
                task.setCategory(category);
                task.setStartDate(startDate);
                task.setEndDate(endDate);
                return true;
            }
        }
        return false;
    }
    public boolean addSharedTask(int taskId, int userId, int canEdit) {
        sharedTasks.add(new Collaborator(taskId,userId,canEdit));
        return true;
    }
    public boolean deleteSharedTask(int taskId, int userId) {
        sharedTasks.removeIf(c -> c.getUserId() == userId && c.getId() == taskId);
        return true;
    }
    public boolean deleteSharedTasksByUserId(int userId) {
        sharedTasks.removeIf(c -> c.getUserId() == userId);
        return true;
    }
    public boolean deleteSharedTasksByTaskId(int taskId) {
        sharedTasks.removeIf(c -> c.getId() == taskId);
        return true;
    }
    public List<Integer> getSharedTaskIdsByUserId(int userId) {
        List<Integer> sharedTaskIds = new ArrayList<>();
        for (Collaborator c : sharedTasks) {
            if(c.getUserId()==userId) sharedTaskIds.add(c.getId());
        }
        return sharedTaskIds;
    }
    public Collaborator getCollaboratorForTask(int taskId, int userId) {
        for(Collaborator collab : sharedTaskLists ){
            if(collab.getUserId()==userId && collab.getId()==taskId) return collab;
        }
        return null;
    }
    public boolean addSharedTaskList(int taskListId, int userId, int canEdit) {
        sharedTaskLists.add(new Collaborator(taskListId,userId,canEdit));
        return true;
    }
    public boolean deleteSharedTaskList(int taskListId, int userId) {
        sharedTaskLists.removeIf(c -> c.getUserId() == userId && c.getId() == taskListId);
        return true;
    }
    public boolean deleteSharedTaskListsByUserId(int userId) {
        sharedTaskLists.removeIf(c -> c.getUserId() == userId);
        return true;
    }
    public boolean deleteSharedTaskListByTaskListId(int taskListId) {
        sharedTaskLists.removeIf(c -> c.getId() == taskListId);
        return true;
    }
    public List<Integer> getSharedTaskListIdsByUserId(int userId) {
        List<Integer> sharedTaskListIds = new ArrayList<>();
        for (Collaborator c : sharedTaskLists) {
            if(c.getUserId()==userId) sharedTaskListIds.add(c.getId());
        }
        return sharedTaskListIds;
    }
    public Collaborator getCollaboratorForTaskList(int taskListId, int userId) {
        for(Collaborator collab : sharedTasks ){
            if(collab.getUserId()==userId && collab.getId()==taskListId) return collab;
        }
        return null;
    }
}