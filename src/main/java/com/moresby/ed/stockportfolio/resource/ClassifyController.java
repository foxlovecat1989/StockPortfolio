package com.moresby.ed.stockportfolio.resource;

import com.moresby.ed.stockportfolio.domain.HttpResponse;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNameExistException;
import com.moresby.ed.stockportfolio.exception.domain.classify.ClassifyNotFoundException;
import com.moresby.ed.stockportfolio.exception.handler.ClassifyExceptionHandling;
import com.moresby.ed.stockportfolio.service.ClassifyService;
import com.moresby.ed.stockportfolio.domain.Classify;
import com.moresby.ed.stockportfolio.domain.TStock;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.moresby.ed.stockportfolio.constant.WatchlistImplConstant.WATCHLIST_DELETED_SUCCESSFULLY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/classify")
@RequiredArgsConstructor
public class ClassifyController extends ClassifyExceptionHandling {

    private final ClassifyService classifyService;

    @GetMapping(path = "/findAll", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('admin:create', 'manage:create')")
    public ResponseEntity<List<Classify>> findAllClassify() {
        var classifies = classifyService.findAllClassify();

        return new ResponseEntity<>(classifies, HttpStatus.OK);
    }

    @GetMapping(path = "/{classifyName}", produces = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('admin:read', 'manage:read')")
    public ResponseEntity<List<TStock>> findStocksByClassifyName(@PathVariable("classifyName") String classifyName) {
        var stocks = classifyService.findStocksByClassifyName(classifyName);

        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyAuthority('admin:create', 'manage:create')")
    public ResponseEntity<Classify> createClassify(@RequestBody Classify classify)
            throws InterruptedException, ClassifyNameExistException {
        var newClassify =  classifyService.createClassify(classify);

         return new ResponseEntity<>(newClassify, HttpStatus.CREATED);
    }

    @PatchMapping
    @PreAuthorize(value = "hasAnyAuthority('admin:update', 'manage:update')")
    public ResponseEntity<Classify> updateClassify(@RequestBody Classify classify)
            throws ClassifyNotFoundException {
        var updateClassify = classifyService.updateClassifyName(classify);

        return new ResponseEntity<>(updateClassify, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{classifyName}")
    @PreAuthorize(value = "hasAnyAuthority('admin:delete')")
    public ResponseEntity<HttpResponse> deleteByName(@PathVariable("classifyName") String classifyName)
            throws ClassifyNotFoundException {
        classifyService.deleteByName(classifyName);

        return response(HttpStatus.NO_CONTENT, WATCHLIST_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(
                new HttpResponse(
                        httpStatus.value(),
                        httpStatus,
                        httpStatus.getReasonPhrase().toUpperCase(),
                        message),
                httpStatus);
    }

}
