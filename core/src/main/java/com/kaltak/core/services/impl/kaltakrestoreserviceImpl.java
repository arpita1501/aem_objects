package com.kaltak.core.services.impl;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.kaltak.core.services.kaltak_restore_service;

@Component(service = kaltak_restore_service.class)
public class kaltakrestoreserviceImpl implements kaltak_restore_service {

    private static final String NEWS_ROOT = "/content/kaltak/en/news";
    private static final String ARCHIVE_ROOT = "/content/kaltak/en/archive";
    private static final String ORIGINAL_PATH_PROPERTY = "originalPath";
    private static final String SUBSERVICE = "content-writer";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public boolean restoreArticle(String originalPath) {

        Map<String, Object> serviceMap = new HashMap<>();
        serviceMap.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(serviceMap)) {
            Session session = resolver.adaptTo(Session.class);
            if (originalPath == null || originalPath.isEmpty()) {
                throw new Exception("Path is empty");
            }

            if (!originalPath.startsWith(NEWS_ROOT)) {
                throw new Exception("Path is not valid");
            }
            String articleName = originalPath.substring(originalPath.lastIndexOf("/") + 1);
            String archivePath = ARCHIVE_ROOT + "/" + articleName;

            if (!session.nodeExists(archivePath)) {
                throw new Exception("Not able to find Archived Article");
            }

            if (session.nodeExists(originalPath)) {
                throw new Exception("Article already exists at original location");
            }

            session.move(archivePath, originalPath);
            Node restoredNode = session.getNode(originalPath);
            Node contentNode = restoredNode.getNode("jcr:content");

            if (contentNode.hasProperty(ORIGINAL_PATH_PROPERTY)) {
                contentNode.getProperty(ORIGINAL_PATH_PROPERTY).remove();
            }

            session.save();
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}