package com.poney.gpuimage.filter.edit;

import android.opengl.GLES20;

import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;

public class GPUImageBrightnessFilter extends GPUImageAdjustFilter {
    public static final String CONTRAST_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform lowp float brightness;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     gl_FragColor = vec4(textureColor.rgb + vec3(brightness), textureColor.w);\n" +
            " }";
    private int brightnessLocation;
    private float brightness;

    public GPUImageBrightnessFilter() {
        this(0.0f);
    }

    public GPUImageBrightnessFilter(float brightness) {
        super(NO_FILTER_VERTEX_SHADER, CONTRAST_FRAGMENT_SHADER);
        this.brightness = brightness;
    }

    @Override
    public void onInit() {
        super.onInit();
        brightnessLocation = GLES20.glGetUniformLocation(getProgram(), "brightness");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setBrightness(brightness);
    }

    public void setBrightness(float range) {
        this.brightness = range;
        setFloat(brightnessLocation, brightness);
    }
}
