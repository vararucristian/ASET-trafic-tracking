import cv2


class VideoFile():
    def __init__(self, file_path):
        self.video_input = cv2.VideoCapture(file_path)
        self.video_width = int(self.video_input.get(cv2.CAP_PROP_FRAME_WIDTH))
        self.video_height = int(self.video_input.get(cv2.CAP_PROP_FRAME_HEIGHT))
        # self.video_width = 640
        # self.video_height = 352
        self.video_fps = self.video_input.get(cv2.CAP_PROP_FPS)

        self.current_frame = 0
        self.video_length = int(self.video_input.get(cv2.CAP_PROP_FRAME_COUNT))

    def read_next_frame(self):
        if self.current_frame < self.video_length:
            self.current_frame += 1
            _, frame = self.video_input.read()
            is_last_frame = False
            if self.current_frame == self.video_length:
                is_last_frame = True

            # frame = cv2.resize(frame, (self.video_width, self.video_height))
            return frame, is_last_frame

