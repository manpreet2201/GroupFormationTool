package CSCI5308.GroupFormationTool.Response;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import CSCI5308.GroupFormationTool.Database.CallStoredProcedure;
import CSCI5308.GroupFormationTool.QuestionManager.IQuestion;
import CSCI5308.GroupFormationTool.QuestionManager.Question;
import CSCI5308.GroupFormationTool.QuestionManager.QuestionType;

public class ResponseDB implements IResponsePersistence{

    private static final Logger LOG = LogManager.getLogger();
	
	@Override
	public List<IQuestion> loadQuestionsWithoutOptions(long courseId) {
		
		List<IQuestion> questionList = new ArrayList<IQuestion>();
		CallStoredProcedure proc = null;
		try
		{
			proc = new CallStoredProcedure("spLoadQuestionsWithoutOptions(?)");
			proc.setParameter(1, courseId);
			ResultSet results = proc.executeWithResults();
			Question question;
			
			if (null != results)
			{
				while (results.next())
				{
					long id = results.getLong(1);
					String title = results.getString(2);
					String text = results.getString(3);
					QuestionType type = QuestionType.valueOf(results.getString(4).toUpperCase());
					Timestamp timestamp = results.getTimestamp(5);
					
					question = new Question();
					question.setId(id);
					question.setTitle(title);
					question.setText(text);
					question.setType(type);
					question.setTimestamp(timestamp);
					questionList.add(question);
				}
			}
		}
		catch (SQLException e)
		{
			LOG.error("Status = Failed, Error Message="+e.getMessage());
			return null;
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return questionList;
	}

	@Override
	public List<IQuestion> loadQuestionsWithOptions(long courseId) {
		
		List<IQuestion> questionList = new ArrayList<IQuestion>();
		CallStoredProcedure proc = null;
		try
		{
			proc = new CallStoredProcedure("spLoadQuestionsWithOptions(?)");
			proc.setParameter(1, courseId);
			ResultSet results = proc.executeWithResults();
			Question question;
			
			if (null != results)
			{
				while (results.next())
				{
					long id = results.getLong(1);
					String title = results.getString(2);
					String text = results.getString(3);
					QuestionType type = QuestionType.valueOf(results.getString(4).toUpperCase());
					Timestamp timestamp = results.getTimestamp(5);
					
					question = new Question();
					question.setId(id);
					question.setTitle(title);
					question.setText(text);
					question.setType(type);
					question.setTimestamp(timestamp);
					questionList.add(question);
				}
			}
		}
		catch (SQLException e)
		{
			LOG.error("Status = Failed, Error Message="+e.getMessage());
			return null;
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return questionList;
	}

	@Override
	public List<IQuestion> loadQuestionsOptions(List<IQuestion> questions) {
		
		List<IQuestion> questionList = new ArrayList<IQuestion>();
		CallStoredProcedure proc = null;
		try
		{

			for(IQuestion question : questions) {
				
				proc = new CallStoredProcedure("spLoadQuestionsOptions(?)");
				proc.setParameter(1, question.getId());
				
				ResultSet results = proc.executeWithResults();
				ArrayList<String> options = new ArrayList<String>();
				
				if (null != results)
				{
					while (results.next())
					{
						String displayText = results.getString(5);
						options.add(displayText);

					}
					question.setOptions(options);
					questionList.add(question);
				}
			}
			
		}
		catch (SQLException e)
		{
			LOG.error("Status = Failed, Error Message="+e.getMessage());
			return null;
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		return questionList;
	}

	@Override
	public boolean saveResponse(HashMap<String, String> answer, String bannerId) {
		
		CallStoredProcedure proc = null;
		try
		{

			for (String questionId : answer.keySet()) {
				
				if(checkIsMCQMultiple(questionId)) {
					
					
					String[] options = answer.get(questionId).split(":");
					for(String option : options) {
						if(option.equals("")) {
							
						}else {
							proc = new CallStoredProcedure("spSaveQuestionResponse(?,?,?)");
							
							proc.setParameter(1, Long.parseLong(questionId));
							proc.setParameter(2, bannerId);
							proc.setParameter(3, option);
							
							proc.execute();			
						}
					}
				}else {
					
					proc = new CallStoredProcedure("spSaveQuestionResponse(?,?,?)");
					
					proc.setParameter(1, Long.parseLong(questionId));
					proc.setParameter(2, bannerId);
					proc.setParameter(3, answer.get(questionId));
					
					proc.execute();	
				}
			}
			
			return true;
			
		}
		catch (SQLException e)
		{
			LOG.error("Status = Failed, Error Message="+e.getMessage());
			return false;
		}
		finally
		{
			if (null != proc)
			{
				proc.cleanup();
			}
		}
		
	}

	@Override
	public boolean checkIsMCQMultiple(String questionId) {
		
        boolean result = false;
        CallStoredProcedure proc = null;
        try
        {
            proc = new CallStoredProcedure("spCheckIsMCQMultiple(?)");
            proc.setParameter(1,questionId);
            ResultSet results = proc.executeWithResults();
            if (null != results)
            {
                if (results.next())
                {
                	String type = results.getString(1);
                	if(type.equals(QuestionType.MCQMULTIPLE.toString())) {
                		result = true;
                	}
                }
            }
        }
        catch (SQLException e)
        {
        	LOG.error("Status = Failed, Error Message="+e.getMessage());
            return false;
        }
        finally
        {
            if (null != proc)
            {
                proc.cleanup();
            }
        }
        return result;
	}

}
