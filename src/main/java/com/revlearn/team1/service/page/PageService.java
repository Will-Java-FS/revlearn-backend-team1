package com.revlearn.team1.service.page;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.page.PageReqDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.model.Page;

public interface PageService {
    public PageResDTO getPageById(Long pageId);

    public PageResDTO createPage(Long moduleId, PageReqDTO pageReqDTO);

    public PageResDTO updatePage(Long pageId, PageReqDTO pageReqDTO);

    public MessageDTO deletePage(Long pageId);
}
