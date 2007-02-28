package org.eclipse.jst.jsf.validation.internal.appconfig;

import java.io.StringReader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jst.jsf.common.internal.types.TypeConstants;
import org.eclipse.jst.jsf.core.internal.JSFCorePlugin;
import org.eclipse.jst.jsf.facesconfig.emf.ListEntriesType;
import org.eclipse.jst.jsf.facesconfig.emf.ManagedBeanScopeType;
import org.eclipse.jst.jsf.facesconfig.emf.MapEntriesType;
import org.eclipse.jst.jsp.core.internal.java.jspel.JSPELParser;
import org.eclipse.jst.jsp.core.internal.java.jspel.ParseException;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.xml.core.internal.emf2xml.EMF2DOMSSEAdapter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Node;

/**
 * Common functions for app config validation
 * 
 * @author cbateman
 *
 */
public final class AppConfigValidationUtil 
{
    /**
     * Per the fully-qualified-classType in the Faces 1.2 schema and
     * the ClassName entity in the 1.1 DTD
     * 
     * @param fullyQualifiedName
     * @param instanceOf 
     * @param project 
     * @return null if no problems or a Message if problem found
     */
    public static IMessage validateClassName(final String fullyQualifiedName, 
                                             final String instanceOf,
                                             final IProject project)
    {
        try
        {
            IType type = getType(project, fullyQualifiedName);
            if (type == null)
            {
                return DiagnosticFactory
                        .create_CANNOT_FIND_CLASS_NAME(fullyQualifiedName);
            }
            
            // must be a class, not an interface or enum
            if (!type.isClass())
            {
                return DiagnosticFactory
                        .create_FULLY_QUALIFIED_NAME_MUST_BE_A_CLASS
                            (fullyQualifiedName);
            }
            
            // must not be abstract since it must instantiable
            if ((type.getFlags() & Flags.AccAbstract) != 0)
            {
                return DiagnosticFactory
                        .create_CLASS_MUST_BE_CONCRETE(fullyQualifiedName);
            }

            if (instanceOf != null)
            {
                if (!isInstanceOf(type, instanceOf))
                {
                    // if we get to here, we haven't found the expected
                    // the super type so error
                    return DiagnosticFactory.create_CLASS_MUST_BE_INSTANCE_OF
                        (fullyQualifiedName, "extend", instanceOf);
                }
            }
        }
        catch(JavaModelException jme)
        {
            // fall-through, not found
            JSFCorePlugin.log(jme, 
                "Error resolving fully qualified class name: "+fullyQualifiedName);
        }

        // either found the class or had an exception so don't report error
        return null;
    }
    
    private static IType getType(final IProject project,
                        final String fullyQualifiedName) throws JavaModelException
    {
        final IJavaProject javaProject = JavaCore.create(project);
        return javaProject.findType(fullyQualifiedName);
    }
    
    /**
     * Per the faces-config-el-expressionType in the Faces 1.2 schema and
     * the Action entity in the 1.1 DTD
     * 
     * @param textContent
     * @return an validation diagnostic or null if the textContent
     * represent an expected EL expression
     */
    public static IMessage validateELExpression(final String textContent)
    {
        final String elRegex = "#\\{(.*)\\}";
        Pattern pattern = Pattern.compile(elRegex);
        Matcher matcher = pattern.matcher(textContent.trim());
        if (matcher.matches())
        {
            final String elText = matcher.group(1).trim();
            
            if ("".equals(elText) || elText == null)
            {
                return DiagnosticFactory.create_SYNTAX_ERROR_IN_EL();
            }
            JSPELParser parser = new JSPELParser(new StringReader(elText));
            
            try {
                parser.Expression();
            } catch (ParseException e) {
                // syntax error
                return DiagnosticFactory.create_SYNTAX_ERROR_IN_EL();
            }
            
            return null;
        }
        return DiagnosticFactory.create_EL_EXPR_MUST_BE_IN_HASH_BRACES();
    }

    /**
     * @param eObj
     * @return the offset character offset in to the XML document of the
     * XML node that eObj was constructed from or -1 if not 
     * computable
     */
    public static int getStartOffset(EObject eObj)
    {
        IDOMNode node = getDomNode(eObj);
        
        if (node != null)
        {
            return node.getStartStructuredDocumentRegion().getStartOffset();
        }
        
        return -1;
    }

    /**
     * @param eObj
     * @return the length in characters of the XML node that
     * eObj was constructed from or -1 if no computable
     */
    public static int getLength(EObject eObj)
    {
        IDOMNode node = getDomNode(eObj);
        
        if (node != null)
        {
            return node.getEndStructuredDocumentRegion().getEndOffset()
                      - node.getStartStructuredDocumentRegion().getStartOffset();
        }
        
        return -1;
    }
    
    /**
     * @param eObj
     * @return the DOM node that eObj was constructed from or
     * null if not computable
     */
    public static IDOMNode getDomNode(EObject eObj)
    {
        for (Iterator it = eObj.eAdapters().iterator(); it.hasNext();)
        {
            Adapter adapter = (Adapter) it.next();
            
            if (adapter instanceof EMF2DOMSSEAdapter)
            {
                final EMF2DOMSSEAdapter sseAdapter = (EMF2DOMSSEAdapter) adapter;
                final Node node = sseAdapter.getNode();
                if (node instanceof IDOMNode)
                {
                    return (IDOMNode) node;
                }
            }
        }
        
        return null;
    }
    
