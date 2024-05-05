package eventCalender;

public class User {

    private String userId;
    private String workingStart;
    private String workingEnd;
    private boolean isPartOfTeam;
    
    public User(String userId, String workingStart, String workingEnd) {
        this.userId = userId;
        this.workingStart = workingStart;
        this.workingEnd = workingEnd;
        this.isPartOfTeam = false;
    }
    
    public boolean isPartOfTeam() {
        return isPartOfTeam;
    }

    public void setPartOfTeam(boolean isPartOfTeam) {
        this.isPartOfTeam = isPartOfTeam;
    }

    public boolean getisPartOfTeam() {
        return isPartOfTeam;
    }
    public void setisPartOfTeam(boolean isPartOfTeam) {
        this.isPartOfTeam = isPartOfTeam;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getWorkingStart() {
        return workingStart;
    }
    public void setWorkingStart(String workingStart) {
        this.workingStart = workingStart;
    }
    public String getWorkingEnd() {
        return workingEnd;
    }
    public void setWorkingEnd(String workingEnd) {
        this.workingEnd = workingEnd;
    }
    @Override
    public String toString() {
        return "User [userId=" + userId + ", workingStart=" + workingStart + ", workingEnd=" + workingEnd
                + ", isPartOfTeam=" + isPartOfTeam + "]";
    }
    
//    //HH:MM 
//    String timeString = "12:30";
//    LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
    
    
    
    
}
