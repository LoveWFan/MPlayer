package com.poney.gpuimage.filter.edit;

import android.opengl.GLES20;

import com.poney.gpuimage.filter.base.GPUImageAdjustFilter;

public class GPUImageSharpenFilter extends GPUImageAdjustFilter {
    public static final String VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "uniform float imageWidthFactor;\n" +
            "uniform float imageHeightFactor;\n" +
            "uniform float sharpness;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate;\n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            " \n" +
            "varying float centerMultiplier;\n" +
            "varying float  edgeMultiplier;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    \n" +
            "    mediump vec2 widthStep = vec2(imageWidthFactor, 0.0);\n" +
            "    mediump vec2 heightStep = vec2(0.0, imageHeightFactor);\n" +
            "    \n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n" +
            "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n" +
            "    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n" +
            "    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n" +

            "    \n" +
            "    centerMultiplier = 1.0 + 4.0 * sharpness;\n" +
            "    edgeMultiplier = sharpness;\n" +
            "}";

    public static final String CONTRAST_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate;\n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            " \n" +
            "varying float centerMultiplier;\n" +
            "varying float  edgeMultiplier;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "    mediump vec3 textureColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "    mediump vec3 leftTextureColor = texture2D(inputImageTexture, leftTextureCoordinate).rgb;\n" +
            "    mediump vec3 rightTextureColor = texture2D(inputImageTexture, rightTextureCoordinate).rgb;\n" +
            "    mediump vec3 topTextureColor = texture2D(inputImageTexture, topTextureCoordinate).rgb;\n" +
            "    mediump vec3 bottomTextureColor = texture2D(inputImageTexture, bottomTextureCoordinate).rgb;\n" +

            "     \n" +
            "    gl_FragColor = vec4((textureColor * centerMultiplier - (leftTextureColor * edgeMultiplier + rightTextureColor * edgeMultiplier + topTextureColor * edgeMultiplier + bottomTextureColor * edgeMultiplier)), texture2D(inputImageTexture, bottomTextureCoordinate).w);\n" +

            " }";
    private int sharpnessLocation;
    private float sharpness;
    private int imageWidthFactorLocation;
    private int imageHeightFactorLocation;

    public GPUImageSharpenFilter() {
        this(0.0f);
    }

    public GPUImageSharpenFilter(float sharpness) {
        super(VERTEX_SHADER, CONTRAST_FRAGMENT_SHADER);
        this.sharpness = sharpness;
    }

    @Override
    public void onInit() {
        super.onInit();
        sharpnessLocation = GLES20.glGetUniformLocation(getProgram(), "sharpness");
        imageWidthFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidthFactor");
        imageHeightFactorLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeightFactor");
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
        setFloat(imageWidthFactorLocation, 1.0f / width);
        setFloat(imageHeightFactorLocation, 1.0f / height);
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setSharpness(sharpness);
    }

    public void setSharpness(float range) {
        this.sharpness = range;
        setFloat(sharpnessLocation, sharpness);
    }
}
