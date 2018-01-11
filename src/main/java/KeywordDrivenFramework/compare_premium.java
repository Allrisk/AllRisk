package KeywordDrivenFramework;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import SupportingClasses.ExcelOperationsJXL;
import SupportingClasses.databaseOperartions;
import SupportingClasses.propertiesHandle;

public class compare_premium {
	protected ExcelOperationsJXL comparision_tc=null;
	protected propertiesHandle configFile;
	protected static Statement statement=null;
	protected static ResultSet rs=null;
	
	public static void main(String[] args) throws Exception, SQLException {
		// TODO Auto-generated method stub
		databaseOperartions objectOutput = new databaseOperartions();
		propertiesHandle configFile = new propertiesHandle("D:/QA Selenium/configuration_file/PL_NEWUI_Markel.properties");
		databaseOperartions.conn_setup(configFile);
		System.setProperty("jsse.enableSNIExtension", "false");
		 objectOutput.get_dataobjects(configFile.getProperty("output_query"));
		 String testdata=objectOutput.read_data("test_case_id");
		 compare_premium testDriver=new compare_premium(configFile);
		 System.out.println(testdata);
		 compare_premium test_prem=new compare_premium();
			do
			{
		       if(objectOutput.read_data("flag_for_execution").equals(configFile.getProperty("flagForExecution")))
			   {  
		    	   testDriver.comparision_data(objectOutput);
		    	   objectOutput.write_data("Flag_for_execution", "Completed");
			   }
		    objectOutput.update_row();
			}while(objectOutput.move_forward());
			databaseOperartions.close_conn();
	}
	public compare_premium()
	{
	}
	
	public void comparision_data(databaseOperartions objectOutput) throws SQLException
	{
		String flag="True";	
		 comparision_tc.set_rownumber(1);
		 while(comparision_tc.has_next_row())
		{
			float Actual;
			float Expected;
			String Status;
			String dbcolumnNmae_Actual = comparision_tc.read_data(comparision_tc.get_rownumber(),1);
			String dbcolumnNmae_Expected = comparision_tc.read_data(comparision_tc.get_rownumber(),2);
			String Type = comparision_tc.read_data(comparision_tc.get_rownumber(),4);
			String actualData = objectOutput.read_data(dbcolumnNmae_Actual);
			String expectedData = objectOutput.read_data(dbcolumnNmae_Expected);
			String dbcolumnNmae_Status= comparision_tc.read_data(comparision_tc.get_rownumber(),3);
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
			    		if(maximum-minimum<=1.0)
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
				comparision_tc.next_row();
		}
		 if(flag.equals("False"))
		 {
			 objectOutput.write_data("Status", "Fail");
		 }
		 else
		 {
			 objectOutput.write_data("Status", "Pass"); 
		 }
		 objectOutput.update_row();
		}

	
	public compare_premium(propertiesHandle configFile) throws SQLException, ClassNotFoundException
	{
		this.configFile = configFile;
		comparision_tc = new ExcelOperationsJXL(this.configFile.getProperty("Test_script_path")+this.configFile.getProperty("File_name"));
		comparision_tc.getsheets(this.configFile.getProperty("status"));
	}
}