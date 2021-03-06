package CSCI5308.GroupFormationTool.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import CSCI5308.GroupFormationTool.QuestionManager.IQuestion;
import CSCI5308.GroupFormationTool.QuestionManager.QuestionType;

public class Response implements IResponse {

	private long id;
	private String response;
	private static final Logger LOG = LogManager.getLogger();

	public Response() {
		setDefaults();
	}

	public void setDefaults() {
		id = -1;
		response = "";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<IQuestion> sortQuestionByDateCreated(List<IQuestion> quesionsWithoutOptions,
			List<IQuestion> quesionsWithOptions) {
		LOG.info("Sorting the question list based on date created");
		List<IQuestion> questions = new ArrayList<>();

		for (IQuestion question : quesionsWithoutOptions) {
			questions.add(question);
		}
		for (IQuestion question : quesionsWithOptions) {
			questions.add(question);
		}

		for (int i = 0; i < questions.size(); i++) {
			for (int j = i + 1; j < questions.size(); j++) {
				if (questions.get(i).getTimestamp().after(questions.get(j).getTimestamp())) {
					IQuestion question = questions.get(i);
					questions.set(i, questions.get(j));
					questions.set(j, question);
				}
			}
		}

		return questions;
	}

	public HashMap<String, String> saveResponseAnswer(HttpServletRequest request, List<IQuestion> questionList,
			List<IQuestion> loadQuestionsOptions) {

		HashMap<String, String> answer = new HashMap<String, String>();

		for (IQuestion question : questionList) {
			String id = Long.toString(question.getId());
			answer.put(id, request.getParameter(id));
		}

		for (IQuestion question : loadQuestionsOptions) {
			String id = Long.toString(question.getId());
			if (question.getType().toString() == QuestionType.MCQMULTIPLE.toString()) {

				String result = "";
				for (String response : question.getOptions()) {

					if (null != request.getParameter(id + ':' + response)) {
						result += ":" + response;
					}
				}
				answer.put(id, result);
			} else {
				answer.put(id, request.getParameter(id));
			}
		}

		return answer;
	}

	public boolean saveResponse(HashMap<String, String> answer, String bannerId) {
		IResponsePersistence responseDB = ResponseAbstractFactory.instance().createResponseDBInstance();
		boolean status = false;
		for (String questionId : answer.keySet()) {
			if (responseDB.checkIsMCQMultiple(questionId)) {
				String[] options = answer.get(questionId).split(":");
				for (String option : options) {
					if (option.equals("")) {

					} else {
						try {
							status = responseDB.saveResponse(questionId, bannerId, option);
						} catch (SQLException e) {
							return false;
						}
					}
				}
			} else {
				try {
					status = responseDB.saveResponse(questionId, bannerId, answer.get(questionId));
				} catch (SQLException e) {
					return false;
				}
			}
		}
		return status;
	}

	public List<IQuestion> loadQuestionOptions(List<IQuestion> questions, IResponsePersistence responsePersistence) {
		List<IQuestion> questionList = new ArrayList<IQuestion>();
		for (IQuestion question : questions) {
			IQuestion loadOptions = responsePersistence.loadQuestionsOptions(question);
			questionList.add(loadOptions);
		}
		return questionList;
	}

}
