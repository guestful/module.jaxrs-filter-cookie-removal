/**
 * Copyright (C) 2013 Guestful (info@guestful.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestful.jaxrs.filter.cookie;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Priority(Priorities.HEADER_DECORATOR)
public class CookieRemovalFilter implements ContainerResponseFilter {

    private static final Date EXPIRED = new Date(System.currentTimeMillis() - 604800000);
    private final Map<String, Cookie> cookies = new TreeMap<>();

    public CookieRemovalFilter(Collection<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            this.cookies.put(cookie.getName(), cookie);
        }
    }

    public CookieRemovalFilter(Cookie cookie) {
        this.cookies.put(cookie.getName(), cookie);
    }

    public CookieRemovalFilter(Cookie... cookies) {
        for (Cookie cookie : cookies) {
            this.cookies.put(cookie.getName(), cookie);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        for (String name : requestContext.getCookies().keySet()) {
            Cookie cookie = this.cookies.get(name);
            if(cookie != null) {
                responseContext.getHeaders().addFirst(HttpHeaders.SET_COOKIE, new NewCookie(cookie, null, 0, EXPIRED, false, true));
            }
        }
    }
}
