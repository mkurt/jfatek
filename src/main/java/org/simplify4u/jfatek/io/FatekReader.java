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

package org.simplify4u.jfatek.io;

import org.simplify4u.jfatek.FatekException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * @author Slawomir Jaranowski
 */
public class FatekReader {

    private static final Logger LOG = LoggerFactory.getLogger(FatekReader.class);

    private final InputStream input;

    private char[] msgBuf;
    private int msgBufOutPos;

    FatekReader(InputStream input) {

        this.input = input;
        this.msgBuf = new char[0];
    }

    public int read(char... buf) {

        int lenToCopy = Math.min(buf.length, msgBuf.length - msgBufOutPos - 3);
        if (lenToCopy <= 0) {
            return -1;
        }

        System.arraycopy(msgBuf, msgBufOutPos, buf, 0, lenToCopy);
        msgBufOutPos += lenToCopy;

        return lenToCopy;
    }

    public int readByte() throws FatekException {

        char[] buf = new char[2];
        if (read(buf) != 2) {
            throw new FatekException("Message too short");
        }

        return (Character.digit(buf[0], 16) << 4 | Character.digit(buf[1], 16)) & 0xff;
    }

    public int readNibble() throws FatekException {

        char[] buf = new char[1];

        int n = read(buf);
        if (n != 1) {
            throw new FatekException("Message too short");
        }
        return Character.digit(buf[0], 16) & 0xff;
    }

    public Boolean readBool() throws FatekException {

        int n = readNibble();
        switch (n) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new FatekException("Invalid value %d for boolean", n);
        }
    }

    public long readInt16() throws FatekException {

        char[] buf = new char[4];

        int n = read(buf);
        if (n != 4) {
            throw new FatekException("Message too short");
        }
        return Long.parseLong(new String(buf), 16);
    }

    public long readInt32() throws FatekException {

        char[] buf = new char[8];

        int n = read(buf);
        if (n != 8) {
            throw new FatekException("Message too short");
        }
        return Long.parseLong(new String(buf), 16);
    }

    /**
     * Read whole fatek message from STX char to ETX char to internal buffer.
     *
     * @throws FatekIOException if problem with connection
     */
    public void readNextMessage() throws FatekIOException, FatekException {

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            int t = input.read();
            if (t != 0x02) {
                if (t < 0) {
                    throw new FatekUnexpectedEOSException();
                }
                throw new FatekException("No STX");
            }
            buf.write(t);

            while (t != 0x03) {
                t = input.read();
                if (t < 0) {
                    throw new FatekUnexpectedEOSException();
                }
                buf.write(t);
            }

            byte[] bufArray = buf.toByteArray();
            if (LOG.isTraceEnabled()) {
                LOG.trace("read:\n\t<- {}", FatekUtils.byteArrayToString(bufArray));
            }

            if (bufArray.length < 8) {
                throw new FatekException("Message too short");
            }

            int crc0 = FatekUtils.countCRC(bufArray, bufArray.length - 3);
            int crc = Integer.parseInt(new String(bufArray, bufArray.length - 3, 2, StandardCharsets.US_ASCII), 16);

            if (crc != crc0) {
                throw new FatekCRCException(crc, crc0);
            }

            msgBuf = new String(bufArray, StandardCharsets.US_ASCII).toCharArray();
            msgBufOutPos = 1;
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException) {
                throw new FatekException(e); //prevent reconnect on socket timeout
            }
            throw new FatekIOException(e);
        }
    }
}
