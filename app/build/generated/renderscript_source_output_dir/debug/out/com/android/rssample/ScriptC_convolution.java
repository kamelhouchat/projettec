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
 * The source Renderscript file: C:\\Users\\d9z\\Desktop\\projet tec\\version combin\\nouvelle version combin\\nouveau\\projettec\\app\\src\\main\\rs\\convolution.rs
 */

package com.android.rssample;

import androidx.renderscript.*;
import com.android.rssample.convolutionBitCode;

/**
 * @hide
 */
public class ScriptC_convolution extends ScriptC {
    private static final String __rs_resource_name = "convolution";
    // Constructor
    public  ScriptC_convolution(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              convolutionBitCode.getBitCode32(),
              convolutionBitCode.getBitCode64());
        __ALLOCATION = Element.ALLOCATION(rs);
        __I32 = Element.I32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    private Element __ALLOCATION;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_ALLOCATION;
    private FieldPacker __rs_fp_I32;
    private final static int mExportVarIdx_pixels = 0;
    private Allocation mExportVar_pixels;
    public synchronized void set_pixels(Allocation v) {
        setVar(mExportVarIdx_pixels, v);
        mExportVar_pixels = v;
    }

    public Allocation get_pixels() {
        return mExportVar_pixels;
    }

    public Script.FieldID getFieldID_pixels() {
        return createFieldID(mExportVarIdx_pixels, null);
    }

    private final static int mExportVarIdx_filter = 1;
    private Allocation mExportVar_filter;
    public void bind_filter(Allocation v) {
        mExportVar_filter = v;
        if (v == null) bindAllocation(null, mExportVarIdx_filter);
        else bindAllocation(v, mExportVarIdx_filter);
    }

    public Allocation get_filter() {
        return mExportVar_filter;
    }

    private final static int mExportVarIdx_width = 2;
    private int mExportVar_width;
    public synchronized void set_width(int v) {
        setVar(mExportVarIdx_width, v);
        mExportVar_width = v;
    }

    public int get_width() {
        return mExportVar_width;
    }

    public Script.FieldID getFieldID_width() {
        return createFieldID(mExportVarIdx_width, null);
    }

    private final static int mExportVarIdx_height = 3;
    private int mExportVar_height;
    public synchronized void set_height(int v) {
        setVar(mExportVarIdx_height, v);
        mExportVar_height = v;
    }

    public int get_height() {
        return mExportVar_height;
    }

    public Script.FieldID getFieldID_height() {
        return createFieldID(mExportVarIdx_height, null);
    }

    private final static int mExportVarIdx_sum = 4;
    private int mExportVar_sum;
    public synchronized void set_sum(int v) {
        setVar(mExportVarIdx_sum, v);
        mExportVar_sum = v;
    }

    public int get_sum() {
        return mExportVar_sum;
    }

    public Script.FieldID getFieldID_sum() {
        return createFieldID(mExportVarIdx_sum, null);
    }

    private final static int mExportVarIdx_sizeFilter = 5;
    private int mExportVar_sizeFilter;
    public synchronized void set_sizeFilter(int v) {
        setVar(mExportVarIdx_sizeFilter, v);
        mExportVar_sizeFilter = v;
    }

    public int get_sizeFilter() {
        return mExportVar_sizeFilter;
    }

    public Script.FieldID getFieldID_sizeFilter() {
        return createFieldID(mExportVarIdx_sizeFilter, null);
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_convolution = 1;
    public Script.KernelID getKernelID_convolution() {
        return createKernelID(mExportForEachIdx_convolution, 59, null, null);
    }

    public void forEach_convolution(Allocation ain, Allocation aout) {
        forEach_convolution(ain, aout, null);
    }

    public void forEach_convolution(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
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

        forEach(mExportForEachIdx_convolution, ain, aout, null, sc);
    }

}

