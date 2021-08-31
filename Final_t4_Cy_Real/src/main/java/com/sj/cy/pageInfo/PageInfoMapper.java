package com.sj.cy.pageInfo;
import com.sj.cy.PageInfoVO;
import com.sj.cy.member.Member;

public interface PageInfoMapper {

	int defaultAutoFill(Member m);

	PageInfoVO getPageInfoById(String c_id);

	void update(PageInfoVO lp);

	void byeById(String id);

	void setProfile(PageInfoVO pp);

}
