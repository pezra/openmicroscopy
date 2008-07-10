 /*
 * treeModel.TreeEditorUI 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
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
package treeModel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

//Java imports

//Third-party libraries

//Application-internal dependencies

/** 
 * 
 *
 * @author  William Moore &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:will@lifesci.dundee.ac.uk">will@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class TreeEditorUI 
	extends JPanel {
	
	private TreeModel model;
	
	private TreeEditorControl controller;
	
	
	private TreeUI treeUI;
	
	
	public void initialise(TreeModel model, TreeEditorControl controller) {
		
		this.model = model;
		
		this.controller = controller;
		
		buildUI();
	}
	
	
	private void buildUI() {
		
		this.setLayout(new BorderLayout());
		
		treeUI = new TreeUI(model);
		
		add(treeUI, BorderLayout.CENTER);
	}

}
