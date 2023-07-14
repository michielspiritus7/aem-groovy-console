/*
 * Copyright 2018 - 2019 Valtech GmbH
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

package de.valtech.aecu.api.groovy.console.bindings.filters;

import javax.annotation.Nonnull;

import org.apache.sling.api.resource.Resource;

/**
 * Filters resources by path. Resources who's path matches the given regex are accepted.
 *
 * @author Roxana Muresan
 */
public class FilterByPathRegex implements FilterBy {

    private String regex;

    /**
     * Constructor
     * 
     * @param regex regular expression (standard Java pattern)
     */
    public FilterByPathRegex(@Nonnull String regex) {
        this.regex = regex;
    }

    @Override
    public boolean filter(@Nonnull Resource resource, @Nonnull StringBuilder output) {
        return resource.getPath().matches(regex);
    }
}
