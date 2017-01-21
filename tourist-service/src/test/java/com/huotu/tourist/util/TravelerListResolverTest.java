package com.huotu.tourist.util;

import org.junit.Test;
import org.springframework.web.context.request.NativeWebRequest;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by lhx on 2017/1/22.
 */
public class TravelerListResolverTest {

    private TravelerListResolver resolver = new TravelerListResolver();

    @Test
    public void resolveArgument() throws Exception {
        /// [{"travelerType":0,"name":"safs","telPhone":"123","number":"123","age":11,"sex":0}]
        assertThat(resolver.resolveArgument(null, null, new NativeWebRequest() {
            @Override
            public Object getNativeRequest() {
                return null;
            }

            @Override
            public Object getNativeResponse() {
                return null;
            }

            @Override
            public <T> T getNativeRequest(Class<T> requiredType) {
                return null;
            }

            @Override
            public <T> T getNativeResponse(Class<T> requiredType) {
                return null;
            }

            @Override
            public String getHeader(String headerName) {
                return null;
            }

            @Override
            public String[] getHeaderValues(String headerName) {
                return new String[0];
            }

            @Override
            public Iterator<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getParameter(String paramName) {
                return "[{\"travelerType\":0,\"name\":\"safs\",\"telPhone\":\"123\",\"number\":\"123\",\"age\":11,\"sex\":0}]";
            }

            @Override
            public String[] getParameterValues(String paramName) {
                return new String[0];
            }

            @Override
            public Iterator<String> getParameterNames() {
                return null;
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public String getContextPath() {
                return null;
            }

            @Override
            public String getRemoteUser() {
                return null;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public boolean checkNotModified(long lastModifiedTimestamp) {
                return false;
            }

            @Override
            public boolean checkNotModified(String etag) {
                return false;
            }

            @Override
            public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
                return false;
            }

            @Override
            public String getDescription(boolean includeClientInfo) {
                return null;
            }

            @Override
            public Object getAttribute(String name, int scope) {
                return null;
            }

            @Override
            public void setAttribute(String name, Object value, int scope) {

            }

            @Override
            public void removeAttribute(String name, int scope) {

            }

            @Override
            public String[] getAttributeNames(int scope) {
                return new String[0];
            }

            @Override
            public void registerDestructionCallback(String name, Runnable callback, int scope) {

            }

            @Override
            public Object resolveReference(String key) {
                return null;
            }

            @Override
            public String getSessionId() {
                return null;
            }

            @Override
            public Object getSessionMutex() {
                return null;
            }
        }, null)).isNotNull();
    }

}