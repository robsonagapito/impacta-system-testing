package steps;

//import cucumber.api.Scenario;
//import cucumber.api.java.After;
//import cucumber.api.java.Before;
import io.cucumber.java.*;
import org.openqa.selenium.TakesScreenshot;
import support.BaseSteps;

import java.util.Random;

import static org.openqa.selenium.OutputType.BYTES;

public class Hooks extends BaseSteps {

    Scenario scenarioAux;


    @Before
    public void beforeScenario(Scenario scenario) {
        scenarioAux = scenario;
        System.out.println("--- Starting scenario " + scenario.getName() + " execution ---");
    }

    @AfterStep
    public void afterStep() {
        System.out.println("\n--- Scenario " + scenarioAux.getName() + " executed ---");
        scenarioAux.log("url: " + driver.getCurrentUrl());
        TakesScreenshot camera = driver.getCamera();
        byte[] screenshot = camera.getScreenshotAs(BYTES);
        String name = "imagem_" + new Random();
        scenarioAux.attach( screenshot,"image/png", name);
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("\n--- Scenario " + scenario.getName() + " executed ---");

        if(scenario.isFailed()){
            scenario.log("url: " + driver.getCurrentUrl());
            TakesScreenshot camera = driver.getCamera();
            byte[] screenshot = camera.getScreenshotAs(BYTES);
            scenario.attach( screenshot,"image/png", "teste imagem");
    }

        driver.quit();
    }
}
