//
// 解码状态定义
//

#ifndef LEARNVIDEO_DECODESTATE_H
#define LEARNVIDEO_DECODESTATE_H

enum DecodeState {
    STOP,
    PREPARE,
    START,
    DECODING,
    PAUSE,
    FINISH
};

#endif //LEARNVIDEO_DECODESTATE_H
