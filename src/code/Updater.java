package code;

import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JProgressBar;

public class Updater {
	JProgressBar progress;
	AtomicInteger totalSongs = new AtomicInteger();
	AtomicInteger songsCompleted = new AtomicInteger();
	
	public Updater(JProgressBar progress){
		this.progress = progress;
	}
	
	public synchronized void threadDone(){
		songsCompleted.incrementAndGet();
		System.out.println("converted " + songsCompleted.get() + "/" + totalSongs.get() + " songs");
		synchronized (progress) {
			progress.setValue((int) (songsCompleted.get()*100/totalSongs.doubleValue()));
		}
		if (songsCompleted.get() == totalSongs.get()) {
			System.out.println("All songs converted!");
		}
	}
	
	public void setTotalSongs(int total){
		totalSongs.set(total);
	}

}
