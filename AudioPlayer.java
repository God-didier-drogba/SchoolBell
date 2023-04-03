import javax.sound.sampled.*;

public class AudioPlayer {
    public static void main(String[] args) {
        try {
            // 오디오 파일 로드
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(AudioPlayer.class.getResource("audio.wav"));

            // 오디오 포맷 가져오기
            AudioFormat format = audioIn.getFormat();

            // DataLine.Info 객체 생성
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Clip 객체 생성
            Clip clip = (Clip) AudioSystem.getLine(info);

            // 오디오 데이터 로드
            clip.open(audioIn);

            // 재생
            clip.start();

            // 재생이 끝나면 종료
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
