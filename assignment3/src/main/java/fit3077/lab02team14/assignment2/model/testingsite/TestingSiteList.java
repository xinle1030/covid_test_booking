package fit3077.lab02team14.assignment2.model.testingsite;

import com.fasterxml.jackson.databind.ObjectMapper;

import fit3077.lab02team14.assignment2.constants.FacilityType;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;

public class TestingSiteList {
    private TestingSite[] testingSiteList = {};

    public TestingSiteList(HttpResponse<String> response) {
        try {
            testingSiteList = new ObjectMapper().readValue(response.body(), TestingSite[].class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < testingSiteList.length; i++) {
            testingSiteList[i].getAdditionalInfo().setTestingSiteStatus();
            int noOfBookings =  testingSiteList[i].getBookingsByDate().length;
            testingSiteList[i].getAdditionalInfo().setWaitingTime(noOfBookings);
        }
    }

    public TestingSite[] getTestingSiteList() {
        return testingSiteList;
    }

    public TestingSite[] filter(String suburb, String postcode, String facilityType) {
        ArrayList<TestingSite> filteredSites = new ArrayList<>();

        for (int i = 0; i < testingSiteList.length; i++) {
            TestingSite testingSite = testingSiteList[i];
            TestingSiteAddress address = testingSite.getAddress();
            boolean suburbMatch = address.getSuburb().toLowerCase().contains(suburb.toLowerCase());
            boolean postcodeMatch = address.getPostcode().toLowerCase().contains(postcode.toLowerCase());
            boolean addressMatch = suburbMatch && postcodeMatch;
            boolean isFacilityTypeMatch = testingSite.getAdditionalInfo().getFacilityType().equals(facilityType);
            if (addressMatch
                    && (facilityType.equals(FacilityType.NOPREFERENCE.getValue()) || facilityType.length() == 0)) {
                filteredSites.add(testingSite);
            } else if (addressMatch && isFacilityTypeMatch) {
                filteredSites.add(testingSite);
            } else if (suburbMatch && isFacilityTypeMatch && postcode.length() == 0) {
                filteredSites.add(testingSite);
            } else if (postcodeMatch && isFacilityTypeMatch && suburb.length() == 0) {
                filteredSites.add(testingSite);
            }
        }

        return filteredSites.toArray(new TestingSite[0]);
    }

    public TestingSite filterById(String testingSiteId){
        for(TestingSite testingSite: testingSiteList){
            if(Objects.equals(testingSite.getId(), testingSiteId)){
                return testingSite;
            }
        }
        return null;
    }

    public TestingSite filterByName(String testingSiteName){
        for(TestingSite testingSite: testingSiteList){
            if(Objects.equals(testingSite.getName(), testingSiteName)){
                return testingSite;
            }
        }
        return null;
    }

}
