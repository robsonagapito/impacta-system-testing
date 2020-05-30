package pages;

import support.DriverQA;

public class LoginOk extends BasePage{

    public LoginOk(DriverQA stepDriver) {
        super(stepDriver);
    }

    public String getResult(){
        return driver.getText("result");
    }
}
