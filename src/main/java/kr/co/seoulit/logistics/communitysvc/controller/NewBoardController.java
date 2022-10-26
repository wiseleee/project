package kr.co.seoulit.logistics.communitysvc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.seoulit.logistics.communitysvc.service.NewBoardService;
import kr.co.seoulit.logistics.communitysvc.to.NewBoardTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;

@RestController
@RequestMapping("/communitysvc/*")
public class NewBoardController {

	@Autowired
	private NewBoardService newBoardService;

	ModelMap map=null;

	private static Gson gson = new GsonBuilder().serializeNulls().create();

	@RequestMapping(value = "/newBoard/list", method = RequestMethod.GET)
	public ModelMap findNewBoardList(HttpServletRequest request, HttpServletResponse response) {
		map = new ModelMap();
		//System.out.println("컨트롤러");
		try {
			ArrayList<NewBoardTO> list = newBoardService.findNewBoardList();
			System.out.println("list"+list);
			map.put("boardlist", list);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/newBoard/register", method = RequestMethod.POST)
	public ModelMap registerNewBoard(NewBoardTO newBoard, HttpServletRequest request, HttpServletResponse response) {
		map = new ModelMap();
		System.out.println("newBoard"+newBoard);
		try {
			newBoardService.registerNewBoard(newBoard);
			map.put("errorCode", 0);
			map.put("errorMsg", "게시글이 등록되었습니다");
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out=response.getWriter();
			out.println("<script>alert('게시글이 등록되었습니다'); location.href='/community/board/view'; </script>");
			out.flush();
		//	response.sendRedirect("/community/board/view");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return null;
	}

	@RequestMapping(value = "/newBoard/detail", method = RequestMethod.GET)
	public ModelMap detailNewBoard(int id, HttpServletRequest request, HttpServletResponse response) {
		map = new ModelMap();
		try {
			NewBoardTO boardDetail = newBoardService.findNewBoardDetail(id);
			System.out.println("boardDetail?"+boardDetail);
			map.put("boardDetail", boardDetail);
			map.put("errorCode", 1);
			map.put("errorMsg", "성공");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}


	@RequestMapping(value = "/newBoard/remove", method = RequestMethod.DELETE)
	public ModelMap removeNewBoard(int id, HttpServletRequest request, HttpServletResponse response) {
		map = new ModelMap();
		System.out.println("id?"+id);
		try {
			newBoardService.removeNewBoard(id);
			map.put("errorCode", 0);
			map.put("errorMsg", "게시글이 삭제되었습니다");
			/*response.sendRedirect("/community/board/view");*/
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/newBoard/modify", method = RequestMethod.PUT)
	public ModelMap modifyNewBoard(  HttpServletRequest request, HttpServletResponse response) {
		map = new ModelMap();
		System.out.println(request.getParameter("updateData"));
		JSONObject obj = new JSONObject(request.getParameter("updateData")); //JSONOBJECT로 새객체 만들어서 문자열로 받아옴
		NewBoardTO newBoardTO = new NewBoardTO(); //새로운 TO만들기
		newBoardTO.setId(Integer.parseInt((String)obj.get("id"))); //각각의 id, title, content값 저장, id는 int형이라서 parseInt해서 String형으로 바꿔줌
		newBoardTO.setTitle((String)obj.get("title"));
		newBoardTO.setContent((String)obj.get("content"));
		System.out.println(newBoardTO.getId());
		try {
			newBoardService.modifyNewBoard(newBoardTO);
			map.put("errorCode", 0);
			map.put("errorMsg", "게시글이 수정되었습니다");
		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}
	/*@PutMapping("modifyTest")
	public ModelMap modifyTest(HttpServletRequest request, HttpServletResponse response){

		map = new ModelMap();
		String sendData = request.getParameter("sendData");
		System.out.println("ㅇㅇㅇ"+sendData);
		try{
			Gson gson = new Gson();
			TestTO moditest = gson.fromJson(sendData, TestTO.class);
			System.out.println(moditest);
			testService.modifyTest(moditest);
			map.put("errorMsg","success");
			map.put("errorCode", 0);

		} catch (Exception dae){
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", dae.getMessage());
		}
		return map;
	}*/

/*


	// delete
	@RequestMapping(value = "/board", method = RequestMethod.DELETE)
	public ModelMap deleteBoard(HttpServletRequest request, HttpServletResponse response) {

		map = new ModelMap();

		response.setContentType("application/json; charset=UTF-8");

		try {
			int board_seq = Integer.parseInt(request.getParameter("board_seq"));
			compInfoService.removeBoard(board_seq);
			map.put("errorMsg", "게시글이 삭제되었습니다");
			map.put("errorCode", 0);

		} catch (Exception e1) {
			e1.printStackTrace();
			map.put("errorCode", -1);
			map.put("errorMsg", e1.getMessage());
		}
		return map;
	}
	

	
	//download
	@RequestMapping(value="/board/download" ,method=RequestMethod.GET)
	public ModelMap downloadFile(HttpServletRequest request, HttpServletResponse response){
		map = new ModelMap();
		OutputStream servletoutputstream1 = null;
		//response.setCharacterEncoding("utf-8");		
		String tempFileName = request.getParameter("tempFileName");
		String fileName = request.getParameter("fileName");
		try {

			String filePath="C:\\dev\\nginx-1.21.6\\nginx-1.21.6\\html\\upload\\"+tempFileName;
			File tempFile = new File(filePath);
			int filesize = (int) tempFile.length();		
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename=" + "" + new String(fileName.getBytes(),"iso-8859-1"));
			response.setHeader("Content-Transper-Encoding","binary");
			response.setContentLength(filesize);
			
			servletoutputstream1 = response.getOutputStream();

			dumpFile(tempFile, servletoutputstream1);

			servletoutputstream1.flush();
			System.out.println("@@@@@@@@@@@다운로드되나??");

		}catch (IOException e){
			e.printStackTrace();
			map.clear();
			map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
        }catch (Exception e){
        	e.printStackTrace();
        	map.clear();
        	map.put("errorCode", -1);
			map.put("errorMsg", e.getMessage());
        }
	
		return map;
	}
	private void dumpFile(File realFile, OutputStream outputstream) {
		byte readByte[] = new byte[4096];
		try {
			BufferedInputStream bufferedinputstream = new BufferedInputStream(new FileInputStream(realFile));
			int i;
			while ((i = bufferedinputstream.read(readByte, 0, 4096)) != -1)
				outputstream.write(readByte, 0, i);		
				outputstream.close();
				bufferedinputstream.close();
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
	}*/


}
