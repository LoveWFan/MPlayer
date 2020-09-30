package com.poney.gpuimage.filter.base

import com.poney.gpuimage.filter.edit.GPUImageBrightnessFilter
import com.poney.gpuimage.filter.edit.GPUImageContrastFilter
import com.poney.gpuimage.filter.edit.GPUImageSharpenFilter

class FilterAdjuster(filter: GPUImageFilter?) {
    private val adjuster: Adjuster<out GPUImageAdjustFilter>? = when (filter) {
        is GPUImageContrastFilter -> ContrastAdjuster(filter)
        is GPUImageBrightnessFilter -> BrightnessAdjuster(filter)
        is GPUImageSharpenFilter -> SharpnessAdjuster(filter)
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

    private inner class ContrastAdjuster(filter: GPUImageContrastFilter) :
            Adjuster<GPUImageContrastFilter>(filter) {
        override fun adjust(percentage: Int) {
            filter.setContrast(range(percentage, 0.0f, 2.0f))
        }
    }

    private inner class BrightnessAdjuster(filter: GPUImageBrightnessFilter) :
            Adjuster<GPUImageBrightnessFilter>(filter) {
        override fun adjust(percentage: Int) {
            filter.setBrightness(range(percentage, -1.0f, 1.0f))
        }
    }

    private inner class SharpnessAdjuster(filter: GPUImageSharpenFilter) :
            Adjuster<GPUImageSharpenFilter>(filter) {
        override fun adjust(percentage: Int) {
            filter.setSharpness(range(percentage, -4.0f, 4.0f))
        }
    }

}