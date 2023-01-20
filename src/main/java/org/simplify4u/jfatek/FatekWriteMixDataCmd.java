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

import java.util.LinkedHashMap;
import java.util.Map;

import org.simplify4u.jfatek.io.FatekIOException;
import org.simplify4u.jfatek.io.FatekWriter;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;

/**
 * <p>Mixed write the random discrete status or register data.</p>
 *
 * @author Slawomir Jaranowski.
 */
public class FatekWriteMixDataCmd extends FatekCommand<Void> {

    public static final int CMD_ID = 0x49;
    private final Map<Reg, RegValue> values = new LinkedHashMap<>();

    public FatekWriteMixDataCmd(FatekPLC fatekPLC, int plcId) {

        super(fatekPLC, plcId);
    }

    public FatekWriteMixDataCmd(FatekPLC fatekPLC, int plcId, Map<? extends Reg, ? extends RegValue> values) {

        super(fatekPLC, plcId);
        this.values.putAll(values);
    }

    public FatekWriteMixDataCmd(FatekPLC fatekPLC, int plcId, Reg reg, RegValue value) {

        super(fatekPLC, plcId);
        values.put(reg, value);
    }

    public FatekWriteMixDataCmd addReg(Reg reg, RegValue value) {
        values.put(reg, value);
        return this;
    }

    public FatekWriteMixDataCmd addReg(Reg reg, long value) {
        values.put(reg, RegValue.getForReg(reg, value));
        return this;
    }

    public FatekWriteMixDataCmd addReg(Reg reg, boolean value) {
        values.put(reg, RegValue.getForReg(reg, value));
        return this;
    }

    public FatekWriteMixDataCmd addReg(Reg reg, float value) {
        values.put(reg, RegValue.getForReg(reg, value));
        return this;
    }

    @Override
    public int getID() {

        return CMD_ID;
    }

    @Override
    protected void writeData(FatekWriter writer) throws FatekIOException {

        writer.writeByte(values.size());

        for (Map.Entry<? extends Reg, ? extends RegValue> ve : values.entrySet()) {
            writer.write(ve.getKey().toString());
            writer.write(ve.getValue().toFatekString());
        }
    }
}
