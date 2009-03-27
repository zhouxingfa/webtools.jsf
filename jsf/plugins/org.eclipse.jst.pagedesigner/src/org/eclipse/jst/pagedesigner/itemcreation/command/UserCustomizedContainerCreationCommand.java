package org.eclipse.jst.pagedesigner.itemcreation.command;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jst.jsf.common.dom.TagIdentifier;
import org.eclipse.jst.pagedesigner.dom.IDOMPosition;
import org.eclipse.jst.pagedesigner.dom.ValidatorSupport;
import org.eclipse.jst.pagedesigner.itemcreation.customizer.ICustomizationData;

/**
 * Tag container creation command for tags dropped on to the WPE by the user
 * 
 * @see TagContainerCreationCommand
 * 
 * @author Debajit Adhikary
 * 
 */
public class UserCustomizedContainerCreationCommand extends
        TagContainerCreationCommand
{
    /**
     * Customization data for tag dropped
     * 
     * @see ICustomizationData
     * @see IAdaptable
     */
    private final IAdaptable    _data;

    
    /**
     * @param domPosition
     *            Position in the DOM where the tag is to be inserted
     * @param containerTag
     *            Container tag
     * @param tagBeingCreated
     *            The tag which was dropped
     * @param data
     *            Customization data for the tag dropped
     * 
     * @see TagContainerCreationCommand#TagContainerCreationCommand(IDOMPosition, TagIdentifier, TagIdentifier)
     */
    public UserCustomizedContainerCreationCommand(IDOMPosition domPosition,
            TagIdentifier containerTag, TagIdentifier tagBeingCreated, IAdaptable data)
    {
        super(domPosition, containerTag, tagBeingCreated);
        _data = data;
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.jst.pagedesigner.itemcreation.command.TagContainerCreationCommand#getContainerCustomizationData()
     */
    @Override
    protected IAdaptable getContainerCustomizationData()
    {
        return _data;
    }

    
    @Override
    protected IDOMPosition doExecute() 
    {
        final IDOMPosition domPosition = getDomPosition();
        final QName  containerQName = getContainerTag().asQName();

        IDOMPosition newPosition = domPosition;
        newPosition = ValidatorSupport.insertContainer(domPosition, containerQName, getContainerCustomizationData());
        if (newPosition == null)
        {
            newPosition = domPosition;
        }
        
        return newPosition;
    }
}
