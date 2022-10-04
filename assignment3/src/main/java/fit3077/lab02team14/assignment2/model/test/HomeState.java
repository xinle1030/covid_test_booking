package fit3077.lab02team14.assignment2.model.test;

import java.net.http.HttpResponse;

import fit3077.lab02team14.assignment2.services.NetworkHelper;

public class HomeState implements TestModeState {

    @Override
    public void updateTestState(CovidTest covidTest) {

        covidTest.setTestModeState(this);

        // update test kit status - where collected or not
        CovidTestAdditionalInfo covidTestAdditionalInfo = covidTest.getAdditionalInfo();
        HttpResponse<String> patchBookingRes = NetworkHelper.patchRequest("/covid-test/" + covidTest.getId(),
                covidTestAdditionalInfo.getPostCovidTestAdditionalInfo());
        System.out.println(patchBookingRes.body() + "\n");
    }
}
