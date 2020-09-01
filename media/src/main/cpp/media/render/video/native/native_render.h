//
// Created by feibiao.ma on 2020/9/1.
//

#ifndef MPLAYER_NATIVE_RENDER_H
#define MPLAYER_NATIVE_RENDER_H

#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <jni.h>

#include "../video_render.h"

extern "C" {
#include <libavutil/mem.h>
};

class NativeRender : public VideoRender {

private:
    jobject m_surface_ref = NULL;

    ANativeWindow_Buffer m_out_buffer;

    ANativeWindow *m_native_window = NULL;

    int m_dst_w;
    int m_dst_h;

public:
    NativeRender(JNIEnv *env, jobject surface);

    ~NativeRender();

    void InitRender(JNIEnv *env, int video_width, int video_height, int *dst_size) override;

    void Render(OneFrame *one_frame) override;

    void ReleaseRender() override;
};


#endif //MPLAYER_NATIVE_RENDER_H
