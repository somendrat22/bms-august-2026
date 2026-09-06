package com.acciojobs.bms_august.controllers;

import com.acciojobs.bms_august.dtos.request.CreateMovieRequest;
import com.acciojobs.bms_august.enums.OperationType;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.services.AuthService;
import com.acciojobs.bms_august.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private AuthService authService;
    private MovieService movieService;

    @Autowired
    public MovieController(AuthService authService,
                           MovieService movieService
    ){
        this.authService = authService;
        this.movieService = movieService;
    }

    @PostMapping("/register-movie")
    public ResponseEntity registerMovie(
            @RequestBody CreateMovieRequest createMovieRequest,
            @RequestHeader String bmsToken
            ){
        Employee internalEmp = authService.isEmployeeHavingAccess(bmsToken, OperationType.CREATE_MOVIE.getValue());
        return new ResponseEntity(movieService.registerMovie(createMovieRequest, internalEmp), HttpStatus.CREATED);
    }
}
