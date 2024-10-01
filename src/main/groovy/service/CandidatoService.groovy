package service

import DTO.Request.CandidatoRequestDTO
import DTO.Response.CandidatoCompetenciaResponseDTO
import DTO.Response.CandidatoResponseDTO
import enums.CompetenciasENUM
import model.Candidato
import repository.CandidatoDAO
import repository.ModelsCRUD
import repository.auxiliary.AuxiliaryTablesCRUD
import repository.auxiliary.CandidatoCompetenciaDAO

import java.util.stream.Collectors

class CandidatoService {

    private ModelsCRUD<Candidato, Long> candidatoRepository = new CandidatoDAO()
    private AuxiliaryTablesCRUD<Candidato, Long> candidatoCompetenciaRepository = new CandidatoCompetenciaDAO()

    void createCandidato(CandidatoRequestDTO request) {

        long returnedID = candidatoRepository.create(new Candidato(request))

        if(request.competences().size() > 0 && request.competences()!==null){
            addCompetencesToCandidato(returnedID, request.competences())
        }
    }

    void addCompetencesToCandidato(Long candidatoID, List<CompetenciasENUM> competences){
        candidatoCompetenciaRepository.create(candidatoID, competences
                .stream()
                .map {it -> it.getId()}
                .collect(Collectors.toList()));
    }

    CandidatoResponseDTO findCandidatoById(Long id) {
        Candidato model = candidatoRepository.findById(id)

        return buildCandidatoDTO(model)
    }

    List<CandidatoResponseDTO> listAll() {
        return candidatoRepository.listAll().forEach {
            it -> buildCandidatoDTO(it)
        }
    }

    CandidatoCompetenciaResponseDTO findCandidatoAndCompetenciasById(Long id) {
        Candidato model = candidatoCompetenciaRepository.findById(id)
        return buildCandidatoWithCompetenciasDTO(buildCandidatoDTO(model), model.getCompetences())
    }

    void deleteCandidato(Long id) {
        candidatoRepository.delete(id)
    }

    void updateCandidato(CandidatoRequestDTO request, Long id) {
        candidatoRepository.update(request, id)
    }


    private static buildCandidatoDTO(Candidato model) {
        return new CandidatoResponseDTO(
                model.getId(),
                model.getFirst_name(),
                model.getLast_name(),
                model.getCPF(),
                model.getDescription(),
                model.getEmail(),
                model.getCEP(),
                model.getCity()
        )
    }

    private static buildCandidatoWithCompetenciasDTO(CandidatoResponseDTO dto, List<CompetenciasENUM> competencias) {
        return new CandidatoCompetenciaResponseDTO (
                dto,
                competencias.stream()
                    .map {it-> it.getDescription()}
                    .collect(Collectors.toList())
        )
    }
}
