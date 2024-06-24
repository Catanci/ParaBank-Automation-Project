package para.bank;

import com.zebrunner.carina.core.AbstractTest;
import com.zebrunner.carina.webdriver.core.capability.impl.desktop.ChromeCapabilities;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import para.bank.pages.AbstractParaBankPage;

import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
public class AbstractParaBankPageTest extends AbstractTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractParaBankPageTest.class);


    public long getBottomY(ExtendedWebElement element) {
        long elementTopPointY = element.getLocation().getY();
        int elementHeight = element.getSize().getHeight();
        return elementTopPointY + elementHeight;
    }

    public boolean waitUntilElementStatic(AbstractParaBankPage abstractPage, ExtendedWebElement element, Duration timeout) {
        AtomicInteger elementPosition = new AtomicInteger(0);
        return abstractPage.waitUntil(d -> {
            int currentY = element.getLocation().getY();
            return currentY == elementPosition.getAndSet(currentY);
        }, timeout.toSeconds());
    }

    public static MutableCapabilities prepareLoggingCapabilities() {
        ChromeOptions options = new ChromeCapabilities().getCapability("chrome");
        options.setCapability("goog:loggingPrefs", prepareLoggingPreferences());
        return options;
    }

    private static Map<String, Object> prepareLoggingPreferences() {
        LoggingPreferences logPreferences = new LoggingPreferences();
        logPreferences.enable(LogType.PERFORMANCE, Level.ALL);
        return logPreferences.toJson();
    }

    public String createRandomStrongPassword() {
        return createRandomStrongPassword(10);
    }

    public String createRandomStrongPassword(int size) {
        String pass = RandomStringUtils.randomAlphabetic(size - 4) +
                RandomStringUtils.randomAlphabetic(1).toUpperCase() +
                RandomStringUtils.randomAlphabetic(1).toLowerCase() +
                RandomStringUtils.randomNumeric(1);
        return pass + getRandomSpecialChar();
    }

    public String createRandomText(int charactersCount) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        boolean isWordCapitalized = true;
        while (charactersCount > 0) {
            int wordSize = random.nextInt(8) + 1;
            wordSize = charactersCount - wordSize < 0 ? charactersCount : wordSize;
            charactersCount = charactersCount - wordSize;
            String newWord = RandomStringUtils.randomAlphabetic(wordSize).toLowerCase();
            if (isWordCapitalized) {
                newWord = StringUtils.capitalize(newWord);
            }
            stringBuilder.append(newWord);
            isWordCapitalized = false;
            if (charactersCount > 1) {
                if (charactersCount <= wordSize) {
                    stringBuilder.append(RandomStringUtils.randomAlphabetic(charactersCount).toLowerCase());
                    break;
                } else {
                    int rand = random.nextInt(5);
                    if (rand == 0) {
                        charactersCount = charactersCount - 1;
                        stringBuilder.append(",");
                    } else if (rand == 1) {
                        charactersCount = charactersCount - 1;
                        stringBuilder.append(".");
                        isWordCapitalized = true;
                    }
                    charactersCount = charactersCount - 1;
                    stringBuilder.append(" ");
                }
            }
        }
        return stringBuilder.toString();
    }

    public char getRandomSpecialChar() {
        Random random = new Random();
        String specialSymbols = "[$&+=?@#|/\"<>^*()%!]~`";
        return specialSymbols.toCharArray()[random.nextInt(specialSymbols.length())];
    }

    public static String createRandomAddress() {
        int streetNumber = new Random().nextInt(8999) + 1000; // 1000 to 9999
        String streetName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(10).toLowerCase());
        return String.format("%d %s St", streetNumber, streetName);
    }

    public static String createRandomPhoneNumber() {
        return String.format("%s-%s-%s",
                RandomStringUtils.randomNumeric(3),
                RandomStringUtils.randomNumeric(3),
                RandomStringUtils.randomNumeric(4));
    }

    public static String createRandomSSN() {
        return String.format("%s-%s-%s",
                RandomStringUtils.randomNumeric(3),
                RandomStringUtils.randomNumeric(2),
                RandomStringUtils.randomNumeric(4));
    }

    public static String createRandomUsername() {
        return "user_" + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
    }
}
