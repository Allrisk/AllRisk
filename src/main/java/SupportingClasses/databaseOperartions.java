package SupportingClasses;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class databaseOperartions
{

	public static propertiesHandle config = null;
	private static Connection conn = null;
	private static String JDBC_DRIVER = null;
	//"com.mysql.jdbc.Driver"
	private static String DB_URL =null;
	//"jdbc:mysql://192.168.35.2:3391/Search_rescue";
	private static String USER=null;
	//"root";
	private static String PASS =null; //app_json_db
	//"password";
	
	private String query = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public static  void conn_setup(propertiesHandle config) throws SQLException,ClassNotFoundException
	{
		databaseOperartions.config = config;
		JDBC_DRIVER =config.getProperty("jdbc_driver");
		DB_URL = config.getProperty("db_url");		
		USER= config.getProperty("db_username");
		PASS =config.getProperty("db_password");
		if(conn == null)
		{
			try{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL,USER,PASS);
			}
			catch(ClassNotFoundException e)
			{
				System.out.println(e);
			}
		}
			
	}
	
	public static void close_conn()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = null;
	}
	
	public void get_dataobjects(String query)throws SQLException
	{
		this.query = query;
		System.out.println(this.query);
		if(conn!=null)
		{
		stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			 rs = stmt.executeQuery(this.query);
		
			rs.first();
		}
		else 
		{
			
			System.out.println("null connection");
		}
		
		
	}

		
	public void insert_row(String query)throws SQLException
	{
		this.query = query;
		System.out.println(this.query);
		if(conn!=null)
		{
		stmt = conn.createStatement();
		stmt.executeUpdate(this.query);
		}
		else 
		{
			System.out.println("null connection");
		}
	}
	
	public Integer Resultset_rowcount(String column_name) throws SQLException
    {    
        int size = 0;
        try {
            rs.last();
            size = rs.getRow();
            rs.beforeFirst();
        }
        catch(Exception ex) 
        {
            System.out.println(size + " is the result set count");
            return 0;
        }
        System.out.println(size + " is the result set count");
        return size;    
    }
	
	public boolean move_forward() throws SQLException
	{
		
			return rs.next();
		
	}
	
	public String read_data(String column_name) throws SQLException
	{
		return rs.getString(column_name);
	}
	
	public void write_data(String column_name,String value) throws SQLException
	{
		rs.updateString(column_name, value);
	}
	
	public void update_row() throws SQLException
	{
		rs.updateRow();
	}
	
	public void move_next() throws SQLException
	{
		rs.next();
	}
	
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException
	{
		databaseOperartions.conn_setup(config);
		databaseOperartions input = new databaseOperartions();
		databaseOperartions output = new databaseOperartions();
		input.get_dataobjects("select * from input_data;");
		output.get_dataobjects("Select * from output_table");
			System.out.println(input.read_data("test_id")+"  -  "+output.read_data("testcase"));
			input.write_data("Flag_for_execution", "N");
			output.write_data("status", "fail");
			input.update_row();
			output.update_row();
		databaseOperartions.close_conn();
	}
}