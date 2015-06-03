/*
 * Copyright 2013 Slawomir Jaranowski
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

package com.github.s4u.jfatek;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.s4u.jfatek.io.FatekIOException;
import com.github.s4u.jfatek.io.FatekReader;
import com.github.s4u.jfatek.io.FatekWriter;
import com.github.s4u.jfatek.registers.Reg;
import com.github.s4u.jfatek.registers.RegValue;

/**
 * <p>Mixed read the random discrete status or register data.</p>
 *
 * @author Slawomir Jaranowski.
 */
public class FatekReadMixDataCmd extends FatekCommand<Map<Reg, RegValue>> {

    public static final int CMD_ID = 0x48;
    private final Reg[] regs;
    private Map<Reg, RegValue> result;
    private int nextRegIndex;
    private int lastRegIndex;

    /**
     * Create new command for mixed read the random discrete status or register data.
     *
     * @param fatekPLC connection manager to use
     * @param regs     regs name to read
     */
    public FatekReadMixDataCmd(FatekPLC fatekPLC, Reg... regs) {

        super(fatekPLC);
        this.regs = regs.clone();
    }

    /**
     * Create new command for mixed read the random discrete status or register data.
     *
     * @param fatekPLC connection manager to use
     * @param regs     list of regs name to read
     */
    public FatekReadMixDataCmd(FatekPLC fatekPLC, List<Reg> regs) {

        super(fatekPLC);
        this.regs = regs.toArray(new Reg[regs.size()]);
    }

    @Override
    public int getID() {

        return CMD_ID;
    }

    @Override
    protected boolean isMoreDataToExecute() {
        return nextRegIndex < regs.length;
    }

    @Override
    protected void beforeExecute() {
        result = new LinkedHashMap<>();
    }

    @Override
    protected void writeData(FatekWriter writer) throws FatekIOException {

        // calculate max regs to one message
        StringBuilder message = new StringBuilder();
        int w = 64;

        lastRegIndex = -1;
        for (int i = nextRegIndex; i < regs.length && w > 0; i++) {

            if (regs[i].is32Bits()) {
                w -= 2;
            } else {
                w--;
            }

            if (w >= 0) {
                lastRegIndex = i;
                message.append(regs[i].toString());
            }
        }

        if (lastRegIndex >= 0) {
            writer.writeByte(lastRegIndex - nextRegIndex + 1);
            writer.write(message.toString());
        }
    }

    @Override
    protected void readData(FatekReader reader) throws FatekIOException {

        for (int i = nextRegIndex; i <= lastRegIndex; i++) {
            if (regs[i].isDiscrete()) {
                result.put(regs[i], reader.readRegValueDis());
            } else if (regs[i].is32Bits()) {
                result.put(regs[i], reader.readRegVal32());
            } else {
                result.put(regs[i], reader.readRegVal16());
            }
        }

        nextRegIndex = lastRegIndex + 1;
    }

    @Override
    public Map<Reg, RegValue> getResult() {

        return Collections.unmodifiableMap(result);
    }
}
