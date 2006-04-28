/*******************************************************************************
 * Copyright (c) 2004, 2005 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sybase, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.facesconfig.ui.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jst.jsf.facesconfig.common.dialogfield.ClassButtonDialogField;
import org.eclipse.jst.jsf.facesconfig.common.dialogfield.DialogField;
import org.eclipse.jst.jsf.facesconfig.common.dialogfield.IDialogFieldApplyListener;
import org.eclipse.jst.jsf.facesconfig.common.dialogfield.LayoutUtil;
import org.eclipse.jst.jsf.facesconfig.common.dialogfield.StringDialogField;
import org.eclipse.jst.jsf.facesconfig.emf.ConverterClassType;
import org.eclipse.jst.jsf.facesconfig.emf.ConverterForClassType;
import org.eclipse.jst.jsf.facesconfig.emf.ConverterIdType;
import org.eclipse.jst.jsf.facesconfig.emf.ConverterType;
import org.eclipse.jst.jsf.facesconfig.emf.DescriptionType;
import org.eclipse.jst.jsf.facesconfig.emf.DisplayNameType;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigFactory;
import org.eclipse.jst.jsf.facesconfig.emf.FacesConfigPackage;
import org.eclipse.jst.jsf.facesconfig.ui.IFacesConfigConstants;
import org.eclipse.jst.jsf.facesconfig.ui.NewEditorResourcesNLS;
import org.eclipse.jst.jsf.facesconfig.ui.page.IFacesConfigPage;
import org.eclipse.jst.jsf.facesconfig.ui.util.ModelUtil;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Bryan Yang
 * 
 */
public class ConverterGeneralSection extends AbstractFacesConfigSection {

	private StringDialogField displayNameField;

	private StringDialogField descriptionField;

	private StringDialogField converterIdField;

	private ClassButtonDialogField converterForClassField;

	private ClassButtonDialogField converterClassField;

	private ConverterGeneralAdapter converterGeneralAdapter;

	/**
	 * 
	 * @param parent
	 * @param managedForm
	 * @param page
	 * @param toolkit
	 */
	public ConverterGeneralSection(Composite parent, IManagedForm managedForm,
			IFacesConfigPage page, FormToolkit toolkit) {
		super(parent, managedForm, page, toolkit, null, null);
		getSection()
				.setText(NewEditorResourcesNLS.ConverterGeneralSection_Name);
		getSection().setDescription(
				NewEditorResourcesNLS.ConverterGeneralSection_Description);
	}

