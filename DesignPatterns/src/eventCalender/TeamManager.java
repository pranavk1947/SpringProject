package eventCalender;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class TeamManager {
    DataBase db = DataBase.getInstance(); 
    //private HashMap<String, List<User>> teamMemberMap = new HashMap<>();
    public Team createTeam(String TeamId) {
        return new Team(TeamId);
    }
    
    public void addUserToTeam(User user, Team T) throws Exception {
        HashMap<String,List<User>> teamMemberMap = db.getTeamMemberMap();
        
        if(user.getisPartOfTeam() == false) {
            T.getMembers().add(user);
            user.setisPartOfTeam(true);
            teamMemberMap.put(T.getTeamId(), T.getMembers());
        }
        else {
             throw new Exception("User already part of a team");
        }
    }
    
    //getAvailableMemebrs(TeamT1,startTime,endTime,eventDate)
    public int getAvailableMembers(Team T,String eventDate,String startTime,String endTime) {
        int availableMembers = 0;
        UserManager uM = new UserManager();
        for(User u : T.getMembers()) {
            if(uM.isAvailable(u.getUserId(), eventDate, startTime, endTime)) {
                availableMembers++;
            }
        }
        return availableMembers;
    }
    
    //getAvailableMemebrs(TeamT1,startTime,endTime,eventDate)
    public List<User> getAvailableMembersList(Team T,String eventDate,String startTime,String endTime) {
        List<User> availableUsers = new ArrayList<>();
        UserManager uM = new UserManager();
        for(User u : T.getMembers()) {
            if(uM.isAvailable(u.getUserId(), eventDate, startTime, endTime)) {
                availableUsers.add(u);
            }
        }
        //return availableMembers;
        return availableUsers;
    }
    
    
    
}