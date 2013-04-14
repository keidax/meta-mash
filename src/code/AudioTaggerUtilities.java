package code;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

public class AudioTaggerUtilities {
	public static void main(String[] args) {

		try {
			File testFile = new File("test_in/song_01.mp3");
//			AudioFile f = AudioFileIO.read(testFile);
			MP3File f = new MP3File(testFile);
//			Tag tag = f.getTag();
			ID3v24Tag tag = f.getID3v2TagAsv24();
			Artwork newCover = ArtworkFactory.createArtworkFromFile(new File("test_in/cover_03.jpg"));
			tag.deleteArtworkField();
			tag.addField(newCover);
			System.out.println(tag.toString());
			f.setTag(tag);
			f.commit();
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			e.printStackTrace();
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}

	}

	public static void setFileArtwork(File targetFile, File artworkFile) {
		try {
			MP3File audioFile = new MP3File(targetFile);
			ID3v24Tag tag = audioFile.getID3v2TagAsv24();

			Artwork cover = ArtworkFactory.createArtworkFromFile(artworkFile);
			System.out.println("adding artwork of type " + cover.getMimeType()
					+ " from " + artworkFile + " to " + targetFile);
			tag.deleteArtworkField();
			tag.addField(cover);
			System.out.println(tag.toString());
			audioFile.setTag(tag);
			audioFile.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			e.printStackTrace();
		} catch (CannotWriteException e) {
			e.printStackTrace();
		}
	}
}
