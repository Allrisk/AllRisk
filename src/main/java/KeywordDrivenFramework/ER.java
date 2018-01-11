package KeywordDrivenFramework;

import java.text.NumberFormat;
import java.util.Locale;

import SupportingClasses.ConditionsChecking;
import SupportingClasses.ExcelOperationsPOI;
import SupportingClasses.databaseOperartions;
import SupportingClasses.databaseOperartions;
import SupportingClasses.propertiesHandle;

public class ER {
	protected static ExcelOperationsPOI objectRaterip=null;
	protected static ExcelOperationsPOI objectRater=null;
	protected static ExcelOperationsPOI objectRaterop=null;
	protected static ExcelOperationsPOI objectRaterform=null;
	protected static ConditionsChecking objectconditions=null;
	

	public static void main(String[] args) throws Exception {
		databaseOperartions objectInput = new databaseOperartions();
		databaseOperartions objectOutput = new databaseOperartions();
		//databaseOperartions objectOutputform = new databaseOperartions();
		databaseOperartions testobj = new databaseOperartions();
		propertiesHandle configFile = new propertiesHandle("D:/QA Selenium/configuration_file/Markel_BR,Vacant.properties");
		databaseOperartions.conn_setup(configFile);
		//.conn_setup(configFile);		
		objectInput.get_dataobjects(configFile.getProperty("input_query"));
		objectOutput.get_dataobjects(configFile.getProperty("output_query"));
		//objectOutputform.get_dataobjects(configFile.getProperty("output_form"));
		objectRater = new ExcelOperationsPOI(configFile.getProperty("Test_script_path")+configFile.getProperty("File_name"));
		objectRater.openWorkbook();
		objectRater.getsheets(configFile.getProperty("Rater"));
		objectRaterip =new ExcelOperationsPOI(configFile.getProperty("Test_script_path")+configFile.getProperty("File_name"));
		objectRaterip.getsheets(configFile.getProperty("InputFilling"));
		objectRaterop =new ExcelOperationsPOI(configFile.getProperty("Test_script_path")+configFile.getProperty("File_name"));
		objectRaterop.getsheets(configFile.getProperty("OutputRead"));
		String state="Texas";
		objectRater.write_data(2, 2,state);
		objectconditions=new ConditionsChecking();
		do
		{
		if(objectInput.read_data("flag_for_execution").equals(configFile.getProperty("flagForExecution")))
		{  
		String testdata=objectInput.read_data("test_case_id");
		System.out.println(testdata);
		objectRaterip.set_rownumber(1);
		objectRaterop.set_rownumber(1);
		while(objectRaterip.has_next_row())
		{
			 String conditions=objectRaterip.read_data(objectRaterip.get_rownumber(),4);
		 if(objectRaterip.read_data(objectRaterip.get_rownumber(),6).toString().equals("enabled") && objectconditions.condition_reading(conditions, objectInput))
			{	   
				String row_number = objectRaterip.read_data(objectRaterip.get_rownumber(),1);
				String col_number =  objectRaterip.read_data(objectRaterip.get_rownumber(),2);
				String dbcolumnNmae = objectRaterip.read_data(objectRaterip.get_rownumber(),3);
				if(objectRaterip.read_data(objectRaterip.get_rownumber(),5).toString().equals("Integer"))
				{
					String val=objectInput.read_data(dbcolumnNmae);
					objectRater.write_data(Integer.parseInt(row_number),Integer.parseInt(col_number),Integer.parseInt(val));
				}
				else if(objectRaterip.read_data(objectRaterip.get_rownumber(),5).toString().equals("Double"))
				{
					String val=objectInput.read_data(dbcolumnNmae);
					objectRater.write_data(Integer.parseInt(row_number),Integer.parseInt(col_number),Double.parseDouble(val)/100);
				}
				else
				{
					String val=objectInput.read_data(dbcolumnNmae);
					objectRater.write_data(Integer.parseInt(row_number),Integer.parseInt(col_number),val);
				}
			}
		
		 objectRaterip.next_row();
		}
		objectRater.refresh();
		objectRater.save();
		//objectRater.saveAs("D:/QA Selenium/"+testdata+".xls");
		while(objectRaterop.has_next_row())
		{
			 String conditions=objectRaterop.read_data(objectRaterop.get_rownumber(),4);
		 if(objectRaterop.read_data(objectRaterop.get_rownumber(),6).toString().equals("enabled") && objectconditions.condition_reading(conditions, objectInput))
			{
			    String enabled = objectRaterop.read_data(objectRaterop.get_rownumber(),6);
				String row_number = objectRaterop.read_data(objectRaterop.get_rownumber(),1);
				String col_number =  objectRaterop.read_data(objectRaterop.get_rownumber(),2);
				String dbcolumnNmae = objectRaterop.read_data(objectRaterop.get_rownumber(),3);
				String rate=objectRater.read_data(Integer.parseInt(row_number),Integer.parseInt(col_number));
				 if(objectRaterop.read_data(objectRaterop.get_rownumber(),5).toString().equals("premiumfloat"))
					{
						double rate1 = Double.parseDouble(rate);
						//objectRater.write_data(Integer.parseInt(row_number),Integer.parseInt(col_number),Double.parseDouble(val)/100);
					   	Locale USLocale = new Locale("en", "US");
					   	NumberFormat cf = NumberFormat.getCurrencyInstance(USLocale);
					   	//String convertedrate=cf.format(rate);
					   	objectOutput.write_data(dbcolumnNmae, cf.format(rate1));
						objectOutput.update_row();
					}
				 else if(objectRaterop.read_data(objectRaterop.get_rownumber(),5).toString().equals("factor2"))
					{
						float factorrate = Float.parseFloat(rate);
						String formattedString = String.format("%.2f", factorrate);
					   	objectOutput.write_data(dbcolumnNmae, formattedString);
						objectOutput.update_row();
					}
				 else if(objectRaterop.read_data(objectRaterop.get_rownumber(),5).toString().equals("factor4"))
					{
						float factorrate = Float.parseFloat(rate);
						String formattedString = String.format("%.4f", factorrate);
					   	objectOutput.write_data(dbcolumnNmae, formattedString);
						objectOutput.update_row();
					}
				 else
				 {
				objectOutput.write_data(dbcolumnNmae, rate);
				objectOutput.update_row();
				 }
			}
		 objectRaterop.next_row();
		}
		objectRater.refresh();
		objectRater.saveAs(configFile.getProperty("Rating_Model_save")+testdata+".xls");
		/*objectRaterform =new ExcelOperationsPOI(configFile.getProperty("Rating_Model_save")+testdata+".xls");
		objectRaterform.openWorkbook();
		objectRaterform.getsheets(configFile.getProperty("Forms")); 
		objectRaterform.set_rownumber(1);
		while(objectRaterform.has_next_row())
		{
		 if(objectRaterform.read_data(objectRaterform.get_rownumber(),6).toString().equals("Y"))
			{
				String form_name = objectRaterform.read_data(objectRaterform.get_rownumber(),3);
				String form=form_name.replace("'","");
				objectRaterform.refresh();
				String insert_query=("Insert into OneLondon_Forms_Expected_Result_CSP(test_case_id,expected_form)values('"+testdata+"','"+form+"')");
				objectOutputform.insert_row(insert_query);	
			}
		 objectRaterform.next_row();
		}*/
		//objectRater.saveAs("D:/QA Selenium/"+testdata+".xls");
		objectRater.save();
		objectInput.write_data("Flag_for_execution","RaterCompleted");
		
		}
		objectInput.update_row();	
		}while(objectInput.move_forward() && objectOutput.move_forward()); 
		databaseOperartions.close_conn();
	}
}