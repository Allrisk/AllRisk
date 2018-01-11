package SupportingClasses;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

//import junit.framework.Assert;






import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.JavascriptExecutor;


public class UIoperartions extends browserLaunching {
	
	 protected String inputValue;
	 protected String outputValue;
	 public WebElement element;
	 public int tablerow;
	 public String carrier_check;
	
//**************************************UI operations***************************************************************************
public databaseOperartions perform(String p,String operation,String objectType,String value,String dbcolumn_name,String dataFlag,databaseOperartions input,databaseOperartions output,String waitingTime) throws SQLException, IOException, InterruptedException
{
	long waitingTimeinseconds=Long.parseLong(waitingTime);
	wait = new WebDriverWait(driver, waitingTimeinseconds);
try
{
	try
	{
 
	if(p.contains("~"))
	{
		p=p.replace("~",Integer.toString(tablerow));
		this.waitWithoutClickable(p, objectType);
	}
	
	else if(p.contains("#"))
	 {
	  p=p.replace("#",Integer.toString(tablerow-1));
	  this.waitWithoutClickable(p, objectType);
	 }
    
	}
	catch(NoSuchElementException e)
	{
		System.out.println("carrier not found");
	}	

switch (operation.toUpperCase())
{
//-------------------------------click operation-------------------------------------------------------------------------------
case "CLICK": 
        this.click(p, objectType);
        break;
 //---------------------------------------SET TEXT-----------------------------------------------------------------------       
 case "SETTEXT":
         inputValue=this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
     	 this.setTextWithEnter(p, objectType,inputValue);
         break;  		 
 //------------------------------------------------GO TO URL--------------------------------------------------------------------------    
 case "GOTOURL":	 
		 inputValue = value;
         this.goToURL(inputValue); 
         break;
 //-------------------------------------------------------GET ATTRIBUTE-------------------------------------------------------------	 
 case "GETATTRIBUTEIN":
	     outputValue=this.getValueByAttribute(p, objectType);
	     //output.write_data(dbcolumn_name, outputValue);
	     //output.update_row();
	     input.write_data(dbcolumn_name, outputValue);
	     input.update_row();
         break;
         //-------------------------------------------------------GET ATTRIBUTE-------------------------------------------------------------	 
   case "GETATTRIBUTEOP":
        outputValue=this.getValueByAttribute(p, objectType);
        output.write_data(dbcolumn_name, outputValue);
        output.update_row();
        break;
 //------------------------------------------------------GET TEXT INPUT----------------------------------------------------------------------                 
 case "GETTEXTIN":
	     outputValue=this.getValueByText(p, objectType);
	     input.write_data(dbcolumn_name, outputValue);
	     input.update_row();
	     //output.write_data(dbcolumn_name, outputValue);
	     //output.update_row();
	     break;
//------------------------------------------------------GET TEXT OUTPUT----------------------------------------------------------------------                 
  case "GETTEXTOP":
   	     outputValue=this.getValueByText(p, objectType);
  	     output.write_data(dbcolumn_name, outputValue);
	     output.update_row();
	     break;
  //----------------------------------------------------SELECT OPERATION------------------------------------------------------------------------- 
 case "SELECT":
	     inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	     this.select(p, objectType, inputValue);
	     break;
//--------------------------------------------------MOUSE HOVER---------------------------------------------------------------------------------- 
 case "MOUSEHOVER": 
	 	 this.mouseHover(p, objectType);
	 	 break;
//-----------------------------------------------AUTO COMPLETE--------------------------------------------------------------------------------------	  
case "AUTOCOMPLETE":
		 inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
		 this.autoComplete(p, objectType, inputValue);
		 break;
//--------------------------------------------------------------------------------------------------------------------------------       	
case "ASSERTTEXT":
	  	 inputValue = value;
	  	 this.assertText(p, objectType, inputValue);
	  	 break; 	   
//------------------------------------------------SCREENSHOT------------------------------------------------------------------   	
case "COMPARETEXT":
 	 	outputValue=this.compareText(p, objectType, inputValue);
 	 	input.write_data(dbcolumn_name, outputValue);
 	 	input.update_row();
 	 	break; 	   
 	 	
case "COMPARETEXTOP":
	 	outputValue=this.compareText(p, objectType, output);
	 	if(outputValue=="Pass")
	 	{
	 		output.write_data(dbcolumn_name, outputValue);
	 	//	output.update_row();
	 	}
	 	//output.write_data(dbcolumn_name, outputValue);
	 	output.update_row();
	 	break; 	
	 	
case "COMPAREPREMIUM":
	 	outputValue=this.comparepremium(p, objectType, inputValue);
	 	input.write_data(dbcolumn_name, outputValue);
	 	input.update_row();
	 	break; 	
//------------------------------------------------SCREENSHOT------------------------------------------------------------------   	
case "SCREENSHOT":
    	 this.takeScreenShot();
    	 break;	   	
 //-----------------------------------------CLICK RADIO BUTTON BY ITS VALUE-------------------------------------------------------------------------------
    	   		
case "RADIOBUTTON":
	     inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	     this.radioButton(p, objectType,inputValue);
		 break;
//------------------------------------------DATE PICKER-----------------------------------------------------------------------------------------
case "DATEPICKER":
		 inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
		 this.DatePickerpopup(p, objectType, inputValue);
         break;
//-------------------------------------------------------------WAIT FOR LOAD--------------------------------------------------------------------
case "DATEPICKER_WITHOUT_ENTER":	
	  inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	  this.datePickerWithoutEnter(p, objectType, inputValue);
      break;       
//---------------------------------------------------------------------------------------------------------------------------------------                
case "WAITLOAD":
		    this.waitLoad(p, objectType);
		    break;
//-------------------------------------------------------CONTINUES OPERATION------------------------------------------------------------------------------	
case "CONTOPERATION":
		inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
		this.contSendkeysOperation(p, objectType, inputValue);
		break;    
//--------------------------------------------------------WAITING FOR IMG INVISIBILITY----------------------------------------------------------------------------     
case "IMGIDVISIBLE":	
			this.waitTillInvisible(p, objectType);
		    break;
//---------------------------------------------------------Set text without enter-------------------------------------------------------------------------------------------	
case "SETTEXT_WITHOUT_ENTER":
		inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
		this.setTextWithoutEnter(p, objectType, inputValue);
		break;
//----------------------------------------------------------Thread sleep------------------------------------------------------------------------------------------------	
case "SETTEXT_THEN_ENTER":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	this.setTexThenEnter(p, objectType, inputValue);
	break;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
case "SETTEXT_THEN_TAB":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	this.setTexThenTab(p, objectType, inputValue);
	break;
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
case "WAIT":
	   Thread.sleep(waitingTimeinseconds);
       break;   
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
case "WAITFORTEXT":	
	element = driver.findElement(this.getObject(p,objectType));
	if(element.isEnabled() && element.isDisplayed())
	{
	String expectedText=element.getText();
	wait.until(ExpectedConditions.textToBePresentInElement(element, expectedText));
	}
    break;   
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
case "DYNAMICDATEPICKER":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	this.dynamicDatePicker(p, objectType, inputValue);
	break;
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
case "SCROLL_DOWN":
	element = driver.findElement(this.getObject(p,objectType));
	element.sendKeys(Keys.PAGE_DOWN);
	break;	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
case "TOGGLEBUTTON":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	this.toggleButton(p, objectType, inputValue);
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "TROW":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	tablerow=this.gettablerow(p, objectType, inputValue);
	break;
//----------------------------------------------------------------------------------------------------------------------------------------------------
case "TROWLIST":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	tablerow=this.gettablerowlist(p, objectType);
	break;
//----------------------------------------------------------------------------------------------------------------------------------------------------
case "RULECOUNT":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	String rlabel=input.read_data("Rule_Label");
	if(rlabel.equals("No Label"))
	{
		tablerow=this.gettablerowlist(p, objectType);
		String rulecount = Integer.toString(tablerow);
		input.write_data("Rule_Count", rulecount);
		input.update_row();
	}
	else
	{
	tablerow=this.getreferralrulecount(p, objectType, input, output);
	String rulecount = Integer.toString(tablerow);
	input.write_data("Rule_Count", rulecount);
	input.update_row();
	}
	break;
//----------------------------------------------------------------------------------------------------------------------------------------------------
case "GETEXPECTEDRESULT":
	String expected=this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "CARRIERCHECK":
	inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	System.out.println("IP Value is:"+inputValue);
	carrier_check=this.carriercheck(p, objectType,input, output, inputValue);
	
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "CHECKPRODUCTDISABLE":
	inputValue=this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	tablerow=this.productdisable(p, objectType);
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "CHECKFORMDISABLE":
	//inputValue=this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	String rulelabel=this.formdisable(p);
	input.write_data("Rule_Label", rulelabel);
	input.update_row();
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "CLEAR": 
    this.clearTextbox(p, objectType);
    break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "JSEXECUTORCHECKBOX":
	element = driver.findElement(this.getObject(p,objectType));
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].click();", element);
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------
case "JSEXECUTORRADIO":
	element = driver.findElement(this.getObject(p,objectType));
	JavascriptExecutor radio = (JavascriptExecutor) driver;
	radio.executeScript("arguments[0].click();", element);
	radio.executeScript("arguments[0].checked=true",element);
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------	
case "JSEXECUTORTEXTBOX":
	inputValue=this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	element = driver.findElement(this.getObject(p,objectType));
	JavascriptExecutor tb = (JavascriptExecutor) driver;
	tb.executeScript("arguments[0].value="+inputValue,element);
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------	
case "JSEXECUTORDROPDOWN":
	inputValue=this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
	element = driver.findElement(this.getObject(p,objectType));
	JavascriptExecutor dd = (JavascriptExecutor) driver;
	dd.executeScript("arguments[0].click();",driver.findElement(this.getObject(p+"/option[@value='"+inputValue+"']",objectType)));
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------		
case "ALERTPRESENCE":
	System.out.println("into ALERTPRESENCE");
	try
	{
	 boolean present = driver.findElement(this.getObject(p,objectType)).isDisplayed();
	 if(driver.findElement(this.getObject(p,objectType)).isDisplayed())
	{
		System.out.println("into if loop");
		element = driver.findElement(this.getObject(p,objectType));
		JavascriptExecutor click = (JavascriptExecutor) driver;
		click.executeScript("arguments[0].click();", element);
	}
	}
	catch(org.openqa.selenium.NoSuchElementException e)
	{
		System.out.println("into Else Loop");
		break;
	}
	break;
//-----------------------------------------------------------------------------------------------------------------------------------------------------		
case "TEXTBOXFILL":
	element = driver.findElement(this.getObject(p,objectType));
	String label=this.getValueByAttribute(p, objectType);
	if(label.equals(""))
	{
		inputValue = this.getInputValue(dataFlag, input, output, value, dbcolumn_name);
		this.setTexThenTab(p, objectType, inputValue);
	}
	/*else
	{
		System.out.println("not blank");
	}*/
    break;
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
case "CLICK_WHEN_NOT_BLANK":
	 element = driver.findElement(this.getObject(p,objectType));
	 String textbox=this.getValueByAttribute(p, objectType);
	 if(textbox.equals(""))
	 {

	 }
	 else
	 {
	  element.sendKeys(Keys.ENTER);
	 }
	    break;  
default:
	    System.out.println("operations not  performed");
	    break;
} 
}
catch(StaleElementReferenceException e)
{
  this.perform(p, operation, objectType, value, dbcolumn_name, dataFlag, input, output, waitingTime);
}
catch(WebDriverException e)
{
  System.out.println(e.getMessage());
  this.perform(p, operation, objectType, value, dbcolumn_name, dataFlag, input, output, waitingTime);
}

return output;
}

