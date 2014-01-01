package com.arcblaze.archire.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.arcblaze.archire.db.dao.RoleDao;
import com.arcblaze.archire.db.dao.UserDao;
import com.arcblaze.archire.db.util.TestDatabase;
import com.arcblaze.archire.model.Enrichment;
import com.arcblaze.archire.model.Role;
import com.arcblaze.archire.model.User;

/**
 * Perform database integration testing.
 */
public class UserDaoTest {
	/**
	 * Perform test setup activities.
	 * 
	 * @throws Exception
	 *             if there is a problem performing test initialization
	 */
	@BeforeClass
	public static void setup() throws Exception {
		TestDatabase.initialize();
	}

	/**
	 * Perform test cleanup activities.
	 */
	@AfterClass
	public static void cleanup() {
		DaoFactory.reset();
	}

	/**
	 * @throws DatabaseException
	 *             if there is a problem with the database
	 */
	@Test
	public void dbIntegrationTests() throws DatabaseException {
		Set<Enrichment> enrichments = new HashSet<>(
				Arrays.asList(Enrichment.ROLES));

		UserDao userDao = DaoFactory.getUserDao();
		Set<User> users = userDao.getAll();
		assertNotNull(users);
		assertEquals(0, users.size());

		User user = new User();
		user.setLogin("user");
		user.setHashedPass("hashed");
		user.setSalt("salt");
		user.setEmail("email");
		user.setFirstName("first");
		user.setLastName("last");

		userDao.add(user);
		assertNotNull(user.getId());

		RoleDao roleDao = DaoFactory.getRoleDao();
		user.addRoles(Role.ADMIN);
		roleDao.add(user.getId(), user.getRoles());

		try {
			User user2 = new User();
			user2.setLogin("user"); // same as other user
			user2.setHashedPass("hashed");
			user2.setEmail("email2");
			user2.setFirstName("first");
			user2.setLastName("last");
			userDao.add(user2);
			throw new RuntimeException("No unique constraint was thrown");
		} catch (DatabaseUniqueConstraintException uniqueConstraint) {
			// Expected
		}

		try {
			User user2 = new User();
			user2.setLogin("user2");
			user2.setHashedPass("hashed");
			user2.setEmail("EMAIL"); // same as other user
			user2.setFirstName("first");
			user2.setLastName("last");
			userDao.add(user2);
			throw new RuntimeException("No unique constraint was thrown");
		} catch (DatabaseUniqueConstraintException uniqueConstraint) {
			// Expected
		}

		users = userDao.getAll();
		assertNotNull(users);
		assertEquals(1, users.size());
		assertTrue(users.contains(user));

		users = userDao.getAll(enrichments);
		assertNotNull(users);
		assertEquals(1, users.size());
		assertTrue(users.contains(user));
		User getAllUser = null;
		for (User e : users)
			if (e.getId() == user.getId())
				getAllUser = e;
		assertNotNull(getAllUser);
		assertEquals(user, getAllUser);
		assertEquals(1, getAllUser.getRoles().size());
		assertTrue(getAllUser.isAdmin());

		User getUser = userDao.get(user.getId());
		assertEquals(user, getUser);
		assertEquals(0, getUser.getRoles().size());

		getUser = userDao.get(user.getId(), enrichments);
		assertEquals(user, getUser);
		assertEquals(1, getUser.getRoles().size());
		assertTrue(getUser.isAdmin());

		User loginUser = userDao.getLogin(user.getLogin());
		assertEquals(user, loginUser);
		assertEquals(0, loginUser.getRoles().size());

		loginUser = userDao.getLogin(user.getEmail());
		assertEquals(user, loginUser);
		assertEquals(0, loginUser.getRoles().size());

		loginUser = userDao.getLogin(user.getEmail().toUpperCase());
		assertEquals(user, loginUser);
		assertEquals(0, loginUser.getRoles().size());

		user.setEmail("New Email");
		userDao.update(user);
		getUser = userDao.get(user.getId());
		assertEquals(user, getUser);
		assertEquals(0, getUser.getRoles().size());

		roleDao.delete(user.getId(), user.getRoles());
		getUser = userDao.get(user.getId(), enrichments);
		assertEquals(user, getUser);
		assertEquals(0, getUser.getRoles().size());

		userDao.delete(user.getId());
		getUser = userDao.get(user.getId());
		assertNull(getUser);

		users = userDao.getAll();
		assertNotNull(users);
		assertEquals(0, users.size());
	}
}
