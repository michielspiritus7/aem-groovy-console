/*
 * Copyright 2018 - 2020 Valtech GmbH
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
package de.valtech.aecu.core.model.execute;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

import de.valtech.aecu.api.service.AecuException;
import de.valtech.aecu.api.service.AecuService;
import de.valtech.aecu.core.security.AccessValidationService;


/**
 * Datasource model for execute page.
 *
 * @author Bryan Chavez
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class ExecuteDataSource {

    private static final String ITEM_TYPE = "valtech/aecu/tools/execute/dataitem";

    @SlingObject
    SlingHttpServletRequest request;

    @OSGiService
    private AecuService aecuService;

    @OSGiService
    private AccessValidationService accessValidationService;

    @PostConstruct
    public void setup() throws AecuException {
        if (!accessValidationService.canExecute(request)) {
            return;
        }

        String path = request.getParameter("searchPath");
        List<Resource> entries = new ArrayList<>();

        if (path != null && StringUtils.isNotEmpty(path) && (path.startsWith(AecuService.AECU_CONF_PATH_PREFIX)
                || path.startsWith(AecuService.AECU_VAR_PATH_PREFIX) || path.startsWith(AecuService.AECU_APPS_PATH_PREFIX))) {
            List<String> allowedScripts = aecuService.getFiles(path);
            ResourceResolver resourceResolver = request.getResourceResolver();
            for (String scriptPath : allowedScripts) {
                entries.add(new ValueMapResource(resourceResolver, scriptPath, ITEM_TYPE));
            }
        }

        DataSource ds = new SimpleDataSource(entries.iterator());
        request.setAttribute(DataSource.class.getName(), ds);
    }

}
