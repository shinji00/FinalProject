package com.sj.cy.random;
import java.util.List;

import com.sj.cy.member.Member;

public interface RandomMapper {

	List<Member> getIdList();

	int getIdCount();

	int checkID(String sid);

}
