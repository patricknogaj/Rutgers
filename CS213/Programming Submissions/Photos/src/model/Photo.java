/**
 * Photo.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents a photo in a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class Photo implements Serializable {

	private static final long serialVersionUID = -2759544425727486297L;

	private String filepath;
	private String caption;
	private long datePhoto;
	private long fileSize;

	private File imageFile;

	private transient ObservableList<Tag> tagList;
	private TreeMap<String, Tag> tagMap;

	/**
	 * Constructor that takes a String as the filePath.
	 * 
	 * @param fileName
	 */
	public Photo(String filepath) {
		this.filepath = filepath;
		caption = "";
		datePhoto = new File(filepath).lastModified();
		fileSize = 0;
		tagList = FXCollections.observableArrayList();
		tagMap = new TreeMap<String, Tag>();
	}

	/**
	 * Overloaded constructor which takes a Photo object.
	 * 
	 * @param photo : Photo object to copy.
	 */
	public Photo(Photo photo) {
		this.imageFile = photo.imageFile;
		this.filepath = photo.filepath;
		this.caption = photo.caption;
		this.datePhoto = photo.datePhoto;
		this.fileSize = photo.fileSize;

		tagMap = new TreeMap<String, Tag>();
		tagList = FXCollections.observableArrayList();
	}

	/**
	 * Overloaded constructor which takes a File object and extracts data.
	 * 
	 * @param imageFile: A file object utilized to store data.
	 */
	public Photo(File imageFile) {
		this.imageFile = imageFile;
		filepath = imageFile.getAbsolutePath();
		caption = "";
		datePhoto = imageFile.lastModified();
		fileSize = 0;
		tagList = FXCollections.observableArrayList();
		tagMap = new TreeMap<String, Tag>();
	}

	/**
	 * Adds a tag to the TreeMap<String, Tag> based on index.
	 * 
	 * @param tagName  : String object containing the value of the tag name.
	 * @param tagValue : String object containing the value of the tag value.
	 * 
	 * @return index of where it was placed in the TreeMap<String, Tag> or -1 if not
	 *         added.
	 */
	public int addTag(String tagName, String tagValue) {
		String tagKey = Tag.makeKey(tagName, tagValue);
		Tag temp = tagMap.get(tagKey);

		System.out.println(temp);

		if (temp == null) {
			temp = new Tag(tagName, tagValue);
			tagMap.put(tagKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}

	/**
	 * Places the tag in the appropriate spot in the tagList.
	 * 
	 * @param tag : Tag object
	 * 
	 * @return an index for where the Tag will be placed in the list.
	 */
	private int indexInsertedSorted(Tag tag) {
		if (tagList.isEmpty()) {
			tagList.add(tag);
			return 0;
		} else {
			for (int i = 0; i < tagList.size(); i++) {
				if (tag.compareTo(tagList.get(i)) < 0) {
					tagList.add(i, tag);
					return i;
				}
			}
			tagList.add(tag);
			return tagList.size() - 1;
		}
	}

	/**
	 * Removes a Tag from the TreeMap<String, Tag> and the ObservableList<Tag> based
	 * on index.
	 * 
	 * @param index : index of where the Tag is in the list which is derived from
	 *              UserController.
	 * 
	 * @return true | false based on if the Tag was deleted.
	 */
	public boolean deleteTag(int index) {
		String key = tagList.get(index).getKey();
		Tag temp = tagMap.get(key);

		if (temp != null) {
			tagMap.remove(key);
			tagList.remove(index);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Converts the lastModified() from file to a legible String.
	 * 
	 * @param time : long object gathered from the File object.
	 * 
	 * @return A String in the format of YYYY-MM-DD HH:MM:SS
	 */
	public String epochToLocalTime(long time) {
		LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return datetime.format(formatter);
	}

	/**
	 * Converts the lastModified() from file to a LocalDateTime object.
	 * 
	 * @return a DateTimeObject utilized for search by dates.
	 */
	public LocalDateTime getLocalDateTime() {
		long time = this.datePhoto;
		LocalDateTime thisDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
		return thisDateTime;
	}

	/**
	 * Checks to see if FROM date picker, and TO date picker are legible in terms of
	 * logic.
	 * 
	 * @param from: Object from DatePicker
	 * @param to: Object from DatePicker
	 * 
	 * @return true | false depending if search is in range.
	 */
	public boolean isInDateRange(LocalDate from, LocalDate to) {
		long time = this.datePhoto;
		LocalDateTime thisDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());

		int thisMonthValue = thisDateTime.getMonthValue();
		int thisDayValue = thisDateTime.getDayOfMonth();
		int thisYearValue = thisDateTime.getYear();

		int fromMonthValue = from.getMonthValue();
		int fromDayValue = from.getDayOfMonth();
		int fromYearValue = from.getYear();

		int toMonthValue = to.getMonthValue();
		int toDayValue = to.getDayOfMonth();
		int toYearValue = to.getYear();

		if (thisYearValue >= fromYearValue && thisYearValue <= toYearValue) {
			if (thisMonthValue >= fromMonthValue && thisMonthValue <= toMonthValue) {
				System.out.println(thisYearValue + "::" + thisMonthValue + "::" + thisDayValue);

				if (thisDayValue >= fromDayValue && thisDayValue <= toDayValue) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Taking a TagConditional object and evaluating if true or false.
	 * 
	 * @param tc: AND, OR, NOT object
	 * 
	 * @return true | false if it can be evaluated.
	 */
	public boolean matchesTagConditional(TagConditional tc) {
		if (tc.isAnd()) {
			return evaluateAnd(tc);
		} else if (tc.isOr()) {
			return evaluateOr(tc);
		} else if (tc.isNot()) {
			return evaluateNot(tc);
		} else if (tc.isSearchNoConditional()) {
			return evaluateSearchNoConditional(tc);
		} else {
			System.out.println("TagConditional does not match AND, OR, or NOT");
			return false;
		}
	}

	/**
	 * Evaluates search with AND operator.
	 * 
	 * @param tc: AND tag conditional object
	 * 
	 * @return true | false if the objects match.
	 */
	private boolean evaluateAnd(TagConditional tc) {
		boolean foundTag1 = false;
		boolean foundTag2 = false;

		for (Tag tag : tagMap.values()) {
			if (tc.getTag1().equals(tag)) {
				foundTag1 = true;
			}

			if (tc.getTag2().equals(tag)) {
				foundTag2 = true;
			}

			if (foundTag1 && foundTag2) {
				break;
			}
		}
		return foundTag1 && foundTag2;
	}

	/**
	 * Evaluates search with OR operator.
	 * 
	 * @param tc: OR tag conditional object
	 * 
	 * @return true | false if the objects match.
	 */
	private boolean evaluateOr(TagConditional tc) {
		// check to see if tc.tag1 OR tc.tag2 is in this photo
		boolean foundTag1 = false;
		boolean foundTag2 = false;

		for (Tag tag : tagMap.values()) {
			if (tc.getTag1().equals(tag)) {
				foundTag1 = true;
				break;
			}

			if (tc.getTag2().equals(tag)) {
				foundTag2 = true;
				break;
			}
		}
		return foundTag1 || foundTag2;
	}

	/**
	 * Evaluates search with NOT operator.
	 * 
	 * @param tc: NOT tag conditional object
	 * 
	 * @return true | false if the objects match.
	 */
	private boolean evaluateNot(TagConditional tc) {
		// check to see if tc.tag1 and tc.tag2 or NOT in this photo
		boolean foundTag1 = false;
		boolean foundTag2 = false;

		for (Tag tag : tagMap.values()) {
			if (tc.getTag1().equals(tag)) {
				foundTag1 = true;
			}

			if (tc.getTag2().equals(tag)) {
				foundTag2 = true;
				break;
			}
		}
		return foundTag1 && !foundTag2;
	}

	/**
	 * Evaluates search with no conditional
	 * 
	 * @param tc TagConditional object with no conditional
	 * 
	 * @return true | false if the objects match
	 */
	private boolean evaluateSearchNoConditional(TagConditional tc) {
		boolean foundTag1 = false;

		for (Tag tag : tagMap.values()) {
			if (tc.getTag1().equals(tag)) {
				foundTag1 = true;
				break;
			}
		}

		return foundTag1;
	}

	/**
	 * Generates the Key of a Photo object by utilizing the File name.
	 * 
	 * @param fileName: String of name of the File
	 * 
	 * @return : String of file name which is lowercased to enforce constraints.
	 */
	public static String makeKey(String fileName) {
		return fileName.toLowerCase();
	}

	/**
	 * Return the file path of a Photo object.
	 * 
	 * @return String containing the file path.
	 */
	public String getFilepath() {
		return filepath;
	}

	/**
	 * Get the File object of a Photo object.
	 * 
	 * @return a File object.
	 */
	public File getImageFile() {
		return imageFile;
	}

	/**
	 * Accessor to compute and return the target filename within filepath
	 * 
	 * @author Gemuele Aludino
	 * 
	 * @return substring of filepath, resultant: filename of photo
	 */
	public String getFilename() {
		return filepath.substring(filepath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * Gets the caption of a Photo.
	 * 
	 * @return A String object with the caption.
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * 
	 * Gets the Date Modified in a String format.
	 * 
	 * @return A String object of the Date Modified in format YYYY-MM-DD HH:MM:SS
	 */
	public String getDatePhoto() {
		return epochToLocalTime(datePhoto);
	}

	/**
	 * Gathers the key of a Photo object used for a TreeMap<String, Photo> in Album.
	 * 
	 * @return a String of the Key of the current Photo object.
	 */
	public String getKey() {
		return makeKey(filepath);
	}

	/**
	 * Gets the File size of an Photo object.
	 * 
	 * @return Long object of the File size.
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * Gets the modified date of a File object in its raw form.
	 * 
	 * @return long containing the file.lastModified() data.
	 */
	public long getModifiedDate() {
		return datePhoto;
	}

	/**
	 * Gets the TreeMap<String, Tag> of a Photo object.
	 * 
	 * @return TreeMap<String, Tag> of tags.
	 */
	public TreeMap<String, Tag> getTagMap() {
		return tagMap;
	}

	/**
	 * Gets the ObservableList<Tag> from the current Photo object.
	 * 
	 * @return ObservableList<Tag>
	 */
	public ObservableList<Tag> getTagList() {
		return tagList;
	}

	/**
	 * Sets the caption of the current Photo object.
	 * 
	 * @param caption : String of caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * Sets the Tag List of the current Photo object.
	 * 
	 * @param tagList : ObservableList<Tag>
	 */
	public void setTagList(ObservableList<Tag> tagList) {
		this.tagList = tagList;
	}

	/**
	 * Sets the date photo of the current Photo object.
	 * 
	 * @param time : long
	 */
	public void setDataPhoto(long time) {
		datePhoto = time;
	}

	/**
	 * Sets the file size into KB by dividing (B/1024).
	 * 
	 * @param size : long
	 */
	public void setSizePhoto(long size) {
		fileSize = (size / 1024);
	}

	/**
	 * Compares two Photo objects
	 * 
	 * @param o an Object, preferably a Photo
	 * 
	 * @return the compareTo value of filepath, otherwise -1 if o is not an instance
	 *         of Photo
	 */
	public int compareTo(Object o) {
		if (o != null && o instanceof Photo) {
			Photo p = (Photo) (o);

			return filepath.compareTo(p.filepath);
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Photo) {
			Photo p = (Photo) (o);

			/**
			 * If filenames are equal: Compare dates. If dates are equal: Compare captions.
			 * If captions equal: return true; Else return false. Else return false. Else
			 * return false.
			 */
			return filepath.contentEquals(p.filepath)
					? ((datePhoto == p.datePhoto) ? ((caption.equals(p.caption)) ? true : false) : true)
					: true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return filepath + " (" + epochToLocalTime(datePhoto) + "): \"" + caption + "\"";
	}

}
