/**
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rmannibucau.cxf.filter;

import static java.util.Optional.ofNullable;

import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Dependent
public class DigestFilter implements ContainerRequestFilter {
    @Inject // todo: replace by @Context or cxf internal
    private DigestHolder digestHolder;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        try {
            final DigestInputStream stream = new DigestInputStream(
                    requestContext.getEntityStream(),
                    MessageDigest.getInstance(
                            ofNullable(requestContext.getProperty(getClass().getName() + ".algorithm"))
                                    .map(String::valueOf)
                                    .orElse("SHA-512")));
            digestHolder.setStream(stream);
            requestContext.setEntityStream(stream);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
