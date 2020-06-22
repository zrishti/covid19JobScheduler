package com.zrishti.boot.covid19Jobs;
import java.io.*;
import java.sql.*;


public class DBAccess {
	public static final String DBURL ="jdbc:mysql://localhost:3306/abc";
	public static final  String  DBUSERNAME ="root";
	public static final String  DBPASSWORD ="";


   Connection con =null;
   
   public void getConnection()
   {
	   String DBURL="jdbc:mysql://localhost:3306/abc";
	   String DBUSERNAME="root";
	   String DBPASSWORD="";
	   
	   String driver="com.mysql.cj.jdbc.Driver";
	   
	try {
		Class.forName(driver);
		con = DriverManager.getConnection(DBURL,DBUSERNAME,DBPASSWORD);
	}
	catch (Exception e) 
	{
		e.printStackTrace();
	}
	
	 
   }


   public boolean insertIntoDB(String query)
   {
	   System.out.println(query);
		  
		ResultSet rs = null;
		try {
			Statement st = con.createStatement();
			st.executeUpdate(query);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}

		return true;//all working
	   
   }
   
}
