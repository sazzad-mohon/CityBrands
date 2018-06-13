package com.shehala.citybrands.sectionindexlistview;

public class ItemsSections implements Item {

	private char sectionLetter;


	public char getSectionLetter() {
		return sectionLetter;
	}


	public void setSectionLetter(char sectionLetter) {
		this.sectionLetter = sectionLetter;
	}


	public boolean isSectionItem() {
		return true;
	}

}
