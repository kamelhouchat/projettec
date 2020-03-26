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
 * The source Renderscript file: C:\\Users\\IBRAHIMA\\projettec\\app\\src\\main\\rs\\lutinit_and_contrast_RGB.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.lutinit_and_contrast_RGBBitCode;

/**
 * @hide
 */
public class ScriptC_lutinit_and_contrast_RGB extends ScriptC {
    private static final String __rs_resource_name = "lutinit_and_contrast_rgb";
    // Constructor
    public  ScriptC_lutinit_and_contrast_RGB(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              lutinit_and_contrast_RGBBitCode.getBitCode32(),
              lutinit_and_contrast_RGBBitCode.getBitCode64());
        __I32_2 = Element.I32_2(rs);
        __U8 = Element.U8(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __I32_2;
    private Element __U8;
    private Element __U8_4;
    private FieldPacker __rs_fp_I32_2;
    private FieldPacker __rs_fp_U8;
    private final static int mExportVarIdx_MinAndMaxR = 0;
    private Int2 mExportVar_MinAndMaxR;
    public synchronized void set_MinAndMaxR(Int2 v) {
        mExportVar_MinAndMaxR = v;
        FieldPacker fp = new FieldPacker(8);
        fp.addI32(v);
        int []__dimArr = new int[1];
        __dimArr[0] = 1;
        setVar(mExportVarIdx_MinAndMaxR, fp, __I32_2, __dimArr);
    }

    public Int2 get_MinAndMaxR() {
        return mExportVar_MinAndMaxR;
    }

    public Script.FieldID getFieldID_MinAndMaxR() {
        return createFieldID(mExportVarIdx_MinAndMaxR, null);
    }

    private final static int mExportVarIdx_MinAndMaxG = 1;
    private Int2 mExportVar_MinAndMaxG;
    public synchronized void set_MinAndMaxG(Int2 v) {
        mExportVar_MinAndMaxG = v;
        FieldPacker fp = new FieldPacker(8);
        fp.addI32(v);
        int []__dimArr = new int[1];
        __dimArr[0] = 1;
        setVar(mExportVarIdx_MinAndMaxG, fp, __I32_2, __dimArr);
    }

    public Int2 get_MinAndMaxG() {
        return mExportVar_MinAndMaxG;
    }

    public Script.FieldID getFieldID_MinAndMaxG() {
        return createFieldID(mExportVarIdx_MinAndMaxG, null);
    }

    private final static int mExportVarIdx_MinAndMaxB = 2;
    private Int2 mExportVar_MinAndMaxB;
    public synchronized void set_MinAndMaxB(Int2 v) {
        mExportVar_MinAndMaxB = v;
        FieldPacker fp = new FieldPacker(8);
        fp.addI32(v);
        int []__dimArr = new int[1];
        __dimArr[0] = 1;
        setVar(mExportVarIdx_MinAndMaxB, fp, __I32_2, __dimArr);
    }

    public Int2 get_MinAndMaxB() {
        return mExportVar_MinAndMaxB;
    }

    public Script.FieldID getFieldID_MinAndMaxB() {
        return createFieldID(mExportVarIdx_MinAndMaxB, null);
    }

    private final static int mExportVarIdx_LutTableR = 3;
    private short[] mExportVar_LutTableR;
    public synchronized void set_LutTableR(short[] v) {
        mExportVar_LutTableR = v;
        FieldPacker fp = new FieldPacker(256);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addU8(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_LutTableR, fp, __U8, __dimArr);
    }

    public short[] get_LutTableR() {
        return mExportVar_LutTableR;
    }

    public Script.FieldID getFieldID_LutTableR() {
        return createFieldID(mExportVarIdx_LutTableR, null);
    }

    private final static int mExportVarIdx_LutTableG = 4;
    private short[] mExportVar_LutTableG;
    public synchronized void set_LutTableG(short[] v) {
        mExportVar_LutTableG = v;
        FieldPacker fp = new FieldPacker(256);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addU8(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_LutTableG, fp, __U8, __dimArr);
    }

    public short[] get_LutTableG() {
        return mExportVar_LutTableG;
    }

    public Script.FieldID getFieldID_LutTableG() {
        return createFieldID(mExportVarIdx_LutTableG, null);
    }

    private final static int mExportVarIdx_LutTableB = 5;
    private short[] mExportVar_LutTableB;
    public synchronized void set_LutTableB(short[] v) {
        mExportVar_LutTableB = v;
        FieldPacker fp = new FieldPacker(256);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addU8(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_LutTableB, fp, __U8, __dimArr);
    }

    public short[] get_LutTableB() {
        return mExportVar_LutTableB;
    }

    public Script.FieldID getFieldID_LutTableB() {
        return createFieldID(mExportVarIdx_LutTableB, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_FillLutTable_RGB = 1;
    public Script.KernelID getKernelID_FillLutTable_RGB() {
        return createKernelID(mExportForEachIdx_FillLutTable_RGB, 41, null, null);
    }

    public void forEach_FillLutTable_RGB(Allocation ain) {
        forEach_FillLutTable_RGB(ain, null);
    }

    public void forEach_FillLutTable_RGB(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8)) {
            throw new RSRuntimeException("Type mismatch with U8!");
        }
        forEach(mExportForEachIdx_FillLutTable_RGB, ain, null, null, sc);
    }

    private final static int mExportForEachIdx_ApplyChanges_RGB = 2;
    public Script.KernelID getKernelID_ApplyChanges_RGB() {
        return createKernelID(mExportForEachIdx_ApplyChanges_RGB, 35, null, null);
    }

    public void forEach_ApplyChanges_RGB(Allocation ain, Allocation aout) {
        forEach_ApplyChanges_RGB(ain, aout, null);
    }

    public void forEach_ApplyChanges_RGB(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Type t0, t1;        // Verify dimensions
        t0 = ain.getType();
        t1 = aout.getType();
        if ((t0.getCount() != t1.getCount()) ||
            (t0.getX() != t1.getX()) ||
            (t0.getY() != t1.getY()) ||
            (t0.getZ() != t1.getZ()) ||
            (t0.hasFaces()   != t1.hasFaces()) ||
            (t0.hasMipmaps() != t1.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between parameters ain and aout!");
        }

        forEach(mExportForEachIdx_ApplyChanges_RGB, ain, aout, null, sc);
    }

    private final static int mExportFuncIdx_ContrastPlusRGB = 0;
    public Script.InvokeID getInvokeID_ContrastPlusRGB() {
        return createInvokeID(mExportFuncIdx_ContrastPlusRGB);
    }

    public void invoke_ContrastPlusRGB(Allocation in, Allocation out) {
        FieldPacker ContrastPlusRGB_fp = new FieldPacker((RenderScript.getPointerSize() == 8) ? 64 : 8);
        ContrastPlusRGB_fp.addObj(in);
        ContrastPlusRGB_fp.addObj(out);
        invoke(mExportFuncIdx_ContrastPlusRGB, ContrastPlusRGB_fp);
    }

}

