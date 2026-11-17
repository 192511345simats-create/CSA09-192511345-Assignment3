import java.io.IOException;

class PortalException extends Exception {
    PortalException(String message) { super(message); }
    PortalException(String message, Throwable cause) { super(message, cause); }
}
class ExamWindowClosedException extends PortalException {
    ExamWindowClosedException() { super("The examination window is closed."); }
}
class StudentNotRegisteredException extends PortalException {
    StudentNotRegisteredException() { super("Student is not registered for this course."); }
}
class AnswerSizeException extends PortalException {
    AnswerSizeException() { super("Answer file exceeds the 500 KB limit."); }
}
class PortalStorageException extends PortalException {
    PortalStorageException(String message, Throwable cause) { super(message, cause); }
}
class SubmissionService {
    void validate(boolean windowOpen, boolean registered, long sizeKb) throws PortalException {
        if (!windowOpen) throw new ExamWindowClosedException();
        if (!registered) throw new StudentNotRegisteredException();
        if (sizeKb > 500) throw new AnswerSizeException();
    }
    void store(String file) throws PortalStorageException {
        try {
            if (file.endsWith(".bad")) throw new IOException("Storage device unavailable");
        } catch (IOException e) {
            throw new PortalStorageException("Submission could not be saved.", e);
        }
    }
}
class ExamController {
    private final SubmissionService service = new SubmissionService();
    void submit(boolean open, boolean registered, long size, String file) {
        try {
            service.validate(open, registered, size);
            service.store(file);
            System.out.println("Submission accepted.");
        } catch (PortalException e) {
            System.out.println("Submission rejected: " + e.getMessage());
            if (e.getCause() != null) System.out.println("Original cause: " + e.getCause());
        }
    }
}
public class ExamPortalDemo {
    public static void main(String[] args) {
        ExamController c = new ExamController();
        c.submit(false, true, 100, "answer.pdf");
        c.submit(true, false, 100, "answer.pdf");
        c.submit(true, true, 800, "answer.pdf");
        c.submit(true, true, 100, "answer.bad");
    }
}