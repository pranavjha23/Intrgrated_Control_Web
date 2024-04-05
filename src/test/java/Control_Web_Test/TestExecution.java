package Control_Web_Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import org.openqa.selenium.JavascriptExecutor;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.common.io.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;


public class TestExecution {

	WebDriver driver;
	ExtentReports extent;
	ExtentHtmlReporter htmlReporter;
	
	@BeforeSuite
	public void setup() {
		
		htmlReporter = new ExtentHtmlReporter("extent2.html");
	
		extent=new ExtentReports();
		extent.attachReporter(htmlReporter);
	}

	@BeforeTest
	public void SetupTest() {
		
		System.setProperty("webdriver.chrome.driver", "E:\\driver\\chromedriver-win32\\chromedriver-win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@Test(priority = 1)
	public void loginWithOTPFromYopmail() throws InterruptedException {
	
		
		ExtentTest Test1 = extent.createTest("Login Page");
		
		Test1.log(Status.INFO, "Starting Test");
		driver.get("https://kic-qa-rs.lgerobot.com/rome");
		Reporter.log("pass");

		String parentHandle = driver.getWindowHandle();
		System.out.print("parent window - "+ parentHandle);

		Test1.log(Status.PASS, "Entet UserName");
		driver.findElement(By.xpath("//*[@id=\"id\"]")).sendKeys("lgsi_l3@yopmail.com");
		
		Test1.log(Status.PASS, "Entet Password");
		driver.findElement(By.xpath("//*[@id=\"pwd\"]")).sendKeys("1qaz2wsx*");
		
		Test1.log(Status.PASS, "Click on Login Button");
		driver.findElement(By.xpath("//*[@id=\"loginBtn\"]")).click();
		// Navigate to Yopmail and get the OTP from the email
		driver.switchTo().newWindow(WindowType.TAB);

		// opening another url in the new tab
		driver.get("https://yopmail.com");

		// Enter the Yopmail inbox
		WebElement yopmailInbox = driver.findElement(By.id("login"));
		yopmailInbox.sendKeys("lgsi_l3@yopmail.com");
		yopmailInbox.sendKeys(Keys.ENTER);

		// Switch to the iframe
		driver.switchTo().frame("ifmail");

		// Extract the OTP from the email content
		
		WebElement otpElement = driver.findElement(By.xpath("/html/body/main/div/div/div/table/tbody/tr/td/table[1]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td"));
		String otp = otpElement.getText();
		// String text = otpElement.getText();
		System.out.println("Text: " + otp);

		Set<String> handels = driver.getWindowHandles();

		for(String handle : handels) {
			System.out.print(handle);
			if (!handle.equals(parentHandle)) {
				driver.switchTo().window(handle);

				driver.close();
			}
		}

		driver.switchTo().window(parentHandle);

		// Enter the OTP into the OTP field on the login page 
		
		Test1.log(Status.PASS, "Enter the OTP");
		WebElement otpField = driver.findElement(By.xpath("/html/body/div[10]/div/div/div[2]/ul/li/div[1]/div/input"));
		otpField.sendKeys(otp)  ;                                                                   
		System.out.println("Page title is2 : " + driver.getTitle());

        Test1.log(Status.PASS, "Click on Login Button");
		WebElement otpSubmitButton = driver.findElement(By.xpath("//button[@id='verification_OTP_Btn2']"));
		otpSubmitButton.click();
		
		Test1.log(Status.INFO, "Login Test Completed");

	}
	@Test(priority = 2)
	public void navigateToRealtimeMenu() throws InterruptedException, IOException {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		ExtentTest Test2 = extent.createTest("RealTime Information");
		
		
		//Real time information
		
		Test2.log(Status.INFO, "Enter into the RealTime Information screen");
		driver.findElement(By.xpath("//*[@id=\"gnb\"]/ul/li[2]/a/span")).click();

		//select the group
		Test2.log(Status.PASS, "Select LGSI Group");
		driver.findElement(By.xpath("//*[@id='group-dropdown-options']")).click();
		driver.findElement(By.xpath("//ul[@class='dropdown-menu show']//button[@class='dropdown-item'][normalize-space()='LGSI']")).click();
		Thread.sleep(2000);  

		//select the site
		Test2.log(Status.PASS, "Select LGSI Site");
		driver.findElement(By.xpath("//*[@id='branch-dropdown-options']")).click();
		driver.findElement(By.xpath("//button[normalize-space()='Guidebot']")).click();

		Test2.log(Status.PASS, "In RealTime Screen, When There is NO robot registered ");
		WebElement text =driver.findElement(By.xpath("//div[@id='realtime_no_robot_full']//span[contains(text(),'No registered robot.')]"));

		String expectedtext ="No registered robot.";
		String actualtext =text.getText();
		System.out.print(actualtext);

		assertEquals(actualtext,expectedtext, "Robot register = ");

		Thread.sleep(5000);

		File f= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(f, new File("E:\\Selenium SS\\Cloi Station.png"));

		Test2.log(Status.INFO, "Testcase 2 completed");
	}

	@Test(priority = 3)
	public void navigateToDashboard() throws InterruptedException, IOException {    
		//ROBOTSTC-8125 - TC_ControlWeb_UX_6_5_4_통계 현황 Flow : 데이터 다운로드_001

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		ExtentTest Test3 = extent.createTest("Dadhboard(Notice)");
		Test3.log(Status.INFO, "TestCase 3 - Dashboard Notice");
		//select the site
		
		Test3.log(Status.PASS, "Change the Site");
		driver.findElement(By.xpath("//*[@id='branch-dropdown-options']")).click();
		driver.findElement(By.xpath("//button[normalize-space()='LGSIServe3.0']")).click();
        
		//Dashboard
		driver.findElement(By.xpath("//span[normalize-space()='Dashboard']")).click();
		// Click on notice
		driver.findElement(By.xpath("//p[@id='noti_title']")).click();
		// click on close button
		driver.findElement(By.xpath("//button[@class='button secondary h_m'][normalize-space()='Close']")).click();
		//click on notice	
		driver.findElement(By.xpath("//p[@id='noti_title']")).click();
		//click on notice list --> Navigate to the Notification GNB -> Notice tab
		driver.findElement(By.xpath("//button[@id='btnViewNotice']")).click();
		
		File f= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(f, new File("E:\\Selenium SS\\Cloi Station Statistic.png"));
	}

	@Test(priority = 4)
	public void navigateToStatistics() throws InterruptedException, IOException {

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//ROBOTSTC-8573 - TC_ControlWeb_UX_6_7_에러통계 Flow : 탭 이동_001
		
		ExtentTest Test3 = extent.createTest("(Statistics)");
		Test3.log(Status.INFO, "TestCase 4 - Statistics");
		//Click on statistics
		driver.findElement(By.xpath("//span[normalize-space()='Statistics']")).click();	
		//Click on date search
		driver.findElement(By.xpath("//input[@id='st_date_search_week']")).click();		
		// select a date
		driver.findElement(By.xpath("//a[normalize-space()='1']")).click();	
		// Click on error tab
		driver.findElement(By.xpath("//button[@id='error-tab']")).click();

		File f= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(f, new File("E:\\Selenium SS\\Cloi Station Notice pop-up.png"));
	}
		
	@Test(priority = 5)
	public void RealTimeinfo() throws InterruptedException {
		
		//https://alm-lge.singlex.com/polarion/#/project/RobotSW_TC_Mgmt/workitem?id=ROBOTSTC-7929
		
		//Enter to realtime
		driver.findElement(By.xpath("//span[normalize-space()='Real-time Information']")).click();
		//Click on select robot status
		driver.findElement(By.xpath("//button[@id='robotStateChecker']")).click();
		//Unselect all the robot status
		driver.findElement(By.xpath("//label[normalize-space()='All robot status']")).click();
		//select the operation status
		driver.findElement(By.xpath("//label[normalize-space()='Operation']")).click();
		//Click on Confirm button
		driver.findElement(By.xpath("//button[@class='button primary robot_state_submit']")).click();

	}
	
	@Test(priority = 6)
	public void Notification() throws InterruptedException {
		
		//https://alm-lge.singlex.com/polarion/#/project/RobotSW_TC_Mgmt/workitem?id=ROBOTSTC-8089
		
		//Click on the notification
		driver.findElement(By.xpath("//span[normalize-space()='Notifications']")).click();
		//click on the notice tab
		driver.findElement(By.xpath("//button[@id='notice-tab']")).click();
		//click on the make notice button
		driver.findElement(By.xpath("//button[normalize-space()='Make notice']")).click();
		//Enter the title details
		driver.findElement(By.xpath("//input[@id='registerBoardTitle']")).sendKeys("Notice_Test");
		//Enter description
		driver.findElement(By.xpath("//textarea[@id='floatingTextarea']")).sendKeys("Hi pranav, How are you ");
		//Click on the register button
		driver.findElement(By.xpath("//button[@id='btnRegistNotice']")).click();

	}
	
		@AfterClass
		public void TeardownTest() {
			
			
			//driver.close();
		}
		
		@AfterSuite
		public void Teardown() {
			extent.flush();
	}
}

