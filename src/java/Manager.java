
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NoyA
 */
public class Manager 
{
    ArrayList<QuestionBase> questions;
    
    public QuestionBase GetQuestionByIndex(int index)
    {
        return questions.get(index);
    }
    
    public boolean IsQuestionIsEmpty()
    {
        return questions.isEmpty();
    }
    
    public int QuestionSize()
    {
        return questions.size();
    }
    
    public void CalculateQuestionList(HashMap<String,String> CategoriesLevel, String path) 
            throws IOException, FileNotFoundException, ClassNotFoundException
    {
        // טעינת השאלות
        ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        allQuestions = FileHandler.ReadQuestions(path);
        
        //הכנסת השאלות המתאימות לאוביקט
        questions = new ArrayList<QuestionBase>();
        for (QuestionBase question : allQuestions)
        {
            if ((CategoriesLevel.containsKey(question.GetCategory().toString()))
                    && (CategoriesLevel.containsValue(question.GetLevel().toString())))
            {
                questions.add(question);
            }
        }
        
        //ערבוב נתונים
        Collections.shuffle(questions, new Random());
    }
}
