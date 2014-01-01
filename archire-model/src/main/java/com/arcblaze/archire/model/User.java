package com.arcblaze.archire.model;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a user of this system.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class User implements Comparable<User>, Principal {
	/**
	 * The unique id of the user.
	 */
	private Integer id;

	/**
	 * The login name for the user.
	 */
	private String login;

	/**
	 * The hashed password value for the user.
	 */
	private String hashedPass;

	/**
	 * The salt value used when hashing the user's password.
	 */
	private String salt;

	/**
	 * The user's email address.
	 */
	private String email;

	/**
	 * The user's first name.
	 */
	private String firstName;

	/**
	 * The user's last name.
	 */
	private String lastName;

	/**
	 * Whether this user is an active account or not.
	 */
	private Boolean active = true;

	/**
	 * The roles assigned to the account.
	 */
	private final Set<Role> roles = new TreeSet<>();

	/**
	 * Default constructor.
	 */
	public User() {
		// Nothing to do.
	}

	/**
	 * @return the unique id of the user
	 */
	@XmlElement
	public Integer getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the new unique user id value
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided id value is invalid
	 */
	public User setId(Integer id) {
		if (id == null)
			throw new IllegalArgumentException("Invalid null id");
		if (id < 0)
			throw new IllegalArgumentException("Invalid negative id");

		this.id = id;
		return this;
	}

	/**
	 * @return the user login value
	 */
	@Override
	@XmlTransient
	public String getName() {
		return getLogin();
	}

	/**
	 * @return the user login value
	 */
	@XmlElement
	public String getLogin() {
		return this.login;
	}

	/**
	 * @param login
	 *            the new user login value
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided login value is invalid
	 */
	public User setLogin(String login) {
		if (StringUtils.isBlank(login))
			throw new IllegalArgumentException("Invalid blank login");

		this.login = StringUtils.trim(login);
		return this;
	}

	/**
	 * @return the hashed value of the user's password
	 */
	@XmlElement
	public String getHashedPass() {
		return this.hashedPass;
	}

	/**
	 * @param hashedPass
	 *            the new hashed value of the user's password
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided hashed password value is invalid
	 */
	public User setHashedPass(String hashedPass) {
		if (StringUtils.isBlank(hashedPass))
			throw new IllegalArgumentException("Invalid blank hashed password");

		this.hashedPass = StringUtils.trim(hashedPass);
		return this;
	}

	/**
	 * @return the password salt value used when hashing the user's password
	 */
	@XmlElement
	public String getSalt() {
		return this.salt;
	}

	/**
	 * @param salt
	 *            the new password salt value used when hashing the user's
	 *            password
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided password salt value is invalid
	 */
	public User setSalt(String salt) {
		if (StringUtils.isBlank(salt))
			throw new IllegalArgumentException("Invalid blank salt");

		this.salt = StringUtils.trim(salt);
		return this;
	}

	/**
	 * @return the user's email address
	 */
	@XmlElement
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email
	 *            the new email address for the user
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided email value is invalid
	 */
	public User setEmail(String email) {
		if (StringUtils.isBlank(email))
			throw new IllegalArgumentException("Invalid blank email");

		this.email = StringUtils.trim(email);
		return this;
	}

	/**
	 * @return the user's first name
	 */
	@XmlElement
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName
	 *            the new first name value for the user
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided first name value is invalid
	 */
	public User setFirstName(String firstName) {
		if (StringUtils.isBlank(firstName))
			throw new IllegalArgumentException("Invalid blank first name");

		this.firstName = StringUtils.trim(firstName);
		return this;
	}

	/**
	 * @return the user's last name
	 */
	@XmlElement
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName
	 *            the new last name value for the user
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided last name value is invalid
	 */
	public User setLastName(String lastName) {
		if (StringUtils.isBlank(lastName))
			throw new IllegalArgumentException("Invalid blank last name");

		this.lastName = StringUtils.trim(lastName);
		return this;
	}

	/**
	 * @return the full name of the user
	 */
	@XmlElement
	public String getFullName() {
		StringBuilder name = new StringBuilder();
		name.append(getFirstName());
		name.append(" ");
		name.append(getLastName());
		return name.toString();
	}

	/**
	 * @return whether this user represents an active account in the system
	 */
	@XmlElement
	public Boolean isActive() {
		return this.active;
	}

	/**
	 * @param active
	 *            the new value indicating whether this is an active account in
	 *            the system
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided active value is invalid
	 */
	public User setActive(Boolean active) {
		if (active == null)
			throw new IllegalArgumentException("Invalid null active value");

		this.active = active;
		return this;
	}

	/**
	 * @return the privileges available to the user, based on the roles
	 */
	@XmlElement
	public String getPrivileges() {
		Set<Character> privs = new TreeSet<>();
		for (Role role : getRoles())
			privs.add(role.name().charAt(0));
		return StringUtils.join(privs, " ");
	}

	/**
	 * @return all of the roles authorized for this account
	 */
	@XmlElementWrapper
	@XmlElement(name = "role")
	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(this.roles);
	}

	/**
	 * @param newRoles
	 *            the new role values to be assigned to this account
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided roles value is invalid
	 */
	public User setRoles(Role... newRoles) {
		if (newRoles == null)
			throw new IllegalArgumentException("Invalid null roles");

		return this.setRoles(Arrays.asList(newRoles));
	}

	/**
	 * @param newRoles
	 *            the new role values to be assigned to this account
	 * 
	 * @return {@code this}
	 */
	public User setRoles(Collection<Role> newRoles) {
		synchronized (this.roles) {
			this.roles.clear();
			if (newRoles != null)
				for (Role role : newRoles)
					if (role != null)
						this.roles.add(role);
		}
		return this;
	}

	/**
	 * @param newRoles
	 *            the new role values to be assigned to this account
	 * 
	 * @return {@code this}
	 * 
	 * @throws IllegalArgumentException
	 *             if the provided roles value is invalid
	 */
	public User addRoles(Role... newRoles) {
		if (newRoles == null)
			throw new IllegalArgumentException("Invalid null roles");

		return this.addRoles(Arrays.asList(newRoles));
	}

	/**
	 * @param newRoles
	 *            the new role values to be assigned to this account
	 * 
	 * @return {@code this}
	 */
	public User addRoles(Collection<Role> newRoles) {
		synchronized (this.roles) {
			if (newRoles != null)
				for (Role role : newRoles)
					if (role != null)
						this.roles.add(role);
		}
		return this;
	}

	/**
	 * @return {@code this}
	 */
	public User clearRoles() {
		this.roles.clear();
		return this;
	}

	/**
	 * @return whether this user is a system administrator
	 */
	@XmlElement
	public boolean isAdmin() {
		return this.roles.contains(Role.ADMIN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", getId());
		builder.append("login", getLogin());
		builder.append("hashedPass", getHashedPass());
		builder.append("salt", getSalt());
		builder.append("email", getEmail());
		builder.append("firstName", getFirstName());
		builder.append("lastName", getLastName());
		builder.append("active", isActive());
		builder.append("roles", StringUtils.join(getRoles(), ","));
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			EqualsBuilder builder = new EqualsBuilder();
			builder.append(getId(), other.getId());
			builder.append(getLogin(), other.getLogin());
			// HashedPass and Salt specifically left out.
			builder.append(getEmail(), other.getEmail());
			builder.append(getFirstName(), other.getFirstName());
			builder.append(getLastName(), other.getLastName());
			builder.append(isActive(), other.isActive());
			return builder.isEquals();
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(getId());
		builder.append(getLogin());
		// HashedPass and Salt specifically left out.
		builder.append(getEmail());
		builder.append(getFirstName());
		builder.append(getLastName());
		builder.append(isActive());
		return builder.toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(User other) {
		CompareToBuilder builder = new CompareToBuilder();
		builder.append(other.isActive(), isActive());
		builder.append(getLastName(), other.getLastName());
		builder.append(getFirstName(), other.getFirstName());
		builder.append(getLogin(), other.getLogin());
		builder.append(getId(), other.getId());
		builder.append(getEmail(), other.getEmail());
		// HashedPass and Salt specifically left out.
		return builder.toComparison();
	}
}
