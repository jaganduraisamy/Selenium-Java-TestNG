import com.beust.jcommander.Parameter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverUtils {

    public WebDriver Driver;


    public void InitializeDriver(String browserType){

        if(browserType.equals("chrome")) {


            System.setProperty("webdriver.chrome.driver", "./\\src\\Backup\\chromedriver.exe");
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", prefs);

            Driver = new ChromeDriver(options);


        }
        else if(browserType.equals("firefox")){
            System.out.println("Run fo Firefox browser.");
            System.setProperty("webdriver.gecko.driver", "./\\src\\Backup\\geckodriver.exe");
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setCapability("marionette", true);
            Driver = new FirefoxDriver(capabilities);

        }

        else if(browserType.equals("edge")){
            System.out.println("Run fo Edge browser.");
            System.setProperty("webdriver.edge.driver", "./\\src\\Backup\\MicrosoftWebDriver.exe");
            DesiredCapabilities capabilities = DesiredCapabilities.edge();
            Driver = new EdgeDriver(capabilities);
        }

        Driver.manage().window().maximize();
        Driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

    }

    public void NavigateTo(String url){
        Driver.get(url);
        WaitForPageLoad(By.className("header-with-search__logo-section"));
       }

    public void WaitForPageLoad(By element) {

        WebDriverWait wait = new WebDriverWait(Driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void closeShoppePopups() {
        WaitForPageLoad(By.className("header-with-search__logo-section"));
        Driver.findElement(By.className("shopee-popup__close-btn")).click();
    }

    protected void searchProductByKeyword(String searchText){
        Driver.findElement(By.className("shopee-searchbar-input__input")).sendKeys(searchText);
        Driver.findElement(By.cssSelector("button[class*='btn-solid-primary']")).click();
    }
}
