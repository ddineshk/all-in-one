package org.ddk.spring.store.web.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ddk.spring.store.domain.Account;
import org.ddk.spring.store.domain.Category;

import org.springframework.beans.support.PagedListHolder;

public class NewAccountAction extends BaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AccountActionForm acctForm = (AccountActionForm) form;
		if (AccountActionForm.VALIDATE_NEW_ACCOUNT.equals(acctForm.getValidate())) {
			Account account = acctForm.getAccount();
			String username = acctForm.getAccount().getUsername();
			String favCategory = acctForm.getFavCategoryName();
			Category category = getStore().getCategory(favCategory);
			account.setFavCategory(category);
			getStore().insertAccount(account);
			acctForm.setAccount(getStore().getAccount(username));
			acctForm.setUsername(account.getUsername());
			acctForm.setPassword(account.getPassword());
			if (request.getParameter("account.displayMyList") != null) {
				PagedListHolder myList = new PagedListHolder(getStore().getProductListByCategory(acctForm.getFavCategoryName()));
				myList.setPageSize(4);
				acctForm.setMyList(myList);
			}
			if (request.getParameter("account.displayBanner") != null) {
				String bannerName = getStore().getBannerNameForUser(username);
				acctForm.setBannerName(bannerName);
			}
			request.getSession().setAttribute("accountForm", acctForm);
			request.getSession().removeAttribute("workingAccountForm");
			return mapping.findForward("success");
		} else {
			request.setAttribute("message","Your account was not created because the submitted information was not validated.");
			return mapping.findForward("failure");
		}
	}
}
