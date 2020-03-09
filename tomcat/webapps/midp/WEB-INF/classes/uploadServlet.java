// File:         uploadServlet.java
// Created:      [2020/02/28 creation date]
// Author:       Lemuel, Karen, Ryan
//
// Desc:
//	allows to the user to upload images from their device
//

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

//complie using
// cd C:\tomcat\webapps\midp\WEB-INF\classes
// javac -classpath .;commons-lang-2.6.jar;commons-logging-1.1.1.jar;hsqldb.jar;jackcess-2.1.2.jar;ucanaccess-3.0.1.jar;c:\tomcat\lib\servlet-api.jar;commons-fileupload-1.4.jar;commons-io-2.6.jar uploadServlet.java

//*******************************************************************
//  uploadServlet
//
//  handles userses putting stuff into the database
//*******************************************************************
public class uploadServlet extends HttpServlet {

   /**
   * Desc:
   * 	passes information from browser to web server, works exactly
   *  the same as get but more reliable and secure
   *
   * Bugs:
   *  None atm
   */
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
         InputStream filecontent = null;
        
         Connection con = null;
         // lets the server know to expect text with the subtype "html"
         response.setContentType("text/html");         
         Part filePart = request.getPart("file");
         String longitude = request.getParameter("longitude");
         String latitude = request.getParameter("latitude");
         String date = request.getParameter("date");
         String caption = request.getParameter("caption");
         filecontent = filePart.getInputStream();
         //String filename = getFilename(file);
         //InputStream filecontent = file.getInputStream();     
         try {
            // load the driver class
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("ORACLE DIVERS CONNECTED!");
         } 
         catch (Exception ex) { 
            System.out.println("ORACLE DIVERS NOT CONNECTED!");
         }


         try {
            // establish connection
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "riseup");
            System.out.println("CONNECTION WITH DATABASE ESTABLISHED!!!");           
            con.setAutoCommit(false);
            // create table ImageTable(CAPTION char(400), TAKEN char(400), LONGITUDE char(400), LATITUDE char(400), PHOTO BLOB);        
            // Get a result set containing all data from test_table 
            PreparedStatement ps=con.prepareStatement("insert into ImageTable(CAPTION, TAKEN, LONGITUDE, LATITUDE, PHOTO) values(?,?,?,?,?)");  
            ps.setString(1, caption);  
            ps.setString(2, date);  
            ps.setString(3, longitude);  
            ps.setString(4, latitude);  
            ps.setBinaryStream(5, filecontent);
            int i=ps.executeUpdate();  
            con.commit();
            con.setAutoCommit(true);
            con.close();
            response.sendRedirect("http://localhost:8081/midp/database.html");           
         }
         catch(SQLException ex) {
            response.sendRedirect("http://localhost:8081/midp/upload.html");

         }	
        

		  
		  
	  }

}