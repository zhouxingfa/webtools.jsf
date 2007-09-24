/*******************************************************************************
 * Copyright (c) 2006 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sybase, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.pagedesigner.converter;

/**
 * @author mengbo
 * @version 1.5
 */
public class PreferenceReader {
	public final static int FULL_EXPRESSION_TYPE = 0;

	public final static int LAST_EXPRESSION_TYPE = 1;

	public final static int REAL_VALUE_TYPE = 2;

	public static int getMapValueType() {
		return LAST_EXPRESSION_TYPE;
	}
}