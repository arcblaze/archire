package com.arcblaze.archire.db.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.arcblaze.archire.db.ConnectionManager;
import com.arcblaze.archire.db.DatabaseException;
import com.arcblaze.archire.db.DatabaseUniqueConstraintException;
import com.arcblaze.archire.db.dao.UserDao;
import com.arcblaze.archire.model.Enrichment;
import com.arcblaze.archire.model.Role;
import com.arcblaze.archire.model.User;

/**
 * Manages users within the back-end database.
 */
public class JdbcUserDao implements UserDao {
	protected User fromResultSet(ResultSet rs, boolean includePass)
			throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setLogin(rs.getString("login"));
		if (includePass) {
			user.setHashedPass(rs.getString("hashed_pass"));
			user.setSalt(rs.getString("salt"));
		}
		user.setEmail(rs.getString("email"));
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setActive(rs.getBoolean("active"));
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getLogin(String login) throws DatabaseException {
		if (StringUtils.isBlank(login))
			throw new IllegalArgumentException("Invalid blank login");

		String sql = "SELECT * FROM users WHERE active = true AND "
				+ "(login = ? OR LOWER(email) = LOWER(?))";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, login);
			ps.setString(2, login);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next())
					return fromResultSet(rs, true);
			}

			return null;
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User get(Integer userId, Enrichment... enrichments)
			throws DatabaseException {
		Set<Enrichment> enrichmentSet = enrichments == null ? null
				: new LinkedHashSet<>(Arrays.asList(enrichments));
		return this.get(userId, enrichmentSet);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User get(Integer userId, Set<Enrichment> enrichments)
			throws DatabaseException {
		if (userId == null)
			throw new IllegalArgumentException("Invalid null user id");

		String sql = "SELECT * FROM users WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					User user = fromResultSet(rs, false);

					enrich(conn, Collections.singleton(user), enrichments);

					return user;
				}
			}

			return null;
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<User> getAll(Enrichment... enrichments) throws DatabaseException {
		Set<Enrichment> enrichmentSet = enrichments == null ? null
				: new LinkedHashSet<>(Arrays.asList(enrichments));
		return this.getAll(enrichmentSet);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<User> getAll(Set<Enrichment> enrichments)
			throws DatabaseException {
		String sql = "SELECT * FROM users";

		Set<User> users = new TreeSet<>();
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					users.add(fromResultSet(rs, false));
			}

			enrich(conn, users, enrichments);

			return users;
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(User... users) throws DatabaseUniqueConstraintException,
			DatabaseException {
		this.add(users == null ? null : Arrays.asList(users));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(Collection<User> users)
			throws DatabaseUniqueConstraintException, DatabaseException {
		if (users == null || users.isEmpty())
			return;

		String sql = "INSERT INTO users (login, hashed_pass, salt, email, "
				+ "first_name, last_name, active) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS)) {
			for (User user : users) {
				int index = 1;
				ps.setString(index++, user.getLogin());
				ps.setString(index++, user.getHashedPass());
				ps.setString(index++, user.getSalt());
				ps.setString(index++, user.getEmail());
				ps.setString(index++, user.getFirstName());
				ps.setString(index++, user.getLastName());
				ps.setBoolean(index++, user.isActive());
				ps.executeUpdate();

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next())
						user.setId(rs.getInt(1));
				}
			}
		} catch (SQLIntegrityConstraintViolationException notUnique) {
			throw new DatabaseUniqueConstraintException(notUnique);
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(User... users) throws DatabaseUniqueConstraintException,
			DatabaseException {
		this.update(users == null ? null : Arrays.asList(users));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Collection<User> users)
			throws DatabaseUniqueConstraintException, DatabaseException {
		if (users == null || users.isEmpty())
			return;

		// NOTE: the hashed_pass and salt values are not updated.

		String sql = "UPDATE users SET login = ?, email = ?, "
				+ "first_name = ?, last_name = ?, active = ? " + "WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			for (User user : users) {
				int index = 1;
				ps.setString(index++, user.getLogin());
				ps.setString(index++, user.getEmail());
				ps.setString(index++, user.getFirstName());
				ps.setString(index++, user.getLastName());
				ps.setBoolean(index++, user.isActive());
				ps.setInt(index++, user.getId());
				ps.executeUpdate();
			}
		} catch (SQLIntegrityConstraintViolationException notUnique) {
			throw new DatabaseUniqueConstraintException(notUnique);
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPassword(Integer userId, String hashedPass, String salt)
			throws DatabaseException {
		if (userId == null)
			throw new IllegalArgumentException("Invalid null user id");
		if (StringUtils.isBlank(hashedPass))
			throw new IllegalArgumentException("Invalid blank password");
		if (StringUtils.isBlank(salt))
			throw new IllegalArgumentException("Invalid blank salt");

		String sql = "UPDATE users SET hashed_pass = ?, salt = ? WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, hashedPass);
			ps.setString(2, salt);
			ps.setInt(3, userId);
			ps.executeUpdate();
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Integer... ids) throws DatabaseException {
		this.delete(ids == null ? null : Arrays.asList(ids));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Collection<Integer> ids) throws DatabaseException {
		if (ids == null || ids.isEmpty())
			return;

		String sql = "DELETE FROM users WHERE id = ?";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			for (Integer id : ids) {
				ps.setInt(1, id);
				ps.executeUpdate();
			}
		} catch (SQLException sqlException) {
			throw new DatabaseException(sqlException);
		}
	}

	protected void enrich(Connection conn, Set<User> users,
			Set<Enrichment> enrichments) throws DatabaseException {
		if (users == null || users.isEmpty())
			return;
		if (enrichments == null || enrichments.isEmpty())
			return;

		if (conn == null)
			throw new IllegalArgumentException("Invalid null connection");

		for (Enrichment enrichment : enrichments) {
			if (enrichment == Enrichment.ROLES)
				enrichWithRoles(conn, users);
			else
				throw new DatabaseException("Invalid enrichment specified: "
						+ enrichment);
		}
	}

	protected void enrichWithRoles(Connection conn, Set<User> users)
			throws DatabaseException {
		Set<Integer> ids = getUserIds(users);
		if (ids.isEmpty())
			return;

		Map<Integer, User> userMap = getUserMap(users);
		Map<Integer, Set<Role>> roleMap = new JdbcRoleDao().get(conn, ids);

		for (Entry<Integer, Set<Role>> entry : roleMap.entrySet()) {
			User user = userMap.get(entry.getKey());
			if (user != null)
				user.setRoles(entry.getValue());
		}
	}

	protected SortedSet<Integer> getUserIds(Set<User> users) {
		SortedSet<Integer> ids = new TreeSet<>();
		for (User user : users)
			if (user.getId() != null)
				ids.add(user.getId());
		return ids;
	}

	protected Map<Integer, User> getUserMap(Set<User> users) {
		Map<Integer, User> map = new HashMap<>();
		for (User user : users)
			if (user.getId() != null)
				map.put(user.getId(), user);
		return map;
	}
}
