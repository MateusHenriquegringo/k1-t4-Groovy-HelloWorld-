package repository.auxiliary

import DB.PostgresDatabaseConnection
import enums.CompetenciaENUM
import model.Candidato
import model.builder.CandidatoBuilder
import model.builder.director.CandidatoDirector

import java.sql.*

class CandidatoCompetenciaDAO implements AuxiliaryTablesCRUD<Candidato, Long, CompetenciaENUM> {

    private Connection connection = PostgresDatabaseConnection.getConnection()

    private CandidatoDirector director = new CandidatoDirector()
    private CandidatoBuilder builder = new CandidatoBuilder()

    CandidatoCompetenciaDAO(Connection connection) {
        this.connection = connection
    }

    CandidatoCompetenciaDAO() {
    }

    @Override
    void create(Long candidatoID, List<CompetenciaENUM> competencias) {
        String command = "INSERT INTO candidato_competencia (candidato_id, competences) VALUES (?, ?);"

        try (PreparedStatement pstmt = connection.prepareStatement(command)) {
            for (CompetenciaENUM competencia : competencias) {
                pstmt.setLong(1, candidatoID)
                pstmt.setString(2, competencia.toString())
                pstmt.addBatch()
            }
            pstmt.executeBatch()

        } catch (SQLException e) {
            SQLException nextException = e.getNextException()
            throw new RuntimeException("Erro ao relacionar competências: " + e.getMessage() +
                    (nextException != null ? " Causa adicional: " + nextException.getMessage() : ""))
        }
    }

    @Override
    void delete(Long candidatoID, List<CompetenciaENUM> competencias) {

        String command = "DELETE FROM candidato_competencia WHERE candidato_id = ? AND competences = ?;"

        try (PreparedStatement pstmt = connection.prepareStatement(command)) {

            for (CompetenciaENUM competencia : competencias) {
                pstmt.setLong(1, candidatoID)
                pstmt.setString(2, competencia.toString())
                pstmt.addBatch()
            }

            pstmt.executeBatch()

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover competências do candidato com ID " + candidatoID.toString() + e)
        }
    }

    void update(Long id, List<CompetenciaENUM> competences){
        this.deleteAllCompetences(id)
        this.create(id, competences)
    }

    void deleteAllCompetences(Long id) {
        String command = "DELETE FROM candidato_competencia WHERE candidato_id = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(command)) {
            pstmt.setLong(1, id)
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover todas as competências", e);
        }
    }

    @Override
    Candidato findById(Long candidatoID) {
        String command = SQLQuerys.RETURN_CANDIDATO_WITH_COMPETENCES.getQuery()

        try (PreparedStatement pstmt = connection.prepareStatement(command)) {
            pstmt.setLong(1, candidatoID)
            ResultSet resultSet = pstmt.executeQuery()

            if (resultSet.next()) {

                return director.constructFromResultSetWithCompetences(resultSet, builder)

            }
        } catch (SQLException e) {
            throw new RuntimeException("nao foi possivel retornar o candidato e as competences " + e)
        }
    }

    @Override
    List<Candidato> listAll() {
        String command = SQLQuerys.LIST_ALL_CANDIDATOS_JOIN_COMPETENCIAS.getQuery()

        List<Candidato> resultList = new ArrayList<>()
        try (Statement stmt = connection.createStatement()
             ResultSet resultSet = stmt.executeQuery(command)) {

            while (resultSet.next()) {
                resultList.add(
                        director.constructFromResultSetWithCompetences(resultSet, builder)
                )
            }

            return resultList

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao retornar os candidatos e suas competencias " + e.getMessage())
        }
    }



}
