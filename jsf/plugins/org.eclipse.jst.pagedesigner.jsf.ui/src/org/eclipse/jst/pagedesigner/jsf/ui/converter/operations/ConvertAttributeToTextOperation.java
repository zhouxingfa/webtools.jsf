/*******************************************************************************
 * Copyright (c) 2005 Oracle Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ian Trimble - initial API and implementation
 *******************************************************************************/ 
package org.eclipse.jst.pagedesigner.jsf.ui.converter.operations;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ConvertAttributeToTextOperation extends AbstractTransformOperation {

	private String attributeName;
	private boolean removeAttribute;

	public ConvertAttributeToTextOperation(String attributeName) {
		this(attributeName, false);
	}

	public ConvertAttributeToTextOperation(String attributeName, boolean removeAttribute) {
		this.attributeName = attributeName;
		this.removeAttribute = removeAttribute;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jst.pagedesigner.jsf.ui.converter.operations.AbstractTransformOperation#transform(org.w3c.dom.Element, org.w3c.dom.Element)
	 */
	public Element transform(Element srcElement, Element curElement) {
		if (srcElement != null && curElement != null) {
			String text = srcElement.getAttribute(attributeName);
			if (text != null && text.length() > 0) {
				Document document = curElement.getOwnerDocument();
				if (document != null) {
					Text textNode = document.createTextNode(text);
					curElement.appendChild(textNode);
				}
			}
			if (removeAttribute) {
				curElement.removeAttribute(attributeName);
			}
		}
		return curElement;
	}

}
