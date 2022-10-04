package fit3077.lab02team14.assignment2;

import fit3077.lab02team14.assignment2.model.actor.UserList;
import fit3077.lab02team14.assignment2.services.NetworkHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.net.http.HttpResponse;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Assignment2Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment2Application.class, args);
		runApi();
	}

	public static void runApi() {
		HttpResponse<String> response = NetworkHelper.getData("/user");
//		User user;
//		try {
//			user = new ObjectMapper().readValue(response.body(), User.class);
//			System.out.println(user);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
		
		 UserList userList = new UserList(response);

		 for (int i = 0; i < userList.getUserList().length; i++) {
			 System.out.println(userList.getUserList()[i].toString());
		 	System.out.println(userList.getUserList()[i].getClass().getName());
			 System.out.println();
		 }

		 System.out.println("----\n\n");
	}
}
