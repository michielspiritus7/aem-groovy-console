package be.orbinson.aem.groovy.console.extension.impl

import be.orbinson.aem.groovy.console.api.BindingExtensionProvider
import be.orbinson.aem.groovy.console.api.BindingVariable
import be.orbinson.aem.groovy.console.api.context.ScriptContext
import be.orbinson.aem.groovy.console.api.context.ServletScriptContext
import be.orbinson.aem.groovy.console.builders.PageBuilder
import be.orbinson.aem.groovy.console.builders.NodeBuilder
import com.day.cq.search.QueryBuilder
import com.day.cq.wcm.api.PageManager
import groovy.json.JsonException
import groovy.json.JsonSlurper
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.ResourceResolver
import org.osgi.framework.BundleContext
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.jcr.Session

@Component(service = BindingExtensionProvider, immediate = true)
class DefaultBindingExtensionProvider implements BindingExtensionProvider {

    @Reference
    private QueryBuilder queryBuilder

    private BundleContext bundleContext

    @Override
    Map<String, BindingVariable> getBindingVariables(ScriptContext scriptContext) {
        def resourceResolver = scriptContext.resourceResolver
        def session = resourceResolver.adaptTo(Session)

        def bindingVariables = [
                log             : new BindingVariable(LoggerFactory.getLogger("groovyconsole"), Logger,
                        "https://www.slf4j.org/api/org/slf4j/Logger.html"),
                session         : new BindingVariable(session, Session,
                        "https://developer.adobe.com/experience-manager/reference-materials/spec/javax.jcr/javadocs/jcr-2.0/javax/jcr/Session.html"),
                pageManager     : new BindingVariable(resourceResolver.adaptTo(PageManager), PageManager),
                resourceResolver: new BindingVariable(resourceResolver, ResourceResolver,
                        "https://sling.apache.org/apidocs/sling12/org/apache/sling/api/resource/ResourceResolver.html"),
                queryBuilder    : new BindingVariable(queryBuilder, QueryBuilder,
                        "https://developer.adobe.com/experience-manager/reference-materials/6-5/javadoc/com/day/cq/search/QueryBuilder.html"),
                nodeBuilder     : new BindingVariable(new NodeBuilder(session), NodeBuilder,
                        "                        https://orbinson.github.io/aem-groovy-console/be/orbinson/aem/groovy/console/builders/NodeBuilder.html"),
                pageBuilder     : new BindingVariable(new PageBuilder(session), PageBuilder,
                        "                        https://orbinson.github.io/aem-groovy-console/be/orbinson/aem/groovy/console/builders/PageBuilder.html"),
                bundleContext   : new BindingVariable(bundleContext, BundleContext,
                        "https://docs.osgi.org/javadoc/r4v43/core/org/osgi/framework/BundleContext.html"),
                out             : new BindingVariable(scriptContext.printStream, PrintStream,
                        "https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html")
        ]

        if (scriptContext instanceof ServletScriptContext) {
            bindingVariables.putAll([
                    slingRequest : new BindingVariable(scriptContext.request, SlingHttpServletRequest,
                            "https://sling.apache.org/apidocs/sling12/org/apache/sling/api/SlingHttpServletRequest.html"),
                    slingResponse: new BindingVariable(scriptContext.response, SlingHttpServletResponse,
                            "https://sling.apache.org/apidocs/sling12/org/apache/sling/api/SlingHttpServletResponse.html")
            ])
        }

        if (scriptContext.data) {
            try {
                def json = new JsonSlurper().parseText(scriptContext.data)

                bindingVariables["data"] = new BindingVariable(json, json.class)
            } catch (JsonException ignored) {
                // if data cannot be parsed as a JSON object, bind it as a String
                bindingVariables["data"] = new BindingVariable(scriptContext.data, String)
            }
        }

        bindingVariables
    }

    @Activate
    void activate(BundleContext bundleContext) {
        this.bundleContext = bundleContext
    }
}