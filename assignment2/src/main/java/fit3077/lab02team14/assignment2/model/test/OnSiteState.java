package fit3077.lab02team14.assignment2.model.test;

import java.net.http.HttpResponse;

import fit3077.lab02team14.assignment2.services.NetworkHelper;

public class OnSiteState implements TestModeState {

    @Override
    public void updateTestState(CovidTest covidTest) {
        // TODO Auto-generated method stub
        covidTest.setTestModeState(this);

        // update test type and administerer
        HttpResponse<String> patchBookingRes = NetworkHelper.patchRequest("/covid-test/" + covidTest.getId(),
                covidTest.getPostCovidTest(), false);
        System.out.println(patchBookingRes.body() + "\n");
    }
}
