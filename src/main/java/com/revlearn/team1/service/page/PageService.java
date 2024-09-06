package com.revlearn.team1.service.page;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.model.ModulePage;

public interface PageService {
    public ModulePage getPageById(Long pageId);

    public ModulePage createPage(Long moduleId, ModulePage page);

    public ModulePage updatePage(Long pageId, ModulePage page);

    public MessageDTO deletePage(Long pageId);
}
