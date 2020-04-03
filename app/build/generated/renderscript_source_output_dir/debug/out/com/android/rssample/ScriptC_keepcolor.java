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
 * The source Renderscript file: C:\\Users\\d9z\\Desktop\\projet tec\\version combin\\nouvelle version combin\\nouveau\\projettec\\app\\src\\main\\rs\\keepcolor.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.keepcolorBitCode;

/**
 * @hide
 */
public class ScriptC_keepcolor extends ScriptC {
    private static final String __rs_resource_name = "keepcolor";
    // Constructor
    public  ScriptC_keepcolor(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              keepcolorBitCode.getBitCode32(),
              keepcolorBitCode.getBitCode64());
        __F32 = Element.F32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __F32;
    private Element __U8_4;
    private FieldPacker __rs_fp_F32;
    private final static int mExportVarIdx_new_hue = 0;
    private float mExportVar_new_hue;
    public synchronized void set_new_hue(float v) {
        setVar(mExportVarIdx_new_hue, v);
        mExportVar_new_hue = v;
    }

    public float get_new_hue() {
        return mExportVar_new_hue;
    }

    public Script.FieldID getFieldID_new_hue() {
        return createFieldID(mExportVarIdx_new_hue, null);
    }

    private final static int mExportVarIdx_color_to_keep = 1;
    private float mExportVar_color_to_keep;
    public synchronized void set_color_to_keep(float v) {
        setVar(mExportVarIdx_color_to_keep, v);
        mExportVar_color_to_keep = v;
    }

    public float get_color_to_keep() {
        return mExportVar_color_to_keep;
    }

    public Script.FieldID getFieldID_color_to_keep() {
        return createFieldID(mExportVarIdx_color_to_keep, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_Colorize = 1;
    public Script.KernelID getKernelID_Colorize() {
        return createKernelID(mExportForEachIdx_Colorize, 35, null, null);
    }

    public void forEach_Colorize(Allocation ain, Allocation aout) {
        forEach_Colorize(ain, aout, null);
    }

    public void forEach_Colorize(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_Colorize, ain, aout, null, sc);
    }

    private final static int mExportForEachIdx_KeepColor = 2;
    public Script.KernelID getKernelID_KeepColor() {
        return createKernelID(mExportForEachIdx_KeepColor, 35, null, null);
    }

    public void forEach_KeepColor(Allocation ain, Allocation aout) {
        forEach_KeepColor(ain, aout, null);
    }

    public void forEach_KeepColor(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_KeepColor, ain, aout, null, sc);
    }

}

