package com.arcblaze.archire.rest.login;

import static org.junit.Assert.assertEquals;

import javax.mail.MessagingException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.arcblaze.archire.db.DaoFactory;
import com.arcblaze.archire.db.DatabaseException;
import com.arcblaze.archire.db.dao.UserDao;
import com.arcblaze.archire.db.util.TestDatabase;
import com.arcblaze.archire.mail.SendResetPasswordEmail;
import com.arcblaze.archire.model.Company;
import com.arcblaze.archire.model.Password;
import com.arcblaze.archire.model.User;

/**
 * Perform testing of the password reset capabilities.
 */
public class ResetPasswordResourceTest {
	/**
	 * Perform test setup activities.
	 * 
	 * @throws Exception
	 *             if there is a problem performing test initialization
	 */
	@Before
	public void setup() throws Exception {
		TestDatabase.initialize();
	}

	/**
	 * Perform test cleanup activities.
	 */
	@After
	public void cleanup() {
		DaoFactory.reset();
	}

	/**
	 * Test how the resource responds when the provided login value is null.
	 */
	@Test(expected = BadRequestException.class)
	public void testNullUser() {
		ResetPasswordResource resource = new ResetPasswordResource();
		resource.reset(null);
	}

	/**
	 * Test how the resource responds when the provided login value is blank.
	 */
	@Test(expected = BadRequestException.class)
	public void testBlankUser() {
		ResetPasswordResource resource = new ResetPasswordResource();
		resource.reset("  ");
	}

	/**
	 * Test how the resource responds when the user doesn't exist.
	 */
	@Test(expected = NotFoundException.class)
	public void testNonExistentUser() {
		ResetPasswordResource resource = new ResetPasswordResource();
		resource.reset("nonexistent-user");
	}

	/**
	 * Test how the resource responds when an existing login is provided as
	 * input.
	 * 
	 * @throws DatabaseException
	 *             if there is a database problem
	 */
	@Test
	public void testExistingUserByLogin() throws DatabaseException {
		Company company = new Company();
		company.setName("company");
		company.setActive(true);

		DaoFactory.getCompanyDao().add(company);

		User user = new User();
		user.setLogin("user");
		user.setHashedPass("hashed");
		user.setSalt("salt");
		user.setEmail("email@whatever.com");
		user.setFirstName("first");
		user.setLastName("last");
		user.setActive(true);

		UserDao userDao = DaoFactory.getUserDao();
		userDao.add(user);

		SendResetPasswordEmail mockEmailSender = Mockito
				.mock(SendResetPasswordEmail.class);
		Password mockPassword = Mockito.mock(Password.class);
		Mockito.when(mockPassword.random()).thenReturn("new-password");
		Mockito.when(mockPassword.random(10)).thenReturn("new-salt");
		Mockito.when(mockPassword.hash("new-password", "new-salt")).thenReturn(
				"hashed-password");

		ResetPasswordResource resource = new ResetPasswordResource(
				mockEmailSender, mockPassword);
		resource.reset("user");

		// Make sure the password was updated.
		User updated = userDao.getLogin(user.getLogin());
		assertEquals("hashed-password", updated.getHashedPass());
		assertEquals("new-salt", updated.getSalt());
	}

	/**
	 * Test how the resource responds when an existing email address is provided
	 * as input.
	 * 
	 * @throws DatabaseException
	 *             if there is a database problem
	 */
	@Test
	public void testExistingUserByEmail() throws DatabaseException {
		Company company = new Company().setName("company").setActive(true);
		DaoFactory.getCompanyDao().add(company);

		User user = new User();
		user.setLogin("user");
		user.setHashedPass("hashed");
		user.setSalt("salt");
		user.setEmail("email@whatever.com");
		user.setFirstName("first");
		user.setLastName("last");
		user.setActive(true);

		UserDao userDao = DaoFactory.getUserDao();
		userDao.add(user);

		SendResetPasswordEmail mockEmailSender = Mockito
				.mock(SendResetPasswordEmail.class);
		Password mockPassword = Mockito.mock(Password.class);
		Mockito.when(mockPassword.random()).thenReturn("new-password");
		Mockito.when(mockPassword.random(10)).thenReturn("new-salt");
		Mockito.when(mockPassword.hash("new-password", "new-salt")).thenReturn(
				"hashed-password");

		ResetPasswordResource resource = new ResetPasswordResource(
				mockEmailSender, mockPassword);
		resource.reset("email@whatever.com");

		// Make sure the password was updated.
		User updated = userDao.getLogin(user.getLogin());
		assertEquals("hashed-password", updated.getHashedPass());
		assertEquals("new-salt", updated.getSalt());
	}

	/**
	 * Test how the resource responds when there is a problem sending the email.
	 * 
	 * @throws DatabaseException
	 *             if there is a database problem
	 * @throws MessagingException
	 *             if there is an email-sending problem
	 */
	@Test
	public void testExistingUserEmailError() throws DatabaseException,
			MessagingException {
		Company company = new Company().setName("company").setActive(true);
		DaoFactory.getCompanyDao().add(company);

		User user = new User();
		user.setLogin("user");
		user.setHashedPass("hashed");
		user.setSalt("salt");
		user.setEmail("email@whatever.com");
		user.setFirstName("first");
		user.setLastName("last");
		user.setActive(true);

		UserDao userDao = DaoFactory.getUserDao();
		userDao.add(user);

		SendResetPasswordEmail emailSender = new SendResetPasswordEmail();
		SendResetPasswordEmail mockEmailSender = Mockito.spy(emailSender);
		Mockito.doThrow(MessagingException.class).when(mockEmailSender)
				.send(user, "new-password");

		Password mockPassword = Mockito.mock(Password.class);
		Mockito.when(mockPassword.random()).thenReturn("new-password");
		Mockito.when(mockPassword.random(10)).thenReturn("new-salt");
		Mockito.when(mockPassword.hash("new-password", "new-salt")).thenReturn(
				"hashed-password");

		try {
			ResetPasswordResource resource = new ResetPasswordResource(
					mockEmailSender, mockPassword);
			resource.reset("user");
			Assert.fail("Expected an email-sending error");
		} catch (InternalServerErrorException expected) {
			// This is expected.
		}

		// Make sure the password and salt values were reverted back to the
		// original.
		User updated = userDao.getLogin(user.getLogin());
		assertEquals("hashed", updated.getHashedPass());
		assertEquals("salt", updated.getSalt());
	}
}
