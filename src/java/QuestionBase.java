import java.io.Serializable;
import java.util.Scanner;

public abstract class QuestionBase implements IQuestionBase, Serializable 
{
    protected static Scanner reader = new Scanner(System.in);
    private String question;
    private Category category;
    private Level level;
    
    public abstract QuestionType GetQuestionType();
    
    @Override
    public Category GetCategory() {
        return this.category;
    }

    @Override
    public Level GetLevel() {
        return this.level;
    }

    @Override
    public void SetCategory(Category category) {
        this.category = category;
    }

    @Override
    public void SetLevel(Level level) {
        this.level = level;
    }
    
    public void SetQuestion(String question)
    {
        this.question = question;
    }
    public String GetQuestion()
    {
        return this.question;
    }
}
