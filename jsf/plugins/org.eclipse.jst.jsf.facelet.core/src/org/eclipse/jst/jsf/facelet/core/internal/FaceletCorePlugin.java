package org.eclipse.jst.jsf.facelet.core.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FaceletCorePlugin extends EMFPlugin
{

    /**
     * Keep track of the singleton.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final FaceletCorePlugin INSTANCE = new FaceletCorePlugin();

    /**
     * The plug-in ID
     */
    public static final String       PLUGIN_ID = "org.eclipse.jst.jsf.facelet.core"; //$NON-NLS-1$

    // The shared instance
    private static Implementation plugin;

    /**
     * Create the instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FaceletCorePlugin()
    {
        super
          (new ResourceLocator [] 
           {
           });
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the singleton instance.
     * @generated
     */
    @Override
    public ResourceLocator getPluginResourceLocator()
    {
        return plugin;
    }

    /**
     * Returns the singleton instance of the Eclipse plugin.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the singleton instance.
     * @generated
     */
    public static Implementation getPlugin()
    {
        return plugin;
    }

    /**
     * The actual implementation of the Eclipse <b>Plugin</b>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static class Implementation extends EclipsePlugin
    {
        private BundleContext context = null;
        /**
         * Creates an instance.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public Implementation()
        {
            super();

            // Remember the static instance.
            //
            plugin = this;
        }

        @Override
        public void start(BundleContext bundleContext) throws Exception
        {
            super.start(bundleContext);
            this.context = bundleContext;
        }
        
        /**
         * @return the bundle context.
         */
        public BundleContext getBundleContext()
        {
            return this.context;
        }
    }

    
    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Implementation getDefault()
    {
        if (plugin == null)
        {
            return new Implementation();
        }
        return plugin;
    }

    /**
     * @param logMessage
     * @param exception
     */
    public static void log(final String logMessage, final Throwable exception)
    {
        final IStatus status = new Status(IStatus.ERROR, PLUGIN_ID,
                logMessage, exception);
        getDefault().getLog().log(status);
    }

}