//============================================Locator Action====================================================================  
 private By getObject(String p,String objectType)
  {
    switch(objectType.toUpperCase())
    {
    case "XPATH":	
    	return By.xpath(p);
    	
    	
    case "CLASSNAME":

    	return By.className(p);
 
    	
    case "NAME":
        
    	return By.name(p);
    	
    	
    case "CSS":
    	
    	return By.cssSelector(p);
    	
    case "LINK":
    	
    	return By.linkText(p);
    
    	
    case "PARTIALLINK":
    	
    	return By.partialLinkText(p);
    		
    case "ID":
    	
    	return By.id(p);
    	
    case "DYNAMICXPATH":
    	return By.xpath(p);	
    	 	
   default:
	   
	   System.out.println("wrong object type");
	   return null;
	   
    }
	   
    }
 //========================================================Methods of UI oprations=============================================================
    
  public void click(String p,String objectType) throws StaleElementReferenceException
  {  
	  	this.waitWithClickable(p, objectType);
	  	element = driver.findElement(this.getObject(p,objectType));
	  	element.click(); 	
  }
    
   protected void setTextWithEnter(String p,String objectType,String inputValue) throws StaleElementReferenceException
   {
	   	this.waitWithClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	element.clear();
	   	element.sendKeys(Keys.ENTER);
	   	element.sendKeys(inputValue);
	   	element.sendKeys(Keys.ENTER);	
   }
   protected void clearTextbox(String p,String objectType) throws StaleElementReferenceException
   {
	   	this.waitWithClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	element.clear();
   }
   
   protected void setTextWithoutEnter(String p,String objectType,String inputValue) throws StaleElementReferenceException
   {
	   	this.waitWithClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	element.clear();
	   	element.sendKeys(inputValue);
	   	
   }
    
   
   private void setTexThenEnter(String p, String objectType, String inputValue) 
   {
	   this.waitWithClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	element.clear();
	   	element.sendKeys(inputValue);
	   	element.sendKeys(Keys.ENTER);	
		
   }
   private void setTexThenTab(String p, String objectType, String inputValue) 
   {
	   this.waitWithClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	//element.clear();
	   	element.sendKeys(inputValue);
	   	element.sendKeys(Keys.TAB);	
		
   }   
 protected void goToURL(String inputURL )
 {
	 	driver.get(inputValue); 	
 }
 public int productdisable(String p, String objectType) throws InterruptedException 
 {
	    int tr=0;
	    this.waitWithoutClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	if(element.isEnabled())
	   	{
	   		tr=1;
	   	}
	   	else
	   	{
	   		tr=0;
	   	}
		return tr;
 }  
 public String formdisable(String p) throws InterruptedException 
 {
	    String noformlabel;
	    try
	    {
	    	driver.findElement(By.xpath(p));
	    	noformlabel="Label Displayed";
	    }
	   	catch(org.openqa.selenium.NoSuchElementException e)
	    {
	   		noformlabel="No Label";
	    }
	    return noformlabel;
 }  
    
 public String getValueByText(String p,String objectType) throws StaleElementReferenceException
 {
	   	this.waitWithoutClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	String label=element.getText();
	   	return label;
 }
 protected String getValueByAttribute(String p,String objectType) throws StaleElementReferenceException
 {
	 	this.waitWithoutClickable(p, objectType);
	 	element = driver.findElement(this.getObject(p,objectType));
	 	String label=element.getAttribute("value");
	 	return label;
 }
 
 public void select(String p,String objectType,String inputValue) throws StaleElementReferenceException
 {
	    this.waitWithClickable(p, objectType);
	    element = driver.findElement(this.getObject(p,objectType));
	    System.out.println("Element is:"+element);
		Select dropdown = new Select(element);
		dropdown.selectByVisibleText(inputValue);
 }
 
 protected void mouseHover(String p,String objectType) throws StaleElementReferenceException
 {
	    this.waitWithClickable(p, objectType);
	    element = driver.findElement(this.getObject(p,objectType));
	    Actions mouse_hover = new Actions(driver);
		mouse_hover.moveToElement(element).build().perform();
 }
 
 protected void autoComplete(String p,String objectType,String inputValue) throws StaleElementReferenceException
 {
	 	this.waitWithClickable(p, objectType);
	 	element = driver.findElement(this.getObject(p,objectType));
	 	element.sendKeys(inputValue);
	 	element.sendKeys(Keys.DOWN);
	 	element.sendKeys(Keys.ENTER);
 }
 
 
 public void waitWithClickable(String p,String objectType) 
 {
	 	wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p,objectType)));
	 	wait.until(ExpectedConditions.elementToBeClickable(this.getObject(p,objectType)));
	 	wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p,objectType)));

 }
 
 protected void waitWithoutClickable(String p,String objectType)
 {
	    wait.until(ExpectedConditions.presenceOfElementLocated(this.getObject(p,objectType)));
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p,objectType)));
 }
 
