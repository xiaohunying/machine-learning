package com.ayawala.machinelearningalgorithms.api;

import com.ayawala.machinelearningalgorithms.model.TagSummary;
import com.ayawala.machinelearningalgorithms.service.TagsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api")
public class TagsController {

    private final TagsService tagsService;

    public TagsController(final TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @RequestMapping(value = "tags")
    public List<TagSummary> tags() {
        return tagsService.getTagsPopulation();
    }

}
