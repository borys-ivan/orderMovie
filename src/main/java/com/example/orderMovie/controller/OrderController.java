package com.example.orderMovie.controller;

import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.dto.order.OrderCreateUpdateDto;
import com.example.orderMovie.dto.order.OrderViewDto;
import com.example.orderMovie.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@Api(tags = { "Movie Order API" }, description = "")
@RequestMapping(value = "/system/order")
public class OrderController {

    private final OrderService orderService;


    @GetMapping(value = "/read/{orderID}")
    @ApiOperation(value = "Read the movie order by ID")
    public ResponseEntity<OrderViewDto> read(@PathVariable long orderID) {
        log.info("read movie order");

        return new ResponseEntity<>(orderService.read(orderID), HttpStatus.OK);
    }

    @PostMapping(value = "/post")
    @ApiOperation(value = "Create the movie order")
    public ResponseEntity<OrderViewDto> post(@RequestBody OrderCreateUpdateDto dto) {
        log.info("creating movie order");

        return new ResponseEntity<>(orderService.post(dto), HttpStatus.OK);
    }

    @PutMapping(value = "/put/{orderID}")
    @ApiOperation(value = "Put the movie order")
    public ResponseEntity<OrderViewDto> put(
            @PathVariable long orderID,
            @RequestBody OrderCreateUpdateDto dto) {
        log.info("Putting movie order");
        return new ResponseEntity<>(orderService.put(orderID, dto), HttpStatus.OK);
    }

    @PatchMapping(value = "/patch/{orderID}")
    @ApiOperation(value = "Update the movie order")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Order",
                    value = "Order fields to patch",
                    required = true,
                    dataType = "OrderModelEdit",
                    paramType = "body")
    })
    public ResponseEntity<OrderViewDto> patch(
            @PathVariable long orderID,
            @ApiParam(name = "Order", value = "Order fields to update", hidden = false)
            @RequestBody Map<String, Object> mapPatch) {
        log.info("Patching movie order");
        return new ResponseEntity<>(orderService.patch(orderID, mapPatch), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{orderID}")
    @ApiOperation(value = "Delete the movie order")
    public ResponseEntity<OrderViewDto> delete(@PathVariable long orderID) {
        log.info("Patching movie order");

        return new ResponseEntity<>(orderService.delete(orderID), HttpStatus.OK);
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "List movie orders")
    @ResponseBody
    public ResponseEntity<SearchResult<OrderViewDto>> list(
            @RequestBody SearchParams searchParams) {
        log.info("Searching in DB");

        return new ResponseEntity<>(orderService.searchList(searchParams), HttpStatus.OK);
    }

}
