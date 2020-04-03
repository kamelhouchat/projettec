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
 * The source Renderscript file: C:\\Users\\d9z\\Desktop\\projet tec\\version combin\\nouvelle version combin\\nouveau\\projettec\\app\\src\\main\\rs\\additional_shading.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.additional_shadingBitCode;

/**
 * @hide
 */
public class ScriptC_additional_shading extends ScriptC {
    private static final String __rs_resource_name = "additional_shading";
    // Constructor
    public  ScriptC_additional_shading(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              additional_shadingBitCode.getBitCode32(),
              additional_shadingBitCode.getBitCode64());
        __U8 = Element.U8(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __U8;
    private Element __U8_4;
    private FieldPacker __rs_fp_U8;
    private final static int mExportVarIdx_red = 0;
    private short mExportVar_red;
    public synchronized void set_red(short v) {
        if (__rs_fp_U8!= null) {
            __rs_fp_U8.reset();
        } else {
            __rs_fp_U8 = new FieldPacker(1);
        }
        __rs_fp_U8.addU8(v);
        setVar(mExportVarIdx_red, __rs_fp_U8);
        mExportVar_red = v;
    }

    public short get_red() {
        return mExportVar_red;
    }

    public Script.FieldID getFieldID_red() {
        return createFieldID(mExportVarIdx_red, null);
    }

    private final static int mExportVarIdx_green = 1;
    private short mExportVar_green;
    public synchronized void set_green(short v) {
        if (__rs_fp_U8!= null) {
            __rs_fp_U8.reset();
        } else {
            __rs_fp_U8 = new FieldPacker(1);
        }
        __rs_fp_U8.addU8(v);
        setVar(mExportVarIdx_green, __rs_fp_U8);
        mExportVar_green = v;
    }

    public short get_green() {
        return mExportVar_green;
    }

    public Script.FieldID getFieldID_green() {
        return createFieldID(mExportVarIdx_green, null);
    }

    private final static int mExportVarIdx_blue = 2;
    private short mExportVar_blue;
    public synchronized void set_blue(short v) {
        if (__rs_fp_U8!= null) {
            __rs_fp_U8.reset();
        } else {
            __rs_fp_U8 = new FieldPacker(1);
        }
        __rs_fp_U8.addU8(v);
        setVar(mExportVarIdx_blue, __rs_fp_U8);
        mExportVar_blue = v;
    }

    public short get_blue() {
        return mExportVar_blue;
    }

    public Script.FieldID getFieldID_blue() {
        return createFieldID(mExportVarIdx_blue, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_shadingEffect = 1;
    public Script.KernelID getKernelID_shadingEffect() {
        return createKernelID(mExportForEachIdx_shadingEffect, 35, null, null);
    }

    public void forEach_shadingEffect(Allocation ain, Allocation aout) {
        forEach_shadingEffect(ain, aout, null);
    }

    public void forEach_shadingEffect(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_shadingEffect, ain, aout, null, sc);
    }

}

