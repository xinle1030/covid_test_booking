# FIT3077 Assignment 3 - TestVidy

## Assignment 2 Code
refer to the assignment2 folder in the git repo

## System Demo A2
https://drive.google.com/file/d/1_nfQ81SqUWovyJId4_1BsEoRKlZnWRHy/view?usp=sharing

## Assignment 3 Code
refer to the assignment3 folder in the git repo

## System Demo A3
https://drive.google.com/file/d/1p3LNnZvbgy3yoGnr7zaDXdD1VtimsIRl/view?usp=sharing

## Workflow Assumptions
1. Every user accessing the system is assumed to be a registered customer and are able to book for a covid test in the application so there is no sign up feature
2. Every user is meant to be a customer, so all of them could place a booking for on site or home testing as long as he or she is a registered user on the system. 
3. Administerer is not assigned to an on site booking when the user makes a booking. The administerer will only be assigned to the on site test booking when an interview has been conducted by the administerer to the patient.
4. Administerer is assigned to the home booking when the user makes a booking.
5. RAT tests provide on-site or home supervised testing while PCR tests only provide on-site tests.

### New Assumptions
6. Editing, Reverting, Cancelling, Deleting and Processing (Interview) a booking is only applied to bookings made for On-Site Testing.
7. Latest User Privileges for OnSite Booking

    ###### a. Resident         - Create, View, Edit, Revert, Cancel Booking [ONLY FOR THE BOOKINGS THAT THEY MADE]
    ###### b. Receptionist     - Create, View (via admin panel), Delete, Edit, Revert, Cancel Booking [FOR ANY BOOKING]
    ###### c. HealthcareWorker - Create, Edit, Revert, Cancel Booking [ONLY FOR THE BOOKINGS THAT THEY MADE]; View, Process (Interview) Booking [FOR ANY BOOKING]
8. Latest User Privileges for Home Booking

    ###### a. Resident         - Create, View Booking [ONLY FOR THE BOOKINGS THAT THEY MADE]
    ###### b. Receptionist     - Create, View Booking; Update whether a user has picked up RAT test kit for home testing if the user indicates that he will pick up test kit at the testing site
    ###### c. HealthcareWorker - Create, View Booking
9. Notification system is only applied to changes made to bookings for On-Site Testing and only the receptionist at the relevant testing site will receive.
10. View the latest notifications in the Notifications Page whereby the latest one will be shown at the top


## User Guidelines
1. *** Important *** Please create a java class file - "APIKey.java" and put under "./assignment3/src/main/java/fit3077/lab02team14/assignment2/constants/", then create a class level variable in the class by placing this line "public static final String MYAPIKEY = "<Your_API_Key>";" into the file. Replace <Your_API_Key> with your actual API key.
2. To check which localhost does this application run on, please refer to "./assignment3/src/main/resources/application.properties" to check for the line server.port=8080. If it is 8080, means that after running the application, you shall open the browser and type "localhost:8080" into the address bar for testing out the application.
3. The root folder of the application should always be the "/assignment3" folder to allow the saving of QR Code to work as intended.
4. The directory which the QR Code generated from booking a home testing is always saved to "assignment3/src/main/resources/static/image/qrCodes/". 
5. QR code will be generated every time when the user views a home testing as the qrCode is retrieved from backend database.
6. The uplaod directory for a Receptionist / Healthcare administerer to search for a home testing should always be in "assignment3/src/main/resources/static/image/qrCodes/", which is the same as the saved directory.
7. If user wishes to test for invalid qRCode, makes sure that the image is uploaded from the directory "assignment3/src/main/resources/static/image/qrCodes/".
8. This application's default server port is set to 8080. To change it, go to the application.properties file in our resources file and change the server port to your desired port.
9. Please do not refresh the webpage after login during the testing process as page refresh might wipe out the data stored temporarily in the http session. 
10. Revert button will only be displayed to the user if edit is made to the booking.
11. Multithreaded programming is not implemented in the assignment - Therefore, in order to test out the notification system that all the receptionists in the same testing site could receive the same notification.

    ###### a. Please make some changes to the booking [Create, View, Delete, Edit, Revert, Cancel, Process (Interview)] as a receptionistA.1 working in testingSiteA.
    ###### b. Then check under the notification page to check if you receive the notification.
    ###### c. Then, logout and login as another receptionistA.2 who is also working in testingSite A, repeat step b.
    ###### d. If the changes made to the booking (Edit / Revert) involve 2 testing sites, all the receptionists in the relevant testing site will receive the notification. Repeat step a to c as a receptionistA working in testingSiteA and receptionistB for verification working in testingSiteB.


## Domain Classes Descriptions

