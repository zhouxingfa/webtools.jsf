package org.eclipse.jst.jsf.common.internal.pde;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jst.jsf.common.JSFCommonPlugin;

/**
 * A utility base class that simplifies the reading and caching of extension
 * point information.
 * 
 * @author cbateman
 * @param <T>
 * 
 */
public abstract class AbstractRegistryReader<T>
{
    private final String _extPtNamespace;
    private final String _extPtId;
    private List<T> _extensions;
    private final AtomicBoolean _isInitialized = new AtomicBoolean(false);

    /**
     * @param extPtNamespace
     * @param extPtId
     */
    protected AbstractRegistryReader(final String extPtNamespace,
            final String extPtId)
    {
        _extPtNamespace = extPtNamespace;
        _extPtId = extPtId;
    }

    /**
     * @return the extensions
     */
    public final List<T> getExtensions()
    {
        synchronized (_isInitialized)
        {
            if (_isInitialized.compareAndSet(false, true))
            {
                SafeRunner.run(new ISafeRunnable()
                {
                    public void run() throws Exception
                    {
                        initialize();
                    }
                    
                    public void handleException(Throwable exception)
                    {
                    	// This is expected during testing if there running as JUnit test (non-plugin)
                        JSFCommonPlugin.log(exception, "Loading extension point"); //$NON-NLS-1$
                    }
                });
            }
            return _extensions;
        }
    }

    /**
     * @param extensions
     */
    protected final void internalSetExtensions(List<T> extensions)
    {
        if (_extensions != null)
        {
            throw new IllegalStateException(
                    "internalSetExtensions should be called exactly once"); //$NON-NLS-1$
        }
        _extensions = Collections.unmodifiableList(extensions);
    }

    /**
     * Called exactly once to initialize the registry.
     */
    protected abstract void initialize();

    /**
     * @return the extension point id. see IConfigurationElement.getName
     */
    protected final String getExtPtId()
    {
        return _extPtId;
    }

    /**
     * @return the namespace of the extension point. see Bundle.getSymbolicName
     */
    protected final String getExtPtNamespace()
    {
        return _extPtNamespace;
    }
}
