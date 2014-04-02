package Cliente;

public class Tarea_Impl implements Tarea {

    public boolean proceso() {
        int a = 2, b = 3, c;
        c = a + b;
        System.out.println("suma de dos numeros: " + c);
        return true;
    }
}
