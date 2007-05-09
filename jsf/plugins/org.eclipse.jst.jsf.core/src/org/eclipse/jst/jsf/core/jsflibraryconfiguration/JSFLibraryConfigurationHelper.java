package org.eclipse.jst.jsf.core.jsflibraryconfiguration;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.jsf.core.internal.JSFCorePlugin;
import org.eclipse.jst.jsf.core.jsflibraryconfiguration.internal.JSFLibraryReferenceFacadeFactory;

/**
 * Helper class for adopters needing to deal with JSF Library References
 * @since WTP JSF 1.0
 */
public final class JSFLibraryConfigurationHelper {
	/**
	 * container id for JSF Library Classpath Containers 
	 */
	public static final String JSF_LIBRARY_CP_CONTAINER_ID="org.eclipse.jst.jsf.core.internal.jsflibrarycontainer";

	/**
	 * @param project 
	 * @return collection of references
	 */
	public static Collection<JSFLibraryReference> getJSFLibraryReferences(IProject project) {
		Collection<JSFLibraryReference> results = new HashSet<JSFLibraryReference>();
		IJavaProject jproj = JavaCore.create(project);
		try {
			IClasspathEntry[] entries = jproj.getRawClasspath();
			boolean foundImpl = false;
			for (int i=0;i<entries.length;i++){
				JSFLibraryReference ref = JSFLibraryReferenceFacadeFactory.create(entries[i]);
				if (ref != null){
					results.add(ref);
					if (ref.isJSFImplementation())
						foundImpl = true;
				}
			}
			if (! foundImpl){
				results.add(JSFLibraryReferenceFacadeFactory.createServerSuppliedJSFLibRef());
			}
		} catch (JavaModelException e) {
			JSFCorePlugin.log(e, "Exception occurred calling getJSFLibraryReferences for "+project.getName());
		}
		return results;
	}

	/**
	 * @param cpEntry
	 * @return boolean indicating that the classpath entry is a JSF Libary Classpath Container
	 */
	public static boolean isJSFLibraryContainer(IClasspathEntry cpEntry) {
		if (cpEntry.getEntryKind() != IClasspathEntry.CPE_CONTAINER)
			return false;
		
		IPath path = cpEntry.getPath();
		return path != null && path.segmentCount() == 2 && JSF_LIBRARY_CP_CONTAINER_ID.equals(path.segment(0));
	}
}