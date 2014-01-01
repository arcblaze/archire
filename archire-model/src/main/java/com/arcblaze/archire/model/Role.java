package com.arcblaze.archire.model;

import org.apache.commons.lang.StringUtils;

/**
 * Describes the personnel category type of an account in this system.
 */
public enum Role {
	/**
	 * A system administrator.
	 */
	ADMIN,

	/**
	 * A user with an account.
	 */
	USER,

	;

	/**
	 * Attempt to convert the provided value into a {@link Role} with more
	 * flexibility than what the {@link #valueOf(String)} method provides.
	 * 
	 * @param value
	 *            the value to attempt conversion into a {@link Role}
	 * 
	 * @return the identified {@link Role}, or {@code null} if the conversion
	 *         fails
	 */
	public static Role parse(String value) {
		for (Role role : values())
			if (StringUtils.equalsIgnoreCase(role.name(), value))
				return role;

		try {
			return Role.valueOf(value);
		} catch (IllegalArgumentException badValue) {
			return null;
		}
	}
}
