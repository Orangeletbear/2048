package exam;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * 
* Copyright: Copyright (c) 2019 ChongYang
* @ClassName: PlaySound.java
* @Description:加载声音组件
* @version: v1.0.0
* @date: 2019年6月18日 下午5:30:36 
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年6月18日     YancyChong           v1.0.0               修改原因
 */
public class PlaySound extends Thread {
	private String filename;

	public PlaySound(String wavfile) {

		filename = "res/" + wavfile;
	}

	public void run() {
		File soundFile = new File(filename);
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		auline.start();
		int nBytesRead = 0;
		// 缓冲
		byte[] abData = new byte[512];
		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}
	}
}