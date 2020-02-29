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
         // get the entered user name
         String location = request.getParameter("location");
         String date = request.getParameter("date");
         String caption = request.getParameter("caption");
         // lets the server know to expect text with the subtype "html"
         response.setContentType("text/html");  


    

         // Injects HTML code into tghe progream
         PrintWriter out = response.getWriter();
         // build HTML code
         String htmlRespone = "<html>";
         htmlRespone += "<h2>The caption is: " + caption + "<br/>";      
         htmlRespone += "the date is: " + date + "</h2>";    
         htmlRespone += "the location is: " + location + "</h2>";    
         htmlRespone += "</html>";
            
         // return response
         out.println(htmlRespone);
		  
		  
		  
	  }

}