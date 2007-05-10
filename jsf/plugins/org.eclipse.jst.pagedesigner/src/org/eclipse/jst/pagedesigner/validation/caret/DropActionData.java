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
package org.eclipse.jst.pagedesigner.validation.caret;

import java.util.Collections;
import java.util.List;

public class DropActionData extends ActionData
{
    public DropActionData(int action, DropData data ) 
    {
        super(action, data);
    }

    public DropData getDropData()
    {
        return (DropData) getData();
    }
    
    public static class DropData
    {
        private final List    _tagIds;
        
        public DropData(List tagIds)
        {
            _tagIds = Collections.unmodifiableList(tagIds);
        }
        
        /**
         * @return the list of tag ids being dropped.  List
         * is unmodifiable
         */
        public List getTagIdentifiers()
        {
            return _tagIds;
        }
    }
}
