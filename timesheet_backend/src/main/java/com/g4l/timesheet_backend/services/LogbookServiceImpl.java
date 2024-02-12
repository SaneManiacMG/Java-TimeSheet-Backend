package com.g4l.timesheet_backend.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.g4l.timesheet_backend.interfaces.LogbookService;
import com.g4l.timesheet_backend.models.entities.Logbook;
import com.g4l.timesheet_backend.models.requests.LogbookHandleRequest;
import com.g4l.timesheet_backend.models.requests.LogbookSubmissionRequest;
import com.g4l.timesheet_backend.models.responses.LogbookResponse;
import com.g4l.timesheet_backend.repositories.LogbookRepository;
import com.g4l.timesheet_backend.utils.mappers.models.LogbookMapper;

@Service
public class LogbookServiceImpl implements LogbookService {
    private final LogbookRepository logbookRepository;
    private final LogbookMapper logbookMapper;

    public LogbookServiceImpl(LogbookRepository logbookRepository, LogbookMapper logbookMapper) {
        this.logbookRepository = logbookRepository;
        this.logbookMapper = logbookMapper;
    }

    @Override
    public LogbookResponse createLogbook(LogbookSubmissionRequest logbookSubmission) {
        Logbook logbook = logbookRepository.save(logbookMapper.logbookSubmissionRequestToLogbook(logbookSubmission));
        return logbookMapper.logbookToLogbookResponse(logbook);
    }

    @Override
    public LogbookResponse updateLogbook(LogbookSubmissionRequest logbookSubmission) {
        Logbook logbook = logbookRepository.save(logbookMapper.logbookSubmissionRequestToLogbook(logbookSubmission));
        return logbookMapper.logbookToLogbookResponse(logbook);
    }

    @Override
    public LogbookResponse getLogbookById(String logbookId) {
        Logbook logbook =  logbookRepository.findById(logbookId).orElse(null);
        return logbookMapper.logbookToLogbookResponse(logbook);
    }

    @Override
    public List<LogbookResponse> getAllLogbooks() {
        List<Logbook> logbooks = logbookRepository.findAll();
        List<LogbookResponse> logbookResponses = null;

        for (Logbook logbook : logbooks) {
            logbookResponses.add(logbookMapper.logbookToLogbookResponse(logbook));
        }
        return logbookResponses;
    }

    @Override
    public String deleteLogbook(String logbookId) {
        logbookRepository.deleteById(logbookId);
        return "Logbook deleted";
    }

    @Override
    public List<LogbookResponse> getLogbooksByConsultantId(String consultantId) {
        List<Logbook> logbooks = logbookRepository.findLogbookByConsultantId(consultantId);
        List<LogbookResponse> logbookResponses = null;
        for (Logbook logbook : logbooks) {
            logbookResponses.add(logbookMapper.logbookToLogbookResponse(logbook));
        }

        return logbookResponses;
    }

    @Override
    public List<LogbookResponse> getLogbooksByManagerId(String managerId) {
        List<Logbook> logbooks = logbookRepository.findLogbookByManagerId(managerId);
        List<LogbookResponse> logbookResponses = null;
        for (Logbook logbook : logbooks) {
            logbookResponses.add(logbookMapper.logbookToLogbookResponse(logbook));
        }

        return logbookResponses;
    }

    @Override
    public LogbookResponse handleLogbookSubmission(LogbookHandleRequest logbookHandleRequest) {
        Logbook logbook = logbookRepository.findById(logbookHandleRequest.logbookId).orElse(null);

        return logbookMapper.logbookToLogbookResponse(logbook);
    }
    
}
