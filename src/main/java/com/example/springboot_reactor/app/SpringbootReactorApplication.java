package com.example.springboot_reactor.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootReactorApplication implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //  ejecutarPrimerSegmento();

        agregando_el_map();
    }

    private void agregando_el_map() {
        Flux<String> nombres = Flux.just("andres", "pedro", "juanes", "diego", "matias")
                .map(nombre -> {
                    return nombre.toUpperCase();
                })

                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("El nombre no puede ser vacio");
                    }
                    System.out.println(e);
                })

                .map(nombre -> {
                    return nombre.toLowerCase();
                });

        nombres.subscribe(
                e -> log.info(e),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecución del observable con éxito")
        );
    }





    private void ejecutarPrimerSegmento() {
        Flux<String> nombres = Flux.just("andres", "pedro", "juanes", "diego", "matias")
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("El nombre no puede ser vacio");
                    }
                    System.out.println(e);
                });

        nombres.subscribe(
                e -> log.info(e),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecución del observable con éxito")
        );
    }



}
