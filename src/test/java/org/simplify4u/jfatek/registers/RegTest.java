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

package org.simplify4u.jfatek.registers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.simplify4u.jfatek.registers.DataReg.D;
import static org.simplify4u.jfatek.registers.DataReg.DD;
import static org.simplify4u.jfatek.registers.DataReg.DF;
import static org.simplify4u.jfatek.registers.DataReg.DR;
import static org.simplify4u.jfatek.registers.DataReg.DRC;
import static org.simplify4u.jfatek.registers.DataReg.DRT;
import static org.simplify4u.jfatek.registers.DataReg.DWC;
import static org.simplify4u.jfatek.registers.DataReg.DWM;
import static org.simplify4u.jfatek.registers.DataReg.DWS;
import static org.simplify4u.jfatek.registers.DataReg.DWT;
import static org.simplify4u.jfatek.registers.DataReg.DWX;
import static org.simplify4u.jfatek.registers.DataReg.DWY;
import static org.simplify4u.jfatek.registers.DataReg.F;
import static org.simplify4u.jfatek.registers.DataReg.R;
import static org.simplify4u.jfatek.registers.DataReg.RC;
import static org.simplify4u.jfatek.registers.DataReg.RT;
import static org.simplify4u.jfatek.registers.DataReg.WC;
import static org.simplify4u.jfatek.registers.DataReg.WM;
import static org.simplify4u.jfatek.registers.DataReg.WS;
import static org.simplify4u.jfatek.registers.DataReg.WT;
import static org.simplify4u.jfatek.registers.DataReg.WX;
import static org.simplify4u.jfatek.registers.DataReg.WY;
import static org.simplify4u.jfatek.registers.DisReg.C;
import static org.simplify4u.jfatek.registers.DisReg.M;
import static org.simplify4u.jfatek.registers.DisReg.S;
import static org.simplify4u.jfatek.registers.DisReg.T;
import static org.simplify4u.jfatek.registers.DisReg.X;
import static org.simplify4u.jfatek.registers.DisReg.Y;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.simplify4u.jfatek.FatekException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegTest {


    @Test
    public void testClone() throws FatekException {

        Reg r1 = X(1);
        Reg r2 = r1.cloneReg();

        assertEquals(r1, r2);
        assertFalse(r1 == r2);
    }

    @DataProvider(name = "provideRegsOK")
    public Object[][] provideData() {

        return new Object[][]{
                {"X1", X(1)},
                {"Y1", Y(1)},
                {"M1", M(1)},
                {"S1", S(1)},
                {"T1", T(1)},
                {"C1", C(1)},

                {"WX1", WX(1)},
                {"WY1", WY(1)},
                {"WM1", WM(1)},
                {"WS1", WS(1)},
                {"WT1", WT(1)},
                {"WC1", WC(1)},
                {"RT1", RT(1)},
                {"RC1", RC(1)},
                {"R1", R(1)},
                {"D1", D(1)},
                {"F1", F(1)},

                {"DWX1", DWX(1)},
                {"DWY1", DWY(1)},
                {"DWM1", DWM(1)},
                {"DWS1", DWS(1)},
                {"DWT1", DWT(1)},
                {"DWC1", DWC(1)},
                {"DRT1", DRT(1)},
                {"DRC1", DRC(1)},
                {"DR1", DR(1)},
                {"DD1", DD(1)},
                {"DF1", DF(1)},
        };
    }

    @Test(dataProvider = "provideRegsOK")
    public void testParse(String strReg, Reg reg) throws Exception {

        assertEquals(Reg.parse(strReg), reg);
    }

    @DataProvider(name = "provideRegsWrong")
    public Object[][] provideDataWrong() {

        return new Object[][]{
                {null},
                {""},
                {" "},
                {"X"},
                {"O2"}
        };
    }

    @Test(dataProvider = "provideRegsWrong", expectedExceptions = UnknownRegNameException.class)
    public void testParseError(String strReg) throws Exception {

        Reg.parse(strReg);
    }


    @DataProvider(name = "compareData")
    public Object[][] compareData() {

        return new Object[][]{
                {R(0), R(0), 0},
                {R(0), R(1), -1},
                {R(1), R(0), 1},
                {R(100), DR(1), -1},
                {X(2), R(0), -1}
        };
    }

    @Test(dataProvider = "compareData")
    public void testCompare(Reg thisObject, Reg specifiedObject, int result) {

        int compareResult = thisObject.compareTo(specifiedObject);

        // normalize result value
        if (compareResult > 0) {
            compareResult = 1;
        } else if (compareResult < 0) {
            compareResult = -1;
        }

        assertEquals(compareResult, result);
    }

    @Test
    public void testSortRegName() {
        List<Reg> regNameList = Arrays.asList(R(0), R(100), R(25), D(24), D(8), X(100), Y(20));

        Collections.sort(regNameList);

        Reg[] regNameSorted = regNameList.toArray(new Reg[regNameList.size()]);

        assertEquals(regNameSorted, new Reg[]{X(100), Y(20), R(0), R(25), R(100), D(8), D(24)});
    }

    @Test
    public void testImmutableInc() {

        Reg r = X(10);
        Reg r2 = r.incAddress(10);

        assertEquals(r.getAddress(), 10);
        assertEquals(r2.getAddress(), 20);
    }
}
