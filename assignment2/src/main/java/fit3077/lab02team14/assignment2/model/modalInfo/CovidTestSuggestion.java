package fit3077.lab02team14.assignment2.model.modalInfo;

import fit3077.lab02team14.assignment2.constants.CovidTestType;
import fit3077.lab02team14.assignment2.constants.DropdownAns;

public class CovidTestSuggestion {
    String[] symptoms = {};
    String closeContact;
    String closeContactPeriod;
    String symptomsPeriod;
    String[] suffers = {};

    public CovidTestSuggestion(String[] symptoms, String closeContact, String closeContactPeriod, String symptomsPeriod, String[] suffers) {
        this.closeContact = closeContact;
        this.closeContactPeriod = closeContactPeriod;
        this.symptomsPeriod = symptomsPeriod;
        setSymptoms(symptoms);
        setSuffers(suffers);
    }

    public String[] getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String[] symptoms) {
        if (symptoms != null){
            this.symptoms = symptoms;
        }
    }

    public String getCloseContact() {
        return closeContact;
    }

    public void setCloseContact(String closeContact) {
        this.closeContact = closeContact;
    }

    public String getCloseContactPeriod() {
        return closeContactPeriod;
    }

    public void setCloseContactPeriod(String closeContactPeriod) {
        this.closeContactPeriod = closeContactPeriod;
    }

    public String getSymptomsPeriod() {
        return symptomsPeriod;
    }

    public void setSymptomsPeriod(String symptomsPeriod) {
        this.symptomsPeriod = symptomsPeriod;
    }

    public String[] getSuffers() {
        return suffers;
    }

    public void setSuffers(String[] suffers) {
        if (suffers != null){
            this.suffers = suffers;
        }
    }

    public CovidTestType suggestTestType(){
        CovidTestType suggestedCovidTestType = CovidTestType.RAT;
        if (closeContact.equals(DropdownAns.YES.getValue()) && closeContactPeriod.equals(DropdownAns.YES.getValue())){
            suggestedCovidTestType = CovidTestType.PCR;
        }
        else if ((closeContact.equals(DropdownAns.INVALID.getValue()) || closeContact.equals(DropdownAns.NO.getValue())) && symptomsPeriod.equals(DropdownAns.YES.getValue())){
            suggestedCovidTestType = CovidTestType.RAT;
        }

        if (suffers.length > 0 || symptoms.length > 3){
            suggestedCovidTestType = CovidTestType.PCR;
        }
        return suggestedCovidTestType;
    }
}
