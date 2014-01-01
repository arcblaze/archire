package com.arcblaze.archire.model;

import org.apache.commons.lang.StringUtils;

/**
 * Describes the types of enrichment to support when retrieving model objects in
 * this system.
 */
public enum Enrichment {
	/**
	 * Used to enrich user data with the user's roles.
	 */
	ROLES,

	;

	/**
	 * Attempt to convert the provided value into an {@link Enrichment} with
	 * more flexibility than what the {@link #valueOf(String)} method provides.
	 * 
	 * @param value
	 *            the value to attempt conversion into a {@link Enrichment}
	 * 
	 * @return the identified {@link Enrichment}, or {@code null} if the
	 *         conversion fails
	 */
	public static Enrichment parse(String value) {
		for (Enrichment enrichment : values())
			if (StringUtils.equalsIgnoreCase(enrichment.name(), value))
				return enrichment;

		try {
			return Enrichment.valueOf(value);
		} catch (IllegalArgumentException badValue) {
			return null;
		}
	}
}
