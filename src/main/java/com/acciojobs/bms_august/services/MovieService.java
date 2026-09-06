package com.acciojobs.bms_august.services;

import com.acciojobs.bms_august.constants.NotificationTemplateConfig;
import com.acciojobs.bms_august.dtos.common.NotificationContext;
import com.acciojobs.bms_august.dtos.request.CreateMovieRequest;
import com.acciojobs.bms_august.enums.MovieStatus;
import com.acciojobs.bms_august.enums.NotificationChannel;
import com.acciojobs.bms_august.enums.NotificationPriority;
import com.acciojobs.bms_august.enums.NotificationStatus;
import com.acciojobs.bms_august.models.Employee;
import com.acciojobs.bms_august.models.Movie;
import com.acciojobs.bms_august.models.Notification;
import com.acciojobs.bms_august.models.User;
import com.acciojobs.bms_august.repositories.MovieRepository;
import com.acciojobs.bms_august.utilities.SystemUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class MovieService {

    private MovieRepository movieRepository;
    private UserService userService;
    private ExecutorService executorService;
    private NotificationService notificationService;

    @Autowired
    public MovieService(MovieRepository movieRepository,
                        UserService userService,
                        ExecutorService executorService,
                        NotificationService notificationService){
        this.movieRepository = movieRepository;
        this.userService = userService;
        this.executorService = executorService;
        this.notificationService = notificationService;
    }

    private Movie saveOrUpdateMovie(Movie movie){
        return this.movieRepository.save(movie);
    }

    //TODO - All the customers who are subscribed to our notification service - Should recieve notification this movie is launched on our platform and book tickets
    public Movie registerMovie(CreateMovieRequest createMovieRequest, Employee internalEmp){
        Movie movie = this.mapToMovie(createMovieRequest, internalEmp);
        movie = this.saveOrUpdateMovie(movie);
        // Notify -
        List<User> subscribedUsers = this.userService.getEmailSubscribedUser();

        Notification notification = Notification.builder()
                .notificationId(SystemUtility.generate("NOTIFICATION"))
                .notificationChannel(NotificationChannel.MAIL)
                .receipts(subscribedUsers)
                .notificationPriority(NotificationPriority.URGENT)
                .notificationStatus(NotificationStatus.DRAFT)
                .templateId(NotificationTemplateConfig.MOVIE_LAUNCHED_TEMPLATE_ID)
                .createdBy("system")
                .updatedBy("system")
                .build();

        NotificationContext notificationContext = new NotificationContext();
        Map<String, String> context = notificationContext.getEmailContext();

        context.put("title", movie.getTitle());
        context.put("originalTitle", movie.getOriginalTitle());
        context.put("synopsis", movie.getSynopsis());
        context.put("language", movie.getLanguage().toString());
        context.put("genre", movie.getGenre());
        context.put("durationMinutes", movie.getDurationMinutes().toString());
        context.put("releaseDate", movie.getReleaseDate().toString());
        context.put("censorCertificate", movie.getCensorCertificate());
        context.put("director", movie.getDirector());
        context.put("producer", movie.getProducer());
        context.put("castMembers", movie.getCastMembers());
        context.put("musicDirector", movie.getMusicDirector());
        context.put("productionHouse", movie.getProductionHouse());
        context.put("country", movie.getCountry());
        context.put("trailerUrl", movie.getTrailerUrl());
        context.put("posterUrl", movie.getPosterUrl());
        context.put("bannerUrl", movie.getBannerUrl());
        context.put("imdbRating", movie.getImdbRating().toString());

        context.put("logoUrl", "https://cdn.aptoide.com/imgs/0/9/e/09e834e598179218f4e0deb989eba2d2_fgraphic.jpg");
       context.put("bookingUrl", "https://in.bookmyshow.com/explore/home/hyderabad");

        executorService.submit(() -> {
            notificationService.sendNotification(notification, notificationContext);
        });

        return movie;
    }



    private Movie mapToMovie(CreateMovieRequest request, Employee creator) {

        return Movie.builder()
                .movieCode(SystemUtility.generate("MOVIE"))
                .title(request.getTitle())
                .originalTitle(request.getOriginalTitle())
                .synopsis(request.getSynopsis())
                .language(request.getLanguage())
                .genre(request.getGenre())
                .durationMinutes(request.getDurationMinutes())
                .releaseDate(request.getReleaseDate())
                .censorCertificate(request.getCensorCertificate())
                .director(request.getDirector())
                .producer(request.getProducer())
                .castMembers(request.getCastMembers())
                .musicDirector(request.getMusicDirector())
                .productionHouse(request.getProductionHouse())
                .country(request.getCountry())
                .trailerUrl(request.getTrailerUrl())
                .posterUrl(request.getPosterUrl())
                .bannerUrl(request.getBannerUrl())
                .imdbRating(request.getImdbRating())
                .status(determineMovieStatus(request.getReleaseDate()))
                .active(true)
                .createdBy(creator.getEmail())
                .updatedBy(creator.getEmail())
                .build();
    }

    private MovieStatus determineMovieStatus(LocalDate releaseDate) {

        if (releaseDate == null) {
            return MovieStatus.COMING_SOON;
        }

        if (releaseDate.isAfter(LocalDate.now())) {
            return MovieStatus.COMING_SOON;
        }

        return MovieStatus.NOW_SHOWING;
    }

}