protected void radioButton(String p,String objectType,String inputValue) throws StaleElementReferenceException
{
		this.waitWithClickable(p, objectType);
		List<WebElement> RadButtonList =driver.findElements(this.getObject(p,objectType));
			for(int i=0; i< RadButtonList.size() ; i++)
			{
				System.out.println(((WebElement) RadButtonList.get(i)).getAttribute("value"));
			if(((WebElement) RadButtonList.get(i)).getAttribute("value").equals(inputValue))
				
			{
			   ((WebElement) RadButtonList.get(i)).click();	
			}
			}	
}
public int gettablerowlist(String p,String objectType) throws StaleElementReferenceException
{
	int list=0;
	this.waitWithClickable(p, objectType);
	List<WebElement> TableElement =driver.findElements(this.getObject(p,objectType));
    list=TableElement.size();
    return list;	
}

public int gettablerow(String p,String objectType,String inputValue) throws StaleElementReferenceException
{
	int tr=0;
	this.waitWithClickable(p, objectType);
	List<WebElement> TableElement =driver.findElements(this.getObject(p,objectType));
    System.out.println("Table Size is:"+TableElement.size());
    for(int i=0; i< TableElement.size() ; i++)
	{
    	System.out.println(((WebElement) TableElement.get(i)).getText());
    	if((((WebElement) TableElement.get(i)).getText()).equals(inputValue))
    	{
    		tr=i+1;
    		System.out.println("Table Row Fn:"+tr);
    	}
	}
    return tr;

}
public int getreferralrulecount(String p,String objectType, databaseOperartions input, databaseOperartions output) throws StaleElementReferenceException
{
	int count=0;
	this.waitWithClickable(p, objectType);
	element = driver.findElement(this.getObject(p,objectType));
 	String string_temp = element.getText();
 	String string_val=string_temp.substring(18,20);
 	String trimmedstr=string_val.replace(" ","");
 	count=Integer.parseInt(trimmedstr);
	return count;
}

