package eventCalender;
import java.util.ArrayList;
import java.util.List;
public class Team {
	
    private String TeamId;
    private List<User> members;
    
    public Team(String teamId) {
        TeamId = teamId;
        this.members = new ArrayList<>();
    }
    public String getTeamId() {
        return TeamId;
    }
    public void setTeamId(String teamId) {
        TeamId = teamId;
    }
    public List<User> getMembers() {
        return members;
    }
    public void setMembers(List<User> members) {
        this.members = members;
    }
    @Override
    public String toString() {
        return "Team [TeamId=" + TeamId + ", members=" + members + "]";
    }
    
    
}