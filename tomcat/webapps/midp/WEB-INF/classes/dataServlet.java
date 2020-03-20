// File:         dataServlet.java
// Created:      [2020/02/28 creation date]
// Author:       Lemuel, Karen, Ryan
//
// Desc:
//	allows to the user to search for images using:
//      1. Catpion
//      2. Date
//      3. Location
//

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

//use this to compile the object
//cd C:\tomcat\webapps\midp\WEB-INF\classes
//javac -classpath .;commons-lang-2.6.jar;commons-logging-1.1.1.jar;hsqldb.jar;jackcess-2.1.2.jar;ucanaccess-3.0.1.jar;c:\tomcat\lib\servlet-api.jar dataServlet.java

//*******************************************************************
//  dataServlet
//
//  Searches through the uploaded photos
//*******************************************************************
public class dataServlet extends HttpServlet {

   

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
         response.setHeader("Cache-Control","no-cache"); //Forces caches to obtain a new copy of the page from the origin server
         response.setHeader("Cache-Control","no-store"); //Directs caches not to store the page under any circumstance
         response.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
         response.setHeader("Pragma","no-cache"); //HTTP 1.0 backward compatibility
         // Set standard HTTP/1.1 no-cache headers.
         response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

         // Set standard HTTP/1.0 no-cache header.
         response.setHeader("Pragma", "no-cache");
         response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
         Connection con = null;
         // get the entered user data
         String longitude = "'" + request.getParameter("longitude") + "'";
         String latitude = "'" + request.getParameter("latitude")+ "'";
         String date = "'" + request.getParameter("date") + "'";
         String caption = "'" + request.getParameter("caption") + "'";
         String query = "select * from ImageTable where CAPTION = " + caption +
                        " AND TAKEN = " + date +
                        " AND LONGITUDE = " + longitude +
                        " AND LATITUDE = " + latitude;


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
            PreparedStatement ps=con.prepareStatement("SELECT * FROM ImageTable WHERE CAPTION = " + caption + 
                                                      " AND TAKEN = " + date + " AND LONGITUDE = " + longitude +
                                                      " AND LATITUDE = " + latitude);  

            ResultSet rs = ps.executeQuery();
            con.setAutoCommit(true);
            System.out.println("query searched" + query);   
            
            if(rs.next()) {
               Blob b=rs.getBlob(5);
               byte barr[]=b.getBytes(1,(int)b.length()); 
                              
               FileOutputStream fout=new FileOutputStream("C:\\tomcat\\webapps\\midp\\searche.jpg");  
               fout.write(barr);
               System.out.println("picture saved");  
              

            }
            con.close();
            response.sendRedirect("http://localhost:8081/midp/database.html");     
            return;              

               
         }
         catch(SQLException ex) {
            // try {
            //    con.rollback();
            //    con.close();
            // } catch (SQLException e) {
            //    System.out.println("\nError Rolling back\n");	
            // } 
            // System.out.println("\n--- SQLException caught ---\n"); 
            // while (ex != null) { 
            //    System.out.println("Message: " + ex.getMessage ()); 
            //    System.out.println("SQLState: " + ex.getSQLState ()); 
            //    System.out.println("ErrorCode: " + ex.getErrorCode ()); 
            //    ex = ex.getNextException(); 
            //    System.out.println("");
            // } 
 

            //response.sendRedirect("http://localhost:8081/midp/database.html");
            // build HTML code
            
            // Injects HTML code into tghe progream
            PrintWriter out = response.getWriter();
                          
            String htmlRespone = "<html>";
            htmlRespone += "<h2>The caption is: " + caption + "<br/>";      
            htmlRespone += "Date: " + date + "<br/>" ;    
            htmlRespone += "latitude: " + latitude + "<br/>";    
            htmlRespone += "longitude: " + longitude + "<br/>"; 
            htmlRespone += "Query was: " + query + "</h2><br/>"; 
            htmlRespone += "Please try again search Failed!</h1><br/>"; 

            htmlRespone += "</html>";
               
            // return response
            out.println(htmlRespone);       


         }	
 
   }
}