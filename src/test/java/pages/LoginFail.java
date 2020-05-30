package pages;

import support.DriverQA;

public class LoginFail extends BasePage{

    public LoginFail(DriverQA stepDriver) {
        super(stepDriver);
    }

    public String getResult(){
        return driver.getText("result");
    }
}
