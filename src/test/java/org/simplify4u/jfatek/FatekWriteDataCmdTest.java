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

import static org.simplify4u.jfatek.registers.DataReg.DWX;
import static org.simplify4u.jfatek.registers.DataReg.F;
import static org.simplify4u.jfatek.registers.DataReg.WX;

import org.simplify4u.jfatek.io.MockConnectionFactory;
import org.simplify4u.jfatek.registers.RegValue16;
import org.simplify4u.jfatek.registers.RegValue32;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Slawomir Jaranowski.
 */
public class FatekWriteDataCmdTest {

    @BeforeClass
    public void setup() {
        FatekPLC.registerConnectionFactory(new MockConnectionFactory());
    }

    @Test
    public void testCmdValue16() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014702F00012AAAA5555&plcInData=01470", null)) {
            new FatekWriteDataCmd(fatekPLC, 1, F(12), RegValue16.asArray(0xaaaa, 0x5555)).send();
        }
    }

    @Test
    public void testCmdLongValues16() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014702F00012AAAA5555&plcInData=01470", null)) {
            new FatekWriteDataCmd(fatekPLC, 1, F(12), 0xaaaa, 0x5555).send();
        }
    }

    @Test
    public void testCmdAddValues16() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014702F00012AAAA5555&plcInData=01470", null)) {
            FatekWriteDataCmd fatekCmd = new FatekWriteDataCmd(fatekPLC, 1, F(12));
            fatekCmd.addValue(new RegValue16(0xaaaa));
            fatekCmd.addValue(new RegValue16(0x5555));
            fatekCmd.send();
        }
    }

    @Test
    public void testCmdAddLongValues16() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014702F00012AAAA5555&plcInData=01470", null)) {
            FatekWriteDataCmd fatekCmd = new FatekWriteDataCmd(fatekPLC, 1, F(12));
            fatekCmd.addValue(0xaaaa);
            fatekCmd.addValue(0x5555);
            fatekCmd.send();
        }
    }

    @Test
    public void testCmdValues32() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014702DWX0012AAAAAAAA55555555"
                + "&plcInData=01470", null)) {
            new FatekWriteDataCmd(fatekPLC, 1, DWX(12), RegValue32.asArray(0xaaaaaaaaL, 0x55555555L)).send();
        }
    }

    @Test
    public void testCmdLongValues32() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=014702DWX0012AAAAAAAA55555555"
                + "&plcInData=01470", null)) {
            new FatekWriteDataCmd(fatekPLC, 1, DWX(12), 0xaaaaaaaaL, 0x55555555L).send();
        }
    }


    @Test(expectedExceptions = FatekException.class, expectedExceptionsMessageRegExp = "Invalid value type")
    public void testCmdWrongValueType() throws Exception {

        try (FatekPLC fatekPLC = new FatekPLC("test://test?plcOutData=&plcInData=01470", null)) {
            new FatekWriteDataCmd(fatekPLC, 1, WX(12), RegValue32.asArray(0xaaaa, 0x5555)).send();
        }
    }
}
