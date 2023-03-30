import java.time.LocalDate;
import java.util.*;
public class Manage {
	//static Data data = DataList.getInstance();
	static Data data = DataDB.getInstance();
	static boolean signUp(String name,String email,String password)  {
		return data.addUser(new User(Main.random.nextInt(999), name, email, password));
	}
	static User login(String email, String password)  {
		for (User user : data.getUserCredentials()) {
			if (user.getEmail().equals(email) && user.getPassword().equals(password)) return data.getUserByEmail(email);
		}
		return null;
	}
	static List<User> displayUsers()  {
		return data.getUsers();
	}
	static List<Task> viewTasks(int taskListId){
		return getTaskListByTaskListId(taskListId).getTasks();
	}
	static List<Task> viewTaskOnPriority(List<Task> tasks){
		tasks.sort((task1, task2) -> task2.getPriority() - task1.getPriority());
		return tasks;
	}
	static List<Task> viewCurrentTasks(List<Task> tasks){
		List<Task> currentTasks = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		for(Task task : tasks)
			if(task.getStartDate().minusDays(1).isBefore(currentDate) && task.getEndDate().plusDays(1).isAfter(currentDate)) currentTasks.add(task);
		return currentTasks;
	}
	static List<Task> viewTasksByCategory(List<Task> tasks, Category category) {
		List<Task> filteredTasks = new ArrayList<>();
		for(Task task : tasks)
			if (task.getCategory().equals(category)) filteredTasks.add(task);
		return filteredTasks;
	}
	static List<TaskList> viewTaskList(int userId){
		return data.getTaskListsByUserId(userId);
	}
	static List<TaskList> viewSharedTaskList(User user) {
		List<TaskList> sharedTaskLists = new ArrayList<>();
		for(int i : getSharedTaskListIdOfUser(user.getUserId())){
			sharedTaskLists.add(getTaskListByTaskListId(i));
		}
		return sharedTaskLists;
	}
	static List<Task> viewSharedTask(User user){
		List<Task> sharedTasks = new ArrayList<>();
		for(int taskId : data.getSharedTaskIdsByUserId(user.getUserId())){
			sharedTasks.add(data.getTaskByTaskId(taskId));
		}return sharedTasks;
	}
	static boolean addTaskList(User user){
		return data.addTaskList(new TaskList(user.getUserId(), Main.random.nextInt(99999), new ArrayList<>(),new ArrayList<>()));
	}
	static boolean deleteTaskList(TaskList taskList)  {
		for(Task task : taskList.getTasks()) data.deleteSharedTasksByTaskId(task.getTaskId());
		data.deleteTasksByTaskListId(taskList.getTaskListId());
		data.deleteSharedTaskListByTaskListId(taskList.getTaskListId());
		data.deleteTaskListByTaskListId(taskList.getTaskListId());
		return true;
	}
	static boolean deleteTask(int taskId) {
		return data.deleteSharedTasksByTaskId(taskId) && data.deleteTaskByTaskId(taskId);
	}
	static boolean editTask(Task task,String title,String description,int priority,Category category,LocalDate startDate,LocalDate endDate,int isCompleted){
		return data.updateTask(task.getTaskId(), title, description, priority,category,startDate,endDate, isCompleted);
	}
	static Collaborator getCollaboratorForTaskList(int taskListId, int userId){
		return data.getCollaboratorForTaskList(taskListId,userId);
	}
	static Collaborator getCollaboratorForTask(int taskId, int userId){
		return data.getCollaboratorForTask(taskId,userId);
	}
	static Task getTaskByTaskId(int taskId){
		return data.getTaskByTaskId(taskId);
	}
	static TaskList getTaskListByTaskListId(int taskListId){
		return data.getTaskListByTaskListId(taskListId);
	}
	static User getUserByEmail(String email){
		return data.getUserByEmail(email);
	}
	static boolean shareTask(int taskId,User collabUser,int canEdit)  {
		return data.addSharedTask(taskId, collabUser.getUserId(),canEdit);
		}
	static boolean removeShareTask(int taskId,int userId) {
		return data.deleteSharedTask(taskId, userId);
	}
	static boolean shareTaskList(int taskListId ,int userId ,int canEdit)  {
		return data.addSharedTaskList(taskListId, userId ,canEdit);
	}
	static boolean removeShareTaskList(int taskListId,int userId)  {
		return data.deleteSharedTaskList(taskListId,userId);
	}
	static String getPassword(int userId){
		return data.getPassword(userId);
	}