	/**
	 * 
	 */
	protected void createContents(Composite container, FormToolkit toolkit) {
		int numberOfColumns = 4;
		GridLayout layout = new GridLayout(numberOfColumns, false);
		container.setLayout(layout);
		GridData td = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(td);

		toolkit.paintBordersFor(container);
		displayNameField = new StringDialogField();
		displayNameField
				.setLabelText(NewEditorResourcesNLS.ConverterGeneralSection_Label_DisplayName);
		displayNameField.doFillIntoGrid(toolkit, container, numberOfColumns);

		displayNameField
				.setDialogFieldApplyListener(new IDialogFieldApplyListener() {

					public void dialogFieldApplied(DialogField field) {
						String newDisplayNameValue = ((StringDialogField) field)
								.getText().trim();
						Command cmd = null;
						ConverterType Converter = (ConverterType) getInput();
						EditingDomain editingDomain = getEditingDomain();
						if (Converter.getDisplayName().size() > 0) {
							DisplayNameType displayName = (DisplayNameType) Converter
									.getDisplayName().get(0);
							cmd = SetCommand.create(editingDomain, displayName,
									FacesConfigPackage.eINSTANCE
											.getDisplayNameType_TextContent(),
									newDisplayNameValue);
						} else {
							DisplayNameType displayNameType = FacesConfigFactory.eINSTANCE
									.createDisplayNameType();
							displayNameType.setTextContent(newDisplayNameValue);

							displayNameType.eAdapters().add(
									getConverterGeneralAdapter());
							cmd = AddCommand.create(editingDomain, Converter,
									FacesConfigPackage.eINSTANCE
											.getConverterType_DisplayName(),
									displayNameType);
						}
						if (cmd.canExecute()) {
							editingDomain.getCommandStack().execute(cmd);
						}
					}

				});

		descriptionField = new StringDialogField(2);
		descriptionField
				.setLabelText(NewEditorResourcesNLS.ConverterGeneralSection_Label_Description);
		descriptionField.doFillIntoGrid(toolkit, container, numberOfColumns);

		descriptionField
				.setDialogFieldApplyListener(new IDialogFieldApplyListener() {
					public void dialogFieldApplied(DialogField field) {
						String newDescriptionValue = ((StringDialogField) field)
								.getText().trim();

						newDescriptionValue = ModelUtil
								.escapeEntities(newDescriptionValue);
						Command cmd = null;
						ConverterType Converter = (ConverterType) getInput();
						EditingDomain editingDomain = getEditingDomain();
						if (Converter.getDescription().size() > 0) {
							DescriptionType description = (DescriptionType) Converter
									.getDescription().get(0);
							cmd = SetCommand.create(editingDomain, description,
									FacesConfigPackage.eINSTANCE
											.getDescriptionType_TextContent(),
									newDescriptionValue);
						} else {
							DescriptionType description = FacesConfigFactory.eINSTANCE
									.createDescriptionType();
							description.setTextContent(newDescriptionValue);

							description.eAdapters().add(
									getConverterGeneralAdapter());
							cmd = AddCommand.create(editingDomain, Converter,
									FacesConfigPackage.eINSTANCE
											.getConverterType_Description(),
									description);
						}
						if (cmd.canExecute()) {
							editingDomain.getCommandStack().execute(cmd);
						}
					}
				});

		converterIdField = new StringDialogField();
		// converterIdField.setRequired(true);
		converterIdField
				.setLabelText(NewEditorResourcesNLS.ConverterGeneralSection_Label_ConverterID);
		converterIdField.doFillIntoGrid(toolkit, container, numberOfColumns);
		converterIdField
				.setDialogFieldApplyListener(new IDialogFieldApplyListener() {
					public void dialogFieldApplied(DialogField field) {
						ConverterIdType converterIdType = FacesConfigFactory.eINSTANCE
								.createConverterIdType();

						converterIdType
								.setTextContent(((StringDialogField) field)
										.getText());

						EditingDomain editingDomain = getEditingDomain();
						List commands = new ArrayList(2);
						Command cmd1 = SetCommand.create(editingDomain,
								getInput(), FacesConfigPackage.eINSTANCE
										.getConverterType_ConverterForClass(),
								SetCommand.UNSET_VALUE);
						commands.add(cmd1);
						Command cmd2 = SetCommand.create(editingDomain,
								getInput(), FacesConfigPackage.eINSTANCE
										.getConverterType_ConverterId(),
								converterIdType);
						commands.add(cmd2);
						Command command = new CompoundCommand(commands);
						if (editingDomain != null) {
							if (command.canExecute()) {
								editingDomain.getCommandStack()
										.execute(command);
								refresh();
							}
						}
					}
				});

		converterForClassField = new ClassButtonDialogField(null);
		// converterForClassField.setRequired(true);
		converterForClassField
				.setLabelText(NewEditorResourcesNLS.ConverterGeneralSection_Label_ConverterForClass);
		converterForClassField.setProject((IProject) getPage().getEditor()
				.getAdapter(IProject.class));
		converterForClassField.doFillIntoGrid(toolkit, container,
				numberOfColumns);
		LayoutUtil.setHorizontalGrabbing(converterForClassField.getTextControl(
				toolkit, container));

		converterForClassField
				.setDialogFieldApplyListener(new IDialogFieldApplyListener() {
					public void dialogFieldApplied(DialogField field) {

						ConverterForClassType converterForClass = FacesConfigFactory.eINSTANCE
								.createConverterForClassType();
						converterForClass
								.setTextContent(((StringDialogField) field)
										.getText());
						EditingDomain editingDomain = getEditingDomain();
						List commands = new ArrayList(2);
						Command cmd1 = SetCommand.create(editingDomain,
								getInput(), FacesConfigPackage.eINSTANCE
										.getConverterType_ConverterId(),
								SetCommand.UNSET_VALUE);
						commands.add(cmd1);
						Command cmd2 = SetCommand.create(editingDomain,
								getInput(), FacesConfigPackage.eINSTANCE
										.getConverterType_ConverterForClass(),
								converterForClass);
						commands.add(cmd2);
						Command command = new CompoundCommand(commands);
						if (editingDomain != null) {
							if (command.canExecute()) {
								editingDomain.getCommandStack()
										.execute(command);
								refresh();
							}
						}

					}
				});

		converterClassField = new ClassButtonDialogField(null);
		// converterClassField.setRequired(true);
		converterClassField
				.setLabelText(NewEditorResourcesNLS.ConverterGeneralSection_Label_ConverterClass);
		converterClassField.setProject((IProject) getPage().getEditor()
				.getAdapter(IProject.class));
		converterClassField.doFillIntoGrid(toolkit, container, numberOfColumns);
		LayoutUtil.setHorizontalGrabbing(converterClassField.getTextControl(
				toolkit, container));

		converterClassField
				.setDialogFieldApplyListener(new IDialogFieldApplyListener() {
					public void dialogFieldApplied(DialogField field) {

						ConverterClassType ConverterClass = FacesConfigFactory.eINSTANCE
								.createConverterClassType();
						ConverterClass
								.setTextContent(((StringDialogField) field)
										.getText());
						EditingDomain editingDomain = getEditingDomain();
						if (editingDomain != null) {
							Command command = SetCommand.create(editingDomain,
									getInput(), FacesConfigPackage.eINSTANCE
											.getConverterType_ConverterClass(),
									ConverterClass);
							if (command.canExecute()) {
								editingDomain.getCommandStack()
										.execute(command);
							}
						}

					}
				});
		converterClassField
				.setInterface(IFacesConfigConstants.CONVERTER_INTERFACE);
	}

