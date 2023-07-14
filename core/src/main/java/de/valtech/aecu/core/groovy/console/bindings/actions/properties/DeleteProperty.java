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
package de.valtech.aecu.core.groovy.console.bindings.actions.properties;

import javax.annotation.Nonnull;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;

import de.valtech.aecu.core.groovy.console.bindings.actions.Action;

/**
 * @author Roxana Muresan
 * @author Roland Gruber
 */
public class DeleteProperty implements Action {

    private String name;
    private String subNodePath;

    public DeleteProperty(@Nonnull String name, String subNodePath) {
        this.name = name;
        this.subNodePath = subNodePath;
    }

    @Override
    public String doAction(@Nonnull Resource resource) {
        Resource operatingResource = resource;
        if (subNodePath != null) {
            operatingResource = resource.getChild(subNodePath);
            if (operatingResource == null) {
                String finalPath = resource.getPath() + "/" + subNodePath;
                return "WARNING: Resource " + finalPath + " not found.";
            }
        }
        ModifiableValueMap properties = operatingResource.adaptTo(ModifiableValueMap.class);
        if (properties != null) {
            properties.remove(name);
            return "Deleting property " + name + " for resource " + operatingResource.getPath();
        }
        return "WARNING: could not get ModifiableValueMap for resource " + operatingResource.getPath();
    }

}
