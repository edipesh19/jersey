/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.server;

import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.internal.inject.AbstractModule;
import org.glassfish.jersey.internal.inject.ReferencingFactory;
import org.glassfish.jersey.internal.util.collection.Ref;
import org.glassfish.jersey.process.internal.RequestScope;

import org.glassfish.hk2.Factory;
import org.glassfish.hk2.TypeLiteral;
import org.glassfish.hk2.scopes.PerLookup;

import org.jvnet.hk2.annotations.Inject;

/**
 * {@link SecurityContext Security Context} HK2 Module.
 *
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */

// TODO: (MM) this module is wrong - the SecurityContext should be taken from the ContainerRequestContext
// TODO: when I tried to fix this by creating Factory<SecurityContext> that injects ContainerRequest and calls
// TODO: getSecurityContext() on it, HK2 suddenly started to inject my SecurityContextFactory into
// TODO: ResourceMethodInvoker.Builder.invocationContextFactory.
class SecurityContextModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SecurityContext.class).toFactory(SecurityContextReferencingFactory.class).in(PerLookup.class);
        bind(new TypeLiteral<Ref<SecurityContext>>() {
        }).toFactory(ReferencingFactory.<SecurityContext>referenceFactory()).in(RequestScope.class);

    }

    /**
     * Referencing factory for SecurityContext.
     */
    private static class SecurityContextReferencingFactory extends ReferencingFactory<SecurityContext> {

        public SecurityContextReferencingFactory(@Inject Factory<Ref<SecurityContext>> referenceFactory) {
            super(referenceFactory);
        }
    }
}