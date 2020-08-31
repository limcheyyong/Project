package com.sc.api_app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sc.api_app.model.request.BaseAppRequest;

import com.sc.api_app.model.response.BaseAppResponse;

import com.sc.api_app.service.ApiAppService;
import com.sc.base.controller.BaseController;


@Controller
@EnableAsync
@RequestMapping(value="/api_app")
public class ApiAppController  extends BaseController {

	@Autowired
	private ApiAppService apiAppService;
	

	
}
