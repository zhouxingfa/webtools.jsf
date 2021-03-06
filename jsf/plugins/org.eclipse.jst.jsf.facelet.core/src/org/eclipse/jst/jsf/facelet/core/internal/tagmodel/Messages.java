/*******************************************************************************
 * Copyright (c) 2008 Oracle Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cameron Bateman - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.facelet.core.internal.tagmodel;

import org.eclipse.osgi.util.NLS;

/**
 * Externalizable string support.
 * 
 * @author cbateman
 * 
 */
public final class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.eclipse.jst.jsf.facelet.core.internal.tagmodel.messages"; //$NON-NLS-1$
    /**
     * 
     */
    public static String        FaceletTaglibWithLibraryClass_TAG_LIBRARY_TYPE_DESCRIPTION;
    /**
     * 
     */
    public static String        FaceletTaglibWithTags_TAG_LIBRARY_WITH_TAGS_TYPE_DESCRIPTION;
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
        // no instantiation
    }
}
