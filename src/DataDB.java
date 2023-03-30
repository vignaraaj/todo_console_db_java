import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class DataDB implements Data {
    private static Data data = DataDB.getInstance();
    private static final String url = "jdbc:sqlite:/Users/vigna-pt6743/Documents/databases/todo.db";
    private static final Connection conn;
    static {
        try {
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection established...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private DataDB() {
    }
    public static Data getInstance() {
        if (data == null) data = new DataDB();
        return data;
    }
    public boolean addUser(User user) {
        try {
            conn.setAutoCommit(false);
            int userId = user.getUserId();
            String name = user.getUserName();
            String email = user.getEmail();
            String password = user.getPassword();
            PreparedStatement preparedStatement1 = conn.prepareStatement("insert into user values(?,?,?)");
            PreparedStatement preparedStatement2 = conn.prepareStatement("insert into userCredentials values(?,?,?)");
            preparedStatement1.setInt(1, userId);
            preparedStatement1.setString(2, name);
            preparedStatement1.setString(3, email);
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
            preparedStatement2.setInt(1,userId);
            preparedStatement2.setString(2, email);
            preparedStatement2.setString(3, password);
            preparedStatement2.executeUpdate();
            preparedStatement2.close();
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean deleteUser(User user) {
        try {
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("delete from user where id=?");
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            PreparedStatement preparedStatement1 = conn.prepareStatement("delete from userCredentials where userId=?");
            preparedStatement1.setInt(1, user.getUserId());
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        }finally{
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<User> getUserCredentials() {
        try {
            List<User> users = new ArrayList<>();
            String selectSQL = "select * from userCredentials";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String email = rs.getString("email");
                String password = rs.getString("password");
                users.add(new User(userId,email, password));
            }
            return users;
        } catch (Exception e) {
            return null;
        }
    }
    public List<User> getUsers() {
        try {
            List<User> users = new ArrayList<>();
            String selectSQL = "select * from user";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("name");
                int userId = rs.getInt("id");
                users.add(new User(email,name,userId));
            }
            return users;
        } catch (Exception e) {
            return null;
        }
    }
    public User getUserByEmail(String email) {
        try {
            String selectSQL = "select * from user where email=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            String mail = rs.getString("email");
            String name = rs.getString("name");
            int userId = rs.getInt("id");
            preparedStatement.close();
            return new User( mail,name,userId);
        } catch (Exception e) {
            return null;
        }
    }
    public String getPassword(int userId){
        try {
            String selectSQL = "select * from userCredentials where userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            ResultSet rs= preparedStatement.executeQuery();
            String password = rs.getString("password");
            preparedStatement.close();
            return password;
        } catch (Exception e) {
            return null;
        }
    }
    public String  getUserName(int userId) {
        try {
            String selectSQL = "select * from user where id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            ResultSet rs= preparedStatement.executeQuery();
            String name = rs.getString("name");
            preparedStatement.close();
            return name;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean addTaskList(TaskList taskList) {
        try {
            String insertSQL = "insert into taskList values(?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setInt(1, taskList.getTaskListId());
            preparedStatement.setInt(2, taskList.getUserId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteTaskListByTaskListId(int taskListId) {
        try {
            String deleteSQL = " delete from taskList where taskListId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, taskListId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public List<TaskList> getTaskListsByUserId(int userId) {
        try {
            List<TaskList> taskLists = new ArrayList<>();
            String selectSQL = "select * from taskList where userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int taskListId = rs.getInt("taskListId");
                List<Task> tasks = getTasksByTaskListId(taskListId);
                List<Collaborator> collaborators = new ArrayList<>();
                String selectSQL1 = "select * from sharedTaskList where taskListId=?";
                PreparedStatement preparedStatement1 = conn.prepareStatement(selectSQL1);
                preparedStatement1.setInt(1, taskListId);
                ResultSet rs1 = preparedStatement1.executeQuery();
                while (rs1.next()) {
                    int userId1 = rs1.getInt("userId");
                    int canEdit=rs1.getInt("canEdit");
                    collaborators.add(new Collaborator(taskListId,userId1,canEdit));
                }
                preparedStatement1.close();
                taskLists.add(new TaskList(userId, taskListId, tasks,collaborators));
            }
            preparedStatement.close();
            return taskLists;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean deleteTaskListsByUserId(int userId) {
        try {
            String deleteSQL = " delete from taskList where userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public TaskList getTaskListByTaskListId(int taskListId) {
        try {
            String selectSQL = "select * from taskList where taskListId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, taskListId);
            ResultSet rs = preparedStatement.executeQuery();
            int userId = rs.getInt("userId");
            List<Task> tasks = getTasksByTaskListId(taskListId);
            List<Collaborator> collaborators = new ArrayList<>();
            String selectSQL1 = "select * from sharedTaskList where taskListId=?";
            PreparedStatement preparedStatement1 = conn.prepareStatement(selectSQL1);
            preparedStatement1.setInt(1, taskListId);
            ResultSet rs1 = preparedStatement1.executeQuery();
            while (rs1.next()) {
                int userId1 = rs1.getInt("userId");
                int canEdit = rs1.getInt("canEdit");
                collaborators.add(new Collaborator(taskListId,userId1,canEdit));
            }
            preparedStatement1.close();
            preparedStatement.close();
            return new TaskList(userId, taskListId, tasks,collaborators);
        } catch (Exception e) {
            return null;
        }
    }
    public boolean addTask(Task task) {
        try {
            String insertSQl1 = "insert into task values(?,?,?,?,?,?,?,?,?,?)";
            int taskId = task.getTaskId();
            int taskListId = task.getTaskListId();
            String title = task.getTitle();
            String description = task.getDescription();
            LocalDate startDate = task.getStartDate();
            LocalDate endDate = task.getEndDate();
            int priority = task.getPriority();
            String category = String.valueOf(task.getCategory());
            String period = String.valueOf(task.getPeriod());
            int isCompleted = task.getIsCompleted();
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQl1);
            preparedStatement.setInt(1, taskId);
            preparedStatement.setInt(2, taskListId);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, String.valueOf(startDate));
            preparedStatement.setString(6, String.valueOf(endDate));
            preparedStatement.setInt(7, priority);
            preparedStatement.setString(8, category);
            preparedStatement.setString(9, period);
            preparedStatement.setInt(10, isCompleted);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteTaskByTaskId(int taskId) {
        try {
            String deleteSQl = "delete from task where taskId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQl);
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean updateTask(int taskId, String title, String description, int priority, Category category,LocalDate startDate ,LocalDate endDate,int isCompleted) {
        try {
            String updateSQL = "update task set title=?,description=?,priority=?,category=?,startDate=?,endDate=?,isCompleted=? where taskId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateSQL);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, priority);
            preparedStatement.setString(4,String.valueOf(category));
            preparedStatement.setString(5,String.valueOf(startDate));
            preparedStatement.setString(6,String.valueOf(endDate));
            preparedStatement.setInt(7, isCompleted);
            preparedStatement.setInt(8, taskId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Task getTaskByTaskId(int taskId) {
        try {
            String selectSQL = "select * from task where taskId=? ";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, taskId);
            ResultSet rs = preparedStatement.executeQuery();
            int taskListId = rs.getInt("taskListId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            LocalDate startDate = LocalDate.parse(rs.getString("startDate"));
            LocalDate endDate = LocalDate.parse(rs.getString("endDate"));
            int priority = rs.getInt("priority");
            Category category = Category.valueOf(rs.getString("category"));
            RecurringTasks period = RecurringTasks.valueOf(rs.getString("period"));
            List<Collaborator> sharedUsers = new ArrayList<>();
            PreparedStatement preparedStatement1 = conn.prepareStatement("select * from sharedTask where taskId=?");
            preparedStatement1.setInt(1, taskId);
            ResultSet rs1 = preparedStatement1.executeQuery();
            while (rs1.next()) {
                int userId = rs1.getInt("userId");
                int canEdit = rs1.getInt("canEdit");
                sharedUsers.add(new Collaborator(taskId,userId,canEdit));
            }
            preparedStatement1.close();
            int isCompleted = rs.getInt("isCompleted");
            preparedStatement.close();
            return new Task(taskId, taskListId, title, description, startDate, endDate, priority, category, period, sharedUsers, isCompleted);
        } catch (Exception e) {
            return null;
        }
    }
    public List<Task> getTasksByTaskListId(int taskListId) {
        try {
            List<Task> tasks = new ArrayList<>();
            String selectSQL = "select * from task where taskListId =?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, taskListId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("taskId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                LocalDate startDate = LocalDate.parse(rs.getString("startDate"));
                LocalDate endDate = LocalDate.parse(rs.getString("endDate"));
                int priority = rs.getInt("priority");
                Category category = Category.valueOf(rs.getString("category"));
                RecurringTasks period = RecurringTasks.valueOf(rs.getString("period"));
                int isCompleted = rs.getInt("isCompleted");
                List<Collaborator> sharedUsers = new ArrayList<>();
                PreparedStatement preparedStatement1 = conn.prepareStatement("select * from sharedTask where taskId=?");
                preparedStatement1.setInt(1, taskId);
                ResultSet rs1 = preparedStatement1.executeQuery();
                while (rs1.next()) {
                    int userId = rs1.getInt("userId");
                    int canEdit = rs1.getInt("canEdit");
                    sharedUsers.add(new Collaborator(taskId,userId,canEdit));
                }
                preparedStatement1.close();
                tasks.add(new Task(taskId, taskListId, title, description, startDate, endDate, priority, category, period, sharedUsers, isCompleted));
            }
            preparedStatement.close();
            return tasks;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean deleteTasksByTaskListId(int taskListId) {
        try {
            String deleteSQl = "delete from task where taskListId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQl);
            preparedStatement.setInt(1, taskListId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean addSharedTask(int taskId, int userId, int canEdit) {
        try {
            String insertSQL = "insert into sharedTask values(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setInt(1, taskId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, canEdit);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteSharedTask(int taskId, int userId) {
        try {
            String deleteSQL = " delete from sharedTask where taskId=? and userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, taskId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteSharedTasksByUserId(int userId) {
        try {
            String deleteSQL = " delete from sharedTask where  userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteSharedTasksByTaskId(int taskId) {
        try {
            String deleteSQL = " delete from sharedTask where taskId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, taskId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public List<Integer> getSharedTaskIdsByUserId(int userId) {
        try {
            List<Integer> sharedTaskIds = new ArrayList<>();
            String selectSQL = "select * from sharedTask where userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("taskId");
                sharedTaskIds.add(taskId);
            }
            preparedStatement.close();
            return sharedTaskIds;
        } catch (Exception e) {
            return null;
        }
    }
    public Collaborator getCollaboratorForTask(int taskId,int userId) {
        try {
            String selectSQL = "select * from sharedTask where userId=? and taskId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2,taskId);
            ResultSet rs = preparedStatement.executeQuery();
            int canEdit = rs.getInt("canEdit");
            preparedStatement.close();
            return new Collaborator(taskId,userId,canEdit);
        } catch (Exception e) {
            return null;
        }
    }
    public boolean deleteSharedTaskListByTaskListId(int taskListId) {
        try {
            String deleteSQL = "delete from sharedTaskList where taskListId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, taskListId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteSharedTaskList(int taskListId, int userId) {
        try {
            String deleteSQL = "delete from sharedTaskList where taskListId=? and userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, taskListId);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean addSharedTaskList(int taskListId, int userId, int canEdit) {
        try {
            String insertSQL = "insert into sharedTaskList values(?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            preparedStatement.setInt(1, taskListId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, canEdit);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public List<Integer> getSharedTaskListIdsByUserId(int userId) {
        try {
            List<Integer> sharedTaskList = new ArrayList<>();
            String selectSQL = "select * from sharedTaskList where userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int taskListId = rs.getInt("taskListId");
                sharedTaskList.add(taskListId);
            }
            preparedStatement.close();
            return sharedTaskList;
        } catch (Exception e) {
            return null;
        }
    }
    public boolean deleteSharedTaskListsByUserId(int userId) {
        try {
            String deleteSQL = "delete from sharedTaskList where userId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Collaborator getCollaboratorForTaskList(int taskListId,int userId) {
        try {
            String selectSQL = "select * from sharedTaskList where userId=? and taskListId=?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectSQL);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2,taskListId);
            ResultSet rs = preparedStatement.executeQuery();
            int canEdit = rs.getInt("canEdit");
            preparedStatement.close();
            return new Collaborator(taskListId,userId,canEdit);
        } catch (Exception e) {
            return null;
        }
    }
}
