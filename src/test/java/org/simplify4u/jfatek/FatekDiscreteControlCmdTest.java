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

import static org.simplify4u.jfatek.registers.DisReg.M;

import org.simplify4u.jfatek.io.MockConnectionFactory;
import org.simplify4u.jfatek.registers.DisRunCode;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Slawomir Jaranowski.
 */
public class FatekDiscreteControlCmdTest {

    @BeforeClass
    public void setup() {
        FatekPLC.registerConnectionFactory(new MockConnectionFactory());
    }

    @Test
    public void testCmd() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=01421M0123&plcInData=01420")) {
            new FatekDiscreteControlCmd(fatekPLC, 1, M(123), DisRunCode.Disable).send();
        }

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=01422M0123&plcInData=01420")) {
            new FatekDiscreteControlCmd(fatekPLC, 1, M(123), DisRunCode.Enable).send();
        }

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=01423M0123&plcInData=01420")) {
            new FatekDiscreteControlCmd(fatekPLC, 1, M(123), DisRunCode.Set).send();
        }

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=01424M0123&plcInData=01420")) {
            new FatekDiscreteControlCmd(fatekPLC, 1, M(123), DisRunCode.Reset).send();
        }
    }
}
