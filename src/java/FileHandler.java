
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class FileHandler 
{
    public static ArrayList<QuestionBase> ReadQuestions(String path) 
        throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try
        {
            String textPath = path + "data.dat";
            FileInputStream File = new FileInputStream(textPath);
            ObjectInputStream ois = new ObjectInputStream(File);
            ArrayList<QuestionBase> allQuestions = (ArrayList<QuestionBase>)ois.readObject();
            ois.close();

            return allQuestions;
        }
        catch(Exception e)
        {
            return new ArrayList<QuestionBase>();
        }
    }

    public static void WriteQuestions(ArrayList<QuestionBase> allQuestions, String path) 
            throws FileNotFoundException, IOException
    {
       /* File theDir = new File("C:\\temp1");

        if (!theDir.exists())
        {
            theDir.mkdir();
        }
 */
        String textPath = path + "data.dat";
        File FileQuestions = new File(textPath);
        FileQuestions.delete();
        
        FileOutputStream File = new FileOutputStream(textPath);
        ObjectOutputStream oos = new ObjectOutputStream(File);
        oos.writeObject(allQuestions);
        oos.close();
    }
}
