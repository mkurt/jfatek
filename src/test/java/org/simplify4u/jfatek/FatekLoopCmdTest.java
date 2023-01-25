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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Slawomir Jaranowski.
 */
public class FatekLoopCmdTest {

    @BeforeClass
    public void setup() {
        FatekPLC.registerConnectionFactory(new LoopConnectionFactory());
        FatekPLC.registerConnectionFactory(new MockConnectionFactory());
    }

    @Test
    public void testDefaultMsg() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("loop://test?t=1", null)) {
            new FatekLoopCmd(fatekPLC, 1).send();
        }
    }

    @Test
    public void testMessage() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014E0ABCDEFG&plcInData=014E0ABCDEFG", null)) {
            new FatekLoopCmd(fatekPLC, 1, "ABCDEFG").send();
        }
    }

    @Test(expectedExceptions = FatekException.class, expectedExceptionsMessageRegExp = "Response not equals")
    public void testMessageNotEqual() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014E0ABCDEFG&plcInData=014E0GFEDCBA", null)) {
            new FatekLoopCmd(fatekPLC, 1, "ABCDEFG").send();
        }
    }

    @Test(expectedExceptions = FatekException.class, expectedExceptionsMessageRegExp = "Invalid response length")
    public void testMessageResLength() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014E0ABCDEFG&plcInData=014E0ABC", null)) {
            new FatekLoopCmd(fatekPLC, 1, "ABCDEFG").send();
        }
    }

}
