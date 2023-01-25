/*
 * Copyright 2019 Slawomir Jaranowski
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

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Connection factory for TCP transport.
 *
 * @author Slawomir Jaranowski.
 */
class TCPConnectionFactory implements FatekConnectionFactory {

    private static final String SCHEMA_NAME = "TCP";

    @Override
    public String getSchema() {
        return SCHEMA_NAME;
    }

    @Override
    public FatekConnection getConnection(FatekConfig fatekConfig, Consumer<Boolean> connectionStateListener) throws IOException {

        return new TCPConnection(fatekConfig, connectionStateListener);
    }
}
