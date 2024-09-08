package com.revlearn.team1.service.page;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.model.Page;

public interface PageService {
    public Page getPageById(Long pageId);

    public Page createPage(Long moduleId, Page page);

    public Page updatePage(Long pageId, Page page);

    public MessageDTO deletePage(Long pageId);
}
