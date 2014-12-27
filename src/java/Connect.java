/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import javafx.scene.web.WebEvent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Bat-El
 */
@WebServlet(urlPatterns = {"/Connect"})
public class Connect extends HttpServlet 
{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        
        // Create a session object if it is not created.
        HttpSession session = request.getSession(true);
        
        String computerName = java.net.InetAddress.getLocalHost().getHostName();
        if (request.getParameter("AfterFill") != null)
        {
            if( request.getParameter("remember-me") != null )
            {
                SaveToCookie(request,response,computerName);
            }
            
            // העברת שם פרטי ומשפחה?!
            response.sendRedirect("/Trivia/StartGame.html");
        }
        
        if (CheckRegisteredUser(request, computerName))
        {
            // העברת שם פרטי ומשפחה?!
            response.sendRedirect("/Trivia/StartGame.html");
        }
        else
        {
            SignIn(request, response);
        }
        
        
        
        
        /*
       
        
                   
            session.setAttribute("First_name", request.getParameter("First"));
            session.setAttribute("Last_name", request.getParameter("Last"));
*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean CheckRegisteredUser (HttpServletRequest request, String computerName)
    {
        Cookie[] cookies = null;
        cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
             //   cookie.getName().equals("user ")
                if (cookie.getValue().contains(computerName))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void SignIn(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Connect</title>");   
            out.println("<link href=\"Style/signin.css\" rel=\"stylesheet\" type=\"text/css\"/>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class=\"container\">\n" +
"            <form class=\"form-signin\" role=\"form\" action=\"Connect\">\n" +
"                <h2 class=\"form-signin-heading\">Please sign in</h2>\n" +
"                <label for=\"inputEmail\" class=\"sr-only\">First Name</label>\n" +
"                <input type=\"text\" class=\"form-control\" placeholder=\"First Name\" name=\"First\" required autofocus>\n" +
"                <label for=\"inputPassword\" class=\"sr-only\">Last Name</label>\n" +
"                <input type=\"text\" class=\"form-control\" placeholder=\"Last Name\" name=\"Last\" required>\n" +
"                <div class=\"checkbox\">\n" +
"                    <label>\n" +
"                        <input type=\"checkbox\" name=\"remember-me\" value=\"remember-me\"> Remember me\n" +
"                    </label>\n" +
"                </div>\n" +
"                <input type=\"hidden\" name=\"AfterFill\" value=\"Fill\">" +
"                <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n" +
"            </form>\n" +
"        </div>");

            out.println("</body>");
            out.println("</html>");
        }
    }
    
    private void SaveToCookie(HttpServletRequest request,HttpServletResponse response, String computerName)
    {
        String cookieString = "computer=" + computerName + ";first_name=" 
                                + request.getParameter("First") + ";last_name=" + request.getParameter("Last");
        Cookie cookie = new Cookie("user", cookieString);
        cookie.setMaxAge(15 * 365 * 24 * 60 * 60);
        response.addCookie(cookie);
    }
}
