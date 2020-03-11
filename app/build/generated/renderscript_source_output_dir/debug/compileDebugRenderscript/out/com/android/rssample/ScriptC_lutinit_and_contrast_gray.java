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
 * The source Renderscript file: /autofs/unitytravail/travail/ibrdiallo/Licence 3/ProjetTech/projet/projettec/app/src/main/rs/lutinit_and_contrast_gray.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.lutinit_and_contrast_grayBitCode;

/**
 * @hide
 */
public class ScriptC_lutinit_and_contrast_gray extends ScriptC {
    private static final String __rs_resource_name = "lutinit_and_contrast_gray";
    // Constructor
    public  ScriptC_lutinit_and_contrast_gray(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              lutinit_and_contrast_grayBitCode.getBitCode32(),
              lutinit_and_contrast_grayBitCode.getBitCode64());
        __I32_2 = Element.I32_2(rs);
        __U8 = Element.U8(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __I32_2;
    private Element __U8;
    private Element __U8_4;
    private FieldPacker __rs_fp_I32_2;
    private FieldPacker __rs_fp_U8;
    private final static int mExportVarIdx_MinAndMax = 0;
    private Int2 mExportVar_MinAndMax;
    public synchronized void set_MinAndMax(Int2 v) {
        mExportVar_MinAndMax = v;
        FieldPacker fp = new FieldPacker(8);
        fp.addI32(v);
        int []__dimArr = new int[1];
        __dimArr[0] = 1;
        setVar(mExportVarIdx_MinAndMax, fp, __I32_2, __dimArr);
    }

    public Int2 get_MinAndMax() {
        return mExportVar_MinAndMax;
    }

    public Script.FieldID getFieldID_MinAndMax() {
        return createFieldID(mExportVarIdx_MinAndMax, null);
    }

    private final static int mExportVarIdx_LutTable = 1;
    private short[] mExportVar_LutTable;
    public synchronized void set_LutTable(short[] v) {
        mExportVar_LutTable = v;
        FieldPacker fp = new FieldPacker(256);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addU8(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_LutTable, fp, __U8, __dimArr);
    }

    public short[] get_LutTable() {
        return mExportVar_LutTable;
    }

    public Script.FieldID getFieldID_LutTable() {
        return createFieldID(mExportVarIdx_LutTable, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_FillLutTable_Plus = 1;
    public Script.KernelID getKernelID_FillLutTable_Plus() {
        return createKernelID(mExportForEachIdx_FillLutTable_Plus, 41, null, null);
    }

    public void forEach_FillLutTable_Plus(Allocation ain) {
        forEach_FillLutTable_Plus(ain, null);
    }

    public void forEach_FillLutTable_Plus(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8)) {
            throw new RSRuntimeException("Type mismatch with U8!");
        }
        forEach(mExportForEachIdx_FillLutTable_Plus, ain, null, null, sc);
    }

    private final static int mExportForEachIdx_FillLutTable_Fewer = 2;
    public Script.KernelID getKernelID_FillLutTable_Fewer() {
        return createKernelID(mExportForEachIdx_FillLutTable_Fewer, 41, null, null);
    }

    public void forEach_FillLutTable_Fewer(Allocation ain) {
        forEach_FillLutTable_Fewer(ain, null);
    }

    public void forEach_FillLutTable_Fewer(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8)) {
            throw new RSRuntimeException("Type mismatch with U8!");
        }
        forEach(mExportForEachIdx_FillLutTable_Fewer, ain, null, null, sc);
    }

    private final static int mExportForEachIdx_ApplyChanges_Gray = 3;
    public Script.KernelID getKernelID_ApplyChanges_Gray() {
        return createKernelID(mExportForEachIdx_ApplyChanges_Gray, 35, null, null);
    }

    public void forEach_ApplyChanges_Gray(Allocation ain, Allocation aout) {
        forEach_ApplyChanges_Gray(ain, aout, null);
    }

    public void forEach_ApplyChanges_Gray(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_ApplyChanges_Gray, ain, aout, null, sc);
    }

    private final static int mExportFuncIdx_ContrastPlusGray = 0;
    public Script.InvokeID getInvokeID_ContrastPlusGray() {
        return createInvokeID(mExportFuncIdx_ContrastPlusGray);
    }

    public void invoke_ContrastPlusGray(Allocation in, Allocation out) {
        FieldPacker ContrastPlusGray_fp = new FieldPacker((RenderScript.getPointerSize() == 8) ? 64 : 8);
        ContrastPlusGray_fp.addObj(in);
        ContrastPlusGray_fp.addObj(out);
        invoke(mExportFuncIdx_ContrastPlusGray, ContrastPlusGray_fp);
    }

    private final static int mExportFuncIdx_ContrastFewerGray = 1;
    public Script.InvokeID getInvokeID_ContrastFewerGray() {
        return createInvokeID(mExportFuncIdx_ContrastFewerGray);
    }

    public void invoke_ContrastFewerGray(Allocation in, Allocation out) {
        FieldPacker ContrastFewerGray_fp = new FieldPacker((RenderScript.getPointerSize() == 8) ? 64 : 8);
        ContrastFewerGray_fp.addObj(in);
        ContrastFewerGray_fp.addObj(out);
        invoke(mExportFuncIdx_ContrastFewerGray, ContrastFewerGray_fp);
    }

}

