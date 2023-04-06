import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;

public class AudioPlayer {
    public static void main(String[] args) {
        try {
            PlayWave playWaveObj = new PlayWave();
            playWaveObj.playWavFile("./sample1.wav");

            // // 오디오 파일 로드
            // AudioInputStream audioIn = AudioSystem.getAudioInputStream(AudioPlayer.class.getResource("sample1.wav"));

            // // 오디오 포맷 가져오기
            // AudioFormat format = audioIn.getFormat();

            // // DataLine.Info 객체 생성
            // DataLine.Info info = new DataLine.Info(Clip.class, format);

            // // Clip 객체 생성
            // Clip clip = (Clip) AudioSystem.getLine(info);

            // // 오디오 데이터 로드
            // clip.open(audioIn);

            // // 재생
            // clip.start();

            // // 재생이 끝나면 종료
            // clip.addLineListener(new LineListener() {
            //     @Override
            //     public void update(LineEvent event) {
            //         if (event.getType() == LineEvent.Type.STOP) {
            //             event.getLine().close();
            //         }
            //     }
            // });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


 class PlayWave {
    public PlayWave() {

    }

    public void playWavFile(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                inputStream);
        AudioInputStream audioStream = null;
        AudioFormat audioFormat = null;
    
        try {
            audioStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            audioFormat = audioStream.getFormat();
        } catch (UnsupportedAudioFileException e) {
            System.out.println(e.toString());
            return;
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }
    
        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                audioFormat);
        SourceDataLine sourceLine;
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            System.out.println(e.toString());
            return;
        }
    
        sourceLine.start();
    
        int nBytesRead = 0;
        byte[] abData = new byte[128000];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                System.out.println(e.toString());
                return;
            }
    
            if (nBytesRead >= 0) {
                sourceLine.write(abData, 0, nBytesRead);
            }
        }
    
        sourceLine.drain();
        sourceLine.close();
    
        try {
            audioStream.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}