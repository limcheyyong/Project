package com.sc.api_backend.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadBase;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.sc.api_backend.model.login.LoginOutput;
import com.sc.api_backend.model.login.RespLogin;
import com.sc.api_backend.model.request.AdminListRequest;
import com.sc.api_backend.model.request.BaseRequest;
import com.sc.api_backend.model.request.DeleteAdmin;
import com.sc.api_backend.model.request.EditAdminRequest;
import com.sc.api_backend.model.request.InsertAdminRequest;
import com.sc.api_backend.model.request.LogRequest;
import com.sc.api_backend.model.request.LoginBackendRequest;
import com.sc.api_backend.model.request.NewsRequest;
import com.sc.api_backend.model.request.OpenData;
import com.sc.api_backend.model.request.UpdataPwRequest;
import com.sc.api_backend.model.response.ContentView;
import com.sc.api_backend.model.response.FileUploadResponse;
import com.sc.api_backend.model.response.NewsView;
import com.sc.api_backend.model.response.OperatingLogView;
import com.sc.api_backend.service.ApiBackendService;
import com.sc.base.controller.BaseController;
import com.sc.base.model.BaseModel;
import com.sc.db.admininfo.model.AdminInfo;
import com.sc.db.content.model.Content;
import com.sc.db.news.model.News;
import com.sc.util.FileUtil;
import com.sc.util.LogUtils;
import com.sc.util.TimerUtil;
import com.sc.util.ValidateUtil;

@Controller
@EnableAsync
@RequestMapping(value = "/api_backend")
@CrossOrigin("http://localhost:4200")
public class ApiBackendController extends BaseController {

	@Autowired
	private ApiBackendService apiBackendService;

