
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {

    private String errorMessage;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        if(request.getParameter("forSave") != null) // view to save question 
        {
            PrintWriter out = response.getWriter();
            try 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddQuestion</title>"); 
                out.println("<link href=\"Style/appliction.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                            "<link href=\"Style/Question.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("</head>");
                out.println("<body>");
                
                AddQuestionByType(request, out);

                out.println("<form name=\"Success\">");        
                out.println("<nav class=\"headerContain\">");
                out.println("<h1 class=\"h1-m\">The question has been saved</h1>");
                out.println("<span><img src=\"Pic/correct.png\" alt=\"\" class=\"correct_pic\"></span>");
                out.println("</nav>");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            catch (InvalidValueException ivEx)
            {
                out.println("<form name=\"Failure\">");
                out.println("<h1>"+errorMessage+"</h1>");
                out.println("<img src=\"Pic/Worng.jpg\"/>");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            catch (Exception ex) 
            {
                out.println("<form name=\"Failure\">");
                out.println("<h1>The question has not been saved</h1>");
                out.println("<img src=\"Pic/Worng.jpg\"/>");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            finally
            { 
                out.close();
            }
        }
        else if(request.getParameter("count") != null) // view - for multiple
        {
            PrintWriter out = response.getWriter();
            try 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddQuestion</title>");  
                out.println("<link href=\"Style/appliction.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                            "<link href=\"Style/Question.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                
                SetJavaScriptForMultiplePossibleQuestion(out);
                
                out.println("</head>");
                out.println("<body>");
                out.println("<form name=\"showViewQuestionToAdd\" Action=\"AddQuestion\" onsubmit=\"return(validateForm());\">");
                
                HiddenInputViewForMultiplePossibleQuestion(out, request);
                out.println("<h1>Insert question:</h1>");
                out.println("<input type=\"text\" name=\"question\" value=" 
                        + request.getParameter("question") 
                        + " width=\"400\" height=\"50\">");
                out.println("<br>");
                out.println("<div class=\"possible-answers\">");
                out.println("   <h1>Insert possible answers:</h1>");
                out.println("   <ol>");
                
                for (int i = 1; i <= Integer.parseInt(request.getParameter("count")); i++) {
                    out.println("       <li class=\"list-answers\">");
                    out.println("           <input type=\"text\" name=\""+i+"\" width=\"400\" height=\"50\">");
                    out.println("       </li>");
                }
                
                out.println("   </ol>");
                out.println("</div>");
                
                out.println("<input type=\"hidden\" name=\"forSave\" value=\"yes\">");
                out.println("<h1>Select answers number:</h1>");
                out.println("<input type=\"text\" name=\"numberOfAnswer\">");
                
                SaveView(out);

                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            finally
            {
                out.close();
            }
        }
        
        else // view - to insert question 
        {
            Level level = Utils.GetLevelByUserChoose((String) request.getParameter("Level"));
            Category category = Utils.GetCategoryByUserChoose((String) request.getParameter("Category"));
            QuestionType questionType = Utils.GetQuestionTypeByUserChoose((String) request.getParameter("QuestionType"));

            PrintWriter out = response.getWriter();
            try 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddQuestion</title>"); 
                out.println("<link href=\"Style/appliction.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                            "<link href=\"Style/Question.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                
                SetJavaScript(questionType, out);
                
                out.println("</head>");
                out.println("<body>");
                out.println("<form name=\"showViewQuestionToAdd\" Action=\"AddQuestion\" onsubmit=\"return(validateForm());\">");
                
                SetViewByQuestionType(questionType, out, request);

                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            finally
            {
                out.close();
            }
        }
    }

    private void SetViewByQuestionType(QuestionType questionType, final PrintWriter out, HttpServletRequest request) {
        if(questionType.equals(QuestionType.Open))
        {
            HiddenInputView(out, request);
            QuestionView(out);
            out.println("<h1>Insert answer:</h1>");
            out.println("<input type=\"text\" name=\"openAnswer\" class=\"Answer\">");
            out.println("<input type=\"hidden\" name=\"forSave\" value=\"yes\">");
            SaveView(out);
        }
        else if (questionType.equals(QuestionType.YesNo))
        {
            HiddenInputView(out, request);
            QuestionView(out);
            out.println("<h1>Select answer:</h1>");
            out.println("<div class=\"yesNoAnswer\">");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"Yes\" checked>Yes");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"No\">No");
            out.println("<input type=\"hidden\" name=\"forSave\" value=\"yes\">");
            out.println("</div>");
            SaveView(out);
        }
        else if (questionType.equals(QuestionType.MultiplePossible))
        {
            QuestionView(out);
            
            out.println("<h1>Insert count of possible answers:</h1>");
            out.println("<input type=\"text\" name=\"count\" class=\"count\">");      
            out.println("<button type=\"submit\" class=\"btn btn-default btn-continue\" value=\"Continue\">Continue</button>");
            
            HiddenInputView(out, request);
        }
        else
        {
            out.println("<form name=\"error\">");
            out.println("<h1 name=\"error\">Error</h1>");
            out.println("</form>");
        }
    }

    private void AddQuestionByType(HttpServletRequest request , final PrintWriter out) throws Exception {
        
        ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        allQuestions = FileHandler.ReadQuestions(request.getRealPath("/"));
        
        if(request.getParameter("openAnswer") != null)
        {
            OpenQuestion openQuestion = new OpenQuestion();
            openQuestion.SetAnswer(request.getParameter("openAnswer"));
            openQuestion.SetQuestion(request.getParameter("question"));
            openQuestion.SetCategory(Utils.GetCategoryByUserChoose(request.getParameter("Category")));
            openQuestion.SetLevel(Utils.GetLevelByUserChoose(request.getParameter("Level")));
            allQuestions.add(openQuestion);
            FileHandler.WriteQuestions(allQuestions, request.getRealPath("/"));
        }
        else if (request.getParameter("yesNoAnswer") != null)
        {
            YesNoQuestion yesNoQuestion = new YesNoQuestion();
            yesNoQuestion.SetAnswer(request.getParameter("yesNoAnswer"));
            yesNoQuestion.SetQuestion(request.getParameter("question"));
            yesNoQuestion.SetCategory(Utils.GetCategoryByUserChoose(request.getParameter("Category")));
            yesNoQuestion.SetLevel(Utils.GetLevelByUserChoose(request.getParameter("Level")));
            allQuestions.add(yesNoQuestion);
            FileHandler.WriteQuestions(allQuestions,request.getRealPath("/"));
        }
        else if (request.getParameter("numberOfAnswer") != null)
        {
            boolean cantSave= false;
            errorMessage = "The question will not save: ";
            
            MultiplePossibleQuestion multiplePossibleQuestion = new MultiplePossibleQuestion();
            multiplePossibleQuestion.SetAnswer(Integer.parseInt(request.getParameter("numberOfAnswer")));
            multiplePossibleQuestion.SetQuestion(request.getParameter("question"));
            int numberOfPossibleAnswer = 0;
            
            for (int i = 1; i <= Integer.parseInt(request.getParameter("count")); i++)
            {
                if(request.getParameter(Integer.toString(i)) != null &&
                        request.getParameter(Integer.toString(i)).isEmpty())
                {
                    cantSave = true;
                    errorMessage += "There are empty possible answers.";
                    break;
                }
                else
                {
                    multiplePossibleQuestion.AddToAllAnswer(Integer.toString(i),
                            request.getParameter(Integer.toString(i)));
                    numberOfPossibleAnswer++;
                }
            }
            
            if(cantSave &&
                Integer.parseInt(request.getParameter("numberOfAnswer")) > 0 &&
                Integer.parseInt(request.getParameter("numberOfAnswer")) <= numberOfPossibleAnswer)
            {
                cantSave = true;
                errorMessage += "The correct answer number is not valid - must be between 1 to " + numberOfPossibleAnswer + ".";
            }
            
            if(cantSave)
            {
                throw new InvalidValueException();
            }
            else
            {
                multiplePossibleQuestion.SetCategory(Utils.GetCategoryByUserChoose(request.getParameter("Category")));
                multiplePossibleQuestion.SetLevel(Utils.GetLevelByUserChoose(request.getParameter("Level")));
                allQuestions.add(multiplePossibleQuestion);
                FileHandler.WriteQuestions(allQuestions,request.getRealPath("/"));
            }
        }
        else
        {
            throw new Exception();
        }
    }

    private void SetJavaScript(QuestionType questionType, final PrintWriter out) {
        if(questionType.equals(QuestionType.Open))
        {
            out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    var y = document.forms[\"showViewQuestionToAdd\"][\"openAnswer\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Answer field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
        }
        else if (questionType.equals(QuestionType.YesNo))
        {
            out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
        }
        else if (questionType.equals(QuestionType.MultiplePossible))
        {
            out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    var y = document.forms[\"showViewQuestionToAdd\"][\"count\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Possible answer count field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (isNaN(parseFloat(y))) {\n" +
                    "        alert(\"Possible answer count field must be numeric\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (parseInt(y) < 2 || parseInt(y) > 20) {\n" +
                    "        alert(\"Possible answer count must be bigger the 1 and smaller than 20\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
        }
    }
    
    private void SetJavaScriptForMultiplePossibleQuestion(final PrintWriter out) {
    
        out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    var y = document.forms[\"showViewQuestionToAdd\"][\"numberOfAnswer\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Answer field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (isNaN(parseFloat(y))) {\n" +
                    "        alert(\"Answer field must be numeric\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
    }

    private void QuestionView(PrintWriter out){
        out.println("<h1>Insert question:</h1>");
        out.println("<input type=\"text\" name=\"question\" width=\"400\" height=\"50\">");
        out.println("<br>");
    }
    
    private void HiddenInputView(PrintWriter out, HttpServletRequest request){
        out.println("<input type=\"hidden\" name=\"Level\" value=\""+request.getParameter("Level")+"\">");
        out.println("<input type=\"hidden\" name=\"Category\" value=\""+request.getParameter("Category")+"\">");
    }
    
    private void HiddenInputViewForMultiplePossibleQuestion(PrintWriter out, HttpServletRequest request){
        out.println("<input type=\"hidden\" name=\"Level\" value=\""+request.getParameter("Level")+"\">");
        out.println("<input type=\"hidden\" name=\"Category\" value=\""+request.getParameter("Category")+"\">");
        out.println("<input type=\"hidden\" name=\"count\" value=\""+request.getParameter("count")+"\">");
    }
    
    private void SaveView(PrintWriter out){
        out.println("<br>");
        out.println("<button type=\"submit\" class=\"btn btn-default btn-save\" value=\"Save\">Save</button>");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AddQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AddQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
