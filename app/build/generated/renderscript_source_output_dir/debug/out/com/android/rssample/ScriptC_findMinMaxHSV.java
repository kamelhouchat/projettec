/*
 * Copyright (C) 2011-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: C:\\Users\\d9z\\Desktop\\projet tec\\version combin\\nouvelle version combin\\nouveau\\nouveau\\projettec\\app\\src\\main\\rs\\findMinMaxHSV.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.findMinMaxHSVBitCode;

/**
 * @hide
 */
public class ScriptC_findMinMaxHSV extends ScriptC {
    private static final String __rs_resource_name = "findminmaxhsv";
    // Constructor
    public  ScriptC_findMinMaxHSV(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              findMinMaxHSVBitCode.getBitCode32(),
              findMinMaxHSVBitCode.getBitCode64());
        mRSLocal = rs;
        __I32_2 = Element.I32_2(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __I32_2;
    private Element __U8_4;
    private RenderScript mRSLocal;
    // To obtain the result, invoke get(), which blocks
    // until the asynchronously-launched operation has completed.
    public static class result_int2 {
        public Int2 get() {
            if (!mGotResult) {
                int[] outArray = new int[2];
                mOut.copyTo(outArray);
                mResult = new Int2(outArray[0], outArray[1]);
                mOut.destroy();
                mOut = null;  // make Java object eligible for garbage collection
                if (mTempIns != null) {
                    for (Allocation tempIn : mTempIns) {
                        tempIn.destroy();
                    }

                    mTempIns = null;  // make Java objects eligible for garbage collection
                }

                mGotResult = true;
            }

            return mResult;
        }

        private  result_int2(Allocation out) {
            mTempIns = null;
            mOut = out;
            mGotResult = false;
        }

        private Allocation[] mTempIns;
        private Allocation mOut;
        private boolean mGotResult;
        private Int2 mResult;
    }

    private final static int mExportReduceIdx_findMinAndMaxHSV = 0;
    // in1 = "in", flattened 4-vectors
    public result_int2 reduce_findMinAndMaxHSV(byte[] in1) {
        // Verify that "in1" is non-null.
        if (in1 == null) {
            throw new RSIllegalArgumentException("Array \"in1\" is null!");
        }
        // Verify that the array length is a multiple of the vector size.
        if (in1.length % 4 != 0) {
            throw new RSIllegalArgumentException("Array \"in1\" is not a multiple of 4 in length!");
        }
        Allocation ain1 = Allocation.createSized(mRSLocal, __U8_4, in1.length / 4);
        ain1.setAutoPadding(true);
        ain1.copyFrom(in1);

        result_int2 result = reduce_findMinAndMaxHSV(ain1, null);
        result.mTempIns = new Allocation[]{ain1};
        return result;
    }

    // ain1 = "uchar4 in"
    public result_int2 reduce_findMinAndMaxHSV(Allocation ain1) {
        return reduce_findMinAndMaxHSV(ain1, null);
    }

    // ain1 = "uchar4 in"
    public result_int2 reduce_findMinAndMaxHSV(Allocation ain1, Script.LaunchOptions sc) {
        // check ain1
        if (!ain1.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Allocation aout = Allocation.createSized(mRSLocal, __I32_2, 1);
        aout.setAutoPadding(true);
        reduce(mExportReduceIdx_findMinAndMaxHSV, new Allocation[]{ain1}, aout, sc);
        return new result_int2(aout);
    }

}

