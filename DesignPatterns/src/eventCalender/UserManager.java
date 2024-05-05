package eventCalender;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
public class UserManager {
    DataBase db = DataBase.getInstance(); 
    
    public User createUser(String userId, String workingStart, String workingEnd) throws Exception {
        User u = null;
        HashMap<String, User> userList = db.getUserList();
        boolean duplicacy = db.getUserList().containsKey(userId);
        if (!duplicacy) {
            u = new User(userId, workingStart, workingEnd);
            userList.put(userId, u);
        } else {
            throw new Exception("Please use a different UserId");
        }
        return u;
    }
    public boolean isAvailable(String userId, String eventDate, String startTime, String endTime) {
        // Implementation to check user availability for the given time slot
        HashMap<String, List<Event>> userEventMap = db.getUserEventMap();
        HashMap<String, User> userList = db.getUserList();
        User u = userList.get(userId);
        boolean free = false;
        
        if(!db.getUserEventMap().containsKey(u.getUserId())) {
            return true;
        }
        
        for(Event e : userEventMap.get(userId)) {
            if(e.getEventDate().equals(eventDate)) {
                LocalTime eStart = LocalTime.parse(e.getEventStartTime());
                LocalTime eEnd = LocalTime.parse(e.getEvenetEndTime());
                
                LocalTime slotStartTime= LocalTime.parse(startTime);
                LocalTime slotEndTime= LocalTime.parse(endTime);
                 
                int v1 = slotStartTime.compareTo(eEnd);
                int v2 = eStart.compareTo(slotEndTime);
                
                if(v1 >=0 ||v2 >=0) {
                    continue;
                }
                else {
                    free = false;
                    break;
                }
            }
        }
        
        return free && !isOutofWorkingHours(u,startTime,endTime);
    }
    public boolean isOutofWorkingHours(User member, String eventStartTime, String evenetEndTime) {
        LocalTime userStartTime = LocalTime.parse(member.getWorkingStart());
        LocalTime userEndTime = LocalTime.parse(member.getWorkingEnd());    
        
        LocalTime eventStart = LocalTime.parse(eventStartTime);
        LocalTime eventEnd = LocalTime.parse(evenetEndTime);
        
        int v1 = eventStart.compareTo(userStartTime);
        int v2 = userEndTime.compareTo(eventEnd);
        
        if(v1 >=0 && v2 >=0) {
            return false;
        }
        else {
            return true;
        }
    }
    
    // Method to add an entry to the userEventMap
    public void addUserEvent(User user, List<Event> events) {
        
        //userEventMap.put(user, events);
    }
}