| Packages          | Class                          | Responsibilities 
| --------------    | -----------                    | ----------- 
| constants         | APIKey                         | To provide the API Key used to authorize access from the API endpoint |
|                   | <<\enum>> BookingAction        | To provide options for actions that can be made to a booking |
|                   | <<\enum>> BookingStatus        | To provide insights of the status of if the Booking is still valid |              
|                   | <<\enum>> CovidTestMode        | To provide options for testing mode such as on-site or home supervised testing |
|                   | <<\enum>> CovidTestType        | To provide options for type of covid test such as PCR or RAT test |
|                   | <<\enum>> DropdownAns          | To provide options for Covid Test Suggestions|    
|                   | <<\enum>> FacilityType         | To classify the types of Facility in the system  |               
|                   | <<\enum>> IsTestKitPickedUp    | To provide insights of the status of if RAT test kit is picked up by a patient registered for home testing |
|                   | <<\enum>> TestingSiteStatus    | To be able to identify if a Testing Site is still available  | 
|                   | <<\enum>> UserRole             | To classify the roles of a user into a Customer, a Patient, a Receptionist and/or a Healthcare worker |
| controllers       | BookingController              | To contain the business logic, handling the interactions between Booking-related Model and the View |
|                   | CovidTestController            | To contain the business logic, handling the interactions between CovidTest-related Model and the View |
|                   | LoginController                | To contain the business logic for login |    
|                   | LogoutController               | To contain the business logic for logout |    
|                   | PagesController                | To contain the business logic, handling the general display and flow of the application main pages |    
|                   | TestingSiteController          | To contain the business logic, handling the interactions between TestingSite-related Model and the View |         
| model.actor       | <<\interface>> User            | To encapsulate all the common attributes of all the users at different levels in the system |                 
|                   | UserList                       | To collect all the Users created in the process |                  
|                   | UserAdditionalInfo             | To contain the additional information of the User in the system |                  
|                   | UserDeserializer               | To convert a Json Object containing the User information to one of the actor classes |                  
|                   | Customer                       | To encapsulate the privileges for Customer for making the booking for the CovidTest |                  
|                   | HealthcareWorker               | To encapsulate the privileges for HealthcareWorker such as searching for a patient’s booking |
|                   | Patient                        | To encapsulate the privileges for Patient such as taking the registered CovidTest |                  
|                   | Receptionist                   | To encapsulate the privileges for Receptionist such as updating the picked up status of RAT test kit | 
| model.booking     | Booking                        | To encapsulate the basic information of a booking details |
|                   | BookingAdditionalInfo          | To encapsulate the additional information of the booking |
|                   | BookingAlgoFactory             | To create the correct strategy for On Site or Home Testing Booking |
|                   | BookingClient                  | To update the booking versions by saving or restoring a checkpoint |  
|                   | BookingContext                 | To contain all of the execution methods for the booking strategies |                  
|                   | BookingList                    | To collect all of the bookings made in the process |    
|                   | BookingNotification            | To hold the related data that is updated in real time as well as messages and titles for display in UI |
|                   | BookingNotiObserver            | To subscribe to the BookingSubject to listen to changes and update data accordingly |
|                   | BookingNotiTemplate            | To store the message templates and generating the final messages dynamically in UI |
|                   | <<\Abstract>> BookingObserver  | To encapsulate all common attributes for methods to make an observer for booking |               
|                   | BookingOriginator              | To create a memento of the booking state |
|                   | BookingUpdate                  | To contain all the necessary information to be updated by the observer |
|                   | <<\Interface>> BookingStrategy | To encapsulate all the common attributes for methods related to actions to be done on booking |
|                   | BookingSubject                 | To create a new Booking State and update all the observers on the new addition |
|                   | HomeTestBooking                | To encapsulate all the attributes of Booking for Home Testing |                 
|                   | OnSiteTestBooking              | To encapsulate all the attributes of Booking for On Site Testing |                  
| model.modalInfo   | BookingInfo                    | To update a Booking information or to create a new Booking | 
|                   | BookingState                   | To contain all the necessary information for a Booking version to be reverted |                 
|                   | CovidTestInfo                  | To update a CovidTest information or to create a new CovidTest for a booking |
|                   | CovidTestSuggestion            | To contain the information required to suggest an appropriate CovidTest type for the User |                   
|                   | UserInfo                       | To contain all the log in information of a User |                  
| model.photo       | Photo                          | To contain all the common information of a photo for the test kit result |                  
|                   | PhotoAdditionalInfo            | To contain all the additional information of a photo for the test kit result |                  
| model.test        | CovidTest                      | To encapsulate all the common information for Covid Test in the system |                 
|                   | CovidTestAdditionalInfo        | To encapsulate all the additional information for Covid Test in the system CovidTest | 
|                   | HomeState                      | To set a covid test to Home Testing and update the test kit collection status |
|                   | OnSiteState                    | To set a covid test to On Site Testing and update the administerer |
|                   | TestModeState                  | To encapsulate common methods for updating Covid Test info, aka test mode |                                  
| model.testingsite | TestingSite                    | To encapsulate all the basic information about a testing site |
|                   | TestingSiteAdditionalInfo      | To encapsulate the additional information of a testing site such as the type of the facility |                  
|                   | TestingSiteList                | To aggregate different sites into a list for searching purposes |                  
|                   | TestingSiteAddress             | The basic information for the address of a testing site in the system |                  
|                   | AddressAdditionalInfo          | To contain the additional information for the address of a testing site in the system |                  
| security          | SecurityConfig                 | To disable the default auto-configuration and enable custom security configuration for this project | 
| services          | NetworkHelper                  | To contain all the methods to access and retrieve necessary data from the API endpoint |                  
|                   | QRCode                         | To contain all the methods related to QR Codes such as the generation or reading of QR Codes |
|                   | StringFormatter                | To contain all the methods related to formatting a string |  
|                   | Util                           | To contain all sorts of utility methods |                  

