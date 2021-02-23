package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunctions.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
	String inputpath="C:\\Eclipse programs\\DecHybrid-master\\TestInput\\HybridTest.xlsx";
	String outputpath="C:\\Eclipse programs\\DecHybrid-master\\TestOutput\\HybridResults.xlsx";
	WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	public void startTest()throws Throwable
	{
		//creating excel object to access excel utilities
		ExcelFileUtil xl= new ExcelFileUtil(inputpath);
		//iterate all rows in Mastertestcases sheet
		for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
		{
			String moduleStatus="";
			if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
			{
				//Define Module Name
				String TCModule=xl.getCellData("MasterTestCases", i, 1);
		//set path for generating reports
				report= new ExtentReports("./ExtentReports/"+FunctionLibrary.generateDate()+TCModule+".html");
				//iterate all rows in TCModule
				for(int j=1; j<=xl.rowCount(TCModule);j++)
				{
					//test case starts here
					test=report.startTest(TCModule);
					//read all cells from TCModule
					String Description=xl.getCellData(TCModule, j, 0);
					String Function_Name=xl.getCellData(TCModule, j, 1);
					String Locator_Type=xl.getCellData(TCModule, j, 2);
					String Locator_Value=xl.getCellData(TCModule, j, 3);
					String Test_Data=xl.getCellData(TCModule, j, 4);
					//calling the test step functions
					try {
						if(Function_Name.equalsIgnoreCase("startbrowser"))
						{
							driver=FunctionLibrary.startbrowser();
							test.log(LogStatus.INFO, Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("openApplication"))
						{
							FunctionLibrary.openApplication(driver);
							test.log(LogStatus.INFO, Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
							test.log(LogStatus.INFO, Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
							test.log(LogStatus.INFO, Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
							test.log(LogStatus.INFO, Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("titleValidation"))
						{
							FunctionLibrary.titleValidation(driver, Test_Data);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser(driver);
							test.log(LogStatus.INFO, Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("captureData"))
						{
							FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
							test.log(LogStatus.INFO, Description);
						}
							
						else if(Function_Name.equalsIgnoreCase("stableValidation"))
						{
							FunctionLibrary.stableValidation(driver, Test_Data);
							test.log(LogStatus.INFO, Description);
						}
						else if(Function_Name.equalsIgnoreCase("stockCategories")) {
							FunctionLibrary.stockCategories(driver);
							test.log(LogStatus.INFO,Description);
							
						}
						else if(Function_Name.equalsIgnoreCase("sttableValidation")) {
					FunctionLibrary.sttableValidation(driver,Test_Data);
					test.log(LogStatus.INFO,Description);
						}
						//write as pass into status cell in TCModule
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						test.log(LogStatus.PASS, Description);
						moduleStatus="true";
					}catch(Exception e)
					{
						System.out.println(e.getMessage());
						//write as fail into status cell in TCModule
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						test.log(LogStatus.FAIL, Description);
						moduleStatus="False";
						File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen,new File("./Screenshot/"+Description+" "+FunctionLibrary.generateDate()+".png"));
						String image=test.addScreenCapture("./Screenshot/"+Description+" "+FunctionLibrary.generateDate()+".png");
					    test.log(LogStatus.FAIL,image);
					    break;
					   }
					catch(AssertionError e) {
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						test.log(LogStatus.FAIL, Description);
						moduleStatus="False";
						File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(screen,new File("./Screenshot/"+Description+" "+FunctionLibrary.generateDate()+".png"));
						String image=test.addScreenCapture("./Screenshot/"+Description+" "+FunctionLibrary.generateDate()+".png");
					    test.log(LogStatus.FAIL,image);
					    break;
					}
					if(moduleStatus.equalsIgnoreCase("true"))
					
						xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath);	
					}
					if(moduleStatus.equalsIgnoreCase("false"))
					{
						xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
					}
					report.endTest(test);
					report.flush();
					
				}
			
		else
		{
			//write as blocked in to master testcases sheet
			xl.setCellData("MasterTestCases", i, 3, "Blocked", outputpath);
		}
		}
	}
	}