	static boolean  closeAccount(User user)throws InvalidDataException {
		List<TaskList> taskLists = data.getTaskListsByUserId(user.getUserId());
		for (TaskList taskList : taskLists) {
			for (Task task : taskList.getTasks()) data.deleteSharedTasksByTaskId(task.getTaskId());
			data.deleteTasksByTaskListId(taskList.getTaskListId());
			data.deleteSharedTaskListByTaskListId(taskList.getTaskListId());
		}
		data.deleteSharedTasksByUserId(user.getUserId()) ;
		data.deleteSharedTaskListsByUserId(user.getUserId()) ;
		data.deleteTaskListsByUserId(user.getUserId()) ;
		data.deleteUser(user);
		return true;
	}
	static List<String> getUserNamesByUserId(List<Integer> userIds){
		List<String> userNames = new ArrayList<>();
		for(Integer userId:userIds){
			String name = data.getUserName(userId);
			userNames.add(name);
		}
		return userNames;
	}
	static String getUserNameByUserId(int userId){
		return data.getUserName(userId);
	}
	static List<Integer> getSharedTaskListIdOfUser(int userId){
		return data.getSharedTaskListIdsByUserId(userId);
	}

	static boolean generateNewTasks(Task task, int recurringNumber) {
		RecurringTasks period = task.getPeriod();
		long days = period.unit;
		data.addTask(task);
		while(recurringNumber!=0 ) {
			task.setStartDate(task.getStartDate().plusDays(days));
			task.setEndDate(task.getEndDate().plusDays(days));
			data.addTask(new Task(Main.random.nextInt(9999), task.getTaskListId(), task.getTitle(), task.getDescription(),
					task.getStartDate(), task.getEndDate(), task.getPriority(), task.getCategory(), period, task.getCollaborator(),
					task.getIsCompleted()));
			recurringNumber--;
		}
		return true;
	}
	static Share findSelectType(int input) {
		for(Share share : Share.values()) if(share.select == input) return share;
		return null;
	}
	static ViewTasks findViewType(int input) {
		for(ViewTasks viewTasks : ViewTasks.values()) if(viewTasks.view == input) return viewTasks;
		return null;
	}
	static ViewTaskLists findViewTaskListOption(int input) {
		for(ViewTaskLists view : ViewTaskLists.values()) if(view.input == input) return view;
		return null;
	}
	static RecurringTasks findRecurringType(int input){
		for(RecurringTasks task : RecurringTasks.values()){
			if(task.unit== input) return task;
		}return null;
	}
	static Category findCategoryType(int input){
		for (Category category : Category.values()) {
			if (category.category == input) return category;
		}return null;
	}
	static TaskActions findOption(int input){
		for(TaskActions taskAction : TaskActions.values()) if(taskAction.option == input) return taskAction;
		return null;
	}
	static TaskListActions findType(int input) {
		for(TaskListActions taskListAction : TaskListActions.values()) if(taskListAction.choose == input) return taskListAction;
		return null;
	}
	static EditTaskLists findEditTaskListOption(int value){
		for(EditTaskLists edit : EditTaskLists.values()) if(edit.input == value) return edit;
		return null;
	}
	static EditTasks findEditTasksOption(int value){
		for(EditTasks edit : EditTasks.values()) if(edit.input == value) return edit;
		return null;
	}
	static Entry findChoice(int input){
		for(Entry entry : Entry.values()) if(entry.choice == input) return entry;
		return null;
	}
	static Edit findEdit(int input){
		for(Edit edit:Edit.values()) if (edit.edit==input) return edit;
		return null;
	}
}

