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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.simplify4u.jfatek.registers.DataReg.DR;
import static org.simplify4u.jfatek.registers.DataReg.DWM;
import static org.simplify4u.jfatek.registers.DataReg.R;
import static org.simplify4u.jfatek.registers.DisReg.Y;
import static org.testng.Assert.assertEquals;

import org.simplify4u.jfatek.io.MockConnectionFactory;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Slawomir Jaranowski.
 */
public class FatekReadMixDataCmdTest {

    @BeforeClass
    public void setup() {
        FatekPLC.registerConnectionFactory(new MockConnectionFactory());
    }

    @Test
    public void testCmd() throws Exception {

        Map<Reg, RegValue> map;
        try (FatekPLC fatekPLC = new FatekPLC("test://test?"
                + "plcOutData=014803R00001Y0009DWM0000&plcInData=014805C341003547BA", null)) {

            map = new FatekReadMixDataCmd(fatekPLC, 1, R(1), Y(9), DWM(0)).send();
        }

        assertEquals(map.size(), 3);
        assertEquals(map.get(R(1)).intValueUnsigned(), 0x5c34);
        assertEquals(map.get(Y(9)).boolValue(), true);
        assertEquals(map.get(DWM(0)).longValueUnsigned(), 0x003547BAL);
    }

    @Test
    public void testLongMessage1() throws Exception {

        StringBuilder outRegs = new StringBuilder();
        StringBuilder inRegs = new StringBuilder();
        List<Reg> regs = new ArrayList<>();

        outRegs.append("014840");
        inRegs.append("01480");
        for (int i = 0; i < 64; i++) {
            outRegs.append(String.format("R%05d", i));
            inRegs.append(String.format("%04X", i));
            regs.add(R(i));
        }

        outRegs.append("014840");
        inRegs.append(";").append("01480");
        for (int i = 64; i < 128; i++) {
            outRegs.append(String.format("R%05d", i));
            inRegs.append(String.format("%04X", i));
            regs.add(R(i));
        }

        outRegs.append("014810");
        inRegs.append(";").append("01480");
        for (int i = 128; i < 144; i++) {
            outRegs.append(String.format("DR%05d", i));
            inRegs.append(String.format("%08X", i));
            regs.add(DR(i));
        }

        Map<Reg, RegValue> result;

        try (FatekPLC fatekPLC = new FatekPLC(String.format("test://test?plcOutData=%s&&plcInData=%s", outRegs, inRegs), null)) {
            result = new FatekReadMixDataCmd(fatekPLC, 1, regs).send();
        }

        for (int i = 0; i < regs.size(); i++) {
            assertEquals(result.get(regs.get(i)).intValue(), i);
        }
    }
}
