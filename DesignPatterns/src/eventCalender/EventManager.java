package eventCalender;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class EventManager { 
    DataBase db = DataBase.getInstance();
    UserManager userManager = new UserManager();
    TeamManager tM = new TeamManager();
    
    public boolean createEvent(String eventId, String eventDate, String eventStartTime, String eventEndTime, int minimumrequired, List<Team> teamsE, List<User> usersE) throws Exception {
        //are all users available? DONE
        //Does required teams have minimumrequired members free in the slot?
        //success
        boolean usersAvailable = areUsersAvailable(usersE,eventDate,eventStartTime,eventEndTime);
        boolean teamsAvailable = areTeamsAvailable(teamsE,eventDate,eventStartTime,eventEndTime,minimumrequired);
        
        try {
            if(usersAvailable && teamsAvailable) {
                
                Event E = new Event(eventId, eventDate, eventStartTime, eventEndTime, minimumrequired,teamsE,usersE);
                List<String> eventUsers = new ArrayList<>();
                
                for(User u : usersE) {
                    eventUsers.add(u.getUserId());
                }
                
                //select minimumrequired members from each team
                for(Team t : teamsE) {
                    int selected = 0;
                    List<User> availableMembers = tM.getAvailableMembersList(t, eventDate, eventStartTime, eventEndTime);
                    for(int i = 0;i<availableMembers.size() && selected<minimumrequired;i++) {
                        selected++;
                        User u = availableMembers.get(i);
                        System.out.println("User "+ u.getUserId()+" Selected from Team" + t.getTeamId());
                        eventUsers.add(u.getUserId());
                        if(!db.getUserEventMap().containsKey(u.getUserId())) {
                            List<Event> userevents = new ArrayList<>();
                            userevents.add(E);
                            db.getUserEventMap().put(u.getUserId(), userevents);
                        }
                        else {
                            db.getUserEventMap().get(u.getUserId()).add(E);
                        }
                        //added each event against user in userEventMap
                    }
                }
                //add event to eventList Map against the date
                if(db.getEventList().containsKey(eventDate)) {
                    db.getEventList().get(eventDate).add(E);
                }
                else {
                    List<Event> eventsondate= new ArrayList<>();
                    eventsondate.add(E);
                    db.getEventList().put(eventDate, eventsondate);
                }
                System.out.print("Users in event "+ eventId+ " : ");
                for(String s : eventUsers) {
                    System.out.print(s + " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean areTeamsAvailable(List<Team> teamsE, String eventDate, String eventStartTime, String eventEndTime, int minimumrequired) throws Exception {
        boolean flag = true;
        
        for(Team t : teamsE) {
            if(tM.getAvailableMembersList(t, eventDate, eventStartTime, eventEndTime).size() >= minimumrequired) {
                continue;
            }
            else {
                flag = false;
                System.out.println("Team "+t.getTeamId()+" is not available");
            }
        }
        if(flag == false) {
            throw new Exception("Teams not available for the event");
        }
        return flag;
    }
    public boolean areUsersAvailable(List<User> usersE,String eventDate, String eventStartTime, String eventEndTime) throws Exception {
        boolean flag = true;
        //iseveryUserAvailable()
        for(User u : usersE) {
            if(userManager.isAvailable(u.getUserId(), eventDate, eventStartTime, eventEndTime)) {
                System.out.println("User "+ u.getUserId() +" is available");
                continue;
            }
            else {
                flag = false;
                System.out.println("User " + u.getUserId() + " is Not available in this slot");
                //("User " + u.getUserId() + " is Not available in this slot") 
            }
        }
        
        if(!flag) {
            throw new Exception("Some users are not available in specified slot");
        }
        return flag;
    }
}