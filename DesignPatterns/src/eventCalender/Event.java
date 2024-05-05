package eventCalender;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class Event {
    private String eventId;
    private String eventDate;
    private String eventStartTime;
    private String EvenetEndTime;
    private int minimumrequired;
    private List<Team> requiredTeams;
    private List<User> requiredUsers;
    
    
    public Event(String eventId, String eventDate, String eventStartTime, String evenetEndTime, int minimumrequired) {
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        EvenetEndTime = evenetEndTime;
        this.minimumrequired = minimumrequired;
        this.requiredTeams = new ArrayList<>();
        this.requiredUsers = new ArrayList<>();
    }
    public String getEventId() {
        return eventId;
    }
    public Event(String eventId, String eventDate, String eventStartTime, String evenetEndTime, int minimumrequired,
            List<Team> requiredTeams, List<User> requiredUsers) {
        super();
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        EvenetEndTime = evenetEndTime;
        this.minimumrequired = minimumrequired;
        this.requiredTeams = requiredTeams;
        this.requiredUsers = requiredUsers;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    public String getEventStartTime() {
        return eventStartTime;
    }
    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
    public String getEvenetEndTime() {
        return EvenetEndTime;
    }
    public void setEvenetEndTime(String evenetEndTime) {
        EvenetEndTime = evenetEndTime;
    }
    public int getMinimumrequired() {
        return minimumrequired;
    }
    public void setMinimumrequired(int minimumrequired) {
        this.minimumrequired = minimumrequired;
    }
    
    public List<Team> getRequiredTeams() {
        return requiredTeams;
    }
    public void setRequiredTeams(List<Team> requiredTeams) {
        this.requiredTeams = requiredTeams;
    }
    public List<User> getRequiredUsers() {
        return requiredUsers;
    }
    public void setRequiredUsers(List<User> requiredUsers) {
        this.requiredUsers = requiredUsers;
    }
    
    @Override
    public String toString() {
        return "Event [eventId=" + eventId + ", eventDate=" + eventDate + ", eventStartTime=" + eventStartTime
                + ", EvenetEndTime=" + EvenetEndTime + ", minimumrequired=" + minimumrequired + ", requiredTeams="
                + requiredTeams + ", requiredUsers=" + requiredUsers + "]";
    }
    
    
    
    
    
    
    
}