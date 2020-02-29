// File:         loginServlet.java
// Created:      [2020/02/28 creation date]
// Author:       Lemuel, Karen, Ryan
//
// Desc:
//	Parses username and password allowing the appropriate user 
//	access to the server  
//

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

//*******************************************************************
//  loginServlet
//
//  Discrimniates between various user by way of password/username
//*******************************************************************
public class loginServlet extends HttpServlet {

   /**
    * Desc:
   * 	passes information from browser to web server by producing
	* 	a long string that appears in your browser's Location:box.
   *  (unsecure do not use for passwords)
   *
   * Bugs:
   *  None atm
   */
   public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

	
   }



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
         // get the entered user name
         String username = request.getParameter("username");
         String password = request.getParameter("password");
         // lets the server know to expect text with the subtype "html"
         response.setContentType("text/html");  

         String correctUser = "rise";
         String correctPass = "up"; 

         if (username.equals(correctUser) && password.equals(correctPass)) {
            // if correct send the user to the database 
            
            response.sendRedirect("http://localhost:8081/midp/database.html");
         } else {
           response.sendRedirect("http://localhost:8081/midp/login.html");

         }

         // // Injects HTML code into tghe progream
         // PrintWriter out = response.getWriter();
         // // build HTML code
         // String htmlRespone = "<html>";
         // htmlRespone += "<h2>Your username is: " + username + "<br/>";      
         // htmlRespone += "Your password is: " + password + "</h2>";    
         // htmlRespone += "</html>";
            
         // // return response
         // out.println(htmlRespone);
		  
		  
		  
	  }

}