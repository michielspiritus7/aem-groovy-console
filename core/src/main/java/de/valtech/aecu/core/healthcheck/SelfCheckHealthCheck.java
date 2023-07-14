/*
 * Copyright 2018 Valtech GmbH
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
package de.valtech.aecu.core.healthcheck;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.hc.api.HealthCheck;
import org.apache.sling.hc.api.Result;
import org.apache.sling.hc.api.Result.Status;
import org.apache.sling.hc.util.FormattingResultLog;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import de.valtech.aecu.api.service.AecuException;
import de.valtech.aecu.core.history.HistoryUtil;
import de.valtech.aecu.core.installhook.HookExecutionHistory;
import de.valtech.aecu.core.serviceuser.ServiceResourceResolverService;

/**
 * Checks if the internal service user is ok.
 *
 * @author Roland Gruber
 */
@Component(immediate = true, service = HealthCheck.class, property = {HealthCheck.TAGS + "=aecu",
        HealthCheck.NAME + "=AECU Self Check", HealthCheck.MBEAN_NAME + "=aecuSelfCheckHCmBean"})
public class SelfCheckHealthCheck implements HealthCheck {

    @Reference
    private ServiceResourceResolverService resolverService;

    @Reference
    private HistoryUtil historyUtil;

    @Override
    public Result execute() {
        final FormattingResultLog resultLog = new FormattingResultLog();
        checkServiceResolver(resultLog);
        if (resultLog.getAggregateStatus().equals(Status.CRITICAL)) {
            return new Result(resultLog);
        }
        checkHistoryNodeAccess(resultLog);
        checkHookHistoryNodeAccess(resultLog);
        return new Result(resultLog);
    }

    /**
     * Checks the history node.
     * 
     * @param resultLog result log
     */
    private void checkHistoryNodeAccess(FormattingResultLog resultLog) {
        try (ResourceResolver resolver = resolverService.getServiceResourceResolver()) {
            historyUtil.selfCheck(resolver);
            resultLog.info("History node ok");
        } catch (LoginException | AecuException e) {
            resultLog.critical("History node check failed: {}", e.getMessage());
        }
    }

    /**
     * Checks the install hook history node.
     * 
     * @param resultLog result log
     */
    private void checkHookHistoryNodeAccess(FormattingResultLog resultLog) {
        try (ResourceResolver resolver = resolverService.getServiceResourceResolver()) {
            HookExecutionHistory.selfCheck(resolver);
            resultLog.info("Install hook history node ok");
        } catch (LoginException | AecuException e) {
            resultLog.critical("Install hook history node check failed: {}", e.getMessage());
        }
    }

    /**
     * Checks if the service resource resolvers are accessible.
     * 
     * @param resultLog result log
     */
    private void checkServiceResolver(FormattingResultLog resultLog) {
        try (ResourceResolver resolver = resolverService.getServiceResourceResolver()) {
            if (resolver == null) {
                resultLog.critical("Unable to open service resource resolver: null");
                return;
            }
            resultLog.info("Service user ok");
        } catch (LoginException e) {
            resultLog.critical("Unable to open service resource resolver {}", e.getMessage());
        }
        try (ResourceResolver resolver = resolverService.getContentMigratorResourceResolver()) {
            if (resolver == null) {
                resultLog.critical("Unable to open migration resource resolver: null");
                return;
            }
            resultLog.info("Migration user ok");
        } catch (LoginException e) {
            resultLog.critical("Unable to open migration resource resolver {}", e.getMessage());
        }
    }

}
