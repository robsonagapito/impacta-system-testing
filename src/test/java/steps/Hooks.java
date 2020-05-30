package steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.TakesScreenshot;
import support.BaseSteps;

import static org.openqa.selenium.OutputType.BYTES;

public class Hooks extends BaseSteps {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("--- Starting scenario " + scenario.getName() + " execution ---");
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("\n--- Scenario " + scenario.getName() + " executed ---");

        if(scenario.isFailed()){
            scenario.write("url: " + driver.getCurrentUrl());
            TakesScreenshot camera = driver.getCamera();
            byte[] screenshot = camera.getScreenshotAs(BYTES);
            scenario.embed(screenshot,"image/png");
        }

        driver.quit();
    }
}
