//
// Created by feibiao.ma on 2020/9/1.
//

#ifndef MPLAYER_OPENSL_RENDER_H
#define MPLAYER_OPENSL_RENDER_H

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <queue>
#include <string>
#include <pthread.h>
#include "../../../utils/logger.h"

#include "a_render.h"

extern "C" {
#include <libavutil/mem.h>
};

static const char *TAG = "OpenSLRender";


class OpenSLRender : public AudioRender {
private:

    class PcmData {
    public:
        PcmData(uint8_t *pcm, int size) {
            this->pcm = pcm;
            this->size = size;
        }
        ~PcmData() {
            if (pcm != NULL) {
                //释放已使用的内存
                free(pcm);
                pcm = NULL;
                used = false;
            }
        }
        uint8_t *pcm = NULL;
        int size = 0;
        bool used = false;
    };


    //引擎接口
    SLObjectItf m_engine_obj = NULL;
    SLEngineItf m_engine = NULL;

    //混音器
    SLObjectItf m_output_mix_obj = NULL;
    SLEnvironmentalReverbItf m_output_mix_evn_reverb = NULL;
    SLEnvironmentalReverbSettings m_reverb_settings = SL_I3DL2_ENVIRONMENT_PRESET_DEFAULT;

    //pcm 播放器
    SLObjectItf m_pcm_player_obj = NULL;
    SLPlayItf m_pcm_player = NULL;
    SLVolumeItf m_pcm_player_volume = NULL;

    //缓冲队列接口
    SLAndroidSimpleBufferQueueItf m_pcm_buffer;
    const SLuint32 SL_QUEUE_BUFFER_COUNT = 2;


    std::queue<PcmData *> m_data_queue;

    // 缓存线程等待锁变量
    pthread_mutex_t m_cache_mutex = PTHREAD_MUTEX_INITIALIZER;
    pthread_cond_t m_cache_cond = PTHREAD_COND_INITIALIZER;


    // 创建引擎
    bool CreateEngine();

    // 创建混音器
    bool CreateOutputMixer();

    // 创建播放器
    bool CreatePlayer();

    // 开始播放渲染
    void StartRender();

    // 音频数据压入缓冲队列
    void BlockEnqueue();

    // 检查是否发生错误
    bool CheckError(SLresult result, std::string hint);

    void static sRenderPcm(OpenSLRender *that);
    // 数据填充通知接口，后续会介绍这个方法的作用
    void static sReadPcmBufferCbFun(SLAndroidSimpleBufferQueueItf bufferQueueItf, void *context);


    void WaitForCache() {
        pthread_mutex_lock(&m_cache_mutex);
        pthread_cond_wait(&m_cache_cond, &m_cache_mutex);
        pthread_mutex_unlock(&m_cache_mutex);
    }

    void SendCacheReadySignal() {
        pthread_mutex_lock(&m_cache_mutex);
        pthread_cond_signal(&m_cache_cond);
        pthread_mutex_unlock(&m_cache_mutex);
    }

public:
    OpenSLRender();

    ~OpenSLRender();

    void InitRender() override;

    void Render(uint8_t *pcm, int size) override;

    void ReleaseRender() override;
};


#endif //MPLAYER_OPENSL_RENDER_H
