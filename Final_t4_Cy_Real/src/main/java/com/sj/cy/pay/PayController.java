package com.sj.cy.pay;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sj.cy.PageInfoVO;
import com.sj.cy.member.MDAO;
import com.sj.cy.member.Member;
import com.sj.cy.pageInfo.PageInfoDAO;

@Controller
public class PayController {

	@Autowired
	private MDAO mdao;

	@Autowired
	private PayDAO pdao;
	
	@Autowired
	private PageInfoDAO pjdao;
	
	@RequestMapping(value = "pay.go", method = RequestMethod.GET)
	public String payGo(HttpServletRequest request) {
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");//p_id 없을때 default
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("contentPage", "pay/payPop.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		return "index";
}
	
	@RequestMapping(value = "pay.call", method = RequestMethod.POST)
	public String payOpen(HttpServletRequest request, HttpServletResponse response) {
		String url = pdao.getPay(request);
		if(url != null) {
			request.setAttribute("successUrl", url);
			request.setAttribute("contentPage", "pay/payPop2.jsp");
		}else {
			request.setAttribute("contentPage", "pay/paySuccess.jsp");
		}
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");//p_id 없을때 default
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		return "index";
	}
	
	
	@RequestMapping(value = "pay.after", method = RequestMethod.GET)
	public String payAfter(HttpServletRequest request) {

		int addDotori = Integer.parseInt(request.getParameter("dotoriNum"));
		Member m = (Member) request.getSession().getAttribute("loginMember");
		String c_id = m.getC_id(); //로그인중인 id값 불러오기
		Pay pay = new Pay(c_id, addDotori);
		int olddotori = m.getC_dotori();
		m.setC_dotori(olddotori+addDotori);
		request.getSession().setAttribute("loginMember", m);
		
		if(addDotori != -1) {
			pdao.updateData(request, pay); 
		}
		return "pay/paySuccess";
	}
	
	@RequestMapping(value = "shop.go", method = RequestMethod.GET)
	public String shopGo(HttpServletRequest request) {
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");//p_id 없을때 default
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("contentPage", "pay/shopList.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		
		return "index";
	}
	
	@RequestMapping(value = "shop.bgm.go", method = RequestMethod.GET)
	public String shopBgmGo(HttpServletRequest request) {
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");//p_id 없을때 default
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("contentPage", "pay/shopBgm.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		
		return "index";
	}
	
	@RequestMapping(value = "shop.theme.go", method = RequestMethod.GET)
	public String shopThemeGo(HttpServletRequest request) {
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");//p_id 없을때 default
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("contentPage", "pay/shopTheme.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		
		return "index";
	}
	
	@RequestMapping(value = "shop.buy", method = RequestMethod.GET)
	public String shopBuy(HttpServletRequest request) {
		Member m = (Member) request.getSession().getAttribute("loginMember");
		String c_id = m.getC_id();
		pjdao.getInfoById(c_id, request);
		PageInfoVO p = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		int checkdotori = m.getC_dotori();
		
		if(checkdotori < 3) {
			//잔액부족할때
			request.setAttribute("shopResult", "도토리가 부족합니다!");
			request.setAttribute("c_dotori", checkdotori);
			
		}else {
			Pay pay = new Pay(c_id, -3);
			pdao.updateData(request, pay);
			m.setC_dotori(checkdotori-3);
			request.setAttribute("shopResult", "브금 구입 완료");
			
			//브금에 브금 세팅
			String bgm = request.getParameter("bgm");
			p.setP_music(bgm);
			Music music = new Music(c_id, bgm);
			pdao.setMusic(music, request);
			request.setAttribute("c_dotori", checkdotori-3);
			request.getSession().setAttribute("loginMember", m);
			request.getSession().setAttribute("pageInfo", p);
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("contentPage", "pay/shopSuccess.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");//p_id 없을때 default
		}
		
		return "index";
	}
	
	
	@RequestMapping(value = "shop.buyTheme", method = RequestMethod.GET)
	public String shopBuyTheme(HttpServletRequest request) {
		Member m = (Member) request.getSession().getAttribute("loginMember");
		String c_id = m.getC_id();
		
		pjdao.getInfoById(c_id, request);
		PageInfoVO p = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		
		int checkdotori = m.getC_dotori();
		
		if(checkdotori < 2) {
			//잔액부족할때
			request.setAttribute("shopResult", "도토리가 부족합니다!");
			request.setAttribute("c_dotori", checkdotori);
			
		}else {
			Pay pay = new Pay(c_id, -2);
			pdao.updateData(request, pay);
			m.setC_dotori(checkdotori-2);
			request.setAttribute("shopResult", "테마 구입 완료");
			
			//테마에 테마 세팅
			String theme = request.getParameter("theme");
			p.setP_theme(theme);
			//브금세팅이랑 똑같으니까 기능그대로씀
			Music music = new Music(c_id, theme);
			pdao.setTheme(music, request);
			request.setAttribute("c_dotori", checkdotori-2);
			request.getSession().setAttribute("loginMember", m);
			request.getSession().setAttribute("pageInfo", p);
		}
		
		request.setAttribute("homePage", "home.jsp");
		request.setAttribute("titlePage", "title.jsp");
		request.setAttribute("contentPage", "pay/shopSuccess.jsp");
		request.setAttribute("profilePage", "profilePage.jsp");
		request.setAttribute("bannerPage", "banner.jsp");
		
		PageInfoVO pp = (PageInfoVO) request.getSession().getAttribute("pageInfo");
		if(pp!=null) {
			request.setAttribute("bgm", pp.getP_music());//브금세팅
			request.setAttribute("theme", pp.getP_theme());//테마세팅
		}else {
			request.setAttribute("theme", "default");
		}
		
		return "index";
	}

	

}
