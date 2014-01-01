package com.arcblaze.archire.db;

import com.arcblaze.archire.config.Property;
import com.arcblaze.archire.db.dao.CompanyDao;
import com.arcblaze.archire.db.dao.RoleDao;
import com.arcblaze.archire.db.dao.UserDao;
import com.arcblaze.archire.db.dao.jdbc.JdbcCompanyDao;
import com.arcblaze.archire.db.dao.jdbc.JdbcRoleDao;
import com.arcblaze.archire.db.dao.jdbc.JdbcUserDao;

/**
 * Used to retrieve DAO instances to work with the configured back-end database.
 */
public class DaoFactory {
	/** Holds the type of back-end data store used in the cached DAOs. */
	private static DatabaseType cachedDatabaseType = null;

	private static CompanyDao cachedCompanyDao = null;
	private static UserDao cachedUserDao = null;
	private static RoleDao cachedRoleDao = null;

	/**
	 * @return an {@link CompanyDao} based on the currently configured database
	 */
	public static CompanyDao getCompanyDao() {
		DatabaseType type = DatabaseType.parse(Property.DB_TYPE.getString());
		if (type != cachedDatabaseType) {
			clearCachedDaos();
			cachedDatabaseType = type;
		}

		if (cachedCompanyDao == null) {
			if (DatabaseType.JDBC.equals(type))
				cachedCompanyDao = new JdbcCompanyDao();
			else
				throw new RuntimeException("Invalid database type: " + type);
			cachedDatabaseType = type;
		}

		return cachedCompanyDao;
	}

	/**
	 * @return an {@link UserDao} based on the currently configured database
	 */
	public static UserDao getUserDao() {
		DatabaseType type = DatabaseType.parse(Property.DB_TYPE.getString());
		if (type != cachedDatabaseType) {
			clearCachedDaos();
			cachedDatabaseType = type;
		}

		if (cachedUserDao == null) {
			if (DatabaseType.JDBC.equals(type))
				cachedUserDao = new JdbcUserDao();
			else
				throw new RuntimeException("Invalid database type: " + type);
			cachedDatabaseType = type;
		}

		return cachedUserDao;
	}

	/**
	 * @return an {@link RoleDao} based on the currently configured database
	 */
	public static RoleDao getRoleDao() {
		DatabaseType type = DatabaseType.parse(Property.DB_TYPE.getString());
		if (type != cachedDatabaseType) {
			clearCachedDaos();
			cachedDatabaseType = type;
		}

		if (cachedRoleDao == null) {
			if (DatabaseType.JDBC.equals(type))
				cachedRoleDao = new JdbcRoleDao();
			else
				throw new RuntimeException("Invalid database type: " + type);
			cachedDatabaseType = type;
		}

		return cachedRoleDao;
	}

	private static synchronized void clearCachedDaos() {
		cachedCompanyDao = null;
		cachedUserDao = null;
		cachedRoleDao = null;
	}

	/**
	 * Reset the internal DAOs and connections to be recreated when next needed.
	 */
	public static synchronized void reset() {
		clearCachedDaos();
		ConnectionManager.reset();
	}
}
