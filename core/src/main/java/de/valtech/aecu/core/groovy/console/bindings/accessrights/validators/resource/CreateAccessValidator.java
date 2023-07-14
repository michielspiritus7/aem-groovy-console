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
package de.valtech.aecu.core.groovy.console.bindings.accessrights.validators.resource;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.sling.api.resource.Resource;

import de.valtech.aecu.api.groovy.console.bindings.accessrights.ValidationResult;
import de.valtech.aecu.core.groovy.console.bindings.accessrights.AccessValidatorContext;
import de.valtech.aecu.core.groovy.console.bindings.accessrights.validators.BaseAccessRightsValidator;

/**
 * Checks if create access is available.
 * 
 * @author Roland Gruber
 */
public class CreateAccessValidator extends BaseAccessRightsValidator {

    /**
     * Constructor.
     * 
     * @param group              group
     * @param resource           resource to check
     * @param context            validation context
     * @param checkAccessGranted checks if the access is granted or denied
     */
    public CreateAccessValidator(Group group, Resource resource, AccessValidatorContext context, boolean checkAccessGranted) {
        super(group, resource, context, checkAccessGranted);
    }

    @Override
    public ValidationResult validate(boolean simulate) {
        return checkAction(RIGHT_CREATE);
    }

    @Override
    public String getLabel() {
        return getCheckAccessGranted() ? "Create" : "Cannot Create";
    }

}
