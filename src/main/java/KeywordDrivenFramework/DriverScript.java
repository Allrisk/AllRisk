package KeywordDrivenFramework;


//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.support.events.EventFiringWebDriver;

import SupportingClasses.TheEventListener;
import SupportingClasses.propertiesHandle;
import SupportingClasses.ConditionsChecking;
import SupportingClasses.UIoperartions;
import SupportingClasses.databaseOperartions;
import SupportingClasses.ExcelOperationsJXL;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.Math;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

public class DriverScript
{
	//***************************global objects and variables****************************************	
	protected String dbColumnNmae=null;
	protected static UIoperartions objectUIoperations=null;
	protected ExcelOperationsJXL objectTestScript=null;
	protected ExcelOperationsJXL objectLoginScript=null;
	protected ExcelOperationsJXL objectExclusion=null;
	protected ExcelOperationsJXL objectCarrierTestScript=null;
	protected ExcelOperationsJXL objectCSPTestScript=null;
	protected ExcelOperationsJXL executeQSTestScript=null;
	protected ExcelOperationsJXL executeUWTestScript=null;
	protected ExcelOperationsJXL comparision=null;
	protected propertiesHandle configFile;
	protected ConditionsChecking objectconditions=null;
	protected static TheEventListener event;
	protected static Statement statement=null;
	protected static ResultSet rs=null;
	public String Form_Flag = "True";
	public String Rule_Flag = "True";
	
	//DriverScript objDriver=new DriverScript(configFile);
	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException, InterruptedException, TimeoutException
	{
		databaseOperartions objectInput = new databaseOperartions();
		databaseOperartions objectOutput = new databaseOperartions();
		databaseOperartions objectActualResult = new databaseOperartions();
		databaseOperartions objectExpectedResult = new databaseOperartions();
		
		event=new TheEventListener();
		propertiesHandle configFile = new propertiesHandle("D:/QA Selenium/configuration_file/PL_NEWUI_OneLondon.properties");
		databaseOperartions.conn_setup(configFile);
		System.setProperty("jsse.enableSNIExtension", "false");	
		DriverScript objDriver=new DriverScript(configFile);
		objDriver.launchBrowser();
		boolean loginStatus=true;
		objectInput.get_dataobjects(configFile.getProperty("input_query"));
		objectOutput.get_dataobjects(configFile.getProperty("output_query"));
		String CSP_form_actual=configFile.getProperty("CSP_formactual");
		String CSP_form_expected=configFile.getProperty("CSP_formexpected");
		//String QS_form_actual=configFile.getProperty("QS_formactual");
		//String QS_form_expected=configFile.getProperty("QS_formexpected");
		//String ref_rule_actual=configFile.getProperty("Ref_ruleactual");
		//String ref_rule_expected=configFile.getProperty("Ref_ruleexpected");
		//objectActualResult.get_dataobjects(configFile.getProperty("actualresulttable"));
		//objectExpectedResult.get_dataobjects(configFile.getProperty("expectedresulttable"));		
		do 
		{
		  if(loginStatus)
		  {
			 objDriver.login(objectInput, objectOutput);
			 loginStatus=false;
		   }
		 String testdata=objectInput.read_data("test_case_id");
		 System.out.println(testdata);
		 event.testData(testdata);
		  if(objectInput.read_data("flag_for_execution").equals(configFile.getProperty("flagForExecution")))
			{  
			   //objDriver.exclusion(objectActualResult,objectExpectedResult,objectInput,testdata,objectOutput,objDriver);
			   objDriver.executeTestScript(objectInput, objectOutput);
			   //objDriver.exclusion(objectActualResult,objectExpectedResult,objectInput,testdata,objectOutput,objDriver);
			   String checkelibility=objDriver.check_eligibility(objectInput, objectOutput);
			   if(checkelibility.equals("Eligible"))
			   {
				   objDriver.executeCarrierTestScript(objectInput, objectOutput);
				   System.out.println("Carrier Status :"+objectUIoperations.carrier_check);
				   if(objectUIoperations.carrier_check.equals("Eligible"))
				   {
				   objDriver.executeCSPTestScript(objectInput, objectOutput);
				   //objDriver.Forms_list(objectActualResult,objectExpectedResult,testdata,CSP_form_actual,CSP_form_expected,"id('CarrierSelectionFormPage:carrierTile4:ScheduleOfFormsDatatable_data')");
				   //objDriver.Formcomparision(objectActualResult,objectExpectedResult,CSP_form_actual,CSP_form_expected,objectOutput,objectInput,testdata);
				   objDriver.executeQSTestScript(objectInput, objectOutput);
				   Thread.sleep(10000);
				   //objDriver.rule_validate(objectActualResult,objectExpectedResult,objectInput, objectOutput,testdata,ref_rule_actual,ref_rule_expected);
				   objDriver.executeUWTestScript(objectInput, objectOutput);
				   //objDriver.Forms_list(objectActualResult,objectExpectedResult,testdata,QS_form_actual,QS_form_expected,"id('ScheduleOfFormTile:ScheduleOfFormscommonformDatatable_data')");
				   objectInput.write_data("Status", "Pass");
				   objectInput.write_data("Flag_for_execution", "Completed");
				   objDriver.comparision(objectInput, objectOutput, objDriver);
				   }
			   }
			   objectUIoperations.perform("//*//tr/td/a[contains(text(),'Home')]","CLICK", "xpath", "", "", "", objectInput, objectOutput, "10");
			}
		  objectInput.update_row();
		  objectOutput.update_row();
		 
		  
		}while(objectInput.move_forward() && objectOutput.move_forward());
		databaseOperartions.close_conn();
		//objDriver.closeBrowser();
}
	
