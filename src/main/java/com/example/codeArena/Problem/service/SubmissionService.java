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
import java.nio.file.Files;
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
        return submissions.stream()
                .map(submission -> convertToDto(submission, userId)) // Use lambda expression
                .collect(Collectors.toList());
    }

    public List<SubmissionDto> getSubmissionsByProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));
        List<Submission> submissions = submissionRepository.findByProblem(problem);
        return submissions.stream()
                .map(submission -> convertToDto(submission, submission.getUser().getId())) // Use lambda expression
                .collect(Collectors.toList());
    }

    public SubmissionDto createSubmission(SubmissionDto submissionDto, Long currentUserId) throws Exception {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("인증된 사용자를 찾을 수 없습니다."));
        Problem problem = problemRepository.findById(submissionDto.getProblemId())
                .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다."));

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
            ExecutionResult executionResult = runCodeInDocker(submission, inputFilePath, command, problem);

            // Check for time and memory limits
            if (executionResult.isTimeLimitExceeded() || executionResult.isMemoryLimitExceeded()) {
                allTestsPassed = false;
                submission.setResult(executionResult.isTimeLimitExceeded() ? "Time Limit Exceeded" : "Memory Limit Exceeded");
                break;
            }

            if (!executionResult.getOutput().trim().equals(testCaseOutput.trim())) {
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
        return convertToDto(savedSubmission, currentUserId);
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

    private ExecutionResult runCodeInDocker(Submission submission, String inputFilePath, String command, Problem problem) {
        ExecutionResult executionResult = new ExecutionResult();
        try {
            // 메모리 제한 설정
            String memoryLimit = problem.getMemoryLimit() + "m";

            // Docker 커맨드 수정
            String modifiedCommand;
            String args = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            switch (submission.getLanguage()) {
                case "java":
                    modifiedCommand = String.format(
                            "docker run --rm -i --memory %s -v \"%s:/home/coder\" codearena-runner /bin/bash -c \"javac /home/coder/Main.java && java -cp /home/coder Main %s\"",
                            memoryLimit, Paths.get(System.getProperty("user.dir"), "temp"), args);
                    break;
                case "python":
                    modifiedCommand = String.format("docker run --rm -i --memory %s -v \"%s:/home/coder\" codearena-runner python3 /home/coder/script.py < /home/coder/input.txt", memoryLimit, Paths.get(System.getProperty("user.dir"), "temp"));
                    break;
                case "cpp":
                    modifiedCommand = String.format(
                            "docker run --rm -i --memory %s -v \"%s:/home/coder\" codearena-runner /bin/bash -c \"g++ /home/coder/program.cpp -o /home/coder/program && /home/coder/program `cat /home/coder/input.txt`\"",
                            memoryLimit, Paths.get(System.getProperty("user.dir"), "temp"));
                    break;
                default:
                    throw new RuntimeException("Unsupported language: " + submission.getLanguage());
            }

            ProcessBuilder builder = new ProcessBuilder(modifiedCommand.split(" "));
            builder.redirectErrorStream(true);

            File inputFile = new File(inputFilePath);
            builder.redirectInput(ProcessBuilder.Redirect.from(inputFile));

            long startTime = System.currentTimeMillis();
            Process process = builder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append('\n');
                }
            }

            long endTime = System.currentTimeMillis();
            long executionTimeMillis = endTime - startTime;
            double executionTimeSeconds = executionTimeMillis / 1000.0;

            executionResult.setOutput(output.toString());
            executionResult.setExecutionTime(executionTimeMillis);

            if (executionTimeMillis > problem.getTimeLimit() * 1000) {
                executionResult.setTimeLimitExceeded(true);
            }

            int exitValue = process.waitFor();
            if (exitValue != 0) {
                // 비정상 종료 시 메모리 초과 간주
                executionResult.setMemoryLimitExceeded(true);
            }

            LOGGER.info("Total execution time (in seconds): " + executionTimeSeconds + " seconds");
            LOGGER.info("Docker Output:\n" + output);
            LOGGER.info("Memory limit for the process: " + memoryLimit);
        } catch (Exception e) {
            LOGGER.severe("Error running Docker command: " + e.getMessage());
            executionResult.setOutput("Error executing code: " + e.getMessage());
        }
        return executionResult;
    }



    private SubmissionDto convertToDto(Submission submission, Long userId) {
        return new SubmissionDto(
                submission.getId(),
                userId, // 사용자 ID 설정
                submission.getProblem().getId(),
                submission.getCode(),
                submission.getLanguage(),
                submission.getStatus(),
                submission.getResult(),
                submission.getExecutionTime()
        );
    }

}
