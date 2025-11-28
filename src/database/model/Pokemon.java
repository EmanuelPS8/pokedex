package database.model;

public class Pokemon {

    private int id;
    private String pokemonName;
    private String pokemonType;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPokemonName() {
        return pokemonName;
    }
    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getPokemonType() {
        return pokemonType;
    }
    public void setPokemonType(String pokemonType) {
        this.pokemonType = pokemonType;
    }

}
