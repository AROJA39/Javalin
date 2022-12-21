package proyecto;

import io.javalin.Javalin;
import metodos.operaciones;

public class HelloWorld {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8000);

        app.get("/hello/:name", ctx -> {
            String valor = ctx.pathParam("name");
            operaciones evaluacion = new operaciones();
            if (evaluacion.valoracion(valor) == true) {
                ctx.result("Hello!: " + valor);
            } else {
                ctx.result("tu nombre no es aceptado");
            }

        });
    }
}
