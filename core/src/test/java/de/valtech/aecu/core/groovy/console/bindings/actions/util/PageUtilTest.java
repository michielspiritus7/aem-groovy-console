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
package de.valtech.aecu.core.groovy.console.bindings.actions.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.day.cq.wcm.api.NameConstants;

/**
 * Tests PageUtil
 * 
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PageUtilTest {

    @Mock
    private Resource resource;

    @Mock
    private ValueMap valueMap;

    @BeforeEach
    public void setup() {
        when(resource.getPath()).thenReturn("/content/project/something");
        when(resource.getValueMap()).thenReturn(valueMap);
        when(valueMap.get(JcrConstants.JCR_PRIMARYTYPE, String.class)).thenReturn(NameConstants.NT_PAGE);
    }

    @Test
    public void isPageResource() {
        PageUtil util = new PageUtil();

        assertTrue(util.isPageResource(resource));
    }

    @Test
    public void isPageResource_noContent() {
        when(resource.getPath()).thenReturn("/var/project/something");

        PageUtil util = new PageUtil();

        assertFalse(util.isPageResource(resource));
    }

    @Test
    public void isPageResource_subnode() {
        when(resource.getPath()).thenReturn("/content/project/something/jcr:content/node");

        PageUtil util = new PageUtil();

        assertFalse(util.isPageResource(resource));
    }

    @Test
    public void isPageResource_wrongPrimaryType() {
        when(valueMap.get(JcrConstants.JCR_PRIMARYTYPE, String.class)).thenReturn(JcrConstants.NT_FOLDER);

        PageUtil util = new PageUtil();

        assertFalse(util.isPageResource(resource));
    }

}