	//================================================================================================
	
	public DriverScript()
	{
		
	}
	
//===========constructor to initialize objects======================================================================================================================
public DriverScript(propertiesHandle configFile) throws SQLException, ClassNotFoundException
{
	this.configFile = configFile;
	objectUIoperations=new UIoperartions();
	objectconditions=new ConditionsChecking();
	
	objectLoginScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	objectLoginScript.getsheets(this.configFile.getProperty("Login"));
	

	objectTestScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	objectTestScript.getsheets(this.configFile.getProperty("ScriptSheetName"));
	
	objectCarrierTestScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	objectCarrierTestScript.getsheets(this.configFile.getProperty("CC"));
	
	//objectExclusion = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	//objectExclusion.getsheets(this.configFile.getProperty("Exclusion"));
	
	objectCSPTestScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	objectCSPTestScript.getsheets(this.configFile.getProperty("CSP"));
	
	executeQSTestScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	executeQSTestScript.getsheets(this.configFile.getProperty("QS"));
	
	executeUWTestScript = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	executeUWTestScript.getsheets(this.configFile.getProperty("UW"));
	
	comparision = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
	comparision.getsheets(this.configFile.getProperty("status"));
	

}

//====================================Function to exclusion table====================================================================================================
public void exclusion(databaseOperartions objectActualResult,databaseOperartions objectExpectedResult,databaseOperartions objectInput,String testdata,databaseOperartions objectOutput,DriverScript objDriver) throws SQLException, IOException, InterruptedException
{
		 for(int i=1;i<=35;i++)
		 {
		 objDriver.executeExclusionScript(objectInput,objectOutput);
		 String slip_name=objectUIoperations.getValueByText("id('CarrierSelectionFormPage:carrierTile4:CarrierNameRepeat:0:SlipPanel')","xpath");
		 String wind_deductible=objectUIoperations.getValueByText("id('CarrierSelectionFormPage:carrierTile4:WindHaildedAmtRepeat:0:WindHaildedAmtpanl')","xpath");
		 String insert_query=("Insert into 2R2_Exclusion_Table_Actual_Result(test_case_id,wind_deductible_default,actual_carrier)values('"+testdata+"','"+wind_deductible+"','"+slip_name+"')");
		 objectActualResult.insert_row(insert_query);
	     }
	 
	 //String update_queryactual=("UPDATE 2R2_Exclusion_Table_Actual_Result INNER JOIN (SELECT 3R2_Exclusion_Table_Expected_Result.test_case_id,expected_carrier,actual_carrier,IF(expected_carrier=actual_carrier,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_carrier=actual_carrier,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM 3R2_Exclusion_Table_Expected_Result LEFT JOIN 2R2_Exclusion_Table_Actual_Result ON 3R2_Exclusion_Table_Expected_Result.test_case_id=2R2_Exclusion_Table_Actual_Result.test_case_id and expected_carrier=actual_carrier WHERE 3R2_Exclusion_Table_Expected_Result.test_case_id='"+ testdata +"' union SELECT 2R2_Exclusion_Table_Actual_Result.test_case_id,expected_carrier,actual_carrier,IF(expected_carrier=actual_carrier,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_carrier=actual_carrier,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM 2R2_Exclusion_Table_Actual_Result LEFT JOIN 3R2_Exclusion_Table_Expected_Result ON 3R2_Exclusion_Table_Expected_Result.test_case_id=2R2_Exclusion_Table_Actual_Result.test_case_id and expected_carrier=actual_carrier WHERE 2R2_Exclusion_Table_Actual_Result.test_case_id= '"+testdata+"')TEMP ON 2R2_Exclusion_Table_Actual_Result.test_case_id=TEMP.test_case_id AND 2R2_Exclusion_Table_Actual_Result.actual_carrier=TEMP.actual_carrier SET 2R2_Exclusion_Table_Actual_Result.`STATUS`= ACTUAL_RULE_STATUS");
	 //objectActualResult.insert_row(update_queryactual);
	 //String update_queryexpected=("UPDATE 3R2_Exclusion_Table_Expected_Result INNER JOIN (SELECT 3R2_Exclusion_Table_Expected_Result.test_case_id,expected_carrier,actual_carrier,IF(expected_carrier=actual_carrier,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_carrier=actual_carrier,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM 3R2_Exclusion_Table_Expected_Result LEFT JOIN 2R2_Exclusion_Table_Actual_Result ON 3R2_Exclusion_Table_Expected_Result.test_case_id=2R2_Exclusion_Table_Actual_Result.test_case_id AND expected_carrier=actual_carrier WHERE 3R2_Exclusion_Table_Expected_Result.test_case_id='"+testdata+"' UNION SELECT 2R2_Exclusion_Table_Actual_Result.test_case_id,expected_carrier,actual_carrier,IF(expected_carrier=actual_carrier,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_carrier=actual_carrier,'Pass','Fail') AS ACTUAL_RULE_STATUS from 2R2_Exclusion_Table_Actual_Result LEFT JOIN 3R2_Exclusion_Table_Expected_Result on 3R2_Exclusion_Table_Expected_Result.test_case_id=2R2_Exclusion_Table_Actual_Result.test_case_id AND expected_carrier=actual_carrier WHERE 2R2_Exclusion_Table_Actual_Result.test_case_id='"+testdata+"')TEMP ON 3R2_Exclusion_Table_Expected_Result.test_case_id=TEMP.test_case_id AND 3R2_Exclusion_Table_Expected_Result.expected_carrier=TEMP.expected_carrier SET 3R2_Exclusion_Table_Expected_Result.`status`=EXPECTED_RULE_STATUS");
	 //objectExpectedResult.insert_row(update_queryexpected);
	 objectUIoperations.perform("//*//tr/td/a[contains(text(),'Home')]","CLICK", "xpath", "", "", "", objectInput, objectOutput, "10");
}

//====================================Function to launch browser====================================================================================================
public void launchBrowser()
{
	    String browser = configFile.getProperty("browser");
		//String url = configFile.getProperty("url");
		objectUIoperations.launch_browser(browser,configFile);
			
}

//==============================================Function to login===================================================================================================
protected void login(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
  {
	  objectLoginScript.set_rownumber(1);
	  while(objectLoginScript.has_next_row())
		{
			if(objectLoginScript.read_data(objectLoginScript.get_rownumber(),8).toString().equals("enabled"))
			{
				//String fieldName = objectTestScript.read_data(objectTestScript.get_rownumber(),1);
				String actionKeyword = objectLoginScript.read_data(objectLoginScript.get_rownumber(),2);
				String ObjectType = objectLoginScript.read_data(objectLoginScript.get_rownumber(),3);
				String PropertyString= objectLoginScript.read_data(objectLoginScript.get_rownumber(),4);
				String dbcolumnNmae = objectLoginScript.read_data(objectLoginScript.get_rownumber(),5);
				String value = objectLoginScript.read_data(objectLoginScript.get_rownumber(),6);
				String dataProvidingFlag=objectLoginScript.read_data(objectLoginScript.get_rownumber(),9);
				String waitingTime=objectLoginScript.read_data(objectLoginScript.get_rownumber(),10);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
				
			}
			objectLoginScript.next_row();
		}	  
  }
 //=============================================Function to run the test script========================================================================================  
protected void executeTestScript(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	objectTestScript.set_rownumber(1);
	while(objectTestScript.has_next_row())
	{	
		String conditions=objectTestScript.read_data(objectTestScript.get_rownumber(),7);
		if(objectTestScript.read_data(objectTestScript.get_rownumber(),8).toString().equals("enabled")&& objectconditions.condition_reading(conditions, objectInput))
		{	
				//String pageName = objectTestScript.read_data(objectTestScript.get_rownumber(),0);
				//String fieldName = objectTestScript.read_data(objectTestScript.get_rownumber(),1);
				String actionKeyword = objectTestScript.read_data(objectTestScript.get_rownumber(),2);
				String ObjectType = objectTestScript.read_data(objectTestScript.get_rownumber(),3);
				String PropertyString= objectTestScript.read_data(objectTestScript.get_rownumber(),4);
				String dbcolumnNmae = objectTestScript.read_data(objectTestScript.get_rownumber(),5);
				String value = objectTestScript.read_data(objectTestScript.get_rownumber(),6);
			    //String condtions1=objectTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=objectTestScript.read_data(objectTestScript.get_rownumber(),9);
				String  waitingTime=objectTestScript.read_data(objectTestScript.get_rownumber(),10);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
				
		}
		objectTestScript.next_row();
	}
	
}
//=============================================Function to run the test script========================================================================================  
protected void executeCSPTestScript(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	objectCSPTestScript.set_rownumber(1);
	while(objectCSPTestScript.has_next_row())
	{	
		String conditions=objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),7);
		String carrierconditions=objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),8);
		if(objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),9).toString().equals("enabled")&& objectconditions.condition_reading(conditions, objectInput)&& objectconditions.condition_reading(carrierconditions, objectInput))
		{	
				//String pageName = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),0);
				//String fieldName = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),1);
				String actionKeyword = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),2);
				String ObjectType = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),3);
				String PropertyString= objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),4);
				String dbcolumnNmae = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),5);
				String value = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),6);
			    //String condtions1=objectCSPTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),10);
				String  waitingTime=objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),11);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
				
		}
		objectCSPTestScript.next_row();
	}
	
}
//=============================================Function to run the test script========================================================================================  
protected void executeCarrierTestScript(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	objectCarrierTestScript.set_rownumber(1);
	while(objectCarrierTestScript.has_next_row())
	{	
		String conditions=objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),7);
		if(objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),8).toString().equals("enabled")&& objectconditions.condition_reading(conditions, objectInput))
		{	
				//String pageName = objectTestScript.read_data(objectTestScript.get_rownumber(),0);
				//String fieldName = objectTestScript.read_data(objectTestScript.get_rownumber(),1);
				String actionKeyword = objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),2);
				String ObjectType = objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),3);
				String PropertyString= objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),4);
				String dbcolumnNmae = objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),5);
				String value = objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),6);
			    //String condtions1=objectTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),9);
				String  waitingTime=objectCarrierTestScript.read_data(objectCarrierTestScript.get_rownumber(),10);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
				
		}
		objectCarrierTestScript.next_row();
	}
	
}
//=============================================Function to run the test script========================================================================================  
protected void executeQSTestScript(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	executeQSTestScript.set_rownumber(1);
	while(executeQSTestScript.has_next_row())
	{	
		String conditions=executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),7);
		String carrierconditions=executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),8);
		if(executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),9).toString().equals("enabled")&& objectconditions.condition_reading(conditions, objectInput)&& objectconditions.condition_reading(carrierconditions, objectInput))
		{	
				//String pageName = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),0);
				//String fieldName = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),1);
				String actionKeyword = executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),2);
				String ObjectType = executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),3);
				String PropertyString= executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),4);
				String dbcolumnNmae = executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),5);
				String value = executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),6);
			    //String condtions1=objectCSPTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),10);
				String  waitingTime=executeQSTestScript.read_data(executeQSTestScript.get_rownumber(),11);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
				
		}
		executeQSTestScript.next_row();
	}
	
}

