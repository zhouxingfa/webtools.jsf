package org.eclipse.jst.jsf.designtime.internal.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jst.jsf.common.runtime.internal.model.ViewObject;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.ITagElement;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.Namespace;
import org.eclipse.jst.jsf.context.IModelContext;
import org.eclipse.jst.jsf.context.resolver.structureddocument.IDOMContextResolver;
import org.eclipse.jst.jsf.context.resolver.structureddocument.IStructuredDocumentContextResolverFactory;
import org.eclipse.jst.jsf.context.resolver.structureddocument.internal.ITextRegionContextResolver;
import org.eclipse.jst.jsf.context.structureddocument.IStructuredDocumentContext;
import org.eclipse.jst.jsf.context.structureddocument.IStructuredDocumentContextFactory;
import org.eclipse.jst.jsf.core.internal.JSFCorePlugin;
import org.eclipse.jst.jsf.designtime.context.DTFacesContext;
import org.eclipse.jst.jsf.designtime.internal.view.IDTViewHandler.ViewHandlerException;
import org.eclipse.jst.jsf.designtime.internal.view.IDTViewHandler.ViewHandlerException.Cause;
import org.eclipse.jst.jsf.designtime.internal.view.model.jsp.registry.TLDTagRegistry;
import org.eclipse.jst.jsp.core.internal.contentmodel.TaglibController;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TLDCMDocumentManager;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.TaglibTracker;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDDocument;
import org.eclipse.jst.jsp.core.internal.domdocument.DOMModelForJSP;
import org.eclipse.jst.jsp.core.internal.regions.DOMJSPRegionContexts;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <p>
 * A view definition adapter that adapts JSF view definitions based on the
 * standard JSP view description language.
 * </p>
 * 
 * @author cbateman
 * 
 */
public class JSPViewDefnAdapter extends XMLViewDefnAdapter
{
    private final TLDTagRegistry _tldTagRegistry;

    JSPViewDefnAdapter(final TLDTagRegistry tldTagRegistry)
    {
        _tldTagRegistry = tldTagRegistry;
    }

    @Override
    public IDocument getContainer(final DTFacesContext context,
            final String viewId)
    {
        final IResource viewDefn = context.adaptContextObject();

        if (viewDefn instanceof IFile)
        {
            final IFile viewDefnFile = (IFile) viewDefn;
            IStructuredModel model = null;
            try
            {
                model =
                    StructuredModelManager.getModelManager()
                    .getModelForRead(viewDefnFile);

                if (model instanceof DOMModelForJSP)
                {
                    return model.getStructuredDocument();
                }
            }
            catch (final IOException e)
            {
                JSFCorePlugin.log(e, "Acquiring model for view root");
            }
            catch (final CoreException e)
            {
                JSFCorePlugin.log(e, "Acquiring model for view root");
            }
            finally
            {
                if (model != null)
                {
                    model.releaseFromRead();
                }
            }
        }
        return null;
    }

    @Override
    public ViewObject mapToViewObject(
            final Node viewDefnObject,
            final ViewObjectConstructionStrategy<? extends Node> constructionData,
            final IDocument document)
    {
        switch (viewDefnObject.getNodeType())
        {
            case Node.ELEMENT_NODE:
                return createFromElement(
                        (Element) viewDefnObject,
                        (ViewObjectConstructionStrategy<Element>) constructionData,
                        document);
        }

        return null;
    }

    @Override
    protected final String getNamespace(final Element element,
            final IDocument doc)
    {
        final String prefix = element.getPrefix();

        // TODO: merge back with JSPUtil.findUri()
        final TLDCMDocumentManager m =
            TaglibController.getTLDCMDocumentManager(doc);

        if (prefix == null || m == null)
        {
            return null;
        }
        final List<?> trackers = m.getTaglibTrackers();
        for (final Object name : trackers)
        {
            final TaglibTracker tracker = (TaglibTracker) name;
            if (prefix.equals(tracker.getPrefix()))
            {
                final CMDocument cmdoc = tracker.getDocument();
                if (cmdoc instanceof TLDDocument)
                {
                    return ((TLDDocument) cmdoc).getUri();
                }
                break; // fall out and return null
            }
        }
        return null;
    }

