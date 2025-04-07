package dat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.dto.SkiInstructionDTO;
import dat.enums.Level;
import dat.utils.DataAPIReader;

import java.util.List;

public class SkiInstructionService {

    private static final String BASE_URL = "https://apiprovider.cphbusinessapps.dk/skilesson/";
    private final DataAPIReader dataAPIReader = new DataAPIReader();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<SkiInstructionDTO> getInstructionsByLevel(Level level) {
        try {
            String url = BASE_URL + level.name().toLowerCase();
            String json = dataAPIReader.getDataFromClient(url);
            JsonNode root = objectMapper.readTree(json);
            JsonNode lessonsNode = root.get("lessons");
            return objectMapper.readerForListOf(SkiInstructionDTO.class).readValue(lessonsNode);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch ski instructions for level: " + level, e);
        }
    }

    public int getTotalDurationByLevel(Level level) {
        List<SkiInstructionDTO> instructions = getInstructionsByLevel(level);
        return instructions.stream().mapToInt(SkiInstructionDTO::getDurationMinutes).sum();
    }
}
