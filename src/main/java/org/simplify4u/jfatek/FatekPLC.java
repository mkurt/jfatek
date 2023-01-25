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

package org.simplify4u.jfatek;

import java.net.URI;
import java.util.function.Consumer;

import org.simplify4u.jfatek.io.FatekConnection;
import org.simplify4u.jfatek.io.FatekConnectionFactory;
import org.simplify4u.jfatek.io.FatekConnectionManager;
import org.simplify4u.jfatek.io.FatekIOException;

/**
 * Main class for FatekPLC client.
 *
 * @author Slawomir Jaranowski.
 */

public final class FatekPLC extends FatekConnectionManager {


    /**
     * Create new FatekPLC client instance.
     *
     * @param uri fatekPLC address
     * @param connectionStateListener connection state listener
     * @throws FatekIOException when some thing wrongs
     */
    public FatekPLC(URI uri, Consumer<Boolean> connectionStateListener) throws FatekIOException {

        super(uri, connectionStateListener);
    }

    /**
     * Create new FatekPLC client instance.
     *
     * @param uriStr fatekPLC address
     * @param connectionStateListener connection state listener
     * @throws FatekIOException when some thing wrongs
     */
    public FatekPLC(String uriStr, Consumer<Boolean> connectionStateListener) throws FatekIOException {

        super(uriStr, connectionStateListener);
    }

    public static void registerConnectionFactory(FatekConnectionFactory connectionFactory) {
        FatekConnectionManager.registerConnectionFactory(connectionFactory);
    }

    public FatekConnection getConnection() throws FatekIOException {

        return super.getConnection0();
    }
}