protected void toggleButton(String p,String objectType,String inputValue) throws StaleElementReferenceException
{
		//this.waitWithClickable(p, objectType);
		element = driver.findElement(this.getObject(p+"/div[contains(.,'"+inputValue+"')]/span",objectType));
		wait.until(ExpectedConditions.elementToBeClickable(this.getObject(p,objectType)));
		element.click();
}
 
 protected void datePicker(String p,String objectType,String inputValue) throws StaleElementReferenceException
 {
	 	this.waitWithClickable(p, objectType);
	 	element = driver.findElement(this.getObject(p,objectType));
	 	element.clear();
	 	element.sendKeys(inputValue);
	 	element.sendKeys(Keys.ENTER);
 }
 
 
 private void datePickerWithoutEnter(String p, String objectType, String inputValue2)
 {
	    this.waitWithClickable(p, objectType);
	 	element = driver.findElement(this.getObject(p,objectType));
	 	element.sendKeys(inputValue);
}
 
 protected void contSendkeysOperation(String p,String objectType,String inputValue) 
 {
	 	this.waitWithClickable(p, objectType);
	 	element = driver.findElement(this.getObject(p,objectType));
	 	Actions builder = new Actions(driver);
	 	Actions seriesOfActions = builder.moveToElement(element).click().sendKeys(element, inputValue);
	 	seriesOfActions.perform();
 }
 
 protected void waitTillInvisible(String p,String objectType)
 {
	 try{
	    wait.until(ExpectedConditions.visibilityOfElementLocated(this.getObject(p,objectType)));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(this.getObject(p,objectType)));
	 }
	 catch(TimeoutException e)
	 {
		 
	 }
 }
 protected void waitLoad(String p,String objectType) 
 {
	    this.waitWithClickable(p, objectType);
	    element = driver.findElement(this.getObject(p,objectType));
		element.isDisplayed();
 }
 
 protected  boolean assertText(String p,String objectType,String expectedText)
 {
	   	boolean status = false;
	   	this.waitWithoutClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	String actualText = element.getText();
	   	if(actualText.equals(expectedText))
	   	{
		   status=true;
	   	}
	   	System.out.println(status);
	   	return status;
 }
 protected  String compareText(String p,String objectType,String expectedText)
 {
	   	String status ;//= "Fail";
	   	this.waitWithoutClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	String actualText = element.getText();
	   	if(actualText.equals(expectedText))
	   	{
	   	   
		   status="Pass";
	   	}
	   	else
	   	{
	   		status="Fail";
	   	}
	   	System.out.println(status);
	   	return status;
 }
 
 protected  String compareText(String p,String objectType,databaseOperartions output) throws SQLException
 {
	    String status ;
	    this.waitWithoutClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	String actualText = element.getText();
	   	String expectedText = output.read_data("Expected_Rule");
	   	if(actualText.equals(expectedText))
	   	{
	   	   
		   status="Pass";
	   	}
	   	else
	   	{
	   		status="Fail";
	   	}
	   	System.out.println(status);
	   	return status;
 }
  
 protected  String comparepremium(String p,String objectType,String expectedText)
 {
	   	String status ;//= "Fail";
	   	this.waitWithoutClickable(p, objectType);
	   	element = driver.findElement(this.getObject(p,objectType));
	   	String actualText = element.getText();
	   	float actual = Float.parseFloat(actualText);
		float expected = Float.parseFloat(expectedText);
		float result=actual - expected;
		System.out.println(result);
	   	if(result<0.5)
	   	{
		   status="Pass";
	   	}
	   	else
	   	{
	   		status="Fail";
	   	}
	   	System.out.println(status);
	   	return status;
 }
 
 public String carriercheck(String p, String objectType, databaseOperartions input, databaseOperartions output, String inputValue) throws SQLException
 {
	 System.out.println("Carriercheck IP value:"+inputValue);
	 tablerow=this.gettablerow(p, objectType, inputValue);
	 System.out.println("Table Row is:"+tablerow);
	 String carriereligibility = "Eligible";
		   if(tablerow==0)
		   {
			   if(input.read_data("Rule_Type").equals("Ineligible"))
			   {
			   output.write_data("Status", "Pass");
			   output.update_row();
			   input.write_data("Flag_for_execution", "Completed");
			   input.update_row();
			   
			   }
			   else
			   {
			   output.write_data("Status", "Fail");
			   output.update_row();
			   input.write_data("Flag_for_execution", "Ineligible");
			   input.update_row();
			   }
			   carriereligibility="Ineligible";
		   }
		return carriereligibility;   
 }
  
 protected void takeScreenShot() throws SQLException, IOException
 {
	 	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	 	FileUtils.copyFile(scrFile, new File("D:\\Exception\\screenshots\\"+".png"));	
   	 
 }
 protected void DatePickerpopup(String p,String objectType,String inputValue) throws InterruptedException
 {
  int totElements=inputValue.length();
  String[] value1=inputValue.split("/");
  for(int i=2;i>=0;--i)
  {
   if(i==2)
   {
     this.waitWithClickable(p+"/div/div/select[2]", objectType);
     element = driver.findElement(this.getObject(p+"/div/div/select[2]",objectType));
     Select dropdown = new Select(element);
     dropdown.selectByVisibleText(value1[i]);
   }
  
   if(i==1)
   {
    this.waitWithClickable(p+"/div/div/select[1]", objectType);
     switch (value1[i]) 
           {
               case "01":  value1[i] = "Jan";          break;
               case "02":  value1[i] = "Feb";        break;
               case "03":  value1[i] = "Mar";        break;
               case "04":  value1[i] = "Apr";          break;
               case "05":  value1[i] = "May";          break;
               case "06":  value1[i] = "Jun";          break;
               case "07":  value1[i] = "Jul";          break;
               case "08":  value1[i] = "Aug";          break;
               case "09":  value1[i] = "Sep";          break;
               case "10":  value1[i] = "Oct";          break;
               case "11":  value1[i] = "Nov";          break;
               case "12":  value1[i] = "Dec";          break;
               default:  value1[i] = "Invalid month"; break;
           }
     element = driver.findElement(this.getObject(p+"/div/div/select[1]",objectType));
     Select dropdown = new Select(element);
     dropdown.selectByVisibleText(value1[i]); 
  }
   if(i==0)
   {
    int day=Integer.parseInt(value1[i]);
     this.waitWithClickable(p+"/div/div/select[2]", objectType);
     element = driver.findElement(this.getObject(p+"/div/div/select[2]",objectType));
     this.click(p+"/table/tbody/tr/td/a[contains(text(),'"+day+"')]", objectType);
    
   }
   }
  
 }
 
 
 protected void dynamicDatePicker(String p,String objectType,String value) throws InterruptedException
 {
	 String[] objtype=objectType.split(";");
		String[] property=p.split(";");
		String[] value1=value.split("/");
		int totElements=objtype.length;
		for(int i=0;i<totElements;i++)
		{
			p=property[i];
			//if(objtype[i].equalsIgnoreCase("DYNAMICXPATH"))
			//{ 
				if(i==1)
				{
					p=property[i].replace("!",value1[2]);
				}
				if(i==2)
				{
			        switch (value1[0]) 
			        {
			            case "01":  value1[0] = "Jan";	         break;
			            case "02":  value1[0] = "Feb";  	     break;
			            case "03":  value1[0] = "Mar";      	 break;
			            case "04":  value1[0] = "Apr";        	 break;
			            case "05":  value1[0] = "May";        	 break;
			            case "06":  value1[0] = "Jun";        	 break;
			            case "07":  value1[0] = "Jul";        	 break;
			            case "08":  value1[0] = "Aug";        	 break;
			            case "09":  value1[0] = "Sep";        	 break;
			            case "10": 	value1[0] = "Oct";        	 break;
			            case "11": 	value1[0] = "Nov";        	 break;
			            case "12": 	value1[0] = "Dec";        	 break;
			            default: 	value1[0] = "Invalid month"; break;
			        }
			        p=property[i].replace("!",value1[0]);
				}
				if(i==3)
				{
					p=property[i].replace("!",value1[1]);
				}
			//}
			
			driver.findElement(this.getObject(p,objtype[i])).click();
			Thread.sleep(1000);
		}
 }
 
 
 
 protected String getInputValue(String dataFlag,databaseOperartions input,databaseOperartions output,String value,String dbcolumn_name) throws SQLException
 {
	 switch(dataFlag)
	 	{
	 		case "Read":
	 			inputValue = input.read_data(dbcolumn_name);
	 			
	 			break;
	 			
	 		case "Readop":
	 			inputValue = output.read_data(dbcolumn_name);
	 			
	 			break;
    		
	 		case "Default":	
	 			inputValue = value;
	 			
	 			break;
	 	}
	 	return inputValue;
 }


/*public void launch_browser(String browser, propertiesHandle configFile) {
	// TODO Auto-generated method stub
	
}*/
}

