//
// Created by feibiao.ma on 2020/9/1.
//

#include "a_decoder.h"

AudioDecoder::AudioDecoder(JNIEnv *env, jstring path, bool forSynthesizer) : BaseDecoder(
        env, path, forSynthesizer) {}

AudioDecoder::~AudioDecoder() {
    if (m_render != NULL) {
        delete m_render;
    }
}

void AudioDecoder::SetRender(AudioRender *render) {
    m_render = render;
}

void AudioDecoder::Prepare(JNIEnv *env) {
    InitSwr();
    InitOutBuffer();
    InitRender();
}

void AudioDecoder::InitSwr() {
    AVCodecContext *codeCtx = codec_cxt();
    m_swr = swr_alloc();
    // 配置输入/输出通道类型
    av_opt_set_int(m_swr, "in_channel_layout", codeCtx->channel_layout, 0);

    // 这里 AUDIO_DEST_CHANNEL_LAYOUT = AV_CH_LAYOUT_STEREO，即 立体声
    av_opt_set_int(m_swr, "out_channel_layout", AUDIO_DEST_CHANNEL_LAYOUT, 0);

    // 配置输入/输出采样率
    av_opt_set_int(m_swr, "in_sample_rate", codeCtx->sample_rate, 0);
    av_opt_set_int(m_swr, "out_sample_rate", GetSampleRate(codeCtx->sample_rate), 0);

    // 配置输入/输出数据格式
    av_opt_set_sample_fmt(m_swr, "in_sample_fmt", codeCtx->sample_fmt, 0);
    av_opt_set_sample_fmt(m_swr, "out_sample_fmt", GetSampleFmt(), 0);

    swr_init(m_swr);

}

void AudioDecoder::InitOutBuffer() {
    // 重采样后一个通道采样数
    m_dest_nb_sample = (int) av_rescale_rnd(ACC_NB_SAMPLES, GetSampleRate(codec_cxt()->sample_rate),
                                            codec_cxt()->sample_rate, AV_ROUND_UP);
    // 重采样后一帧数据的大小
    m_dest_data_size = (size_t) av_samples_get_buffer_size(
            NULL, AUDIO_DEST_CHANNEL_COUNTS,
            m_dest_nb_sample, GetSampleFmt(), 1);

    m_out_buffer[0] = (uint8_t *) malloc(m_dest_data_size);
}

void AudioDecoder::InitRender() {
    m_render->InitRender();
}

void AudioDecoder::ReleaseOutBuffer() {
    if (m_out_buffer[0] != NULL) {
        free(m_out_buffer[0]);
        m_out_buffer[0] = NULL;
    }
}

void AudioDecoder::Render(AVFrame *frame) {
    // 转换，返回每个通道的样本数
    int ret = swr_convert(m_swr, m_out_buffer, m_dest_data_size / 2,
                          (const uint8_t **) frame->data, frame->nb_samples);
    if (ret > 0) {
        m_render->Render(m_out_buffer[0], (size_t) m_dest_data_size);
    }
}

void AudioDecoder::Release() {
    if (m_swr != NULL) {
        swr_free(&m_swr);
    }
    if (m_render != NULL) {
        m_render->ReleaseRender();
    }
    ReleaseOutBuffer();
}