    /**
     * @param node
     * @param document
     * @return the tag element for node or null if none.
     */
    @Override
    protected ITagElement findTagElement(final Element node,
            final IDocument document)
    {
        final String uri = getNamespace(node, document);
        final String tagName = node.getLocalName();

        if (uri != null)
        {
            // currently tied to JSP, need to abstract for others
            final Namespace lib = _tldTagRegistry.getTagLibrary(uri);

            if (lib != null)
            {
                return findTagElement(tagName, lib);
            }
        }
        return null;
    }

    @Override
    public String getId(final Node viewDefnObject)
    throws IllegalArgumentException
    {
        if (viewDefnObject instanceof Element)
        {
            return ((Element) viewDefnObject).getAttribute("id");
        }
        throw new IllegalArgumentException(
        "Only Elements can define view object ids");
    }

    @Override
    public List<Node> getViewDefnRoots(final IDocument container)
    {
        final List<Node> roots = new ArrayList<Node>();

        final IStructuredDocumentContext context =
            IStructuredDocumentContextFactory.INSTANCE.getContext(
                    container, -1);

        if (context != null)
        {
            final IDOMContextResolver resolver =
                IStructuredDocumentContextResolverFactory.INSTANCE
                .getDOMContextResolver(context);

            if (resolver != null)
            {
                final Document doc = resolver.getDOMDocument();

                if (doc != null)
                {
                    roots.add(doc);
                }
            }
        }

        return roots;
    }

    @Override
    public DTELExpression getELExpression(final IModelContext genericContext)
    throws ViewHandlerException
    {
        final IStructuredDocumentContext context =
            (IStructuredDocumentContext) genericContext
            .getAdapter(IStructuredDocumentContext.class);

        if (context == null)
        {
            throw new ViewHandlerException(Cause.EL_NOT_FOUND);
        }

        ITextRegionContextResolver resolver =
            IStructuredDocumentContextResolverFactory.INSTANCE
            .getTextRegionResolver(context);

        String elText = null;

        if (resolver != null)
        {
            final String regionType = resolver.getRegionType();

            if (regionType != null && resolver.matchesRelative(new String[]
                              { DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE }))
            {
                // if we are in the EL content, then get the current region
                // text
                if (DOMJSPRegionContexts.JSP_VBL_CONTENT.equals(regionType))
                {
                    elText = resolver.getRegionText().trim();
                }
                // otherwise, we may be at the end of a content region but
                // at
                // the beginning of a closing brace so check to see if the
                // previous
                // region was a VBL_CONTENT
                else if (regionType.equals(DOMJSPRegionContexts.JSP_VBL_CLOSE))
                {
                    final IStructuredDocumentContext previousContext =
                        resolver.getPreviousContext();

                    final ITextRegionContextResolver prevResolver =
                        IStructuredDocumentContextResolverFactory.INSTANCE
                        .getTextRegionResolver(previousContext);

                    if (prevResolver != null)
                    {
                        if (DOMJSPRegionContexts.JSP_VBL_CONTENT
                                .equals(prevResolver.getRegionType()))
                        {
                            resolver = prevResolver;
                            elText = prevResolver.getRegionText().trim();
                        }
                        else if (DOMJSPRegionContexts.JSP_VBL_OPEN
                                .equals(prevResolver.getRegionType()))
                        {
                            elText = ""; //$NON-NLS-1$
                        }
                    }
                }
            }
        }

        if (elText != null)
        {
            final IStructuredDocumentContext elContext =
                IStructuredDocumentContextFactory.INSTANCE.getContext(
                        context.getStructuredDocument(), resolver
                        .getStartOffset());
            return new DTELExpression(elContext, elText);
        }
        return null;
    }

    @Override
    public String getGeneratedIdPrefix()
    {
        return "_idJsp";
    }
}
