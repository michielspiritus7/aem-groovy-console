/*
 * Copyright 2020 - 2022 Valtech GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.valtech.aecu.core.groovy.console.bindings.actions.resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.codehaus.groovy.runtime.GStringImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import groovy.lang.GString;

/**
 * Tests CreateResource
 * 
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreateResourceTest {

    private static final String NT_UNSTRUCTURED = "nt:unstructured";
    private static final GString NT_UNSTRUCTURED_G = new GStringImpl(new Object[] {"unstructured"}, new String[] {"nt:"});

    private static final String JCR_PRIMARY_TYPE = "jcr:primaryType";

    private static final String INTERMEDIATE = "intermediate";

    private static final String NEW_NAME = "newName";

    @Mock
    private ResourceResolver resolver;

    @Mock
    private Resource resource;

    @Mock
    private Resource newResource;

    @Mock
    private Resource intermediateResource;

    private Map<String, Object> properties = new HashMap<>();

    @BeforeEach
    public void setup() throws PersistenceException {
        when(newResource.getPath()).thenReturn("/parent/node/nodeNew");
        when(resolver.getResource(resource, INTERMEDIATE)).thenReturn(intermediateResource);
        properties.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED);
        when(resolver.create(resource, NEW_NAME, properties)).thenReturn(newResource);
        when(resolver.create(intermediateResource, NEW_NAME, properties)).thenReturn(newResource);
    }

    @Test
    public void doAction_noRelative() throws PersistenceException, ItemExistsException, PathNotFoundException, VersionException,
            ConstraintViolationException, LockException, RepositoryException {
        CreateResource action = new CreateResource(NEW_NAME, properties, null, resolver);

        action.doAction(resource);

        verify(resolver, times(1)).create(resource, NEW_NAME, properties);
    }

    @Test
    public void doAction_Relative() throws PersistenceException, ItemExistsException, PathNotFoundException, VersionException,
            ConstraintViolationException, LockException, RepositoryException {
        CreateResource action = new CreateResource(NEW_NAME, properties, INTERMEDIATE, resolver);

        action.doAction(resource);

        verify(resolver, times(1)).create(intermediateResource, NEW_NAME, properties);
    }

    @Test
    public void doAction_noRelativeGString() throws PersistenceException, ItemExistsException, PathNotFoundException,
            VersionException, ConstraintViolationException, LockException, RepositoryException {
        Map<String, Object> propertiesInput = new HashMap<>(properties);
        propertiesInput.put(JCR_PRIMARY_TYPE, NT_UNSTRUCTURED_G);
        CreateResource action = new CreateResource(NEW_NAME, propertiesInput, null, resolver);

        action.doAction(resource);

        verify(resolver, times(1)).create(resource, NEW_NAME, properties);
    }

}
