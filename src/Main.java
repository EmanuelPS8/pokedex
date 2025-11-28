
import database.connection.ConnectionFactory;
import database.dao.PokemonDAO;
import database.model.Pokemon;
import view.InitialFrame;
import view.MainFrame;

import java.sql.Connection;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
    	InitialFrame frame = new InitialFrame();
        frame.setLocationRelativeTo(null); // centraliza na tela
        frame.setVisible(true);
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection("localhost", "3306", "root", "1234", "pokedex");
            if (connection != null) {
                System.out.println("Conectado com sucesso!");
                PokemonDAO pokemonDAO = new PokemonDAO(connection);
//               pokemonDAO.insertPokemon("moltreas", "fogo");
//               pokemonDAO.insertPokemon("moltreas", "fogo");
//               pokemonDAO.insertPokemon("charmande", "fogo");
//               pokemonDAO.insertPokemon("zapdos", "eletrico");
//               pokemonDAO.insertPokemon("zapdos", "eletrico");
//               pokemonDAO.insertPokemon("pikachu", "eletrico");
//               pokemonDAO.insertPokemon("bulbassaur", "planta");
//               pokemonDAO.insertPokemon("bulbassaur", "planta");
//               pokemonDAO.insertPokemon("caterpie", "planta");
//               pokemonDAO.insertPokemon("Venossaur", "planta");
//               pokemonDAO.insertPokemon("Venossaur", "planta");
//               pokemonDAO.insertPokemon("gengar", "venenoso");
                System.out.println("Pokemon inserido com sucesso!");
                MainFrame mf = new MainFrame();
                
               
                List<Pokemon> pokemonList = pokemonDAO.selectAll();
                for (Pokemon pokemon : pokemonList) {
                    System.out.println("Erro ao listar pokemon");

                    System.out.println("ID: " + pokemon.getId());
                    System.out.println("Nome: " + pokemon.getPokemonName());
                    System.out.println("Tipo: " + pokemon.getPokemonType());
                    System.out.println("---------------------------------------");
                }
                pokemonDAO.sortPokemonByType();
                pokemonDAO.generateTotalByType();
                pokemonDAO.deleteAllDuplicatesFromTypeTables();
                pokemonDAO.printPokemonStatus();
                pokemonDAO.printPokemonTotais();
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar com o banco de dados!");
            e.printStackTrace();
        }


    }
}