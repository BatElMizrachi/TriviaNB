import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/DeleteQuestion"})
public class DeleteQuestion extends HttpServlet {

    
    ArrayList<QuestionBase> allQuestions;
    
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
            throws ServletException, IOException, FileNotFoundException {
 
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            if(request.getParameter("numberToDelete") != null)
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Show All Q</title>");   
                out.println("<link href=\"Style/bootstrap-theme.min.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/bootstrap-theme.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/bootstrap.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("</head>");
                out.println("<body>");
                
                try
                {
                    allQuestions.remove(Integer.parseInt(request.getParameter("numberToDelete")) - 1);
                    FileHandler.WriteQuestions(allQuestions);
                    
                    out.println("<form name=\"Success\">");
                    out.println("<h1>The question has been deleted</h1>");
                    out.println("<img src=\"Pic/Correct.jpg\"/>");
                    out.println("</form>");
                }
                catch (Exception ex)
                {
                    out.println("<form name=\"Failure\">");
                    out.println("<h1>The question has not been deleted</h1>");
                    out.println("<img src=\"Pic/Worng.jpg\"/>");
                    out.println("</form>");
                }
                finally
                {
                    out.println("</body>");
                    out.println("</html>");
                }
                
            }
            else
            {
                allQuestions = new ArrayList<QuestionBase>();
                try 
                {
                    allQuestions = FileHandler.ReadQuestions();
                } 
                catch (ClassNotFoundException ex) 
                {
                    Logger.getLogger(DeleteQuestion.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Show All Q</title>");   
                out.println("<link href=\"Style/bootstrap-theme.min.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/bootstrap-theme.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/bootstrap.css\" rel=\"stylesheet\" type=\"text/css\"/>");
               
                out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var y = document.forms[\"DeleteForm\"][\"numberToDelete\"].value;\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Number of question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (isNaN(parseFloat(y))) {\n" +
                    "        alert(\"Number of question field must be numeric\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
                
                out.println("</head>");
                out.println("<body>");
                out.println(ShowForDelete(allQuestions));
                
                out.println("<form name=\"DeleteForm\">");
                out.println("<h3>Insert number of question to delete:</h3>");
                out.println("<input type=\"text\" name=\"numberToDelete\"/>");
                out.println("<input type=\"submit\" value=\"Delete\" onclick=\"return validateForm();\"/>");
                out.println("</form>");
                
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    protected String ShowForDelete ( ArrayList<QuestionBase> allQuestions)
    {
        String listQuestion = "<div>List of question</div>";
        listQuestion += "<ol>";
        int index = 1;

        for (QuestionBase question : allQuestions) 
        {
            listQuestion+= "<li>" + question.GetQuestion() + "</li>";
            index++;
        }
        
        listQuestion += "</ol>";
        if (index == 1)
            return "<div class=\"alert alert-danger\" role=\"alert\">There are no questions</div>";
        
        return listQuestion;
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
