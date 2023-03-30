import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
public class Utility {
    private static final String adminId = "vkj";
    private static final String password = "123";
    static void loginAdmin(String admin, String pass)throws InvalidDataException {
        if(!(admin.equals(adminId) && pass.equals(password))) throw new InvalidDataException("Invalid login");
    }
    static void checkEmptyUsers(List<User> details)throws Exception{
        if(details.isEmpty())throw new Exception("There are no users yet...");
    }
    static void checkPassWordConstraint(String originalPassword,String password)throws InvalidDataException{
        if(!originalPassword.equals(password))throw new InvalidDataException("Wrong password");
    }
    static void userExists(User user)throws InvalidDataException{
        if (user != null) throw new InvalidDataException("  this E-mail has an existing account try different mail id");
    }
    static void checkInvalidLoginConstraint(User user)throws AuthenticationException{
        if(user==null) throw new AuthenticationException("Invalid login");
    }
    static void checkPriorityConstraint(int priority)throws InvalidDataException{
        if(priority > 10 || priority <= 0) throw new InvalidDataException("Invalid priority valid from 1 to 10");
    }
    static void checkCategoryConstraint(Category category)throws InvalidDataException{
        if(category == null) throw new InvalidDataException("Invalid input for category press valid only from 1 to 5");
    }
    static LocalDate checkStartDateConstraint(String startDate1)throws DateTimeParseException {
        return LocalDate.parse(startDate1);
    }
    static LocalDate checkEndDateConstraint(LocalDate startDate,String endDate1)throws DateTimeParseException,InvalidDataException{
        LocalDate endDate = LocalDate.parse(endDate1);
        if(startDate.isAfter(endDate)) throw new InvalidDataException("end date cant be before start date");
        return endDate;
    }
    static void checkPeriodConstraint(RecurringTasks period)throws InvalidDataException{
        if(period == null) throw new InvalidDataException("Invalid input for Recurring tasks press valid input");
    }
    static void checkRecurringNumber(int recurringNumber)throws InvalidDataException{
        if(recurringNumber<0 || recurringNumber>50) throw new InvalidDataException("recurring Number should be between 0 and 50 (inclusive)");
    }
    static void checkCompleteStatusConstraint(int isCompleted) throws InvalidDataException{
        if (isCompleted != 1 && isCompleted != 0)
            throw new InvalidDataException("only valid value is 1 or 0");
    }
    static void checkTaskListHasTask(Task task,int taskListId)throws AuthenticationException{
        if (task.getTaskListId() != taskListId) throw new AuthenticationException("the task doesn't belong to this taskList,you are not authenticated...");
    }
    static void checkTaskListBelongsToUser(int userIdInTaskList,int userId)throws AuthenticationException{
        if(userIdInTaskList!=userId) throw new AuthenticationException("You are not authenticated to use this taskList");
    }
    static void checkSameAccountCollaboration(String userEmail,String email)throws SameAccountException{
        if (userEmail.equals(email)) throw new SameAccountException("You cant be collaborator for your own task.");
    }
    static void canEditStatus(Collaborator c) throws AuthenticationException {
        if(c.getCanEdit()==0) throw new AuthenticationException("You can only view  can't edit ");
    }
    static void isCollaboratorNull(Collaborator c) throws InvalidDataException{
        if(c==null) throw new InvalidDataException("invalid collaborator mail id...");
    }
    static void isCollaboratorNotNull(Collaborator c)throws ExistCollaborationException{
        if(c!=null)throw new ExistCollaborationException("The collaboration already exists...");
    }
    static void validTaskIdConstraint(Task task) throws InvalidDataException{
        if (task == null) throw new InvalidDataException("invalid taskId");
    }
    static void validTaskListIdConstraint(TaskList taskList) throws InvalidDataException{
        if (taskList == null) throw new InvalidDataException("taskList doesn't exist");
    }
    static void validEmailConstraint(User user)throws InvalidDataException{
        if (user == null) throw new InvalidDataException("Collaborator doesn't even have an account");
    }
    static void doTaskListOfTaskShared(List<Integer> sharedTaskListId,int taskListId,int collabTaskEdit,int collabTaskListEdit)throws AuthenticationException{
        if(sharedTaskListId.contains(taskListId) && collabTaskListEdit==1 && collabTaskEdit==0)
            throw new AuthenticationException("The taskList of this task is also shared to you by deleting this task you could potentially" +
                    " edit the task for which you didn't have access \n try deleting the shared taskList first and delete this shared task");
    }

}
