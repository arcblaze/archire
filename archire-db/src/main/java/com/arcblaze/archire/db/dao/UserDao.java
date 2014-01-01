package com.arcblaze.archire.db.dao;

import java.util.Collection;
import java.util.Set;

import com.arcblaze.archire.db.DatabaseException;
import com.arcblaze.archire.db.DatabaseUniqueConstraintException;
import com.arcblaze.archire.model.Enrichment;
import com.arcblaze.archire.model.User;

/**
 * Performs operations on users in the system.
 */
public interface UserDao {
	/**
	 * @param login
	 *            the login value provided by the user
	 * 
	 * @return the requested user, possibly {@code null} if not found
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided parameters are invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public User getLogin(String login) throws DatabaseException;

	/**
	 * @param userId
	 *            the unique id of the user to retrieve
	 * @param enrichments
	 *            the types of additional data to include in the returned users
	 * 
	 * @return the requested user, possibly {@code null} if not found
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public User get(Integer userId, Enrichment... enrichments)
			throws DatabaseException;

	/**
	 * @param userId
	 *            the unique id of the user to retrieve
	 * @param enrichments
	 *            the types of additional data to include in the returned users
	 * 
	 * @return the requested user, possibly {@code null} if not found
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public User get(Integer userId, Set<Enrichment> enrichments)
			throws DatabaseException;

	/**
	 * @param enrichments
	 *            the types of additional data to include in the returned users
	 * 
	 * @return all available users, possibly empty but never {@code null}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public Set<User> getAll(Enrichment... enrichments) throws DatabaseException;

	/**
	 * @param enrichments
	 *            the types of additional data to include in the returned users
	 * 
	 * @return all available users, possibly empty but never {@code null}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public Set<User> getAll(Set<Enrichment> enrichments)
			throws DatabaseException;

	/**
	 * @param users
	 *            the new users to be added
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseUniqueConstraintException
	 *             if there is a problem adding the user due to a unique
	 *             constraint violation
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void add(User... users) throws DatabaseUniqueConstraintException,
			DatabaseException;

	/**
	 * @param users
	 *            the new users to be added
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseUniqueConstraintException
	 *             if there is a problem adding the user due to a unique
	 *             constraint violation
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void add(Collection<User> users)
			throws DatabaseUniqueConstraintException, DatabaseException;

	/**
	 * Save property updates within the provided users to the database. Note
	 * that this does not save any password changes, the
	 * {@link #setPassword(Integer, String, String)} method is used for that.
	 * 
	 * @param users
	 *            the users to be updated
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseUniqueConstraintException
	 *             if there is a problem adding the user due to a unique
	 *             constraint violation
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void update(User... users) throws DatabaseUniqueConstraintException,
			DatabaseException;

	/**
	 * Save property updates within the provided users to the database. Note
	 * that this does not save any password changes, the
	 * {@link #setPassword(Integer, String, String)} method is used for that.
	 * 
	 * @param users
	 *            the users to be updated
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseUniqueConstraintException
	 *             if there is a problem adding the user due to a unique
	 *             constraint violation
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void update(Collection<User> users)
			throws DatabaseUniqueConstraintException, DatabaseException;

	/**
	 * Save property updates within the provided users to the database. Note
	 * that this does not save any password changes.
	 * 
	 * @param userId
	 *            the unique id of the user whose password is being reset
	 * @param hashedPass
	 *            the new hashed password value to set for the user
	 * @param salt
	 *            the salt value to use when hashing the user's password
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void setPassword(Integer userId, String hashedPass, String salt)
			throws DatabaseException;

	/**
	 * @param userIds
	 *            the unique ids of the users to be deleted
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void delete(Integer... userIds) throws DatabaseException;

	/**
	 * @param userIds
	 *            the unique ids of the users to be deleted
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id is invalid
	 * @throws DatabaseException
	 *             if there is a problem communicating with the database
	 */
	public void delete(Collection<Integer> userIds) throws DatabaseException;
}
