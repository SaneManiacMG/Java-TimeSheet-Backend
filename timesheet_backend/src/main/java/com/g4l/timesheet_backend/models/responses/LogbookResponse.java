package com.g4l.timesheet_backend.models.responses;

import com.g4l.timesheet_backend.models.enums.LogbookStatus;
import com.g4l.timesheet_backend.models.requests.LogbookSubmissionRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class LogbookResponse extends LogbookSubmissionRequest {
    public LogbookStatus status;
    public String statusDescription;
    public String managerId;

    public LogbookResponse(String consultantId, int week, int monday, int tuesday, int wednesday, int thursday,
            int friday, int saturday, int sunday, int totalHours, LogbookStatus status, String statusDescription,
            String managerId) {
        super(consultantId, week, monday, tuesday, wednesday, thursday, friday, saturday, sunday, totalHours);
        this.status = status;
        this.statusDescription = statusDescription;
        this.managerId = managerId;
    }
}
