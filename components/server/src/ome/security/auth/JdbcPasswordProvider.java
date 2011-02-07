/*
 *   $Id$
 *
 *   Copyright 2009 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */

package ome.security.auth;

import java.security.Permissions;

import ome.security.PasswordUtil;
import ome.security.SecuritySystem;
import ome.util.SqlAction;

import org.springframework.util.Assert;

/**
 * Central {@link PasswordProvider} which uses the "password" table in the
 * central OMERO database.
 * 
 * @author Josh Moore, josh at glencoesoftware.com
 * @since 4.0
 */

public class JdbcPasswordProvider extends ConfigurablePasswordProvider {

    final protected SqlAction sql;

    public JdbcPasswordProvider(SqlAction sql) {
        super();
        Assert.notNull(sql);
        this.sql = sql;
    }

    public JdbcPasswordProvider(SqlAction sql, boolean ignoreUnknown) {
        super(ignoreUnknown);
        Assert.notNull(sql);
        this.sql = sql;
    }

    @Override
    public boolean hasPassword(String user) {
        Long id = PasswordUtil.userId(sql, user);
        return id != null;
    }

    /**
     * Retrieves password from the database and calls
     * {@link ConfigurablePasswordProvider#comparePasswords(String, String)}.
     * Uses default logic if user is unknown.
     */
    @Override
    public Boolean checkPassword(String user, String password) {
        Long id = PasswordUtil.userId(sql, user);

        // If user doesn't exist, use the default settings for
        // #ignoreUknown.

        if (id == null) {
            return super.checkPassword(user, password);
        } else {
            String trusted = PasswordUtil.getUserPasswordHash(sql, id);
            return comparePasswords(trusted, password);
        }
    }

    @Override
    public void changePassword(String user, String password)
            throws PasswordChangeException {
        Long id = PasswordUtil.userId(sql, user);
        if (id == null) {
            throw new PasswordChangeException("Couldn't find id");
        }
        PasswordUtil.changeUserPasswordById(sql, id, password);
    }

}
