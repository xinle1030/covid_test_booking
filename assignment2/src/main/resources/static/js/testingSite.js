let covidTestType = document.getElementById("covidTestType");
let covidTestMode = document.getElementById("covidTestMode");
let isTestKitPickedUp = document.getElementById("isTestKitPickedUp");
isTestKitPickedUp.setAttribute("disabled", "disabled");
initialSetUp();

covidTestType.addEventListener("change", ()=>{
    initialSetUp();
})

covidTestMode.addEventListener("change", ()=>{
    if (covidTestMode.value == CovidTestModeEnum.HOME && covidTestType.value == CovidTestTypeEnum.RAT){
        isTestKitPickedUp.removeAttribute("disabled");
        isTestKitPickedUp.value = isTestKitPickedUpEnum.NO;
    }
    else{
        resetIsTestKitPickedUp();
    }
})

function resetIsTestKitPickedUp(){
    isTestKitPickedUp.setAttribute("disabled", "disabled");
    isTestKitPickedUp.value = isTestKitPickedUpEnum.NO;
}

function initialSetUp(){
    if (covidTestType.value == CovidTestTypeEnum.PCR){
        covidTestMode.value = CovidTestModeEnum.ONSITE;
        covidTestMode.setAttribute("disabled", "disabled");
        resetIsTestKitPickedUp();
    }
    else{
        covidTestMode.removeAttribute("disabled");
        isTestKitPickedUp.value = isTestKitPickedUpEnum.NO;
    }
}