/*******************************************************************************
 * Copyright (c) 2006 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http:// www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Sybase, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.pagedesigner.jsf.ui.actions;

import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;

import org.eclipse.jst.pagedesigner.IJMTConstants;
import org.eclipse.jst.pagedesigner.IJSFConstants;
import org.eclipse.jst.pagedesigner.properties.attrgroup.DialogUtil;

/**
 * @author mengbo
 * @version 1.5
 */
public class AddSelectItemAction extends JSFAddChildAction
{

    /**
     * @param text
     * @param parentNode
     */
    public AddSelectItemAction(IDOMElement parentNode)
    {
        super(ActionsResources.getString("AddSelectItemAction.ActionLabel.SelectItem"), parentNode); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        DialogUtil.createSubElement(getShell(), getParentElement(), IJMTConstants.URI_JSF_CORE,
                IJSFConstants.TAG_SELECTITEM, new String[] { IJSFConstants.ATTR_ITEMLABEL, IJSFConstants.ATTR_ITEMVALUE});
    }
}
