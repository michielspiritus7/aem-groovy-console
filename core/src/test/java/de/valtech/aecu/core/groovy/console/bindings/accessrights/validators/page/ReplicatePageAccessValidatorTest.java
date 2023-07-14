/*
 * Copyright 2019 Valtech GmbH
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
package de.valtech.aecu.core.groovy.console.bindings.accessrights.validators.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import com.day.cq.security.util.CqActions;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import de.valtech.aecu.core.groovy.console.bindings.accessrights.AccessValidatorContext;
import de.valtech.aecu.core.groovy.console.bindings.accessrights.AccessValidatorContext.TestUser;
import de.valtech.aecu.core.groovy.console.bindings.accessrights.validators.BaseAccessRightsValidator;

/**
 * Tests ReplicatePageAccessValidator
 * 
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReplicatePageAccessValidatorTest {

    @Mock
    private Group group;

    @Mock
    private Resource resource;

    @Mock
    private AccessValidatorContext context;

    @Mock
    private PageManager adminPageManager;

    @Mock
    private Page page;

    @Mock
    private TestUser user;

    @Mock
    private ResourceResolver resolver;

    @Mock
    private CqActions cqActions;

    @Mock
    private Replicator replicator;

    @InjectMocks
    private ReplicatePageAccessValidator validator =
            new ReplicatePageAccessValidator(group, resource, context, true, ReplicationActionType.ACTIVATE);

    @BeforeEach
    public void setup() {
        when(context.getCqActions()).thenReturn(cqActions);
        when(context.getAdminPageManager()).thenReturn(adminPageManager);
        when(adminPageManager.getPage(Mockito.any())).thenReturn(page);
        when(context.getTestUserForGroup(group)).thenReturn(user);
        when(user.getResolver()).thenReturn(resolver);
        when(resolver.adaptTo(PageManager.class)).thenReturn(adminPageManager);
        when(context.getReplicator()).thenReturn(replicator);
    }

    @Test
    public void getLabel() {
        assertEquals("Replicate Page", validator.getLabel());
    }

    @Test
    public void validate_granted() throws RepositoryException {
        Collection<String> actions = Arrays.asList(BaseAccessRightsValidator.RIGHT_REPLICATE);
        when(cqActions.getAllowedActions(Mockito.any(), Mockito.any())).thenReturn(actions);

        assertTrue(validator.validate(true).isSuccessful());
    }

    @Test
    public void validate_notGranted() throws RepositoryException {
        Collection<String> actions = Arrays.asList();
        when(cqActions.getAllowedActions(Mockito.any(), Mockito.any())).thenReturn(actions);

        assertFalse(validator.validate(true).isSuccessful());
    }

}
