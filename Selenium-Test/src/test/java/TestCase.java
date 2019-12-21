import com.beust.jcommander.Parameter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestCase {

    DriverUtils driverUtils;
    WebDriver driver;
    WebElement highProdEle;
    int highProdAvailablePageNo = 1;
    double highProdValue = 0.00;
    String highProdValueName = "";

public TestCase(){
    driverUtils = new DriverUtils();
}
    @BeforeTest
    @Parameters({ "browserType" })
    private void BeforeTest(String  browserType){
        driverUtils.InitializeDriver(browserType);
    }
    @Test
    public void test() {
        driver = driverUtils.Driver;
        //Step-1 : Navigate to shoppe web site
        driverUtils.NavigateTo("https://shopee.sg");
        assertTrue(driverUtils.Driver.getTitle().startsWith("Shopee Singapore"));
        System.out.println("Step-1 : Launched Browser and navigated to Shoppe website successfully.");

        // Close shopee popups
        driverUtils.closeShoppePopups();

        // Search with Keyword
        driverUtils.searchProductByKeyword("Toy");

        // Get search result
          ClickOnHighestPriceToyFromSearchResultPages();

        //print product details
        printProductDetails();

        // Add product quantity
        AddProductQuantity();


        //Click Add cart btn
        ClickOnAddCartBtn();

        // login to shopee
        LoginToShoppe();  // After entering credentials login screen expecting OTP. Hence, unable to verify following steps in script

        ////Verify card added items
        //VerifyCartItems();
    }

    public void VerifyCartItems() {
        driver = driverUtils.Driver;
        driver.findElement(By.className("cart-drawer-container")).click();
        driverUtils.WaitForPageLoad(By.className("cart-page-logo__page-name"));

        List<WebElement> cartItems = driver.findElements(By.className("cart-item__content"));

        for (WebElement cartItem : cartItems) {
            if(cartItem.findElement(By.className("cart-item-overview__name")).getText().equals(highProdValueName)){
                assertEquals(cartItem.findElement(By.cssSelector("span[class$='cart-item__unit-price--after']")).getText(),(String.valueOf(highProdValue)));
                assertEquals(cartItem.findElement(By.cssSelector("input[class$='_18Y8Ul']")).getText(),"8");
            }
        }
    }

    protected void LoginToShoppe() {

        driver.findElement(By.name("loginKey")).sendKeys("testjagan");
        driver.findElement(By.name("password")).sendKeys("Test`123");
        driver.findElement(By.cssSelector("button[class$='_3JP5il']")).click();
    }

    protected void ClickOnAddCartBtn() {
        driver.findElement(By.cssSelector("button[class$='_3a6p6c']")).click();
    }

    protected void AddProductQuantity() {

        driver.findElement(By.cssSelector("input[class$='_18Y8Ul']")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.cssSelector("input[class$='_18Y8Ul']")).sendKeys("2");
    }

    protected void ClickOnHighestPriceToyFromSearchResultPages() {

        for(int page = 1; page<= 5; page++){
            GetHighestProductInThisPage(page);
            driver.findElement(By.cssSelector("button[class$='shopee-mini-page-controller__next-btn']")).click();
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("-------Highest product page no is " + highProdAvailablePageNo);
        System.out.println("-------Highest product element is " + highProdEle);
        System.out.println("--------------------------------------------------------------");
        NavigateToPage(highProdAvailablePageNo);
    }

    protected void NavigateToPage(int highProdAvailablePageNo) {
        int currentResultsPage = Integer.parseInt(driver.findElement(By.className("shopee-mini-page-controller__current")).getText());

        int diff = highProdAvailablePageNo - currentResultsPage;
        if(diff > 0){
            NavigateForward(diff);
        } else
        {
            NavigateBack(diff);
        }

        driver.findElement(By.xpath("//div[text()='"+highProdValueName+"']")).click();
    }

    private void NavigateBack(int diff) {
        while(diff<0){
            driver.findElement(By.cssSelector("button[class*='shopee-mini-page-controller__prev-btn']")).click();
            diff++;
        }
    }

    private void NavigateForward(int diff) {
        while(diff>0){
            driver.findElement(By.cssSelector("button[class$='shopee-mini-page-controller__next-btn']")).click();
            diff--;
        }
    }

    protected void GetHighestProductInThisPage(int pageNo) {
        driverUtils.WaitForPageLoad(By.className("header-with-search__logo-section"));
        List<WebElement> items = driver.findElements(By.className("_3eufr2"));

        for (WebElement item : items)
        {
            double ProdValue = Double.parseDouble(item.findElement(By.className("_341bF0")).getText());
            if(ProdValue >= highProdValue){
                highProdValue = ProdValue;
                highProdEle = item;
                highProdAvailablePageNo = pageNo;
                highProdValueName = item.findElement(By.className("O6wiAW")).getText();
            }
        }
    }

    protected void printProductDetails() {

        System.out.println("============ Product Name        : " + driver.findElement(By.className("qaNIZv")).getText());
        System.out.println("============ Rating is           : " + driver.findElement(By.cssSelector("div[class$='_32fuIU']")).getText());
        //    System.out.println("============ Current price is    : " + driver.findElement(By.className("_3_ISdg")).getText());
        System.out.println("============ Discounted price is : " + driver.findElement(By.className("_3n5NQx")).getText());
        //   System.out.println("============ OFF percentage is   : " + driver.findElement(By.className("MITExd")).getText());
        System.out.println("============ Shipping fee is     : " + driver.findElement(By.cssSelector("div[class*='BtHdNz']")).getText());
    }


    @AfterTest
    private void AfterTest(){
    if(driver != null)
        driver.quit();
    }
}
