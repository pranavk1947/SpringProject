package eventCalender;
import java.util.HashMap;
import java.util.List;
public class DataBase {
	//Singleton
    private static DataBase instance = null;
    private HashMap<String, User> userList = new HashMap<>();
    private HashMap<String, List<Event>> userEventMap = new HashMap<>();
    private HashMap<String, List<User>> teamMemberMap = new HashMap<>();
    private HashMap<String, List<Event>> eventList = new HashMap<>();
    
    private DataBase() {
        // Private constructor to prevent direct instantiation
    }
    
    @Override
    public String toString() {
        return "DataBase [userList=" + userList + ", userEventMap=" + userEventMap + ", teamMemberMap=" + teamMemberMap
                + ", eventList=" + eventList + "]";
    }
    public static DataBase getInstance() {
        if (instance == null) {
            synchronized (DataBase.class) {
                if (instance == null) {
                    instance = new DataBase();
                }
            }
        }
        return instance;
    }
    public HashMap<String, User> getUserList() {
        return userList;
    }
    public void setUserList(HashMap<String, User> userList) {
        this.userList = userList;
    }
    public HashMap<String, List<Event>> getUserEventMap() {
        return userEventMap;
    }
    public void setUserEventMap(HashMap<String, List<Event>> userEventMap) {
        this.userEventMap = userEventMap;
    }
    public HashMap<String, List<Event>> getEventList() {
        return eventList;
    }
    public void setEventList(HashMap<String, List<Event>> eventList) {
        this.eventList = eventList;
    }
    public static void setInstance(DataBase instance) {
        DataBase.instance = instance;
    }
    public HashMap<String, List<User>> getTeamMemberMap() {
        return teamMemberMap;
    }
    public void setTeamMemberMap(HashMap<String, List<User>> teamMemberMap) {
        this.teamMemberMap = teamMemberMap;
    }
    // Getter and setter methods for userList, userEventMap, and eventList
}