    /**
     * @param scope
     * @return an error message if scope does not match a valid
     * scope enum.
     */
    public static IMessage validateManagedBeanScope(ManagedBeanScopeType scope)
    {
        // scope must be one of a few enums
        if (!"request".equals(scope.getTextContent())
                && !"session".equals(scope.getTextContent())
                && !"application".equals(scope.getTextContent())
                && !"none".equals(scope.getTextContent()))
        {
            return DiagnosticFactory.create_BEAN_SCOPE_NOT_VALID();
        }
        
        return null;
    }

    /**
     * @param targetName 
     * @param targetType the type of the object that mapEntries will be assigned to
     * @param mapEntries
     * @param project
     * @return null if okay or an error message if the mapEntries type is 
     * invalid in some way
     * Note: when Java 1.5 support is added we can validate against the template types
     */
    public static IMessage validateMapEntries(String targetName, String targetType, MapEntriesType mapEntries, IProject project)
    {
        if (mapEntries == null || targetType == null || project == null)
        {
            throw new AssertionError("Arguments to validateMapEntries can't be null");
        }
        
        try
        {
            // TODO: do a bean look-up for targetName to verify that it a) matches the type
            // and b) exists on the bean
            IType type = getType(project, targetType);
            
            if (type != null &&
                    !(isInstanceOf(type, Signature.toString(TypeConstants.TYPE_MAP))))
            {
                return DiagnosticFactory
                    .create_MAP_ENTRIES_CAN_ONLY_BE_SET_ON_MAP_TYPE(targetName);
            }
            // TODO: validate the the map entries
            // TODO: validate the types of the map entries against the values present
            // TODO: validate the map key and value types against the template
        }
        catch (JavaModelException jme)
        {
            JSFCorePlugin.log(new Exception(jme), "Exception while validating mapEntries");
        }
        // if we get to here, we have not found anything meaningful to report
        return null;
    }
    
    /**
     * @param targetName 
     * @param targetType the type of the object that mapEntries will be assigned to
     * @param listEntries
     * @param project
     * @return null if okay or an error message if the listEntries type is 
     * invalid in some way
     * Note: when Java 1.5 support is added we can validate against the template types
     */
    public static IMessage validateListEntries(String targetName, String targetType, ListEntriesType listEntries, IProject project)
    {
        if (listEntries == null || targetType == null || project == null)
        {
            throw new AssertionError("Arguments to validateMapEntries can't be null");
        }
        
        try
        {
            IType type = getType(project, targetType);
            // TODO: do a bean look-up for targetName to verify that it a) matches the type
            // and b) exists on the bean
            if (type != null &&
                    !(isInstanceOf(type, Signature.toString(TypeConstants.TYPE_LIST))))
            {
                return DiagnosticFactory
                    .create_LIST_ENTRIES_CAN_ONLY_BE_SET_ON_LIST_TYPE(targetName);
            }
            // TODO: validate the the list entries
            // TODO: validate the types of the list entries against the values present
            // TODO: validate the value types against the template
        }
        catch (JavaModelException jme)
        {
            JSFCorePlugin.log(new Exception(jme), "Exception while validating mapEntries");
        }
        // if we get to here, we have not found anything meaningful to report
        return null;
    }
    
    /**
     * @param localeType
     * @return a diagnostic if 'localeType' does not match the
     * expected format or null if all is clear
     */
    public static IMessage validateLocaleType(final String localeType)
    {
        // based on the localeType in the Faces 1.2 schema.  This is safe
        // to apply to 1.1 since it expects the same pattern even though 
        // the DTD cannot validate it
        final String localeTypePattern = "[a-z]{2}(_|-)?([\\p{L}\\-\\p{Nd}]{2})?";
        final Matcher matcher = Pattern.compile(localeTypePattern).matcher(localeType);
        
        if (!matcher.matches())
        {
            return DiagnosticFactory.create_LOCALE_FORMAT_NOT_VALID();
        }
        
        return null;
    }
    
    /**
     * @param type
     * @param instanceOf
     * @return true if type instanceof instanceOf is true
     * 
     * @throws JavaModelException
     */
    public static boolean isInstanceOf(final IType type, final String instanceOf) throws JavaModelException
    {
        if (instanceOf != null)
        {
            // must have either a no-arg constructor or an adapter constructor
            // that is of the type of instanceOf
//            IType  constructorParam = getType(project, instanceOf);
//            if (constructorParam != null)
//            {
//                final String constructorMethodName = 
//                    type.getElementName();
//                final IMethod defaultConstructor = 
//                    type.getMethod(constructorMethodName, new String[0]);
//                final IMethod adapterConstructor =
//                    type.getMethod(constructorMethodName, new String[]{instanceOf});
//                final boolean isDefaultConstructor =
//                    defaultConstructor != null && defaultConstructor.isConstructor();
//                final boolean isAdapterConstructor =
//                    adapterConstructor != null && adapterConstructor.isConstructor();
//                if (!isDefaultConstructor && !isAdapterConstructor)
//                {
                    // TODO: no constructor == default constructor...
//                }
//            }

            // if the type is an exact match
            if (instanceOf.equals(type.getFullyQualifiedName()))
            {
                return true;
            }

            final ITypeHierarchy typeHierarchy = 
                type.newSupertypeHierarchy(new NullProgressMonitor());
            
            final IType[] supers = typeHierarchy.getAllSupertypes(type);
            for (int i = 0; i < supers.length; i++)
            {
                if (instanceOf.equals(supers[i].getFullyQualifiedName()))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private AppConfigValidationUtil()
    {
        // no external construction
    }
}
