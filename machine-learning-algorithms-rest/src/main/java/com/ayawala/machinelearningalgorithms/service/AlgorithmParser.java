package com.ayawala.machinelearningalgorithms.service;

import com.ayawala.machinelearningalgorithms.model.Algorithm;
import com.ayawala.machinelearningalgorithms.model.AlgorithmImage;
import com.ayawala.machinelearningalgorithms.model.AlgorithmMetadata;
import com.ayawala.machinelearningalgorithms.service.io.ContentNode;
import com.ayawala.machinelearningalgorithms.service.io.ContentRepository;
import com.ayawala.machinelearningalgorithms.service.markdown.MarkdownException;
import com.ayawala.machinelearningalgorithms.service.markdown.MarkdownProcessor;
import com.ayawala.machinelearningalgorithms.service.markdown.MarkdownRequest;
import com.ayawala.machinelearningalgorithms.service.markdown.MarkdownResult;
import com.ayawala.machinelearningalgorithms.util.Yaml;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AlgorithmParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmParser.class);

    private final MarkdownProcessor markdownProcessor;

    public AlgorithmParser(final MarkdownProcessor markdownProcessor){
        this.markdownProcessor = markdownProcessor;
    }

    public boolean parse(final Algorithm algorithm, final ContentRepository contentRepository) throws IOException {

        if(contentRepository.getContentNode("README.md") == null) {
            return false;
        }

        final MarkdownResult result = new MarkdownResult();
        final CharSource markdown = contentRepository.getContentNode("README.md").getByteSource()
                .asCharSource(Charsets.UTF_8);

        // Metadata
        extractMetadata(result, markdown);
        parseMetadata(algorithm, result.getMetadataContent());

        // Content
        final MarkdownRequest request = new MarkdownRequest();
        request.setMarkdown(result.getMarkdownContent());
        request.setImagePrefix("../api/algorithms/" + algorithm.getId() + "/images/");
        request.setLinkPrefix("/algorithms/" + algorithm.getId());
        markdownProcessor.process(result, request);
        algorithm.setHtmlContent(result.getHtmlContent());

        // images
        for(final String imagePath : result.getImages()){
            final ContentNode imageSource = contentRepository.getContentNode(imagePath);
            if(imageSource != null){
                final AlgorithmImage algorithmImage = new AlgorithmImage();
                algorithmImage.setPath(imagePath);
                algorithmImage.setContent(imageSource.getByteSource().read());
                algorithm.getImages().put(imagePath, algorithmImage);
            }
        }

        return true;
    }

    private void extractMetadata(final MarkdownResult result, final CharSource markdown)
            throws IOException, MarkdownException{
        if (markdown.readFirstLine().startsWith("~~~")) {
            final List<String> lines = markdown.readLines();
            final StringBuilder metadata = new StringBuilder();
            final StringBuilder content = new StringBuilder();

            boolean metadataLine = true;
            for(int i = 1; i < lines.size(); i++){
                final String line = lines.get(i);
                if(metadataLine) {
                    if(line.startsWith("~~~")){
                        metadataLine = false;
                    } else {
                        metadata.append(line).append("\n");
                    }
                } else {
                    content.append(line).append("\n");
                }
            }
            result.setMarkdownContent(content.toString());
            result.setMetadataContent(metadata.toString());
        } else {
            throw new MarkdownException("README.md must start with a metadata section.");
        }
    }

    private void parseMetadata(final Algorithm algorithm, final String metadata) throws IOException {
        final AlgorithmMetadata metadataContent = Yaml.readValue(metadata, AlgorithmMetadata.class);

        if(Strings.isNullOrEmpty(algorithm.getMetadata().getTitle())){
            algorithm.getMetadata().setTitle(metadataContent.getTitle());
        }
        if(algorithm.getMetadata().getAuthors().isEmpty()){
            algorithm.getMetadata().setAuthors(metadataContent.getAuthors());
        }
        if(Strings.isNullOrEmpty(algorithm.getMetadata().getSummary())){
            algorithm.getMetadata().setSummary(metadataContent.getSummary());
        }
        if(Strings.isNullOrEmpty(algorithm.getMetadata().getPublishDate())){
            algorithm.getMetadata().setPublishDate(metadataContent.getPublishDate());
        }
        if(Strings.isNullOrEmpty(algorithm.getMetadata().getUpdateDate())){
            algorithm.getMetadata().setUpdateDate(metadataContent.getUpdateDate());
        }
        if(algorithm.getMetadata().getTags().isEmpty()){
            algorithm.getMetadata().setTags(metadataContent.getTags());
        }
    }
}
