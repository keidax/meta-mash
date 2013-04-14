package code;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

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
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.mp4.Mp4Tag;

public class AudioTaggerUtilities {
	public static void main(String[] args) {

		try {
			File testFile = new File("test_in/song_01.mp3");
			AudioFile f = AudioFileIO.read(testFile);
//			MP3File f = new Aud
//			Tag tag = f.getTag();
			Tag tag = f.getTag();
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
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setFileArtworkAndTags(File targetFile, File artworkFile, boolean addArtwork, HashMap<String, String> yamlData) {
		try {
			AudioFile audioFile = AudioFileIO.read(targetFile);
			Tag tag = audioFile.getTag();
			if(addArtwork){
				Artwork cover = ArtworkFactory.createArtworkFromFile(artworkFile);
				System.out.println("adding artwork of type " + cover.getMimeType() + " from " + artworkFile + " to " + targetFile);
				tag.deleteArtworkField();
				tag.addField(cover);
				audioFile.commit();
			}
			
			for(Entry<String, String> entry: yamlData.entrySet()){
				if(entry.getKey().equals("artist")){
					tag.deleteField(FieldKey.ARTIST);
					tag.addField(FieldKey.ARTIST, entry.getValue());
				} else if (entry.getKey().equals("album")){
					tag.deleteField(FieldKey.ALBUM);
					tag.addField(FieldKey.ALBUM, entry.getValue());
				} else if (entry.getKey().equals("title")){
					tag.deleteField(FieldKey.TITLE);
					tag.addField(FieldKey.TITLE, entry.getValue());
				}
			}
			
			
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
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
