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

package org.eclipse.jst.jsf.facesconfig.ui.pageflow.editpart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jst.jsf.facesconfig.ui.EditorPlugin;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.model.Pageflow;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.synchronization.FC2PFTransformer;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.synchronization.PFBatchAdapter;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.util.PageflowAnnotationUtil;
import org.eclipse.jst.jsf.facesconfig.ui.preference.GEMPreferences;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.PlatformUI;

/**
 * The container editr part for the whole pageflow, which uses the
 * WindowFigure(GEM) as the container figure.
 * 
 */
public class PageflowEditPart extends PageflowContainerEditPart implements
		LayerConstants, ILayerPanePreference {
	/** The seperation for the two possible coincided connections */
	private static final int CONNECTION_SEPERATION = 20;

	private int connectionStyle = -1;

	/**
	 * Creates a new PageflowEditPart instance.
	 * 
	 * @param pageflow -
	 *            pageflow model
	 */
	protected PageflowEditPart(Pageflow pageflow) {
		super(pageflow);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		// layer.setOpaque(true);
		layer.setLayoutManager(new FreeformLayout());
		return layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();

		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		// installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == SnapToHelper.class) {
			IPreferenceStore store = EditorPlugin.getDefault()
					.getPreferenceStore();

			List snapStrategies = new ArrayList();
			Boolean bRulerVisible = (Boolean) getViewer().getProperty(
					RulerProvider.PROPERTY_RULER_VISIBILITY);
			if (bRulerVisible != null && bRulerVisible.booleanValue()) {
				snapStrategies.add(new SnapToGuides(this));
			}

			boolean bSnapToGeometry = store
					.getBoolean(GEMPreferences.SNAP_TO_GEOMETRY);
			if (bSnapToGeometry) {
				snapStrategies.add(new SnapToGeometry(this));
			}
			boolean bSnapToGrid = store.getBoolean(GEMPreferences.SNAP_TO_GRID);
			if (bSnapToGrid) {
				snapStrategies.add(new SnapToGrid(this));
			}

			if (snapStrategies.size() == 0) {
				return null;
			}
			if (snapStrategies.size() == 1) {
				return snapStrategies.get(0);
			}

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++) {
				ss[i] = (SnapToHelper) snapStrategies.get(i);
			}
			return new CompoundSnapToHelper(ss);
		}
		return super.getAdapter(key);
	}

	/**
	 * Returns the Pageflow.
	 * 
	 * @return the pageflow
	 */
	public Pageflow getPageflow() {
		return (Pageflow) getPageflowElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		List allChildren = new LinkedList();
		Iterator it;

		it = getPageflow().getNodes().iterator();
		while (it.hasNext()) {
			allChildren.add(it.next());
		}

		return allChildren;
	}

	public Adapter createEMFAdapter() {
		return new PFBatchAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see Adapter#notifyChanged(Notification)
			 */
			public void doNotifyChanged(Notification notification) {
				int type = notification.getEventType();

				// int featureId = notification.getFeatureID(
				// PageflowPackage.class );
				switch (type) {
				case Notification.ADD:
				case Notification.ADD_MANY:
				case Notification.REMOVE:
				case Notification.REMOVE_MANY:
					if (Thread.currentThread() == PlatformUI.getWorkbench().getDisplay().getThread()) {
						refreshChildren();
					}
					else {
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
	
							public void run() {
								refreshChildren();		
							}
							
						});
					}
					break;

				case Notification.SET:
					if (Thread.currentThread() == PlatformUI.getWorkbench().getDisplay().getThread()) {
						refreshChildren();
						refreshVisuals();
					}
					else {
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				
							public void run() {
								refreshChildren();	
								refreshVisuals();
							}
							
						});
					}
					break;
				case FC2PFTransformer.MY_NOTIFICATION_TYPE1:
					restore((Pageflow) getModel());
					if (Thread.currentThread() == PlatformUI.getWorkbench().getDisplay().getThread()) {
						refreshChildren();
						refreshVisuals();
					}
					else {
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				
							public void run() {
								refreshChildren();	
								refreshVisuals();
							}
							
						});
					}
					break;
				// restore all children
				case FC2PFTransformer.MY_NOTIFICATION_TYPE:
					// stop all children
					postPone((Pageflow) getModel());
					break;
				}
			}

			protected void restore(Pageflow pageflow) {
				TreeIterator nodes;
				// restore
				nodes = pageflow.eAllContents();
				while (nodes.hasNext()) {
					setPostpone((EObject) nodes.next(), false);
				}
				setPostpone(pageflow, false);
			}

			void setPostpone(EObject node, boolean enable) {
				List adapters = node.eAdapters();
				for (int i = 0; i < adapters.size(); i++) {
					if (adapters.get(i) instanceof PFBatchAdapter) {
						((PFBatchAdapter) adapters.get(i))
								.setNeedPostpone(enable);
					}
				}
			}

			protected void postPone(Pageflow pageflow) {
				TreeIterator nodes;
				// postpone
				nodes = pageflow.eAllContents();
				while (nodes.hasNext()) {
					setPostpone((EObject) nodes.next(), true);
				}
				setPostpone(pageflow, true);
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		super.refreshVisuals();
		for (int i = 0; i < this.getChildren().size(); i++) {
			((AbstractEditPart) getChildren().get(i)).refresh();
		}
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(CONNECTION_LAYER);

		if (cLayer.getConnectionRouter() == null) {
			setConnectionRouterStyle(getConnectionRouterStyle());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	protected void refreshChildren() {
		super.refreshChildren();
		PageflowAnnotationUtil.validatePageflow(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.IFigurePreference#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font f) {
		getFigure().setFont(f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.ILayerPanePreference#setConnectionRouterStyle(int)
	 */
	public void setConnectionRouterStyle(int style) {
		//TODO does this need to be execed on UI thread only???
		connectionStyle = style;
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(CONNECTION_LAYER);
		if (style == ILayerPanePreference.LINE_ROUTING_MANHATTAN) {
			FanRouter router = new FanRouter();
			router.setSeparation(CONNECTION_SEPERATION);
			router.setNextRouter(new ManhattanConnectionRouter());
			cLayer.setConnectionRouter(router);
		} else if (style == ILayerPanePreference.LINE_ROUTING_MANUAL) {
			FanRouter router = new FanRouter();
			router.setSeparation(CONNECTION_SEPERATION);
			router.setNextRouter(new BendpointConnectionRouter());
			cLayer.setConnectionRouter(router);
		}
	}

	/**
	 * get the foreground color from preference
	 * @return the router style value
	 * 
	 */
	public int getConnectionRouterStyle() {
		if (this.connectionStyle == -1) {
			IPreferenceStore store = EditorPlugin.getDefault()
					.getPreferenceStore();
			String connectionStyle_ = store
					.getString(GEMPreferences.LINE_ROUTING);

			if (GEMPreferences.LINE_ROUTING_MANHATTAN.equals(connectionStyle_)) {
				this.connectionStyle = ILayerPanePreference.LINE_ROUTING_MANHATTAN;
			} else {
				this.connectionStyle = ILayerPanePreference.LINE_ROUTING_MANUAL;
			}
		}
		return this.connectionStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.IFigurePreference#setForegroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setForegroundColor(Color c) {
		getFigure().setForegroundColor(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.IFigurePreference#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color c) {
		// getLayer(PRIMARY_LAYER).setOpaque(true);
		getLayer(PRIMARY_LAYER).setBackgroundColor(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.IFigurePreference#setGridVisible(boolean)
	 */
	public void setGridVisible(boolean bVisible) {
		GridLayer gl = (GridLayer) getLayer(GRID_LAYER);
		gl.setVisible(bVisible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.IFigurePreference#setGridSpacing(org.eclipse.draw2d.geometry.Dimension)
	 */
	public void setGridSpacing(Dimension d) {
		GridLayer gl = (GridLayer) getLayer(GRID_LAYER);
		gl.setSpacing(d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sybase.stf.jmt.editors.pageflow.editparts.ILayerPanePreference#setGridForegroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setGridForegroundColor(Color c) {
		GridLayer gl = (GridLayer) getLayer(GRID_LAYER);
		gl.setForegroundColor(c);
	}

    protected void performDirectEdit() {
        // do nothing
    }

    protected void performOpen() {
        // do nothing
    }

    public void addAnnotation(Annotation annotation) {
        // do nothing
        
    }

    public void removeAnnotation() {
        // do nothing
    }
}
