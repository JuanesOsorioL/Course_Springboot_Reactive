package com.example.springboot_reactor.app;


import com.example.springboot_reactor.app.models.Comentarios;
import com.example.springboot_reactor.app.models.Usuario;
import com.example.springboot_reactor.app.models.UsuarioComentarios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringbootReactorApplication implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // ejecutarPrimerSegmento();
        // agregando_el_map_simple();
        // agregando_el_map_a_modelo();
        // agregando_el_map_con_el_operador_filter();
        // ejemplo_map_filter_consumiendo_una_lista();
        // ejemplo_flapmap();
        // ejemplo_flapmap_de_usuario_to_list();
        // ejemplo_collectList();
        // ejemplo_usuarioComentarioFlatMap();
        // ejemplo_usuarioComentarioZipWith();
        // ejemplo_usuarioComentarioZipWith_Other_forma2();
        ejemplo_ZipWith_Rangos_combinarDosFlujosDistintos();
    }

    private void ejemplo_ZipWith_Rangos_combinarDosFlujosDistintos() {
        Flux<Integer> rangos = Flux.range(0, 4);
        Flux.just(1, 2, 3,4)
                .map(i -> i * 2)
                .zipWith(rangos, (uno,dos) -> String.format("Primer Flux %d, Segundo Flux %d", uno, dos))
                .subscribe(v-> log.info(String.valueOf(v)));
    }






    private void ejemplo_usuarioComentarioZipWith_Other_forma2() {
        Mono<Usuario> usuarioMono=Mono.fromCallable(()->new Usuario("juan","carlos"));

        Mono<Comentarios> comentariosUsuarioMono=Mono.fromCallable(()->{
            Comentarios comentarios=new Comentarios();
            comentarios.addComentario("primer comentario");
            comentarios.addComentario("segundo comentario");
            comentarios.addComentario("tercero comentario");
            comentarios.addComentario("cuarto comentario");
            return comentarios;
        });

        Mono<UsuarioComentarios> UsuarioComentarios = usuarioMono
                .zipWith(comentariosUsuarioMono)
                        .map(tuple->{
                            Usuario usuario=tuple.getT1();
                            Comentarios comentarios=tuple.getT2();
                            return new UsuarioComentarios(usuario,comentarios);
                        });

        UsuarioComentarios.subscribe(usuarioComentarios -> log.info(usuarioComentarios.toString()));
    }

    private void ejemplo_usuarioComentarioZipWith() {
        Mono<Usuario> usuarioMono=Mono.fromCallable(()->new Usuario("juan","carlos"));

        Mono<Comentarios> comentariosUsuarioMono=Mono.fromCallable(()->{
            Comentarios comentarios=new Comentarios();
            comentarios.addComentario("primer comentario");
            comentarios.addComentario("segundo comentario");
            comentarios.addComentario("tercero comentario");
            comentarios.addComentario("cuarto comentario");
            return comentarios;
        });

        Mono<UsuarioComentarios> UsuarioComentarios = usuarioMono
                .zipWith(comentariosUsuarioMono,
                        (usuario, comentarios) -> new UsuarioComentarios(usuario,comentarios));

        UsuarioComentarios.subscribe(usuarioComentarios -> log.info(usuarioComentarios.toString()));
    }

    private void ejemplo_usuarioComentarioFlatMap() {
      Mono<Usuario> usuarioMono=Mono.fromCallable(()->new Usuario("juan","carlos"));

      Mono<Comentarios> comentariosUsuarioMono=Mono.fromCallable(()->{
          Comentarios comentarios=new Comentarios();
          comentarios.addComentario("primer comentario");
          comentarios.addComentario("segundo comentario");
          comentarios.addComentario("tercero comentario");
          comentarios.addComentario("cuarto comentario");
          return comentarios;
      });

      usuarioMono.flatMap(usuario -> comentariosUsuarioMono.map(comentarios -> new UsuarioComentarios(usuario,comentarios)))
              .subscribe(usuarioComentarios -> log.info(usuarioComentarios.toString()));
    }

    private void ejemplo_collectList() {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("andres"," lopez"));
        usuariosList.add(new Usuario("pedro","santamaria"));
        usuariosList.add(new Usuario("juan"," osorio"));
        usuariosList.add(new Usuario("diego"," lopera"));
        usuariosList.add(new Usuario("matias"," avendaño"));
        usuariosList.add(new Usuario("lukas"," bueno"));
        usuariosList.add(new Usuario("valeria"," lopez"));
        usuariosList.add(new Usuario("juan"," lopera"));

        Flux.fromIterable(usuariosList)
                .collectList()
                .subscribe(lista -> {
                    lista.forEach(item -> log.info(item.toString()));
                        });
    }

    private void ejemplo_flapmap_de_usuario_to_list() {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("andres"," lopez"));
        usuariosList.add(new Usuario("pedro","santamaria"));
        usuariosList.add(new Usuario("juan"," osorio"));
        usuariosList.add(new Usuario("diego"," lopera"));
        usuariosList.add(new Usuario("matias"," avendaño"));
        usuariosList.add(new Usuario("lukas"," bueno"));
        usuariosList.add(new Usuario("valeria"," lopez"));
        usuariosList.add(new Usuario("juan"," lopera"));

        Flux.fromIterable(usuariosList)
                .map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
                .flatMap(nombre -> {
                    if (nombre.contains(("juan").toUpperCase())) {
                        return Mono.just(nombre);
                    }else {
                        return Mono.empty();
                    }

                })
                .map(nombre -> nombre.toLowerCase())

                .subscribe(e -> log.info(e.toString()));
    }

    private void ejemplo_flapmap() {
        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("andres lopez");
        usuariosList.add("pedro santamaria");
        usuariosList.add("juan osorio");
        usuariosList.add("diego lopera");
        usuariosList.add("matias avendaño");
        usuariosList.add("lukas bueno");
        usuariosList.add("valeria lopez");
        usuariosList.add("juan lopera");

        Flux.fromIterable(usuariosList)
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .flatMap(usuario -> {
                    if (usuario.getNombre().equalsIgnoreCase("juan")) {
                        return Mono.just(usuario);
                    }else {
                        return Mono.empty();
                    }

                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                })

        .subscribe(e -> log.info(e.toString()));
    }

    private void ejemplo_map_filter_consumiendo_una_lista() {
        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("andres lopez");
        usuariosList.add("pedro santamaria");
        usuariosList.add("juan osorio");
        usuariosList.add("diego lopera");
        usuariosList.add("matias avendaño");
        usuariosList.add("lukas bueno");
        usuariosList.add("valeria lopez");
        usuariosList.add("juan lopera");

        Flux<String> nombres = Flux.fromIterable(usuariosList);
        Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("juan"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("El nombre no puede ser vacio");
                    }
                    System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
                })

                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        usuarios.subscribe(
                e -> log.info(e.toString()),
                error -> log.error(error.getMessage()),
                () -> log.info("Ha finalizado la ejecución del observable con éxito")
        );
    }

    private void agregando_el_map_con_el_operador_filter() {
        Flux<Usuario> nombres = Flux.just("andres lopez", "pedro santamaria", "juan osorio", "diego lopera", "matias avendaño", "lukas bueno", "valeria lopez", "juan lopera")
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("juan"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
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
                .map(nombre -> new Usuario(nombre.toUpperCase(), null))
                .doOnNext(usuario -> {
                    if (usuario == null) {
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