	/**
	 * 
	 */
	public void refreshAll() {
		refresh();
	}

	/**
	 * 
	 */
	public void refresh() {
		super.refresh();
		Object input = this.getInput();
		if (input instanceof ConverterType) {
			ConverterType Converter = (ConverterType) input;

			if (Converter.getDisplayName().size() > 0) {
				DisplayNameType displayName = (DisplayNameType) Converter
						.getDisplayName().get(0);
				displayNameField.setTextWithoutUpdate(displayName
						.getTextContent());
			} else {
				displayNameField.setTextWithoutUpdate("");
			}

			if (Converter.getDescription().size() > 0) {
				DescriptionType description = (DescriptionType) Converter
						.getDescription().get(0);
				String descriptionString = description.getTextContent();
				descriptionString = ModelUtil
						.unEscapeEntities(descriptionString);
				descriptionField.setTextWithoutUpdate(descriptionString);
			} else {
				descriptionField.setTextWithoutUpdate("");
			}

			if (Converter.getConverterId() != null) {
				converterIdField.setTextWithoutUpdate(Converter
						.getConverterId().getTextContent());
			} else {
				converterIdField.setTextWithoutUpdate("");
			}

			if (Converter.getConverterForClass() != null) {
				converterForClassField.setTextWithoutUpdate(Converter
						.getConverterForClass().getTextContent());
			} else {
				converterForClassField.setTextWithoutUpdate("");
			}

			if (Converter.getConverterClass() != null) {
				converterClassField.setTextWithoutUpdate(Converter
						.getConverterClass().getTextContent());
			} else {
				converterClassField.setTextWithoutUpdate("");
			}

		}
	}

