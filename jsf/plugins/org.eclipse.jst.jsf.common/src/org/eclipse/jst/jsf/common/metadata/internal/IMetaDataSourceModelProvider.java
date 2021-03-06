/*******************************************************************************
 * Copyright (c) 2007 Oracle Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Oracle - initial API and implementation
 *    
 ********************************************************************************/
package org.eclipse.jst.jsf.common.metadata.internal;

import org.eclipse.core.runtime.IAdaptable;


/**
 * Provides a source of metadata that can be transformed into a merged standard model
 * LIKELY TO CHANGE
 */
public interface IMetaDataSourceModelProvider extends IAdaptable{
	/**
	 * @return the source model
	 */
	public Object getSourceModel();
	/**
	 * @return the IMetaDataLocator instance that located this model provider instance
	 */
	public IMetaDataLocator getLocator();
	/**
	 * @param locator instance that located this model provider instance
	 */
	public void setLocator(IMetaDataLocator locator);
}
