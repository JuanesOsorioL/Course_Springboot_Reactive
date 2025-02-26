package com.example.springboot_reactor.app;


import com.example.springboot_reactor.app.models.Usuario;
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
       // agregando_el_map_simple();
        // agregando_el_map_a_modelo();
        agregando_el_map_con_el_operador_filter();
    }

    private void agregando_el_map_con_el_operador_filter() {
        Flux<Usuario> nombres = Flux.just("andres lopez", "pedro santamaria", "juan osorio", "diego lopera", "matias avendaño", "lukas bueno", "valeria lopez","juan lopera")
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(),nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("juan"))
                .doOnNext(usuario -> {
                    if (usuario ==null) {
                        throw new RuntimeException("El nombre no puede ser vacio");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })

                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        nombres.subscribe(
                e -> log.info(e.toString()),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecución del observable con éxito")
        );
    }

    private void agregando_el_map_a_modelo() {
        Flux<Usuario> nombres = Flux.just("andres", "pedro", "juanes", "diego", "matias")
                .map(nombre -> new Usuario(nombre.toUpperCase(),null))
                .doOnNext(usuario -> {
                    if (usuario ==null) {
                        throw new RuntimeException("El nombre no puede ser vacio");
                    }
                    System.out.println(usuario.getNombre());
                })

                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        nombres.subscribe(
                e -> log.info(e.toString()),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecución del observable con éxito")
        );
    }

    private void agregando_el_map_simple() {
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
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecución del observable con éxito");
                    }
                }
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
