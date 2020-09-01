//
// Created by feibiao.ma on 2020/9/1.
//

#ifndef MPLAYER_PLAYER_H
#define MPLAYER_PLAYER_H



#include "decoder/video/v_decoder.h"

class Player {
private:
    VideoDecoder *m_v_decoder;
    VideoRender *m_v_render;

public:
    Player(JNIEnv *jniEnv, jstring path, jobject surface);
    ~Player();

    void play();
    void pause();
};


#endif //MPLAYER_PLAYER_H
