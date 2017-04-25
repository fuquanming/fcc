package com.fcc.web.sys.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Description: hightchart 导出图片</p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 * @Email fuquanming@gmail.com
 */
@Controller
public class HightchartController {
	private Logger logger = Logger.getLogger(HightchartController.class);
	/**
	 * hightchart导出图片 svg格式
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/manage/sys/hightchart/export.do")
	public void export(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String type = request.getParameter("type");
		String svg = request.getParameter("svg");
		String fileName = request.getParameter("filename");
		logger.info(svg);
		fileName = fileName == null ? "chart" : fileName;
		ServletOutputStream out = response.getOutputStream();
		try {
			if (type != null && svg != null) {
				svg = svg.replaceAll(":rect", "rect");
				String ext = "";
				Transcoder t = null;
				if (type.equals("image/png")) {
					ext = "png";
					t = new PNGTranscoder();
				} else if (type.equals("image/jpeg")) {
					ext = "jpg";
					t = new JPEGTranscoder();
				} else if (type.equals("application/pdf")) {
					ext = "pdf";
					t = (Transcoder) new PDFTranscoder();
				} else if (type.equals("image/svg+xml"))
					ext = "svg";
				
				//如果该文件的名称有中文，就会出现乱码，在这对文件的名称进行转码，就可以解决乱码的问题
		        if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
		        	fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");//firefox浏览器
		        } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
		        	fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
		        }
		        /**
		         * 如果有换行，对于文本文件没有什么问题，但是对于其它格式，
		         * 比如AutoCAD、Word、Excel等文件下载下来的文件中就会多出一些换行符0x0d和0x0a，
		         * 这样可能导致某些格式的文件无法打开，有些也可以正常打开。
		         * 同时response.reset()这种方式也能清空缓冲区, 防止页面中的空行等输出到下载内容里去
		         */
		        response.reset(); //非常重要
				
				response.addHeader("Content-Disposition",
						"attachment; filename=" + fileName + "." + ext);
				response.addHeader("Content-Type", type);

				if (null != t) {
					TranscoderInput input = new TranscoderInput(
							new StringReader(svg));
					TranscoderOutput output = new TranscoderOutput(out);

					try {
						t.transcode(input, output);
					} catch (TranscoderException e) {
						out.print("Problem transcoding stream. See the web logs for more details.");
						e.printStackTrace();
					}
				} else if (ext.equals("svg")) {
					OutputStreamWriter writer = new OutputStreamWriter(out,
							"UTF-8");
					writer.append(svg);
					writer.close();
				} else
					out.print("Invalid type: " + type);
			} else {
				response.addHeader("Content-Type", "text/html");
				out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted."
								+ "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
			}
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
}
