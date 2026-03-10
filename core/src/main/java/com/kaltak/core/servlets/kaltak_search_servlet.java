package com.kaltak.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.kaltak.core.services.kaltak_search_service;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/kaltak/search",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET
        }
)
public class kaltak_search_servlet extends SlingSafeMethodsServlet {

    @Reference
    private kaltak_search_service kaltaksearchservice;

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("q");
        if(keyword == null || keyword.trim().isEmpty()){
            response.setStatus(400);
            response.getWriter().write("q is required in this");
            return;
        }

        response.setContentType("application/json");
        response.getWriter().write(
                kaltaksearchservice.searchArticles(keyword).toString()
        );
    }
}