//=============================================Function to run the test script========================================================================================  
protected void executeUWTestScript(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	executeUWTestScript.set_rownumber(1);
	while(executeUWTestScript.has_next_row())
	{	
		String conditions=executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),7);
		String carrierconditions=executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),8);
		if(executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),9).toString().equals("enabled")&& objectconditions.condition_reading(conditions, objectInput)&& objectconditions.condition_reading(carrierconditions, objectInput))
		{	
				//String pageName = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),0);
				//String fieldName = objectCSPTestScript.read_data(objectCSPTestScript.get_rownumber(),1);
				String actionKeyword = executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),2);
				String ObjectType = executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),3);
				String PropertyString= executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),4);
				String dbcolumnNmae = executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),5);
				String value = executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),6);
			    //String condtions1=objectCSPTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),10);
				String  waitingTime=executeUWTestScript.read_data(executeUWTestScript.get_rownumber(),11);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
				
		}
		executeUWTestScript.next_row();
	}
	
}

//=============================================Function to run the test script exclusion table========================================================================================  
protected void executeExclusionScript(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	objectExclusion.set_rownumber(1);
	while(objectExclusion.has_next_row())
	{
		String conditions=objectExclusion.read_data(objectExclusion.get_rownumber(),7);
		
		if(objectExclusion.read_data(objectExclusion.get_rownumber(),8).toString().equals("enabled")&& objectconditions.condition_reading(conditions, objectInput))
		{	
				//String pageName = objectTestScript.read_data(objectTestScript.get_rownumber(),0);
				//String fieldName = objectTestScript.read_data(objectTestScript.get_rownumber(),1);
				String actionKeyword = objectExclusion.read_data(objectExclusion.get_rownumber(),2);
				String ObjectType = objectExclusion.read_data(objectExclusion.get_rownumber(),3);
				String PropertyString= objectExclusion.read_data(objectExclusion.get_rownumber(),4);
				String dbcolumnNmae = objectExclusion.read_data(objectExclusion.get_rownumber(),5);
				String value = objectExclusion.read_data(objectExclusion.get_rownumber(),6);
			    //String condtions1=objectTestScript.read_data(objectTestScript.get_rownumber(),7);
				String dataProvidingFlag=objectExclusion.read_data(objectExclusion.get_rownumber(),9);
				String  waitingTime=objectExclusion.read_data(objectExclusion.get_rownumber(),10);
				objectUIoperations.perform(PropertyString,actionKeyword,ObjectType,value,dbcolumnNmae,dataProvidingFlag,objectInput,objectOutput,waitingTime);
		}
		objectExclusion.next_row();
	}
	
}
//====================================Function to eligibility====================================================================================================
public String check_eligibility(databaseOperartions objectInput,databaseOperartions objectOutput) throws SQLException, IOException, InterruptedException
{
	String eligibility;
	String no_form=objectUIoperations.formdisable("//label[contains(text(),'No form available')]");
	System.out.println("Form :"+no_form);
	String rule_type=objectInput.read_data("Rule_Type");
	if(no_form.equals("Label Displayed"))
	 {
		if(rule_type.equals("Ineligible"))
		{
		objectInput.write_data("Flag_for_execution", "Completed");	
		objectOutput.write_data("Status", "Pass");
		}
		else
		{
		objectInput.write_data("Flag_for_execution", "Ineligible");
		objectOutput.write_data("Status", "Fail");	
		}
		eligibility="InEligible";
	 }
	else
	{
		eligibility="Eligible";
	}
	
	return eligibility;
}
//====================================Function to get forms list====================================================================================================
public void Forms_list(databaseOperartions objectActualResult,databaseOperartions objectExpectedResult,String testdata,String form_actual,String form_expected,String object_form) throws SQLException, IOException, InterruptedException
{
	
	 int form_count=objectUIoperations.gettablerowlist(object_form+"/tr/td[3]/div//a", "xpath");
	 for(int i=1;i<=form_count;i++)
	 {
	 String form_temp=objectUIoperations.getValueByText(object_form+"/tr["+i+"]/td[3]/div","xpath");
	 String form=form_temp.replace("'","");
	 String insert_query=("Insert into "+form_actual+"(test_case_id,actual_form)values('"+testdata+"','"+form+"')");
	 objectActualResult.insert_row(insert_query);
     }
	
	 String update_queryactual=("UPDATE "+form_actual+" INNER JOIN (SELECT "+form_expected+".test_case_id,expected_form,actual_form,IF(expected_form=actual_form,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_form=actual_form,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM "+form_expected+" LEFT JOIN "+form_actual+" ON "+form_expected+".test_case_id="+form_actual+".test_case_id and expected_form=actual_form WHERE "+form_expected+".test_case_id='"+ testdata +"' union SELECT "+form_actual+".test_case_id,expected_form,actual_form,IF(expected_form=actual_form,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_form=actual_form,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM "+form_actual+" LEFT JOIN "+form_expected+" ON "+form_expected+".test_case_id="+form_actual+".test_case_id and expected_form=actual_form WHERE "+form_actual+".test_case_id= '"+testdata+"')TEMP ON "+form_actual+".test_case_id=TEMP.test_case_id AND "+form_actual+".actual_form=TEMP.actual_form SET "+form_actual+".`STATUS`= ACTUAL_RULE_STATUS");
	 objectActualResult.insert_row(update_queryactual);
	 String update_queryexpected=("UPDATE "+form_expected+" INNER JOIN (SELECT "+form_expected+".test_case_id,expected_form,actual_form,IF(expected_form=actual_form,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_form=actual_form,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM "+form_expected+" LEFT JOIN "+form_actual+" ON "+form_expected+".test_case_id="+form_actual+".test_case_id AND expected_form=actual_form WHERE "+form_expected+".test_case_id='"+testdata+"' UNION SELECT "+form_actual+".test_case_id,expected_form,actual_form,IF(expected_form=actual_form,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(expected_form=actual_form,'Pass','Fail') AS ACTUAL_RULE_STATUS from "+form_actual+" LEFT JOIN "+form_expected+" on "+form_expected+".test_case_id="+form_actual+".test_case_id AND expected_form=actual_form WHERE "+form_actual+".test_case_id='"+testdata+"')TEMP ON "+form_expected+".test_case_id=TEMP.test_case_id AND "+form_expected+".expected_form=TEMP.expected_form SET "+form_expected+".`status`=EXPECTED_RULE_STATUS");
	 objectExpectedResult.insert_row(update_queryexpected);
	 objectUIoperations.click("*//span[contains(text(),'SCHEDULE OF FORMS')]/../a","xpath");
	 //Formcomparision( objectActualResult, objectExpectedResult, form_actual, form_expected, objectOutput, objectInput,  testdata);
	 //objectUIoperations.click("//*//tr/td/a[contains(text(),'Home')]", "xpath");
	 
}
//====================================Function to compare Forms====================================================================================================
public void Formcomparision(databaseOperartions objectActualResult,databaseOperartions objectExpectedResult,String CSP_form_actual,String CSP_form_expected,databaseOperartions objectOutput,databaseOperartions objectInput, String testdata) throws SQLException
{
    /*String Actualvalue = "";
    String Expectedvalue = "";    */
    String Actual_query=("SELECT actual_form FROM `" + CSP_form_actual + "` WHERE "  +  CSP_form_actual + ".status='Fail' AND "  +  CSP_form_actual + ".test_case_id='" + testdata + "'");
    String Expected_query= ("SELECT expected_form FROM `" + CSP_form_expected +"` WHERE "  +  CSP_form_expected + ".status='Fail' AND "  +  CSP_form_expected + ".test_case_id='" + testdata + "'");
    //Boolean firstResultset = true;
    objectActualResult.get_dataobjects(Actual_query);
    objectExpectedResult.get_dataobjects(Expected_query);
    
    Integer Actual_rscount = objectActualResult.Resultset_rowcount("actual_form");
    Integer Expected_rscount  = objectExpectedResult.Resultset_rowcount("expected_form");
    if(Actual_rscount == 0 && Expected_rscount == 0)
        objectOutput.write_data("Form_Status","Pass");
    else
        {
        Form_Flag  = "False";
        objectOutput.write_data("Form_Status","Fail");
        }
}
//==================================Function to validate referral rule========================================================================================================
public void rule_validate(databaseOperartions objectActualResult,databaseOperartions objectExpectedResult,databaseOperartions objectInput,databaseOperartions objectOutput, String testdata,String rule_actual,String rule_expected) throws InterruptedException, SQLException, IOException
{
	int rule_count = Integer.parseInt(objectInput.read_data("Rule_Count"));
	int mod = rule_count%3;
	int div = rule_count/3;
	int pg_no;
	if(mod!=0)
	{
		pg_no=div+1;
	}
	else
	{
		pg_no=div;
	}
	if(rule_count>3)
	{
		for(int i=1;i<=pg_no;i++)
		{
			objectUIoperations.click("id('BusinessRuleTile:BusinessRuleform:HamburgerBusinssRuleDatatable_paginator_bottom')/span[3]/span["+i+"]","xpath");
			int row = objectUIoperations.gettablerowlist("id('BusinessRuleTile:BusinessRuleform:HamburgerBusinssRuleDatatable_data')/tr","xpath");
			for(int k=1;k<=row;k++)
			{
				Thread.sleep(10000);
				String rule = objectUIoperations.getValueByText("id('BusinessRuleTile:BusinessRuleform:HamburgerBusinssRuleDatatable')/div/table/tbody/tr["+k+"]/td[2]/label","xpath");
				String insert_query = ("Insert into "+rule_actual+"(Test_Case_ID,Actual_Rule)values('"+testdata+"','"+rule+"')");
				objectActualResult.insert_row(insert_query);
			}
		}
	}
	else
	{
		int row = objectUIoperations.gettablerowlist("id('BusinessRuleTile:BusinessRuleform:HamburgerBusinssRuleDatatable_data')/tr","xpath");
		for(int k=1;k<=row;k++)
		{
			String rule = objectUIoperations.getValueByText("id('BusinessRuleTile:BusinessRuleform:HamburgerBusinssRuleDatatable')/div/table/tbody/tr["+k+"]/td[2]/label","xpath");
			String insert_query = ("Insert into "+rule_actual+"(Test_Case_ID,Actual_Rule)values('"+testdata+"','"+rule+"')");
			objectActualResult.insert_row(insert_query);
		}
	}
	String update_queryactual=("UPDATE "+rule_actual+" INNER JOIN (SELECT "+rule_expected+".Test_Case_ID,Expected_Rule,Actual_Rule,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM "+rule_expected+" LEFT JOIN "+rule_actual+" ON "+rule_expected+".Test_Case_ID="+rule_actual+".Test_Case_ID and Expected_Rule=Actual_Rule WHERE "+rule_expected+".Test_Case_ID='"+ testdata +"' union SELECT "+rule_actual+".Test_Case_ID,Expected_Rule,Actual_Rule,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM "+rule_actual+" LEFT JOIN "+rule_expected+" ON "+rule_expected+".Test_Case_ID="+rule_actual+".Test_Case_ID and Expected_Rule=Actual_Rule WHERE "+rule_actual+".Test_Case_ID= '"+testdata+"')TEMP ON "+rule_actual+".Test_Case_ID=TEMP.Test_Case_ID AND "+rule_actual+".Actual_Rule=TEMP.Actual_Rule SET "+rule_actual+".`STATUS`= ACTUAL_RULE_STATUS");
	objectActualResult.insert_row(update_queryactual);
	String update_queryexpected=("UPDATE "+rule_expected+" INNER JOIN (SELECT "+rule_expected+".Test_Case_ID,Expected_Rule,Actual_Rule,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS ACTUAL_RULE_STATUS FROM "+rule_expected+" LEFT JOIN "+rule_actual+" ON "+rule_expected+".Test_Case_ID="+rule_actual+".Test_Case_ID AND Expected_Rule=Actual_Rule WHERE "+rule_expected+".Test_Case_ID='"+testdata+"' UNION SELECT "+rule_actual+".Test_Case_ID,Expected_Rule,Actual_Rule,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS EXPECTED_RULE_STATUS,IF(Expected_Rule=Actual_Rule,'Pass','Fail') AS ACTUAL_RULE_STATUS from "+rule_actual+" LEFT JOIN "+rule_expected+" on "+rule_expected+".Test_Case_ID="+rule_actual+".Test_Case_ID AND Expected_Rule=Actual_Rule WHERE "+rule_actual+".Test_Case_ID='"+testdata+"')TEMP ON "+rule_expected+".Test_Case_ID=TEMP.Test_Case_ID AND "+rule_expected+".Expected_Rule=TEMP.Expected_Rule SET "+rule_expected+".`status`=EXPECTED_RULE_STATUS");
	objectExpectedResult.insert_row(update_queryexpected);
	String Actual_query=("SELECT Actual_Rule FROM `" + rule_actual + "` WHERE "  +  rule_actual + ".status='Fail' AND "  +  rule_actual + ".test_case_id='" + testdata + "'");
    String Expected_query= ("SELECT Expected_Rule FROM `" + rule_expected +"` WHERE "  +  rule_expected + ".status='Fail' AND "  +  rule_expected + ".test_case_id='" + testdata + "'");
    objectActualResult.get_dataobjects(Actual_query);
    objectExpectedResult.get_dataobjects(Expected_query);
    
    Integer Actual_rscount = objectActualResult.Resultset_rowcount("Actual_Rule");
    Integer Expected_rscount  = objectExpectedResult.Resultset_rowcount("Expected_Rule");
    if(Actual_rscount == 0 && Expected_rscount == 0)
        objectOutput.write_data("Rule_Status","Pass");
    else
        {
        Rule_Flag  = "False";
        objectOutput.write_data("Rule_Status","Fail");
        }
}
//==================================Function to close the browser========================================================================================================
public void closeBrowser()
{
	objectUIoperations.stop_browser();
}

