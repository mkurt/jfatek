/*
 * Copyright 2017 Slawomir Jaranowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.simplify4u.jfatek.io;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a connection configuration.
 *
 * @author Slawomir Jaranowski.
 */
public class FatekConfig {

    public static final String PARAM_TIMEOUT = "timeout";

    public static final int DEFAULT_TIMEOUT = 5000;

    private final URI uri;

    private final Map<String, String> params = new HashMap<>();

    FatekConfig(URI uri) {

        this.uri = uri;
        parseQuery();
    }

    /**
     * Returns the scheme component of current URI.
     *
     * @return The scheme component of this URI, or null if the scheme is undefined.
     * @see java.net.URI#getScheme()
     */
    public String getScheme() {

        return uri.getScheme();
    }

    /**
     * Returns the host component of current URI.
     *
     * @return The host component of this URI, or null if the host is undefined
     * @see java.net.URI#getHost()
     */
    public String getHost() {

        return uri.getHost();
    }

    public int getPort(int defaultPort) {

        int port = uri.getPort();
        if (port < 0) {
            port = defaultPort;
        }
        return port;
    }

    /**
     * Returns the host and path of current URI.
     *
     * @return uri.getHost + uri.getPath
     */
    public String getFullName() {
        return Optional.ofNullable(uri.getHost()).orElse("") + Optional.ofNullable(uri.getPath()).orElse("");
    }

    /**
     * Connection timeout in milliseconds.
     * Default value is DEFAULT_TIMEOUT
     *
     * @return connection timeout
     * @see FatekConfig#DEFAULT_TIMEOUT
     */
    public int getTimeout() {
        return getParamAsInt(PARAM_TIMEOUT).orElse(DEFAULT_TIMEOUT);
    }

    /**
     * @param key key for looking param
     * @return Parameter value  for given key
     */
    public Optional<String> getParam(String key) {
        return Optional.ofNullable(params.get(key));
    }

    /**
     * Retrieve parameter as Integer.
     *
     * @param key key for looking param
     * @return Parameter value  for given key
     */
    public Optional<Integer> getParamAsInt(String key) {
        return getParam(key).map(Integer::valueOf);
    }

    public SocketAddress getSocketAddress(int defaultPort) {

        return new InetSocketAddress(getHost(), getPort(defaultPort));
    }

    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("^\\s*(\\w+)=(.*)$");

    private void parseQuery() {

        String query = uri.getRawQuery();
        if (query != null) {


            for (String param : query.split("&")) {
                Matcher matcher = KEY_VALUE_PATTERN.matcher(param);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String val = matcher.group(2);

                    try {
                        val = URLDecoder.decode(val, "ASCII");
                    } catch (UnsupportedEncodingException e) {
                        val = null;
                    }
                    params.put(key, val);
                }
            }
        }
    }
}
