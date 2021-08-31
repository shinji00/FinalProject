package com.sj.cy.pay;
public interface PayMapper {

	void dotoriAutoFill(String c_id);

	int checkDotori(String c_id);

	void updateDotori(Pay pay);

	void setMusic(Music music);

	String getMusic(String c_id);
	
	void musicAutoFill(String c_id);

	void setTheme(Music music);

}
