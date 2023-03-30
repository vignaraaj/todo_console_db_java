enum Category {
    WORK(1),PERSONAL(2),LEARNING(3),SHOPPING(4),OTHER(5);
    final int category;
    Category(int input) {
        this.category = input;
    }
}
enum RecurringTasks {
    DAILY(1),WEEKLY(2),MONTHLY(3);
    final int unit;
    RecurringTasks(int unit){
        this.unit=unit;
    }
}
enum Entry {
    SIGNUP(1),LOGIN(2),DISPLAY_USERS(3),EXIT(4);
    final int choice;
    Entry(int choice){
        this.choice=choice;
    }
}
enum TaskActions {
    DELETE_TASK(1),EDIT_TASK(2),SHARE_TASK(3),EXIT(4);
    final int option;
    TaskActions(int option){
        this.option=option;
    }
}
enum TaskListActions {
    ADD_TASK(1),DELETE_TASK_LIST(2),USE_TASK_LIST(3),VIEW_TASK(4),SHARE_TASK_LIST(5),EXIT(6);
    final int choose;
    TaskListActions(int choose){
        this.choose=choose;
    }
}
enum ViewTasks {
    CATEGORY_TASKS(1),PRIORITY_TASKS(2),CURRENT_TASKS(3),EXIT(4);
    final int view;
    ViewTasks(int view){
        this.view = view;
    }
}
enum Share {
    ADD_COLLABORATOR(1),REMOVE_COLLABORATOR(2),EXIT(3);
    final int select;
    Share(int select){
        this.select=select;
    }
}
enum EditTaskLists {
   EDIT_SHARED_TASK_LIST(1),DELETE_SHARED_TASK_LIST(2),VIEW_TASKS_OF_SHARED_TASK_LIST(3),EXIT(4);
    final int input;
    EditTaskLists(int input) {this.input=input;}
}
enum EditTasks {
    EDIT_SHARED_TASK(1),DELETE_SHARED_TASK(2),EXIT(3);
    final int input;
    EditTasks(int input) {this.input=input;}
}
enum ViewTaskLists{
    ADD_TASK_LIST(1),VIEW_TASK_LISTS(2),VIEW_SHARED_TASK_LISTS(3),VIEW_SHARED_TASKS(4),CLOSE_ACCOUNT(5),EXIT(6);
    final int input;
    ViewTaskLists(int input) {this.input=input;}
}
enum Edit{
    TITLE(1),DESCRIPTION(2),PRIORITY(3),CATEGORY(4),DATE(5),IS_COMPLETED(6),Exit(7);
    final int edit;
    Edit(int edit){
        this.edit = edit;
    }
}
