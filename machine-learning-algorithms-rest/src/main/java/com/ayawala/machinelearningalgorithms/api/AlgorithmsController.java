package com.ayawala.machinelearningalgorithms.api;

import com.ayawala.machinelearningalgorithms.model.Algorithm;
import com.ayawala.machinelearningalgorithms.model.AlgorithmImage;
import com.ayawala.machinelearningalgorithms.model.AlgorithmSummary;
import com.ayawala.machinelearningalgorithms.service.AlgorithmCatalog;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api")
public class AlgorithmsController {

    private final AlgorithmCatalog algorithmCatalog;

    public AlgorithmsController(final AlgorithmCatalog algorithmCatalog) {
        this.algorithmCatalog = algorithmCatalog;
    }

    @RequestMapping(path = "algorithms", method = RequestMethod.GET)
    public List<AlgorithmSummary> algorithms(@RequestParam(value = "tag", required = false) final String tag) {
        if(tag == null){
            return algorithmCatalog.getAlgorithmSummaries().stream().collect(Collectors.toList());
        } else {
            return algorithmCatalog.getAlgorithmSummariesByTag(tag).stream().collect(Collectors.toList());
        }
    }

    @RequestMapping(value = "algorithms/{algorithmId:.+}", method = RequestMethod.GET)
    public Algorithm algorithm(@PathVariable("algorithmId") final String algorithmId) {
        return algorithmCatalog.getAlgorithm(algorithmId);
    }

    @RequestMapping(value = "algorithms/{algorithmId}/images/**")
    public ResponseEntity<?> streamImage(@PathVariable("algorithmId") final String algorithmId,
                                        HttpServletRequest request) {
        final Algorithm algorithm = algorithmCatalog.getAlgorithm(algorithmId);
        if(algorithm == null) {
            return ResponseEntity.notFound().build();
        }

        final AlgorithmImage algorithmImage = getAlgorithmImageByRequestPath(algorithm, request.getRequestURI());
        if(algorithmImage == null){
            return ResponseEntity.notFound().build();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setCacheControl(CacheControl.maxAge(12, TimeUnit.HOURS).getHeaderValue());
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(algorithmImage.getContent()));
        httpHeaders.setContentLength(algorithmImage.getContent().length);

        if(algorithmImage.getMimeType() != null){
            httpHeaders.setContentType(MediaType.valueOf(algorithmImage.getMimeType()));
        }

        return new ResponseEntity<Object>(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    private AlgorithmImage getAlgorithmImageByRequestPath(final Algorithm algorithm, final String requestUrl) {
        final String path = "api/algorithms/" + algorithm.getId() + "/images/";
        final int index = requestUrl.indexOf(path);
        final String imagePath = requestUrl.substring(index + path.length());
        return algorithm.getImages().get(imagePath);
    }

    @RequestMapping(path = "test")
    public String test() {
        return "wu";
    }

}