	/**
	 * 
	 */
	public void clearAll() {
		// TODO Auto-generated method stub

	}

	/**
	 * Add ConverterGeneralAdapter onto <converter> and the first <display-name>
	 * and <description> elements.
	 */
	protected void addAdaptersOntoInput(Object newInput) {
		ConverterType converter = (ConverterType) newInput;
		if (EcoreUtil.getExistingAdapter(converter,
				ConverterGeneralSection.class) == null) {

			converter.eAdapters().add(getConverterGeneralAdapter());
		}

		if (converter.getDisplayName().size() > 0) {
			DisplayNameType displayName = (DisplayNameType) converter
					.getDisplayName().get(0);
			if (EcoreUtil.getExistingAdapter(displayName,
					ConverterGeneralSection.class) == null) {

				displayName.eAdapters().add(getConverterGeneralAdapter());
			}
		}

		if (converter.getDescription().size() > 0) {
			DescriptionType description = (DescriptionType) converter
					.getDescription().get(0);
			if (EcoreUtil.getExistingAdapter(description,
					ConverterGeneralSection.class) == null) {

				description.eAdapters().add(getConverterGeneralAdapter());
			}
		}
	}

	/**
	 * Remove ConverterGeneralAdapter from <converter> and the first
	 * <display-name> and <description> elements.
	 */
	protected void removeAdaptersFromInput(Object oldInput) {
		ConverterType converter = (ConverterType) oldInput;
		if (EcoreUtil.getExistingAdapter(converter,
				ConverterGeneralSection.class) != null) {
			converter.eAdapters().remove(getConverterGeneralAdapter());
		}
		if (converter.getDisplayName().size() > 0) {
			DisplayNameType displayName = (DisplayNameType) converter
					.getDisplayName().get(0);
			if (EcoreUtil.getExistingAdapter(displayName,
					ConverterGeneralSection.class) != null) {

				displayName.eAdapters().remove(getConverterGeneralAdapter());
			}
		}

		if (converter.getDescription().size() > 0) {
			DescriptionType description = (DescriptionType) converter
					.getDescription().get(0);
			if (EcoreUtil.getExistingAdapter(description,
					ConverterGeneralSection.class) != null) {

				description.eAdapters().remove(getConverterGeneralAdapter());
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	private Adapter getConverterGeneralAdapter() {

		if (converterGeneralAdapter == null) {
			converterGeneralAdapter = new ConverterGeneralAdapter();
		}
		return converterGeneralAdapter;
	}

	/**
	 * The adapter that will be added onto <converter> element, to listen the
	 * events of the children that are displayed in this section, notify the
	 * section to refresh.
	 * 
	 * @author sfshi
	 * 
	 */
	class ConverterGeneralAdapter extends AdapterImpl {

		public boolean isAdapterForType(Object type) {
			if (type == ConverterGeneralSection.class)
				return true;
			return false;
		}

		public void notifyChanged(Notification msg) {

			if (msg.getEventType() == Notification.ADD
					|| msg.getEventType() == Notification.REMOVE
					|| msg.getEventType() == Notification.SET) {
				if (msg.getFeature() == FacesConfigPackage.eINSTANCE
						.getConverterType_ConverterId()
						|| msg.getFeature() == FacesConfigPackage.eINSTANCE
								.getConverterType_ConverterClass()
						|| msg.getFeature() == FacesConfigPackage.eINSTANCE
								.getConverterType_ConverterForClass()
						|| msg.getFeature() == FacesConfigPackage.eINSTANCE
								.getConverterType_Description()
						|| msg.getFeature() == FacesConfigPackage.eINSTANCE
								.getConverterType_DisplayName()) {
					refresh();
				} else if (msg.getFeature() == FacesConfigPackage.eINSTANCE
						.getDisplayNameType_TextContent()
						|| msg.getFeature() == FacesConfigPackage.eINSTANCE
								.getDescriptionType_TextContent()) {
					refresh();
				}
			}
		}
	}
}
