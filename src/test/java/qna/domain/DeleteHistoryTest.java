package qna.domain;

import java.time.LocalDateTime;

import static qna.domain.AnswerTest.ANSWER_1;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

public class DeleteHistoryTest {
    public static final DeleteHistory DELETE_HISTORY_1 = new DeleteHistory(ContentType.ANSWER, ANSWER_1.getId(), JAVAJIGI.getId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_2 = new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriterId(), LocalDateTime.now());
}
