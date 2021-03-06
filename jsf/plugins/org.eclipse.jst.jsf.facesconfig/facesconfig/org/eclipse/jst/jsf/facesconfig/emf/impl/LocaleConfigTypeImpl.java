/***************************************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM Corporation - initial API and implementation
 *   Oracle Corporation - revision
 **************************************************************************************************/
package org.eclipse.jst.jsf.facesconfig.emf.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jst.jsf.facesconfig.emf.DefaultLocaleType;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigPackage;
import org.eclipse.jst.jsf.facesconfig.emf.LocaleConfigType;
import org.eclipse.jst.jsf.facesconfig.emf.SupportedLocaleType;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Locale Config Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.jsf.facesconfig.emf.impl.LocaleConfigTypeImpl#getDefaultLocale <em>Default Locale</em>}</li>
 *   <li>{@link org.eclipse.jst.jsf.facesconfig.emf.impl.LocaleConfigTypeImpl#getSupportedLocale <em>Supported Locale</em>}</li>
 *   <li>{@link org.eclipse.jst.jsf.facesconfig.emf.impl.LocaleConfigTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocaleConfigTypeImpl extends EObjectImpl implements LocaleConfigType {
    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2005, 2006 IBM Corporation and others"; //$NON-NLS-1$

    /**
	 * The cached value of the '{@link #getDefaultLocale() <em>Default Locale</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLocale()
	 * @generated
	 * @ordered
	 */
	protected DefaultLocaleType defaultLocale;

    /**
	 * The cached value of the '{@link #getSupportedLocale() <em>Supported Locale</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSupportedLocale()
	 * @generated
	 * @ordered
	 */
	protected EList supportedLocale;

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocaleConfigTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return FacesConfigPackage.Literals.LOCALE_CONFIG_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DefaultLocaleType getDefaultLocale() {
		return defaultLocale;
	}

    /**
	 * <!-- begin-user-doc -->
     * @param newDefaultLocale 
     * @param msgs 
     * @return the notification chain 
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDefaultLocale(DefaultLocaleType newDefaultLocale, NotificationChain msgs) {
		DefaultLocaleType oldDefaultLocale = defaultLocale;
		defaultLocale = newDefaultLocale;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE, oldDefaultLocale, newDefaultLocale);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultLocale(DefaultLocaleType newDefaultLocale) {
		if (newDefaultLocale != defaultLocale) {
			NotificationChain msgs = null;
			if (defaultLocale != null)
				msgs = ((InternalEObject)defaultLocale).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE, null, msgs);
			if (newDefaultLocale != null)
				msgs = ((InternalEObject)newDefaultLocale).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE, null, msgs);
			msgs = basicSetDefaultLocale(newDefaultLocale, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE, newDefaultLocale, newDefaultLocale));
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getSupportedLocale() {
		if (supportedLocale == null) {
			supportedLocale = new EObjectContainmentEList(SupportedLocaleType.class, this, FacesConfigPackage.LOCALE_CONFIG_TYPE__SUPPORTED_LOCALE);
		}
		return supportedLocale;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FacesConfigPackage.LOCALE_CONFIG_TYPE__ID, oldId, id));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE:
				return basicSetDefaultLocale(null, msgs);
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__SUPPORTED_LOCALE:
				return ((InternalEList)getSupportedLocale()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE:
				return getDefaultLocale();
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__SUPPORTED_LOCALE:
				return getSupportedLocale();
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__ID:
				return getId();
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE:
				setDefaultLocale((DefaultLocaleType)newValue);
				return;
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__SUPPORTED_LOCALE:
				getSupportedLocale().clear();
				getSupportedLocale().addAll((Collection)newValue);
				return;
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__ID:
				setId((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
		switch (featureID) {
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE:
				setDefaultLocale((DefaultLocaleType)null);
				return;
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__SUPPORTED_LOCALE:
				getSupportedLocale().clear();
				return;
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__ID:
				setId(ID_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__DEFAULT_LOCALE:
				return defaultLocale != null;
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__SUPPORTED_LOCALE:
				return supportedLocale != null && !supportedLocale.isEmpty();
			case FacesConfigPackage.LOCALE_CONFIG_TYPE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		}
		return super.eIsSet(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (id: "); //$NON-NLS-1$
		result.append(id);
		result.append(')');
		return result.toString();
	}

} //LocaleConfigTypeImpl
