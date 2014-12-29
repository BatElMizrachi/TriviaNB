import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
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
        String computerName = java.net.InetAddress.getLocalHost().getHostName();
        if (request.getParameter("AfterFill") != null)
        {
            if( request.getParameter("remember-me") != null )
            {
                Cookie c = SaveToCookie(request,response,computerName);
                UserConnect(request,response,c.getValue().toString());
            }
            else
            {
                String user = "computer=" + computerName + ";first_name=" 
                                + request.getParameter("First") + ";last_name=" + request.getParameter("Last");
                 UserConnect(request,response, user);
            }
        }
        else
        {
            String user = CheckRegisteredUser(request, computerName);
            if (user.equals("New user"))
            {
                SignIn(request, response);
            }
            else
            {
                UserConnect(request,response,user);
            }
        }
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

    private String CheckRegisteredUser (HttpServletRequest request, String computerName)
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
                    return cookie.getValue().toString();
                }
            }
        }
        return "New user";
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
"                Please sign in\n" +
"                <input type=\"text\" class=\"form-control\" placeholder=\"First Name\" name=\"First\" required autofocus>\n" +
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
    
    private Cookie SaveToCookie(HttpServletRequest request,HttpServletResponse response, String computerName)
    {
        String cookieString = "computer=" + computerName + "-first_name=" 
                                + request.getParameter("First") + "-last_name=" + request.getParameter("Last");
        Cookie cookie = new Cookie("user", cookieString);
        cookie.setMaxAge(15 * 365 * 24 * 60 * 60);
        response.addCookie(cookie);
        
        return cookie;
    }
    
    private void UserConnect(HttpServletRequest request, HttpServletResponse response,String user) throws IOException
    {
        HttpSession session =request.getSession(true);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletLogin</title>");    
            out.println("</head>");
            out.println("<body>");
            out.println("<left> Welcome, "+ user + " </left>");
        }
    }
}
