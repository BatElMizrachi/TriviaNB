
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/ServletLogin"})
public class ServletLogin extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Cookie[] cookies = request.getCookies();        
        String sname="";    

        if (cookies!=null) {
         for (Cookie c : cookies){
             if (c.getName().equals("Name"))
                 if (request.getParameter("logout")!=null){
                     c.setMaxAge(0);
                     response.addCookie(c);
                 }
                 
                 else
                     sname=c.getValue();         
         }
        }
         if (sname=="" && request.getParameter("username")!=null && request.getParameter("username")!=""){
             Cookie firstName = new Cookie("Name", request.getParameter("username"));
             sname=request.getParameter("username");
             response.addCookie( firstName ); 
            if (request.getParameter("remember")!=null)
                  firstName.setMaxAge(60*60*24); 
         }
         
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletLogin</title>");    
            out.println("</head>");
            out.println("<body>");

         if (sname!=""){

            out.println("<h3 style=\"color:white;\"> Welcome "+ sname + " </h3>");
            out.println("<form action=\"ServletLogin\" method=\"GET\">");
            out.println("<input type=\"hidden\" name=\"logout\" value=\"true\">");
            out.println("<h3> <INPUT TYPE=\"SUBMIT\" VALUE=\"Log Out\">");
            out.println("</h3> </form>");
        }
         else {
            out.println("<form action=\"ServletLogin\" method=\"GET\">");
            out.println("<h3 style=\"color:white;\"> Name: <input  type=\"text\"  name=\"username\" placeholder=\"Write Here\"><br>");
            out.println("<INPUT align=\"right\" TYPE=\"SUBMIT\" VALUE=\"Log in \">");
            out.println("<input type=\"checkbox\" name=\"remember\" value=\"True\">Remember Me<br>");

            out.println("</h3> </form>");

         }
         
            out.println("</body>");
            out.println("</html>");
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
            throws ServletException, IOException {
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

}
