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

import java.util.Base64;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Dependent
public class FilterNeedingDigest implements ContainerResponseFilter {
    @Inject // todo @Context
    private DigestHolder holder;

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        // final InputStream entityStream = requestContext.getEntityStream(); // can be read from there too casting it
        // todo: if needed finish to read the stream in a bufferized way (pass to a ByteArrayInputStream as main)
        responseContext.getHeaders().putSingle("This-Is-The-Digest",
                Base64.getEncoder().encodeToString(holder.getStream().getMessageDigest().digest()));
    }
}
