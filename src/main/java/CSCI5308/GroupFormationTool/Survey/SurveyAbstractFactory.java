package CSCI5308.GroupFormationTool.Survey;

public class SurveyAbstractFactory implements ISurveyAbstractFactory {

	private static SurveyAbstractFactory uniqueInstance = null;
	private SurveyAdminDB surveyAdminDB;
	private SurveyManageDB surveyManageDB;

	public static SurveyAbstractFactory instance() {
		if (null == uniqueInstance) {
			uniqueInstance = new SurveyAbstractFactory();
		}
		return uniqueInstance;
	}

	private SurveyAbstractFactory() {
		surveyAdminDB = new SurveyAdminDB();
		surveyManageDB = new SurveyManageDB();
	}

	@Override
	public SurveyAdminDB createSurveyAdminDBInstance() {
		return surveyAdminDB;
	}

	@Override
	public SurveyManageDB createSurveyManageDBInstance() {
		return surveyManageDB;
	}

}
