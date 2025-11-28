package database.dao;

import database.model.Pokemon;
import database.model.PokemonStatus;
import database.model.PokemonTotais;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonDAO {
    private Connection connection;

    private String selectAll = "SELECT * FROM tb_pokemon";
    private String insert = "INSERT INTO tb_pokemon (pokemon, tipo) VALUES (?,?)";
    private String update = "UPDATE tb_pokemon SET pokemon = ?, tipo = ? WHERE id = ?";
    private String delete = "DELETE FROM tb_pokemon WHERE id = ?";

    private String selectByType= "SELECT id, pokemon, tipo FROM tb_pokemon WHERE tipo = ?";
    private String selectStatusView = "SELECT id, pokemon, tipo, status, motivo_remocao FROM vw_pokemon_status";
    private String selectTotalsView = "SELECT tipo, total_antes, removidos, total_depois FROM vw_pokemon_totais";


    private PreparedStatement pstSelectAll;
    private PreparedStatement pstInsert;
    private PreparedStatement pstUpdate;
    private PreparedStatement pstDelete;
    private PreparedStatement pstSelectByType;
    private PreparedStatement pstSelectStatusView;
    private PreparedStatement pstSelectTotalsView;



    public PokemonDAO(Connection connection) throws SQLException {
        this.connection = connection;
        pstSelectAll = connection.prepareStatement(selectAll);
        pstInsert = connection.prepareStatement(insert);
        pstUpdate = connection.prepareStatement(update);
        pstDelete = connection.prepareStatement(delete);
        pstSelectByType = connection.prepareStatement(selectByType);
        pstSelectStatusView = connection.prepareStatement(selectStatusView);
        pstSelectTotalsView = connection.prepareStatement(selectTotalsView);

    }

    public List<Pokemon> selectAll()  {
        List<Pokemon> pokemonList = new ArrayList<Pokemon>();
        ResultSet rs = null;
        try {
            rs = pstSelectAll.executeQuery();
            while(rs.next()){
                Pokemon pokemon = new Pokemon();
                pokemon.setId(rs.getInt(1));
                pokemon.setPokemonName(rs.getString(2));
                pokemon.setPokemonType(rs.getString(3));
                pokemonList.add(pokemon);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao selecionar todos os pokemons");
            throw new RuntimeException(e);
        }

        return pokemonList;
    }

    public boolean existsInTable(String table, int id) {
        String sql = "SELECT 1 FROM " + table + " WHERE id = ? LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro checando duplicidade em " + table, e);
        }
    }




    public void sortPokemonByType()  {
        List<Pokemon> pokemons = null;
        pokemons = selectAll();
        String table = "";

        for (Pokemon p : pokemons) {
            switch (p.getPokemonType().toLowerCase()) {
                case "fogo": table = "tb_pokemon_fogo"; break;
                case "eletrico": table = "tb_pokemon_eletrico"; break;
//                case "voador": table = "tb_pokemon_voador"; break;
                default: table = null;
            }

            if (table == null) {
                System.out.println("Tipo desconhecido para nome=" + p.getPokemonName() + " e id=" + p.getId());
                continue;
            }

            if (existsInTable(table, p.getId())) {
                System.out.println("O id: " + p.getId() + " já existe na tabela " + table );
                continue;
            }

            String sql = "INSERT INTO " + table + " (id, pokemon, tipo) VALUES (?,?,?)";

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                pst.setInt(1, p.getId());
                pst.setString(2, p.getPokemonName());
                pst.setString(3, p.getPokemonType());
                pst.executeUpdate();
                System.out.println("Inserido em " + table + " para id=" + p.getId());
            } catch (SQLException e) {
                System.out.println("Erro inserindo em " + table + " para id=" + p.getId());
                e.printStackTrace();
            }
        }
    }

    public long insertPokemon(String pokemon, String type){
          try {
            pstInsert.setString(1, pokemon);
            pstInsert.setString(2, type);
            return pstInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void saveTotalDuplicate(String tipo, int quantidade, int duplicados) {
        // Ajuste o nome da tabela para o que você está usando: tb_pokemon_totalizador
        String sql = """
        INSERT INTO tb_pokemon_totalizador (tipo, quantidade, duplicados)
        VALUES (?, ?, ?)
        ON DUPLICATE KEY UPDATE
            quantidade = VALUES(quantidade),
            duplicados = VALUES(duplicados)
    """;

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, tipo);
            pst.setInt(2, quantidade);
            pst.setInt(3, duplicados);
            pst.executeUpdate();
        } catch (SQLException e) {
            // O erro está sendo relançado aqui, o que é bom para depuração
            throw new RuntimeException("Erro inserindo no totalizador", e);
        }
    }



    public void generateTotalByType() {
        // 1. Pega todos os pokémons em uma única chamada
        List<Pokemon> pokemons = selectAll();

        // Mapa para armazenar os resultados: <Tipo, <Nome do Pokémon, Contagem>>
        Map<String, Map<String, Integer>> countsPerType = new HashMap<>();

        // 2. Itera sobre a lista UMA ÚNICA VEZ para calcular todos os totais
        for (Pokemon p : pokemons) {
            String tipo = p.getPokemonType();
            String nome = p.getPokemonName();

            // Obtém ou cria o mapa de contagem para este tipo
            Map<String, Integer> namesCount = countsPerType.computeIfAbsent(tipo, k -> new HashMap<>());

            // Incrementa a contagem para o nome do Pokémon
            namesCount.merge(nome, 1, Integer::sum);
        }

        // 3. Processa os resultados e salva no banco de dados
        for (Map.Entry<String, Map<String, Integer>> entry : countsPerType.entrySet()) {
            String tipo = entry.getKey();
            Map<String, Integer> namesCount = entry.getValue();

            // A quantidade total é a soma de todas as contagens para este tipo
            int quantidade = namesCount.values().stream().mapToInt(Integer::intValue).sum();

            // Calcula os duplicados: (contagem - 1) para cada nome com contagem > 1
            int duplicados = 0;
            for (Integer count : namesCount.values()) {
                if (count > 1) {
                    duplicados += (count - 1);
                }
            }

            // Salva usando o novo método UPSERT
            saveTotalDuplicate(tipo, quantidade, duplicados);
        }

        // Não há mais necessidade do bloco try-catch, pois a lógica de banco de dados
        // foi movida para o saveTotalDuplicate, que já trata a exceção.
    }


    public void deleteDuplicatesFrom(String tableName) {
        // CORREÇÃO: A lista de colunas no INSERT deve corresponder
        // exatamente aos valores e à ordem do SELECT.
        // A sua tabela espera (pokemon, tipo, original_id).
        String insertLogSql = """
        INSERT INTO tb_pokemon_deletados (pokemon, tipo, original_id)
        SELECT p1.pokemon, p1.tipo, p1.id
        FROM %s p1
        JOIN %s p2
          ON p1.pokemon = p2.pokemon
         AND p1.tipo = p2.tipo
         AND p1.id > p2.id
    """.formatted(tableName, tableName);

        // A query de DELETE não muda.
        String deleteSql = """
        DELETE p1 FROM %s p1
        JOIN %s p2
          ON p1.pokemon = p2.pokemon
         AND p1.tipo = p2.tipo
         AND p1.id > p2.id
    """.formatted(tableName, tableName);

        // Como nenhuma query usa '?', podemos usar um único Statement para ambas.
        try (java.sql.Statement stmt = connection.createStatement()) {
            // 1. Loga os registros que SERÃO deletados.
            int logged = stmt.executeUpdate(insertLogSql);

            // 2. Deleta esses mesmos registros da tabela de origem.
            int deleted = stmt.executeUpdate(deleteSql);

            System.out.println("Tabela: " + tableName);
            System.out.println(" - Registros de log criados: " + logged);
            System.out.println(" - Registros duplicados deletados: " + deleted);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro removendo duplicados da tabela " + tableName, e);
        }
    }

    public void deleteAllDuplicatesFromTypeTables() {
        deleteDuplicatesFrom("tb_pokemon_fogo");
        deleteDuplicatesFrom("tb_pokemon_eletrico");
//        deleteDuplicatesFrom("tb_pokemon_voador");
    }

    public List<PokemonStatus> viewListPokemonStatus() {
        List<PokemonStatus> lista = new ArrayList<>();
        try (ResultSet rs = pstSelectStatusView.executeQuery()) {
            while (rs.next()) {
                PokemonStatus ps = new PokemonStatus();
                ps.setId(rs.getInt("id"));
                ps.setPokemon(rs.getString("pokemon"));
                ps.setTipo(rs.getString("tipo"));
                ps.setStatus(rs.getString("status"));
                ps.setMotivoRemocao(rs.getString("motivo_remocao"));
                lista.add(ps);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar vw_pokemon_status", e);
        }
        return lista;
    }

    public List<PokemonTotais> viewListPokemonTotais() {
        List<PokemonTotais> lista = new ArrayList<>();

        try (ResultSet rs = pstSelectTotalsView.executeQuery()) {
            while (rs.next()) {
                PokemonTotais pt = new PokemonTotais();
                pt.setTipo(rs.getString("tipo"));
                pt.setTotalAntes(rs.getInt("total_antes"));
                pt.setRemovidos(rs.getInt("removidos"));
                pt.setTotalDepois(rs.getInt("total_depois"));
                lista.add(pt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar vw_pokemon_totais", e);
        }

        return lista;
    }



    public void printPokemonStatus() {
        List<PokemonStatus> lista = viewListPokemonStatus();

        System.out.println("===== STATUS DOS POKÉMONS =====");

        for (PokemonStatus ps : lista) {
            System.out.printf(
                    "ID: %d | Nome: %-12s | Tipo: %-10s | Status: %-8s | Motivo: %s%n",
                    ps.getId(),
                    ps.getPokemon(),
                    ps.getTipo(),
                    ps.getStatus(),
                    ps.getMotivoRemocao() == null ? "-" : ps.getMotivoRemocao()
            );
        }

        System.out.println("================================");
    }

    public void printPokemonTotais() {
        List<PokemonTotais> lista = viewListPokemonTotais();

        System.out.println("====== TOTAL DE POKEMON =======");

        for (PokemonTotais pt : lista) {
            System.out.println("ID: " + pt.getTipo() + " | Total: " + pt.getTotalAntes() + " | Removidos: " + pt.getRemovidos() + " | Total: " + pt.getTotalDepois());
        }
    }





}
