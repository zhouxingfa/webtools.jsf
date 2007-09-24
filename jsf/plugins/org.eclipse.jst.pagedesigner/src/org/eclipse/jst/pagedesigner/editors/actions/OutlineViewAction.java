/*******************************************************************************
 * Copyright (c) 2001, 2007 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.pagedesigner.editors.actions;

import org.eclipse.ui.IPageLayout;

public class OutlineViewAction extends ShowViewAction 
{
    public final static String ID = "org.eclipse.jst.pagedesigner.editors.actions.OutlineViewAction"; //$NON-NLS-1$

    public OutlineViewAction() 
    {
        super(ActionsMessages
                .getString("OutlineViewAction.Menu.OutlineView")
              , IPageLayout.ID_OUTLINE); //$NON-NLS-1$
    }
}