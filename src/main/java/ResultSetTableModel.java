// A TableModel that supplies ResultSet data to a JTable.
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.x.protobuf.MysqlxPrepare;


// ResultSet rows and columns are counted from 1 and JTable 
// rows and columns are counted from 0. When processing 
// ResultSet rows or columns for use in a JTable, it is 
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is 
// ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class ResultSetTableModel extends AbstractTableModel 
{
   private Statement statement;
   private PreparedStatement preparedStatement;
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   private int numberOfRows;

   // keep track of database connection status
   // private boolean connectedToDatabase = false;
   
   // constructor initializes resultSet and obtains its meta data object;
   // determines number of rows
   public ResultSetTableModel( String query ) 
      throws SQLException, ClassNotFoundException
   {         
	   // Properties properties = new Properties();
	   // FileInputStream filein = null;
	   // MysqlDataSource dataSource = null;
       //read properties file
	   try {
	    	// filein = new FileInputStream("db.properties");
	    	// properties.load(filein);
	    	// dataSource = new MysqlDataSource();
	    	// dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
	    	// dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
	    	// dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
	    
            // connect to database bikes and query database
  	        // establish connection to database
   	        // Connection connection = dataSource.getConnection();
	
            // create Statement to query database
            preparedStatement = ClientApp.connect.prepareStatement( query );

            // update database connection status
            // connectedToDatabase = true;

            // set query and execute it
            setQuery( query );
		
		    //set update and execute it
		    //setUpdate (query);
	  } //end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
         System.exit( 1 );
      } // end catch

   } // end constructor ResultSetTableModel

   // get class that represents column type
   public Class getColumnClass( int column ) throws IllegalStateException
   {
      // ensure database connection is available
      // determine Java class of column
      try 
      {
         String className = metaData.getColumnClassName( column + 1 );
         
         // return Class object that represents className
         return Class.forName( className );
      } // end try
      catch ( Exception exception ) 
      {
         exception.printStackTrace();
      } // end catch
      
      return Object.class; // if problems occur above, assume type Object
   } // end method getColumnClass

   // get number of columns in ResultSet
   public int getColumnCount() throws IllegalStateException
   {   
      // ensure database connection is available
      // determine number of columns
      try 
      {
         return metaData.getColumnCount(); 
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return 0; // if problems occur above, return 0 for number of columns
   } // end method getColumnCount

   // get name of a particular column in ResultSet
   public String getColumnName( int column ) throws IllegalStateException
   {    
      // ensure database connection is available

      // determine column name
      try 
      {
         return metaData.getColumnName( column + 1 );  
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string for column name
   } // end method getColumnName

   // return number of rows in ResultSet
   public int getRowCount() throws IllegalStateException
   {      
      // ensure database connection is available

      return numberOfRows;
   } // end method getRowCount

   // obtain value in particular row and column
   public Object getValueAt( int row, int column ) 
      throws IllegalStateException
   {
      // ensure database connection is available

      // obtain a value at specified ResultSet row and column
      try 
      {
		   resultSet.next();  /* fixes a bug in MySQL/Java with date format */
         resultSet.absolute( row + 1 );
         return resultSet.getObject( column + 1 );
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string object
   } // end method getValueAt
   
   // set new database query string
   public void setQuery( String query )
      throws SQLException, IllegalStateException 
   {
      // ensure database connection is available

      // specify query and execute it
      resultSet = preparedStatement.executeQuery( query );

      // obtain meta data for ResultSet
      metaData = resultSet.getMetaData();

      // determine number of rows in ResultSet
      resultSet.last();                   // move to last row
      numberOfRows = resultSet.getRow();  // get row number      
      
      // notify JTable that model has changed
      fireTableStructureChanged();
   } // end method setQuery


// set new database update-query string
   public void setUpdate( String query ) 
      throws SQLException, IllegalStateException 
   {
	  int res;
      // ensure database connection is available

      // specify query and execute it
      res = preparedStatement.executeUpdate( query );
/*
      // obtain meta data for ResultSet
      metaData = resultSet.getMetaData();
      // determine number of rows in ResultSet
      resultSet.last();                   // move to last row
      numberOfRows = resultSet.getRow();  // get row number      
*/    
      // notify JTable that model has changed
      //fireTableStructureChanged();
   } // end method setUpdate

   // close Statement and Connection               
   public void disconnectFromDatabase()            
   {              

      try
      {                                            
         statement.close();                        
         // connection.close();
      } // end try                                 
      catch ( SQLException sqlException )          
      {                                            
         sqlException.printStackTrace();           
      } // end catch                               

   } // end method disconnectFromDatabase          
}  // end class ResultSetTableModel





