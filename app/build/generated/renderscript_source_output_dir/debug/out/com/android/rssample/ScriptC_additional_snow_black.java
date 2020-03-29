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
 * The source Renderscript file: C:\\Users\\d9z\\Desktop\\projet tec\\version combin\\nouvelle version combin\\nouveau\\projettec\\app\\src\\main\\rs\\additional_snow_black.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.additional_snow_blackBitCode;

/**
 * @hide
 */
public class ScriptC_additional_snow_black extends ScriptC {
    private static final String __rs_resource_name = "additional_snow_black";
    // Constructor
    public  ScriptC_additional_snow_black(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              additional_snow_blackBitCode.getBitCode32(),
              additional_snow_blackBitCode.getBitCode64());
        __BOOLEAN = Element.BOOLEAN(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __BOOLEAN;
    private Element __U8_4;
    private FieldPacker __rs_fp_BOOLEAN;
    private final static int mExportVarIdx_isSnow = 0;
    private boolean mExportVar_isSnow;
    public synchronized void set_isSnow(boolean v) {
        if (__rs_fp_BOOLEAN!= null) {
            __rs_fp_BOOLEAN.reset();
        } else {
            __rs_fp_BOOLEAN = new FieldPacker(1);
        }
        __rs_fp_BOOLEAN.addBoolean(v);
        setVar(mExportVarIdx_isSnow, __rs_fp_BOOLEAN);
        mExportVar_isSnow = v;
    }

    public boolean get_isSnow() {
        return mExportVar_isSnow;
    }

    public Script.FieldID getFieldID_isSnow() {
        return createFieldID(mExportVarIdx_isSnow, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_snowBlackEffect = 1;
    public Script.KernelID getKernelID_snowBlackEffect() {
        return createKernelID(mExportForEachIdx_snowBlackEffect, 35, null, null);
    }

    public void forEach_snowBlackEffect(Allocation ain, Allocation aout) {
        forEach_snowBlackEffect(ain, aout, null);
    }

    public void forEach_snowBlackEffect(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_snowBlackEffect, ain, aout, null, sc);
    }

}

