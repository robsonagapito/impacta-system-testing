package pages;

import support.DriverQA;

/**
 * @author Victor Moraes
 */
class BasePage {
    final DriverQA driver;

    BasePage(DriverQA stepDriver) {
        this.driver = stepDriver;
    }
}
