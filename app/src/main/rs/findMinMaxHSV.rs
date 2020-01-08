#pragma version (1)
#pragma rs java_package_name ( com . android . rssample )
#pragma rs_fp_relaxed
# include "rs_debug.rsh"

#define ARRAY_SIZE 256
#define MAX_VALUE 256
#define MIN_VALUE 0

typedef struct {
  int min_value;
  int max_value;
} intMinMax;

#pragma rs reduce(findMinAndMaxHSV) \
  initializer(findMinMaxInitHSV) accumulator(findMinMaxAccumulatorHSV) \
  combiner(findMinMaxCombinerHSV) outconverter(findMinMaxOutConverterHSV)



static void findMinMaxInitHSV(intMinMax* accum){
    accum->min_value = MAX_VALUE ;
    accum->max_value = MIN_VALUE ;
}


static void findMinMaxAccumulatorHSV(intMinMax *accum, uchar4 in) {
    int value;
    value = in.r > in.g ? in.r : in.g ;
    value = value > in.b ? value : in.b ;
    if (value <= accum->min_value)
        accum->min_value = value;
    if (value >= accum->max_value)
        accum->max_value = value;
}

static void findMinMaxCombinerHSV(intMinMax *accum, const intMinMax *addend){
    if (accum->min_value < 0 || addend->min_value < accum->min_value)
        accum->min_value = addend->min_value;

    if (accum->max_value < 0 || addend->max_value > accum->max_value)
        accum->max_value = addend->max_value;

}

static void findMinMaxOutConverterHSV(int2* result, const intMinMax *accum){
    result->x = accum->min_value ;
    result->y = accum->max_value ;
}