public void comparision(databaseOperartions objectInput,databaseOperartions objectOutput, DriverScript objDriver) throws InterruptedException, SQLException, IOException
{
 String flag="True";	
 comparision.set_rownumber(1);
 while(comparision.has_next_row())
{
	float Actual;
	float Expected;
	String Status;
	String dbcolumnNmae_Actual = comparision.read_data(comparision.get_rownumber(),1);
	String dbcolumnNmae_Expected = comparision.read_data(comparision.get_rownumber(),2);
	String Type = comparision.read_data(comparision.get_rownumber(),4);
	String actualData = objectOutput.read_data(dbcolumnNmae_Actual);
	String expectedData = objectOutput.read_data(dbcolumnNmae_Expected);
	String dbcolumnNmae_Status= comparision.read_data(comparision.get_rownumber(),3);

		if(actualData==null || actualData.equals("") || actualData.equals(" "))
		{
			actualData="0";
		}
		if(expectedData==null || expectedData.equals("") || expectedData.equals(" "))
		{
			expectedData="0";
		}
				if(Type.equals("premiumfloat"))
				{
			      String removedollar_Actual=actualData.replace("$","").replace(",","").replace("(","-").replace(")","");
			      String removedollar_Expected=expectedData.replace("$","").replace(",","").replace("(","-").replace(")","");	
			      Actual = Float.parseFloat(removedollar_Actual);
			      Expected = Float.parseFloat(removedollar_Expected);
				}
				/*else if(Type.equals("String"))
				{
					Actual = objectOutput.read_data("Actual_Rule");
					Expected = objectOutput.read_data("Expected_Rule");
					objDriver.rule_validate(objectInput, objectOutput);
				}*/
				else
				{
					Actual = Float.parseFloat(actualData);
					Expected = Float.parseFloat(expectedData);
				}
	    try
	    {		
	    	if(Actual==Expected)
	    	{
	    		Status="Pass";
	    	}
	    	else 
	    	{
	    		float maximum = Math.max(Actual,Expected);
	    		float minimum = Math.min(Actual,Expected);
	    		if((maximum-minimum<=1.0) && (Type.equals("premiumfloat")))
	    		{
	    			Status="Pass-with roundoff";
	    		}
	    		else
	    		{
	    			Status="Fail";
	    			flag="False";
	    		}
	    	}
	    	objectOutput.write_data(dbcolumnNmae_Status, Status);
	    	objectOutput.update_row();
	    }
	    catch(NullPointerException e)
	    {
	    	Status="Fail";
	    	flag="False";
	    	objectOutput.write_data(dbcolumnNmae_Status, Status);
	    	objectOutput.update_row();
	    	
	    }
	    objectOutput.write_data(dbcolumnNmae_Status, Status);
	    objectOutput.update_row();
		comparision.next_row();
}
 if(flag.equals("False") || Form_Flag.equals("False") || Rule_Flag.equals("False"))
 {
	 objectOutput.write_data("Status", "Fail");
 }
 else
 {
	 objectOutput.write_data("Status", "Pass"); 
 }
 objectOutput.update_row();
}
}