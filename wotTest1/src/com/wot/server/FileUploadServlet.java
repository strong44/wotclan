package com.wot.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 * servlet to handle file upload requests
 * 
 * @author hturksoy
 * 
 */
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "\\";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // process only multipart requests
        if (ServletFileUpload.isMultipartContent(req)) {

            // Create a factory for disk-based file items
            FileItemFactory factory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            try {
            	
//                List<FileItem> items = upload.parseRequest(req);
//                for (FileItem item : items) {
//                    // process only file upload - discard other form item types
//                    if (item.isFormField()) continue;
//                    
//                    String fileName = item.getName();
//                    // get only the file name not whole path
//                    if (fileName != null) {
//                        fileName = FilenameUtils. getName(fileName);
//                    }
//
//                    File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
//                    if (uploadedFile.createNewFile()) {
//                        item.write(uploadedFile);
//                        resp.setStatus(HttpServletResponse.SC_CREATED);
//                        resp.getWriter().print("The file was created successfully.");
//                        resp.flushBuffer();
//                    } else
//                        throw new IOException("The file already exists in repository.");
//                }
            	
            	
            	
            
                resp.setContentType("text/plain"); 


                FileItemIterator iterator = upload.getItemIterator(req);

                while (iterator.hasNext()) {
                    FileItemStream item = iterator.next();

                    InputStream stream = item.openStream();

                    if (item.isFormField()) {
                        System.out.println("Got a form field: " + item.getFieldName()  + " " +item);



                    } else{
                    	 System.out.println("Got an uploaded file: " + item.getFieldName() +
                                  ", name = " + item.getName());
                        int len;
                        byte[] buffer = new byte[8192];
                        while ((len = stream.read(buffer, 0, buffer.length)) != -1) {

                          resp.getOutputStream().write(buffer, 0, len);

                        }

                    }

                }
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred while creating the file : " + e.getMessage());
            }

        } else {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                            "Request contents type is not supported by the servlet.");
        }
    }
}