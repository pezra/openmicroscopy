/*
 * org.openmicroscopy.shoola.env.data.views.calls.ObjectFinder 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2007 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.env.data.views.calls;




//Java imports

//Third-party libraries

//Application-internal dependencies
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openmicroscopy.shoola.env.data.OmeroDataService;
import org.openmicroscopy.shoola.env.data.model.DeletableObject;
import org.openmicroscopy.shoola.env.data.util.SearchDataContext;
import org.openmicroscopy.shoola.env.data.util.SecurityContext;
import org.openmicroscopy.shoola.env.data.views.BatchCall;
import org.openmicroscopy.shoola.env.data.views.BatchCallTree;

/** 
 * Searches for objects.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class ObjectFinder 
	extends BatchCallTree
{

	 /** The root nodes of the found trees. */
    private Object result;
    
    /** The search call. */
    private BatchCall loadCall;
    
    /** The security context.*/
    private List<SecurityContext> ctx;
    
    /** The context of the search.*/
    private SearchDataContext searchContext;
    
    /**
     * Creates a {@link BatchCall} to retrieve the data
     * 
     * @param ctx The security context.
     * @return The {@link BatchCall}.
     */
    private BatchCall searchFor(final SecurityContext ctx)
    {
        return new BatchCall("Searching") {
            public void doCall() throws Exception
            {
                OmeroDataService os = context.getDataService();
                Map<SecurityContext, Object> r = new HashMap<SecurityContext, 
                Object>();
                r.put(ctx, os.advancedSearchFor(ctx, searchContext));
                result = r;
            }
        };
    }
	
	/**
     * Adds the {@link #loadCall} to the computation tree.
     * @see BatchCallTree#buildTree()
     */
    protected void buildTree()
    { 
    	Iterator<SecurityContext> i = ctx.iterator();
    	while (i.hasNext()) {
			final SecurityContext sc = i.next();
			add(searchFor(sc));
		}
    }


    /**
     * Returns the server call-handle to the computation.
     * 
     * @return See above.
     */
    protected Object getPartialResult() { return result; }
    
    /**
     * Returns the result of the search.
     * @see BatchCallTree#getResult()
     */
    protected Object getResult() { return null; }
    
    /**
     * Creates a new instance.
     * 
     * @param ctx The security context.
     * @param searchContext The context of the search.
     */
    public ObjectFinder(List<SecurityContext> ctx,
    		SearchDataContext searchContext)
    {
    	this.ctx = ctx;
    	this.searchContext = searchContext;
    }
    
}
