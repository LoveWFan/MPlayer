package com.poney.gpuimage.filter.base

class FilterAdjuster(filter: GPUImageFilter?) {
    private val adjuster: Adjuster<out GPUImageAdjustFilter>? = when (filter) {
        else -> null
    }

    fun canAdjust(): Boolean {
        return adjuster != null
    }

    fun adjust(percentage: Int) {
        adjuster?.adjust(percentage)
    }

    private abstract inner class Adjuster<T : GPUImageFilter>(protected val filter: T) {
        abstract fun adjust(percentage: Int)
        protected fun range(percentage: Int, start: Float, end: Float): Float {
            return (end - start) * percentage / 100.0f + start
        }

        protected fun range(percentage: Int, start: Int, end: Int): Int {
            return (end - start) * percentage / 100 + start
        }
    }
}