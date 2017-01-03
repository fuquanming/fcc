package com.fcc.commons.web.servlet;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fcc.commons.image.CodeImage;
import com.fcc.commons.web.common.Constants;

/**
 * <p>Description:随机码
 * 访问地址 /randCode?randCode
 * </p>
 * <p>Copyright:Copyright (c) 2009 </p>
 * @author 傅泉明
 * @version v1.0
 */
public class RandCodeServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the object.
     */
    public RandCodeServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //        ImageCaptchaService instance = new DefaultManageableImageCaptchaService();
        //        byte[] captchaChallengeAsJpeg = null;
        //        // the output stream to render the captcha image as jpeg into
        //        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        //        try {
        //            // get the session id that will identify the generated captcha. 
        //            //the same id must be used to validate the response, the session id is a good candidate!
        //            String captchaId = request.getSession().getId();
        //            // call the ImageCaptchaService getChallenge method
        //
        //            BufferedImage challenge = instance.getImageChallengeForID(captchaId, request.getLocale());
        //
        //            // a jpeg encoder
        //            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
        //            jpegEncoder.encode(challenge);
        //        } catch (IllegalArgumentException e) {
        //            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        //            return;
        //        } catch (CaptchaServiceException e) {
        //            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        //            return;
        //        }
        //        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        //        // flush it in the response
        //        response.setHeader("Cache-Control", "no-store");
        //        response.setHeader("Pragma", "no-cache");
        //        response.setDateHeader("Expires", 0);
        //        response.setContentType("image/jpeg");
        //        ServletOutputStream responseOutputStream = response.getOutputStream();
        //        responseOutputStream.write(captchaChallengeAsJpeg);
        //        responseOutputStream.flush();
        //        responseOutputStream.close();
        CodeImage instance = new CodeImage(120, 40, 4, 50, CodeImage.FONT_FILLING_SOLID, Color.BLACK);
        request.getSession().setAttribute(Constants.RAND_CODE_KEY, instance.getCode());
        instance.write(response.getOutputStream());
    }

    public void init() throws ServletException {
        // Put your code here
    }

}
