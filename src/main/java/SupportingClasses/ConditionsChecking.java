package SupportingClasses;
import java.sql.SQLException;



public class ConditionsChecking {
	
	public boolean condition_reading(String condition,databaseOperartions input) throws SQLException
	{
		boolean condition_reading=false;
		
		
		if(condition.equals(""))
		{
			condition_reading=true;
			return condition_reading;
		}
		else
		{
		
		//String[] splits=condition.toString().split(";");
		String[] splits=condition.split(";");	
		int length=splits.length;
		for(int i=0;i<length;i++)
		{
			condition_reading=false;
			String[] cond_value = new String[10];
			String operator = null;
			if(splits[i].contains("="))
			{
				cond_value=splits[i].split("=");
				operator = "=";
			}
			else if(splits[i].contains("<>"))
			{
				cond_value=splits[i].split("<>");
				operator = "<>";
			}
			else if(splits[i].contains("<"))
			{
				cond_value=splits[i].split("<");
				operator = "<";
			}
			else if(splits[i].contains(">"))
			{
				cond_value=splits[i].split(">");
				operator = ">";
			}
			String cond=cond_value[0];
			String value=cond_value[1];
			String[] individualValue = value.split("\\|");
			for(int j=0;j<individualValue.length;j++)
			{
				switch(operator)
				{
				
				case "=": if((input.read_data(cond).equals(individualValue[j])))
						   {
					 			condition_reading=true;
					 			//return condition_reading;
							}
				 			break;
				case "<>": if((!input.read_data(cond).equals(individualValue[j])))
							{
								condition_reading=true;
								//return condition_reading;
							}
							break;
				
				case "<": if (Integer.parseInt((input.read_data(cond))) < Integer.parseInt(individualValue[j]))
							{
                             condition_reading=true;
							}
						   break;
				case ">":  if (Integer.parseInt((input.read_data(cond))) > Integer.parseInt(individualValue[j]))
						   	{
								condition_reading=true;
						   	}
				           else
						   {
								condition_reading=false;
						   }
						break;
				}
			}
			if(!condition_reading)
			{
				return condition_reading;
			}
		}	
		
	}
		
	return condition_reading;
		
	}

/*public static void main(String args[]) throws ClassNotFoundException, SQLException
{
	propertiesHandle configFile = new propertiesHandle("A:/1 Projects/13 Starr Assist/PDF Change/Configuration/Config_C1128.properties");
	databaseOperartions.conn_setup(configFile);
	System.setProperty("jsse.enableSNIExtension", "false");
	String condition="State_of_Residence<>Alabama|Alaska;Plan_Name<>Air Ticket Protecto;";
	databaseOperartions objectInput = new databaseOperartions();
	objectInput.get_dataobjects(configFile.getProperty("input_query"));
	 do
		{
		 
		  if(objectInput.read_data("flag_for_execution").equals("Y"))
			{  
			  ConditionsChecking obj=new ConditionsChecking();
			  System.out.println(obj.condition_reading(condition, objectInput));
			}
		}while(objectInput.move_forward()); //end of do
	   databaseOperartions.close_conn(); 
	
	
} */

}
