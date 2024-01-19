package com.example.codeArena.Problem.service;

import com.example.codeArena.User.domain.User;
import com.example.codeArena.User.repository.UserRepository;
import com.example.codeArena.Problem.domain.Problem;
import com.example.codeArena.Problem.domain.Submission;
import com.example.codeArena.Problem.domain.TestCase;
import com.example.codeArena.Problem.dto.SubmissionDto;
import com.example.codeArena.Problem.repository.ProblemRepository;
import com.example.codeArena.Problem.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SubmissionService {
    private static final Logger LOGGER = Logger.getLogger(SubmissionService.class.getName());
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public SubmissionService(UserRepository userRepository, ProblemRepository problemRepository, SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<SubmissionDto> getSubmissionsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        List<Submission> submissions = submissionRepository.findByUser(user);
        return submissions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<SubmissionDto> getSubmissionsByProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));
        List<Submission> submissions = submissionRepository.findByProblem(problem);
        return submissions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public SubmissionDto createSubmission(SubmissionDto submissionDto) throws Exception {
        User user = userRepository.findById(submissionDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Problem problem = problemRepository.findById(submissionDto.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setCode(submissionDto.getCode());
        submission.setLanguage(submissionDto.getLanguage());
        submission.setStatus(Submission.SubmissionStatus.PENDING);
        submission.setResult("");
        submission.setExecutionTime(0L);

        List<TestCase> testCases = problem.getTestCases();

        boolean allTestsPassed = true;
        for (TestCase testCase : testCases) {
            String testCaseInput = testCase.getInput();
            String testCaseOutput = testCase.getOutput();

            String inputFilePath = createFileWithContent("input.txt", testCaseInput);
            String codeFilePath = createFile(submission.getLanguage(), submission.getCode());
            String command = buildDockerCommandWithFileInput(submission.getLanguage(), inputFilePath, codeFilePath);

            LOGGER.info("Executing Docker command: " + command);
            String executionResult = runCodeInDocker(submission, inputFilePath, command);

            if (!executionResult.trim().equals(testCaseOutput.trim())) {
                allTestsPassed = false;
                break;
            }
        }

        if (allTestsPassed) {
            submission.setStatus(Submission.SubmissionStatus.ACCEPTED);
        } else {
            submission.setStatus(Submission.SubmissionStatus.REJECTED);
        }

        Submission savedSubmission = submissionRepository.save(submission);
        return convertToDto(savedSubmission);
    }

    private String createFileWithContent(String fileName, String content) {
        try {
            String filePath = Paths.get(System.getProperty("user.dir"), "temp", fileName).toString();
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }
            return filePath;
        } catch (Exception e) {
            LOGGER.severe("Error creating file: " + e.getMessage());
            return "";
        }
    }

    private String createFile(String language, String code) throws Exception {
        String fileName;
        String extension;

        switch (language) {
            case "java":
                fileName = "Main";
                extension = ".java";
                break;
            case "python":
                fileName = "script";
                extension = ".py";
                break;
            case "cpp":
                fileName = "program";
                extension = ".cpp";
                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }

        String filePath = Paths.get(System.getProperty("user.dir"), "temp", fileName + extension).toString();
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(code);
        }
        return filePath;
    }

    private String buildDockerCommandWithFileInput(String language, String inputFilePath, String codeFilePath) {
        String imageTag = "codearena-runner";
        return switch (language) {
            case "java" -> "docker run --rm -v \"" + inputFilePath + ":/home/coder/input.txt\" -v \"" + codeFilePath + ":/home/coder/Main.java\" " + imageTag + " javac Main.java && java Main < /home/coder/input.txt";
            case "python" -> "docker run --rm -v \"" + inputFilePath + ":/home/coder/input.txt\" -v \"" + codeFilePath + ":/home/coder/script.py\" " + imageTag + " python3 /home/coder/script.py < /home/coder/input.txt";
            case "cpp" -> "docker run --rm -v \"" + inputFilePath + ":/home/coder/input.txJAt\" -v \"" + codeFilePath + ":/home/coder/program.cpp\" " + imageTag + " g++ program.cpp -o program && ./program < /home/coder/input.txt";
            default -> throw new IllegalArgumentException("Unsupported language: " + language);
        };
    }

    private String runCodeInDocker(Submission submission, String inputFilePath, String command) {
        try {
            // Docker 커맨드 수정
            String modifiedCommand = "docker run --rm -i -v \"" + System.getProperty("user.dir") + "\\temp:/home/coder\" " + "codearena-runner python3 /home/coder/script.py";

            // Docker 커맨드 준비
            ProcessBuilder builder = new ProcessBuilder(modifiedCommand.split(" "));
            builder.redirectErrorStream(true);

            // 표준 입력 스트림 준비
            File inputFile = new File(inputFilePath);
            builder.redirectInput(ProcessBuilder.Redirect.from(inputFile));

            // 프로세스 시작
            Process process = builder.start();

            // Output 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                }
            }

            // Error Output 읽기
            StringBuilder errorOutput = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line).append('\n');
                }
            }

            LOGGER.info("Docker Output:\n" + output.toString());
            LOGGER.info("Docker Error:\n" + errorOutput.toString());

            process.waitFor();

            return output.toString();
        } catch (Exception e) {
            LOGGER.severe("Error running Docker command: " + e.getMessage());
            return "";
        }
    }



    private SubmissionDto convertToDto(Submission submission) {
        SubmissionDto dto = new SubmissionDto();
        dto.setId(submission.getId());
        dto.setUserId(submission.getUser().getId());
        dto.setProblemId(submission.getProblem().getId());
        dto.setCode(submission.getCode());
        dto.setLanguage(submission.getLanguage());
        dto.setStatus(submission.getStatus());
        dto.setResult(submission.getResult());
        dto.setExecutionTime(submission.getExecutionTime());
        return dto;
    }

    // 필요한 추가 메소드
}
