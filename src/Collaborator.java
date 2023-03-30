public final class Collaborator {
    private final int id;
    private final int userId;
    private final int canEdit;
    public Collaborator(int id, int userId, int canEdit) {
        this.id=id;
        this.userId = userId;
        this.canEdit = canEdit;
    }
    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public int getCanEdit() {
        return canEdit;
    }
}
