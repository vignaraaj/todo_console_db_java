import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class Main {
    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();
    public static void main(String[] args) {
        System.out.println("TODO APPLICATION");
        while (true) {
            System.out.println("1.Signup 2.login 3.Admin 4.Exit");
            Entry entry = Manage.findChoice(checkType());
            try {
                if (entry == null) continue;
                if(entry==Entry.EXIT) break;
                switch (entry) {
                    case SIGNUP:
                        System.out.print("Enter name : ");
                        String name = checkBlank();
                        System.out.print("Enter email : ");
                        String email = checkBlank();
                        System.out.print("Enter password : ");
                        String password = checkBlank();
                        User oldUser = Manage.getUserByEmail(email);
                        Utility.userExists(oldUser);
                        if (Manage.signUp(name, email, password))  System.out.println("Account added successfully");
                        else System.out.println("error occurred while adding new account ");
                        break;
                    case LOGIN:
                        System.out.print("Enter emailId : ");
                        String email1 = checkBlank();
                        System.out.print("Enter password : ");
                        String password1 = checkBlank();
                        User user = Manage.login(email1, password1);
                        Utility.checkInvalidLoginConstraint(user);
                        manageTaskList(user);
                        break;
                    case DISPLAY_USERS:
                        System.out.print("Enter adminId : ");
                        String admin = checkBlank();
                        System.out.print("Enter password : ");
                        String pass = checkBlank();
                        Utility.loginAdmin(admin, pass);
                        List<User> userDetails = Manage.displayUsers();
                        Utility.checkEmptyUsers(userDetails);
                        String format = ("| %15s  | %20s  |%n");
                        System.out.println("--------------------------------------------");
                        System.out.format(format, " User Name ", " Email ");
                        System.out.println("--------------------------------------------");
                        for (User users : userDetails) System.out.format(format, users.getUserName(), users.getEmail());
                        System.out.println("--------------------------------------------");
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    static void manageTaskList(User user)  {
        while (true) {
            System.out.println("1.Add taskList 2.view taskLists 3.view sharedTaskLists 4.view sharedTasks 5.closeAccount 6.Exit");
            ViewTaskLists viewTaskLists = Manage.findViewTaskListOption(checkType());
            if (viewTaskLists == null) continue;
            if(viewTaskLists==ViewTaskLists.EXIT) break;
            switch (viewTaskLists) {
                case ADD_TASK_LIST:
                    if (Manage.addTaskList(user)) System.out.println("New taskList has been added to your account");
                    else System.out.println("An error occurred while creating newTaskList, try again later");
                    break;
                case VIEW_TASK_LISTS:
                    taskListActions(user);
                    break;
                case VIEW_SHARED_TASK_LISTS:
                    sharedTaskListActions(user);
                    break;
                case VIEW_SHARED_TASKS:
                    sharedTasksActions(user);
                    break;
                case CLOSE_ACCOUNT:
                    System.out.print("confirm your password : ");
                    String password = checkBlank();
                    try {
                        String originalPassword = Manage.getPassword(user.getUserId());
                        Utility.checkPassWordConstraint(originalPassword,password);
                        if (Manage.closeAccount(user)) {
                            System.out.println("Account closed successfully");
                            return;
                        } else System.out.println("An error occurred while closing the account");
                    } catch (InvalidDataException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        }
    }
    static void taskListActions(User user){
        while(true) {
            List<TaskList> taskLists = Manage.viewTaskList(user.getUserId());
            System.out.println("Your taskLists...");
            if (displayTaskLists(taskLists)) {
                try {
                    System.out.println("1.Add task 2.Delete taskList 3.Use taskList 4.view TaskList 5.Share taskList 6.Exit");
                    TaskListActions taskListActions = Manage.findType(checkType());
                    if (taskListActions == null ) continue;
                    if(taskListActions==TaskListActions.EXIT) break;
                    System.out.print("Enter the taskListId to be operated : ");
                    int taskListId = checkType();
                    TaskList taskList = Manage.getTaskListByTaskListId(taskListId);
                    Utility.validTaskListIdConstraint(taskList);
                    Utility.checkTaskListBelongsToUser(taskList.getUserId(), user.getUserId());
                    switch (taskListActions) {
                        case ADD_TASK:
                            System.out.print("Enter title : ");
                            String title = checkBlank();
                            System.out.print("Enter description : ");
                            String description = checkBlank();
                            int priority;
                            while(true) {
                                try {
                                    System.out.print("Enter priority (1 to 10): ");
                                    priority = checkType();
                                    Utility.checkPriorityConstraint(priority);
                                    break;
                                } catch (InvalidDataException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            Category category;
                            while(true){
                                try{
                                    System.out.println("WORK(1),PERSONAL(2),LEARNING(3),SHOPPING(4),OTHER(5)");
                                    System.out.print("Enter category type (integer only) : ");
                                    category = Manage.findCategoryType(checkType());
                                    Utility.checkCategoryConstraint(category);
                                    break;
                                }catch(InvalidDataException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            LocalDate startDate;
                            while(true) {
                                try {
                                    System.out.print("Enter start date in (yyyy-mm-dd) format : ");
                                    String startDate1 = checkBlank();
                                    startDate = Utility.checkStartDateConstraint(startDate1);
                                    break;
                                }catch (DateTimeParseException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            LocalDate endDate;
                            while(true) {
                                try {
                                    System.out.print("Enter end date in (yyyy-mm-dd) format (should be past the start date or start date itself): ");
                                    String endDate1 = checkBlank();
                                    endDate = Utility.checkEndDateConstraint(startDate,endDate1);
                                    break;
                                }catch (DateTimeParseException | InvalidDataException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            RecurringTasks period;
                            while(true) {
                                try {
                                    System.out.println("1.DAILY 2.WEEKLY,3.MONTHLY");
                                    System.out.print("when the task to be recurred (valid integer only) : ");
                                    period = Manage.findRecurringType(checkType());
                                    Utility.checkPeriodConstraint(period);
                                    break;
                                }catch(InvalidDataException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            int recurringNumber;
                            while(true){
                                try {
                                    System.out.print("Enter the number of times to be recurred [0 to 50]: ");
                                    recurringNumber = checkType();
                                    Utility.checkRecurringNumber(recurringNumber);
                                    break;
                                }catch(InvalidDataException e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            int taskId = random.nextInt(9999);
                            Task task = new Task(taskId, taskListId, title, description, startDate, endDate, priority, category, period, new ArrayList<>(), 0);
                            if(Manage.generateNewTasks(task,recurringNumber))
                                System.out.println("Task added successfully");
                            else System.out.println("An error occurred while adding the task");
                            break;
                        case DELETE_TASK_LIST:
                            if (Manage.deleteTaskList(taskList))
                                System.out.println("TaskList deleted successfully");
                            else System.out.println("An error occurred while deleting the taskList");
                            break;
                        case USE_TASK_LIST:
                            useTask(user, taskListId);
                            break;
                        case VIEW_TASK:
                            viewTask(taskList.getTasks());
                            break;
                        case SHARE_TASK_LIST:
                            shareTaskList(user.getEmail(), taskListId);
                            break;
                    }
                } catch (AuthenticationException | InvalidDataException e) {
                    System.out.println(e.getMessage());
                }
            }
            else break;
        }
    }
    static void sharedTaskListActions(User user){
        while (true) {
            List<TaskList> sharedTaskLists = Manage.viewSharedTaskList(user);
            System.out.println("TaskLists shared to you");
            if (displayTaskLists(sharedTaskLists)) {
                try {
                    System.out.println("1.Edit shared taskList 2.Delete your copy of a shared taskList 3.view Tasks of shared TaskList 4.Exit");
                    EditTaskLists editOption = Manage.findEditTaskListOption(checkType());
                    if (editOption == null) continue;
                    if(editOption==EditTaskLists.EXIT) break;
                    System.out.print("Enter taskListId to be operated : ");
                    int taskListId = checkType();
                    TaskList taskList = Manage.getTaskListByTaskListId(taskListId);
                    Utility.validTaskListIdConstraint(taskList);
                    Collaborator collaboratorForTaskList = Manage.getCollaboratorForTaskList(taskList.getTaskListId(), user.getUserId());
                    Utility.isCollaboratorNull(collaboratorForTaskList);
                    switch (editOption) {
                        case EDIT_SHARED_TASK_LIST:
                                Utility.canEditStatus(collaboratorForTaskList);
                                System.out.print("Enter taskId to be edited : ");
                                int taskId = checkType();
                                Task task = Manage.getTaskByTaskId(taskId);
                                Utility.validTaskIdConstraint(task);
                                Utility.checkTaskListHasTask(task, taskListId);
                                Collaborator collaboratorForTask = Manage.getCollaboratorForTask(taskId, user.getUserId());
                                if(collaboratorForTask!=null) Utility.canEditStatus(collaboratorForTask);
                                editTask(task);
                            break;
                        case DELETE_SHARED_TASK_LIST:
                            if (Manage.removeShareTaskList(taskList.getTaskListId(), collaboratorForTaskList.getUserId()))
                                System.out.println("shared copy of taskList deleted successfully");
                            break;
                        case VIEW_TASKS_OF_SHARED_TASK_LIST:
                            displayTasks(taskList.getTasks());
                            break;
                    }
                } catch (InvalidDataException |AuthenticationException e) {
                    System.out.println(e.getMessage());
                }
            }
            else break;
        }
    }
    static void sharedTasksActions(User user){
        while (true) {
            List<Task> sharedTasks = Manage.viewSharedTask(user);
            System.out.println("Tasks shared to you...");
            if (displayTasks(sharedTasks)) {
                try {
                    System.out.println("1.Edit SharedTask 2.Delete SharedTask 3.Exit");
                    EditTasks editOption = Manage.findEditTasksOption(checkType());
                    if (editOption == null) continue;
                    if(editOption==EditTasks.EXIT) break;
                    System.out.print("Enter taskId to be operated : ");
                    int taskId = checkType();
                    Task task = Manage.getTaskByTaskId(taskId);
                    Utility.validTaskIdConstraint(task);
                    Collaborator collab = Manage.getCollaboratorForTask(taskId, user.getUserId());
                    Utility.isCollaboratorNull(collab);
                    switch (editOption) {
                        case EDIT_SHARED_TASK:
                            Utility.canEditStatus(collab);
                            editTask(task);
                            break;
                        case DELETE_SHARED_TASK:
                            Collaborator collabTaskList = Manage.getCollaboratorForTaskList(task.getTaskListId(), user.getUserId());
                            List<Integer> sharedTaskListId = Manage.getSharedTaskListIdOfUser(user.getUserId());
                            if(collabTaskList!=null) Utility.doTaskListOfTaskShared(sharedTaskListId, task.getTaskListId(), collab.getCanEdit(), collabTaskList.getCanEdit());
                            if (Manage.removeShareTask(taskId, user.getUserId())) System.out.println("shared copy of task deleted successfully");
                            break;
                    }
                } catch (InvalidDataException | AuthenticationException e) {
                    System.out.println(e.getMessage());
                }
            }
            else break;
        }
    }
    static void editTask(Task task) throws InvalidDataException {
        while(true) {
            System.out.println("1.Edit title 2.Edit description 3.Edit priority 4.Edit category 5.edit date 6.Edit completed status 7.Exit");
            Edit edit = Manage.findEdit(checkType());
            if (edit == null) continue;
            if(edit==Edit.Exit) break;
            switch (edit) {
                case TITLE:
                    System.out.print("Enter updated title : ");
                    String editTitle = checkBlank();
                    task.setTitle(editTitle);
                    break;
                case DESCRIPTION:
                    System.out.print("Enter updated description : ");
                    String editDescription = checkBlank();
                    task.setDescription(editDescription);
                    break;
                case PRIORITY:
                    int editPriority;
                    while (true) {
                        try {
                            System.out.print("Enter updated priority (1 to 10): ");
                            editPriority = checkType();
                            Utility.checkPriorityConstraint(editPriority);
                            break;
                        } catch (InvalidDataException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    task.setPriority(editPriority);
                    break;
                case CATEGORY:
                    Category category;
                    while (true) {
                        try {
                            System.out.println("WORK(1),PERSONAL(2),LEARNING(3),SHOPPING(4),OTHER(5)");
                            System.out.print("Enter updated category type (integer only) : ");
                            category = Manage.findCategoryType(checkType());
                            Utility.checkCategoryConstraint(category);
                            break;
                        } catch (InvalidDataException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    task.setCategory(category);
                    break;
                case DATE:
                    LocalDate startDate;
                    while (true) {
                        try {
                            System.out.print("Enter start date in (yyyy-mm-dd) format : ");
                            String startDate1 = checkBlank();
                            startDate = Utility.checkStartDateConstraint(startDate1);
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    LocalDate endDate;
                    while (true) {
                        try {
                            System.out.print("Enter end date in (yyyy-mm-dd) format (should be past the start date or start date itself): ");
                            String endDate1 = checkBlank();
                            endDate = Utility.checkEndDateConstraint(startDate, endDate1);
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    task.setStartDate(startDate);
                    task.setEndDate(endDate);
                    break;
                case IS_COMPLETED:
                    int isCompleted;
                    while (true) {
                        try {
                            System.out.print("Press 1 if you completed the task or 0 if not: ");
                            isCompleted = checkType();
                            Utility.checkCompleteStatusConstraint(isCompleted);
                            break;
                        } catch (InvalidDataException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    task.setIsCompleted(isCompleted);
                    break;
            }
            if (Manage.editTask(task, task.getTitle(), task.getDescription(), task.getPriority(), task.getCategory(), task.getStartDate(), task.getEndDate(), task.getIsCompleted()))
                System.out.println("task edited successfully");
            else System.out.println("An error occurred while editing the task");
        }
    }
    static void useTask (User user, int taskListId)  {
        while (true) {
            List<Task> tasks = Manage.viewTasks(taskListId);
            if(displayTasks(tasks)){
                try {
                    System.out.println("1.delete task 2.edit task 3.share task 4.exit");
                    TaskActions taskActions = Manage.findOption(checkType());
                    if (taskActions == null) continue;
                    if(taskActions==TaskActions.EXIT) break;
                    System.out.print("Enter taskId : ");
                    int taskId = checkType();
                    Task task = Manage.getTaskByTaskId(taskId);
                    Utility.validTaskIdConstraint(task);
                    Utility.checkTaskListHasTask(task, taskListId);
                    switch (taskActions) {
                        case DELETE_TASK:
                            if (Manage.deleteTask(task.getTaskId()))
                                System.out.println("task deleted successfully in all traces");
                            else System.out.println("an error occurred in deleting the task");
                            break;
                        case EDIT_TASK:
                            editTask(task);
                            break;
                        case SHARE_TASK:
                            shareTask(user.getEmail(), taskId);
                            break;
                    }
                } catch (InvalidDataException | AuthenticationException e) {
                    System.out.println(e.getMessage());
                }
            }
            else break;
        }
    }
    static void viewTask(List<Task> tasks) {
        while(true) {
            System.out.println("1.GroupTasksByCategory 2.DisplayTaskOnPriority 3.Current tasks 4.Exit");
            ViewTasks viewTasks = Manage.findViewType(checkType());
            if (viewTasks == null) continue;
            if(viewTasks==ViewTasks.EXIT)break;
            switch (viewTasks) {
                case CATEGORY_TASKS:
                    System.out.print("WORK(1),PERSONAL(2),LEARNING(3),SHOPPING(4),OTHER(5) \nEnter category type (integer only) : ");
                    Category category = Manage.findCategoryType(checkType());
                    displayTasks(Manage.viewTasksByCategory(tasks, category));
                    break;
                case PRIORITY_TASKS:
                    displayTasks(Manage.viewTaskOnPriority(tasks));
                    break;
                case CURRENT_TASKS:
                    displayTasks(Manage.viewCurrentTasks(tasks));
                    break;
            }
        }
    }
    static boolean displayTasks(List<Task> tasks){
        if(tasks.isEmpty()) {
            System.out.println("There are no tasks here...");
            return false;
        }
        String format = ("| %6s | %8s | %20s | %8s | %10s | %6s | %10s | %10s | %7s | %45s |%n");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format(format, "TaskId", "Title", "Description", "Priority", "Category", "status", "Start Date", "End Date", "Period", "Collaborators");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Task task : tasks) {
            List<Integer> userId = new ArrayList<>();
            for (Collaborator c : task.getCollaborator()) userId.add(c.getUserId());
            List<String> userName = Manage.getUserNamesByUserId(userId);
            String status ;
            if(task.getIsCompleted()==1) status = "completed";
            else status = "-";
            System.out.format(format, task.getTaskId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getCategory(), status, task.getStartDate(), task.getEndDate(), task.getPeriod(), userName);
        }System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        return true;
    }
    static boolean displayTaskLists(List<TaskList> taskLists){
        if(taskLists.isEmpty()) {
            System.out.println("There are no taskLists here...");
            return false;
        }
        String format = ("| %15s | %15s | %70s | %51s|%n");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format(format, "TaskList Id", "UserName ", "Task Id", "Collaborators ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (TaskList taskList : taskLists) {
            List<Integer> userId = new ArrayList<>();
            for (Collaborator c : taskList.getCollaborator()) userId.add(c.getUserId());
            List<String> userName = Manage.getUserNamesByUserId(userId);
            System.out.format(format, taskList.getTaskListId(), Manage.getUserNameByUserId(taskList.getUserId()), taskList.getTasks(), userName);
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        return true;
    }
    static void shareTaskList(String userEmail,int taskListId){
        while (true) {
            try {
                System.out.println("1.Add collaborator 2.Remove collaborator 3.Exit");
                Share share = Manage.findSelectType(checkType());
                if (share == null) continue;
                if (share == Share.EXIT) break;
                System.out.print("Enter email id of collaborator for the taskList : ");
                String collabEmail = checkBlank();
                Utility.checkSameAccountCollaboration(userEmail, collabEmail);
                User collabUser = Manage.getUserByEmail(collabEmail);
                Utility.validEmailConstraint(collabUser);
                Collaborator collaborator = Manage.getCollaboratorForTaskList(taskListId, collabUser.getUserId());
                switch (share) {
                    case ADD_COLLABORATOR:
                        Utility.isCollaboratorNotNull(collaborator);
                        String canEdit;
                        do {
                            System.out.print("Can the user edit  ('yes' or 'no') : ");
                            canEdit = checkBlank();
                        } while (!canEdit.equals("yes") && !canEdit.equals("no"));
                        int access;
                        if (canEdit.equals("yes")) access = 1;
                        else access = 0;
                        if (Manage.shareTaskList(taskListId, collabUser.getUserId(), access))
                            System.out.println("Collaborator " + collabUser.getUserName() + " added to the taskList");
                        else System.out.println("Error in adding collaborator to the taskList");
                        break;
                    case REMOVE_COLLABORATOR:
                        Utility.isCollaboratorNull(collaborator);
                        if (Manage.removeShareTaskList(taskListId, collabUser.getUserId()))
                            System.out.println("collaborator " + collabUser.getUserName() + " removed successfully from the taskList");
                        else System.out.println("An error occurred while removing collaborator to the taskList");
                        break;
                    }
            } catch(InvalidDataException | ExistCollaborationException | SameAccountException e){
                System.out.println(e.getMessage());
            }
        }
    }
    static void shareTask(String userEmail,int taskId){
        while (true) {
            try {
                System.out.println("1.Add collaborator 2.Remove collaborator 3.Exit");
                Share share = Manage.findSelectType(checkType());
                if (share == null) continue;
                if (share == Share.EXIT) break;
                System.out.print("Enter email id of collaborator for the task : ");
                String collabEmail = checkBlank();
                Utility.checkSameAccountCollaboration(userEmail, collabEmail);
                User collabUser = Manage.getUserByEmail(collabEmail);
                Utility.validEmailConstraint(collabUser);
                Collaborator collaborator = Manage.getCollaboratorForTask(taskId, collabUser.getUserId());
                switch (share) {
                    case ADD_COLLABORATOR:
                        Utility.isCollaboratorNotNull(collaborator);
                        String canEdit;
                        do {
                            System.out.print("Can the user edit  ('yes' or 'no') : ");
                            canEdit = checkBlank();
                        } while (!canEdit.equals("yes") && !canEdit.equals("no"));
                        int access;
                        if (canEdit.equals("yes")) access = 1;
                        else access = 0;
                        if (Manage.shareTask(taskId, collabUser, access))
                            System.out.println("Collaborator " + collabUser.getUserName() + " added to the task");
                        else System.out.println("Error in adding collaborator to the task");
                        break;
                    case REMOVE_COLLABORATOR:
                        Utility.isCollaboratorNull(collaborator);
                        if (Manage.removeShareTask(taskId, collabUser.getUserId()))
                            System.out.println("collaborator " + collabUser.getUserName() + " removed successfully");
                        else System.out.println("An error occurred while removing collaborator to the task");
                        break;
                }
            } catch(InvalidDataException | ExistCollaborationException | SameAccountException e){
                System.out.println(e.getMessage());
            }
        }
    }
    static int checkType(){
        int num = 0;
        boolean flag = true;
        while (flag) {
            String str =sc.nextLine();
            try {
                num = Integer.parseInt(str);
                flag = false;
            } catch (NumberFormatException e) {
                System.out.print("Enter an integer : ");
            }
        }
        return num;
    }
    static String checkBlank(){
        String name;
        do {
            name =sc.nextLine();
        } while (name.isEmpty() || name.trim().isEmpty());
        return name;
    }
}

