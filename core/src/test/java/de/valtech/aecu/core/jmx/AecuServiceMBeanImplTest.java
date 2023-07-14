/*
 * Copyright 2018 - 2022 Valtech GmbH
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
package de.valtech.aecu.core.jmx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import de.valtech.aecu.api.service.AecuException;
import de.valtech.aecu.api.service.AecuService;
import de.valtech.aecu.api.service.ExecutionResult;
import de.valtech.aecu.api.service.HistoryEntry;

/**
 * Tests AecuServiceMBeanImpl
 * 
 * @author Roland Gruber
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AecuServiceMBeanImplTest {

    private static final String FILE1 = "/path/file1";

    private static final String PATH = "/path";

    @Mock
    private AecuService service;

    @InjectMocks
    private AecuServiceMBeanImpl bean;

    @BeforeEach
    public void setup() throws AecuException, LoginException, RepositoryException {
        when(service.getVersion()).thenReturn("1");
        when(service.getFiles(PATH)).thenReturn(Arrays.asList(FILE1));
    }

    @Test
    public void getVersion() {
        String version = bean.getVersion();

        assertEquals("1", version);
    }

    @Test
    public void getFiles() throws AecuException {
        List<String> files = bean.getFiles(PATH);

        assertEquals(1, files.size());
        assertEquals(FILE1, files.get(0));
    }

    @Test
    public void execute() throws AecuException {
        ExecutionResult result = mock(ExecutionResult.class);
        when(service.execute(FILE1)).thenReturn(result);

        bean.execute(PATH);

        verify(service, times(1)).getFiles(PATH);
        verify(service, times(1)).createHistoryEntry();
        verify(service, times(1)).execute(FILE1);
        verify(service, times(1)).execute(FILE1);
        verify(service, times(1)).finishHistoryEntry(Mockito.any());
    }

    @Test
    public void executeWithHistory() throws AecuException, LoginException {
        HistoryEntry entry = mock(HistoryEntry.class);
        when(entry.toString()).thenReturn(PATH);
        when(service.executeWithInstallHookHistory(PATH)).thenReturn(entry);

        String history = bean.executeWithHistory(PATH);

        verify(service, times(1)).executeWithInstallHookHistory(PATH);
        assertEquals(PATH, history);
    }

    @Test
    public void getHistory() throws AecuException {
        HistoryEntry entry = mock(HistoryEntry.class);
        when(entry.toString()).thenReturn(PATH);
        List<HistoryEntry> entries = Arrays.asList(entry);
        when(service.getHistory(0, 1)).thenReturn(entries);

        String history = bean.getHistory(0, 1);

        assertEquals(PATH + "\n\n", history);
    }

}
