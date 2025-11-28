package database.model;

public class PokemonTotais {
    private String tipo;
    private int totalAntes;
    private int removidos;
    private int totalDepois;

    // getters e setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getTotalAntes() { return totalAntes; }
    public void setTotalAntes(int totalAntes) { this.totalAntes = totalAntes; }

    public int getRemovidos() { return removidos; }
    public void setRemovidos(int removidos) { this.removidos = removidos; }

    public int getTotalDepois() { return totalDepois; }
    public void setTotalDepois(int totalDepois) { this.totalDepois = totalDepois; }
}
