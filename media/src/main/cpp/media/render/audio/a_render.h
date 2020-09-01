//
// Created by feibiao.ma on 2020/9/1.
//

#ifndef MPLAYER_A_RENDER_H
#define MPLAYER_A_RENDER_H

#include <cstdint>

class AudioRender {
public:
    virtual void InitRender() = 0;

    virtual void Render(uint8_t *pcm, int size) = 0;

    virtual void ReleaseRender() = 0;

    virtual ~AudioRender() {

    }
};

#endif //MPLAYER_A_RENDER_H
