package support;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DriverQA {

    private static WebDriver driver;
    private static final long DEFAULT_WAIT_TIMEOUT = 20;
    private static final long IMPLICITLY_WAIT_TIMEOUT = 10;

    public void start(String parBrowser) {
        String title;
        try {
            title = driver.getTitle();
        } catch (Exception e) {
            title = "ERROR";
        }
        if (title.equals("ERROR")) {
            switch (parBrowser) {
                case "firefox":
                    FirefoxDriverManager.getInstance().setup();
                    FirefoxOptions options = new FirefoxOptions();
                    options.addPreference(FirefoxDriver.MARIONETTE, true);
                    driver = new FirefoxDriver(options);
                    driver.manage().window().maximize();
                    break;
                case "chrome":
//                    ChromeDriverManager.getInstance().setup();
                    ChromeOptions optionsC = new ChromeOptions();
                    // hides the info message that says chrome is being controlled by automated test software
                    optionsC.addArguments(Arrays.asList(
                            "disable-infobars", "ignore-certificate-errors",
                            "start-maximized"));
                    driver = new ChromeDriver(optionsC);
                    break;
                default:
                    break;
            }
        }
        driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIMEOUT, TimeUnit.SECONDS);
    }

    public static WebDriver getDriver() {
        return driver;
    }

    private String getAttributeType(String... parType) {
        String type;
        if (parType.length == 0) {
            type = "id";
        } else {
            type = parType[0];
        }
        return type;
    }

    private By getLocatorBy(String parValue, String... parType) {
        final String selector = getAttributeType(parType);
        switch (selector) {
            case "id":
                return By.id(parValue);
            case "name":
                return By.name(parValue);
            case "css":
                return By.cssSelector(parValue);
            case "xpath":
                return By.xpath(parValue);
            case "link":
                return By.linkText(parValue);
            default:
                return By.id(parValue);
        }
    }

    private WebElement findElem(String parValue, String... parType) {
        final By locator = getLocatorBy(parValue, parType);
        WebElement element;
        try {
            element = driver.findElement(locator);
        } catch (NoSuchElementException e) {
            element = null;
        }
        return element;
    }

    private List<WebElement> findElems(String parValue, String... parType) {
        List<WebElement> elements;
        final By locator = getLocatorBy(parValue, parType);
        try {
            elements = driver.findElements(locator);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            elements = Collections.emptyList();
        }
        return elements;
    }

    public void click(String parValue, String... parType) {
        final WebElement element = findElem(parValue, parType);
        element.click();
    }

    public void openURL(String parUrl) {
        driver.get(parUrl);
    }

    public void quit() {
        driver.quit();
    }

    public void close() {
        driver.close();
    }

    public void sendKeys(String parText, String parName, String... parType) {
        final WebElement element = findElem(parName, parType);
        element.clear();
        element.sendKeys(parText);
    }

    public String getText(String parValue, String... parType) {
        final WebElement element = findElem(parValue, parType);
        return element.getText();
    }

    public List<String> getTexts(String parValue, String... parType) {
        final List<WebElement> elements = findElems(parValue, parType);
        final List<String> texts = new ArrayList<>();
        for (WebElement element : elements) {
            texts.add(element.getText().replace("\n", ""));
        }
        return texts;
    }

    public void selectByIndex(Integer parIndex, String parName, String... parType) {
        final WebElement element = findElem(parName, parType);
        final Select dropdown = new Select(element);
        dropdown.selectByIndex(parIndex);
    }

    public void selectByText(String parText, String parName, String... parType) {
        final WebElement element = findElem(parName, parType);
        final Select dropdown = new Select(element);
        dropdown.selectByVisibleText(parText);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void waitElement(String parName, String... parType) {
        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        final By locator = getLocatorBy(parName, parType);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (NoSuchElementException e) {
            System.out.println("ERROR WAIT => " + e.toString());
        } catch (TimeoutException e) {
            System.err.println("Timeout reached while waiting for element =>" + e.toString());
        }
    }

    public void waitInvisibilityOfElement(String parName, String... parType) {
        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        final By locator = getLocatorBy(parName, parType);

        /* Temporarily removing the implicitly wait timeout, because in here the element is for sure on screen,
         * making it wait unnecessarily if the element is gone before the timeout */
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (NoSuchElementException e) {
            System.err.println("ERROR WAIT =>" + e.toString());
        } catch (TimeoutException e) {
            System.err.println("Timeout reached and element is still visible =>" + e.toString());
        }
        // Resetting implicitly wait to the default
        driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void waitElementToBeClickable(String parName, String... parType) {
        final WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        final By locator = getLocatorBy(parName, parType);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (NoSuchElementException e) {
            System.err.println("ERROR WAIT => " + e.toString());
        } catch (TimeoutException e) {
            System.err.println("Element is not clickable => " + e.toString());
        }
    }

    public void switchTo(String... parValue) {
        if (parValue.length == 0) {
            driver.switchTo().defaultContent();
        } else {
            driver.switchTo().window(String.valueOf(parValue));
        }
    }

    public void ChooseOkOnNextConfirmation() {
        final Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void ChooseCancelOnNextConfirmation() {
        final Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    public String getAttribute(String identifier, String attribute, String... type) {
        final WebElement element = findElem(identifier, type);
        return element.getAttribute(attribute);
    }

    public boolean isDisplayed(String identifier, String... type) {
        try {
            final WebElement element = findElem(identifier, type);
            return element.isDisplayed();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void doubleClick(String parValue, String... parType) {
        final WebElement element = findElem(parValue, parType);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('dblclick',{bubbles:true}));", element);
    }

    public TakesScreenshot getCamera() {
        return (TakesScreenshot) driver;
    }

    public List<WebElement> getElements(String parValue, String... parType) {
        return findElems(parValue, parType);
    }
}