	@RequestMapping(value = "/imageFile/**", method = RequestMethod.GET)
	public @ResponseBody HttpEntity<byte[]> loadFile(HttpServletRequest request, HttpServletResponse resp) {
		byte[] image = null;
		HttpHeaders headers = new HttpHeaders();
		String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		if (ValidateUtil.isBlank(fullPath))
			return null;
		String path = fullPath.replace("/api_backend/imageFile", "");
		if (ValidateUtil.isBlank(path))
			return null;

		String extension = (path.substring(path.lastIndexOf(".") + 1)).replace("/", "");
		if ("png".equals(extension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else if ("bdw".equals(extension)) {
			headers.setContentType(new MediaType("application", "octet-stream"));
		} else if ("jpg".equals(extension) || "jpeg".equals(extension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("mp4".equals(extension)) {
			headers.setContentType(new MediaType("video", "mp4"));
		} else if ("pdf".equals(extension)) {
			headers.setContentType(MediaType.APPLICATION_PDF);
		} else {
			return null;
		}
		String rootPath = System.getProperty("catalina.home");
		String filePath = rootPath + path;
		File dir = new File(filePath);
		try {
			image = org.apache.commons.io.FileUtils.readFileToByteArray(dir);
			headers.setContentLength(image.length);
			return new HttpEntity<byte[]>(image, headers);
		} catch (IOException e) {
			LogUtils.fileConteollerError(filePath + " loadFile has error : 輸出發生異常 =>" + e);
		} finally {
			System.gc();
		}
		return null;
	}

	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public @ResponseBody FileUploadResponse fileUpload(HttpServletRequest request, HttpServletResponse resp,
			@RequestParam("file") CommonsMultipartFile file) {
		FileUploadResponse respResult = new FileUploadResponse();
		int res = 0;
		String msg = "";

		try {
			Integer id = request.getParameter("id") != null ? Integer.valueOf(request.getParameter("id")) : null;
			Integer ftype = request.getParameter("ftype") != null ? Integer.valueOf(request.getParameter("ftype"))
					: null;

			if (file != null && !file.isEmpty()) {
				// set file path
				StringBuffer path = new StringBuffer();
				String timeStr = TimerUtil.getNowTimestamp();
				switch (ftype) {
				case 0:

					path.append("contentimage/");
					respResult.setfName(timeStr);
					break;
				case 1:

					path.append("newsimage/");
					respResult.setfName(timeStr);
					break;

				case 2: // news picture
					path.append("news/").append(id).append("/newsimage");
					respResult.setfName("newsImage");
					break;

				}
				Map<String, Object> result = FileUtil.uploadMultipartFile(file, path.toString());
				String filePath = result.get("path").toString();
				if (filePath != null && filePath.length() > 0)
					res = 1;

				respResult.setUrl(filePath);
				// save to db
				if (ValidateUtil.isNotBlank(filePath)) {
					if (ftype == 2) {
						News news = new News();
						news.setNewsId(id);
						news.setNewsImage(filePath);
						res = apiBackendService.updateNews(news, 1);
					}
				}
			}
		} catch (NoSuchMessageException e) {
			LogUtils.fileConteollerError(e.toString());
			msg = "file service has error , check log file.";
		}

		respResult.setRes(res);
		if (res == 1)
			msg = "儲存成功!";
		else
			msg = "儲存失敗!";

		respResult.setMsg(msg);
		return respResult;
	}

	@RequestMapping(value = "/queryNewsListOpenData", produces = "application/json")
	public @ResponseBody BaseModel queryNewsListOpenData(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody OpenData openData) {
		BaseModel res = new BaseModel();

		List<News> newsList = apiBackendService.queryNewsListOpenData(openData);
		res = basicOutput(newsList);
		return res;
	}

	// admin登入
	@RequestMapping(value = "/login_backend", produces = "application/json")
	public @ResponseBody BaseModel loginBackend(HttpSession session, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody LoginBackendRequest loginBackendRequest) {

		resp.setHeader("Access-Control-Allow-Origin", "*");

		RespLogin respLogin = new RespLogin();
		LoginOutput result = apiBackendService.loginBackend(loginBackendRequest);
		if (result != null) {
			String sessionId = (String) session.getId();
			result.setSessionId(sessionId);
			respLogin.setResult(true);
			respLogin.setData(result);
			respLogin.setMessage("登入成功");
			
			AdminInfo adminInfo = result.getAdminInfo();
			String strtp = String.valueOf(TimerUtil.getNowDate().getTime());
			session.setAttribute("time_key", strtp);
			session.setAttribute("loginSuccess", "yes");
			session.setAttribute("pass_id", adminInfo.getAdminId());
			session.setAttribute("adminInfo", adminInfo);
			session.setAttribute("serviceKey", result.getSecurityKey());
			System.out.println(session.getAttribute("adminInfo"));
			System.out.println(session.isNew());
		} else {
			respLogin.setResult(false);
			respLogin.setMessage("帳號密碼錯誤,請確認!");
		}
		return respLogin;
	}

	// admin列表
	@RequestMapping(value = "/adminList", produces = "application/json")
	public @ResponseBody BaseModel adminList(HttpSession session, HttpServletRequest request, HttpServletResponse resp,
			@RequestBody AdminListRequest adminListRequest) {
		BaseModel res = new BaseModel();
		System.out.println(session.isNew());
		AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		List<AdminInfo> adminList = apiBackendService.queryAdminList();
		res.setResult(true);
		res.setData(adminList);
		res.setMessage("成功");
		return res;
	}

	// 新增admin
	@RequestMapping(value = "/addAdmin", produces = "application/json")
	public @ResponseBody BaseModel addAdmin(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody InsertAdminRequest insertAdminRequest) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) httpSession.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}

		int result = apiBackendService.insertAdminInfo(insertAdminRequest, adminInfo.getAdminId());

		if (result == 1) {
			res.setResult(true);
			res.setMessage("新增成功");
		} else {
			res.setResult(false);
			res.setMessage("新增失敗");
		}
		return res;
	}

	// 修改admin
	@RequestMapping(value = "/editAdmin", produces = "application/json")
	public @ResponseBody BaseModel editAdmin(HttpSession session, HttpServletRequest request, HttpServletResponse resp,
			@RequestBody EditAdminRequest editAdminRequest) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}

