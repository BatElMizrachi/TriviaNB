import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author NoyA
 */
@WebServlet(urlPatterns = {"/StartGame"})
public class StartGame extends HttpServlet 
{
    private Manager manager;
    
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
            throws ServletException, IOException, ClassNotFoundException 
    {
        HttpSession session = request.getSession(true);
        manager = (Manager) session.getAttribute ("manager");
        if (manager==null) 
        {
            manager=new Manager();
            session.setAttribute ("manager", manager);
            session.setAttribute("CorrectAnswers", 0); 
            session.setAttribute("NumofQuestions", 0); 
            
            session.setAttribute("FoodCount", 0);
            session.setAttribute("HistoryCount", 0);
            session.setAttribute("SportCount", 0);
            session.setAttribute("OtherCount", 0);
        }
        
        if(request.getParameter("endOrNot") != null && request.getParameter("endOrNot").equals("Continue")) // ask question
        {
            try (PrintWriter out = response.getWriter()) 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet StartGame</title>");            
                out.println("</head>");
                out.println("<body>");
                AskQuestionForm(out, session);
                out.println("</body>");
                out.println("</html>");
            }
        }
        else if(request.getParameter("Check") != null) // Check if correct and ask if wants more
        {
            int index = (int)session.getAttribute("NumofQuestions");
            QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).GetQuestionByIndex(index);
            CheckAnswer(request, currentQuestion, response);
        }
        else if(request.getParameter("endOrNot") != null && request.getParameter("endOrNot").equals("End game")) // show points
        {
            int numberOfQuestion = (int)session.getAttribute("NumofQuestions");
            int numberOfCorrectAnswers = (int)session.getAttribute("CorrectAnswers");
            int score = numberOfCorrectAnswers * 100 / numberOfQuestion;
            
            ShowScoreView(response, score, session);
        }
        else // calc questions
        {
            HashMap<String,String> CategoriesLevel = GetCategoriesLevelByUserChoose(request,response);
            ((Manager)session.getAttribute("manager")).CalculateQuestionList(CategoriesLevel);
            
            int index = (int)session.getAttribute("NumofQuestions");
            
            try (PrintWriter out = response.getWriter()) 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet StartGame</title>");   
                
                if(!((Manager)session.getAttribute("manager")).IsQuestionIsEmpty())
                {
                    QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).GetQuestionByIndex(index);
                    if(currentQuestion.GetQuestionType() == QuestionType.YesNo)
                    {
                        out.println("<script language=\"javascript\">");
                        out.println("function validateForm()\n");
                        if(currentQuestion.GetQuestionType() == QuestionType.Open)
                        {
                            out.println("    var x = document.forms[\"AskForm\"][\"openAnswer\"].value;\n" +
                                        "    if (x==null || x==\"\") {\n" +
                                        "        alert(\"Answer field must be filled out\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "}\n");
                        }
                        else if (currentQuestion.GetQuestionType() == QuestionType.MultiplePossible)
                        {
                            out.println("    var y = document.forms[\"AskForm\"][\"answerNumber\"].value;\n" +
                                        "    if (y==null || y==\"\") {\n" +
                                        "        alert(\"Answer field must be filled out\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "    if (isNaN(parseFloat(y))) {\n" +
                                        "        alert(\"Answer field must be numeric\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "    if (y < 1) {\n" +
                                        "        alert(\"Answer must be bigger then 0\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "}\n" );
                        }

                        out.println("</script>");
                    }
                }
                
                out.println("</head>");
                out.println("<body>");
                
                if(((Manager)session.getAttribute("manager")).IsQuestionIsEmpty())
                {
                    out.println("<form name=\"Failure\">");
                    out.println("<h1>There are no questions thet meet the conditions</h1>");
                    out.println("</form>");
                }
                else
                {
                    AskQuestionForm(out, session);
                }
                
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
    
    private void CheckAnswer(HttpServletRequest request, QuestionBase currentQuestion, HttpServletResponse response)
            throws NumberFormatException, IOException 
    {
        boolean isCorrect = false;
        
        if(currentQuestion.GetQuestionType().equals(QuestionType.Open))
        {
            if(((OpenQuestion)currentQuestion).GetAnswer().equals(request.getParameter("openAnswer")))
            {
                isCorrect = true;
            }
        }
        else if(currentQuestion.GetQuestionType().equals(QuestionType.YesNo))
        {
            if((((YesNoQuestion)currentQuestion).GetAnswer() && request.getParameter("yesNoAnswer").equals("Yes")) ||
                    (!((YesNoQuestion)currentQuestion).GetAnswer() && request.getParameter("yesNoAnswer").equals("No")))
            {
                isCorrect = true;
            }
        }
        else if (currentQuestion.GetQuestionType().equals(QuestionType.MultiplePossible))
        {
            if(((MultiplePossibleQuestion)currentQuestion).GetAnswer() ==
                    Integer.parseInt(request.getParameter("answerNumber")))
            {
                isCorrect = true;
            }
        }
        HttpSession session = request.getSession(true);
        int index = (int)session.getAttribute("NumofQuestions");
        int correctAnswers = (int)session.getAttribute("CorrectAnswers");
        
        session.setAttribute("NumofQuestions", index+1);
        if(isCorrect)
        {
            session.setAttribute("CorrectAnswers", correctAnswers+1);
        }
        
        CheckView(response, index+1 < ((Manager)session.getAttribute("manager")).QuestionSize(), isCorrect);
        
    }

    private void CheckView(HttpServletResponse response, boolean lastNotQuestion, boolean isCorrect)
            throws IOException 
    {
        try (PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StartGame</title>");     
            out.println("</head>");
            out.println("<body>");
            
            if(isCorrect)
            {
                out.println("<form name=\"Success\">");
                out.println("<h1>Excellent!! correct answer</h1>");
                out.println("</form>");
                out.println("<form name=\"Submit\">");
                
                out.println("<div id=\"divForEndOrNot\"></div>");
                if(lastNotQuestion)
                {
                    out.println("<br>");
                    out.println("<input type=\"submit\" name =\"endOrNot\" value=\"Continue\">");
                }
                
                out.println("<br>");
                
                out.println("<input type=\"submit\" value=\"End game\" name =\"endOrNot\">");
                
                out.println("</form>");
            }
            else
            {
                out.println("<form name=\"Failure\">");
                out.println("<h1>Wrong answer</h1>");
                out.println("</form>");
                out.println("<form name=\"Submit\">");
                
                out.println("<div id=\"divForEndOrNot\"></div>");
                if(lastNotQuestion)
                {
                    out.println("<br>");
                    out.println("<input type=\"submit\" value=\"Continue\" name =\"endOrNot\">");
                }
                
                out.println("<br>");
                
                out.println("<input type=\"submit\" value=\"End game\" name =\"endOrNot\">");
                
                out.println("</form>");
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void ShowScoreView(HttpServletResponse response, int score, HttpSession session) 
            throws IOException 
    {
        try (PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StartGame</title>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h2>hey, " + session.getAttribute("FirstName") + " " + 
                                      session.getAttribute("LastName") + ".</h1>");
            
            out.println("<h3>You asked by categories</h1>");
            out.println("<h3>Food: " + session.getAttribute("FoodCount") + "</h1>");
            out.println("<h3>History: " + session.getAttribute("HistoryCount") + "</h1>");
            out.println("<h3>Sport: " + session.getAttribute("SportCount") + "</h1>");
            out.println("<h3>Other: " + session.getAttribute("OtherCount") + "</h1>");
            
            if(score <= 60)
            {
                out.println("<form name=\"scoreBad\">");
                out.println("<h1>Your score is: "+ score +"</h1>");
                out.println("<h1>Maybe next time you will have better luck</h1>");
                out.println("</form>");
            }
            else if (score < 90)
            {
                out.println("<form name=\"scoreGood\">");
                out.println("<h1>Your score is: "+ score +"</h1>");
                out.println("<h1>There is room to improve, try again</h1>");
                out.println("</form>");
            }
            else
            {
                out.println("<form name=\"scoreExcellent\">");
                out.println("<h1>Your score is: "+ score +"</h1>");
                out.println("<h1>Excellent! It seems that you are an expert</h1>");
                out.println("</form>");
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void AskQuestionForm(final PrintWriter out, HttpSession session) 
    {
        int index = (int)session.getAttribute("NumofQuestions");
        QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).GetQuestionByIndex(index);
        
        out.println("<form name=\"AskForm\">");
        out.println("<input type=\"hidden\" name=\"Check\" value=\"Yes\">");
        UpdateCategoryCount(session, currentQuestion);
        
        if(currentQuestion.GetQuestionType().equals(QuestionType.Open))
        {
            ShowQuestion(out, currentQuestion);
            out.println("<h1>Your answer:</h1>");
            out.println("<br>");
            out.println("<input type=\"text\" name=\"openAnswer\" width=\"400\" height=\"50\">");
            
        }
        else if(currentQuestion.GetQuestionType().equals(QuestionType.YesNo))
        {
            ShowQuestion(out, currentQuestion);
            
            out.println("<h1>Your answer:</h1>");
            out.println("<br>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"Yes\" checked>Yes");
            out.println("<br>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"No\">No");  
        }
        else if (currentQuestion.GetQuestionType().equals(QuestionType.MultiplePossible))
        {
            ShowQuestion(out, currentQuestion);
            out.println("   <ol>");
            
            Map<String, String> allAnswer = ((MultiplePossibleQuestion)currentQuestion).GetAllAnswer();
            for (int i = 1; i <= allAnswer.size(); i++) {
                out.println("       <li>" + allAnswer.get(Integer.toString(i)) + "</li>");
            }
            
            out.println("   </ol>");
            
            out.println("<h1>Select answers number:</h1>");
            out.println("<input type=\"text\" name=\"answerNumber\">");
        }
        
        out.println("<br>");
        out.println("<input type=\"submit\" value=\"Send\" onsubmit=\"return validateForm()\" >");
        out.println("</form>");
    }

    private void ShowQuestion(final PrintWriter out, QuestionBase currentQuestion) {
        out.println("<h1>The question is:</h1>");
        out.println("<br>");
        out.println("<h1>"+ currentQuestion.GetQuestion() +"</h1>");
        out.println("<br>");
    }

    private void UpdateCategoryCount(HttpSession session, QuestionBase question)
    {
        if(question.GetCategory() == Category.Food)
        {
            int count = (int)session.getAttribute("FoodCount");
            session.setAttribute("FoodCount", count+1);
        }
        else if(question.GetCategory() == Category.History)
        {
            int count = (int)session.getAttribute("HistoryCount");
            session.setAttribute("HistoryCount", count+1);
        }
        else if(question.GetCategory() == Category.Sport)
        {
            int count = (int)session.getAttribute("SportCount");
            session.setAttribute("SportCount", count+1);
        }
        else if(question.GetCategory() == Category.Other)
        {
            int count = (int)session.getAttribute("OtherCount");
            session.setAttribute("OtherCount", count+1);
        }
    }
    
    protected HashMap<String,String> GetCategoriesLevelByUserChoose(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
       // ArrayList<String,String> categoryLevelUser = new ArrayList<String,String>();
        HashMap<String,String> categoryLevelUser = new HashMap<String,String>();
        String[] category = request.getParameterValues("Category");
        String categoryLevel;
        
        for (int i = 0; i < category.length; i++) 
        {
            categoryLevel = "Level" + category[i];
            categoryLevelUser.put(category[i], request.getParameter(categoryLevel)) ;
        }
        
        return categoryLevelUser;
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
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
