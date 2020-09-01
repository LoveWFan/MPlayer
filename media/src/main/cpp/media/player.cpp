//
// Created by feibiao.ma on 2020/9/1.
//

#include "player.h"
#include "render/video/native/native_render.h"
#include "render/audio/opensl_render.h"

Player::Player(JNIEnv *jniEnv, jstring path, jobject surface) {
    m_v_decoder = new VideoDecoder(jniEnv, path);
    m_v_render = new NativeRender(jniEnv, surface);
    m_v_decoder->SetRender(m_v_render);

    // 实例化音频解码器和渲染器
    m_a_decoder = new AudioDecoder(jniEnv, path, false);
    m_a_render = new OpenSLRender();
    m_a_decoder->SetRender(m_a_render);
}

Player::~Player() {

}

void Player::play() {
    if (m_v_decoder != NULL) {
        m_v_decoder->GoOn();
        m_a_decoder->GoOn();
    }
}

void Player::pause() {
    if (m_v_decoder != NULL) {
        m_v_decoder->Pause();
        m_a_decoder->Pause();
    }
}