		int result = apiBackendService.updateAdminInfo(editAdminRequest, adminInfo.getAdminId());
		if (result == 1) {
			res.setResult(true);
			res.setMessage("修改成功");
		} else {
			res.setResult(false);
			res.setMessage("修改失敗");
		}
		return res;
	}

	// 刪除admin
	@RequestMapping(value = "/deleteAdmin", produces = "application/json")
	public @ResponseBody BaseModel deleteAdmin(HttpSession session, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody DeleteAdmin deleteAdmin) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		int result = apiBackendService.removeAdminInfo(deleteAdmin, adminInfo.getAdminId());
		if (result == 1) {
			res.setResult(true);
			res.setMessage("刪除成功");
		} else {
			res.setResult(false);
			res.setMessage("刪除失敗");
		}
		return res;
	}

	// 修改密碼
	@RequestMapping(value = "/updataPw", produces = "application/json")
	public @ResponseBody BaseModel updataPw(HttpSession session, HttpServletRequest request, HttpServletResponse resp,
			@RequestBody UpdataPwRequest updataPwRequest) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		int result = apiBackendService.updataPassWord(updataPwRequest, adminInfo.getAdminId());
		if (result == 1) {
			res.setResult(true);
			res.setMessage("修改成功");
		} else {
			res.setResult(false);
			res.setMessage("修改失敗");
		}
		return res;
	}

	// 查詢Content
	@RequestMapping(value = "/queryContent", produces = "application/json")
	public @ResponseBody BaseModel queryContent(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody Content content) {

		BaseModel res = new BaseModel();

		ContentView contentView = apiBackendService.queryContent(content);
		res = basicOutput(contentView);
		return res;
	}

	// 更新Content資訊
	@RequestMapping(value = "/saveContent", produces = "application/json")
	public @ResponseBody BaseModel saveContent(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody ContentView contentView) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) httpSession.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		int result = apiBackendService.saveContent(contentView, adminInfo.getAdminId());
		if (result == 1) {
			res.setResult(true);
			res.setMessage("新增成功");
		} else {
			res.setResult(false);
			res.setMessage("新增失敗");
		}
		return res;
	}

	// ----------------News---------------
	// 加入新聞
	@RequestMapping(value = "/saveNews", produces = "application/json")
	public @ResponseBody BaseModel saveNews(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody News news) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) httpSession.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		int result = 0;
		if (news.getNewsId() == null)
			result = apiBackendService.addNews(news, adminInfo.getAdminId());
		else
			result = apiBackendService.updateNews(news, adminInfo.getAdminId());

		if (result == 1) {
			res.setResult(true);
			res.setMessage("儲存成功");
		} else {
			res.setResult(false);
			res.setMessage("儲存失敗");
		}
		return res;
	}

	// 查詢新聞列表
	@RequestMapping(value = "/queryNewsList", produces = "application/json")
	public @ResponseBody BaseModel queryNewsList(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody NewsRequest newsRequest) {
		BaseModel res = new BaseModel();

		List<News> newsList = apiBackendService.queryNewsList(newsRequest);
		res = basicOutput(newsList);
		return res;
	}

	// 查詢新聞
	@RequestMapping(value = "/queryNews", produces = "application/json")
	public @ResponseBody BaseModel queryNews(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody NewsRequest newsRequest) {
		BaseModel res = new BaseModel();

		News news = apiBackendService.queryNews(newsRequest);
		res = basicOutput(news);
		return res;
	}

	// 更新新聞資訊
	@RequestMapping(value = "/updateNews", produces = "application/json")
	public @ResponseBody BaseModel updateNews(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody News news) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) httpSession.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		int result = apiBackendService.updateNews(news, adminInfo.getAdminId());
		if (result == 1) {
			res.setResult(true);
			res.setMessage("新增成功");
		} else {
			res.setResult(false);
			res.setMessage("新增失敗");
		}
		return res;
	}

	// 刪除新聞資訊
	@RequestMapping(value = "/deleteNews", produces = "application/json")
	public @ResponseBody BaseModel deleteNews(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody NewsRequest newsRequest) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) httpSession.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}
		int result = apiBackendService.deleteNews(newsRequest, adminInfo.getAdminId());
		if (result == 1) {
			res.setResult(true);
			res.setMessage("新增成功");
		} else {
			res.setResult(false);
			res.setMessage("新增失敗");
		}
		return res;
	}

	// 前台分月查詢
	@RequestMapping(value = "/queryNewsViewList", produces = "application/json")
	public @ResponseBody BaseModel queryNewsViewList(HttpSession httpSession, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody NewsRequest newsRequest) {
		BaseModel res = new BaseModel();

		List<NewsView> newsViewList = apiBackendService.queryNewsViewList(newsRequest);
		res = basicOutput(newsViewList);
		return res;
	}

	// log列表
	@RequestMapping(value = "/queryLogList", produces = "application/json")
	public @ResponseBody BaseModel queryLogList(HttpSession session, HttpServletRequest request,
			HttpServletResponse resp, @RequestBody LogRequest logRequest) {
		BaseModel res = new BaseModel();
		AdminInfo adminInfo = (AdminInfo) session.getAttribute("adminInfo");
		if (adminInfo == null) {
			res.setResult(false);
			res.setMessage("unlogin");
			return res;
		}

		List<OperatingLogView> logList = apiBackendService.queryLogList(logRequest);
		res.setResult(true);
		res.setData(logList);
		res.setMessage("成功");
		return res;
	}

}
