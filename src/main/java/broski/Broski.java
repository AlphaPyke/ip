package broski;

import java.util.Scanner;

public class Broski {
    private final Scanner scanner = new Scanner(System.in);
    private TaskList taskList;
    private final TaskManager manager = new TaskManager();
    private final DateTimeParser dateTimeParser = new DateTimeParser();
    private final Parser parser = new Parser();
    private final Ui ui = new Ui();

    /**
     * Starts the chatbot with the initial prompts.
     */
    public void start() {
        ui.greeting();
        this.taskList = new TaskList(this.manager.loadTasks());
    }

    /**
     * The main content of the chatbot and all its possible responses.
     */
    public void chatbot() throws TodoException, DeadlineException,
            EventException, WrongInputException {
        String reply = scanner.nextLine();
        if (reply.equals("list")) {
            ui.list(taskList);
            this.chatbot();
        } else if (reply.equals("bye")) {
            ui.exit();
        } else if (reply.length() > 5 && reply.startsWith("mark")) {
            ui.mark(taskList, parser, reply);
            this.save();
            this.chatbot();
        } else if (reply.length() > 7 && reply.startsWith("unmark")) {
            ui.unmark(taskList, parser, reply);
            this.save();
            this.chatbot();
        } else if (reply.length() > 7 && reply.startsWith("delete")) {
            ui.delete(taskList, parser, reply);
            this.save();
            this.chatbot();
        } else if (reply.length() > 5 && reply.startsWith("find")) {
            ui.find(taskList, reply);
            this.chatbot();
        } else {
            ui.mainResponse(taskList, parser, reply, dateTimeParser);
            this.save();
            this.chatbot();
        }
    }

    public void save() {
        this.manager.saveTasks(this.taskList.peek());
    }

    public void run() {
        try {
            this.chatbot();
        } catch (TodoException e) {
            ui.todoException();
            this.run();
        } catch (DeadlineException e) {
            ui.deadlineException();
            this.run();
        } catch (EventException e) {
            ui.eventException();
            this.run();
        } catch (WrongInputException e) {
            ui.wrongInputException();
            this.run();
        } catch (InvalidDateTimeException e) {
            ui.invalidDateTimeException();
            this.run();
        }
    }

    public static void main(String[] args) {
        Broski bot = new Broski();
        bot.start();
        bot.run();
    }
}
