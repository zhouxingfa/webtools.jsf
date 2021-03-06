/*******************************************************************************
 * Copyright (c) 2004, 2006 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sybase, Inc. - initial API and implementation
 *******************************************************************************/


package org.eclipse.jst.jsf.facesconfig.ui.dialog;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.jsf.common.ui.internal.dialogfield.ClassButtonDialogField;
import org.eclipse.jst.jsf.common.ui.internal.dialogfield.DialogField;
import org.eclipse.jst.jsf.common.ui.internal.dialogfield.DialogFieldBase;
import org.eclipse.jst.jsf.common.ui.internal.dialogfield.LayoutUtil;
import org.eclipse.jst.jsf.common.ui.internal.guiutils.SWTUtils;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigFactory;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigPackage;
import org.eclipse.jst.jsf.facesconfig.emf.KeyClassType;
import org.eclipse.jst.jsf.facesconfig.emf.KeyType;
import org.eclipse.jst.jsf.facesconfig.emf.MapEntriesType;
import org.eclipse.jst.jsf.facesconfig.emf.MapEntryType;
import org.eclipse.jst.jsf.facesconfig.emf.NullValueType;
import org.eclipse.jst.jsf.facesconfig.emf.ValueClassType;
import org.eclipse.jst.jsf.facesconfig.emf.ValueType;
import org.eclipse.jst.jsf.facesconfig.ui.EditorMessages;
import org.eclipse.jst.jsf.facesconfig.ui.EditorPlugin;
import org.eclipse.jst.jsf.facesconfig.ui.section.AbstractFacesConfigSection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 
 * @author sfshi
 * 
 */
public class EditMapEntriesDialog extends Dialog {

	private static final int MIN_DIALOG_WIDTH = 300;

	private static final int TABLE_DEFAULT_HEIGHT = 160;

	private ClassButtonDialogField keyClassField;

	private ClassButtonDialogField valueClassField;

	private IProject project;

	private TableViewer tableViewer;

	private Button removeButton;

	private Button editButton;

	private MapEntriesType mapEntries;

	private AbstractFacesConfigSection section;

	/**
	 * 
	 * @param parentShell
	 * @param mapEntries
	 *            the map-entries element that working on.
	 * @param section 
	 */
	public EditMapEntriesDialog(Shell parentShell, MapEntriesType mapEntries,
			AbstractFacesConfigSection section) {
		super(parentShell);
		this.mapEntries = mapEntries;
		this.section = section;
	}

