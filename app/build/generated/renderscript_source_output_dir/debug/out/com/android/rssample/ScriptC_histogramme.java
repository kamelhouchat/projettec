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
 * The source Renderscript file: C:\\Users\\d9z\\Desktop\\projet tec\\version combin\\nouvelle version combin\\projettec\\app\\src\\main\\rs\\histogramme.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.histogrammeBitCode;

/**
 * @hide
 */
public class ScriptC_histogramme extends ScriptC {
    private static final String __rs_resource_name = "histogramme";
    // Constructor
    public  ScriptC_histogramme(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              histogrammeBitCode.getBitCode32(),
              histogrammeBitCode.getBitCode64());
        __I32 = Element.I32(rs);
        mRSLocal = rs;
        __U8_4 = Element.U8_4(rs);
    }

    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_I32;
    private RenderScript mRSLocal;
    private final static int mExportVarIdx_size = 0;
    private int mExportVar_size;
    public synchronized void set_size(int v) {
        setVar(mExportVarIdx_size, v);
        mExportVar_size = v;
    }

    public int get_size() {
        return mExportVar_size;
    }

    public Script.FieldID getFieldID_size() {
        return createFieldID(mExportVarIdx_size, null);
    }

    // To obtain the result, invoke get(), which blocks
    // until the asynchronously-launched operation has completed.
    public static class resultArray256_int {
        public int[] get() {
            if (!mGotResult) {
                int[] outArray = new int[256];
                mOut.copyTo(outArray);
                mResult = outArray;
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

        private  resultArray256_int(Allocation out) {
            mTempIns = null;
            mOut = out;
            mGotResult = false;
        }

        private Allocation[] mTempIns;
        private Allocation mOut;
        private boolean mGotResult;
        private int[] mResult;
    }

    private final static int mExportReduceIdx_calculate_histogram = 0;
    // in1 = "in", flattened 4-vectors
    public resultArray256_int reduce_calculate_histogram(byte[] in1) {
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

        resultArray256_int result = reduce_calculate_histogram(ain1, null);
        result.mTempIns = new Allocation[]{ain1};
        return result;
    }

    // ain1 = "uchar4 in"
    public resultArray256_int reduce_calculate_histogram(Allocation ain1) {
        return reduce_calculate_histogram(ain1, null);
    }

    // ain1 = "uchar4 in"
    public resultArray256_int reduce_calculate_histogram(Allocation ain1, Script.LaunchOptions sc) {
        // check ain1
        if (!ain1.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Allocation aout = Allocation.createSized(mRSLocal, __I32, 256);
        aout.setAutoPadding(true);
        reduce(mExportReduceIdx_calculate_histogram, new Allocation[]{ain1}, aout, sc);
        return new resultArray256_int(aout);
    }

    private final static int mExportReduceIdx_calculate_histogram_CUM = 1;
    // in1 = "in", flattened 4-vectors
    public resultArray256_int reduce_calculate_histogram_CUM(byte[] in1) {
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

        resultArray256_int result = reduce_calculate_histogram_CUM(ain1, null);
        result.mTempIns = new Allocation[]{ain1};
        return result;
    }

    // ain1 = "uchar4 in"
    public resultArray256_int reduce_calculate_histogram_CUM(Allocation ain1) {
        return reduce_calculate_histogram_CUM(ain1, null);
    }

    // ain1 = "uchar4 in"
    public resultArray256_int reduce_calculate_histogram_CUM(Allocation ain1, Script.LaunchOptions sc) {
        // check ain1
        if (!ain1.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Allocation aout = Allocation.createSized(mRSLocal, __I32, 256);
        aout.setAutoPadding(true);
        reduce(mExportReduceIdx_calculate_histogram_CUM, new Allocation[]{ain1}, aout, sc);
        return new resultArray256_int(aout);
    }

}

