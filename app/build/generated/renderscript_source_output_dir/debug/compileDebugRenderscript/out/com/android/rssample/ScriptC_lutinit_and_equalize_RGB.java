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
 * The source Renderscript file: /autofs/unitytravail/travail/ibrdiallo/Licence 3/ProjetTech/projet/projettec/app/src/main/rs/lutinit_and_equalize_RGB.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.lutinit_and_equalize_RGBBitCode;

/**
 * @hide
 */
public class ScriptC_lutinit_and_equalize_RGB extends ScriptC {
    private static final String __rs_resource_name = "lutinit_and_equalize_rgb";
    // Constructor
    public  ScriptC_lutinit_and_equalize_RGB(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              lutinit_and_equalize_RGBBitCode.getBitCode32(),
              lutinit_and_equalize_RGBBitCode.getBitCode64());
        __I32 = Element.I32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_I32;
    private final static int mExportVarIdx_LutTable = 0;
    private int[] mExportVar_LutTable;
    public synchronized void set_LutTable(int[] v) {
        mExportVar_LutTable = v;
        FieldPacker fp = new FieldPacker(1024);
        for (int ct1 = 0; ct1 < 256; ct1++) {
            fp.addI32(v[ct1]);
        }

        int []__dimArr = new int[1];
        __dimArr[0] = 256;
        setVar(mExportVarIdx_LutTable, fp, __I32, __dimArr);
    }

    public int[] get_LutTable() {
        return mExportVar_LutTable;
    }

    public Script.FieldID getFieldID_LutTable() {
        return createFieldID(mExportVarIdx_LutTable, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_ApplyChanges_Equalize_RGB = 1;
    public Script.KernelID getKernelID_ApplyChanges_Equalize_RGB() {
        return createKernelID(mExportForEachIdx_ApplyChanges_Equalize_RGB, 35, null, null);
    }

    public void forEach_ApplyChanges_Equalize_RGB(Allocation ain, Allocation aout) {
        forEach_ApplyChanges_Equalize_RGB(ain, aout, null);
    }

    public void forEach_ApplyChanges_Equalize_RGB(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_ApplyChanges_Equalize_RGB, ain, aout, null, sc);
    }

}

