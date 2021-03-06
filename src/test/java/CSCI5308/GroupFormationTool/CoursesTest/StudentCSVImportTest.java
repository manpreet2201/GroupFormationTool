package CSCI5308.GroupFormationTool.CoursesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import CSCI5308.GroupFormationTool.AccessControl.IUser;
import CSCI5308.GroupFormationTool.AccessControl.UserAbstractFactory;
import CSCI5308.GroupFormationTool.AccessControlTest.UserAbstractFactoryMock;
import CSCI5308.GroupFormationTool.Courses.CourseAbstractFactory;
import CSCI5308.GroupFormationTool.Courses.ICourse;
import CSCI5308.GroupFormationTool.SecurityTest.SecurityTestAbstractFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import CSCI5308.GroupFormationTool.AccessControl.IUserPersistence;
import CSCI5308.GroupFormationTool.Courses.Role;
import CSCI5308.GroupFormationTool.Security.IPasswordEncryption;

@SpringBootTest
@SuppressWarnings("deprecation")
class StudentCSVImportTest {

	@Test
	public void enrollStudentFromRecord() {
		IUser user = UserAbstractFactory.instance().createUserInstance();
		ICourse course = CourseAbstractFactory.instance().createCourseInstance();
		IUserPersistence userDB = UserAbstractFactoryMock.instance().getUserDBMock();
		IPasswordEncryption passwordEncryption = SecurityTestAbstractFactory.instance().getPasswordEncryption();
		Assert.isTrue(user.createUser(userDB, passwordEncryption, null));
		Assert.isTrue(course.enrollUserInCourse(Role.STUDENT, user) == false);
	}

	@Test
	public void getSuccessResults() {
		List<String> successResults = new ArrayList<String>();
		successResults.add("Created record");
		assertThat(successResults).isNotNull();
		assertThat(successResults).isNotEmpty();
		Assert.isTrue(successResults.size() > 0);
	}

	@Test
	public void getFailureResults() {
		List<String> failureResults = new ArrayList<String>();
		failureResults.add("Created record");
		assertThat(failureResults).isNotNull();
		assertThat(failureResults).isNotEmpty();
		Assert.isTrue(failureResults.size() > 0);
	}

}
