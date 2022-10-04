
package fit3077.lab02team14.assignment2.model.actor;
import com.fasterxml.jackson.databind.ObjectMapper;
import fit3077.lab02team14.assignment2.constants.UserRole;

import javax.annotation.Generated;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Generated("net.hexar.json2pojo")
public class UserList {
    private User[] userList = {};

    public UserList(HttpResponse<String> response) {
        try{
            userList = new ObjectMapper().readValue(response.body(), User[].class);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public UserList(){}

    public UserList(User[] userList){
        this.userList = userList;
    }

    public User[] getUserList() {
        return userList;
    }

    public User findUserByUsername(String targetUsername){

        for (int i = 0; i < userList.length; i++){
            User user = userList[i];
            String username = user.getUserName();
            if (username.equals(targetUsername)){
                return user;
            }
        }
        return null;
    }

    public User[] findUsersbyRoles(UserRole userRole){
        ArrayList <User> filteredUsers = new ArrayList<>();

        for (int i = 0; i < userList.length; i++){
            User user = userList[i];
            if (userRole.equals(UserRole.HEALTHCAREWORKER)){
                if (user.getIsHealthcareWorker()){
                    filteredUsers.add(user);
                }
            }
            else if (userRole.equals(UserRole.CUSTOMER)){
                if (user.getIsCustomer()){
                    filteredUsers.add(user);
                }
            }
            else if (userRole.equals(UserRole.RECEPTIONIST)){
                if (user.getIsReceptionist()){
                    filteredUsers.add(user);
                }
            }
        }

        return filteredUsers.toArray(new User[0]);
    }

    public User[] findUsersbyTestingSite(String testingSiteId){
        ArrayList <User> filteredUsers = new ArrayList<>();

        for (int i = 0; i < userList.length; i++){
            User user = userList[i];
            if (user.getAdditionalInfo().getTestingSite().getId().equals(testingSiteId)){
                filteredUsers.add(user);
            }
        }

        return filteredUsers.toArray(new User[0]);
    }
}
