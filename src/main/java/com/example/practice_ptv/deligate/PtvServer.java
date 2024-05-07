package com.example.practice_ptv.deligate;

import com.openapi.demo.client.api.XLocateApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PtvServer {
    private final XLocateApi xlocaAPI;

    public PtvServer() {
        this.xlocaAPI = new XLocateApi();
    }

    @GetMapping("{address}")
    public ResponseEntity<String> test(@PathVariable String address) {

        System.out.println(xlocaAPI.locations(address, null));
        return ResponseEntity.ok("Ok");
    }
}
