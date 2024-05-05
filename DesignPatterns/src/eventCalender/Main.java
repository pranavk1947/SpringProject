package eventCalender;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        UserManager managerUsers = new UserManager();
        TeamManager manageTeams = new TeamManager();
        EventManager managerEvents = new EventManager();
        
        try {
        	//User G = new User();
            User A = managerUsers.createUser("A","09:00","17:00");
            User B = managerUsers.createUser("B","10:00","17:00");
            User C = managerUsers.createUser("C","09:30","18:00");
            User D = managerUsers.createUser("D","11:00","17:00");
            User E = managerUsers.createUser("E","10:30","18:30");
            User F = managerUsers.createUser("F","09:30","18:00");
            
            
            System.out.println(A.toString());
            System.out.println(B.toString());
            System.out.println(C.toString());
            System.out.println(D.toString());
            System.out.println(E.toString());
            System.out.println(F.toString());
            
            Team T1;
            Team T2;
            
            T1 = manageTeams.createTeam("T1");
            T2 = manageTeams.createTeam("T2");
//            
            manageTeams.addUserToTeam(A, T1);
            manageTeams.addUserToTeam(B, T1);
            manageTeams.addUserToTeam(C, T2);
            manageTeams.addUserToTeam(D, T2);
            
            System.out.println(T1.toString());
            System.out.println(T2.toString());
            
            List<Team> teamsE1 = new ArrayList<>();
            List<User> usersE1 = new ArrayList<>();
//            
            teamsE1.add(T1);
            teamsE1.add(T2);
//            
            usersE1.add(E);
            
            managerEvents.createEvent("E1", "22-04-2024", "12:00", "14:00", 2, teamsE1, usersE1);
            
            
            System.out.println("");
            System.out.println("||");
            
            List<Team> teamsE2 = new ArrayList<>();
            List<User> usersE2 = new ArrayList<>();
//            
            teamsE2.add(T1);
            teamsE2.add(T2);
//            
            usersE2.add(F);
//            
            managerEvents.createEvent("E2", "22-04-2024", "12:00", "14:00", 1, teamsE2, usersE2);
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        
        
        
        
        //createEvent(E1,st,et,List<User> participants)
    }
}