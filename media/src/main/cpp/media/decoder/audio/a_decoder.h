//
// Created by feibiao.ma on 2020/9/1.
//

#ifndef MPLAYER_A_DECODER_H
#define MPLAYER_A_DECODER_H


#include "../base_decoder.h"
#include "../../../utils/const.h"
#include "../../render/audio/a_render.h"

extern "C" {
#include <libswresample/swresample.h>
#include <libavutil/opt.h>
#include <libavutil/audio_fifo.h>
};

class AudioDecoder : public BaseDecoder {
private:
    SwrContext *m_swr = NULL;
    uint8_t *m_out_buffer[1] = {NULL};
    int m_dest_nb_sample = 1024;
    size_t m_dest_data_size = 0;

    AudioRender *m_render;

    void InitSwr();

    void InitOutBuffer();

    void InitRender();

    void ReleaseOutBuffer();

    AVSampleFormat GetSampleFmt() {
        return AV_SAMPLE_FMT_S16;
    }

    int GetSampleRate(int spr) {
        return AUDIO_DEST_SAMPLE_RATE;
    }

public:
    AudioDecoder(JNIEnv *env, const jstring path, bool forSynthesizer);

    ~AudioDecoder();

    void SetRender(AudioRender *render);


protected:
    void Prepare(JNIEnv *env) override;

    void Render(AVFrame *frame) override;

    void Release() override;

    bool NeedLoopDecode() override {
        return true;
    }

    AVMediaType GetMediaType() override {
        return AVMEDIA_TYPE_AUDIO;
    }

    const char *const LogSpec() override {
        return "AUDIO";
    };
};


#endif //MPLAYER_A_DECODER_H
