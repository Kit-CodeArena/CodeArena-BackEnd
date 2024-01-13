package com.example.codeArena.Problem.service;

import com.example.codeArena.Problem.dto.ProblemCreateDto;
import com.example.codeArena.Problem.dto.ProblemDto;
import com.example.codeArena.Problem.dto.ProblemUpdateDto;
import com.example.codeArena.Problem.model.Problem;
import com.example.codeArena.Problem.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository problemRepository;

    // 문제 생성
    public ProblemDto createProblem(ProblemCreateDto createDto) {
        Problem problem = new Problem();
        updateProblemFields(problem, createDto);
        return convertToDto(problemRepository.save(problem));
    }

    // 문제 삭제
    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }

    // 모든 문제 조회
    public List<ProblemDto> getAllProblems() {
        return problemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 특정 문제 조회
    public Optional<ProblemDto> getProblemById(Long id) {
        return problemRepository.findById(id)
                .map(this::convertToDto);
    }

    // 문제 업데이트
    public Optional<ProblemDto> updateProblem(Long id, ProblemUpdateDto updateDto) {
        return problemRepository.findById(id)
                .map(problem -> {
                    updateProblemFields(problem, updateDto);
                    return convertToDto(problemRepository.save(problem));
                });
    }

    // 제목으로 문제 필터링
    public List<ProblemDto> getProblemsByTitle(String title) {
        return problemRepository.findByTitleContaining(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 난이도로 문제 필터링
    public List<ProblemDto> getProblemsByDifficulty(String difficulty) {
        return problemRepository.findByDifficulty(difficulty).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 카테고리로 문제 필터링
    public List<ProblemDto> getProblemsByCategory(String category) {
        return problemRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 태그로 문제 필터링
    public List<ProblemDto> getProblemsByTag(String tag) {
        return problemRepository.findByTagsContaining(tag).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Problem 엔티티를 ProblemDto로 변환
    private ProblemDto convertToDto(Problem problem) {
        return new ProblemDto(problem.getId(), problem.getTitle(), problem.getDescription(),
                problem.getDifficulty(), problem.getInputFormat(),
                problem.getOutputFormat(), problem.getSampleInput(),
                problem.getSampleOutput(), problem.getTimeLimit(),
                problem.getMemoryLimit(), problem.getTotalSubmissions(),
                problem.getCorrectSubmissions(), problem.getAccuracy(),
                problem.getCategory(), problem.getTags().toArray(new String[0]));
    }

    // Update fields of Problem based on DTO
    private void updateProblemFields(Problem problem, Object dto) {
        if (dto instanceof ProblemCreateDto createDto) {
            setProblemFieldsFromDto(problem, createDto.getTitle(), createDto.getDescription(),
                    createDto.getDifficulty(), createDto.getInputFormat(),
                    createDto.getOutputFormat(), createDto.getSampleInput(),
                    createDto.getSampleOutput(), createDto.getTimeLimit(),
                    createDto.getMemoryLimit(), createDto.getCategory(),
                    new ArrayList<>(Arrays.asList(createDto.getTags())));
        } else if (dto instanceof ProblemUpdateDto updateDto) {
            setProblemFieldsFromDto(problem, updateDto.getTitle(), updateDto.getDescription(),
                    updateDto.getDifficulty(), updateDto.getInputFormat(),
                    updateDto.getOutputFormat(), updateDto.getSampleInput(),
                    updateDto.getSampleOutput(), updateDto.getTimeLimit(),
                    updateDto.getMemoryLimit(), updateDto.getCategory(),
                    new ArrayList<>(Arrays.asList(updateDto.getTags())));
        }
    }
    // Set fields of Problem from DTO data
    private void setProblemFieldsFromDto(Problem problem, String title, String description,
                                         String difficulty, String inputFormat, String outputFormat,
                                         String sampleInput, String sampleOutput, Integer timeLimit,
                                         Integer memoryLimit, String category, List<String> tags) {
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setDifficulty(difficulty);
        problem.setInputFormat(inputFormat);
        problem.setOutputFormat(outputFormat);
        problem.setSampleInput(sampleInput);
        problem.setSampleOutput(sampleOutput);
        problem.setTimeLimit(timeLimit);
        problem.setMemoryLimit(memoryLimit);
        problem.setCategory(category);
        problem.setTags(tags);
    }
}
