package support;

import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DatePicker {

    private final String dateFormat = "dd MMMM yyyy";
    private final DriverQA driver;

    private final String prev = "provide locator of calendar's previous button";
    private final String next = "provide locator of calendar's next button";
    private final String curDate = "provide locator of calendar title (current month and year)";
    private final String dates = "provide locator that finds all days";

    public DatePicker(DriverQA driver) {
        this.driver = driver;
    }

    public void setDate(String date) {

        String formattedDate = formatDate(date);
        long diff = this.getDateDifferenceInMonths(formattedDate);
        int day = this.getDay(formattedDate);

        String arrow = diff >= 0 ? next : prev;
        diff = Math.abs(diff);

        //click the arrows
        for (int i = 0; i < diff; i++) {
            driver.click(arrow, "css");
        }

        //select the date
        driver.getElements(dates, "css").stream()
                .filter(ele -> Integer.parseInt(ele.getText()) == day)
                .findFirst()
                .ifPresent(WebElement::click);

    }

    private int getDay(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withLocale(Locale.US);
        LocalDate dpToDate = LocalDate.parse(date, dtf);
        return dpToDate.getDayOfMonth();
    }

    private long getDateDifferenceInMonths(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withLocale(Locale.US);
        LocalDate dpCurDate = LocalDate.parse("01 " + this.getCurrentMonthFromDatePicker(), dtf);
        LocalDate dpToDate = LocalDate.parse(date, dtf);
        return YearMonth.from(dpCurDate).until(dpToDate, ChronoUnit.MONTHS);
    }

    private String getCurrentMonthFromDatePicker() {
        return driver.getText(this.curDate, "css");
    }

    private String formatDate(String date) {
        //user input date format. Change as needed
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("M/d/yyyy").withLocale(Locale.US);
        LocalDate curDate = LocalDate.parse(date, inputFormat);
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern(dateFormat).withLocale(Locale.US);
        return curDate.format(outputFormat);
    }

}