	/*
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(EditorMessages.EditMapEntriesDialog_EditingMapEntries);
	}

	/*
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		GridData data = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(data);

		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		// gl.marginHeight = 0;
		container.setLayout(gl);
		createKeyValueSection(container);
		createTableSection(container);

		initFields();
		return container;
	}

	private void initFields() {

		if (mapEntries.getKeyClass() != null)
			keyClassField.setText(mapEntries.getKeyClass().getTextContent());

		if (mapEntries.getValueClass() != null)
			valueClassField
					.setText(mapEntries.getValueClass().getTextContent());

		tableViewer.setInput(mapEntries);
	}

	private void createKeyValueSection(Composite parent) {
		keyClassField = new ClassButtonDialogField(getProject());
		keyClassField
				.setLabelText(EditorMessages.InitializationSection_MapType_KeyClass);

		valueClassField = new ClassButtonDialogField(getProject());

		valueClassField
				.setLabelText(EditorMessages.InitializationSection_MapType_ValueClass);

		Composite typeSelectionSection = SWTUtils.createComposite(parent,
				SWT.NONE);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		typeSelectionSection.setLayoutData(gd);

		int numberOfColumns = 4;
		GridLayout gl = new GridLayout(numberOfColumns, false);
		typeSelectionSection.setLayout(gl);

		keyClassField.doFillIntoGrid(null, typeSelectionSection,
				numberOfColumns);
		LayoutUtil.setHorizontalGrabbing(keyClassField.getTextControl(null,
				parent));

		valueClassField.doFillIntoGrid(null, typeSelectionSection,
				numberOfColumns);
	}

	/**
	 * 
	 * @param parent
	 */
	private void createTableSection(Composite parent) {
		Composite mapValueSection = SWTUtils.createComposite(parent, SWT.NONE);

		GridData gd = new GridData(GridData.FILL_BOTH);
		mapValueSection.setLayoutData(gd);

		int numberOfColumns = 3;
		GridLayout gl = new GridLayout(numberOfColumns, false);
		mapValueSection.setLayout(gl);

		DialogField valuesTitle = new DialogFieldBase();
		valuesTitle
				.setLabelText(EditorMessages.InitializationSection_MapTable_Title);    

		valuesTitle.doFillIntoGrid(null, mapValueSection, numberOfColumns);

		Table mapTable = new Table(mapValueSection, SWT.FULL_SELECTION
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		mapTable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateButtonsStatus();

			}

		});
		mapTable.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				if (((IStructuredSelection) tableViewer.getSelection()).size() > 0)
					editButtonSelected(null);
			}
		});
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		gd.heightHint = TABLE_DEFAULT_HEIGHT;
		mapTable.setLayoutData(gd);
		mapTable.setHeaderVisible(true);
		mapTable.setLinesVisible(true);
		TableLayout layout = new TableLayout();
		mapTable.setLayout(layout);

		TableColumn keyCol = new TableColumn(mapTable, SWT.NONE);
		keyCol
				.setText(EditorMessages.InitializationSection_MapTable_Key);
		layout.addColumnData(new ColumnWeightData(1, true));
		keyCol.setResizable(true);

		TableColumn valueCol = new TableColumn(mapTable, SWT.NONE);
		layout.addColumnData(new ColumnWeightData(1, true));

		valueCol
				.setText(EditorMessages.InitializationSection_MapTable_Value);
		valueCol.setResizable(true);

		tableViewer = new TableViewer(mapTable);
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(
				getAdapterFactory()));
		tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(
				getAdapterFactory()));

		tableViewer.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				return FacesConfigPackage.eINSTANCE.getMapEntryType()
						.isInstance(element);
			}
		});

		// map's operation buttons
		Composite operationContainer = null;

		operationContainer = SWTUtils
				.createComposite(mapValueSection, SWT.NONE);

		gd = new GridData(GridData.FILL_VERTICAL);
		operationContainer.setLayoutData(gd);
		gl = new GridLayout();
		operationContainer.setLayout(gl);

		Button addButton = SWTUtils.createPushButton(operationContainer,
				EditorMessages.UI_Button_Add_more);

		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gd.grabExcessHorizontalSpace = false;
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addButtonSelected(e);
			}
		});

		editButton = SWTUtils.createPushButton(operationContainer,
				EditorMessages.UI_Button_Edit_more);

		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gd.grabExcessHorizontalSpace = false;
		editButton.setLayoutData(gd);
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editButtonSelected(e);
			}

		});
		removeButton = SWTUtils.createPushButton(operationContainer,
				EditorMessages.UI_Button_Remove);

		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gd.grabExcessHorizontalSpace = false;
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				removeButtonSelected();
			}

		});
		editButton.setEnabled(false);
		removeButton.setEnabled(false);
	}
	
	/**
	 * @return the current project
	 */
	public IProject getProject() {
		if (project == null) {
			project = (IProject) section.getPage().getEditor().getAdapter(
					IProject.class);
		}
		return project;
	}
	private AdapterFactory getAdapterFactory() {
		return section.getAdapterFactory();
	}

	private void addButtonSelected(SelectionEvent e) {
		AddEditMapEntryDialog dialog = new AddEditMapEntryDialog(EditorPlugin
				.getActiveShell(), true);
		if (dialog.open() == Dialog.OK) {
			MapEntryType mapEntry = FacesConfigFactory.eINSTANCE
					.createMapEntryType();
			if (dialog.getKey() != null) {
				KeyType key = FacesConfigFactory.eINSTANCE.createKeyType();
				key.setTextContent(dialog.getKey());
				mapEntry.setKey(key);
			}
			if (dialog.isNullValue()) {
				NullValueType nullValue = FacesConfigFactory.eINSTANCE
						.createNullValueType();
				mapEntry.setNullValue(nullValue);
			} else {
				ValueType value = FacesConfigFactory.eINSTANCE
						.createValueType();
				value.setTextContent(dialog.getValue());
				mapEntry.setValue(value);
			}

			mapEntries.getMapEntry().add(mapEntry);
		}

	}

	private void editButtonSelected(SelectionEvent e) {
		MapEntryType mapEntry = (MapEntryType) ((IStructuredSelection) tableViewer
				.getSelection()).getFirstElement();
		AddEditMapEntryDialog dialog = new AddEditMapEntryDialog(EditorPlugin
				.getActiveShell(), false);
		if (mapEntry.getKey() != null)
			dialog.setKey(mapEntry.getKey().getTextContent());
		if (mapEntry.getNullValue() != null)
			dialog.setNullValue(true);
		else if (mapEntry.getValue() != null)
			dialog.setValue(mapEntry.getValue().getTextContent());

		if (dialog.open() == Dialog.OK) {
			if (mapEntry.getKey() != null)
				mapEntry.getKey().setTextContent(dialog.getKey());
			else {
				KeyType keyType = FacesConfigFactory.eINSTANCE.createKeyType();
				keyType.setTextContent(dialog.getKey());
				mapEntry.setKey(keyType);
			}
			if (dialog.isNullValue()) {
				if (mapEntry.getValue() != null)
					EcoreUtil.remove(mapEntry.getValue());
				// mapEntry.eUnset(FacesConfigPackage.eINSTANCE.getMapEntryType_Value());
				mapEntry.setNullValue(FacesConfigFactory.eINSTANCE
						.createNullValueType());

			} else {
				if (mapEntry.getNullValue() != null)
					EcoreUtil.remove(mapEntry.getNullValue());
				if (mapEntry.getValue() != null)
					mapEntry.getValue().setTextContent(dialog.getValue());
				else {
					ValueType value = FacesConfigFactory.eINSTANCE
							.createValueType();
					value.setTextContent(dialog.getValue());
					mapEntry.setValue(value);
				}

			}
			tableViewer.refresh(mapEntry);
		}

	}

	private void removeButtonSelected() {
		MapEntryType mapEntry = (MapEntryType) ((IStructuredSelection) tableViewer
				.getSelection()).getFirstElement();
		mapEntries.getMapEntry().remove(mapEntry);
		tableViewer.refresh();
		updateButtonsStatus();
	}

	/**
	 * update the button status
	 */
	public void updateButtonsStatus() {
		if (((IStructuredSelection) tableViewer.getSelection()).size() > 0) {
			editButton.setEnabled(true);
			removeButton.setEnabled(true);
		} else {
			editButton.setEnabled(false);
			removeButton.setEnabled(false);
		}
	}

	/**
	 * 
	 */
	protected void okPressed() {
		KeyClassType keyClass = FacesConfigFactory.eINSTANCE
				.createKeyClassType();
		keyClass.setTextContent(this.keyClassField.getText().trim());
		mapEntries.setKeyClass(keyClass);
		ValueClassType valueClass = FacesConfigFactory.eINSTANCE
				.createValueClassType();
		valueClass.setTextContent(this.valueClassField.getText().trim());
		mapEntries.setValueClass(valueClass);
		super.okPressed();
	}

	/*
	 * @see org.eclipse.jface.window.Window#getInitialSize()
	 */
	protected Point getInitialSize() {
		Point shellSize = super.getInitialSize();
		return new Point(Math.max(
				convertHorizontalDLUsToPixels(MIN_DIALOG_WIDTH), shellSize.x),
				shellSize.y);
	}

	/**
	 * @param project
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * @return the map entries
	 */
	public MapEntriesType getMapEntries() {
		return mapEntries;
	}

	/**
	 * @param mapEntries
	 */
	public void setMapEntries(MapEntriesType mapEntries) {
		this.mapEntries = mapEntries;
	}
}
