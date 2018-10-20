package com.blebail.components.webservices.fake;

import com.blebail.components.core.pagination.Page;
import com.blebail.components.webservices.controller.ResourceController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/uuid/resource")
@RestController
public class UuidResourceController extends ResourceController<UuidResourceDto, UuidResource, String, UuidResourceService> {

    @Inject
    public UuidResourceController(UuidResourceService service) {
        super(service, new UuidResourceDtoConversion());
    }

    @RequestMapping(method = GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<UuidResourceDto> getAll(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) String page,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) String size,
            @RequestParam(value = "direction", defaultValue = DEFAULT_DIRECTION) String direction,
            @RequestParam(value = "properties", defaultValue = DEFAULT_PROPERTIES) String... properties) {

        return super.getAll(null, page, size, direction, properties);
    }
}
