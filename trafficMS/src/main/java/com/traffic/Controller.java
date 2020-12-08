package com.traffic;

import DTOs.Commands.AddTrafficCommand;
import DTOs.Queries.GetIntersectionQuery;
import Handlers.HandlerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/getIntersection/{name}")
    public String getUser(String name) {
        GetIntersectionQuery query = new GetIntersectionQuery(name);
        HandlerFactory factory = new HandlerFactory();
        return factory.createHandler(query).handle();
    }

    @PostMapping(path = "/addTraffic", consumes = "application/json", produces = "application/json")
    public String createUser(@RequestBody AddTrafficCommand command)
    {
        HandlerFactory factory = new HandlerFactory();
        return factory.createHandler(command).handle();
    }
}