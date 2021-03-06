/*
 * Copyright (C) 2013 University of Dundee & Open Microscopy Environment.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ome.util.checksum;

import java.util.zip.CRC32;

/**
 * An implementation of the {@link ChecksumProvider} interface using
 * CRC32 as the message digest algorithm. Passes in a new object of the type
 * {@link NonGuavaHashFunctionImpl} to the parent constructor.
 *
 * @author Blazej Pindelski, bpindelski at dundee.ac.uk
 * @since 5.0
 */
public final class CRC32ChecksumProviderImpl extends AbstractChecksumProvider {

    protected CRC32ChecksumProviderImpl() {
        super(new NonGuavaHashFunctionImpl(new CRC32()));
    }

}
