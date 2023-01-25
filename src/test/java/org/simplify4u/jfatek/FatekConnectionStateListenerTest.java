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

import org.simplify4u.jfatek.io.LoopConnectionFactory;
import org.simplify4u.jfatek.io.MockConnectionFactory;
import org.simplify4u.jfatek.registers.DisRunCode;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.simplify4u.jfatek.registers.DisReg.M;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Slawomir Jaranowski.
 */
public class FatekConnectionStateListenerTest {

    @BeforeClass
    public void setup() {
        FatekPLC.registerConnectionFactory(new LoopConnectionFactory());
    }

    @Test
    public void testConnectionStateListener() throws Exception {
        AtomicBoolean connectionState = new AtomicBoolean(false);
        Consumer<Boolean> listener = connectionState::set;
        try (FatekPLC fatekPLC = new FatekPLC("loop://test?t=1", listener)) {
            new FatekLoopCmd(fatekPLC, 1).send();
            assertTrue(connectionState.get(), "Connected");
        }
        assertFalse(connectionState.get(), "Disconnected");
    }
}
